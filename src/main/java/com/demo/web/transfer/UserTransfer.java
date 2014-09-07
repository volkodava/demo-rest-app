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

    public UserTransfer(String userName) {
        this.name = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Boolean> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Boolean> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return name + " [roles=" + roles + ", token=" + token + ']';
    }
}
