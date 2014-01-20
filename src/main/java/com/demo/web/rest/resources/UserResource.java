package com.demo.web.rest.resources;

import com.demo.web.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.demo.web.rest.TokenUtils;
import com.demo.web.transfer.UserTransfer;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired(required = true)
    private UserRepository userRepository;

    @Autowired(required = true)
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    @Autowired(required = true)
    private TokenUtils tokenUtils;

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

        Map<String, Boolean> roles = new HashMap<String, Boolean>();

        /*
         * Reload user as password of authentication principal will be null after authorization and
         * password is needed for token generation
         */
        UserDetails userDetails = userRepository.loadUserByUsername(username);

        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.toString(), Boolean.TRUE);
        }

        String token = tokenUtils.createToken(userDetails);
        UserTransfer userTransfer = new UserTransfer(userDetails.getUsername(), roles, token);

        Response response = Response.status(Response.Status.OK).entity(userTransfer).build();
        return response;
    }

}
