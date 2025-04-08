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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


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
        loguuid(queryId);
        
        String param = resource+"?queryId="+queryId;
        
        tmp = render_template("list_person.html");

        
        final String html = tmp;
        return webClient.get()
                .uri(param)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error( new  RuntimeException ( "Помилка серверу" ))) 
                .bodyToMono(Answer.class)                       //перетворюємо на Answer
                .map(value-> transformToTable(value, html,queryId))     //модіфікуємо сторінку list_person.html
                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
//      .bodyToMono(String.class)
//                .flatMap(response-> Mono.just("Error: "+response))
                .onErrorResume(e -> { 
                String htmlError = transformToTable(Answer.builder().status(Boolean.FALSE).descr(e.getMessage()).build(), html,queryId);     //модіфікуємо сторінку list_person.html
          System.out.println( "Відбулась помилка: " + e.getMessage()); 
          return Mono.just( htmlError ); 
      })
      //.subscribe(System.out::println, error -> System.out.println( "Помилка: " + error.getMessage()))
                //.onStatus(HttpStatusCode::is5xxServerError, response ->response.bodyToMono(String.class).map(Exception::new))
                //.onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new MyServiceException(response.statusCode())))
//                                    .Mono.just("Some Error"))
//                .flatMap(body -> Mono.just("Server Error: " + response.statusCode().toString())))
    //                    .flatMap(body -> Mono.error(new RuntimeException("Server Error: " + body))))
//                .bodyToMono(Answer.class)                       //перетворюємо на Answer
//                .map(value-> transformToTable(value, html,queryId))     //модіфікуємо сторінку list_person.html
//                .flatMap(response-> Mono.just(response))                //перетворюємо відповідь в Моно String. flatMap тому що на виході об'ект Моно.
                ;
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
        if(!ans.getStatus()){
            result = showErrorBlock(result, ans.getDescr());
        }
    
        
        return result;
    }

    @Override
    public String render_template(String templateName) {
        // the stream holding the file content
        String html = "";
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("templates/"+templateName);

            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
            html = scanner.useDelimiter("\\A").next();
        } catch (Exception ex) {
            html = "";
            System.out.println("Error: "+ex.getMessage());
        }
        

        return html;
    }
    
    
    public Mono<Answer> showAll() {
        WebClient webClient = new WebConfig().getWebClient();

    return webClient.get()
      .uri("/list")
      .retrieve()
      .bodyToMono(Answer.class)
      .onErrorContinue(WebClientResponseException.class, (ex, v) -> 
        Mono.just("Error: "+ex.getMessage()))
//            .map(it->it.getResult());
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
    public String listCerts() {
        String html;
        
        html = render_template_files("list_certs.html","cert",AppSettings.CERTS_PATH);
    
        //return Mono.just(html);
        return html;
    }
    
    private String render_template_files(String templateName, String page,String path){
        
        List<File> files = Stream.of(new File(path).listFiles())
            .filter(file -> !file.isDirectory())
            //.map(File::getName)
            .collect(Collectors.toList());

        
        String replaceString = "";
        String html ="";
        
        for(int i=0;i<files.size();i++){
            Long timestamp = files.get(i).lastModified();
            String fileLastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), 
                                TimeZone.getDefault().toZoneId()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));    
            replaceString +=  "<tr>\n"
            +"    <td> "+files.get(i).getName() +"</td>  <!-- Відображаємо ім'я файлу -->\n"
            +"    <td> "+fileLastModified +"</td>  <!-- Відображаємо дату і час створення файлу -->\n"
            +"    <td>\n"
            +"        <a href=\"/download/"+page+"/"+files.get(i).getName()+"\" class=\"btn btn-primary\">Скачати</a>  <!-- Посилання для завантаження файлу -->\n"
            +"    </td>\n"
            +"    </tr>\n";
            
        }

        html = render_template(templateName);
        html = html.replaceAll("<!--@DataTable -->",replaceString);

        return html;
    }

    @Override
    public Mono<String> downloadFile(String path) {
    
        return Mono.just("");
    }

    private String showErrorBlock(String html, String errorMessage){
        String error = "    <div class=\"container mt-5\">\n" +
            "        <!--<h1 class=\"mb-4\">Помилка</h1>  <!-- Заголовок для сторінки з помилкою -->\n" +
            "        <div class=\"alert alert-danger\" role=\"alert\" >  <!-- Відображаємо повідомлення про помилку у вигляді червоного блоку -->\n" +
            "            <h4 class=\"alert-heading\">Сталась помилка!</h4>  <!-- Заголовок повідомлення про помилку -->\n" +
            "            <p>"+errorMessage+"</p>  <!-- Виводимо повідомлення про помилку з переданого змінного error_message -->\n" +
            "            <hr>\n" +
            "    <!--        <button class=\"btn btn-primary\" onclick=\"history.back()\">Назад</button>  <!-- Кнопка для повернення на попередню сторінку -->\n" +
            "        </div>\n" +
            "    </div>\n";

        return html.replaceAll("<!--ONERROR-->", error);
    }
    
    @Override
    public String listAsic() {
        List<String> queries = readLogQueries();
        boolean wasSuccess = false;
        for(int i=0; i<queries.size();i++){
            String parametres = queries.get(i);
            boolean isSuccess = getASIC(parametres);
            //System.out.println("p="+parametres);
            if (isSuccess){
                wasSuccess = true;
            }
            //в любому випадку, якщо були завантажені хоч які файли, очищаємо файл asic.log щоб не скачувати кожного разу теж саме.
        }
        
        if(wasSuccess){
            DeleteFile(AppSettings.ASIC_PATH+"/asic.log");
        }
        
        String html = render_template_files("list_files.html","asic",AppSettings.ASIC_PATH);
        
        return html;
        
    }
    //Після загрузки переліку контейнерів видаляемо файл з неотриманними контейнерами
    private void DeleteFile (String filename){
        File file = new File(filename);

      	// Delete the File
        file.delete();
        
    }
    

    private List <String> readLogQueries(){
        List<String> queries = new ArrayList<String>(); 
        
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(AppSettings.ASIC_PATH+"/asic.log"));
            String line = br.readLine();

            while (line != null) {
                queries.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error: "+ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        
        return queries;
    }
    
    private boolean getASIC(String queryId){
        boolean responseResult = false;
        
        InputStream is = null;
        String result = "";

        
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/octet-stream");

        try {
            // Step 1: Load the certificate from PEM file or use JKS
            // (If you used PKCS#12, uncomment the next block)
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(AppSettings.TRUSTSTORE_PATH), AppSettings.TRUSTSTORE_PASSWORD.toCharArray());

            // Step 2: Create an SSL context with your certificate
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, AppSettings.TRUSTSTORE_PASSWORD.toCharArray());
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                @Override
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }};

            sslContext.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());

            // Step 3: Configure the SSL socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create an URL connection using your custom SSL context
            //URL url = new URL("https://192.168.99.92/signature?queryId=79881fc2-7a83-4e87-be43-b938760dc032&xRoadInstance=test1&memberClass=GOV&memberCode=00000088&subsystemCode=SUB_SERVICE");
            String hostname = AppSettings.SERVER_PATH.toUpperCase();
            if(!hostname.contains("HTTPS")){
                hostname = hostname.replaceAll("HTTP://", "HTTPS://");
            }
            System.out.println(hostname);
            URL url = new URL(hostname+"/signature"+queryId);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
            }

          // Set request method to GET
            connection.setRequestMethod("GET");
            
            for(String key: headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
              }
            
            // Connect
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);

            
            // Check if the request was successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                SaveAttachedFile(connection);
                
                responseResult = true;
            } else {
                result = "Request failed. Response code: " + responseCode;
            }
            // Disconnect
            connection.disconnect();
 
            } catch (MalformedURLException ex) {
                result = "ERROR #0001: "+ex.getMessage();
            } catch (IOException ex) {
                result = "ERROR: #0002"+ex.getMessage();
            }   catch (KeyStoreException ex) {
                result = "ERROR: #0003"+ex.getMessage();
            } catch (NoSuchAlgorithmException ex) {
                result = "ERROR: #0004"+ex.getMessage();
            } catch (CertificateException ex) {
                result = "ERROR: #0005"+ex.getMessage();
            } catch (UnrecoverableKeyException ex) {
                result = "ERROR: #0006"+ex.getMessage();
            } catch (KeyManagementException ex) {
                result = "ERROR: #0007"+ex.getMessage();
            }

            System.out.println(result);

        return responseResult;
    }

    
    private void loguuid(String uuid){
        
        String xRoadInstance = AppSettings.SERVICE_X_ROAD_INSTANCE;
        String memberClass = AppSettings.SERVICE_MEMBER_CLASS;
        String memberCode = AppSettings.SERVICE_MEMBER_CODE;
        String subsystemCode = AppSettings.SERVICE_SUBSYSTEM_CODE;
    
        String queryid = "?queryId="+uuid+"&xRoadInstance="+xRoadInstance+"&memberClass="+memberClass+"&memberCode="+memberCode+"&subsystemCode="+subsystemCode;
        
        BufferedWriter writer;
        try {
            File f = new File(AppSettings.ASIC_PATH+"/asic.log");
            if(f.exists() && !f.isDirectory()) { 
                writer = new BufferedWriter(new FileWriter(AppSettings.ASIC_PATH+"/asic.log", true));
                writer.append(queryid+"\n");
                writer.close();
            }else{
                writer = new BufferedWriter(new FileWriter(AppSettings.ASIC_PATH+"/asic.log", true));
                writer.write(queryid+"\n");
                writer.close();
            }

        } catch (IOException ex) {
            System.out.println("Неможливо зробити запис в asic-файл: "+ex.getMessage());
        }
        
    }
    
    private void SaveAttachedFile(HttpURLConnection connection){
        BufferedInputStream inputStream = null;
        String result = "";
        try {
            // Get the input stream from the connection
            inputStream = new BufferedInputStream(connection.getInputStream());
            String filename = connection.getHeaderField("Content-Disposition").replaceAll("filename=", "").replaceAll("\"", "");
            if(filename.isEmpty()){
                LocalDateTime dt = LocalDateTime.now();
                filename = ""+dt.getYear()+dt.getMonth()+dt.getDayOfMonth()+dt.getHour()+dt.getMinute()+dt.getSecond()+dt.getNano()+".zip";
            }   String outputFilePath = AppSettings.ASIC_PATH+"/"+filename;
            // Create output file
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            // Read bytes and write to file
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }   // Close streams
            bufferedOutputStream.close();
            inputStream.close();
            //System.out.println("File saved to: " + outputFilePath);
            
            result = "File saved to: " + outputFilePath;
        } catch (IOException ex) {
                result = "ERROR: #0008"+ex.getMessage();
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                result = "ERROR: #0008"+ex.getMessage();
            }
        }
        
        System.out.println(result);
    }
    

}

