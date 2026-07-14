package com.moch.monitorapp.controller;


import com.moch.monitorapp.model.History;
import com.moch.monitorapp.model.MonitoredService;
import com.moch.monitorapp.model.ServiceStatus;
import com.moch.monitorapp.service.SimulatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Deque;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {
    
    private final SimulatorService simulatorService;

    public MainController(SimulatorService simulatorService){
        this.simulatorService = simulatorService;
    }
    
    //  DEVELOP THESE LATER ON  !!!
    
    @GetMapping("/services")
    public ResponseEntity<List<MonitoredService>> getFullServiceStatus(){

        List<MonitoredService> tempList = simulatorService.getFullServicesStatus();
        if(tempList == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(tempList);
    }

    @GetMapping("/services/status")
    public ResponseEntity<List<MonitoredService>> findServicesByStatus(@RequestParam ServiceStatus status){
        
        List<MonitoredService> tempList = simulatorService.findServicesByStatus(status);
        if(tempList == null || tempList.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(tempList);
    }

    @GetMapping("/services/{name}")
    public ResponseEntity<MonitoredService> getServiceDetails(@PathVariable String name){
        MonitoredService monitoredService = simulatorService.findServiceByName(name);

        if(monitoredService == null )
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(monitoredService);
    }

    @GetMapping("/services/{name}/history")
    public ResponseEntity<Deque<History>> getServiceHistory(@PathVariable String name){
        Deque<History> tempDeque = simulatorService.fetchServiceHistory(name);
        if(tempDeque == null || tempDeque.isEmpty())
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(tempDeque);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<ServiceStatus,Double>> getGlobalStatistics(){
        Map<ServiceStatus,Double> statsMap = simulatorService.fetchStatisticsAboutServices();
        
        if(statsMap == null || statsMap.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(statsMap);
    }
    
}
