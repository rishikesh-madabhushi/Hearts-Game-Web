package com.llwantedll.webhearts.models.gameapi.cards;

import com.llwantedll.webhearts.models.gameapi.cards.exceptions.NoCardsInDeckException;

import java.util.Set;

public interface CardDeck<CardType> {
    int getMaxSize();

    int getRemainCardsSize();

    CardType pullRandomCard() throws NoCardsInDeckException;

    CardType pullNextCard() throws NoCardsInDeckException;

    Set<CardType> getAll();

    void pushCard(CardType card);

    void mix();

    void refresh();
}
