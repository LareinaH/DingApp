package com.admin.ac.ding.model;

public class SuggestManageVO extends SuggestManage {
    OapiUserGetWithDeptResponse submitUserDetail;
    OapiUserGetWithDeptResponse processUserDetail;

    public OapiUserGetWithDeptResponse getSubmitUserDetail() {
        return submitUserDetail;
    }

    public void setSubmitUserDetail(OapiUserGetWithDeptResponse submitUserDetail) {
        this.submitUserDetail = submitUserDetail;
    }

    public OapiUserGetWithDeptResponse getProcessUserDetail() {
        return processUserDetail;
    }

    public void setProcessUserDetail(OapiUserGetWithDeptResponse processUserDetail) {
        this.processUserDetail = processUserDetail;
    }
}
