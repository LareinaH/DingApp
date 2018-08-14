package com.admin.ac.ding.controller;

import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserListResponse;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

    @RequestMapping(value = "/meetingBookApply", method = {RequestMethod.POST})
    public RestResponse<Void> meetingBookApply(
            MeetingBook meetingBook,
            String time,
            String start
    ) {
        // 检查基础参数
        // 检查指定会议室是否已经被占用
        // 检查指定会议室是否被其它人申请中
        // 插入记录
        // 通知管理员审核
        return RestResponse.getSuccesseResponse();
    }
}
