package com.llwantedll.webhearts.models.gameapi.games.hearts;

import com.llwantedll.webhearts.models.gameapi.cards.CardDeck;
import com.llwantedll.webhearts.models.gameapi.entities.Game;

import java.util.List;

public interface HeartsGame<PlayerClass> extends Game {
    CardDeck getCardDeck();

    List<PlayerClass> getHeartsPlayers();

    void setHeartPlayers(List<PlayerClass> players);

    void setPlayerReady(PlayerClass player, boolean ready);
}
