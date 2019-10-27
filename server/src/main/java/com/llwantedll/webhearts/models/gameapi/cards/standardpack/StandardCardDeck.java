package com.llwantedll.webhearts.models.gameapi.cards.standardpack;

import com.llwantedll.webhearts.models.gameapi.cards.CardDeck;
import com.llwantedll.webhearts.models.gameapi.cards.exceptions.NoCardsInDeckException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StandardCardDeck implements CardDeck<StandardCard> {

    private static final int MAX_CARDS = 52;

    private Set<StandardCard> cards;

    private StandardCardDeck() {
        refresh();
    }

    @Override
    public int getMaxSize() {
        return MAX_CARDS;
    }

    @Override
    public int getRemainCardsSize() {
        return cards.size();
    }

    @Override
    public StandardCard pullRandomCard() throws NoCardsInDeckException {
        //DECK IMPLEMENTED BY COLLECTION WITH NO ORDER. NEXT CARD IN COLLECTION IS RANDOM.
        return pullNextCard();
    }

    @Override
    public StandardCard pullNextCard() throws NoCardsInDeckException {
        Iterator<StandardCard> iterator = cards.iterator();

        if(iterator.hasNext()){

            StandardCard card = iterator.next();
            cards.remove(card);
            return card;
        }

        throw new NoCardsInDeckException();
    }

    @Override
    public Set<StandardCard> getAll() {
        return cards;
    }

    @Override
    public void pushCard(StandardCard standardCard) {
        cards.add(standardCard);
    }

    @Override
    public void mix() {
        //NO NEED FOR MIXING DECK IMPLEMENTED BY COLLECTION WITH NO ORDER
    }

    @Override
    public void refresh() {
        cards = new HashSet<>();

        for (StandardCardSuit standardCardSuit : StandardCardSuit.values()) {
            for (StandardCardRank standardCardRank : StandardCardRank.values()) {
                cards.add(new StandardCard(standardCardSuit, standardCardRank));
            }
        }
    }

    public static StandardCardDeck getInstance(){
        return new StandardCardDeck();
    }
}
