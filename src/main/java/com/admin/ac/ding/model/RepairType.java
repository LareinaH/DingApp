package com.admin.ac.ding.model;

import javax.persistence.*;

@Table(name = "`repair_type`")
public class RepairType extends BaseModel {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_type.repair_type_id
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    private Long repairTypeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_type.worker_name
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    private String workerName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column repair_type.worker_phone
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    private String workerPhone;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_type.repair_type_id
     *
     * @return the value of repair_type.repair_type_id
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    public Long getRepairTypeId() {
        return repairTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_type.repair_type_id
     *
     * @param repairTypeId the value for repair_type.repair_type_id
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    public void setRepairTypeId(Long repairTypeId) {
        this.repairTypeId = repairTypeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_type.worker_name
     *
     * @return the value of repair_type.worker_name
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_type.worker_name
     *
     * @param workerName the value for repair_type.worker_name
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    public void setWorkerName(String workerName) {
        this.workerName = workerName == null ? null : workerName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column repair_type.worker_phone
     *
     * @return the value of repair_type.worker_phone
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    public String getWorkerPhone() {
        return workerPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column repair_type.worker_phone
     *
     * @param workerPhone the value for repair_type.worker_phone
     *
     * @mbg.generated Tue Aug 28 01:37:55 CST 2018
     */
    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone == null ? null : workerPhone.trim();
    }
}