package com.admin.ac.ding.model;

import com.dingtalk.api.response.OapiUserGetResponse;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoomDetailVO extends MeetingRoomDetail {
    List<OapiUserGetResponse> inCharge = new ArrayList<>();
    List<OapiUserGetResponse> mediaInCharge = new ArrayList<>();
    List<String> pics = new ArrayList<>();

    public List<OapiUserGetResponse> getInCharge() {
        return inCharge;
    }

    public void setInCharge(List<OapiUserGetResponse> inCharge) {
        this.inCharge = inCharge;
    }

    public List<OapiUserGetResponse> getMediaInCharge() {
        return mediaInCharge;
    }

    public void setMediaInCharge(List<OapiUserGetResponse> mediaInCharge) {
        this.mediaInCharge = mediaInCharge;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}