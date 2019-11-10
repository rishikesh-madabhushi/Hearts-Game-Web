package com.llwantedll.webhearts.models.dtolayer.wrappers;

import java.util.List;

public class UserDetailsWrapper {
    private String login;
    private String password;
    private String email;
    private List<RoleWrapper> roles;

    public UserDetailsWrapper() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleWrapper> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleWrapper> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
