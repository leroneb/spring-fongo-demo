package com.johnathanmarksmith.mongodb.example.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.johnathanmarksmith.mongodb.example.domain.Person;

/**
 * Implementing class of the REST web service
 * @author lerone.bleasdille
 *
 */
public class PeopleServiceImpl implements PeopleService
{
	public Person getPersonInformation(@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@PathParam("id") String name)
	{

		PersonResource res = getResourceFactory();
		return res.getPersonInformation(headers, request, name);
	}

	
	/**
	 * Could be a Factory to get a specific resource for your project
	 * @param res
	 * @return
	 */
	protected PersonResource getResourceFactory()
	{
		PersonResource ret = new PersonResource();
		return ret;
	}
}
