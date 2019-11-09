package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.entities.User;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.socket.messaging.SessionConnectEvent;

public interface WebSocketAuthenticationService {
    UsernamePasswordAuthenticationToken authenticate(String username, String password) throws AuthenticationException;
    User getRemote(SimpMessageHeaderAccessor headerAccessor) throws AuthenticationException;
}
