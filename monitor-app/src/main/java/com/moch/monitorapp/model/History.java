package com.moch.monitorapp.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class History {

    private String name;
    private LocalDateTime timeStamp;

    private ServiceStatus serviceStatus;
    private HttpStatus httpStatus;
    private Long responseTime;

    public History(MonitoredService monitoredService){

        this.name = monitoredService.getName();
        this.timeStamp = LocalDateTime.now();
        
        this.serviceStatus = monitoredService.getStatus();
        this.httpStatus = monitoredService.getHttpStatus();
        this.responseTime = monitoredService.getResponseTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
}
