/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.controllers;

import com.ega.springclientdemo.WebConfig;
import com.ega.springclientdemo.interfaces.SpringClientDemoInterface;
import com.ega.springclientdemo.models.Answer;
import com.ega.springclientdemo.models.AppSettings;
import com.ega.springclientdemo.models.Persona;
import com.ega.springclientdemo.services.SpringClientDemoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        
        @GetMapping("/create")
        //перехід до сторинкі створення персони
	public String viewCreateForm() throws FileNotFoundException{
            System.out.println("Create persona page!");
            return service.render_template("create_person.html");
	}

        @GetMapping("/certs")
        //перехід до сторинкі створення персони
	public String listCerts() throws FileNotFoundException{
            System.out.println("Certs page!");
            
            return service.listCerts();
	}

        @GetMapping("/files")
        //перехід до сторинкі створення персони
	public String listAsic() throws FileNotFoundException{
            System.out.println("ASIC page!");
            return service.listAsic();
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

            switch(searchKey){
                case "rnokpp" -> res = service.getHtml("/find/"+searchValue);    
                case "firstName" -> res = service.getHtml("/find/firstname/"+searchValue);    
                case "lastName" -> res = service.getHtml("/find/lastname/"+searchValue);    
                case "birthDate" -> res = service.getHtml("/find/birthDate/"+searchValue);    
                case "pasport" -> res = service.getHtml("/find/pasport/"+searchValue);    
                case "unzr" -> res = service.getHtml("/find/unzr/"+searchValue);    
            }
        
            return res;
	}

        @GetMapping("/find")
        //перехід до сторинкі відображення персони
	public Mono<String> findPersonForm( @RequestParam(value="search_field",required=true)String searchKey, @RequestParam(value="search_value",required=true)String searchValue){
            System.out.println("Search persons...");
            System.out.println("key: " + searchKey);
            System.out.println("value: " + searchValue);
            
            Mono<String> res = null;
            
            switch(searchKey){
                case "rnokpp" -> res = service.getHtml("/find/"+searchValue);    
                case "firstName" -> res = service.getHtml("/find/firstname/"+searchValue);    
                case "lastName" -> res = service.getHtml("/find/lastname/"+searchValue);    
                case "birthDate" -> res = service.getHtml("/find/birthDate/"+searchValue);    
                case "pasport" -> res = service.getHtml("/find/pasport/"+searchValue);    
                case "unzr" -> res = service.getHtml("/find/unzr/"+searchValue);    
            }
        
            return res;
	}
        
        @GetMapping("/list")
        //перехід до сторинкі відображення списка персон
	public Mono<String> listPersona() throws FileNotFoundException{
            System.out.println("List persona page!");
            
            return service.getHtml("/list");
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

        @PostMapping("/check")
	public Mono<Answer> checkPersona(@RequestBody String data) {
            JSONObject js = new JSONObject(data);
            Mono<Answer> ans;
            System.out.println("===============================CHECK PERSONA==============================================");
            System.out.println(data);
            System.out.println(js.toString());
            ans = service.checkPersona(js.getString("rnokpp"));
            System.out.println(ans.toString());
            
            return ans;
        }

        
	@GetMapping("/notify")
	public ResponseEntity<Void> notification() throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED)
				.header("HX-Trigger", mapper.writeValueAsString(Map.of("notice", "Notification"))).build();
	}
        
        
        //завантаження сертіфікатів
        @GetMapping("/download/{page}/{filename}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String page, @PathVariable String filename) throws IOException{
            String path = "";
            if(page.equalsIgnoreCase("cert")){
                if(AppSettings.CERTS_PATH.startsWith(".")){
                    path = new File(".").getCanonicalPath()+"/"+AppSettings.CERTS_PATH.substring(2)+"/"+filename;
                }else{
                    path = new File(".").getCanonicalPath()+"/"+AppSettings.CERTS_PATH+"/"+filename;
                }
                
            }else if(page.equalsIgnoreCase("asic")){
                if(AppSettings.ASIC_PATH.startsWith(".")){
                    path = new File(".").getCanonicalPath()+"/"+AppSettings.ASIC_PATH.substring(2)+"/"+filename;
                }else{
                    path = new File(".").getCanonicalPath()+"/"+AppSettings.ASIC_PATH+"/"+filename;
                }
            }else{    
                path = new File(".").getCanonicalPath()+"/files/"+filename;
            }
            System.out.println("Download file: "+path);
            File ff = new File(path);
            
            InputStream bin1 = new FileInputStream(path);
            long len = ff.length();

            return ResponseEntity
                .ok()
                .contentLength(len)
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(bin1));

        }

}

