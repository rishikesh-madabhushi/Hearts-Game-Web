package com.llwantedll.webhearts.models.gameapi.entities;

public interface Player {
    String getUsername();
    String getIpAddress();
    boolean isReady();
    void setReady(boolean ready);
}
