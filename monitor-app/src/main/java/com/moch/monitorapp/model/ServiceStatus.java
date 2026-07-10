package com.moch.monitorapp.model;

public enum ServiceStatus {
    UP,                 //  70%
    DEGRADED,            //  15%
    DOWN,               //  10%
    TIMEOUT,            //  5%
    UNKNOWN             //  This will act mostly as default value (uninitialized)
}
