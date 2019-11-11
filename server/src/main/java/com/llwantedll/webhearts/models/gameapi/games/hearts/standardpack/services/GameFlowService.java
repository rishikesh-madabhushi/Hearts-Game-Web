package com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.services;

import com.llwantedll.webhearts.models.entities.GameRoom;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.gameapi.exceptions.NoPlayerFoundException;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGame;
import com.llwantedll.webhearts.models.gameapi.games.hearts.standardpack.StandardHeartsGamePlayer;

public interface GameFlowService {
    void hideOtherUserCards(StandardHeartsGame currentGame, StandardHeartsGamePlayer currentPlayer);
    StandardHeartsGamePlayer getPlayerByUser(StandardHeartsGame currentGame, User user) throws NoPlayerFoundException;
    GameRoom readyPlayer(GameRoom gameRoom, StandardHeartsGame standardHeartsGame, StandardHeartsGamePlayer player, boolean ready);
}
