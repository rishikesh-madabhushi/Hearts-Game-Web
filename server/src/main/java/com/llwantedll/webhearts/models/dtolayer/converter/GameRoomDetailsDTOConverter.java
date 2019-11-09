package com.llwantedll.webhearts.models.dtolayer.converter;

import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomDetailsWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.entities.Game;
import com.llwantedll.webhearts.models.repositories.GameRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class GameRoomDetailsDTOConverter implements DTOConverter<GameRoom, GameRoomDetailsWrapper> {

    private final static String UNKNOWN_GAME_NAME = "Unknown game";

    private final GameRoomRepository gameRoomService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    @Autowired
    public GameRoomDetailsDTOConverter(GameRoomRepository gameRoomService) {
        this.gameRoomService = gameRoomService;
    }

    @Override
    public GameRoom forward(GameRoomDetailsWrapper dto) {
        return gameRoomService.getByName(dto.getName());
    }

    @Override
    public GameRoomDetailsWrapper backward(GameRoom entity) {
        GameRoomDetailsWrapper gameSession = new GameRoomDetailsWrapper();

        List<User> users = entity.getUsers();
        Object gameData = entity.getGameData();

        if (!(gameData instanceof Game)) {
            gameSession.setGameTitle(UNKNOWN_GAME_NAME);
        } else {
            Game game = (Game) gameData;
            gameSession.setGameTitle(game.getGameDetails().getTitle());
        }

        gameSession.setName(entity.getName());
        gameSession.setHasPassword(StringUtils.isEmpty(entity.getPassword()));
        gameSession.setMaxPlayers(entity.getMaxPlayers());
        gameSession.setMinPlayers(entity.getMinPlayers());
        gameSession.setStartDate(dateFormat.format(entity.getStartedDate()));
        gameSession.setCurrentPlayers(users.size());

        return gameSession;
    }
}
