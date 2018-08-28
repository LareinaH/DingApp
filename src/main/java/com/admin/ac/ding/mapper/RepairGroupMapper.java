package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.RepairGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface RepairGroupMapper extends BaseMapper<RepairGroup> {
    @Update("update repair_group set is_deleted=1 where repair_type=#{repairTypeId} and supervisor_user_id=#{userId} and is_deleted=0")
    void delRepairGroupForUser(@Param("repairTypeId") Long repairTypeId, @Param("userId") String userId);
}