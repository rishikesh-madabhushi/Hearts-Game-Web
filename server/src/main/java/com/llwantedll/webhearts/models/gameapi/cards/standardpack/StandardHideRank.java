package com.llwantedll.webhearts.models.gameapi.cards.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.CardRank;

public enum StandardHideRank implements CardRank {
    HIDE_RANK(0);

    private int num;

    StandardHideRank(int num) {
        this.num = num;
    }

    @Override
    public int getNum() {
        return num;
    }
}
