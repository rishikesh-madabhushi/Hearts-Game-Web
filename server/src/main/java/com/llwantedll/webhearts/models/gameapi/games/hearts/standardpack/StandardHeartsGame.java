package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.CardDeck;
import com.llwantedll.webhearts.models.gameapi.cards.standardpack.StandardCardDeck;
import com.llwantedll.webhearts.models.gameapi.entities.Player;
import com.llwantedll.webhearts.models.gameapi.games.hearts.HeartsGame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StandardHeartsGame implements HeartsGame {
    private static final int PLAYERS_IN_GAME = 4;
    private List<StandardHeartsGamePlayer> players = new ArrayList<>(PLAYERS_IN_GAME);
    private CardDeck cardDeck = StandardCardDeck.getInstance();

    public StandardHeartsGame(List<StandardHeartsGamePlayer> players) {
        this.players = players;
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void end() {

    }

    @Override
    public int getMaxUsers() {
        return PLAYERS_IN_GAME;
    }

    @Override
    public int getMinUsers() {
        return PLAYERS_IN_GAME;
    }

    @Override
    public List<Player> getPlayers() {
        return players
                .stream()
                .map(e -> (Player) e)
                .collect(Collectors.toList());
    }

    @Override
    public CardDeck getCardDeck() {
        return cardDeck;
    }
}
