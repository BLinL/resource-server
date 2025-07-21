package org.kk.resource_server.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.kk.resource_server.domain.RolePerm;

import java.util.List;

@Mapper
public interface RolePermMapper {

    RolePerm selectById(@Param("id") Integer id);

    List<RolePerm> selectAll();

    int insert(RolePerm rolePerm);

    int update(RolePerm rolePerm);

    int deleteById(@Param("id") Integer id);
}
