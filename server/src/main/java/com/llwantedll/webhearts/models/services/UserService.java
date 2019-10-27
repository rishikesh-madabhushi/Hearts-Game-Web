package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.dtolayer.wrappers.UserForm;
import com.llwantedll.webhearts.models.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User create(UserForm userForm);

    User getByLogin(String login);

    User getByEmail(String email);

    boolean isLoginExists(String login);
}
