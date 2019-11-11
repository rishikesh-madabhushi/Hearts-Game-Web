package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack;

import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.exceptions.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoCardsInDeckException;
import com.llwantedll.webhearts.models.gameapi.cards.standardpack.StandardCard;
import com.llwantedll.webhearts.models.gameapi.cards.standardpack.StandardCardDeck;
import com.llwantedll.webhearts.models.gameapi.entities.GameDetails;
import com.llwantedll.webhearts.models.gameapi.entities.Player;
import com.llwantedll.webhearts.models.gameapi.games.hearts.HeartsGame;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services.GameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StandardHeartsGame implements HeartsGame<StandardHeartsGamePlayer> {

    private GameDetails gameDetails;
    private List<StandardHeartsGamePlayer> players = new ArrayList<>();
    private StandardCardDeck cardDeck = StandardCardDeck.getInstance();
    private Map<StandardHeartsGamePlayer, StandardCard> deskHold = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardHeartsGame.class);

    public StandardHeartsGame() {
        gameDetails = StandardHeartsGameDetails.getInstance();
    }

    @Override
    public void start() {
        try {
            GameHelper.giveAllPlayersStartCards(this);
        } catch (NoCardsInDeckException e) {
            LOGGER.warn("No card in deck exception");
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void end() {

    }

    @Override
    public GameDetails getGameDetails() {
        return gameDetails;
    }

    @Override
    public List<Player> getPlayers() {
        return players
                .stream()
                .map(e -> (Player) e)
                .collect(Collectors.toList());
    }

    @Override
    public Player addPlayer(User user) throws FullRoomException {
        if (players.size() >= gameDetails.getMaxPlayers()) {
            throw new FullRoomException();
        }

        StandardHeartsGamePlayer player = new StandardHeartsGamePlayer(user.getLogin());

        players.add(player);

        return player;
    }

    @Override
    public void removePlayer(User user) {
        for (StandardHeartsGamePlayer player : players) {
            if (player.getUsername().equals(user.getLogin())) {
                players.remove(player);
                return;
            }
        }
    }

    @Override
    public StandardCardDeck getCardDeck() {
        return cardDeck;
    }

    @Override
    public List<StandardHeartsGamePlayer> getHeartsPlayers() {
        return players;
    }

    @Override
    public void setHeartPlayers(List<StandardHeartsGamePlayer> players) {
        this.players = players;
    }

    @Override
    public void setPlayerReady(StandardHeartsGamePlayer player, boolean ready) {
        for (int i = 0; i < this.players.size(); i++) {
            StandardHeartsGamePlayer checkPlayer = this.players.get(i);
            if (checkPlayer.getUsername().equals(player.getUsername())) {
                checkPlayer.setReady(ready);
                this.players.set(i, checkPlayer);
            }
        }
    }

    public Map<StandardHeartsGamePlayer, StandardCard> getDeskHold() {
        return deskHold;
    }

    public void setDeskHold(Map<StandardHeartsGamePlayer, StandardCard> deskHold) {
        this.deskHold = deskHold;
    }
}
