package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.MeetingMediaInCharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MeetingMediaInChargeMapper extends BaseMapper<MeetingMediaInCharge> {
    @Update("update meeting_media_in_charge set is_deleted=1 where meeting_room_id=#{meetingRoomId} and is_deleted=0")
    void delMeetingMediaInCharge(@Param("meetingRoomId") Long meetingRoomId);
}