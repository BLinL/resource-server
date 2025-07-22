package org.kk.resource_server.controller;


import org.kk.resource_server.domain.vm.ResourceVM;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/resource")
public class ResourceConntroller {

    private CopyOnWriteArrayList resource = new CopyOnWriteArrayList();


    @GetMapping("")
    public Collection get(){
        return resource;
    }

    @PreAuthorize("hasAuthority('resource:write')") //
//    @PreAuthorize("hasRole('ROLE_admin')") // 注意加ROLE_前缀
    @PostMapping("")
    public String add(@RequestBody ResourceVM resourceVM){
        resource.add(resourceVM.getName());
        return "success";
    }


}
