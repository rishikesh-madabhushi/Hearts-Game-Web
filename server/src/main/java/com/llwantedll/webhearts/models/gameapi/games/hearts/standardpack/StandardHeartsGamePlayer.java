package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.Card;
import com.llwantedll.webhearts.models.gameapi.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class StandardHeartsGamePlayer implements Player {
    private static final int CARDS_COUNT_AT_START = 13;
    private static final int LOSE_POINTS_FROM_START = 0;

    private final String login;

    private boolean ready = false;

    private int losePoints = LOSE_POINTS_FROM_START;

    private List<Card> cards = new ArrayList<>(CARDS_COUNT_AT_START);

    public StandardHeartsGamePlayer(String login) {
        this.login = login;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String getIpAddress() {
        return null;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getLosePoints() {
        return losePoints;
    }

    public void setLosePoints(int losePoints) {
        this.losePoints = losePoints;
    }

    public void addLosePoints(int losePoints) {
        this.losePoints = this.losePoints + losePoints;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
