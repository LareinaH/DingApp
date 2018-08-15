package com.admin.ac.ding.model;

import com.dingtalk.api.response.OapiUserGetResponse;

public class MeetingBookVO extends MeetingBook {
    MeetingRoomDetail meetingRoomDetail;

    OapiUserGetResponse bookUserDetail;

    public MeetingRoomDetail getMeetingRoomDetail() {
        return meetingRoomDetail;
    }

    public void setMeetingRoomDetail(MeetingRoomDetail meetingRoomDetail) {
        this.meetingRoomDetail = meetingRoomDetail;
    }

    public OapiUserGetResponse getBookUserDetail() {
        return bookUserDetail;
    }

    public void setBookUserDetail(OapiUserGetResponse bookUserDetail) {
        this.bookUserDetail = bookUserDetail;
    }
}
