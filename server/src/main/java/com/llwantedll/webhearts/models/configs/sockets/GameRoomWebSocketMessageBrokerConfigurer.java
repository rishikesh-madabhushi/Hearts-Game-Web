package com.llwantedll.webhearts.models.configs.sockets;

import com.llwantedll.webhearts.models.configs.ConfigurationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin(ConfigurationData.CROSS_ORIGIN_URL)
public class GameRoomWebSocketMessageBrokerConfigurer implements WebSocketMessageBrokerConfigurer{

    private final GameRoomHandshakeHandler gameRoomHandshakeHandler;

    private final ChannelInterceptor channelInterceptor;

    @Autowired
    public GameRoomWebSocketMessageBrokerConfigurer(GameRoomHandshakeHandler gameRoomHandshakeHandler,
                                                    @Qualifier("GameRoomSocketChannelInterceptor") ChannelInterceptor channelInterceptor) {
        this.gameRoomHandshakeHandler = gameRoomHandshakeHandler;
        this.channelInterceptor = channelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/messages")
                .setHandshakeHandler(gameRoomHandshakeHandler)
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/", "/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}
