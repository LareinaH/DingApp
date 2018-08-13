package com.admin.ac.ding.mapper;

import com.admin.ac.ding.base.BaseMapper;
import com.admin.ac.ding.model.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    @Update("update sys_role set is_deleted=1 where user_id=#{userId} and role=#{role} and is_deleted=0")
    void delSystemRole(
            @Param("userId") String userId,
            @Param("role") String role
    );
}