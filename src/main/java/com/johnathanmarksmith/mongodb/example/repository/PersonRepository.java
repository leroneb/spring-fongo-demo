package com.johnathanmarksmith.mongodb.example.repository;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.johnathanmarksmith.mongodb.example.domain.Person;

@Repository
public class PersonRepository
{
	static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

	@Autowired
	MongoTemplate mongoTemplate;

	public long countUnderAge()
	{
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("age").lte(21);

		query.addCriteria(criteria);
		long count = this.mongoTemplate.count(query, Person.class);

		logger.info("Total number of under age in database: {}", count);
		return count;
	}

	/**
	 * This will count how many Person Objects I have
	 */
	public long countAllPersons()
	{
		// findAll().size() approach is very inefficient, since it returns the whole documents
		// List<Person> results = mongoTemplate.findAll(Person.class);

		long total = this.mongoTemplate.count(null, Person.class);
		logger.info("Total number in database: {}", total);

		return total;
	}

	/**
	 * This will install a new Person object with my
	 * name and random age
	 */
	public void insertPersonWithNameJohnathan(double age)
	{
		Person p = new Person("Johnathan", (int) age);

		mongoTemplate.insert(p);
	}

	/**
	 * This will remove all Person objects in a collection
	 * @author lerone.bleasdille
	 */
	public void removeAllEntriesIn_PersonCollection()
	{
		logger.info("Removing documents loaded per test...");
		logger.info("@Before: " + this.mongoTemplate.count(null, Person.class));
		Query removeAllEntries = new Query();
		mongoTemplate.remove(removeAllEntries, Person.class);
		logger.info("@After: " + this.mongoTemplate.count(null, Person.class));

	}
	
	/**
	 * Retrieve record from Fongo determined by Id.
	 * @author lerone.bleasdille
	 * @param collection
	 * @param id
	 * @return Person
	 */
	public Person getValue(String collection, String id)
	{
		try
		{
			Person person = null;
			if (mongoTemplate.collectionExists(Person.class))
			{
				logger.info("count of records inserted: " + this.countAllPersons());
				logger.info("collection name: {}" + mongoTemplate.getCollectionName(Person.class));
					
				//way to find all persons in the collection, for display purposes to show capability
				List<Person> personList = mongoTemplate.findAll(Person.class);
				
				logger.info("All Person Objects in the collection:");
				for (Person p : personList)
				{
					logger.info(p.getName());
					logger.info(String.valueOf(p.getAge()));
				}

				logger.info("******************************************");
								
				//searching for a specific person by id or name
				Query query = new Query();
				Criteria criteria = new Criteria();
				criteria = criteria.and("name").in(id);

				query.addCriteria(criteria);
				List<Person> personList2 = mongoTemplate.find(query, Person.class);

				logger.info("Person Object Requested:");
				for (Person p : personList2)
				{					
					logger.info(p.getName());
					logger.info(String.valueOf(p.getAge()));
					//actual important part
					person = p;
				}			
			}
			return person;
		}
		catch (Exception e)
		{
			logger.info("Exception Occurred:");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * this will create a {@link Person} collection if the collection does not already exists
	 */
	public void createPersonCollection()
	{
		if (!mongoTemplate.collectionExists(Person.class))
		{
			mongoTemplate.createCollection(Person.class);
		}
	}

	/**
	 * this will drop the {@link Person} collection if the collection does already exists
	 */
	public void dropPersonCollection()
	{
		if (mongoTemplate.collectionExists(Person.class))
		{
			mongoTemplate.dropCollection(Person.class);
		}
	}
}
