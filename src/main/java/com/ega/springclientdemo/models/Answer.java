/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientdemo.models;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * Цей клас містить модель відповіді на будь-який запит
 * status - вказує чи є запит успішним.
 * descr - текстовий опис результату відповіді. Якщо status = false, тут має бути опис причини помилки
 * result - результат запиту. Зазвичай, результат надається в форматі JSON 
 */
@Data

// Анотація @Builder створює складні API-інтерфейси компонувальника для ваших класів.
// Builder дозволяє автоматично генерувати код, необхідний для створення екземпляра вашего класу за допомогою коду.
@Builder
public class Answer implements Serializable{
    private Boolean status;
    private String descr;
    public String result;
}
