package org.kk.resource_server.controller;


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

    @PreAuthorize("hasAuthority('SCOPE_resource:write')") // 注意加SCOPE_前缀
    @PostMapping("")
    public String add(@RequestBody  ResourceVM resourceVM){
        resource.add(resourceVM.getName());
        return "success";
    }


    static class ResourceVM {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
