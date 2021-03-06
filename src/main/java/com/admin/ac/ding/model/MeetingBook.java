package com.admin.ac.ding.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`meeting_book`")
public class MeetingBook extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.book_day
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date bookDay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.book_time
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String bookTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.book_user_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String bookUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.meeting_room_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Long meetingRoomId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.meeting_start
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="HH:mm:ss")
    private Date meetingStart;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.meeting_participant_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Integer meetingParticipantCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.meeting_chairman_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Integer meetingChairmanCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.meeting_microphone_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Integer meetingMicrophoneCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.meeting_name
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String meetingName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.major_leaders
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String majorLeaders;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.use_conditioner
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Byte useConditioner;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.use_banner
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Byte useBanner;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.use_projector
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Byte useProjector;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.book_status
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String bookStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.invoice_name
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String invoiceName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.invoice_tax_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String invoiceTaxId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.pre_arrange
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Byte preArrange;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.confirm_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Byte confirmRemind;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.gmt_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Date gmtRemind;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.pre_arrange_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Byte preArrangeRemind;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.gmt_pre_arrange
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    @JsonFormat(locale="zh_CN", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date gmtPreArrange;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.gmt_pre_arrange_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private Date gmtPreArrangeRemind;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.book_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String bookComment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.deny_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String denyComment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.cancel_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String cancelComment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column meeting_book.agree_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    private String agreeComment;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.book_day
     *
     * @return the value of meeting_book.book_day
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Date getBookDay() {
        return bookDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.book_day
     *
     * @param bookDay the value for meeting_book.book_day
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setBookDay(Date bookDay) {
        this.bookDay = bookDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.book_time
     *
     * @return the value of meeting_book.book_time
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getBookTime() {
        return bookTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.book_time
     *
     * @param bookTime the value for meeting_book.book_time
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setBookTime(String bookTime) {
        this.bookTime = bookTime == null ? null : bookTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.book_user_id
     *
     * @return the value of meeting_book.book_user_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getBookUserId() {
        return bookUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.book_user_id
     *
     * @param bookUserId the value for meeting_book.book_user_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setBookUserId(String bookUserId) {
        this.bookUserId = bookUserId == null ? null : bookUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.meeting_room_id
     *
     * @return the value of meeting_book.meeting_room_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.meeting_room_id
     *
     * @param meetingRoomId the value for meeting_book.meeting_room_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.meeting_start
     *
     * @return the value of meeting_book.meeting_start
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Date getMeetingStart() {
        return meetingStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.meeting_start
     *
     * @param meetingStart the value for meeting_book.meeting_start
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMeetingStart(Date meetingStart) {
        this.meetingStart = meetingStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.meeting_participant_count
     *
     * @return the value of meeting_book.meeting_participant_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Integer getMeetingParticipantCount() {
        return meetingParticipantCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.meeting_participant_count
     *
     * @param meetingParticipantCount the value for meeting_book.meeting_participant_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMeetingParticipantCount(Integer meetingParticipantCount) {
        this.meetingParticipantCount = meetingParticipantCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.meeting_chairman_count
     *
     * @return the value of meeting_book.meeting_chairman_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Integer getMeetingChairmanCount() {
        return meetingChairmanCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.meeting_chairman_count
     *
     * @param meetingChairmanCount the value for meeting_book.meeting_chairman_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMeetingChairmanCount(Integer meetingChairmanCount) {
        this.meetingChairmanCount = meetingChairmanCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.meeting_microphone_count
     *
     * @return the value of meeting_book.meeting_microphone_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Integer getMeetingMicrophoneCount() {
        return meetingMicrophoneCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.meeting_microphone_count
     *
     * @param meetingMicrophoneCount the value for meeting_book.meeting_microphone_count
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMeetingMicrophoneCount(Integer meetingMicrophoneCount) {
        this.meetingMicrophoneCount = meetingMicrophoneCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.meeting_name
     *
     * @return the value of meeting_book.meeting_name
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getMeetingName() {
        return meetingName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.meeting_name
     *
     * @param meetingName the value for meeting_book.meeting_name
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName == null ? null : meetingName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.major_leaders
     *
     * @return the value of meeting_book.major_leaders
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getMajorLeaders() {
        return majorLeaders;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.major_leaders
     *
     * @param majorLeaders the value for meeting_book.major_leaders
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setMajorLeaders(String majorLeaders) {
        this.majorLeaders = majorLeaders == null ? null : majorLeaders.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.use_conditioner
     *
     * @return the value of meeting_book.use_conditioner
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Byte getUseConditioner() {
        return useConditioner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.use_conditioner
     *
     * @param useConditioner the value for meeting_book.use_conditioner
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setUseConditioner(Byte useConditioner) {
        this.useConditioner = useConditioner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.use_banner
     *
     * @return the value of meeting_book.use_banner
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Byte getUseBanner() {
        return useBanner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.use_banner
     *
     * @param useBanner the value for meeting_book.use_banner
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setUseBanner(Byte useBanner) {
        this.useBanner = useBanner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.use_projector
     *
     * @return the value of meeting_book.use_projector
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Byte getUseProjector() {
        return useProjector;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.use_projector
     *
     * @param useProjector the value for meeting_book.use_projector
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setUseProjector(Byte useProjector) {
        this.useProjector = useProjector;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.book_status
     *
     * @return the value of meeting_book.book_status
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getBookStatus() {
        return bookStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.book_status
     *
     * @param bookStatus the value for meeting_book.book_status
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus == null ? null : bookStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.invoice_name
     *
     * @return the value of meeting_book.invoice_name
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getInvoiceName() {
        return invoiceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.invoice_name
     *
     * @param invoiceName the value for meeting_book.invoice_name
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName == null ? null : invoiceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.invoice_tax_id
     *
     * @return the value of meeting_book.invoice_tax_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getInvoiceTaxId() {
        return invoiceTaxId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.invoice_tax_id
     *
     * @param invoiceTaxId the value for meeting_book.invoice_tax_id
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setInvoiceTaxId(String invoiceTaxId) {
        this.invoiceTaxId = invoiceTaxId == null ? null : invoiceTaxId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.pre_arrange
     *
     * @return the value of meeting_book.pre_arrange
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Byte getPreArrange() {
        return preArrange;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.pre_arrange
     *
     * @param preArrange the value for meeting_book.pre_arrange
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setPreArrange(Byte preArrange) {
        this.preArrange = preArrange;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.confirm_remind
     *
     * @return the value of meeting_book.confirm_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Byte getConfirmRemind() {
        return confirmRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.confirm_remind
     *
     * @param confirmRemind the value for meeting_book.confirm_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setConfirmRemind(Byte confirmRemind) {
        this.confirmRemind = confirmRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.gmt_remind
     *
     * @return the value of meeting_book.gmt_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Date getGmtRemind() {
        return gmtRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.gmt_remind
     *
     * @param gmtRemind the value for meeting_book.gmt_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setGmtRemind(Date gmtRemind) {
        this.gmtRemind = gmtRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.pre_arrange_remind
     *
     * @return the value of meeting_book.pre_arrange_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Byte getPreArrangeRemind() {
        return preArrangeRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.pre_arrange_remind
     *
     * @param preArrangeRemind the value for meeting_book.pre_arrange_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setPreArrangeRemind(Byte preArrangeRemind) {
        this.preArrangeRemind = preArrangeRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.gmt_pre_arrange
     *
     * @return the value of meeting_book.gmt_pre_arrange
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Date getGmtPreArrange() {
        return gmtPreArrange;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.gmt_pre_arrange
     *
     * @param gmtPreArrange the value for meeting_book.gmt_pre_arrange
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setGmtPreArrange(Date gmtPreArrange) {
        this.gmtPreArrange = gmtPreArrange;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.gmt_pre_arrange_remind
     *
     * @return the value of meeting_book.gmt_pre_arrange_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public Date getGmtPreArrangeRemind() {
        return gmtPreArrangeRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.gmt_pre_arrange_remind
     *
     * @param gmtPreArrangeRemind the value for meeting_book.gmt_pre_arrange_remind
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setGmtPreArrangeRemind(Date gmtPreArrangeRemind) {
        this.gmtPreArrangeRemind = gmtPreArrangeRemind;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.book_comment
     *
     * @return the value of meeting_book.book_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getBookComment() {
        return bookComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.book_comment
     *
     * @param bookComment the value for meeting_book.book_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setBookComment(String bookComment) {
        this.bookComment = bookComment == null ? null : bookComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.deny_comment
     *
     * @return the value of meeting_book.deny_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getDenyComment() {
        return denyComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.deny_comment
     *
     * @param denyComment the value for meeting_book.deny_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setDenyComment(String denyComment) {
        this.denyComment = denyComment == null ? null : denyComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.cancel_comment
     *
     * @return the value of meeting_book.cancel_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getCancelComment() {
        return cancelComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.cancel_comment
     *
     * @param cancelComment the value for meeting_book.cancel_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setCancelComment(String cancelComment) {
        this.cancelComment = cancelComment == null ? null : cancelComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column meeting_book.agree_comment
     *
     * @return the value of meeting_book.agree_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public String getAgreeComment() {
        return agreeComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column meeting_book.agree_comment
     *
     * @param agreeComment the value for meeting_book.agree_comment
     *
     * @mbg.generated Thu Aug 30 01:55:30 CST 2018
     */
    public void setAgreeComment(String agreeComment) {
        this.agreeComment = agreeComment == null ? null : agreeComment.trim();
    }
}