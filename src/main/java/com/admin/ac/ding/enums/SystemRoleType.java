package com.admin.ac.ding.enums;

/**
 * MessageTypeEnum
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/6/1
 */
public enum SystemRoleType {
    CUSTOMER_SERVICE("接线员"),
    PRIVILEGED_PERSON("特权人"),
    SYS_ADMIN("系统管理员"),
    MEETING_USER("会议室预订联系人"),
    MEETING_BOOK_REVIEW("会议室申请审核员"),
    SUGGEST_ADMIN("意见建议管理员");

    private String displayName;

    SystemRoleType(String displayName){

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
