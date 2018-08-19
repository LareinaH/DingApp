package com.admin.ac.ding.model;

public class MeetingBookVO extends MeetingBook {
    MeetingRoomDetail meetingRoomDetail;

    OapiUserGetWithDeptResponse bookUserDetail;

    public MeetingRoomDetail getMeetingRoomDetail() {
        return meetingRoomDetail;
    }

    public void setMeetingRoomDetail(MeetingRoomDetail meetingRoomDetail) {
        this.meetingRoomDetail = meetingRoomDetail;
    }

    public OapiUserGetWithDeptResponse getBookUserDetail() {
        return bookUserDetail;
    }

    public void setBookUserDetail(OapiUserGetWithDeptResponse bookUserDetail) {
        this.bookUserDetail = bookUserDetail;
    }
}
