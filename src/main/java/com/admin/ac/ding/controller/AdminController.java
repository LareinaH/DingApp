package com.admin.ac.ding.controller;

import com.admin.ac.ding.constants.Constants;
import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.admin.ac.ding.service.CacheService;
import com.admin.ac.ding.utils.DingTalkEncryptException;
import com.admin.ac.ding.utils.DingTalkJsApiSingnature;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/admin", produces = "application/json; charset=UTF-8")
public class AdminController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    MeetingRoomDetailMapper meetingRoomDetailMapper;

    @Autowired
    MeetingInChargeMapper meetingInChargeMapper;

    @Autowired
    MeetingMediaInChargeMapper meetingMediaInChargeMapper;

    @Autowired
    MeetingPicsMapper meetingPicsMapper;

    @Autowired
    PrivilegedDeptMapper privilegedDeptMapper;

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    DingService dingService;

    @Autowired
    CacheService cacheService;

    @Autowired
    RepairTypeMapper repairTypeMapper;

    @Autowired
    RepairSubTypeMapper repairSubTypeMapper;

    @Autowired
    RepairGroupMapper repairGroupMapper;

    @Autowired
    RepairManGroupMapper repairManGroupMapper;

    @Autowired
    DeptListLocalMapper deptListLocalMapper;

    @Value("${ding.app.meetingbook.agentid}")
    Long meetingBookAppAgentId;

    @RequestMapping(value = "/getMeetingRoomList", method = {RequestMethod.GET})
    public RestResponse<List<MeetingRoomDetailVO>> getMeetingRoomList() {
        Example example = new Example(MeetingRoomDetail.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", false);
        List<MeetingRoomDetail> meetingRoomDetailList = meetingRoomDetailMapper.selectByExample(example);
        return RestResponse.getSuccesseResponse(
            meetingRoomDetailList.stream().map(x -> {
                MeetingRoomDetailVO meetingRoomDetailVO = new MeetingRoomDetailVO();
                BeanUtils.copyProperties(x, meetingRoomDetailVO);
                Example example2 = new Example(MeetingInCharge.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("isDeleted", false);
                criteria2.andEqualTo("meetingRoomId", x.getId());
                List<MeetingInCharge> meetingInChargeList = meetingInChargeMapper.selectByExample(example2);
                meetingRoomDetailVO.getInCharge().addAll(
                        meetingInChargeList.stream().map(y -> {
                            try {
                                return cacheService.getUserDetail(y.getUserId());
                            } catch (DingServiceException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }).filter(z -> z != null).collect(Collectors.toList())
                );

                Example example3 = new Example(MeetingMediaInCharge.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("isDeleted", false);
                criteria3.andEqualTo("meetingRoomId", x.getId());
                List<MeetingMediaInCharge> meetingMediaInChargeList = meetingMediaInChargeMapper.selectByExample(example3);
                meetingRoomDetailVO.getMediaInCharge().addAll(
                        meetingMediaInChargeList.stream().map(y -> {
                            try {
                                return cacheService.getUserDetail(y.getUserId());
                            } catch (DingServiceException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }).filter(z -> z != null).collect(Collectors.toList())
                );

                Example example4 = new Example(MeetingPics.class);
                Example.Criteria criteria4 = example4.createCriteria();
                criteria4.andEqualTo("isDeleted", false);
                criteria4.andEqualTo("meetingRoomId", x.getId());
                List<MeetingPics> meetingPicsList = meetingPicsMapper.selectByExample(example4);
                meetingRoomDetailVO.getPics().addAll(
                        meetingPicsList.stream().map(y -> y.getPicsUrl()).collect(Collectors.toList())
                );

                return meetingRoomDetailVO;
            }).collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/delMeetingRoom", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> delMeetingRoom(
           Long id
    ) {
        meetingRoomDetailMapper.delMeetingRoom(id);
        return RestResponse.getSuccesseResponse();
    }


    @RequestMapping(value = "/addMeetingRoom", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> addMeetingRoom(
              @RequestBody Map<String, Object> extraInfo
    ) {
        String name = (String)extraInfo.get("name");
        String place = (String)extraInfo.get("place");
        Integer size = (Integer)extraInfo.get("size");
        String type = (String)extraInfo.get("type");
        String memo = (String)extraInfo.get("memo");
        MeetingRoomDetail meetingRoomDetail = new MeetingRoomDetail();
        meetingRoomDetail.setName(name);
        meetingRoomDetail.setPlace(place);
        meetingRoomDetail.setSize(size);
        meetingRoomDetail.setType(type);
        meetingRoomDetail.setMemo(memo);

        meetingRoomDetailMapper.insert(meetingRoomDetail);

        List<String> inChargeList = (List<String>)extraInfo.get("inCharge");
        List<String> mediaInChargeList = (List<String>)extraInfo.get("mediaInCharge");
        List<String> picsList = (List<String>)extraInfo.get("pics");

        meetingInChargeMapper.insertList(
                inChargeList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                    MeetingInCharge meetingInCharge = new MeetingInCharge();
                    meetingInCharge.setUserId(x);
                    meetingInCharge.setMeetingRoomId(meetingRoomDetail.getId());
                    return meetingInCharge;
                }).collect(Collectors.toList())
        );
        meetingMediaInChargeMapper.insertList(
                mediaInChargeList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                    MeetingMediaInCharge meetingMediaInCharge = new MeetingMediaInCharge();
                    meetingMediaInCharge.setUserId(x);
                    meetingMediaInCharge.setMeetingRoomId(meetingRoomDetail.getId());
                    return meetingMediaInCharge;
                }).collect(Collectors.toList())
        );
        List<MeetingPics> meetingPicsList = picsList.stream()
                .filter(x -> StringUtils.isNotBlank(x))
                .map(x -> {
                    MeetingPics meetingPics = new MeetingPics();
                    meetingPics.setMeetingRoomId(meetingRoomDetail.getId());
                    meetingPics.setPicsUrl(x);
                    return meetingPics;
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(meetingPicsList)) {
            meetingPicsMapper.insertList(
                    meetingPicsList
            );
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/updateMeetingRoom", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> updateMeetingRoom(
            @RequestBody Map<String, Object> extraInfo
    ) {
        Long id = Long.valueOf((Integer)extraInfo.get("id"));
        String name = (String)extraInfo.get("name");
        String place = (String)extraInfo.get("place");
        Integer size = (Integer)extraInfo.get("size");
        String type = (String)extraInfo.get("type");
        String memo = (String)extraInfo.get("memo");
        MeetingRoomDetail meetingRoomDetail = new MeetingRoomDetail();
        meetingRoomDetail.setId(id);
        meetingRoomDetail.setName(name);
        meetingRoomDetail.setPlace(place);
        meetingRoomDetail.setSize(size);
        meetingRoomDetail.setType(type);
        meetingRoomDetail.setMemo(memo);

        meetingRoomDetailMapper.updateByPrimaryKey(meetingRoomDetail);

        // 删除其它字段，再重新插入
        meetingInChargeMapper.delMeetingInCharge(id);
        meetingMediaInChargeMapper.delMeetingMediaInCharge(id);
        meetingPicsMapper.delMeetingPics(id);

        List<String> inChargeList = (List<String>)extraInfo.get("inCharge");
        List<String> mediaInChargeList = (List<String>)extraInfo.get("mediaInCharge");
        List<String> picsList = (List<String>)extraInfo.get("pics");


        meetingInChargeMapper.insertList(
                inChargeList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                            MeetingInCharge meetingInCharge = new MeetingInCharge();
                            meetingInCharge.setUserId(x);
                            meetingInCharge.setMeetingRoomId(meetingRoomDetail.getId());
                            return meetingInCharge;
                        }).collect(Collectors.toList())
        );
        meetingMediaInChargeMapper.insertList(
                mediaInChargeList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                            MeetingMediaInCharge meetingMediaInCharge = new MeetingMediaInCharge();
                            meetingMediaInCharge.setUserId(x);
                            meetingMediaInCharge.setMeetingRoomId(meetingRoomDetail.getId());
                            return meetingMediaInCharge;
                        }).collect(Collectors.toList())
        );
        List<MeetingPics> meetingPicsList = picsList.stream()
                .filter(x -> StringUtils.isNotBlank(x))
                .map(x -> {
                    MeetingPics meetingPics = new MeetingPics();
                    meetingPics.setMeetingRoomId(meetingRoomDetail.getId());
                    meetingPics.setPicsUrl(x);
                    return meetingPics;
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(meetingPicsList)) {
            meetingPicsMapper.insertList(
                    meetingPicsList
            );
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/getDeptList", method = {RequestMethod.GET})
    public RestResponse<List<OapiDepartmentListResponse.Department>> getDeptList(
            @RequestParam(required = false, defaultValue = "1") String deptId,
            @RequestParam(required = false, defaultValue = "true") Boolean fetchChild
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptList(deptId, fetchChild));
    }

    @RequestMapping(value = "/getDeptListLocal", method = {RequestMethod.GET})
    public RestResponse<List<DeptListLocal>> getDeptListLocal() {
        return RestResponse.getSuccesseResponse(deptListLocalMapper.select(new DeptListLocal()));
    }

    @RequestMapping(value = "/getDeptUserSimpleList", method = {RequestMethod.GET})
    public RestResponse<List<OapiUserSimplelistResponse.Userlist>> getDeptUserSimpleList(
            @RequestParam(required = false, defaultValue = "1") Long deptId,
            @RequestParam(required = false, defaultValue = "0") Long pageStart,
            @RequestParam(required = false, defaultValue = "100") Long pageSize
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptUserSimpleList(deptId, pageStart, pageSize));
    }

    @RequestMapping(value = "/getDeptDetail", method = {RequestMethod.GET})
    public RestResponse<OapiDepartmentGetResponse> getDeptDetail(
            Long deptId
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptDetail(deptId));
    }

    @RequestMapping(value = "/getUserDetail", method = {RequestMethod.GET})
    public RestResponse<OapiUserGetWithDeptResponse> getUserDetail(
            String userId
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(cacheService.getUserDetail(userId));
    }

    @RequestMapping(value = "/getDeptUserList", method = {RequestMethod.GET})
    public RestResponse<List<String>> getDeptUserList(
            Long deptId
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptUserList(deptId));
    }

    @RequestMapping(value = "/getDeptUserListDetail", method = {RequestMethod.GET})
    public RestResponse<List<OapiUserListResponse.Userlist>> getDeptUserListDetail(
            Long deptId
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptUserListDetail(deptId));
    }

    @RequestMapping(value = "/getUserByCode", method = {RequestMethod.GET})
    public RestResponse<OapiUserGetuserinfoResponse> getUserByCode(
            String code
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getUserByCode(code));
    }

    @RequestMapping(value = "/addPrivilegedDept", method = {RequestMethod.POST})
    public RestResponse<Void> addPrivilegedDept(
            Long deptId
    ) {
        PrivilegedDept privilegedDept = new PrivilegedDept();
        privilegedDept.setDeptId(deptId);
        privilegedDeptMapper.insert(privilegedDept);
        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delPrivilegedDept", method = {RequestMethod.POST})
    public RestResponse<Void> delPrivilegedDept(
            Long deptId
    ) {
        privilegedDeptMapper.delPrivilegedDept(deptId);
        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/getPrivilegedDept", method = {RequestMethod.GET})
    public RestResponse<List<OapiDepartmentGetResponse>> getPrivilegedDept() {
        Example example2 = new Example(PrivilegedDept.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("isDeleted", false);
        List<PrivilegedDept> privilegedDeptList = privilegedDeptMapper.selectByExample(example2);
        return RestResponse.getSuccesseResponse(
                privilegedDeptList.stream().map(x -> {
                    try {
                        return dingService.getDeptDetail(x.getDeptId());
                    } catch (DingServiceException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }

                    return null;
                }).filter(z -> z != null).collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/addSystemRole", method = {RequestMethod.POST})
    public RestResponse<Void> addSystemRole(
            String userId,
            String role
    ) {
        SystemRoleType systemRoleType = SystemRoleType.valueOf(role);
        SysRole sysRole = new SysRole();
        sysRole.setUserId(userId);
        sysRole.setRole(systemRoleType.name());
        // check if exist
        List<SysRole> existSysRoleList = sysRoleMapper.select(sysRole);
        if (CollectionUtils.isEmpty(existSysRoleList)) {
            sysRoleMapper.insert(sysRole);
        } else {
            logger.warn("user {} role {} exist", userId, systemRoleType.name());
        }

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delSystemRole", method = {RequestMethod.POST})
    public RestResponse<Void> delSystemRole(
            String userId,
            String role
    ) {
        sysRoleMapper.delSystemRole(userId, role);
        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/getSystemRole", method = {RequestMethod.GET})
    public RestResponse<List<OapiUserGetWithDeptResponse>> getSystemRole(
            String role
    ) {
        Example example2 = new Example(SysRole.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("isDeleted", false);
        criteria2.andEqualTo("role", role);
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(example2);
        return RestResponse.getSuccesseResponse(
                sysRoleList.stream().map(x -> {
                    try {
                        return cacheService.getUserDetail(x.getUserId());
                    } catch (DingServiceException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }

                    return null;
                }).filter(z -> z != null).collect(Collectors.toList())
        );
    }

    @RequestMapping(value = "/getSignParam", method = {RequestMethod.GET})
    public RestResponse<Map<String, Object>> getSignParam(
            String url
    ) throws DingTalkEncryptException, ExecutionException, ApiException, DingServiceException {
        String jsApiTicket = dingService.getJsApiTicket().getTicket();
        String nonceStr = UUID.randomUUID().toString();
        Long timeStamp = System.currentTimeMillis();
        String sign = DingTalkJsApiSingnature.getJsApiSingnature(url, nonceStr, timeStamp, jsApiTicket);
        Map<String, Object> signMap = new TreeMap<>();
        signMap.put("url", url);
        signMap.put("nonceStr", nonceStr);
        signMap.put("timeStamp", timeStamp);
        signMap.put("signature", sign);
        return RestResponse.getSuccesseResponse(signMap);
    }

    @RequestMapping(value = "/getUserRole", method = {RequestMethod.GET})
    public RestResponse<JSONObject> getUserRole(
            String userId
    ) {
        Set<String> userRoles = new TreeSet<>();
        Example example2 = new Example(SysRole.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("isDeleted", false);
        criteria2.andEqualTo("userId", userId);
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(example2);
        userRoles.addAll(
                sysRoleList.stream().map(x -> x.getRole()).collect(Collectors.toList())
        );

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sysRole", userRoles);

        Example example3 = new Example(MeetingInCharge.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("isDeleted", false);
        criteria3.andEqualTo("userId", userId);
        List<MeetingInCharge> meetingInChargeList =  meetingInChargeMapper.selectByExample(example3);

        jsonObject.put("meeingInCharge", meetingInChargeList.stream().map(x -> x.getMeetingRoomId()).collect(Collectors.toSet()));

        Example example4 = new Example(MeetingMediaInCharge.class);
        Example.Criteria criteria4 = example4.createCriteria();
        criteria4.andEqualTo("isDeleted", false);
        criteria4.andEqualTo("userId", userId);
        List<MeetingMediaInCharge> meetingMediaInChargeList =  meetingMediaInChargeMapper.selectByExample(example4);

        jsonObject.put("meeingMediaInCharge", meetingMediaInChargeList.stream().map(x -> x.getMeetingRoomId()).collect(Collectors.toSet()));

        RepairGroup repairGroup = new RepairGroup();
        repairGroup.setSupervisorUserId(userId);
        jsonObject.put("repairGroup", repairGroupMapper.select(repairGroup).stream().map(x -> x.getRepairType()).collect(Collectors.toSet()));

        return RestResponse.getSuccesseResponse(jsonObject);
    }

    @RequestMapping(value = "/sendNotificationToUsers", method = {RequestMethod.POST})
    public RestResponse<Void> sendNotificationToUsers(
            String userIds
    ) throws ExecutionException, ApiException, DingServiceException, UnsupportedEncodingException {
        if (StringUtils.isBlank(userIds)) {
            return RestResponse.getFailedResponse(Constants.RcError, "待发送钉钉通知的用户列表为空");
        }

        dingService.sendNotificationToUser(
                meetingBookAppAgentId,
                Arrays.asList(userIds.split(",")),
                "会议室预约审批通知",
                "有一个新的会议室预约申请，请前往处理",
                "http://10.0.0.7:8020/DingApp/redirect.html?action="
        );
        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/querySendResultAll", method = {RequestMethod.GET})
    public RestResponse<JSONObject> querySendResultAll(
            Long taskId,
            Long agentId
    ) throws ExecutionException, ApiException, DingServiceException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("progress", dingService.getSendProgress(taskId, agentId));
        jsonObject.put("result", dingService.getSendResult(taskId, agentId));

        return RestResponse.getSuccesseResponse(jsonObject);
    }

    @RequestMapping(value = "/addRepairType", method = {RequestMethod.POST})
    public RestResponse<Void> addRepairType(
            String repairType
    ) {
        RepairType repairType1 = new RepairType();
        repairType1.setRepairType(repairType);
        repairTypeMapper.insert(repairType1);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delRepairType", method = {RequestMethod.POST})
    public RestResponse<Void> delRepairType(
            Long id
    ) {
        repairTypeMapper.delRepairType(id);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/addRepairSubType", method = {RequestMethod.POST})
    public RestResponse<Void> addRepairType(
            Long repairTypeId,
            String repairSubTypes
    ) {
        List<RepairSubType> repairSubTypeList  =
        Stream.of(repairSubTypes.split(",", -1))
                .filter(x -> StringUtils.isNotBlank(x))
                .map(x -> {
                    RepairSubType repairSubType1 = new RepairSubType();
                    repairSubType1.setRepairTypeId(repairTypeId);
                    repairSubType1.setRepairSubType(x);
                    return repairSubType1;
                }).collect(Collectors.toList());

        repairSubTypeMapper.insertList(repairSubTypeList);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delRepairSubType", method = {RequestMethod.POST})
    public RestResponse<Void> delRepairSubType(
            Long id
    ) {
        repairSubTypeMapper.delRepairSubType(id);
        return RestResponse.getSuccesseResponse();
    }


    @RequestMapping(value = "/addRepairGroup", method = {RequestMethod.POST})
    public RestResponse<Void> addRepairGroup(
            Long repairTypeId,
            String userId
    ) {
        RepairGroup repairGroup = new RepairGroup();
        repairGroup.setRepairType(repairTypeId);
        repairGroup.setSupervisorUserId(userId);
        repairGroupMapper.insert(repairGroup);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delRepairGroupForUser", method = {RequestMethod.POST})
    public RestResponse<Void> delRepairGroupForUser(
            Long repairTypeId,
            String userId
    ) {
        repairGroupMapper.delRepairGroupForUser(repairTypeId, userId);
        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/addRepairManGroup", method = {RequestMethod.POST})
    public RestResponse<Void> addRepairManGroup(
            Long repairTypeId,
            String name,
            String phone
    ) {
        if (StringUtils.isBlank(name)) {
            return RestResponse.getFailedResponse(Constants.RcError, "维修人员姓名不能为空");
        }

        RepairManGroup repairManGroup = new RepairManGroup();
        repairManGroup.setRepairType(repairTypeId);
        repairManGroup.setName(name);
        repairManGroup.setPhone(phone);

        repairManGroupMapper.insert(repairManGroup);

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/delRepairManGroup", method = {RequestMethod.POST})
    public RestResponse<Void> delRepairManGroup(
            Long id
    ) {
        repairManGroupMapper.delRepairManGroup(id);
        return RestResponse.getSuccesseResponse();
    }
}
