package com.admin.ac.ding.controller;

import com.admin.ac.ding.constants.Constants;
import com.admin.ac.ding.enums.MeetingBookStatus;
import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserListResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
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

    @RequestMapping(value = "/meetingBookApply", method = {RequestMethod.POST})
    public RestResponse<Void> meetingBookApply(
            @RequestBody MeetingBook meetingBook
    ) {
        // 检查基础参数

        // 检查指定会议室是否已经被占用
        // 检查指定会议室是否被其它人申请中
        // 插入记录
        // 通知管理员审核

        meetingBook.setBookStatus(MeetingBookStatus.WAIT_APPROVE.name());
        meetingBookMapper.insert(meetingBook);

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
            criteria4.andEqualTo("bookUserId", userId);
        }

        if (StringUtils.isNotBlank(userId)) {
            criteria4.andEqualTo("bookStatus", MeetingBookStatus.valueOf(bookStatus).name());
        }

        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("gmt_create DESC");
        List<MeetingBook> meetingBookList = meetingBookMapper.selectAll();

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
        OapiUserGetResponse oapiUserGetResponse = dingService.getUserDetail(meetingBook.getBookUserId());
        meetingBookVO.setBookUserDetail(oapiUserGetResponse);

        return RestResponse.getSuccesseResponse(meetingBookVO);
    }
}
