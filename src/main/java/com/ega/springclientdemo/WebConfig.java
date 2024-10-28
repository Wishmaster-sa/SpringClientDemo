/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo;

import com.ega.springclientdemo.models.AppSettings;
import io.github.edwardUL99.inject.lite.annotations.PreConstruct;
import java.util.HashMap;
import org.mockito.Mockito;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author parallels
 */
@Configuration
//@PropertySource("classpath:application.properties")
//public class WebConfig implements InitializingBean{
public class WebConfig {
    //private final AppSettings appSettings;
    //@Value("${webclient.settings.serverpath:http:\/\/localhost:8080\/api\/v1\/persons\/}")
    //@Value("${property.name}")
    //private static final String PROPERTY_NAME;
    //@Value("${webclient.settings.logfilename}")
    //private String PROPERTY_NAME;

    //@Value("${webclient.settings.serverpath}")
    //private String serverpath;
    
    //private static String SERVER_PATH;
    
    //@Autowired
    //private static Environment env;
    

    public WebConfig(){
    //    appSettings = new AppSettings();
    //    this.serverpath = AppSettings.SERVER_PATH;
    }
    
    
    @Bean
    public WebClient getWebClient() {

    // Create a WebClient bean for making reactive web requests
    //Consumer httpHeaders;
    HashMap headers = new HashMap();
    System.out.println("WebConstruct: "+AppSettings.SERVER_PATH);
    
    headers.put("Content-Type", "Application/json");
            
    WebClient webClient;
        webClient = WebClient.builder()                
                .baseUrl(AppSettings.SERVER_PATH) // Set the base URL for the requests
                .defaultCookie("cookie-name", "cookie-value") // Set a default cookie for the requests
                //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set a default header for the requests
                .defaultHeaders(HttpHeaders-> {
                    HttpHeaders.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
                    
                            headers.forEach((key,value)->{
                                    HttpHeaders.set(key.toString(), value.toString());
                            });
                }) // Set a default header for the requests
                .build();
    //webClient.defaultHeader()
    return webClient; // Return the configured WebClient bean
  }
/*
    @Override
    public void afterPropertiesSet()  {
        System.out.println("PostConstruct: ExampleBean is initialized = "+SERVER_PATH);
        System.out.println("PostConstruct: ExampleBean is initialized = "+serverpath);
        WebConfig.SERVER_PATH = serverpath;
    
    }
*/
}
