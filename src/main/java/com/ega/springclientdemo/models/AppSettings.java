/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.models;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author sa
 */

@Component

public class AppSettings implements InitializingBean{
    
    @Value("${webclient.settings.serverpath}")
    private String serverpath;
    
    @Value("${webclient.settings.logfilename}")
    private String logfile;
    
    @Value("${webclient.settings.loglevel}")
    private String loglevel;
    
    @Value("${webclient.test}")
    private String test;

    public static String SERVER_PATH;
    
    public static String LOG_FILENAME;
    
    public static String LOG_LEVEL;

    public static String TEST;
/*    
    @Value("${webclient.settings.serverpath}")
    public void setServerPath(String path){
        this.SERVER_PATH = path;
    }

    @Value("${webclient.settings.logfilename}")
    public void setLogFilePath(String file){
        this.LOG_FILENAME = file;
    }
    
    public String getServerPath(){
        return SERVER_PATH;
    }

    public String getLogName(){
        return LOG_FILENAME;
    }
    public String getLogLevel(){
        return LOG_LEVEL;
    }
    public String getTest(){
        return TEST;
    }
*/
    @Override
    public void afterPropertiesSet() {
        AppSettings.SERVER_PATH = serverpath;
        AppSettings.LOG_FILENAME = logfile;
        AppSettings.LOG_LEVEL = loglevel;
        AppSettings.TEST = test;
        System.out.println("afterPropertiesSet: ExampleBean is initialized = "+SERVER_PATH);

    }

    
}