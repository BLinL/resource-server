package org.kk.resource_server.repository;

import org.kk.resource_server.domain.User;

import java.util.Optional;

public interface UserMapper {

    Optional<User> findByUserName(String username);
    int save(User user);
}
