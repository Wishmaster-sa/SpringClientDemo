/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ega.springclientdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author sa
 */

@ServletComponentScan
@PropertySource("classpath:application.properties")
//Ця анотація говорить Спрингу, що це основний клас, який запускає наш Веб-додаток
@SpringBootApplication
public class SpringClientDemo {

	public static void main(String[] args) {
		SpringApplication.run(SpringClientDemo.class, args);
	}

}