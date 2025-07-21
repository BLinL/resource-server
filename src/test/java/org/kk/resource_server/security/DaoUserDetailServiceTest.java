package org.kk.resource_server.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DaoUserDetailServiceTest {


    @Autowired
    private DaoUserDetailService daoUserDetailService;


//    @Test
//    public void test() {
//        UserDetails userDetails = daoUserDetailService.loadUserByUsername("张三");
//        assertEquals(1, userDetails.getAuthorities().size());
//    }

}