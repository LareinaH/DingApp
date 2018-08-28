package com.admin.ac.ding.model;

import java.util.List;

public class RepairTypeTreeVO {
    Long value;
    String text;
    List<OapiUserGetWithDeptResponse> supervisorList;
    List<RepairManGroup> repairManGroupList;
    List<RepairSubTypeVO> children;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<OapiUserGetWithDeptResponse> getSupervisorList() {
        return supervisorList;
    }

    public void setSupervisorList(List<OapiUserGetWithDeptResponse> supervisorList) {
        this.supervisorList = supervisorList;
    }

    public List<RepairManGroup> getRepairManGroupList() {
        return repairManGroupList;
    }

    public void setRepairManGroupList(List<RepairManGroup> repairManGroupList) {
        this.repairManGroupList = repairManGroupList;
    }

    public List<RepairSubTypeVO> getChildren() {
        return children;
    }

    public void setChildren(List<RepairSubTypeVO> children) {
        this.children = children;
    }
}
