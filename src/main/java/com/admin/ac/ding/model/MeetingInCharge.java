package com.admin.ac.ding.model;

import javax.persistence.*;

@Table(name = "`meeting_in_charge`")
public class MeetingInCharge extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_in_charge.user_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_in_charge.meeting_room_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    private Long meetingRoomId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_in_charge.user_id
     *
     * @return the value of meeting_in_charge.user_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_in_charge.user_id
     *
     * @param userId the value for meeting_in_charge.user_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_in_charge.meeting_room_id
     *
     * @return the value of meeting_in_charge.meeting_room_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_in_charge.meeting_room_id
     *
     * @param meetingRoomId the value for meeting_in_charge.meeting_room_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }
}