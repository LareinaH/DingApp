package com.admin.ac.ding.controller;

import com.admin.ac.ding.constants.Constants;
import com.admin.ac.ding.enums.MeetingBookStatus;
import com.admin.ac.ding.enums.MeetingSlot;
import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.admin.ac.ding.service.CacheService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/ding", produces = "application/json; charset=UTF-8")
public class DingController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(DingController.class);

    @Autowired
    MeetingRoomDetailMapper meetingRoomDetailMapper;

    @Autowired
    MeetingInChargeMapper meetingInChargeMapper;

    @Autowired
    MeetingMediaInChargeMapper meetingMediaInChargeMapper;

    @Autowired
    MeetingPicsMapper meetingPicsMapper;

    @Autowired
    PrivilegedDeptMapper privilegedDeptMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    DingService dingService;

    @Autowired
    MeetingBookMapper meetingBookMapper;

    @Autowired
    AdminController adminController;

    @Autowired
    CacheService cacheService;

    @Value("${ding.app.meetingbook.url}")
    String meetingBookUrl;

    @RequestMapping(value = "/meetingBookApply", method = {RequestMethod.POST})
    public RestResponse<Void> meetingBookApply(
            @RequestBody MeetingBook meetingBook
    ) {
        // 检查基础参数

        // 检查指定会议室是否已经被占用

        // 检查指定会议室是否被其它人申请中
        // 插入记录
        meetingBook.setBookStatus(MeetingBookStatus.WAIT_APPROVE.name());
        meetingBookMapper.insert(meetingBook);

        // 通知相关人员
        Example example4 = new Example(MeetingInCharge.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andEqualTo("meetingRoomId", meetingBook.getMeetingRoomId());
        List<MeetingInCharge> meetingInChargeList = meetingInChargeMapper.selectByExample(example4);

        Example example3 = new Example(MeetingMediaInCharge.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("isDeleted", false);
        criteria3.andEqualTo("meetingRoomId", meetingBook.getMeetingRoomId());
        List<MeetingMediaInCharge> meetingMediaInChargeList = meetingMediaInChargeMapper.selectByExample(example3);

        RestResponse<List<OapiUserGetWithDeptResponse>> resp = adminController.getSystemRole(SystemRoleType.MEETING_BOOK_REVIEW.name());
        Set<String> bookReviewers = resp.getData().stream()
                .map(x -> x.getUserid()).collect(Collectors.toSet());

        Set<String> notificationUsers = new TreeSet<>();
        notificationUsers.addAll(meetingInChargeList.stream().map(x -> x.getUserId()).collect(Collectors.toList()));
        notificationUsers.addAll(meetingMediaInChargeList.stream().map(x -> x.getUserId()).collect(Collectors.toList()));

        try {
            dingService.sendNotificationToUser(
                    new ArrayList<>(notificationUsers),
                    "会议室预约申请通知",
                    "有新的会议室预约申请，请前往查看详情",
                    meetingBookUrl
            );

            dingService.sendNotificationToUser(
                    new ArrayList<>(bookReviewers),
                    "会议室预约审批通知",
                    "你有一个新的会议室预约申请需要处理，请前往查看详情",
                    meetingBookUrl
            );
        } catch (Exception e) {
            logger.error("send notification failed", e);
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/queryMeetingBookPageList", method = {RequestMethod.GET})
    public RestResponse<PageInfo<MeetingBookVO>> queryMeetingBookPageList(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "bookStatus", required = false) String bookStatus,
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize
    ) {
        Example example4 = new Example(MeetingBook.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        if (StringUtils.isNotBlank(userId)) {
            RestResponse<JSONObject> resp = adminController.getUserRole(userId);
            if (!resp.getSuccessed()) {
                return  RestResponse.getFailedResponse(Constants.RcError, resp.getMessage());
            }

            JSONObject jsonObject = resp.getData();
            JSONArray jsonArray = jsonObject.getJSONArray("sysRole");
            Long isMeetingBookReview = jsonArray.stream().filter(x -> SystemRoleType.MEETING_BOOK_REVIEW.name().equalsIgnoreCase((String)x)).count();
            if (isMeetingBookReview > 0) {

            } else {
                JSONArray meetingInChargArray = jsonObject.getJSONArray("meeingInCharge");
                JSONArray meeingMediaInChargeArray = jsonObject.getJSONArray("meeingMediaInCharge");
                Set<String> inChargeSet = new TreeSet<>();
                inChargeSet.addAll(meetingInChargArray.toJavaList(String.class));
                inChargeSet.addAll(meeingMediaInChargeArray.toJavaList(String.class));
                if (inChargeSet.size() > 0) {
                    criteria4.andIn("meetingRoomId", inChargeSet);
                } else {
                    criteria4.andEqualTo("bookUserId", userId);
                }
            }
        }

        if (StringUtils.isNotBlank(bookStatus)) {
            criteria4.andIn("bookStatus", Arrays.asList(
                    bookStatus.split(",", -1)
            ));
        }

        criteria4.andGreaterThanOrEqualTo("bookDay", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("gmt_create DESC");
        List<MeetingBook> meetingBookList = meetingBookMapper.selectByExample(example4);

        // 获取所有会议室的列表
        Example example = new Example(MeetingRoomDetail.class);
        List<MeetingRoomDetail> meetingRoomDetailList = meetingRoomDetailMapper.selectByExample(example);
        Map<Long, MeetingRoomDetail> meetingRoomDetailMap = meetingRoomDetailList.stream()
                .collect(Collectors.toMap(MeetingRoomDetail::getId, Function.identity()));

        return RestResponse.getSuccesseResponse(
                new PageInfo<>(
                        meetingBookList.stream().map(x -> {
                            MeetingBookVO meetingBookVO = new MeetingBookVO();
                            BeanUtils.copyProperties(x, meetingBookVO);
                            if (meetingRoomDetailMap.containsKey(meetingBookVO.getMeetingRoomId())) {
                                meetingBookVO.setMeetingRoomDetail(meetingRoomDetailMap.get(meetingBookVO.getMeetingRoomId()));
                            }
                            return meetingBookVO;
                        }).collect(Collectors.toList())
                )
        );
    }

    @RequestMapping(value = "/queryMeetingBookById", method = {RequestMethod.GET})
    public RestResponse<MeetingBookVO> queryMeetingBookPageList(
            Long id
    ) throws ExecutionException, ApiException, DingServiceException {
        Example example4 = new Example(MeetingBook.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andEqualTo("id", id);

        List<MeetingBook> meetingBookList = meetingBookMapper.selectByExample(example4);
        if (CollectionUtils.isEmpty(meetingBookList)) {
            return RestResponse.getFailedResponse(Constants.RcError, "该会议预约记录不存在");
        }

        MeetingBook meetingBook = meetingBookList.get(0);
        MeetingBookVO meetingBookVO = new MeetingBookVO();
        BeanUtils.copyProperties(meetingBook, meetingBookVO);

        // 查找会议室详情
        Example example = new Example(MeetingRoomDetail.class);
        List<MeetingRoomDetail> meetingRoomDetailList = meetingRoomDetailMapper.selectByExample(example);
        Map<Long, MeetingRoomDetail> meetingRoomDetailMap = meetingRoomDetailList.stream()
                .collect(Collectors.toMap(MeetingRoomDetail::getId, Function.identity()));
        if (meetingRoomDetailMap.containsKey(meetingBook.getMeetingRoomId())) {
            meetingBookVO.setMeetingRoomDetail(meetingRoomDetailMap.get(meetingBook.getMeetingRoomId()));
        }

        // 查询申请人信息
        OapiUserGetWithDeptResponse oapiUserGetResponse = cacheService.getUserDetail(meetingBook.getBookUserId());
        meetingBookVO.setBookUserDetail(oapiUserGetResponse);

        return RestResponse.getSuccesseResponse(meetingBookVO);
    }

    @RequestMapping(value = "/processMeetingBook", method = {RequestMethod.POST})
    public RestResponse<Void> processMeetingBook(
            String userId,
            Long meetingBookId,
            String oper,
            String comment
    ) {
        Example example4 = new Example(MeetingBook.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andEqualTo("id", meetingBookId);

        List<MeetingBook> meetingBookList = meetingBookMapper.selectByExample(example4);
        if (CollectionUtils.isEmpty(meetingBookList)) {
            return RestResponse.getFailedResponse(Constants.RcError, "该会议预约记录不存在");
        }

        MeetingBook meetingBook = meetingBookList.get(0);
        MeetingBookStatus meetingBookStatus = MeetingBookStatus.valueOf(oper);
        MeetingBookStatus oldStatus = MeetingBookStatus.valueOf(meetingBook.getBookStatus());
        if (MeetingBookStatus.AGREE.equals(meetingBookStatus)
                || MeetingBookStatus.DENY.equals(meetingBookStatus)) {
            if (!MeetingBookStatus.WAIT_APPROVE.equals(oldStatus)) {
                return RestResponse.getFailedResponse(Constants.RcError, "啊噢,这个会议室预约已经被处理了");
            }
        }

        if (MeetingBookStatus.ADMIN_CANCEL.equals(meetingBookStatus)) {
            if (!MeetingBookStatus.AGREE.equals(oldStatus)) {
                return RestResponse.getFailedResponse(Constants.RcError, "啊噢,这个会议室预约还没有被通过,不能进行系统取消操作");
            }
        }

        if (MeetingBookStatus.USER_CANCEL.equals(meetingBookStatus)) {
            if (!MeetingBookStatus.WAIT_APPROVE.equals(oldStatus)) {
                return RestResponse.getFailedResponse(Constants.RcError, "啊噢,这个会议室预约不是待审核状态,不能进行用户取消操作");
            }
        }

        meetingBook.setBookStatus(meetingBookStatus.name());
        if (MeetingBookStatus.DENY.equals(meetingBookStatus)) {
            meetingBook.setDenyComment(comment);
        } else if (MeetingBookStatus.ADMIN_CANCEL.equals(meetingBookStatus)) {
            meetingBook.setCancelComment(comment);
        } else if (MeetingBookStatus.AGREE.equals(meetingBookStatus)) {
            meetingBook.setAgreeComment(comment);
        }

        meetingBookMapper.updateByPrimaryKey(meetingBook);

        logger.info("user {} process meeting book apply {} with oper {}", userId, meetingBookId, oper);

        try {

            MeetingRoomDetail meetingRoomDetail = meetingRoomDetailMapper.selectByPrimaryKey(meetingBook.getMeetingRoomId());
            if (meetingRoomDetail != null) {
                dingService.sendNotificationToUser(
                        Arrays.asList(meetingBook.getBookUserId()),
                        "会议室预约结果通知",
                        String.format(
                                "你预约的%s%s的会议室:%s,申请结果为:%s,请前往查看详情",
                                DateFormatUtils.format(
                                        meetingBook.getBookDay(),
                                        "yyyy-MM-dd"
                                ),
                                MeetingSlot.valueOf(meetingBook.getBookTime()).getDisplayName(),
                                meetingRoomDetail.getName(),
                                meetingBookStatus.getDisplayName()
                                ),
                        meetingBookUrl
                );
            } else {
                logger.error("meeting room {} not exist", meetingBook.getMeetingRoomId());
            }

            Example example1 = new Example(MeetingInCharge.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("isDeleted", false);
            criteria1.andEqualTo("meetingRoomId", meetingBook.getMeetingRoomId());
            List<MeetingInCharge> meetingInChargeList = meetingInChargeMapper.selectByExample(example1);

            Example example3 = new Example(MeetingMediaInCharge.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("isDeleted", false);
            criteria3.andEqualTo("meetingRoomId", meetingBook.getMeetingRoomId());
            List<MeetingMediaInCharge> meetingMediaInChargeList = meetingMediaInChargeMapper.selectByExample(example3);

            Set<String> notificationUsers = new TreeSet<>();
            notificationUsers.addAll(meetingInChargeList.stream().map(x -> x.getUserId()).collect(Collectors.toList()));
            notificationUsers.addAll(meetingMediaInChargeList.stream().map(x -> x.getUserId()).collect(Collectors.toList()));

            dingService.sendNotificationToUser(
                    Arrays.asList(meetingBook.getBookUserId()),
                    "会议室预约操作通知",
                    String.format(
                            "%s%s的会议室:%s,预约结果为:%s,点击可查看详情列表",
                            DateFormatUtils.format(
                                    meetingBook.getBookDay(),
                                    "yyyy-MM-dd"
                            ),
                            MeetingSlot.valueOf(meetingBook.getBookTime()).getDisplayName(),
                            meetingRoomDetail.getName(),
                            meetingBookStatus.getDisplayName()
                    ),
                    meetingBookUrl
            );
        } catch (Exception e) {
            logger.error("send notification failed", e);
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/queryMeetingRoomBookDetailByDate", method = {RequestMethod.GET})
    public RestResponse<List<MeetingRoomDetailVO>> queryMeetingRoomBookDetail(
            String date
    ) {
        RestResponse<List<MeetingRoomDetailVO>> resp = adminController.getMeetingRoomList();
        if (!resp.getSuccessed()) {
            return resp;
        }

        List<MeetingRoomDetailVO> meetingRoomDetailVOList = resp.getData();

        // 查询一下今天的所有预约记录且审批通过的
        Example example4 = new Example(MeetingBook.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andGreaterThanOrEqualTo("bookDay", date);
        criteria4.andIn("bookStatus", Arrays.asList(MeetingBookStatus.AGREE.name(), MeetingBookStatus.WAIT_APPROVE.name()));

        List<MeetingBook> meetingBookList = meetingBookMapper.selectByExample(example4);

        for (MeetingRoomDetailVO meetingRoomDetailVO : meetingRoomDetailVOList) {
            for (MeetingBook meetingBook : meetingBookList) {
                if (meetingBook.getMeetingRoomId().equals(meetingRoomDetailVO.getId())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ordered", true);
                    jsonObject.put("bookDetail", meetingBook);
                    meetingRoomDetailVO.getOrdered().put(meetingBook.getBookTime(), jsonObject);
                }
            }
        }

        return RestResponse.getSuccesseResponse(meetingRoomDetailVOList);
    }
}
