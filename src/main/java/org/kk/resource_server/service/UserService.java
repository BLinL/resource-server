package org.kk.resource_server.service;


import org.kk.resource_server.domain.Permission;
import org.kk.resource_server.domain.Role;
import org.kk.resource_server.domain.User;
import org.kk.resource_server.repository.PermissionMapper;
import org.kk.resource_server.repository.RoleMapper;
import org.kk.resource_server.repository.UserMapper;
import org.kk.resource_server.security.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapper;

    public Optional<SysUser> getLoginUserInfo(String username) {
        Optional<User> user = userMapper.findByUserName(username);
        if (user.isPresent()) {
            User u = user.get();
            List<Permission> permissions = permissionMapper.selectByUsername(username);
            u.setPermissionList(permissions);

            List<Role> roles = roleMapper.selectByUserId(u.getId());
            u.setRoleList(roles);

            SysUser sysUser = new SysUser(u);
            return Optional.of(sysUser);
        }
        return Optional.empty();

    }
}
