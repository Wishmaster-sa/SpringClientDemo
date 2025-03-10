/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.ega.springclientdemo.services;

import com.ega.springclientdemo.models.Answer;
import com.ega.springclientdemo.models.Persona;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import reactor.core.publisher.Mono;

/**
 *
 * @author sa
 */
public class SpringClientDemoServiceTest {
    
    public SpringClientDemoServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getHtml method, of class SpringClientDemoService.
     */
    @Test
    public void testGetHtml() {
        System.out.println("getHtml");
        String resource = "";
        SpringClientDemoService instance = null;
        Mono<String> expResult = null;
        Mono<String> result = instance.getHtml(resource);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of transformToTable method, of class SpringClientDemoService.
     */
    @Test
    public void testTransformToTable() {
        System.out.println("transformToTable");
        Answer ans = null;
        String html = "";
        String queryId = "";
        SpringClientDemoService instance = null;
        String expResult = "";
        String result = instance.transformToTable(ans, html, queryId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of render_template method, of class SpringClientDemoService.
     */
    @Test
    public void testRender_template() throws Exception {
        System.out.println("render_template");
        String templateName = "";
        SpringClientDemoService instance = null;
        String expResult = "";
        String result = instance.render_template(templateName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showAll method, of class SpringClientDemoService.
     */
    @Test
    public void testShowAll() {
        System.out.println("showAll");
        SpringClientDemoService instance = null;
        Mono<Answer> expResult = null;
        Mono<Answer> result = instance.showAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of savePersona method, of class SpringClientDemoService.
     */
    @Test
    public void testSavePersona() {
        System.out.println("savePersona");
        Persona persona = null;
        SpringClientDemoService instance = null;
        Mono<Answer> expResult = null;
        Mono<Answer> result = instance.savePersona(persona);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deletePersona method, of class SpringClientDemoService.
     */
    @Test
    public void testDeletePersona() {
        System.out.println("deletePersona");
        String rnokpp = "";
        SpringClientDemoService instance = null;
        Mono<Answer> expResult = null;
        Mono<Answer> result = instance.deletePersona(rnokpp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkPersona method, of class SpringClientDemoService.
     */
    @Test
    public void testCheckPersona() {
        System.out.println("checkPersona");
        String rnokpp = "";
        SpringClientDemoService instance = null;
        Mono<Answer> expResult = null;
        Mono<Answer> result = instance.checkPersona(rnokpp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkup method, of class SpringClientDemoService.
     */
    @Test
    public void testCheckup() {
        System.out.println("checkup");
        String rnokpp = "";
        SpringClientDemoService instance = null;
        Mono<Answer> expResult = null;
        Mono<Answer> result = instance.checkup(rnokpp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listCerts method, of class SpringClientDemoService.
     */
    @Test
    public void testListCerts() {
        System.out.println("listCerts");
        SpringClientDemoService instance = null;
        Mono<String> expResult = null;
        Mono<String> result = instance.listCerts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of downloadFile method, of class SpringClientDemoService.
     */
    @Test
    public void testDownloadFile() {
        System.out.println("downloadFile");
        String path = "";
        SpringClientDemoService instance = null;
        Mono<String> expResult = null;
        Mono<String> result = instance.downloadFile(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
