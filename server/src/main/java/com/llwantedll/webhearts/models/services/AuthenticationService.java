package com.llwantedll.webhearts.models.services;


import com.llwantedll.webhearts.models.dtolayer.wrappers.UserForm;
import com.llwantedll.webhearts.models.entities.User;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface AuthenticationService {
    User getRemoteUser() throws UserPrincipalNotFoundException;
    boolean isRemoteUser(User user) throws UserPrincipalNotFoundException;
    boolean login(String login, String password);
    void register(UserForm userForm);
}
