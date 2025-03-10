/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.services;

import com.ega.springclientdemo.WebConfig;
import com.ega.springclientdemo.interfaces.SpringClientDemoInterface;
import com.ega.springclientdemo.models.Answer;
import com.ega.springclientdemo.models.AppSettings;
import com.ega.springclientdemo.models.LogRecord;
import com.ega.springclientdemo.models.Persona;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


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

    private WebClient logWebClient(WebClient webClient){
        
        return webClient;
    }

    
    // Method to retrieve an employee using a GET request
    @Override
    public Mono<String> getHtml(String resource) {
        String tmp;
        WebClient webClient = new WebConfig().getWebClient();
        String queryId = UUID.randomUUID().toString();
        HashMap log = new HashMap();
        
        log.put("type", "REQUEST");
        log.put("httpMethod", "GET");
        log.put("uri", AppSettings.SERVER_PATH);
        log.put("resource", resource);
        log.put("queryId", queryId);
        
        writeLog(log);
        
        String param = resource+"?queryId="+queryId;
                
        try {
            tmp = render_template("list_person.html");
        } catch (FileNotFoundException ex) {
            tmp = "ERROR: "+ex.getMessage();
        }

        final String html = tmp;
        return webClient.get()
                .uri(param)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                    clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value-> transformToTable(value, html,queryId))     //модіфікуємо сторінку list_person.html
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
    }
    
    
    private void getASIC(String queryId){
        Path path = Paths.get("src/main/resources/sample.zip");

        WebClient webClient = new WebConfig().getWebClient();
        String tmp;

        Flux<DataBuffer> dataBufferFlux = webClient.get()
                .uri("signature?&queryId="+queryId+"&xRoadInstance=test1&memberClass=GOV&memberCode=00000088&subsystemCode=TEST_SUB888")
                .retrieve().bodyToFlux(DataBuffer.class);
        DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block(); //Creates new file or overwrites exisiting file
        
    }
    
    public String transformToTable(Answer ans, String html,String queryId){
        Boolean isSuccess;
        String replaceString = "";
        String result = html;
     
        HashMap log = new HashMap();
        
        log.put("type", "RESPONSE");
        log.put("httpMethod", "GET");
        log.put("uri", AppSettings.SERVER_PATH);
        log.put("resource", "");
        log.put("queryId", queryId);
        log.put("body", ans.toString());

        writeLog(log);
        
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
                .uri("/check/"+rnokpp)
                .retrieve()
                .bodyToMono(Answer.class);

    }

    @Override
    public Mono<Answer> checkup(String rnokpp) {
        WebClient webClient = new WebConfig().getWebClient();
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
        //запис лога
    private void writeLog(HashMap logrecord){
    
        LogRecordService logService = new LogRecordService();
        
        LogRecord log = new LogRecord();
        
        log.setType((String)logrecord.getOrDefault("type",""));
        log.setUri((String)logrecord.getOrDefault("uri",AppSettings.SERVER_PATH));
        log.setHttpMethod((String)logrecord.getOrDefault("httpMethod",""));
        log.setQuieryId((String)logrecord.getOrDefault("queryId",""));
        log.setResource((String)logrecord.getOrDefault("resource",""));
        //log.setHeaders((String)logrecord.getOrDefault("headers"));

        log.setBody((String)logrecord.getOrDefault("body",""));
        
        logService.addRecord(log);
        
    }

    @Override
    public Mono<String> listCerts() {
        String html;
        
        html = render_template_certs(AppSettings.CERTS_PATH);
    
        return Mono.just(html);
    }
    
    private String render_template_certs(String path){
        
        List<File> files = Stream.of(new File(path).listFiles())
            .filter(file -> !file.isDirectory())
            //.map(File::getName)
            .collect(Collectors.toList());

        
        String replaceString = "";
        String html;
        //LocalDateTime fileLastModified;
        
        for(int i=0;i<files.size();i++){
            Long timestamp = files.get(i).lastModified();
            String fileLastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), 
                                TimeZone.getDefault().toZoneId()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));    
            replaceString +=  "<tr>\n"
            +"    <td> "+files.get(i).getName() +"</td>  <!-- Відображаємо ім'я файлу -->\n"
            +"    <td> "+fileLastModified +"</td>  <!-- Відображаємо дату і час створення файлу -->\n"
            +"    <td>\n"
            +"        <a href=\"/download_cert/"+files.get(i).getName()+"\" class=\"btn btn-primary\">Скачати</a>  <!-- Посилання для завантаження файлу -->\n"
            +"    </td>\n"
            +"    </tr>\n";
            
        }

            try {
                html = render_template("list_certs.html");
            } catch (FileNotFoundException ex) {
                html = "";
                System.out.println("Error: "+ex.getMessage());
            }
            html = html.replaceAll("<!--@DataTable -->",replaceString);

            

        
        return html;
    }

    @Override
    public Mono<String> downloadFile(String path) {
    
        return Mono.just("");
    }

}

