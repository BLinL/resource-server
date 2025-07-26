package org.kk.resource_server.security;

import org.junit.jupiter.api.Test;
import org.kk.resource_server.controller.ResourceConntroller;
import org.kk.resource_server.domain.vm.ResourceVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DaoUserDetailServiceTest {


    @Autowired
    private DaoUserDetailService daoUserDetailService;

    @Autowired
    private ResourceConntroller resourceConntroller;

    @Test
    @WithUserDetails(value = "张三", userDetailsServiceBeanName = "daoUserDetailService")
    public void test() {
        resourceConntroller.add(new ResourceVM());
    }

}