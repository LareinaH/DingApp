package com.admin.ac.ding.model;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MeetingRoomDetailVO extends MeetingRoomDetail {
    List<OapiUserGetWithDeptResponse> inCharge = new ArrayList<>();
    List<OapiUserGetWithDeptResponse> mediaInCharge = new ArrayList<>();
    List<String> pics = new ArrayList<>();
    Map<String, JSONObject> ordered = new TreeMap<>();

    public List<OapiUserGetWithDeptResponse> getInCharge() {
        return inCharge;
    }

    public void setInCharge(List<OapiUserGetWithDeptResponse> inCharge) {
        this.inCharge = inCharge;
    }

    public List<OapiUserGetWithDeptResponse> getMediaInCharge() {
        return mediaInCharge;
    }

    public void setMediaInCharge(List<OapiUserGetWithDeptResponse> mediaInCharge) {
        this.mediaInCharge = mediaInCharge;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public Map<String, JSONObject> getOrdered() {
        return ordered;
    }

    public void setOrdered(Map<String, JSONObject> ordered) {
        this.ordered = ordered;
    }
}
