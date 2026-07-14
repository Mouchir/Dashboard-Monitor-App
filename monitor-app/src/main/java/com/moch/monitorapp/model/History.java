package com.moch.monitorapp.model;

import java.time.LocalDateTime;

public class History {

    private String name;
    private LocalDateTime timeStamp;

    private ServiceStatus serviceStatus;
    private int httpStatusCode;
    private Long responseTime;

    public History(MonitoredService monitoredService){

        this.name = monitoredService.getName();
        this.timeStamp = LocalDateTime.now();
        
        this.serviceStatus = monitoredService.getStatus();
        this.httpStatusCode = monitoredService.getHttpStatusCode();
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

    public int getHttpStatus() {
        return httpStatusCode;
    }

    public void setHttpStatus(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
}
