package com.llwantedll.webhearts.models.gameapi.entities;

public interface GameDetails {
    int getMinPlayers();
    int getMaxPlayers();
    String getTitle();

    void setMinPlayers(int minPlayers);
    void setMaxPlayers(int maxPlayers);
    void setTitle(String title);
}
