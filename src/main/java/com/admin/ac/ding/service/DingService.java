package com.admin.ac.ding.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class DingService {
    private Logger logger = LoggerFactory.getLogger(DingService.class);

    public static final String DING_ACCESS_TOKEN_KEY = "DING_ACCESS_TOKEN_KEY";

    @Value("${ding.corpid}")
    String corpId;

    @Value("${ding.corpsecret}")
    String corpSecret;

    LoadingCache<String,String> cahceBuilder = CacheBuilder
            .newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>(){
                @Override
                public String load(String key) throws Exception {
                    if (DING_ACCESS_TOKEN_KEY.equals(key)) {
                        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
                        OapiGettokenRequest request = new OapiGettokenRequest();
                        request.setCorpid(corpId);
                        request.setCorpsecret(corpSecret);
                        request.setHttpMethod("GET");
                        OapiGettokenResponse response = client.execute(request);
                        if (!response.isSuccess()) {
                            throw new Exception("获取access token失败:" + response.getErrmsg());
                        }

                        logger.info("cache access token with {}", response.getAccessToken());

                        return response.getAccessToken();
                    } else {
                        throw new Exception("暂时只支持以下key查询:" + DING_ACCESS_TOKEN_KEY);
                    }
                }

            });

    public String getAccessToken() throws ExecutionException {
        return cahceBuilder.get(DING_ACCESS_TOKEN_KEY);
    }
}
