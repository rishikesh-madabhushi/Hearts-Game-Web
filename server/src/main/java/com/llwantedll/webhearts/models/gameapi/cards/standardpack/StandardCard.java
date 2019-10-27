package com.llwantedll.webhearts.models.gameapi.cards.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.Card;

public class StandardCard implements Card {
    private StandardCardSuit standardCardSuit;
    private StandardCardRank standardCardRank;

    public StandardCard(StandardCardSuit standardCardSuit,
                        StandardCardRank standardCardRank) {
        this.standardCardSuit = standardCardSuit;
        this.standardCardRank = standardCardRank;
    }

    public StandardCardSuit getCardSuit() {
        return standardCardSuit;
    }

    public StandardCardRank getCardRank() {
        return standardCardRank;
    }
}
