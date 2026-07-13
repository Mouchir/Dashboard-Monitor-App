package com.moch.monitorapp.ressources;

import com.moch.monitorapp.model.MonitoredService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This Class contains Metadata about APIs and Services (name, description, URL ...)
 * This Class is a Singleton*/

public final class MetaData {
    
    private static final MetaData instance = new MetaData();
    private static final List<MonitoredService> monitoredServiceList = new ArrayList<>();
    
    private MetaData(){
        try{
            loadServices();
        }
        catch(MalformedURLException e){
            System.out.println("URL Exception ???" + e);
        }
    }
    
    public static MetaData getInstance(){
        return MetaData.instance;
    }
    
    public static List<MonitoredService> getMonitoredServicesList(){
        return Collections.unmodifiableList(monitoredServiceList);
    }
    
    private static void loadServices() throws MalformedURLException {

        monitoredServiceList.add(
                new MonitoredService("API-COMPTE","Gestion des comptes courants",
                    new URL("http://preprod-api-01:8081/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-VIREMENTS","Transferts d'argent",
                        new URL("http://preprod-api-02:8082/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-CARTES","Gestion des cartes bancaires",
                        new URL("http://preprod-api-03:8083/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-AUTH","Authentification des utilisateurs",
                        new URL("http://preprod-api-04:8084/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-HISTORIQUE","Historique des transactions",
                        new URL("http://preprod-api-05:8085/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-PAIEMENT","Traitement des paiements",
                        new URL("http://preprod-api-06:8086/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-FRAUDE","Détection des fraudes",
                        new URL("http://preprod-api-07:8087/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-CLIENTS","Gestion des clients",
                        new URL("http://preprod-api-08:8088/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-CREDIT","Simulation de crédits",
                        new URL("http://preprod-api-09:8089/actuator/health")));

        monitoredServiceList.add(
                new MonitoredService("API-NOTIFICATION","Envoi de notifications",
                        new URL("http://preprod-api-10:8090/actuator/health")));
        
    }
    
}
