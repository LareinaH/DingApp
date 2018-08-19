package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.MeetingInCharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MeetingInChargeMapper extends BaseMapper<MeetingInCharge> {
    @Update("update meeting_in_charge set is_deleted=1 where meeting_room_id=#{meetingRoomId} and is_deleted=0")
    void delMeetingInCharge(@Param("meetingRoomId") Long meetingRoomId);
}