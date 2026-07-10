package com.moch.monitorapp.model;

import org.springframework.http.HttpStatus;

import java.net.URL;
import java.time.LocalDateTime;

public class MonitoredService {

    private String name;
    private String description;
    private URL url;
    private LocalDateTime timeStamp;

    private ServiceStatus status;
    private HttpStatus httpStatus;
    private Long responseTime;        // between 50 and 2000 ms

    //  ADD HISTORY HERE

    public MonitoredService(String name, String description, URL url, LocalDateTime timeStamp) {

        checkParameterValidity(name,description,url,timeStamp);

        this.name = name;
        this.description = description;
        this.url = url;
        this.timeStamp = timeStamp;
        
        this.status = ServiceStatus.UNKNOWN;
        this.httpStatus = HttpStatus.OK;
        this.responseTime = null;
    }

    private void checkParameterValidity(String name, String description, URL url, LocalDateTime timeStamp){
        this.setName(name);
        this.setDescription(description);
        this.setUrl(url);
        this.setTimeStamp(timeStamp);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null)
            throw new NullPointerException("Name cannot be null");
        
        if(name.isBlank())
            throw new IllegalArgumentException("Name cannot be blank");
        
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description == null)
            throw new NullPointerException("Description cannot be null");

        if(description.isBlank())
            throw new IllegalArgumentException("Description cannot be blank");

        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        if(url == null)
            throw new NullPointerException("URL cannot be null");
        this.url = url;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        if(status == null)
            throw new NullPointerException("ServiceStatus Cannot be null");
        
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        if(httpStatus == null)
            throw new NullPointerException("HttpStatus Cannot be null");

        this.httpStatus = httpStatus;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        if(responseTime == null)
            throw new NullPointerException("ResponseTime Cannot be null");
        if(responseTime < 0 )
            throw new IllegalArgumentException("ResponseTime Cannot be of negative value (responesTime = "+responseTime+" ms)");
        
        this.responseTime = responseTime;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        if(timeStamp == null)
            throw new NullPointerException("TimeStamp Cannot be null");
        
        this.timeStamp = timeStamp;
    }
}
