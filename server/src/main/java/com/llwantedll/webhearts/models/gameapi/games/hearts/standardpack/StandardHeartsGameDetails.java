package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack;

import com.llwantedll.webhearts.models.gameapi.entities.GameDetails;

public class StandardHeartsGameDetails implements GameDetails{

    private int minPlayers = 4;
    private int maxPlayers = 4;
    private String gameTitle = "Standard Hearts";

    private StandardHeartsGameDetails() {

    }

    public static StandardHeartsGameDetails getInstance(){
        return new StandardHeartsGameDetails();
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String getTitle() {
        return gameTitle;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void setTitle(String title) {
        this.gameTitle = title;
    }
}
