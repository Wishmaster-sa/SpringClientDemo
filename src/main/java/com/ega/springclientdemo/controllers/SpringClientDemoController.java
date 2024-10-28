/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.controllers;

import com.ega.springclientdemo.HttpRequestUtils;
import com.ega.springclientdemo.WebConfig;
import com.ega.springclientdemo.interfaces.SpringClientDemoInterface;
import com.ega.springclientdemo.models.Answer;
import com.ega.springclientdemo.models.Greeting;
import com.ega.springclientdemo.models.LogRecord;
import com.ega.springclientdemo.models.Persona;
import com.ega.springclientdemo.services.SpringClientDemoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.management.Query;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author sa
 */

@RestController
public class SpringClientDemoController {
        private final ObjectMapper mapper;
        private final SpringClientDemoInterface service;
        private final WebConfig webClient;
        
	public SpringClientDemoController(ObjectMapper objectMapper) {
		this.mapper = objectMapper;
                
                this.webClient = new WebConfig();
                this.service = new SpringClientDemoService(webClient.getWebClient());                
	}
        
        @PostMapping("/test")
        //test
	public String test(@ModelAttribute("queryForm") Query query, Map<String, Object> model){
            System.out.println(query.toString());
            return "Fred";
	}

        @GetMapping("/create")
        //перехід до сторинкі створення персони
	public String viewCreateForm() throws FileNotFoundException{
            System.out.println("Create persona page!");
            return render_template("create_person.html");
	}

        @GetMapping("/certs")
        //перехід до сторинкі створення персони
	public String viewCerts() throws FileNotFoundException{
            System.out.println("Certs page!");
            return render_template("list_certs.html");
	}

        @GetMapping("/files")
        //перехід до сторинкі створення персони
	public String viewFiles() throws FileNotFoundException{
            System.out.println("Files page!");
            return render_template("list_files.html");
	}
        
        @PostMapping("/create")
	public Mono<Answer> addPersona(@RequestBody Persona persona) {
            Mono<Answer> ans;
            System.out.println("===============================ADD PERSONA==============================================");
            System.out.println(persona.toString());
            ans = service.savePersona(persona);
            System.out.println(ans.toString());
            
            return ans;
        }
        

        @PostMapping("/search")
        //перехід до сторинкі відображення персони
	public Mono<String> searchPersonForm(@RequestBody String query){
            System.out.println("Search persons...");
            JSONObject js = new JSONObject(query);
            System.out.println("query: " + query.toString());
            System.out.println("js: " + js.toString());
            
            String searchKey = js.getString("searchKey");
            String searchValue = js.getString("searchValue");
            Mono<String> res = null;
            
            //String method = HttpRequestUtils.getHttpMethod();
            //System.out.println("method: " + method);
            
            switch(searchKey){
                case "rnokpp" -> res = service.findPersona(searchValue);    
                case "firstName" -> res = service.findByFirstName(searchValue);    
                case "lastName" -> res = service.findByLastName(searchValue);    
                case "pasport" -> res = service.findByPasport(searchValue);    
                case "unzr" -> res = service.findByUnzr(searchValue);    
            }
            
            
            return res;
	}
        
        @GetMapping("/list")
        //перехід до сторинкі відображення списка персон
	public Mono<String> listPersona() throws FileNotFoundException{
            System.out.println("List persona page!");
            return service.listPersons();
	}
        
        @PatchMapping("/update")
	public Mono<Answer> updatePersona(@RequestBody Persona persona) {
            Mono<Answer> ans;
            System.out.println("===============================ADD PERSONA==============================================");
            System.out.println(persona.toString());
            ans = service.savePersona(persona);
            System.out.println(ans.toString());
            
            return ans;
        }
        
        @DeleteMapping("/delete")
	public Mono<Answer> deletePersona(@RequestBody String data) {
            JSONObject js = new JSONObject(data);
            Mono<Answer> ans;
            System.out.println("===============================DELETE PERSONA==============================================");
            System.out.println(data);
            System.out.println(js.toString());
            ans = service.deletePersona(js.getString("rnokpp"));
            System.out.println(ans.toString());
            
            return ans;
        }
        
	@GetMapping("/notify")
	public ResponseEntity<Void> notification() throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED)
				.header("HX-Trigger", mapper.writeValueAsString(Map.of("notice", "Notification"))).build();
	}
        
    
    public String render_template(String templateName)
        throws FileNotFoundException
    {
 
        // the stream holding the file content
        InputStream is = getClass().getClassLoader().getResourceAsStream("templates/"+templateName);
          
        String html = null;
        try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            html = scanner.useDelimiter("\\A").next();
        }

        return html;
    }

}

