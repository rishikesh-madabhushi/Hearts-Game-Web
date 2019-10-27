package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.configs.security.CustomUserDetails;
import com.llwantedll.webhearts.models.dtolayer.converter.DTOConverter;
import com.llwantedll.webhearts.models.dtolayer.wrappers.UserDetailsWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.UserForm;
import com.llwantedll.webhearts.models.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final DTOConverter<User, UserDetailsWrapper> userDTOConverter;

    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    public AuthenticationServiceImpl(UserService userService,
                                     @Qualifier(value = "UserDetailsServiceImpl") UserDetailsService userDetailsService,
                                     AuthenticationManager authenticationManager,
                                     DTOConverter<User, UserDetailsWrapper> userDTOConverter) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userDTOConverter = userDTOConverter;
    }

    @Override
    public User getRemoteUser() throws UserPrincipalNotFoundException {
        Object userDetailsObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userDetailsObj instanceof CustomUserDetails)) {
            throw new UserPrincipalNotFoundException("No remote user details was found");
        }
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsObj;
        UserDetailsWrapper user = userDetails.getUser();
        return userDTOConverter.forward(user);
    }

    @Override
    public boolean isRemoteUser(User user) throws UserPrincipalNotFoundException {
        User remoteUser = getRemoteUser();

        return !Objects.isNull(user) &&
                user.getLogin().equals(remoteUser.getLogin());

    }

    @Override
    public boolean login(String login, String password) {
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(login);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, login, user.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.debug("User " + login + " successfully logged in");
            return true;
        }
        return false;
    }

    @Override
    public void register(UserForm userForm) {
        userService.create(userForm);
        String login = userForm.getLogin();
        String password = userForm.getConfirmPassword();
        login(login, password);
    }
}
