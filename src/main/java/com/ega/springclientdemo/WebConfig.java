/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo;

import com.ega.springclientdemo.models.AppSettings;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

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
    if(AppSettings.HEADERS!=null){
        AppSettings.HEADERS.forEach((key,value)->{
                headers.put(key.toString(), value.toString());
                System.out.println("add key: "+key+"; value: "+value);
        });
    }


    WebClient webClient = null;
    
    if(AppSettings.USE_SSL){        
        Path truststorePath = Paths.get(AppSettings.TRUSTSTORE_PATH);
        
        try {
            //InputStream truststoreInputStream = new FileInputStream(AppSettings.TRUSTSTORE_PATH);
            InputStream truststoreInputStream = Files.newInputStream(truststorePath, StandardOpenOption.READ);
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(truststoreInputStream, AppSettings.TRUSTSTORE_PASSWORD.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(truststore);
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(trustManagerFactory)
                    .build();
            HttpClient httpClient = HttpClient.create()
                    .secure(sslSpec -> sslSpec.sslContext(sslContext));
            webClient =  WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(AppSettings.SERVER_PATH) // Set the base URL for the requests
                    .defaultCookie("cookie-name", "cookie-value") // Set a default cookie for the requests
                    //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set a default header for the requests
                    .defaultHeaders(HttpHeaders-> {
                        HttpHeaders.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);

                                headers.forEach((key,value)->{
                                        HttpHeaders.set(key.toString(), value.toString());
                                        System.out.println("add header: "+key+" = "+value);
                                });
                    }) // Set a default header for the requests
                    .build();
            
        } catch (IOException ex) {
            Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error IOException: "+ex.getMessage());
        } catch (KeyStoreException ex) {
            Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error KeyStoreException: "+ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error NoSuchAlgorithmException: "+ex.getMessage());
        } catch (CertificateException ex) {
            Logger.getLogger(WebConfig.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error CertificateException: "+ex.getMessage());
        }
    }else{
        webClient = WebClient.builder()                
                    .baseUrl(AppSettings.SERVER_PATH) // Set the base URL for the requests
                    .defaultCookie("cookie-name", "cookie-value") // Set a default cookie for the requests
                    //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set a default header for the requests
                    .defaultHeaders(HttpHeaders-> {
                        HttpHeaders.set(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);

                                headers.forEach((key,value)->{
                                        HttpHeaders.set(key.toString(), value.toString());
                                        System.out.println("add header: "+key+" = "+value);
                                });
                    }) // Set a default header for the requests
                    .build();
    }
    
    
    return webClient; // Return the configured WebClient bean
  }
}
