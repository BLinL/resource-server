package org.kk.resource_server.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.kk.resource_server.domain.Permission;
import org.kk.resource_server.domain.Role;
import org.kk.resource_server.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SysUser implements UserDetails {

    private String username;
    @JsonIgnore
    private String password;
    private List<Permission> permissionList;
    private List<Role> roleList;

    public SysUser(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.permissionList = user.getPermissionList();
        this.roleList = user.getRoleList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(permissionList)) {
            return new ArrayList<>();
        }
        Set<SimpleGrantedAuthority> permissions = permissionList.stream()
                .filter(p -> StringUtils.hasText(p.getPerm()))
                .map(p -> new SimpleGrantedAuthority(p.getPerm()))
                .collect(Collectors.toSet());

        Set<SimpleGrantedAuthority> rolePermissions = roleList.stream()
                .filter(r -> StringUtils.hasText(r.getRole()))
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole()))
                .collect(Collectors.toSet());
        permissions.addAll(rolePermissions);
        return permissions;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
