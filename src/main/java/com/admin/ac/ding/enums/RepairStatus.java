package com.admin.ac.ding.enums;

/**
 * MessageTypeEnum
 *
 * @author lareina_h
 * @version 1.0
 * @date 2018/6/1
 */
public enum RepairStatus {
    WAIT_CONFIRM("等待维修班组长确认"),
    WAIT_REPAIR("等待维修"),
    REPAIR_COMPLETE("维修完成,等待评价"),
    ORDER_COMPLETE("维修单结束");

    private String displayName;

    RepairStatus(String displayName){

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
