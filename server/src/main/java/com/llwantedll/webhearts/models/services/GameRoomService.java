package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomDetailsWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomForm;
import com.llwantedll.webhearts.models.dtolayer.wrappers.PaginatedWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.UserAlreadyInGameRoomException;
import com.llwantedll.webhearts.models.gameapi.entities.Game;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
public interface GameRoomService {
    GameRoom create(GameRoomForm gameRoomForm) throws NoGameFoundException, UserPrincipalNotFoundException;

    void join(GameRoom gameRoom, User name) throws UserAlreadyInGameRoomException, NoGameFoundException, FullRoomException;

    void leaveUser(GameRoom gameRoom, User user) throws NoGameFoundException;

    Game getGame(GameRoom gameRoom) throws NoGameFoundException;

    void leaveAllByUser(User user);

    GameRoom getByName(String name);

    boolean isExistByName(String name);

    List<GameRoom> getRoomsPage(int page);

    long getOpenRoomsPagesCount();

    PaginatedWrapper<GameRoomDetailsWrapper> getOpenRoomsPaginated(int page);

    GameRoom saveGame(GameRoom gameRoom, Object serializedDetails);
}
