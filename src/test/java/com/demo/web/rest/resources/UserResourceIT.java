package com.demo.web.rest.resources;

import com.demo.web.transfer.UserTransfer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserResourceIT {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String endpointUrl;

    @BeforeClass
    public static void beforeClass() {
        endpointUrl = System.getProperty("service.url");
    }

    @Test
    public void testAuthenticationWithValidCredentials() throws Exception {
        final String URL = endpointUrl + "/rest/user/authenticate";
        final String USERNAME = "user";
        final String PASSWORD = "user";

        logger.info("Service url: {}", URL);

        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(config);
        WebResource webResource = client.resource(UriBuilder.fromUri(URL).build());
        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
        formData.add("username", USERNAME);
        formData.add("password", PASSWORD);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).
            accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, formData);

        int statusCode = response.getStatus();
        logger.info("Response status code: {}", statusCode);

        assertThat(statusCode, is(equalTo(Status.OK.getStatusCode())));

        UserTransfer entity = response.getEntity(new GenericType<UserTransfer>() {
        });
        logger.info("Response: {}", entity);

        assertThat(entity, is(not(nullValue())));
        assertThat(entity.getName(), is(equalTo(USERNAME)));
        assertThat(entity.getToken(), is(not(nullValue())));
        assertThat(entity.getToken().length(), is(greaterThan(0)));
        assertThat(entity.getRoles(), is(not(nullValue())));
        assertThat(entity.getRoles().size(), is(greaterThan(0)));
        assertThat(entity.getRoles().get("user"), is(not(nullValue())));
        assertThat(entity.getRoles().get("user"), is(equalTo(true)));
    }
}
