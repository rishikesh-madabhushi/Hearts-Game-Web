package com.llwantedll.webhearts.models.gameapi;

import com.llwantedll.webhearts.models.gameapi.entities.GameDetails;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGame;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGameDetails;
import org.springframework.stereotype.Component;

@Component
public class GameMapperImpl implements GameMapper {

    @Override
    public GameDetails getGameDetailsByTitle(String title) throws NoGameFoundException {

        GameDetails standardHeartsGame = StandardHeartsGameDetails.getInstance();

        if (standardHeartsGame.getTitle().equals(title)) {
            return standardHeartsGame;
        }

        throw new NoGameFoundException();
    }

    @Override
    public Object getGameInstanceByGameDetails(GameDetails gameDetails) throws NoGameFoundException {
        GameDetails standardHeartsGame = StandardHeartsGameDetails.getInstance();

        if (gameDetails.getTitle().equals(standardHeartsGame.getTitle())) {
            return new StandardHeartsGame();
        }

        throw new NoGameFoundException();
    }
}
