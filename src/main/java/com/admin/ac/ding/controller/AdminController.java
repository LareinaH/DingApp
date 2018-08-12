package com.admin.ac.ding.controller;

import com.admin.ac.ding.mapper.MeetingInChargeMapper;
import com.admin.ac.ding.mapper.MeetingMediaInChargeMapper;
import com.admin.ac.ding.mapper.MeetingPicsMapper;
import com.admin.ac.ding.mapper.MeetingRoomDetailMapper;
import com.admin.ac.ding.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin", produces = "application/json; charset=UTF-8")
public class AdminController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    MeetingRoomDetailMapper meetingRoomDetailMapper;

    @Autowired
    MeetingInChargeMapper meetingInChargeMapper;

    @Autowired
    MeetingMediaInChargeMapper meetingMediaInChargeMapper;

    @Autowired
    MeetingPicsMapper meetingPicsMapper;

    @RequestMapping(value = "/getMeetingRoomList", method = {RequestMethod.GET})
    public RestResponse<List<Map<String, Object>>> getMeetingRoomList() {
        return null;
    }

    @RequestMapping(value = "/addMeetingRoom", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> addMeetingRoom(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "place") String place,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "memo") String memo,
            @RequestBody Map<String, Object> extraInfo
    ) {
        MeetingRoomDetail meetingRoomDetail = new MeetingRoomDetail();
        meetingRoomDetail.setName(name);
        meetingRoomDetail.setPlace(place);
        meetingRoomDetail.setSize(size);
        meetingRoomDetail.setType(type);
        meetingRoomDetail.setMemo(memo);

        meetingRoomDetailMapper.insert(meetingRoomDetail);

        List<String> inChargeList = (List<String>)extraInfo.get("inCharge");
        List<String> mediaInChargeList = (List<String>)extraInfo.get("mediaInCharge");
        List<String> picsList = (List<String>)extraInfo.get("pics");

        meetingInChargeMapper.insertList(
                inChargeList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                    MeetingInCharge meetingInCharge = new MeetingInCharge();
                    meetingInCharge.setUserId(x);
                    meetingInCharge.setMeetingRoomId(meetingRoomDetail.getId());
                    return meetingInCharge;
                }).collect(Collectors.toList())
        );
        meetingMediaInChargeMapper.insertList(
                mediaInChargeList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                    MeetingMediaInCharge meetingMediaInCharge = new MeetingMediaInCharge();
                    meetingMediaInCharge.setUserId(x);
                    meetingMediaInCharge.setMeetingRoomId(meetingRoomDetail.getId());
                    return meetingMediaInCharge;
                }).collect(Collectors.toList())
        );

        meetingPicsMapper.insertList(
                picsList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                    MeetingPics meetingPics = new MeetingPics();
                    meetingPics.setMeetingRoomId(meetingRoomDetail.getId());
                    meetingPics.setPicsUrl(x);
                    return meetingPics;
                }).collect(Collectors.toList())
        );

        return RestResponse.getSuccesseResponse();
    }
}
