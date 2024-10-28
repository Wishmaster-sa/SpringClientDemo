/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.services;

import com.ega.springclientdemo.WebConfig;
import com.ega.springclientdemo.interfaces.SpringClientDemoInterface;
import com.ega.springclientdemo.models.Answer;
import com.ega.springclientdemo.models.LogRecord;
import com.ega.springclientdemo.models.Persona;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author sa
 */
@Service
public class SpringClientDemoService implements SpringClientDemoInterface{
    // Constructor-based dependency injection for WebClient
    @Autowired
    public SpringClientDemoService(WebClient webClient) {
  }


    // Method to retrieve an employee using a GET request
    @Override
    public Mono<String> findPersona(String rnokpp) {
        String tmp;
        WebClient webClient = new WebConfig().getWebClient();

        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient.get()
                .uri("/find/"+rnokpp)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value-> transformToTable(value, html))     //модіфікуємо сторінку list_person.html
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }
    
    public String transformToTable(Answer ans, String html){
        Boolean isSuccess;
        String replaceString = "";
        String result = html;
     
        writeLog(ans);
        
        String res = "";
        
        if(ans.getStatus()){
            res = ans.getResult();
            try{
                isSuccess = true;
                JSONObject item = new JSONObject(res);

                replaceString +=  "<tr>\n"
                    +"        <td>"+item.getString("firstName")+"</td>  <!-- Відображаємо ім'я -->\n"
                    +"        <td>"+item.getString("lastName")+"</td>  <!-- Відображаємо прізвище -->\n"
                    +"        <td>"+item.getString("patronymic")+"</td>  <!-- Відображаємо по батькові -->\n"
                    +"        <td>"+item.getString("unzr")+"</td>  <!-- Відображаємо УНЗР -->\n"
                    +"        <td>\n"
                    +"            <button class=\"btn btn-info\" onclick=\"showDetails("+item.getLong("id")+")\">Детальніше</button>  <!-- Кнопка для відображення деталей -->\n"
                    +"        </td>\n"
                    +"    </tr>\n";

                result = result.replaceAll("@dataToJson", "["+res+"]");
            }
            catch(Exception e){
                isSuccess = false;
                res="[{}]";
            }
            if(!isSuccess){
                res = ans.getResult();
                try{
                    JSONArray jsArray = new JSONArray(res);
                    for(int i=0;i<jsArray.length();i++){
                        JSONObject item = jsArray.getJSONObject(i);

                        replaceString +=  "<tr>\n"
                            +"        <td>"+item.getString("firstName")+"</td>  <!-- Відображаємо ім'я -->\n"
                            +"        <td>"+item.getString("lastName")+"</td>  <!-- Відображаємо прізвище -->\n"
                            +"        <td>"+item.getString("patronymic")+"</td>  <!-- Відображаємо по батькові -->\n"
                            +"        <td>"+item.getString("unzr")+"</td>  <!-- Відображаємо УНЗР -->\n"
                            +"        <td>\n"
                            +"            <button class=\"btn btn-info\" onclick=\"showDetails("+item.getLong("id")+")\">Детальніше</button>  <!-- Кнопка для відображення деталей -->\n"
                            +"        </td>\n"
                            +"    </tr>\n";

                    }
                }
                catch(Exception e){
                    res="[{}]";
                }
            }

        }else
        {
            res="[{}]";
        }
        
                
        
        
        result = result.replaceAll("<!--@PersonsTable-->", replaceString);
        result = result.replaceAll("history.back()", "history.back(0)");
        result = result.replaceAll("@dataToJson", res);
    
        
        return result;
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
    
    @Override
    public Mono<String> listPersons() {
        String tmp;
        WebClient webClient = new WebConfig().getWebClient();

        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpringClientDemoService.class.getName()).log(Level.SEVERE, null, ex);
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient.get()
                .uri("/list")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value->transformToTable(value, html))                             //отримаемо результат запита (String)
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }

    @Override
    public Mono<Answer> showAll() {
        WebClient webClient = new WebConfig().getWebClient();

    return webClient.get()
      .uri("/list")
      .retrieve()
      .bodyToMono(Answer.class)
      .onErrorResume(Exception.class, e -> Mono.empty()); // Return an empty collection on error
    
    }


    @Override
    public Mono<Answer> savePersona(Persona persona) {
        Mono<Answer> ans;
        WebClient webClient = new WebConfig().getWebClient();
        
        if(persona.getId()==null){ //ADD new persona
            ans = webClient.post()
                    .uri("/add")
                    .bodyValue(persona.toJSON().toString())
                    .retrieve()
                    .bodyToMono(Answer.class);
        }else{  //UPDATE persona
            ans = webClient.delete()
                    .uri("/delete/"+persona.getRnokpp())
                    //.bodyValue(persona.toJSON().toString())
                    .retrieve()
                    .bodyToMono(Answer.class);
            ans = webClient.post()
                    .uri("/add")
                    .bodyValue(persona.toJSON().toString())
                    .retrieve()
                    .bodyToMono(Answer.class);
        }
      
        return ans;
    }

    @Override
    public Mono<Answer> deletePersona(String rnokpp) {
        WebClient webClient = new WebConfig().getWebClient();

        return webClient.delete()
                .uri("/delete/"+rnokpp)
                .retrieve()
                .bodyToMono(Answer.class);
     
    }
    
    @Override
    public Mono<Answer> checkPersona(String rnokpp) {
        WebClient webClient = new WebConfig().getWebClient();

        return  webClient.get()
                //.uri("/find/{rnokpp}", "RALUMHK1") // Assuming you want to retrieve an employee with ID 1
                .uri("/check/"+rnokpp)
                .retrieve()
                .bodyToMono(Answer.class);

    }

    @Override
    public Mono<Answer> checkup(String rnokpp) {
        WebClient webClient = new WebConfig().getWebClient();
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Mono<String> findByFirstName(String searchData) {
        String tmp;
        WebClient webClient2 = new WebConfig().getWebClient();
        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpringClientDemoService.class.getName()).log(Level.SEVERE, null, ex);
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient2.get()
                .uri("/find/firstname/"+searchData)
                .header("Content-Type", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value->transformToTable(value, html))                             //отримаемо результат запита (String)
                //.map((String data)-> transformToTable2(data, html))     //модіфікуємо сторінку list_person.html
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }

    @Override
    public Mono<String> findByLastName(String searchData) {
        String tmp;
        WebClient webClient2 = new WebConfig().getWebClient();
        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpringClientDemoService.class.getName()).log(Level.SEVERE, null, ex);
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient2.get()
                .uri("find/lastname/"+searchData)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value->transformToTable(value, html))                             //отримаемо результат запита (String)
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }

    @Override
    public Mono<String> findByPasport(String searchData) {
        WebClient webClient = new WebConfig().getWebClient();

        String tmp;
        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpringClientDemoService.class.getName()).log(Level.SEVERE, null, ex);
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient.get()
                .uri("find/pasport/"+searchData)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value->transformToTable(value, html))                             //отримаемо результат запита (String)
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }

    @Override
    public Mono<String> findByUnzr(String searchData) {
        WebClient webClient = new WebConfig().getWebClient();
        String tmp;

        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpringClientDemoService.class.getName()).log(Level.SEVERE, null, ex);
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient.get()
                .uri("find/unzr/"+searchData)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value->transformToTable(value, html))                             //отримаемо результат запита (String)
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }

        //запис лога
    private void writeLog(Answer ans){
    
        LogRecordService logService = new LogRecordService();
        
        LogRecord log = new LogRecord();
        
        log.setIp("localhost");
        //log.setHttpMethod(HttpRequestUtils.getHttpMethod());
        //log.setHeaders(HttpRequestUtils.getHeaders());
        log.setError(!ans.getStatus());
        //log.setResource(HttpRequestUtils.getPath());
        log.setResult(ans);
        log.setDescr(ans.getDescr());
        log.setBody("");
        
        
        logService.addRecord(log);
        
    }


}

