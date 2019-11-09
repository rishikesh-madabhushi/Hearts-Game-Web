package com.llwantedll.webhearts.models.gameapi.cards.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.Card;

public class StandardHideCard implements Card {
    private StandardHideSuit standardHideSuit;
    private StandardHideRank standardHideRank;

    public StandardHideCard() {
        this.standardHideSuit = StandardHideSuit.HIDE_SUIT;
        this.standardHideRank = new StandardHideRank();
    }

    @Override
    public StandardHideRank getCardRank() {
        return standardHideRank;
    }

    @Override
    public StandardHideSuit getCardSuit() {
        return standardHideSuit;
    }
}
