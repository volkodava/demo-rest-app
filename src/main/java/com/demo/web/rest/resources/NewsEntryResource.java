package com.demo.web.rest.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.demo.web.JsonViews;
import com.demo.web.entity.NewsEntry;
import com.demo.web.repository.NewsEntryRepository;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Path("/news")
public class NewsEntryResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = true)
    private NewsEntryRepository newsEntryRepository;

    @Autowired(required = true)
    private ObjectMapper mapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() throws JsonGenerationException, JsonMappingException, IOException {

        logger.info("list()");

        ObjectWriter viewWriter;
        if (this.isAdmin()) {
            viewWriter = mapper.writerWithView(JsonViews.Admin.class);
        } else {
            viewWriter = mapper.writerWithView(JsonViews.User.class);
        }
        List<NewsEntry> allEntries = newsEntryRepository.findAll();

        String valueAsString = viewWriter.writeValueAsString(allEntries);
        Response response = Response.status(Response.Status.OK).entity(valueAsString).build();

        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response read(@PathParam("id") Long id) {

        logger.info("read(id)");

        NewsEntry newsEntry = newsEntryRepository.findOne(id);
        if (newsEntry == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Response response = Response.status(Response.Status.OK).entity(newsEntry).build();

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(NewsEntry newsEntry) {

        logger.info("create(): " + newsEntry);

        NewsEntry savedEntry = newsEntryRepository.save(newsEntry);
        Response response = Response.status(Response.Status.OK).entity(savedEntry).build();

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(@PathParam("id") Long id, NewsEntry newsEntry) {

        logger.info("update(): " + newsEntry);

        NewsEntry savedEntry = newsEntryRepository.save(newsEntry);
        Response response = Response.status(Response.Status.OK).entity(savedEntry).build();

        return response;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {

        logger.info("delete(id)");

        newsEntryRepository.delete(id);

        Response response = Response.status(Response.Status.OK).build();

        return response;
    }

    private boolean isAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).equalsIgnoreCase("anonymousUser")) {
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.toString().equals("admin")) {
                return true;
            }
        }

        return false;
    }
}
