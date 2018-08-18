package com.admin.ac.ding.service;

import com.admin.ac.ding.exception.DingServiceException;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class UserCacheService {
    Map<String, OapiUserGetResponse> userCacheMap = new ConcurrentHashMap<>();

    @Autowired
    DingService dingService;

    public OapiUserGetResponse getUserDetail(String userId) throws ExecutionException, ApiException, DingServiceException {
        if (userCacheMap.containsKey(userId)) {
            return userCacheMap.get(userId);
        } else {
            OapiUserGetResponse resp = dingService.getUserDetail(userId);
            userCacheMap.put(userId, resp);
            return resp;
        }
    }
}
