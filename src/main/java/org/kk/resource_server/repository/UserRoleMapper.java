package org.kk.resource_server.mapper;

import org.kk.resource_server.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    UserRole selectById(@Param("id") Integer id);

    List<UserRole> selectAll();

    int insert(UserRole userRole);

    int update(UserRole userRole);

    int deleteById(@Param("id") Integer id);
}
