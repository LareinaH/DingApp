package com.admin.ac.ding.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`repair_apply`")
public class RepairApply extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.submit_user_id
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String submitUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_location
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String repairLocation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_status
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String repairStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_proc_user_id
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private Long repairProcUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_duration
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String repairDuration;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_type
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private Long repairType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.src_type
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String srcType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.real_user_name
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String realUserName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.real_user_phone
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String realUserPhone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.score
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private Integer score;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.gmt_confirm
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private Date gmtConfirm;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.gmt_repair_complete
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private Date gmtRepairComplete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.gmt_score
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private Date gmtScore;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_desc
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String repairDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_pics
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String repairPics;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.repair_comment
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String repairComment;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_apply.score_comment
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    private String scoreComment;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.submit_user_id
     *
     * @return the value of repair_apply.submit_user_id
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getSubmitUserId() {
        return submitUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.submit_user_id
     *
     * @param submitUserId the value for repair_apply.submit_user_id
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setSubmitUserId(String submitUserId) {
        this.submitUserId = submitUserId == null ? null : submitUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_location
     *
     * @return the value of repair_apply.repair_location
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRepairLocation() {
        return repairLocation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_location
     *
     * @param repairLocation the value for repair_apply.repair_location
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairLocation(String repairLocation) {
        this.repairLocation = repairLocation == null ? null : repairLocation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_status
     *
     * @return the value of repair_apply.repair_status
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRepairStatus() {
        return repairStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_status
     *
     * @param repairStatus the value for repair_apply.repair_status
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus == null ? null : repairStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_proc_user_id
     *
     * @return the value of repair_apply.repair_proc_user_id
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public Long getRepairProcUserId() {
        return repairProcUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_proc_user_id
     *
     * @param repairProcUserId the value for repair_apply.repair_proc_user_id
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairProcUserId(Long repairProcUserId) {
        this.repairProcUserId = repairProcUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_duration
     *
     * @return the value of repair_apply.repair_duration
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRepairDuration() {
        return repairDuration;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_duration
     *
     * @param repairDuration the value for repair_apply.repair_duration
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairDuration(String repairDuration) {
        this.repairDuration = repairDuration == null ? null : repairDuration.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_type
     *
     * @return the value of repair_apply.repair_type
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public Long getRepairType() {
        return repairType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_type
     *
     * @param repairType the value for repair_apply.repair_type
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairType(Long repairType) {
        this.repairType = repairType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.src_type
     *
     * @return the value of repair_apply.src_type
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getSrcType() {
        return srcType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.src_type
     *
     * @param srcType the value for repair_apply.src_type
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setSrcType(String srcType) {
        this.srcType = srcType == null ? null : srcType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.real_user_name
     *
     * @return the value of repair_apply.real_user_name
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRealUserName() {
        return realUserName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.real_user_name
     *
     * @param realUserName the value for repair_apply.real_user_name
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRealUserName(String realUserName) {
        this.realUserName = realUserName == null ? null : realUserName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.real_user_phone
     *
     * @return the value of repair_apply.real_user_phone
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRealUserPhone() {
        return realUserPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.real_user_phone
     *
     * @param realUserPhone the value for repair_apply.real_user_phone
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRealUserPhone(String realUserPhone) {
        this.realUserPhone = realUserPhone == null ? null : realUserPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.score
     *
     * @return the value of repair_apply.score
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public Integer getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.score
     *
     * @param score the value for repair_apply.score
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.gmt_confirm
     *
     * @return the value of repair_apply.gmt_confirm
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public Date getGmtConfirm() {
        return gmtConfirm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.gmt_confirm
     *
     * @param gmtConfirm the value for repair_apply.gmt_confirm
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setGmtConfirm(Date gmtConfirm) {
        this.gmtConfirm = gmtConfirm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.gmt_repair_complete
     *
     * @return the value of repair_apply.gmt_repair_complete
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public Date getGmtRepairComplete() {
        return gmtRepairComplete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.gmt_repair_complete
     *
     * @param gmtRepairComplete the value for repair_apply.gmt_repair_complete
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setGmtRepairComplete(Date gmtRepairComplete) {
        this.gmtRepairComplete = gmtRepairComplete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.gmt_score
     *
     * @return the value of repair_apply.gmt_score
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public Date getGmtScore() {
        return gmtScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.gmt_score
     *
     * @param gmtScore the value for repair_apply.gmt_score
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setGmtScore(Date gmtScore) {
        this.gmtScore = gmtScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_desc
     *
     * @return the value of repair_apply.repair_desc
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRepairDesc() {
        return repairDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_desc
     *
     * @param repairDesc the value for repair_apply.repair_desc
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc == null ? null : repairDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_pics
     *
     * @return the value of repair_apply.repair_pics
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRepairPics() {
        return repairPics;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_pics
     *
     * @param repairPics the value for repair_apply.repair_pics
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairPics(String repairPics) {
        this.repairPics = repairPics == null ? null : repairPics.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.repair_comment
     *
     * @return the value of repair_apply.repair_comment
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getRepairComment() {
        return repairComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.repair_comment
     *
     * @param repairComment the value for repair_apply.repair_comment
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setRepairComment(String repairComment) {
        this.repairComment = repairComment == null ? null : repairComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_apply.score_comment
     *
     * @return the value of repair_apply.score_comment
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public String getScoreComment() {
        return scoreComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_apply.score_comment
     *
     * @param scoreComment the value for repair_apply.score_comment
     *
     * @mbg.generated Tue Aug 28 21:37:19 CST 2018
     */
    public void setScoreComment(String scoreComment) {
        this.scoreComment = scoreComment == null ? null : scoreComment.trim();
    }
}