package com.admin.ac.ding.inject.startup;

import com.admin.ac.ding.enums.RepairSrcType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.CacheService;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class QueryUserCommandLineRunner implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(QueryUserCommandLineRunner.class);

    @Autowired
    CacheService cacheService;

    @Autowired
    MeetingInChargeMapper meetingInChargeMapper;

    @Autowired
    MeetingMediaInChargeMapper meetingMediaInChargeMapper;

    @Autowired
    MeetingBookMapper meetingBookMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    RepairGroupMapper repairGroupMapper;

    @Autowired
    RepairApplyMapper repairApplyMapper;

    @Override
    public void run(String... strings) throws Exception {
        Example example3 = new Example(MeetingMediaInCharge.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("isDeleted", false);
        List<MeetingMediaInCharge> meetingMediaInChargeList = meetingMediaInChargeMapper.selectByExample(example3);
        for (MeetingMediaInCharge meetingMediaInCharge : meetingMediaInChargeList) {
            try {
                cacheService.getUserDetail(meetingMediaInCharge.getUserId());
            } catch (Exception e) {

            }
        }

        Example example2 = new Example(MeetingInCharge.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("isDeleted", false);
        List<MeetingInCharge> meetingInChargeList = meetingInChargeMapper.selectByExample(example2);
        for (MeetingInCharge meetingInCharge : meetingInChargeList) {
            try {
                cacheService.getUserDetail(meetingInCharge.getUserId());
            } catch (Exception e) {

            }
        }

        Example example4 = new Example(MeetingBook.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        List<MeetingBook> meetingBookList = meetingBookMapper.selectByExample(example4);
        for (MeetingBook meetingBook : meetingBookList) {
            try {
                cacheService.getUserDetail(meetingBook.getBookUserId());
            } catch (Exception e) {

            }
        }

        Example exampleSysRole = new Example(SysRole.class);
        Example.Criteria criteriaSysRole = exampleSysRole.createCriteria();
        criteriaSysRole.andEqualTo("isDeleted", false);
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(exampleSysRole);
        for (SysRole sysRole : sysRoleList) {
            try {
                cacheService.getUserDetail(sysRole.getUserId());
            } catch (Exception e) {

            }
        }

        repairGroupMapper.select(new RepairGroup()).forEach(x -> {
            try {
                cacheService.getUserDetail(x.getSupervisorUserId());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (DingServiceException e) {
                e.printStackTrace();
            }
        });

        repairApplyMapper.select(new RepairApply()).forEach(x -> {
            try {
                cacheService.getUserDetail(x.getSubmitUserId());
                if (StringUtils.isNotBlank(x.getDispatcherUserId())) {
                    cacheService.getUserDetail(x.getDispatcherUserId());
                }

                if (StringUtils.isNotBlank(x.getCompleteUserId())) {
                    cacheService.getUserDetail(x.getCompleteUserId());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (DingServiceException e) {
                e.printStackTrace();
            }
        });

        logger.info("cache stat:{}", cacheService.getCacheStat());
        logger.info("cache keys:{}", cacheService.getCacheKeys());
    }
}
