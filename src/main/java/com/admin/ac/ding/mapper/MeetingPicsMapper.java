package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.MeetingPics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MeetingPicsMapper extends BaseMapper<MeetingPics> {
    @Update("update meeting_pics set is_deleted=1 where meeting_room_id=#{meetingRoomId} and is_deleted=0")
    void delMeetingPics(@Param("meetingRoomId") Long meetingRoomId);
}