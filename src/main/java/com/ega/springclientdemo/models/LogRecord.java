/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.json.JSONObject;

/**
 * Це клас моделі даних для запису в лог-файл.
 * Він містить інформацію стосовно часу, коли відбулася подія, IP адреси клієнта, HTTP метод запиту, URL запиту та інші параметри.
 * @author sa
 */
@Data
public class LogRecord {
    private LocalDateTime dateTime;
    private String type;
    private String quieryId;
    private String uri;
    private String httpMethod;
    private String resource;
    private String body;
    private boolean isError;
    private String descr;
    private Answer result;
    private Map<String, String> headers;

    //Конструктор класу за замовчуванням
    public LogRecord(){
        this.dateTime = LocalDateTime.now();
        this.type = "";
        this.quieryId = "";
        this.uri = AppSettings.SERVER_PATH;
        this.httpMethod = "";
        this.resource = "";
        this.body = "";
        this.isError = true;
        this.descr = "log record is not init!";
        this.headers = new HashMap<>();
                
    }
    
    //перетворює об'єкт класу в JSON
    public JSONObject toJSON(){
        JSONObject jsData = new JSONObject();
        jsData.put("dateTime",getDateTime());
        jsData.put("type",getType());
        jsData.put("queryId",getQuieryId());
        jsData.put("uri",getUri());
        jsData.put("httpMethod",getHttpMethod());
        jsData.put("headers", headers);
        jsData.put("resource",getResource());
        jsData.put("isError",isError());
        jsData.put("descr",getDescr());
        jsData.put("result",getResult());
        jsData.put("body",body);
        
        return jsData;
    }
}
