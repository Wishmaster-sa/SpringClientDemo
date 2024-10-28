/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.controllers;

import com.ega.springclientdemo.WebConfig;
import com.ega.springclientdemo.interfaces.SpringClientDemoInterface;
import com.ega.springclientdemo.models.Greeting;
import com.ega.springclientdemo.services.SpringClientDemoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sa
 */
@Controller
public class SearchFormController {
        private final ObjectMapper mapper;
        private final SpringClientDemoInterface service;
        private final WebConfig webClient;
        
	public SearchFormController(ObjectMapper objectMapper) {
		this.mapper = objectMapper;
                
                this.webClient = new WebConfig();
                this.service = new SpringClientDemoService(webClient.getWebClient());                
	}
        
        @GetMapping("/greeting")
        public String greetingForm(Model model) throws FileNotFoundException {
            System.out.println("GET request: "+model.toString());
            model.addAttribute("greeting", new Greeting());
            System.out.println("GET request: "+model.toString());

            return "greeting.html";
        }

        @PostMapping("/greeting")
        public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
            System.out.println("Search persons...");
            model.addAttribute("greeting", greeting);
            System.out.println("Query1: "+greeting.toString());
            System.out.println("Query2: "+model.toString());
            System.out.println("query: " + greeting.getContent());
            
            return "result";
        }
    
}
