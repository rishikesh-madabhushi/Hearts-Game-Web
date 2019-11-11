package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services;

import com.llwantedll.webhearts.models.dtolayer.wrappers.GameRoomWrapper;
import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.*;
import com.llwantedll.webhearts.models.gameapi.exceptions.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoGameFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoPlayerFoundException;
import com.llwantedll.webhearts.models.gameapi.exceptions.UserAlreadyInGameRoomException;

public interface StandardHeartsGameService {
    GameRoomWrapper readyUser(boolean ready, String gameRoomTitle, User remoteUser) throws NoGameFoundException, NoPlayerFoundException;

    GameRoomWrapper connectUser(String roomName, User remoteUser) throws NoGameFoundException, UserAlreadyInGameRoomException, FullRoomException;

    GameRoomWrapper getGameDetails(String gameRoomTitle, User remoteUser) throws NoGameFoundException, NoPlayerFoundException;

    void setGameRoomWorkflow(GameRoom byName, GameStatus gameStatus);
}
