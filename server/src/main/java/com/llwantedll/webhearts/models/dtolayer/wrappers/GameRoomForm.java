package com.llwantedll.webhearts.models.dtolayer.wrappers;

public class GameRoomForm {
    private String name;
    private String password;
    private String gameTitle;

    public GameRoomForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
}
