package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services;

import com.llwantedll.webhearts.models.gameapi.cards.CardDeck;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoCardsInDeckException;
import com.llwantedll.webhearts.models.gameapi.cards.standardpack.StandardCard;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGame;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGamePlayer;

public class GameHelper {

    private static final int CARDS_AT_START_AMOUNT = 13;

    public static void giveAllPlayersStartCards(StandardHeartsGame standardHeartsGame) throws NoCardsInDeckException {
        CardDeck<StandardCard> cardDeck = standardHeartsGame.getCardDeck();

        for (StandardHeartsGamePlayer player : standardHeartsGame.getHeartsPlayers()) {
            for (int i = 0; i < CARDS_AT_START_AMOUNT; i++) {
                player.setCard(cardDeck.pullNextCard());
            }
        }
    }
}
