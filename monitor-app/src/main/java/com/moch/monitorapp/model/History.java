package com.moch.monitorapp.model;

import org.springframework.http.HttpStatus;

import java.net.URL;
import java.time.LocalDateTime;

public class History {
    
    public History(String name, String description, URL url, LocalDateTime timeStamp,
                   ServiceStatus serviceStatus, HttpStatus httpStatus, Long responseTime){
        
    }

}
