package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack;

import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.FullRoomException;
import com.llwantedll.webhearts.models.gameapi.cards.CardDeck;
import com.llwantedll.webhearts.models.gameapi.cards.standardpack.StandardCardDeck;
import com.llwantedll.webhearts.models.gameapi.entities.GameDetails;
import com.llwantedll.webhearts.models.gameapi.entities.Player;
import com.llwantedll.webhearts.models.gameapi.games.hearts.HeartsGame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StandardHeartsGame implements HeartsGame<StandardHeartsGamePlayer> {

    private GameDetails gameDetails;
    private List<StandardHeartsGamePlayer> players = new ArrayList<>();
    private CardDeck cardDeck = StandardCardDeck.getInstance();

    public StandardHeartsGame() {
        gameDetails = StandardHeartsGameDetails.getInstance();
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
            if(player.getUsername().equals(user.getLogin())){
                players.remove(player);
                return;
            }
        }
    }

    @Override
    public CardDeck getCardDeck() {
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
            if(this.players.get(i).getUsername().equals(player.getUsername())){
                StandardHeartsGamePlayer readyPlayer = this.players.get(i);
                readyPlayer.setReady(ready);
                this.players.set(i, readyPlayer);
            }
        }
    }
}
