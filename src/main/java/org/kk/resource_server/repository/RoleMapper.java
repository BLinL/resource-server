package org.kk.resource_server.repository;

import org.apache.ibatis.annotations.Param;
import org.kk.resource_server.domain.Role;

import java.util.List;

public interface RoleMapper {

    Role selectById(@Param("id") Long id);
    List<Role> selectByUserId(@Param("userId") Long userId);

    List<Role> selectAll();

    int insert(Role role);

    int update(Role role);

    int deleteById(@Param("id") Integer id);
}
