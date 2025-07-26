package org.kk.resource_server.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kk.resource_server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest()
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("查询")
    @Test
    public void selectUser() {
        Optional<User> notfound = userMapper.findByUserName("");
        assertNull(notfound.orElse(null));
    }

    @DisplayName("插入")
    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("张三");
        user.setUserCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode("123"));
        int rows = userMapper.save(user);
        assertEquals(1, rows);
    }
}