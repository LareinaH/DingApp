package com.admin.ac.ding.schedule;

import com.admin.ac.ding.controller.AdminController;
import com.admin.ac.ding.enums.MeetingBookStatus;
import com.admin.ac.ding.enums.RepairStatus;
import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class ScheduledTask {
    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    RepairApplyMapper repairApplyMapper;

    @Autowired
    DingService dingService;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    MeetingBookMapper meetingBookMapper;

    @Autowired
    AdminController adminController;

    @Autowired
    MeetingInChargeMapper meetingInChargeMapper;

    @Autowired
    MeetingMediaInChargeMapper meetingMediaInChargeMapper;

    @Value("${ding.app.repair.url")
    String repairListUrl;

    @Value("${ding.app.meetingbook.url}")
    String meetingBookUrl;

    @Scheduled(cron = "0 */1 * * * ?" )
    public void repairDispatchRemind() throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        RepairApply repairApply = new RepairApply();
        repairApply.setRepairStatus(RepairStatus.WAIT_CONFIRM.name());
        repairApply.setRemindDispatch(Byte.valueOf("0"));
        Long now = System.currentTimeMillis() / 1000;
        List<RepairApply> repairApplyList = repairApplyMapper.select(repairApply)
                .stream().filter(x -> {
                    // 时间差>=5分钟
                    Long gmtCreate = x.getGmtCreate().getTime() / 1000;
                    if (now - gmtCreate >= 300) {
                        return true;
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList());
        // 取所有接线员
        SysRole sysRole = new SysRole();
        sysRole.setRole(SystemRoleType.CUSTOMER_SERVICE.name());
        List<String> customerServiceList = sysRoleMapper.select(sysRole).stream()
                .map(x -> x.getUserId()).collect(Collectors.toList());

        if (customerServiceList.size() <= 0) {
            logger.error("未配置接线员");
            return ;
        }

        for (RepairApply apply : repairApplyList) {
            dingService.sendNotificationToUser(
                    customerServiceList,
                    "维修催促通知",
                    String.format(
                            "维修工单(申请单号为%d)尚未确认,请催促维修组长处理",
                            apply.getId()
                    ),
                    repairListUrl
            );

            apply.setRemindDispatch(Byte.valueOf("1"));
            apply.setGmtRemind(new Date());
            repairApplyMapper.updateByPrimaryKey(apply);
        }
    }

    @Scheduled(cron = "15 */1 * * * ?" )
    public void meetingBookConfirmRemind() throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        MeetingBook meetingBook = new MeetingBook();
        meetingBook.setBookStatus(MeetingBookStatus.WAIT_APPROVE.name());
        meetingBook.setConfirmRemind(Byte.valueOf("0"));
        Long now = System.currentTimeMillis() / 1000;
        List<MeetingBook> meetingBookList = meetingBookMapper.select(meetingBook)
                .stream().filter(x -> {
                    // 时间差>=5分钟
                    Long gmtCreate = x.getGmtCreate().getTime() / 1000;
                    if (now - gmtCreate >= 300) {
                        return true;
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList());

        RestResponse<List<OapiUserGetWithDeptResponse>> resp = adminController.getSystemRole(SystemRoleType.MEETING_BOOK_REVIEW.name());
        List<String> bookReviewers = resp.getData().stream()
                .map(x -> x.getUserid()).distinct().collect(Collectors.toList());

        if (bookReviewers.size() <= 0) {
            logger.error("未配置会议室预约审核人员");
            return ;
        }

        for (MeetingBook book : meetingBookList) {
            dingService.sendNotificationToUser(
                    bookReviewers,
                    "会议室预约申请审核催促通知",
                    String.format("会议室预约申请(申请单号为%d)尚未审核，请前往处理", book.getId()),
                    meetingBookUrl
            );

            book.setConfirmRemind(Byte.valueOf("1"));
            book.setGmtRemind(new Date());
            meetingBookMapper.updateByPrimaryKey(book);
        }
    }

    @Scheduled(cron = "30 */1 * * * ?" )
    public void meetingBookPreArrangeRemind() throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        MeetingBook meetingBook = new MeetingBook();
        meetingBook.setBookStatus(MeetingBookStatus.AGREE.name());
        meetingBook.setPreArrange(Byte.valueOf("1"));
        meetingBook.setPreArrangeRemind(Byte.valueOf("0"));
        Long now = System.currentTimeMillis() / 1000;
        List<MeetingBook> meetingBookList = meetingBookMapper.select(meetingBook)
                .stream().filter(x -> {
                    // 时间差<=5分钟
                    Long gmtCreate = x.getGmtPreArrange().getTime() / 1000;
                    if (now < gmtCreate && gmtCreate - now <= 300) {
                        return true;
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList());

        for (MeetingBook book : meetingBookList) {
            Example example4 = new Example(MeetingInCharge.class);
            Example.Criteria criteria4 = example4.createCriteria();
            criteria4.andEqualTo("isDeleted", false);
            criteria4.andEqualTo("meetingRoomId", book.getMeetingRoomId());
            List<MeetingInCharge> meetingInChargeList = meetingInChargeMapper.selectByExample(example4);

            Example example3 = new Example(MeetingMediaInCharge.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("isDeleted", false);
            criteria3.andEqualTo("meetingRoomId", book.getMeetingRoomId());
            List<MeetingMediaInCharge> meetingMediaInChargeList = meetingMediaInChargeMapper.selectByExample(example3);

            Set<String> notificationUsers = new TreeSet<>();
            notificationUsers.addAll(meetingInChargeList.stream().map(x -> x.getUserId()).collect(Collectors.toList()));
            notificationUsers.addAll(meetingMediaInChargeList.stream().map(x -> x.getUserId()).collect(Collectors.toList()));

            notificationUsers.add(book.getBookUserId());

            dingService.sendNotificationToUser(
                    new ArrayList<>(notificationUsers),
                    "会议室预约申请审核催促通知",
                    String.format("会议室预约申请(申请单号为%d)尚未审核，请前往处理", book.getId()),
                    meetingBookUrl
            );

            book.setPreArrangeRemind(Byte.valueOf("1"));
            book.setGmtPreArrangeRemind(new Date());
            meetingBookMapper.updateByPrimaryKey(book);
        }
    }
}
