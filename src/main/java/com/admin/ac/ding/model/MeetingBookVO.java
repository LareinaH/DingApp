package com.admin.ac.ding.model;

public class MeetingBookVO extends MeetingBook {
    MeetingRoomDetail meetingRoomDetail;

    public MeetingRoomDetail getMeetingRoomDetail() {
        return meetingRoomDetail;
    }

    public void setMeetingRoomDetail(MeetingRoomDetail meetingRoomDetail) {
        this.meetingRoomDetail = meetingRoomDetail;
    }
}
