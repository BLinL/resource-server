package org.kk.resource_server.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.kk.resource_server.domain.Permission;
import org.kk.resource_server.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SysUser implements UserDetails {

    private String username;
    @JsonIgnore
    private String password;
    private List<Permission> permissionList;

    public SysUser(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.permissionList = user.getPermissionList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(permissionList)) {
            return new ArrayList<>();
        }
        return permissionList.stream()
                .filter(p -> StringUtils.hasText(p.getPerm()))
                .map(p -> new SimpleGrantedAuthority(p.getPerm()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return username;
    }

    @Override
    public String getUsername() {
        return password;
    }
}
