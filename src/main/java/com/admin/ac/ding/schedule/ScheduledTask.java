package com.admin.ac.ding.schedule;

import com.admin.ac.ding.enums.RepairStatus;
import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.RepairApplyMapper;
import com.admin.ac.ding.mapper.SysRoleMapper;
import com.admin.ac.ding.model.RepairApply;
import com.admin.ac.ding.model.SysRole;
import com.admin.ac.ding.service.DingService;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
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

    @Value("${ding.app.repair.url")
    String repairListUrl;

    @Scheduled(cron = "0 */1 * * * ?" )
    public void repairDispatchRemind() throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        RepairApply repairApply = new RepairApply();
        repairApply.setRepairStatus(RepairStatus.WAIT_CONFIRM.name());
        repairApply.setRemindDispatch(Byte.valueOf("0"));
        Long now = System.currentTimeMillis() / 1000;
        List<RepairApply> repairApplyList = repairApplyMapper.select(repairApply)
                .stream().filter(x -> {
                    // 时间差>=5分钟
                    Long gmtCreate = x.getGmtCreate().getTime()  / 1000;
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
}
