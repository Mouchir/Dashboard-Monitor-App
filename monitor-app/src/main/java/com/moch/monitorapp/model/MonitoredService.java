package com.moch.monitorapp.model;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

public class MonitoredService {

    private String name;
    private String description;
    private URL url;

    private ServiceStatus status;
    private int httpStatusCode;
    private Long responseTime;        // between 50 and 2000 ms
    private final Deque<History> historyQueue = new ArrayDeque<>();
    

    public MonitoredService(String name, String description, URL url) {

        checkParameterValidity(name,description,url);

        this.name = name;
        this.description = description;
        this.url = url;
        
        this.status = ServiceStatus.UNKNOWN;
        this.httpStatusCode = 200;  // OK STATUS
        this.responseTime = null;
    }

    private void checkParameterValidity(String name, String description, URL url){
        this.setName(name);
        this.setDescription(description);
        this.setUrl(url);
    }
    
    public void updateHistory(History newHistory){

        historyQueue.addLast(newHistory);

        if(historyQueue.size() > 15)
            historyQueue.removeFirst();
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

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        if(httpStatusCode < 0)
            throw new IllegalArgumentException("httpStatusCode Cannot be of negative value (httpStatusCode = "+httpStatusCode+" ms)");

        this.httpStatusCode = httpStatusCode;
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

    public Deque<History> getHistoryQueue() {
        return new ArrayDeque<>(historyQueue);
    }
}
