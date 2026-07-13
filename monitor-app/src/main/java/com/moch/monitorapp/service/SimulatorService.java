package com.moch.monitorapp.service;

import com.moch.monitorapp.model.History;
import com.moch.monitorapp.model.MonitoredService;
import com.moch.monitorapp.model.ServiceStatus;
import com.moch.monitorapp.model.ServiceType;
import com.moch.monitorapp.ressources.MetaData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  THIS CLASS IS SUPPOSED TO SIMULATE A MICRO-SERVICES ARCHITECTURE IN A MONOLITHIC WAY
 */

public class SimulatorService {

    private final Map<ServiceType,MonitoredService> monitoredServices = new HashMap<>();

    public SimulatorService(){      //  LOAD THE CONSTRUCTOR WITH DATA AND INJECT IT LATER ON !
        initServices();
    }

    private void initServices(){
        ServiceType[] tempServiceType = ServiceType.values();   //  extracts an array from the enum in an ordered way
        List<MonitoredService> tempList = MetaData.getMonitoredServicesList();
        
        for(int i = 0 ; i < tempList.size() ; i++)
            monitoredServices.put(tempServiceType[i],tempList.get(i));
    }

    public void updateAllMonitoredServices(){
        for(MonitoredService monitoredService : MetaData.getMonitoredServicesList())
            simulate(monitoredService);
        
    }
    
    private void simulate(MonitoredService service){
        storePastActivity(service);
        ServiceStatus oldServiceStatus = service.getStatus();

        if(oldServiceStatus == ServiceStatus.UNKNOWN)    //  this is only to initiliaze the first iteration of the service
            service.setStatus(generateRandomStatus());
        
        else{
            ServiceStatus newServiceStatus = reattemptToReconnect(oldServiceStatus);
            service.setStatus(newServiceStatus);
        }
        service.setResponseTime(generateResponseTime(service.getStatus()));
        service.setHttpStatusCode(returnStatusAsInt(service.getStatus()));
    }

    private void storePastActivity(MonitoredService service){
        service.updateHistory(new History(service));
    }

    private ServiceStatus generateRandomStatus(){
        int randomNumber = ThreadLocalRandom.current().nextInt(100);
        if(randomNumber < 70)
            return ServiceStatus.UP;
        else if(randomNumber < 85)
            return ServiceStatus.DOWN;
        else if(randomNumber < 95)
            return ServiceStatus.DEGRADED;
        else 
            return ServiceStatus.TIMEOUT;
    }

    private long generateResponseTime(ServiceStatus serviceStatus){
        switch(serviceStatus){
            case UP:
                return ThreadLocalRandom.current().nextLong(251) + 50; // between 50 - 300 ms
            case DOWN:
                return ThreadLocalRandom.current().nextLong(601) + 300; // between 300 - 900 ms
            case DEGRADED:
                return ThreadLocalRandom.current().nextLong(1001) + 900; // between 900 - 2000 ms
            case TIMEOUT:
                return ThreadLocalRandom.current().nextLong(18000) + 2001; // between 2001 - 20000 ms
            default:
                throw new IllegalArgumentException("Cannot generate random response time with type : " + serviceStatus.toString());
        }
    }
    
    //  I added a more cases to simulate realistic
    private ServiceStatus reattemptToReconnect(ServiceStatus oldServiceStatus){    //  tries to pass to a random status
        int randomNumber = -1;
        
        if(oldServiceStatus == ServiceStatus.UP){
            randomNumber = ThreadLocalRandom.current().nextInt(100);
            if(randomNumber < 20)               //  20%
                return ServiceStatus.DEGRADED; 
            else if(randomNumber < 25)          //  5%
                return ServiceStatus.DOWN;
            else if(randomNumber < 30)          //  5%
                return ServiceStatus.TIMEOUT;
            else                                //  70%
                return ServiceStatus.UP;
        }

        else if(oldServiceStatus == ServiceStatus.DEGRADED){
            randomNumber = ThreadLocalRandom.current().nextInt(100);
            if(randomNumber < 50)               //  50%
                return ServiceStatus.UP;
            else if(randomNumber < 80)          //  30%
                return ServiceStatus.DEGRADED;
            else if(randomNumber < 95)          //  15%
                return ServiceStatus.DOWN;
            else                                //  5%
                return ServiceStatus.TIMEOUT;
        }

        else if(oldServiceStatus == ServiceStatus.DOWN){
            randomNumber = ThreadLocalRandom.current().nextInt(100);
            if(randomNumber < 30)               //  30%
                return ServiceStatus.UP;
            else if(randomNumber < 60)          //  30%
                return ServiceStatus.DOWN;
            else if(randomNumber < 90)          //  30%
                return ServiceStatus.DEGRADED;
            else                                //  10%
                return ServiceStatus.TIMEOUT;
        }

        else if(oldServiceStatus == ServiceStatus.TIMEOUT){
            randomNumber = ThreadLocalRandom.current().nextInt(100);
            if(randomNumber < 60)               //  60%
                return ServiceStatus.DEGRADED;
            else if(randomNumber < 80)          //  20%
                return ServiceStatus.DOWN;
            else if(randomNumber < 95)          //  15%
                return ServiceStatus.TIMEOUT;
            else                                //  5%
                return ServiceStatus.UP;
        }
        else
            throw new IllegalArgumentException("Cannot reattempt to reconnect with type : " + oldServiceStatus.toString());
    }

    private int returnStatusAsInt(ServiceStatus serviceStatus){
        switch(serviceStatus){
            case UP:
                return 200;
            case DEGRADED:
                return 429;
            case DOWN:
                return 500;
            case TIMEOUT:
                return 408;
            /*case UNKNOWN:
                return -1;*/
            default:
                throw new IllegalArgumentException("Unknown Status ??? --> " + serviceStatus.toString());
        }
    }

}
