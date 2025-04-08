
package com.ega.springclientdemo.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//Click https://projectlombok.org/features/ to view all features of lombok
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Цей клас задає модель інформаційного об'єкта - користувача, та його поля (ім'я, прізвище, дату народження, вік тощо)
 */
//Ця анотація надає можливість автоматично формувати конструктор класу, та реалізує встановлення та отримання всіх полів класу.
@Data
// The @Builder annotation produces complex builder APIs for your classes.
// Builder lets you automatically produce the code required to have your class be instantiable with code
//@Builder

//Анотація @Entity говорить SPRINGу що цю сутність потрібно включити до бази даних.
//Так як у нас анотація відноситься до всього класу - це означає, що на базі класу буде створена таблиця в БД
//Докладніше https://www.baeldung.com/jpa-entities
//@Entity

//Задає назву таблиці. Якщо ця анотація не задана, то назва таблиці буде дублювати назву класу.
//@Table(name="Persona")
public class Persona implements Serializable{
    //Ця анотація зазначає, що наступне поле класу буде виконувати роль ідентіфікатора в БД    
//    @Id
    //Говорить, що ідентіфікатор наступного поля класу буде генеруватись автоматично
//    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String gender;
    private LocalDate birthDate;
    private String pasport;
    //Говорить, що наступне поле потрібно зробити унікальним в БД
//    @Column(unique = true)
    private String unzr;                    //УНЗР
    private Boolean isChecked;              //ознака опрацювання запису оператором вручну (для прикладу асинхронних запитів-відповідей)
    private LocalDateTime CheckedRequest;   //коли був запит на перевірку користувача
    
    //Говорить, що наступне поле потрібно зробити унікальним в БД
//    @Column(unique = true)
//    @NonNull
    private String rnokpp;
    //Говорить, що значення цього поля буде розраховуватись автоматично
//    @Transient
    private int age;
    
    public Persona(){
        this.firstName = "";
        this.lastName = "";
        this.patronymic = "";
        this.gender = "";
        this.birthDate = LocalDate.of(1, 1, 1);
        this.pasport = "";
        this.unzr = "";
        this.isChecked = false;
        this.CheckedRequest = LocalDateTime.of(1, 1, 1,0,0,0);
    }
    
    // Перевизначення функції отримання віку. Отримуємо вік як різницю кількості років між
    // поточним роком та роком народження.
    public int getAge(){
        if((birthDate == null)||(birthDate==LocalDate.of(1, 1, 1))){
            return 0;
        }else return LocalDate.now().getYear() - birthDate.getYear();
    }

    //метод, який перетворює об'єкт класу користувача на JSON об'єкт
    public JSONObject toJSON(){
        JSONObject jsData=new JSONObject();
        //Persona persona = result.get(i);
        jsData.put("id",getId());
        jsData.put("firstName",getFirstName());
        jsData.put("lastName",getLastName());
        jsData.put("patronymic",getPatronymic());
        jsData.put("gender",getGender());
        jsData.put("unzr",getUnzr());
        jsData.put("rnokpp",getRnokpp());
        jsData.put("pasport",getPasport());
        jsData.put("age",getAge());
        jsData.put("birthDate",getBirthDate());
        jsData.put("isChecked",getIsChecked());
        jsData.put("CheckedRequest",getCheckedRequest());
        
        return jsData;
    }  
    
    //метод, який перетворює список користувачів на масив JSON
    public static JSONArray listToJSON(List <Persona> personsList){
        JSONArray persons = new JSONArray(personsList);
        
        return persons;
    } 
    
    @Override
    public String toString(){
        String result = "{\n";
        if(getId()!=null){
            result += "\"id\":\""+getId()+"\",\n";
        }
        result += "\"lastName\":\""+getLastName()+"\",\n";
        result += "\"firstName\":\""+getFirstName()+"\",\n";
        result += "\"patronymic\":\""+getPatronymic()+"\",\n";
        result += "\"gender\":\""+getGender()+"\",\n";
        result += "\"unzr\":\""+getUnzr()+"\",\n";
        result += "\"rnokpp\":\""+getRnokpp()+"\",\n";
        result += "\"pasport\":\""+getPasport()+"\",\n";
        result += "\"age\":\""+getAge()+"\",\n";
        result += "\"birthDate\":\""+getBirthDate()+"\",\n";
        result += "\"isChecked\":\""+getIsChecked()+"\",\n";
        result += "\"CheckedRequest\":\""+getCheckedRequest()+"\"\n}";

        return result;
    }
    
}

