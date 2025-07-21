package org.kk.resource_server.repository;

import org.kk.resource_server.domain.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {

    Permission selectById(@Param("id") Integer id);

    List<Permission> selectByUsername(String username);


    List<Permission> selectAll();

    int insert(Permission permission);

    int update(Permission permission);

    int deleteById(@Param("id") Integer id);

}
