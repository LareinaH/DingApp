package com.admin.ac.ding.controller;

import com.admin.ac.ding.constants.Constants;
import com.admin.ac.ding.enums.*;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.admin.ac.ding.service.CacheService;
import com.admin.ac.ding.utils.MyDateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    RepairApplyMapper repairApplyMapper;

    @Autowired
    RepairTypeMapper repairTypeMapper;

    @Autowired
    RepairSubTypeMapper repairSubTypeMapper;

    @Autowired
    RepairGroupMapper repairGroupMapper;

    @Autowired
    RepairManGroupMapper repairManGroupMapper;

    @Autowired
    SuggestManageMapper suggestManageMapper;

    @Value("${ding.app.meetingbook.url}")
    String meetingBookUrl;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;

    @Value("${ding.app.repair.url")
    String repairListUrl;

    @Value("${ding.app.repair.agentid}")
    Long repairAppAgentId;

    @Value("${ding.app.suggest.agentid}")
    Long suggestAppAgentId;

    @RequestMapping(value = "/meetingBookApply", method = {RequestMethod.POST})
    public RestResponse<Void> meetingBookApply(
            @RequestBody MeetingBook meetingBook
    ) {
        // 检查基础参数

        // 检查指定会议室是否已经被占用

        // 检查指定会议室是否被其它人申请中
        // 插入记录
        meetingBook.setBookStatus(MeetingBookStatus.WAIT_APPROVE.name());
        if (meetingBook.getPreArrange() == null) {
            meetingBook.setPreArrange(Byte.valueOf("0"));
        }

        meetingBook.setConfirmRemind(Byte.valueOf("0"));
        meetingBook.setPreArrangeRemind(Byte.valueOf("0"));
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
                    meetingBookAppAgentId,
                    new ArrayList<>(notificationUsers),
                    "会议室预约申请通知",
                    String.format("有新的会议室预约申请(申请单号为%d)，请前往查看详情", meetingBook.getId()),
                    dingService.getNotificationUrl("BOARD_ROOM", "STAFF")
            );

            dingService.sendNotificationToUser(
                    meetingBookAppAgentId,
                    new ArrayList<>(bookReviewers),
                    "会议室预约审批通知",
                    String.format("你有一个新的会议室预约申请需要处理(申请单号为%d)，请前往查看详情", meetingBook.getId()),
                    dingService.getNotificationUrl("BOARD_ROOM", "STAFF")
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

                            try {
                                OapiUserGetWithDeptResponse oapiUserGetResponse = cacheService.getUserDetail(x.getBookUserId());
                                meetingBookVO.setBookUserDetail(oapiUserGetResponse);
                            } catch (Exception e) {
                                logger.error("getUserDetail failed", e);
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
                return RestResponse.getFailedResponse(Constants.RcError, "啊噢,这个会议室预约审批已经被处理了");
            }
        }

        if (MeetingBookStatus.ADMIN_CANCEL.equals(meetingBookStatus)) {
            if (!MeetingBookStatus.AGREE.equals(oldStatus)) {
                return RestResponse.getFailedResponse(Constants.RcError, "啊噢,这个会议室预约还没有被通过,不能进行系统取消操作");
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
                        meetingBookAppAgentId,
                        Arrays.asList(meetingBook.getBookUserId()),
                        "会议室预约结果通知",
                        String.format(
                                "你预约的%s%s的会议室:%s(申请单号为%d),申请结果为:%s,请前往查看详情",
                                DateFormatUtils.format(
                                        meetingBook.getBookDay(),
                                        "yyyy-MM-dd"
                                ),
                                MeetingSlot.valueOf(meetingBook.getBookTime()).getDisplayName(),
                                meetingRoomDetail.getName(),
                                meetingBook.getId(),
                                meetingBookStatus.getDisplayName()
                                ),
                        dingService.getNotificationUrl("BOARD_ROOM", "USER")
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
                    meetingBookAppAgentId,
                    new ArrayList<>(notificationUsers),
                    "会议室预约操作通知",
                    String.format(
                            "%s%s的会议室:%s(申请单号为%d),预约结果为:%s,点击可查看详情列表",
                            DateFormatUtils.format(
                                    meetingBook.getBookDay(),
                                    "yyyy-MM-dd"
                            ),
                            MeetingSlot.valueOf(meetingBook.getBookTime()).getDisplayName(),
                            meetingRoomDetail.getName(),
                            meetingBook.getId(),
                            meetingBookStatus.getDisplayName()
                    ),
                    dingService.getNotificationUrl("BOARD_ROOM", "STAFF")
            );
        } catch (Exception e) {
            logger.error("send notification failed", e);
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/queryMeetingRoomBookDetailByDateRange", method = {RequestMethod.GET})
    public RestResponse<JSONObject> queryMeetingRoomBookDetailByDateRange(
            String gmtStart,
            String gmtEnd
    ) {
        Example example4 = new Example(MeetingBook.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andBetween("bookDay", gmtStart + " 00:00:00", gmtEnd + " 23:59:59");
        criteria4.andEqualTo("bookStatus", MeetingBookStatus.AGREE.name());

        List<MeetingBook> meetingBookList = meetingBookMapper.selectByExample(example4);

        final Map<Long, MeetingRoomDetailVO> meetingRoomDetailMap;
        RestResponse<List<MeetingRoomDetailVO>> resp = adminController.getMeetingRoomList();
        if (resp.getSuccessed()) {
            meetingRoomDetailMap = resp.getData().stream()
                    .collect(Collectors.toMap(MeetingRoomDetailVO::getId, Function.identity()));
        } else {
            return RestResponse.getFailedResponse(Constants.RcError, "未能查询到会议室详情列表");
        }

        Map<Date, List<MeetingBook>> dateMap =
                meetingBookList.stream().collect(Collectors.groupingBy(MeetingBook::getBookDay));

        JSONObject resultJo = new JSONObject();
        dateMap.entrySet().stream().forEach(x -> {
            String date = DateFormatUtils.format(x.getKey(), "yyyy-MM-dd");

            resultJo.put(
                    date,
                    x.getValue().stream().map(y -> {
                        MeetingBookVO meetingBookVO = new MeetingBookVO();
                        BeanUtils.copyProperties(y, meetingBookVO);
                        if (meetingRoomDetailMap.containsKey(meetingBookVO.getMeetingRoomId())) {
                            meetingBookVO.setMeetingRoomDetail(meetingRoomDetailMap.get(meetingBookVO.getMeetingRoomId()));
                        }

                        try {
                            OapiUserGetWithDeptResponse oapiUserGetResponse = cacheService.getUserDetail(y.getBookUserId());
                            meetingBookVO.setBookUserDetail(oapiUserGetResponse);
                        } catch (Exception e) {
                            logger.error("getUserDetail failed", e);
                        }

                        return meetingBookVO;
                    }).collect(Collectors.toList())
            );
        });

        return RestResponse.getSuccesseResponse(resultJo);
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
        criteria4.andEqualTo("bookDay", date);
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

    @RequestMapping(value = "/repairApply", method = {RequestMethod.POST})
    public RestResponse<Void> repairApply(
            @RequestBody RepairApply repairApply
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        repairApply.setRepairStatus(RepairStatus.WAIT_CONFIRM.name());
        repairApply.setRemindDispatch(Byte.valueOf("0"));
        repairApplyMapper.insert(repairApply);

        // 通知维修组长确认
        RepairGroup repairGroup = new RepairGroup();
        repairGroup.setRepairType(repairApply.getRepairType());
        List<RepairGroup> repairGroupList = repairGroupMapper.select(repairGroup);
        dingService.sendNotificationToUser(
                repairAppAgentId,
                repairGroupList.stream().map(x -> x.getSupervisorUserId()).collect(Collectors.toList()),
                "维修确认通知",
                String.format(
                        "有新的维修工单(申请单号为%d),请前往处理",
                        repairApply.getId()
                ),
                dingService.getNotificationUrl("FIX", "STAFF")
        );

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/dispatchRepairOrder", method = {RequestMethod.POST})
    public RestResponse<Void> dispatchRepairOrder(
            String dispatcherUserId,
            Long repairApplyId,
            Long repairManId,
            String repairDuration
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        RepairApply repairApply = repairApplyMapper.selectByPrimaryKey(repairApplyId);
        if (repairApply == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到维修单:" + repairApplyId);
        }

        if (!RepairStatus.WAIT_CONFIRM.name().equals(repairApply.getRepairStatus())) {
            return RestResponse.getFailedResponse(Constants.RcError, String.format("维修单当前状态为:%s,无法被派遣", RepairStatus.valueOf(repairApply.getRepairStatus()).getDisplayName()));
        }

        RepairManGroup repairManGroup = repairManGroupMapper.selectByPrimaryKey(repairManId);
        if (repairManGroup == null) {
            return RestResponse.getFailedResponse(Constants.RcError, String.format("指定的维修人员(%d)不存在", repairManId));
        }

        repairApply.setDispatcherUserId(dispatcherUserId);
        repairApply.setRepairProcUserId(repairManId);
        repairApply.setRepairDuration(repairDuration);
        repairApply.setGmtConfirm(new Date());
        repairApply.setRepairStatus(RepairStatus.WAIT_REPAIR.name());
        repairApplyMapper.updateByPrimaryKey(repairApply);

        RepairSrcType repairSrcType = RepairSrcType.valueOf(repairApply.getSrcType());
        // 通知提交人维修完成
        if (repairSrcType.equals(RepairSrcType.USER)) {
            dingService.sendNotificationToUser(
                    repairAppAgentId,
                    Arrays.asList(repairApply.getSubmitUserId()),
                    "维修派遣通知",
                    String.format(
                            "您提交的维修工单(申请单号为%d)已安排维修人员%s前往处理,请耐心等候",
                            repairApply.getId(),
                            repairManGroup.getName()
                    ),
                    dingService.getNotificationUrl("FIX", "USER")
            );
        } else if (repairSrcType.equals(RepairSrcType.SERVICE)) {
            dingService.sendNotificationToUser(
                    repairAppAgentId,
                    Arrays.asList(repairApply.getSubmitUserId()),
                    "维修派遣通知",
                    String.format(
                            "您提交的维修工单(申请单号为%d)已安排维修人员%s前往处理,请及时通知提交人%s(%s)维修进展",
                            repairApply.getId(),
                            repairApply.getRealUserName(),
                            repairApply.getRealUserPhone()
                    ),
                    dingService.getNotificationUrl("FIX", "STAFF")
            );
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/completeRepairOrder", method = {RequestMethod.POST})
    public RestResponse<Void> completeRepairOrder(
            String completeUserId,
            Long repairApplyId
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        RepairApply repairApply = repairApplyMapper.selectByPrimaryKey(repairApplyId);
        if (repairApply == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到维修单:" + repairApplyId);
        }

        if (!RepairStatus.WAIT_REPAIR.name().equals(repairApply.getRepairStatus())) {
            return RestResponse.getFailedResponse(Constants.RcError, String.format("维修单当前状态为:%s,无法操作完成", RepairStatus.valueOf(repairApply.getRepairStatus()).getDisplayName()));
        }

        repairApply.setCompleteUserId(completeUserId);
        repairApply.setRepairStatus(RepairStatus.REPAIR_COMPLETE.name());
        repairApply.setGmtRepairComplete(new Date());
        repairApplyMapper.updateByPrimaryKey(repairApply);

        RepairSrcType repairSrcType = RepairSrcType.valueOf(repairApply.getSrcType());
        // 通知提交人维修完成
        if (repairSrcType.equals(RepairSrcType.USER)) {
            dingService.sendNotificationToUser(
                    repairAppAgentId,
                    Arrays.asList(repairApply.getSubmitUserId()),
                    "维修完成通知",
                    String.format(
                            "您提交的维修工单(申请单号为%d)已维修完毕,请对本次服务提出评价",
                            repairApply.getId()
                    ),
                    dingService.getNotificationUrl("FIX", "USER")
            );
        } else if (repairSrcType.equals(RepairSrcType.SERVICE)) {
            dingService.sendNotificationToUser(
                    repairAppAgentId,
                    Arrays.asList(repairApply.getSubmitUserId()),
                    "维修完成通知",
                    String.format(
                            "您提交的维修工单(申请单号为%d)已维修完毕,请及时通知提交人%s(%s)维修进展",
                            repairApply.getId(),
                            repairApply.getRealUserName(),
                            repairApply.getRealUserPhone()
                    ),
                    dingService.getNotificationUrl("FIX", "STAFF")
            );
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/scoreRepairOrder", method = {RequestMethod.POST})
    public RestResponse<Void> scoreRepairOrder(
            Long repairApplyId,
            Integer score,
            String scoreComment
    ) {
        RepairApply repairApply = repairApplyMapper.selectByPrimaryKey(repairApplyId);
        if (repairApply == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到维修单:" + repairApplyId);
        }

        if (!RepairStatus.REPAIR_COMPLETE.name().equals(repairApply.getRepairStatus())) {
            return RestResponse.getFailedResponse(Constants.RcError, String.format("维修单当前状态为:%s,无法评价", RepairStatus.valueOf(repairApply.getRepairStatus()).getDisplayName()));
        }

        if (score < 1 || score > 5) {
            return RestResponse.getFailedResponse(Constants.RcError, "评分的范围是1-5");
        }

        repairApply.setScore(score);
        repairApply.setScoreComment(scoreComment);
        repairApply.setRepairStatus(RepairStatus.ORDER_COMPLETE.name());
        repairApply.setGmtScore(new Date());
        repairApplyMapper.updateByPrimaryKey(repairApply);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/getRepairTypeTree", method = {RequestMethod.GET})
    public RestResponse<List<RepairTypeTreeVO>> getRepairTypeTree() {
        RepairType p1 = new RepairType();
        List<RepairType> repairTypeList = repairTypeMapper.select(p1);

        return RestResponse.getSuccesseResponse(
                repairTypeList.stream().map(x -> {
                    RepairTypeTreeVO repairTypeTreeVO = new RepairTypeTreeVO();
                    repairTypeTreeVO.setValue(x.getId());
                    repairTypeTreeVO.setText(x.getRepairType());
                    // 查找子类型
                    RepairSubType p2 = new RepairSubType();
                    p2.setRepairTypeId(x.getId());
                    List<RepairSubType> repairSubTypeList = repairSubTypeMapper.select(p2);
                    repairTypeTreeVO.setChildren(
                            repairSubTypeList.stream().map(y -> {
                                RepairSubTypeVO repairSubTypeVO = new RepairSubTypeVO();
                                repairSubTypeVO.setValue(y.getId());
                                repairSubTypeVO.setText(y.getRepairSubType());
                                return repairSubTypeVO;
                            }).collect(Collectors.toList())
                    );
                    // 查找维修组长
                    RepairGroup p3 = new RepairGroup();
                    p3.setRepairType(x.getId());
                    List<RepairGroup> repairGroupList = repairGroupMapper.select(p3);
                    repairTypeTreeVO.setSupervisorList(
                            repairGroupList.stream().map(z -> {
                                try {
                                    return cacheService.getUserDetail(z.getSupervisorUserId());
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (ApiException e) {
                                    e.printStackTrace();
                                } catch (DingServiceException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }).filter(zz -> zz != null).collect(Collectors.toList())
                    );

                    // 查找维修工列表
                    RepairManGroup p4 = new RepairManGroup();
                    p4.setRepairType(x.getId());
                    List<RepairManGroup> repairManGroupList = repairManGroupMapper.select(p4);
                    repairTypeTreeVO.setRepairManGroupList(repairManGroupList);
                    return repairTypeTreeVO;
                }).collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/getRepairApplyDetail", method = {RequestMethod.GET})
    public RestResponse<RepairApplyDetailVO> getRepairApplyDetail(
            Long repairApplyId
    ) throws ExecutionException, ApiException, DingServiceException {
        RepairApply repairApply = repairApplyMapper.selectByPrimaryKey(repairApplyId);
        if (repairApply == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到维修单:" + repairApplyId);
        }

        RepairManGroup repairManGroup = repairManGroupMapper.selectByPrimaryKey(repairApply.getRepairProcUserId());

        RepairApplyDetailVO repairApplyDetailVO = new RepairApplyDetailVO();
        BeanUtils.copyProperties(repairApply, repairApplyDetailVO);

        repairApplyDetailVO.setSubmitUserDetail(cacheService.getUserDetail(repairApply.getSubmitUserId()));
        repairApplyDetailVO.setRepairManGroup(repairManGroup);
        repairApplyDetailVO.setRepairTypeDetail(repairTypeMapper.selectByPrimaryKey(repairApply.getRepairType()));
        repairApplyDetailVO.setRepairSubTypeDetail(repairSubTypeMapper.selectByPrimaryKey(repairApply.getRepairSubType()));

        return RestResponse.getSuccesseResponse(repairApplyDetailVO);
    }

    @RequestMapping(value = "/queryRepairApplyDetails", method = {RequestMethod.GET})
    public RestResponse<List<RepairApplyDetailVO>> queryRepairApplyDetails(
            String submitUserId,
            Long repairTypeId,
            Long repairSubTypeId,
            String srcType,
            String repairStatus,
            Long repairManId,
            String scoreList,
            String gmtStart,
            String gmtEnd
    ) throws ExecutionException, ApiException, DingServiceException, ParseException {
        RepairApply param = new RepairApply();
        if (StringUtils.isNotBlank(submitUserId)) {
            param.setSubmitUserId(submitUserId);
        }
        if (repairTypeId != null) {
            param.setRepairType(repairTypeId);
        }
        if (repairSubTypeId != null) {
            param.setRepairSubType(repairSubTypeId);
        }
        if (StringUtils.isNotBlank(srcType)) {
            param.setSrcType(srcType);
        }
        if (repairManId != null) {
            param.setRepairProcUserId(repairManId);
        }
        List<RepairApply> repairApplyList = repairApplyMapper.select(param);

        // 支持多状态筛选
        if (StringUtils.isNotBlank(repairStatus)) {
            Set<String> repairStatusSet = Stream.of(repairStatus.split(",", -1)).collect(Collectors.toSet());
            repairApplyList = repairApplyList.stream().filter(x -> repairStatusSet.contains(x.getRepairStatus())).collect(Collectors.toList());
        }

        // 支持多评分筛选
        if (StringUtils.isNotBlank(scoreList)) {
            Set<Integer> scoreSet = Stream.of(scoreList.split(",", -1)).map(x -> Integer.valueOf(x)).collect(Collectors.toSet());
            repairApplyList = repairApplyList.stream().filter(x -> {
                if (scoreSet.contains(Integer.valueOf(5))) {
                    // 未评价的也算好评
                    return x.getScore() == null || scoreSet.contains(x.getScore());
                } else {
                    return scoreSet.contains(x.getScore());
                }
            }).collect(Collectors.toList());
        }

        if (StringUtils.isNotBlank(gmtStart)) {
            Date d = DateUtils.parseDate(gmtStart, "yyyy-MM-dd");
            repairApplyList = repairApplyList.stream().filter(x -> x.getGmtCreate().after(d)).collect(Collectors.toList());
        }

        if (StringUtils.isNotBlank(gmtEnd)) {
            Date d = DateUtils.parseDate(gmtEnd + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            repairApplyList = repairApplyList.stream().filter(x -> x.getGmtCreate().before(d)).collect(Collectors.toList());
        }

        RepairManGroup repairManGroup = new RepairManGroup();
        List<RepairManGroup> repairManGroupList = repairManGroupMapper.select(repairManGroup);
        Map<Long, RepairManGroup> repairManGroupMap = repairManGroupList.stream().collect(Collectors.toMap(RepairManGroup::getId, Function.identity()));

        Map<Long, RepairType> repairTypeMap = repairTypeMapper.select(new RepairType()).stream()
                .collect(Collectors.toMap(RepairType::getId, Function.identity()));

        Map<Long, RepairSubType> repairSubTypeMap = repairSubTypeMapper.select(new RepairSubType()).stream()
                .collect(Collectors.toMap(RepairSubType::getId, Function.identity()));

        return RestResponse.getSuccesseResponse(
                repairApplyList.stream().map(x -> {
                    RepairApplyDetailVO repairApplyDetailVO = new RepairApplyDetailVO();
                    BeanUtils.copyProperties(x, repairApplyDetailVO);

                    try {
                        repairApplyDetailVO.setSubmitUserDetail(cacheService.getUserDetail(x.getSubmitUserId()));
                        if (x.getRepairProcUserId() != null && repairManGroupMap.containsKey(x.getRepairProcUserId())) {
                            repairApplyDetailVO.setRepairManGroup(repairManGroupMap.get(x.getRepairProcUserId()));
                        }

                        if (repairTypeMap.containsKey(x.getRepairType())) {
                            repairApplyDetailVO.setRepairTypeDetail(repairTypeMap.get(x.getRepairType()));
                        }

                        if (repairSubTypeMap.containsKey(x.getRepairSubType())) {
                            repairApplyDetailVO.setRepairSubTypeDetail(repairSubTypeMap.get(x.getRepairSubType()));
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    } catch (DingServiceException e) {
                        e.printStackTrace();
                    }

                    return repairApplyDetailVO;
                }).collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/queryRepairTypeStats", method = {RequestMethod.GET})
    public RestResponse<List<JSONObject>> queryRepairTypeStats(
            Long repairTypeId
    ) {
        RepairApply p = new RepairApply();
        p.setRepairType(repairTypeId);
        List<RepairApply> repairApplyList = repairApplyMapper.select(p);

        // 取维修完成和评价完成的工单
        Map<Long, List<RepairApply>> repairApplyMap = repairApplyList.stream()
                .filter(x -> RepairStatus.REPAIR_COMPLETE.name().equals(x.getRepairStatus()) || RepairStatus.ORDER_COMPLETE.name().equals(x.getRepairStatus()))
                .collect(Collectors.groupingBy(RepairApply::getRepairProcUserId));

        // 查一下所有维修人员
        RepairManGroup repairManGroup = new RepairManGroup();
        List<RepairManGroup> repairManGroupList = repairManGroupMapper.select(repairManGroup);
        Map<Long, RepairManGroup> repairManGroupMap = repairManGroupList.stream().collect(Collectors.toMap(RepairManGroup::getId, Function.identity()));

        List<JSONObject> result = new ArrayList<>();
        for (Map.Entry<Long, List<RepairApply>> longListEntry : repairApplyMap.entrySet()) {
            Long repairManId = longListEntry.getKey();
            List<RepairApply> repairListForMan = longListEntry.getValue();
            JSONObject jsonObject = new JSONObject();
            if (repairManGroupMap.containsKey(repairManId)) {
                RepairManGroup rmg = repairManGroupMap.get(repairManId);
                jsonObject.put("repairManId", repairManId);
                jsonObject.put("name", rmg.getName());
                jsonObject.put("phone", rmg.getPhone());

                // 统计历史总数
                long total = repairListForMan.size();
                long goodTotal = repairListForMan.stream().filter(x -> x.getScore() >= 5 || x.getScore() == null).count();
                jsonObject.put("total", total);
                jsonObject.put("goodTotal", goodTotal);
                if (total <= 0) {
                    jsonObject.put("goodTotalRate", "0.0%");
                } else {
                    jsonObject.put("goodTotalRate", String.format("%.1f%%", (double)goodTotal / total * 100));
                }

                // 统计本月
                Date date = new Date();
                long totalMonth = repairListForMan.stream().filter(x -> MyDateUtils.isSameMonth(x.getGmtCreate(), date)).count();
                long goodTotalMonth = repairListForMan.stream().filter(x -> MyDateUtils.isSameMonth(x.getGmtCreate(), date))
                        .filter(x -> x.getScore() >= 5 || x.getScore() == null).count();
                jsonObject.put("totalMonth", totalMonth);
                jsonObject.put("goodTotalMonth", goodTotalMonth);
                if (totalMonth <= 0) {
                    jsonObject.put("goodTotalMonthRate", "0.0%");
                } else {
                    jsonObject.put("goodTotalMonthRate", String.format("%.2f%%", (double)goodTotalMonth / totalMonth * 100));
                }


                result.add(jsonObject);
            }
        }

        return RestResponse.getSuccesseResponse(result);
    }

    @RequestMapping(value = "/queryScoreStats", method = {RequestMethod.GET})
    public RestResponse<List<JSONObject>> queryScoreStats(
            String gmtStart,
            String gmtEnd,
            @RequestParam(value = "repairTypeId", required = false) Long repairTypeId
    ) throws ParseException {
        Example example4 = new Example(RepairApply.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        Date d1 = DateUtils.parseDate(gmtStart, "yyyy-MM-dd");
        Date d2 = DateUtils.parseDate(gmtEnd + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        criteria4.andBetween("gmtCreate", d1, d2);

        List<RepairApply> repairApplyList = repairApplyMapper.selectByExample(example4);
        // 指定了大类就筛选大类
        if (repairTypeId != null && repairTypeId > 0) {
            repairApplyList = repairApplyList.stream().filter(x -> x.getRepairType().equals(repairTypeId)).collect(Collectors.toList());
        }

        Map<Long, List<RepairApply>> repairApplyMap = repairApplyList.stream()
                .collect(Collectors.groupingBy(RepairApply::getRepairType));

        Map<Long, RepairType> repairTypeMap = repairTypeMapper.select(new RepairType())
                .stream().collect(Collectors.toMap(RepairType::getId, Function.identity()));

        List<JSONObject> result = new ArrayList<>();
        Long sumTotal = 0L;
        Long sumComplete = 0L;
        Long sumComplain = 0L;
        // 统计每个大类的数据
        for (Map.Entry<Long, RepairType> longRepairTypeEntry : repairTypeMap.entrySet()) {
            Long rTypeId = longRepairTypeEntry.getKey();
            List<RepairApply> repairApplyListForThisType = repairApplyMap.containsKey(rTypeId) ?
                    repairApplyMap.get(rTypeId) : new ArrayList<>();
            JSONObject jsonObject = new JSONObject();

            if (repairTypeMap.containsKey(rTypeId)) {
                jsonObject.put("typeName", repairTypeMap.get(rTypeId).getRepairType());
                jsonObject.put("repairTypeId", rTypeId);
            } else {
                jsonObject.put("typeName", "未知类别");
                jsonObject.put("repairTypeId", 0);
            }
            Long total = Long.valueOf(repairApplyListForThisType.size());
            Long complete = repairApplyListForThisType.stream().filter(
                    x -> RepairStatus.REPAIR_COMPLETE.name().equals(x.getRepairStatus()) || RepairStatus.ORDER_COMPLETE.name().equals(x.getRepairStatus())
            ).count();
            Long complain = repairApplyListForThisType.stream().filter(
                    x -> x.getScore() != null && x.getScore() < 3
            ).count();

            jsonObject.put("total", total);
            if (total <= 0) {
                jsonObject.put("completeRate", "0.0%");
                jsonObject.put("complainRate", "0.0%");
            } else {
                jsonObject.put("completeRate", String.format("%.1f%%", (double)complete / total * 100));
                jsonObject.put("complainRate", String.format("%.1f%%", (double)complain / total * 100));
            }

            result.add(jsonObject);

            sumTotal += total;
            sumComplete += complete;
            sumComplain += complain;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("typeName", "合计");
        jsonObject.put("repairTypeId", 0);
        jsonObject.put("total", sumTotal);
        if (sumTotal <= 0) {
            jsonObject.put("completeRate", "0.0%");
            jsonObject.put("complainRate", "0.0%");
        } else {
            jsonObject.put("completeRate", String.format("%.1f%%", (double)sumComplete / sumTotal * 100));
            jsonObject.put("complainRate", String.format("%.1f%%", (double)sumComplain / sumTotal * 100));
        }

        result.add(0, jsonObject);

        return RestResponse.getSuccesseResponse(result);
    }

    @RequestMapping(value = "/suggestSubmit", method = {RequestMethod.POST})
    public RestResponse<SuggestManage> suggestSubmit(
            @RequestBody SuggestManage suggestManage
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        suggestManage.setStatus(SuggestProcessStatus.WAIT_REPLY.name());
        suggestManageMapper.insert(suggestManage);

        // 通知接线员处理
        SysRole sysRole = new SysRole();
        sysRole.setRole(SystemRoleType.CUSTOMER_SERVICE.name());
        List<String> customerServiceList = sysRoleMapper.select(sysRole).stream()
                .map(x -> x.getUserId()).collect(Collectors.toList());

        if (customerServiceList.size() <= 0) {
            logger.error("未配置接线员");
            return RestResponse.getFailedResponse(Constants.RcError, "未配置接线员");
        }

        dingService.sendNotificationToUser(
                suggestAppAgentId,
                customerServiceList,
                "意见建议处理通知",
                String.format(
                        "有新的意见建议工单(单号为%d),请处理回复",
                        suggestManage.getId()
                ),
                dingService.getNotificationUrl("SUGGEST", "STAFF")
        );

        return RestResponse.getSuccesseResponse(suggestManage);
    }

    @RequestMapping(value = "/getSuggestDetail", method = {RequestMethod.GET})
    public RestResponse<SuggestManageVO> suggestSubmit(
            Long id
    ) throws ExecutionException, ApiException, DingServiceException {
        SuggestManage suggestManage = suggestManageMapper.selectByPrimaryKey(id);
        if (suggestManage == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到工单详情");
        }

        SuggestManageVO suggestManageVO = new SuggestManageVO();
        BeanUtils.copyProperties(suggestManage, suggestManageVO);
        suggestManageVO.setSubmitUserDetail(cacheService.getUserDetail(suggestManage.getUserId()));
        if (StringUtils.isNotBlank(suggestManage.getProcessUserId())) {
            suggestManageVO.setProcessUserDetail(cacheService.getUserDetail(suggestManage.getProcessUserId()));
        }

        return RestResponse.getSuccesseResponse(suggestManageVO);
    }

    @RequestMapping(value = "/querySuggestPageList", method = {RequestMethod.GET})
    public RestResponse<List<SuggestManageVO>> querySuggestPageList(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize
    ) {
        Example example4 = new Example(SuggestManage.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        if (StringUtils.isNotBlank(userId)) {
            criteria4.andEqualTo("userId", userId);
        }

        if (StringUtils.isNotBlank(status)) {
            criteria4.andIn("status", Arrays.asList(status.split(",", -1)));
        }

        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("gmt_create DESC");
        List<SuggestManage> suggestManageList = suggestManageMapper.selectByExample(example4);

        List<SuggestManageVO> suggestManageVOList = suggestManageList.stream().map(x -> {
            SuggestManageVO suggestManageVO = new SuggestManageVO();
            BeanUtils.copyProperties(x, suggestManageVO);
            try {
                suggestManageVO.setSubmitUserDetail(cacheService.getUserDetail(x.getUserId()));
                if (StringUtils.isNotBlank(x.getProcessUserId())) {
                    suggestManageVO.setProcessUserDetail(cacheService.getUserDetail(x.getProcessUserId()));
                }

                return suggestManageVO;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (DingServiceException e) {
                e.printStackTrace();
            }

            return null;
        }).filter(x -> x != null).collect(Collectors.toList());

        return RestResponse.getSuccesseResponse(suggestManageVOList);
    }

    @RequestMapping(value = "/transferSuggest", method = {RequestMethod.POST})
    public RestResponse<Void> transferSuggest(
            Long id,
            String processUserId,
            String relayComment
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        SuggestManage suggestManage = suggestManageMapper.selectByPrimaryKey(id);
        if (suggestManage == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到工单详情");
        }

        if (!SuggestProcessStatus.WAIT_TRANSFER.name().equals(suggestManage.getStatus())) {
            return RestResponse.getFailedResponse(Constants.RcError, String.format(
                    "工单状态为:%s,不能分派",
                    SuggestProcessStatus.WAIT_TRANSFER.getDisplayName()
            ));
        }

        suggestManage.setProcessUserId(processUserId);
        suggestManage.setRelayComment(relayComment);
        suggestManage.setGmtProcess(new Date());
        suggestManage.setStatus(SuggestProcessStatus.WAIT_REPLY.name());
        suggestManageMapper.updateByPrimaryKey(suggestManage);

        dingService.sendNotificationToUser(
                suggestAppAgentId,
                Arrays.asList(suggestManage.getUserId()),
                "意见建议已转交通知",
                String.format(
                        "您提交的意见建议工单(单号为%d)已转交处理,请前往查看详情",
                        suggestManage.getId()
                ),
                dingService.getNotificationUrl("SUGGEST", "USER")
        );

        dingService.sendNotificationToUser(
                suggestAppAgentId,
                Arrays.asList(suggestManage.getProcessUserId()),
                "意见建议待处理通知",
                String.format(
                        "有新的意见建议工单(单号为%d)待回复处理,请前往查看详情",
                        suggestManage.getId()
                ),
                dingService.getNotificationUrl("SUGGEST", "STAFF")
        );

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/processSuggest", method = {RequestMethod.POST})
    public RestResponse<Void> processSuggest(
            Long id,
            String processComment
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        SuggestManage suggestManage = suggestManageMapper.selectByPrimaryKey(id);
        if (suggestManage == null) {
            return RestResponse.getFailedResponse(Constants.RcError, "未查找到工单详情");
        }

        if (!SuggestProcessStatus.WAIT_REPLY.name().equals(suggestManage.getStatus())) {
            return RestResponse.getFailedResponse(Constants.RcError, String.format(
                    "工单状态为:%s,不能回复处理",
                    SuggestProcessStatus.WAIT_REPLY.getDisplayName()
            ));
        }

        suggestManage.setProcessComment(processComment);
        suggestManage.setGmtProcess(new Date());
        suggestManage.setStatus(SuggestProcessStatus.COMPLETE.name());
        suggestManageMapper.updateByPrimaryKey(suggestManage);

        dingService.sendNotificationToUser(
                suggestAppAgentId,
                Arrays.asList(suggestManage.getUserId()),
                "意见建议处理完毕通知",
                String.format(
                        "您提交的意见建议工单(单号为%d)已回复处理,请前往查看详情",
                        suggestManage.getId()
                ),
                dingService.getNotificationUrl("SUGGEST", "USER")
        );

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/querySuggestByDateRange", method = {RequestMethod.GET})
    public RestResponse<List<SuggestManageVO>> querySuggestByDateRange(
            String gmtStart,
            String gmtEnd
    ) {
        Example example4 = new Example(SuggestManage.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andBetween("gmtCreate", gmtStart, gmtEnd);

        List<SuggestManage> suggestManageList = suggestManageMapper.selectByExample(example4);

        List<SuggestManageVO> suggestManageVOList = suggestManageList.stream().map(x -> {
            SuggestManageVO suggestManageVO = new SuggestManageVO();
            BeanUtils.copyProperties(x, suggestManageVO);
            try {
                suggestManageVO.setSubmitUserDetail(cacheService.getUserDetail(x.getUserId()));
                if (StringUtils.isNotBlank(x.getProcessUserId())) {
                    suggestManageVO.setProcessUserDetail(cacheService.getUserDetail(x.getProcessUserId()));
                }

                return suggestManageVO;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (DingServiceException e) {
                e.printStackTrace();
            }

            return null;
        }).filter(x -> x != null).collect(Collectors.toList());

        return RestResponse.getSuccesseResponse(suggestManageVOList);
    }
}
