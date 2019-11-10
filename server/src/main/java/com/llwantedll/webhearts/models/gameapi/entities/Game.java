package com.llwantedll.webhearts.models.gameapi.entities;

import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.FullRoomException;

import java.util.List;

public interface Game {
    void start();
    void pause();
    void end();
    GameDetails getGameDetails();
    List<Player> getPlayers();
    Player addPlayer(User user) throws FullRoomException;
    void removePlayer(User user);
}
