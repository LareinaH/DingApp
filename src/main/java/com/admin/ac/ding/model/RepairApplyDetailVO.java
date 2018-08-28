package com.admin.ac.ding.model;

public class RepairApplyDetailVO extends RepairApply {
    OapiUserGetWithDeptResponse submitUserDetail;

    RepairManGroup repairManGroup;

    RepairType repairTypeDetail;

    RepairSubType repairSubTypeDetail;

    public OapiUserGetWithDeptResponse getSubmitUserDetail() {
        return submitUserDetail;
    }

    public void setSubmitUserDetail(OapiUserGetWithDeptResponse submitUserDetail) {
        this.submitUserDetail = submitUserDetail;
    }

    public RepairManGroup getRepairManGroup() {
        return repairManGroup;
    }

    public void setRepairManGroup(RepairManGroup repairManGroup) {
        this.repairManGroup = repairManGroup;
    }

    public RepairType getRepairTypeDetail() {
        return repairTypeDetail;
    }

    public void setRepairTypeDetail(RepairType repairTypeDetail) {
        this.repairTypeDetail = repairTypeDetail;
    }

    public RepairSubType getRepairSubTypeDetail() {
        return repairSubTypeDetail;
    }

    public void setRepairSubTypeDetail(RepairSubType repairSubTypeDetail) {
        this.repairSubTypeDetail = repairSubTypeDetail;
    }
}
