package com.johnathanmarksmith.mongodb.example.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Date:   6/26/13 / 1:21 PM
 * Author: Johnathan Mark Smith
 * Email:  john@johnathanmarksmith.com
 * <p/>
 * Comments:
 * Using tags XMLAccessorType and XmlElement as used in REST development
 * <p/>
 * This is a Person object that I am going to be using for my demo
 */

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
@Document
public class Person implements Serializable{

	private static final long serialVersionUID = 4836764361534000863L;

	@Id
    private String personId;
	
	@XmlElement(required=false)
    private String name;
	@XmlElement(required=false)
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(final String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person [id=" + "personId" + ", name=" + name
                + ", age=" + age + "]";
    }

}