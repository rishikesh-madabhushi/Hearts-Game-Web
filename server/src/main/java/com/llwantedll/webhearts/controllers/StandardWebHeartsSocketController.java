package com.llwantedll.webhearts.controllers;

import com.google.gson.Gson;
import com.llwantedll.webhearts.models.configs.ConfigurationData;
import com.llwantedll.webhearts.models.configs.PathConfiguration;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomWrapper;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.exceptions.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoPlayerFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services.StandardHeartsGameService;
import com.llwantedll.webhearts.models.services.AuthenticationService;
import com.llwantedll.webhearts.models.services.WebSocketAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@Controller
@CrossOrigin(ConfigurationData.CROSS_ORIGIN_URL)
public class StandardWebHeartsSocketController {

    private static final String ROOM_NAME_HEADER = "roomName";
    private static final String READY_ATTRIBUTE = "ready";
    private static final String READY_FLAG = "true";

    private final SimpMessagingTemplate template;

    private final StandardHeartsGameService standardHeartsGameService;

    private final AuthenticationService authenticationService;

    private final WebSocketAuthenticationService webSocketAuthenticationService;

    @Autowired
    public StandardWebHeartsSocketController(SimpMessagingTemplate template,
                                             StandardHeartsGameService standardHeartsGameService,
                                             WebSocketAuthenticationService webSocketAuthenticationService,
                                             AuthenticationService authenticationService) {
        this.template = template;
        this.standardHeartsGameService = standardHeartsGameService;
        this.webSocketAuthenticationService = webSocketAuthenticationService;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/game_room/{gameRoom}/get",
            consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity getGameInfo(@PathVariable("gameRoom") String gameRoomTitle) throws UserPrincipalNotFoundException, NoPlayerFoundException, NoGameFoundException {
        User remoteUser = authenticationService.getRemoteUser();
        GameRoomWrapper gameDetails = standardHeartsGameService.getGameDetails(gameRoomTitle, remoteUser);

        return ResponseEntity.ok(new Gson().toJson(gameDetails));
    }

    @MessageMapping({"/game_room/{gameRoom}/ready"})
    public void getReady(@DestinationVariable("gameRoom") String gameRoomTitle,
                         Message<?> message) throws UserPrincipalNotFoundException, NoPlayerFoundException, NoGameFoundException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
        String readyString = headers.getFirstNativeHeader(READY_ATTRIBUTE);
        User remoteUser = webSocketAuthenticationService.getRemote(headers);
        boolean ready = Objects.nonNull(readyString) && readyString.equals(READY_FLAG);

        template.convertAndSend(PathConfiguration.WEBSOCKET_TOPIC_PREFIX + gameRoomTitle, standardHeartsGameService.readyUser(ready, gameRoomTitle, remoteUser));
    }


    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) throws AuthenticationException, NoGameFoundException, UserAlreadyInGameRoomException, FullRoomException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        User remoteUser = webSocketAuthenticationService.getRemote(headers);
        String gameRoomTitle = headers.getFirstNativeHeader(ROOM_NAME_HEADER);

        template.convertAndSend(PathConfiguration.WEBSOCKET_TOPIC_PREFIX + gameRoomTitle, standardHeartsGameService.connectUser(gameRoomTitle, remoteUser));
    }

 /*
    //THIS IS DISCONNECT EVENT LISTENER THAT WORKS WITH DELAY
    //AND CAUSE TROUBLES WITH PAGE REFRESH AND RECONNECT

    @EventListener
    private void handleSessionDisconnected(SessionDisconnectEvent event) throws NoGameFoundException, UserPrincipalNotFoundException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        User remoteUser = webSocketAuthenticationService.getRemote(headers);

        gameRoomService.leaveAllByUser(remoteUser);
        globalUpdateSign();

    private void globalUpdateSign() {
        template.convertAndSend("/topic/message/global/update", true);
    }
    }*/

}
