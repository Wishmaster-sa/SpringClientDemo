/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ega.springclientdemo.interfaces;

import com.ega.springclientdemo.models.Answer;
import com.ega.springclientdemo.models.Persona;
import reactor.core.publisher.Mono;

/**
 *
 * @author sa
 */
public interface SpringClientDemoInterface {
   public Mono<String> getHtml(String param);
   public Mono<Answer> showAll();
   public Mono<Answer> checkup(String rnokpp);
   public Mono<Answer> checkPersona(String rnokpp);
   public Mono<Answer> savePersona(Persona persona);
   public Mono<Answer> deletePersona(String rnokpp);
   public Mono<String> listCerts();
   public Mono<String> downloadFile(String path);
    
}
