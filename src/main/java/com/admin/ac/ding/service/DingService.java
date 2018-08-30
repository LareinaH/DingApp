package com.admin.ac.ding.service;

import com.admin.ac.ding.exception.DingServiceException;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.DingTalkResponse;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
                            throw new DingServiceException("获取access token失败", response);
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

    private void checkResponse(DingTalkResponse dingTalkResponse, String exceptionTitle) throws DingServiceException {
        if (!dingTalkResponse.isSuccess()) {
            throw new DingServiceException(exceptionTitle, dingTalkResponse);
        }
    }

    public List<OapiDepartmentListResponse.Department> getDeptList(
            String deptId,
            Boolean fetchChild
    ) throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest departmentListRequest = new OapiDepartmentListRequest();
        departmentListRequest.setId(deptId);
        departmentListRequest.setFetchChild(fetchChild);
        departmentListRequest.setHttpMethod("GET");
        OapiDepartmentListResponse departmentListResponse = client.execute(departmentListRequest, getAccessToken());
        checkResponse(departmentListResponse, "查询部门列表失败");

        return departmentListResponse.getDepartment();
    }

    public OapiDepartmentGetResponse getDeptDetail(Long deptId) throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/get");
        OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
        request.setId(String.valueOf(deptId));
        request.setHttpMethod("GET");
        OapiDepartmentGetResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "查询部门详情失败");

        return response;
    }

    public List<String> getDeptUserList(Long deptId) throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getDeptMember");
        OapiUserGetDeptMemberRequest req = new OapiUserGetDeptMemberRequest();
        req.setDeptId(String.valueOf(deptId));
        req.setHttpMethod("GET");
        OapiUserGetDeptMemberResponse rsp = client.execute(req, getAccessToken());
        checkResponse(rsp, "查询部门用户列表失败");
        return rsp.getUserIds();
    }

    public OapiUserGetResponse getUserDetail(
            String userId
    ) throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        OapiUserGetResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "查询用户信息失败");

        return response;
    }

    public List<OapiUserListResponse.Userlist> getDeptUserListDetail(Long deptId) throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/list");
        OapiUserListRequest request = new OapiUserListRequest();
        request.setDepartmentId(deptId);
        request.setHttpMethod("GET");

        OapiUserListResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "查询部门用户列表详情失败");
        return response.getUserlist();
    }

    public OapiUserGetuserinfoResponse getUserByCode(String code) throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");
        OapiUserGetuserinfoResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "获取登录用户失败");
        return response;
    }

    public OapiGetJsapiTicketResponse getJsApiTicket() throws DingServiceException, ExecutionException, ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/get_jsapi_ticket");
        OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
        request.setHttpMethod("GET");
        OapiGetJsapiTicketResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "获取jsapi_ticket失败");
        return response;
    }

    public void sendNotificationToUser(
            Long agentId,
            List<String> userIdList,
            String title,
            String content,
            String url
    ) throws ExecutionException, DingServiceException, ApiException, UnsupportedEncodingException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(userIdList.stream().collect(Collectors.joining(",")));
        request.setAgentId(agentId);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("link");
        msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
        msg.getLink().setTitle(title);
        msg.getLink().setText(content);
        msg.getLink().setMessageUrl("dingtalk://dingtalkclient/page/link?pc_slide=true&url=" + URLEncoder.encode(url, "utf-8"));
        if (title.startsWith("会议室")) {
            msg.getLink().setPicUrl("https://static.dingtalk.com/media/lALPBY0V5Esldu7OW6SLrs5PGgDY_1327104216_1537510318.png");
        } else if (title.startsWith("维修")) {
            msg.getLink().setPicUrl("https://static.dingtalk.com/media/lALPBY0V5FUHhGbOACAAbs4AaQB5_6881401_2097262.png");
        } else if (title.startsWith("意见建议")) {
            msg.getLink().setPicUrl("https://static.dingtalk.com/media/lALPBY0V5FUJw__OACAAbs4AaQB5_6881401_2097262.png");
        } else {
            msg.getLink().setPicUrl("https://static.dingtalk.com/media/lALPBY0V5Esldu7OW6SLrs5PGgDY_1327104216_1537510318.png");
        }

//        msg.getLink().setPicUrl("");
        request.setMsg(msg);

        OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, getAccessToken());
        checkResponse(response, "发送钉钉通知失败");

        logger.info("send notification {} to user {} success with task id {}", title, userIdList, response.getTaskId());
    }

    public OapiMessageCorpconversationGetsendprogressResponse getSendProgress(Long taskId, Long agentId) throws ExecutionException, ApiException, DingServiceException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress");
        OapiMessageCorpconversationGetsendprogressRequest request  = new OapiMessageCorpconversationGetsendprogressRequest();
        request.setAgentId(agentId);
        request.setTaskId(taskId);
        OapiMessageCorpconversationGetsendprogressResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "查询消息发送进度失败");

        return response;
    }

    public OapiMessageCorpconversationGetsendresultResponse getSendResult(Long taskId, Long agentId) throws ExecutionException, ApiException, DingServiceException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult");
        OapiMessageCorpconversationGetsendresultRequest request  = new OapiMessageCorpconversationGetsendresultRequest();
        request.setAgentId(agentId);
        request.setTaskId(taskId);
        OapiMessageCorpconversationGetsendresultResponse response = client.execute(request, getAccessToken());
        checkResponse(response, "查询消息发送结果失败");

        return response;
    }

    public String getNotificationUrl(String action, String target) {
        return String.format(
                "http://47.97.212.22/DingApp/redirect.html?action=%s&target=%s",
                action, target
        );
    }
}
