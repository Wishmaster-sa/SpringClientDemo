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
        AppSettings.SERVER_PATH = serverpath;
        AppSettings.LOG_FILENAME = logfile;
        AppSettings.LOG_LEVEL = loglevel;
        AppSettings.CERTS_PATH = certspath;
        AppSettings.USE_SSL = usessl;
        AppSettings.TRUSTSTORE_PATH = truststore;
        AppSettings.TRUSTSTORE_PASSWORD = truststore_pass;
        AppSettings.ASIC_PATH = asicpath;
        AppSettings.HEADERS = headers;
        
        System.out.println("afterPropertiesSet: ExampleBean is initialized = "+SERVER_PATH);

    }
    
}