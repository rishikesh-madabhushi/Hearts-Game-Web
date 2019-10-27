package com.llwantedll.webhearts.models.gameapi.cards.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.CardRank;

public enum StandardCardRank implements CardRank {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private int num;

    StandardCardRank(int num) {
        this.num = num;
    }

    @Override
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
