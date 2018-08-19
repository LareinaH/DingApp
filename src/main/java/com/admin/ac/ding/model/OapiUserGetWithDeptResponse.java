package com.admin.ac.ding.model;

import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-08-19.
 */
public class OapiUserGetWithDeptResponse extends OapiUserGetResponse {
    List<OapiDepartmentGetResponse> deptInfoList = new ArrayList<>();

    public List<OapiDepartmentGetResponse> getDeptInfoList() {
        return deptInfoList;
    }

    public void setDeptInfoList(List<OapiDepartmentGetResponse> deptInfoList) {
        this.deptInfoList = deptInfoList;
    }
}
