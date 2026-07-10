package com.moch.monitorapp.service;

import com.moch.monitorapp.model.MonitoredService;

import java.util.ArrayList;
import java.util.List;

/**
 *  THIS CLASS IS SUPPOSED TO SIMULATE A MICRO-SERVICES ARCHITECTURE IN A MONOLITHIC WAY
 */

public class SimulatorService {

    private final List<MonitoredService> monitoredServices = new ArrayList<>();

    public SimulatorService(){      //  LOAD THE CONSTRUCTOR WITH DATA AND INJECT IT LATER ON !
        initServices();
    }

    private void initServices(){
        /*
        for(int i = 0 ; i < 10 ; i++)
            monitoredServices.add(new MonitoredService());

        */
    }

}
