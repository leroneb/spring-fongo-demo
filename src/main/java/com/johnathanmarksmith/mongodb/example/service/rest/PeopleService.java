package com.johnathanmarksmith.mongodb.example.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.johnathanmarksmith.mongodb.example.domain.Person;

@Path("v1/people")
public interface PeopleService {

	@GET
	@Path("/person/{s}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public Person getPersonInformation(
			@Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@PathParam("id") String id);
}
