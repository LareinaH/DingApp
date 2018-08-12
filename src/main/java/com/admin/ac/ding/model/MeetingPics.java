package com.admin.ac.ding.model;

import javax.persistence.*;

@Table(name = "`meeting_pics`")
public class MeetingPics extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_pics.meeting_room_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    private Long meetingRoomId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_pics.pics_url
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    private String picsUrl;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_pics.meeting_room_id
     *
     * @return the value of meeting_pics.meeting_room_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_pics.meeting_room_id
     *
     * @param meetingRoomId the value for meeting_pics.meeting_room_id
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_pics.pics_url
     *
     * @return the value of meeting_pics.pics_url
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public String getPicsUrl() {
        return picsUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_pics.pics_url
     *
     * @param picsUrl the value for meeting_pics.pics_url
     *
     * @mbg.generated Sun Aug 12 21:21:39 CST 2018
     */
    public void setPicsUrl(String picsUrl) {
        this.picsUrl = picsUrl == null ? null : picsUrl.trim();
    }
}