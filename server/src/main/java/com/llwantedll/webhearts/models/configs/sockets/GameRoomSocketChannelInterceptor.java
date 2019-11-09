package com.llwantedll.webhearts.models.configs.sockets;

import com.llwantedll.webhearts.models.services.WebSocketAuthenticationService;
import com.llwantedll.webhearts.models.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("GameRoomSocketChannelInterceptor")
public class GameRoomSocketChannelInterceptor implements ChannelInterceptor {

    private static final String AUTH_TOKEN_HEADER = "authToken";

    private final WebSocketAuthenticationService authenticationService;

    @Autowired
    public GameRoomSocketChannelInterceptor(WebSocketAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String token = accessor.getFirstNativeHeader(AUTH_TOKEN_HEADER);
            final String username = AuthUtils.getNameFromHeader(token);
            final String password = AuthUtils.getPasscodeFromHeader(token);

            final UsernamePasswordAuthenticationToken user = authenticationService.authenticate(username, password);

            accessor.setUser(user);
        }
        return message;
    }
}
