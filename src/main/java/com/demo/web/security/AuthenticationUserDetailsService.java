package com.demo.web.security;

import com.demo.web.entity.User;
import com.demo.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("authenticationUserDetailsService")
public class AuthenticationUserDetailsService implements UserDetailsService {

    private static final String MAGIC_KEY = "MyMagicKey-right_here";

    @Autowired(required = true)
    private UserRepository userRepository;

    @Autowired(required = true)
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findUserByName(username);

        if (user == null) {
            return null;
        }

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
            username, user.getPassword(), getGrantedAuthorities(user));

        return userDetails;
    }

    public String createToken(UserDetails userDetails) {

        // Expires in one hour
        long expires = System.currentTimeMillis() + 1000L * 60 * 60;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(createEncryptedSignature(userDetails, expires));

        return tokenBuilder.toString();
    }

    public String getUserNameFromToken(String authToken) {

        if (null == authToken) {
            return null;
        }

        String[] parts = authToken.split(":");
        return parts[0];
    }

    public boolean validateToken(String authToken, UserDetails userDetails) {

        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String encryptedSignature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }

        String rawSignature = createRawSignature(userDetails, expires);
        boolean isValid = passwordEncoder.matches(rawSignature, encryptedSignature);
        return isValid;
    }

    private String createEncryptedSignature(UserDetails userDetails, long expires) {

        String rawSignature = createRawSignature(userDetails, expires);

        return passwordEncoder.encode(rawSignature);
    }

    private String createRawSignature(UserDetails userDetails, long expires) {

        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(AuthenticationUserDetailsService.MAGIC_KEY);

        return signatureBuilder.toString();
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(User user) {

        Set<String> roles = user.getRoles();

        if (roles == null) {
            return Collections.emptyList();
        }

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
