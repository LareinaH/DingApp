package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.RepairSubType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface RepairSubTypeMapper extends BaseMapper<RepairSubType> {
    @Update("update repair_sub_type set is_deleted=1 where id=#{id} and is_deleted=0")
    void delRepairSubType(@Param("id") Long id);
}