package com.llwantedll.webhearts.models.gameapi;

import com.llwantedll.webhearts.models.gameapi.entities.GameDetails;

public interface GameMapper {
    GameDetails getGameDetailsByTitle(String title) throws NoGameFoundException;
    Object getGameInstanceByGameDetails(GameDetails gameDetails) throws NoGameFoundException;
}
