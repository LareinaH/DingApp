package com.admin.ac.ding.controller;

import com.admin.ac.ding.enums.SystemRoleType;
import com.admin.ac.ding.exception.DingServiceException;
import com.admin.ac.ding.mapper.*;
import com.admin.ac.ding.model.*;
import com.admin.ac.ding.service.DingService;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
                                return dingService.getUserDetail(y.getUserId());
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
                                return dingService.getUserDetail(y.getUserId());
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

    @RequestMapping(value = "/addMeetingRoom", method = {RequestMethod.POST})
    @Transactional
    public RestResponse<Void> addMeetingRoom(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "place") String place,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "memo") String memo,
            @RequestBody Map<String, Object> extraInfo
    ) {
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

        meetingPicsMapper.insertList(
                picsList.stream()
                        .filter(x -> StringUtils.isNotBlank(x))
                        .map(x -> {
                    MeetingPics meetingPics = new MeetingPics();
                    meetingPics.setMeetingRoomId(meetingRoomDetail.getId());
                    meetingPics.setPicsUrl(x);
                    return meetingPics;
                }).collect(Collectors.toList())
        );

        return RestResponse.getSuccesseResponse();
    }

    @RequestMapping(value = "/getDeptList", method = {RequestMethod.GET})
    public RestResponse<List<OapiDepartmentListResponse.Department>> getDeptList(
            @RequestParam(required = false, defaultValue = "1") String deptId,
            @RequestParam(required = false, defaultValue = "true") Boolean fetchChild
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptList(deptId, fetchChild));
    }

    @RequestMapping(value = "/getDeptDetail", method = {RequestMethod.GET})
    public RestResponse<OapiDepartmentGetResponse> getDeptDetail(
            Long deptId
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getDeptDetail(deptId));
    }

    @RequestMapping(value = "/getUserDetail", method = {RequestMethod.GET})
    public RestResponse<OapiUserGetResponse> getUserDetail(
            String userId
    ) throws ApiException, ExecutionException, DingServiceException {
        return RestResponse.getSuccesseResponse(dingService.getUserDetail(userId));
    }

    @RequestMapping(value = "/getDeptUserList", method = {RequestMethod.GET})
    public RestResponse<List<Long>> getDeptUserList(
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
        sysRoleMapper.insert(sysRole);
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
    public RestResponse<List<OapiUserGetResponse>> getSystemRole(
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
                        return dingService.getUserDetail(x.getUserId());
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
}
