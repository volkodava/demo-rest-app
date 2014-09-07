package com.demo.web.rest.resources;

import com.demo.web.security.AuthenticationUserDetailsService;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.demo.web.transfer.UserTransfer;
import javax.ws.rs.GET;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Path("/user")
public class UserResource {

    @Value("${facebook.apiKey}")
    private String apiKey;

    @Value("${facebook.apiSecret}")
    private String apiSecret;

    @Autowired(required = true)
    private AuthenticationUserDetailsService userDetailsService;

    @Autowired(required = true)
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserTransfer getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) principal;

        Map<String, Boolean> roles = createRolesMap(userDetails);
        UserTransfer userTransfer = new UserTransfer(userDetails.getUsername());
        userTransfer.setRoles(roles);

        return userTransfer;
    }

    @Path("fbAuth")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate() {
        throw new UnsupportedOperationException();
    }

    @Path("authenticate")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@FormParam("username") String username, @FormParam("password") String password) {

        UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication;

        try {
            authentication = authManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        /*
         * Reload user as password of authentication principal will be null after authorization and
         * password is needed for token generation
         */
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Map<String, Boolean> roles = createRolesMap(userDetails);
        String token = userDetailsService.createToken(userDetails);

        UserTransfer userTransfer = new UserTransfer(userDetails.getUsername());
        userTransfer.setRoles(roles);
        userTransfer.setToken(token);

        Response response = Response.status(Response.Status.OK).entity(userTransfer).build();
        return response;
    }

    private Map<String, Boolean> createRolesMap(UserDetails userDetails) {
        Map<String, Boolean> roles = new HashMap<String, Boolean>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }

        return roles;
    }
}
