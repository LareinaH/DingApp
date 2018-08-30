package com.admin.ac.ding.enums;

/**
 * MessageTypeEnum
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/6/1
 */
public enum SuggestProcessStatus {
    WAIT_TRANSFER("待转发处理"),
    WAIT_REPLY("等待回复"),
    COMPLETE("完成");

    private String displayName;

    SuggestProcessStatus(String displayName){

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
