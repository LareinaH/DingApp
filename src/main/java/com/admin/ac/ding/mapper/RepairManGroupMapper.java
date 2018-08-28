package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.RepairManGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface RepairManGroupMapper extends BaseMapper<RepairManGroup> {
    @Update("update repair_man_group set is_deleted=1 where id=#{id} and is_deleted=0")
    void delRepairManGroup(@Param("id") Long id);
}