package com.admin.ac.ding.inject.startup;

import com.admin.ac.ding.mapper.MeetingInChargeMapper;
import com.admin.ac.ding.mapper.MeetingMediaInChargeMapper;
import com.admin.ac.ding.model.MeetingInCharge;
import com.admin.ac.ding.model.MeetingMediaInCharge;
import com.admin.ac.ding.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Component
public class QueryUserCommandLineRunner implements CommandLineRunner {

    @Autowired
    CacheService cacheService;

    @Autowired
    MeetingInChargeMapper meetingInChargeMapper;

    @Autowired
    MeetingMediaInChargeMapper meetingMediaInChargeMapper;

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
    }
}
