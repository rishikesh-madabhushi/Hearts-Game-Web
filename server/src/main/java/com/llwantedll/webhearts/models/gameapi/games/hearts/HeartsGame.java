package com.llwantedll.webhearts.models.gameapi.games.hearts;

import com.llwantedll.webhearts.models.gameapi.cards.CardDeck;
import com.llwantedll.webhearts.models.gameapi.entities.Game;

public interface HeartsGame extends Game {
    CardDeck getCardDeck();
}
