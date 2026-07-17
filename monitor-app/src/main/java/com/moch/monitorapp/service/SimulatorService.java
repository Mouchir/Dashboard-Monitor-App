package com.moch.monitorapp.service;

import com.moch.monitorapp.model.History;
import com.moch.monitorapp.model.MonitoredService;
import com.moch.monitorapp.model.ServiceStatus;
import com.moch.monitorapp.model.ServiceType;
import com.moch.monitorapp.ressources.ServiceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  THIS CLASS IS SUPPOSED TO SIMULATE A MICRO-SERVICES ARCHITECTURE IN A MONOLITHIC WAY
 */

@Service
public class SimulatorService {

    private final Map<ServiceType,MonitoredService> monitoredServices = new HashMap<>();
    private final ServiceLoader serviceLoader;
    
    public SimulatorService(ServiceLoader metaData){      //  LOAD THE CONSTRUCTOR WITH DATA AND INJECT IT LATER ON !
        this.serviceLoader = metaData;
        initServices();
    }

    private void initServices(){
        ServiceType[] tempServiceType = ServiceType.values();   //  extracts an array from the enum in an ordered way
        List<MonitoredService> tempList = serviceLoader.getMonitoredServicesList();
        
        for(int i = 0 ; i < tempList.size() ; i++)
            monitoredServices.put(tempServiceType[i],tempList.get(i));
    }
    
    //  API FOR MAINCONTROLLER

    public List<MonitoredService>getFullServicesStatus(){
        return serviceLoader.getMonitoredServicesList();
    }

    public List<MonitoredService>findServicesByStatus(ServiceStatus serviceStatus){
        if(serviceStatus == null)
            return null;

        List<MonitoredService> serviceList = new ArrayList<>();
        for(MonitoredService monitoredService : serviceLoader.getMonitoredServicesList())
            if(monitoredService.getStatus() == serviceStatus)
                serviceList.add(monitoredService);

        return Collections.unmodifiableList(serviceList);
    }

    public MonitoredService findServiceByName(String name){
        if(name == null || name.isEmpty())
            return null;
        
        for(MonitoredService monitoredService : serviceLoader.getMonitoredServicesList())
            if(monitoredService.getName().equalsIgnoreCase(name))
                return monitoredService;
        
        return null;
    }

    public Deque<History> fetchServiceHistory(String name){
        MonitoredService monitoredService = findServiceByName(name);
        if(monitoredService == null)
            return null;
        
        return monitoredService.getHistoryQueue();
    }

    public Map<ServiceStatus,Double> fetchStatisticsAboutServices(){
        
        Map<ServiceStatus,Double> statsMap = new LinkedHashMap<>(); //  this maintains order + gives us Map behavior of Keys and Values
        List<MonitoredService> tempList = serviceLoader.getMonitoredServicesList();

        if(tempList == null || tempList.isEmpty())
            return null;
        
        statsMap.put(ServiceStatus.UP,0d);
        statsMap.put(ServiceStatus.DEGRADED,0d);
        statsMap.put(ServiceStatus.DOWN,0d);
        statsMap.put(ServiceStatus.TIMEOUT,0d);

        ServiceStatus tempServiceStatus;
        
        for(MonitoredService monitoredService : tempList){
            tempServiceStatus = monitoredService.getStatus();
            switch(tempServiceStatus){
                case UP :
                    statsMap.put(ServiceStatus.UP,
                            statsMap.get(ServiceStatus.UP) + 1);
                    break;
                case DEGRADED :
                    statsMap.put(ServiceStatus.DEGRADED,
                            statsMap.get(ServiceStatus.DEGRADED) + 1);
                    break;
                case DOWN :
                    statsMap.put(ServiceStatus.DOWN,
                            statsMap.get(ServiceStatus.DOWN) + 1);
                    break;
                case TIMEOUT:
                    statsMap.put(ServiceStatus.TIMEOUT,
                            statsMap.get(ServiceStatus.TIMEOUT) + 1);
                    break;
                default :   //  might produce undefined behavior incase of unknown/null ???
                    System.out.println("Uninitiliazed ServiceStatus ??? ---> " + tempServiceStatus);
            }
        }
        int numberOfServices = tempList.size();
        for(ServiceStatus status : statsMap.keySet())
            statsMap.put(status, (statsMap.get(status) / numberOfServices) * 100);
        
        return statsMap;
    }
    
    //  -----------------------------------------------

    @Scheduled(fixedRate = 10_000)     //  120 s --> 2 min
    public void updateAllMonitoredServices(){
        for(MonitoredService monitoredService : serviceLoader.getMonitoredServicesList())
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
