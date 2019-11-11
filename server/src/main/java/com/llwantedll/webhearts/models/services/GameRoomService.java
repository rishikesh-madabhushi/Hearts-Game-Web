package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomDetailsWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomForm;
import com.llwantedll.webhearts.models.dtolayer.wrappers.PaginatedWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.exceptions.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.gameapi.entities.Game;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@Service
public interface GameRoomService {
    void join(GameRoom gameRoom, User name) throws UserAlreadyInGameRoomException, NoGameFoundException, FullRoomException;

    void leaveUser(GameRoom gameRoom, User user) throws NoGameFoundException;

    void leaveAllByUser(User user);

    Game getGame(GameRoom gameRoom) throws NoGameFoundException;

    GameRoom create(GameRoomForm gameRoomForm) throws NoGameFoundException, UserPrincipalNotFoundException;

    GameRoom getByName(String name);

    GameRoom saveGame(GameRoom gameRoom, Object serializedDetails);

    GameRoom saveRoom(GameRoom gameRoom);

    List<GameRoom> getRoomsPage(int page);

    PaginatedWrapper<GameRoomDetailsWrapper> getOpenRoomsPaginated(int page);

    long getOpenRoomsPagesCount();

    boolean isExistByName(String name);

    boolean isReadyToStart(GameRoom gameRoom) throws NoGameFoundException;
}
