package com.admin.ac.ding.service;

import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.model.OapiUserGetWithDeptResponse;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class CacheService {
    Map<String, OapiUserGetResponse> userCacheMap = new ConcurrentHashMap<>();
    Map<Long, OapiDepartmentGetResponse> deptCacheMap = new ConcurrentHashMap<>();

    @Autowired
    DingService dingService;

    public OapiUserGetWithDeptResponse getUserDetail(String userId) throws ExecutionException, ApiException, DingServiceException {
        OapiUserGetResponse oapiUserGetResponse;
        if (userCacheMap.containsKey(userId)) {
            oapiUserGetResponse = userCacheMap.get(userId);
        } else {
            OapiUserGetResponse resp = dingService.getUserDetail(userId);
            userCacheMap.put(userId, resp);
            oapiUserGetResponse = resp;
        }

        OapiUserGetWithDeptResponse oapiUserGetWithDeptResponse = new OapiUserGetWithDeptResponse();
        BeanUtils.copyProperties(oapiUserGetResponse, oapiUserGetWithDeptResponse);

        for (Long deptId : oapiUserGetResponse.getDepartment()) {
            oapiUserGetWithDeptResponse.getDeptInfoList().add(getDeptDetail(deptId));
        }

        return oapiUserGetWithDeptResponse;
    }

    public OapiDepartmentGetResponse getDeptDetail(Long deptId) throws ExecutionException, ApiException, DingServiceException {
        if (deptCacheMap.containsKey(deptId)) {
            return deptCacheMap.get(deptId);
        } else {
            OapiDepartmentGetResponse resp = dingService.getDeptDetail(deptId);
            deptCacheMap.put(deptId, resp);
            return resp;
        }
    }
}
