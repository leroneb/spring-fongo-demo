package com.johnathanmarksmith.mongodb.example.service.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.johnathanmarksmith.mongodb.example.domain.Person;

/**
 * Data Access Object
 * @author lerone.bleasdille
 * Our Integration test would override the retrieveFundamentalMongoData method.
 *
 */
public class PersonResource
{
	static final Logger logger = LoggerFactory.getLogger(PersonResource.class);
	
	//Imagine that this is an actual MongoDB database
	private static Map<String, Person> MongoDB_Database = new HashMap<String, Person>();
	
	public Person getPersonInformation(HttpHeaders headers, HttpServletRequest request, String name)
	{
		logger.info("Person object to be returned for: ", name);		
		//Retrieve data from a mongoDB database
		Person retPerson = retrievePersonMongoData(name);				
		return retPerson;
	}

	protected Person retrievePersonMongoData(String name)
	{
		return MongoDB_Database.get(name);
	}
	
	
	//load fake database
	static{
		Person p = new Person("Johnathan", 10);		
			MongoDB_Database.put("Johnathan", p);
			
		p = new Person("Mark", 20);	
			MongoDB_Database.put("Mark", p);
			
		p = new Person("Smith", 21);	
			MongoDB_Database.put("Smith", p);
		
		p = new Person("Miguel", 40);	
			MongoDB_Database.put("Miguel", p);
		
		p = new Person("Cartegena", 50);	
			MongoDB_Database.put("Cartegena", p);
	}

}
