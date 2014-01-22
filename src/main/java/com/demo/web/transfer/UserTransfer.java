package com.demo.web.transfer;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserTransfer {

    private String name;

    private Map<String, Boolean> roles;

    private String token;

    public UserTransfer() {
    }

    public UserTransfer(String userName, Map<String, Boolean> roles, String token) {

        this.name = userName;
        this.roles = roles;
        this.token = token;
    }

    public String getName() {

        return this.name;
    }

    public Map<String, Boolean> getRoles() {

        return this.roles;
    }

    public String getToken() {

        return this.token;
    }

    @Override
    public String toString() {
        return name + " [roles=" + roles + ", token=" + token + ']';
    }
}
