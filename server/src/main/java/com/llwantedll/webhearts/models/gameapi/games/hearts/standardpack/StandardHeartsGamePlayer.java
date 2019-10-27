package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.Card;
import com.llwantedll.webhearts.models.gameapi.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class StandardHeartsGamePlayer implements Player {
    private static final int CARDS_COUNT_AT_START = 13;

    private final String login;

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
    public String disconnect() {
        return null;
    }
}
