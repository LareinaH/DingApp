package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.RepairType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface RepairTypeMapper extends BaseMapper<RepairType> {
    @Update("update repair_type set is_deleted=1 where id=#{id} and is_deleted=0")
    void delRepairType(@Param("id") Long id);
}