package com.moch.monitorapp.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {
    
    //  DEVELOP THESE LATER ON  !!!
    
    @GetMapping("/services")
    public void getFullServiceStatus(){

    }

    @GetMapping("/services/status/{status}")
    public void findServicesByStatus(){

    }

    @GetMapping("/services/{name}")
    public void getServiceDetails(){

    }

    @GetMapping("/services/{name}/history")
    public void getServiceHistory(){

    }

    @GetMapping("/stats")
    public void getGlobalStatistics(){

    }

    
}
