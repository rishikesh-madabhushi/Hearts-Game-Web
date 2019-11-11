package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services;

import com.llwantedll.webhearts.models.configs.PathConfiguration;
import com.llwantedll.webhearts.models.dtolayer.converter.DTOConverter;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.*;
import com.llwantedll.webhearts.models.gameapi.exceptions.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoPlayerFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGame;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGamePlayer;
import com.llwantedll.webhearts.models.services.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StandardHeartsGameServiceImpl implements StandardHeartsGameService {

    private final GameRoomService gameRoomService;

    private final DTOConverter<GameRoom, GameRoomWrapper> gameRoomDTOConverter;

    private final GameFlowService gameFlowService;

    private final SimpMessagingTemplate template;

    @Autowired
    public StandardHeartsGameServiceImpl(GameRoomService gameRoomService,
                                         DTOConverter<GameRoom, GameRoomWrapper> gameRoomDTOConverter,
                                         GameFlowService gameFlowService,
                                         SimpMessagingTemplate template) {
        this.gameRoomService = gameRoomService;
        this.gameRoomDTOConverter = gameRoomDTOConverter;
        this.gameFlowService = gameFlowService;
        this.template = template;
    }

    @Override
    public GameRoomWrapper readyUser(boolean ready, String gameRoomTitle, User remoteUser) throws NoGameFoundException, NoPlayerFoundException {
        StandardHeartsGame standardHeartsGame = getStandardHeartsGame(gameRoomTitle);

        GameRoom byName = gameRoomService.getByName(gameRoomTitle);

        if (Objects.isNull(byName)) {
            throw new NoGameFoundException();
        }

        GameRoom gameRoom = gameFlowService.readyPlayer(byName, standardHeartsGame, gameFlowService.getPlayerByUser(standardHeartsGame, remoteUser), ready);

        if(gameRoomService.isReadyToStart(gameRoom)){
            standardHeartsGame.start();
            gameRoom.setGameData(standardHeartsGame);
            setGameRoomWorkflow(gameRoom, GameStatus.STARTED);
        }

        return gameRoomDTOConverter.backward(gameRoom);
    }

    @Override
    public void setGameRoomWorkflow(GameRoom byName, GameStatus gameStatus){
        byName.setStatus(gameStatus.name());
        gameRoomService.saveRoom(byName);
        template.convertAndSend(PathConfiguration.WEBSOCKET_TOPIC_PREFIX + byName.getName() + PathConfiguration.WEBSOCKET_WORKFLOW_POSTFIX, gameStatus.name());
    }

    @Override
    public GameRoomWrapper connectUser(String roomName, User remoteUser) throws NoGameFoundException, UserAlreadyInGameRoomException, FullRoomException {
        if (Objects.isNull(roomName)) {
            throw new NoGameFoundException();
        }

        GameRoom byName = gameRoomService.getByName(roomName);

        if (Objects.isNull(byName)) {
            throw new NoGameFoundException();
        }

        gameRoomService.join(byName, remoteUser);

        return gameRoomDTOConverter.backward(byName);
    }

    @Override
    public GameRoomWrapper getGameDetails(String gameRoomTitle, User remoteUser) throws NoGameFoundException, NoPlayerFoundException {

        StandardHeartsGame standardHeartsGame = getStandardHeartsGame(gameRoomTitle);

        StandardHeartsGamePlayer playerByUser = gameFlowService.getPlayerByUser(standardHeartsGame, remoteUser);

        gameFlowService.hideOtherUserCards(standardHeartsGame, playerByUser);

        GameRoom byName = gameRoomService.getByName(gameRoomTitle);

        if (Objects.isNull(byName)) {
            throw new NoGameFoundException();
        }

        byName.setGameData(standardHeartsGame);

        return gameRoomDTOConverter.backward(byName);
    }

    private StandardHeartsGame getStandardHeartsGame(String gameRoom) {
        GameRoom byName = gameRoomService.getByName(gameRoom);
        return (StandardHeartsGame) byName.getGameData();
    }
}
