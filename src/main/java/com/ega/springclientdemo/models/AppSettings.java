/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.models;

import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author sa
 */

@Component

public class AppSettings implements InitializingBean{
    
    @Value("${webclient.settings.server-path}")
    private String serverpath;
    
    @Value("${webclient.settings.logfilename}")
    private String logfile;
    
    @Value("${webclient.settings.loglevel}")
    private String loglevel;
    
    @Value("${webclient.settings.certs-path}")
    private String certspath;

    @Value("${webclient.settings.asic-store}")
    private String asicpath;

    @Value("${webclient.settings.ssl}")
    private boolean usessl;

    @Value("${webclient.settings.trust-store-path}")
    private String truststore;

    @Value("${webclient.settings.trust-store-password}")
    private String truststore_pass;

    //valuesMap={key1: '1', key2: '2', key3: '3'}
    //@Value("#{${valuesMap}}")
    //private Map<String, Integer> valuesMap;

    @Value("#{${webclient.settings.headers:{key:'1'}}}")
    private Map<String,String> headers;

    @Value("${trembita.service.xRoadInstance}")
    private String service_xRoadInstance;
    public static String SERVICE_X_ROAD_INSTANCE;

    @Value("${trembita.service.memberClass}")
    private String service_memberClass;
    public static String SERVICE_MEMBER_CLASS;

    @Value("${trembita.service.memberCode}")
    private String service_memberCode;
    public static String SERVICE_MEMBER_CODE;

    @Value("${trembita.service.subsystemCode}")
    private String service_subsystemCode;
    public static String SERVICE_SUBSYSTEM_CODE;

    @Value("${trembita.client.xRoadInstance}")
    private String client_xRoadInstance;
    public static String CLIENT_X_ROAD_INSTANCE;

    @Value("${trembita.client.memberClass}")
    private String client_memberClass;
    public static String CLIENT_MEMBER_CLASS;

    @Value("${trembita.client.memberCode}")
    private String client_memberCode;
    public static String CLIENT_MEMBER_CODE;

    @Value("${trembita.client.subsystemCode}")
    private String client_subsystemCode;
    public static String CLIENT_SUBSYSTEM_CODE;

    @Value("${trembita.client.endpoint}")
    private String client_endpoint;
    public static String CLIENT_ENDPOINT;

    
    public static String SERVER_PATH;
    
    public static String LOG_FILENAME;
    
    public static String LOG_LEVEL;

    public static String CERTS_PATH;

    public static String ASIC_PATH;

    public static boolean USE_SSL;
    
    public static String TRUSTSTORE_PATH;

    public static String TRUSTSTORE_PASSWORD;

    public static Map<String,String> HEADERS;
    

    @Override
    public void afterPropertiesSet() {
        AppSettings.SERVER_PATH             = serverpath;
        AppSettings.LOG_FILENAME            = logfile;
        AppSettings.LOG_LEVEL               = loglevel;
        AppSettings.CERTS_PATH              = certspath;
        AppSettings.USE_SSL                 = usessl;
        AppSettings.TRUSTSTORE_PATH         = truststore;
        AppSettings.TRUSTSTORE_PASSWORD     = truststore_pass;
        AppSettings.ASIC_PATH               = asicpath;
        AppSettings.HEADERS                 = headers;
        
        AppSettings.SERVICE_X_ROAD_INSTANCE = service_xRoadInstance;
        AppSettings.SERVICE_MEMBER_CLASS    = service_memberClass;
        AppSettings.SERVICE_MEMBER_CODE     = service_memberCode;
        AppSettings.SERVICE_SUBSYSTEM_CODE  = service_subsystemCode;
        AppSettings.CLIENT_X_ROAD_INSTANCE  = client_xRoadInstance;
        AppSettings.CLIENT_MEMBER_CLASS     = client_memberClass;
        AppSettings.CLIENT_MEMBER_CODE      = client_memberCode;
        AppSettings.CLIENT_SUBSYSTEM_CODE   = client_subsystemCode;
        AppSettings.CLIENT_ENDPOINT         = client_endpoint;
        
        System.out.println("afterPropertiesSet: Bean is initialized = "+SERVER_PATH);

    }
    
}