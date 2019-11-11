package com.llwantedll.webhearts.models.dtolayer.converter;

import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.gameapi.entities.Game;
import com.llwantedll.webhearts.models.repositories.GameRoomRepository;
import com.llwantedll.webhearts.models.services.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

@Component
public class GameRoomDTOConverter implements DTOConverter<GameRoom, GameRoomWrapper> {

    private final GameRoomRepository gameRoomService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    @Autowired
    public GameRoomDTOConverter(GameRoomRepository gameRoomService) {
        this.gameRoomService = gameRoomService;
    }

    @Override
    public GameRoom forward(GameRoomWrapper dto) {
        return gameRoomService.getByName(dto.getName());
    }

    @Override
    public GameRoomWrapper backward(GameRoom entity) {
        GameRoomWrapper gameSession = new GameRoomWrapper();

        gameSession.setGameData(entity.getGameData());
        gameSession.setName(entity.getName());
        gameSession.setHasPassword(StringUtils.isEmpty(entity.getPassword()));
        gameSession.setMaxPlayers(entity.getMaxPlayers());
        gameSession.setMinPlayers(entity.getMinPlayers());
        gameSession.setStartDate(dateFormat.format(entity.getStartedDate()));
        gameSession.setStatus(entity.getStatus());

        return gameSession;
    }
}
