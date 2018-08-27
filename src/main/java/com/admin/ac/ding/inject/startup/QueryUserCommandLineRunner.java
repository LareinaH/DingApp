package com.admin.ac.ding.inject.startup;

import com.admin.ac.ding.mapper.MeetingBookMapper;
import com.admin.ac.ding.mapper.MeetingInChargeMapper;
import com.admin.ac.ding.mapper.MeetingMediaInChargeMapper;
import com.admin.ac.ding.mapper.SysRoleMapper;
import com.admin.ac.ding.model.MeetingBook;
import com.admin.ac.ding.model.MeetingInCharge;
import com.admin.ac.ding.model.MeetingMediaInCharge;
import com.admin.ac.ding.model.SysRole;
import com.admin.ac.ding.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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

        logger.info("cache stat:{}", cacheService.getCacheStat());
        logger.info("cache keys:{}", cacheService.getCacheKeys());
    }
}
