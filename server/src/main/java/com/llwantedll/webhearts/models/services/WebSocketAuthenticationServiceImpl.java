package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class WebSocketAuthenticationServiceImpl implements WebSocketAuthenticationService {

    private final UserService userService;

    @Autowired
    public WebSocketAuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UsernamePasswordAuthenticationToken authenticate(String username, String password) throws AuthenticationException {
        if (Objects.isNull(username) || username.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }
        if (Objects.isNull(password) || password.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Password was null or empty.");
        }

        User user = userService.getByLogin(username);

        if (Objects.isNull(user)) {
            throw new BadCredentialsException("Password was null or empty.");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BadCredentialsException("Password was null or empty.");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles()
                .forEach(e -> grantedAuthorities
                        .add(new SimpleGrantedAuthority(e.getKey())));

        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }

    @Override
    public User getRemote(SimpMessageHeaderAccessor headerAccessor) throws AuthenticationException {
        Principal principalUser = headerAccessor.getUser();

        if (Objects.isNull(principalUser)) {
            throw new AuthenticationCredentialsNotFoundException("No remote principal was found");
        }

        User user = userService.getByLogin(principalUser.getName());

        if (Objects.isNull(user)) {
            throw new AuthenticationCredentialsNotFoundException("No such principal username was found in the database");
        }

        return user;
    }
}
