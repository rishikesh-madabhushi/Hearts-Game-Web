package com.llwantedll.webhearts.models.gameapi.cards.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.Card;

public class StandardHideCard implements Card {
    private StandardHideSuit standardCardSuit;
    private StandardHideRank standardCardRank;

    public StandardHideCard() {
        this.standardCardSuit = StandardHideSuit.HIDE_SUIT;
        this.standardCardRank = StandardHideRank.HIDE_RANK;
    }

    @Override
    public StandardHideRank getCardRank() {
        return standardCardRank;
    }

    @Override
    public StandardHideSuit getCardSuit() {
        return standardCardSuit;
    }
}
