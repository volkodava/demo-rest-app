package com.demo.web.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User extends BusinessEntity {

    @Column(unique = true, length = 16, nullable = false)
    private String name;

    @Column(length = 64, nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<String>();

    public User() {
    }

    public User(String name, String passwordHash) {

        this.name = name;
        this.password = passwordHash;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Set<String> getRoles() {

        return this.roles;
    }

    public void setRoles(Set<String> roles) {

        this.roles = roles;
    }

    public void addRole(String role) {

        this.roles.add(role);
    }

    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
}
