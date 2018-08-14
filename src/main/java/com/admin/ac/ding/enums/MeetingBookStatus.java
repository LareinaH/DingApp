package com.admin.ac.ding.enums;

/**
 * MessageTypeEnum
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/6/1
 */
public enum MeetingBookStatus {

    WAIT_APPROVE("等待审核"),
    USER_CANCEL("用户取消"),
    DENY("申请被拒绝"),
    AGREE("申请通过"),
    ADMIN_CANCEL("管理员取消");

    private String displayName;

    MeetingBookStatus(String displayName){

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
