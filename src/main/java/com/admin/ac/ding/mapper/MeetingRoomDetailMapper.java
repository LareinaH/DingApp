package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.MeetingRoomDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MeetingRoomDetailMapper extends BaseMapper<MeetingRoomDetail> {
    @Update("update meeting_room_detail set is_deleted=1 where id=#{id} and is_deleted=0")
    void delMeetingRoom(@Param("id") Long id);
}