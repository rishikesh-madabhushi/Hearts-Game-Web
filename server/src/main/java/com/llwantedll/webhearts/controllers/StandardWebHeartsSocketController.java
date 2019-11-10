package com.llwantedll.webhearts.controllers;

import com.google.gson.Gson;
import com.llwantedll.webhearts.models.configs.ConfigurationData;
import com.llwantedll.webhearts.models.dtolayer.converter.DTOConverter;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.NoPlayerFoundException;
import com.llwantedll.webhearts.models.gameapi.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGame;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGamePlayer;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services.GameFlowService;
import com.llwantedll.webhearts.models.services.AuthenticationService;
import com.llwantedll.webhearts.models.services.GameRoomService;
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
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Objects;

@Controller
@CrossOrigin(ConfigurationData.CROSS_ORIGIN_URL)
public class StandardWebHeartsSocketController {

    private static final String ROOM_NAME_HEADER = "roomName";
    private static final String READY_FLAG = "true";
    private static final String READY_ATTRIBUTE = "ready";

    private final SimpMessagingTemplate template;

    private final GameRoomService gameRoomService;

    private final GameFlowService gameFlowService;

    private final WebSocketAuthenticationService webSocketAuthenticationService;

    private final DTOConverter<GameRoom, GameRoomWrapper> gameRoomDTOConverter;


    private final AuthenticationService authenticationService;

    @Autowired
    public StandardWebHeartsSocketController(SimpMessagingTemplate template,
                                             GameRoomService gameRoomService,
                                             GameFlowService gameFlowService,
                                             WebSocketAuthenticationService webSocketAuthenticationService,
                                             DTOConverter<GameRoom, GameRoomWrapper> gameRoomDTOConverter, AuthenticationService authenticationService) {
        this.template = template;
        this.gameRoomService = gameRoomService;
        this.gameFlowService = gameFlowService;
        this.webSocketAuthenticationService = webSocketAuthenticationService;
        this.gameRoomDTOConverter = gameRoomDTOConverter;
        this.authenticationService = authenticationService;
    }

    @GetMapping(value = "/game_room/{gameRoom}/get",
            consumes = MimeTypeUtils.APPLICATION_JSON_VALUE,
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity getGameInfo(@PathVariable("gameRoom") String gameRoomTitle) throws UserPrincipalNotFoundException, NoPlayerFoundException, NoGameFoundException {
        User remoteUser = authenticationService.getRemoteUser();

        StandardHeartsGame standardHeartsGame = getStandardHeartsGame(gameRoomTitle);

        StandardHeartsGamePlayer playerByUser = gameFlowService.getPlayerByUser(standardHeartsGame, remoteUser);

        StandardHeartsGame cardHidedGame = gameFlowService.hideOtherUserCards(standardHeartsGame, playerByUser);

        GameRoom byName = gameRoomService.getByName(gameRoomTitle);

        if (Objects.isNull(byName)) {
            throw new NoGameFoundException();
        }

        byName.setGameData(cardHidedGame);

        return ResponseEntity.ok(new Gson().toJson(gameRoomDTOConverter.backward(byName)));
    }

    @MessageMapping({"/game_room/{gameRoom}/ready"})
    public void getReady(@DestinationVariable("gameRoom") String gameRoomTitle,
                         Message<?> message) throws UserPrincipalNotFoundException, NoPlayerFoundException, NoGameFoundException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);

        StandardHeartsGame standardHeartsGame = getStandardHeartsGame(gameRoomTitle);

        String readyString = headers.getFirstNativeHeader(READY_ATTRIBUTE);

        GameRoom byName = gameRoomService.getByName(gameRoomTitle);

        if (Objects.isNull(byName)) {
            throw new NoGameFoundException();
        }

        User remoteUser = webSocketAuthenticationService.getRemote(headers);

        boolean ready = Objects.nonNull(readyString) && readyString.equals(READY_FLAG);

        GameRoom gameRoom = gameFlowService.readyPlayer(byName, standardHeartsGame, gameFlowService.getPlayerByUser(standardHeartsGame, remoteUser), ready);

        template.convertAndSend("/topic/message/" + gameRoomTitle, gameRoomDTOConverter.backward(gameRoom));
    }

    private StandardHeartsGame getStandardHeartsGame(String gameRoom) {
        GameRoom byName = gameRoomService.getByName(gameRoom);
        return (StandardHeartsGame) byName.getGameData();
    }

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) throws AuthenticationException, NoGameFoundException, UserAlreadyInGameRoomException, FullRoomException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        User remoteUser = webSocketAuthenticationService.getRemote(headers);

        Object roomName = headers.getFirstNativeHeader(ROOM_NAME_HEADER);

        if (Objects.isNull(roomName)) {
            throw new NoGameFoundException();
        }

        GameRoom byName = gameRoomService.getByName(roomName.toString());

        if (Objects.isNull(byName)) {
            throw new NoGameFoundException();
        }

        gameRoomService.join(byName, remoteUser);

        template.convertAndSend("/topic/message/" + roomName.toString(), getStandardHeartsGame(roomName.toString()));
    }

    @EventListener
    private void handleSessionDisconnected(SessionDisconnectEvent event) throws NoGameFoundException, UserPrincipalNotFoundException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        User remoteUser = webSocketAuthenticationService.getRemote(headers);

        gameRoomService.leaveAllByUser(remoteUser);
        globalUpdateSign();
    }

    private void globalUpdateSign() {
        template.convertAndSend("/topic/message/global/update", true);
    }

}
