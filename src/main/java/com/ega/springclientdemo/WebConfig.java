/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo;

import com.ega.springclientdemo.models.AppSettings;
import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author sa
 */
@Configuration
public class WebConfig {
    
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
}
