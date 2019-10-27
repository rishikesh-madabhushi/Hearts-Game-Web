package com.llwantedll.webhearts.models.gameapi.entities;

import java.util.List;

public interface Game {
    void start();
    void pause();
    void end();
    int getMaxUsers();
    int getMinUsers();
    List<Player> getPlayers();
}
