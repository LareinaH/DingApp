package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.PrivilegedDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface PrivilegedDeptMapper extends BaseMapper<PrivilegedDept> {
    @Update("update privileged_dept set is_deleted=1 where dept_id=#{deptId} and is_deleted=0")
    void delPrivilegedDept(@Param("deptId") Long deptId);
}