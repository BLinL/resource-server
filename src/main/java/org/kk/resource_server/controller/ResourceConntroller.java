package org.kk.resource_server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceConntroller {

    @GetMapping("/resource")
    public String resource(){
        return "this is project resource";
    }
}
