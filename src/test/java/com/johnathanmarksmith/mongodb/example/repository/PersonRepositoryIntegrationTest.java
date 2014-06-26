package com.johnathanmarksmith.mongodb.example.repository;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.foursquare.fongo.Fongo;
import com.johnathanmarksmith.mongodb.example.domain.Person;
import com.johnathanmarksmith.mongodb.example.service.rest.PeopleService;
import com.johnathanmarksmith.mongodb.example.service.rest.PeopleServiceImpl;
import com.johnathanmarksmith.mongodb.example.service.rest.PersonResource;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.Mongo;

/**
 * White box 
 * see: http://softwaretestingfundamentals.com/integration-testing/
 * @author lerone.bleasdille
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PersonRepositoryIntegrationTest
{
	static final Logger logger = LoggerFactory.getLogger(PersonRepositoryIntegrationTest.class);
	
	private static PeopleService peopleServiceImpl;
	
	private static Person markPerson;

	@Rule
	public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("demo-test");

	/**
	 * nosql-unit requirement
	 */
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private PersonRepository personRepository;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		peopleServiceImpl = new PeopleServiceImpl()
		{
			@Override
			protected PersonResource getResourceFactory()
			{
				//Override instantiation of the DAO
				PersonResource res = new PersonResource()
				{
					//Override method that retrieves DB data in the DAO
					@Override    
					protected Person retrievePersonMongoData(String name)
					{
						if (markPerson != null)
						{
							logger.info("Person object for mark is not null, loaded from Fongo: {}", name);	
							return markPerson;
						}else{
							return null;
						}
					}
				};
				return res;
			}
		};

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		//clean up
		peopleServiceImpl = null;
	}

	@Before
	public void setUp() throws Exception
	{}

	@After
	public void tearDown() throws Exception
	{
		//After each test for integrity, remove all entries in the collection
		this.personRepository.removeAllEntriesIn_PersonCollection();
	}	

	/**
	 * Insert data from "five-person.json" and test retrieval of specific value for a name
	 * Save that retrieved object for access by the Application to use for
	 * mocking out MongoDB
	 * 
	 * ala... white box integration test using Fongo to mock MongoDB.
	 */
	@Test
	@UsingDataSet(
			locations = {"/five-person.json"},
			loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void testGetSpecificPerson_ToUse_OnThe_ApplicationLayer()
	{
		/*
		 * static 'markPerson' is being retrieved from Fongo to be returned in the overridden
		 * retrievePersonMongoData method.
		 */
		markPerson = this.personRepository.getValue(null, "Mark");
		
		
		/*
		 * White box test of REST service
		 * The application will expect a response from the database
		 * not knowing that it will be using Fongo loaded above and not MongoDB
		 */
		Person resp = peopleServiceImpl.getPersonInformation(null, null, "Mark");
		
		
		assertTrue(resp.getAge() == 20);
		assertTrue(resp.getName().equals("Mark"));
	}
	

	@Configuration
	@EnableMongoRepositories
	@ComponentScan(basePackageClasses = {PersonRepository.class})
	// modified to not load configs from com.johnathanmarksmith.mongodb.example.MongoConfiguration
	@PropertySource("classpath:application.properties")
	static class PersonRepositoryTestConfiguration extends AbstractMongoConfiguration
	{

		@Override
		protected String getDatabaseName()
		{
			return "demo-test";
		}

		@Override
		public Mongo mongo()
		{
			// uses fongo for in-memory tests
			return new Fongo("mongo-test").getMongo();
		}

		@Override
		protected String getMappingBasePackage()
		{
			return "com.scivantage.tests.medium.component.domain";
		}

	}
}
