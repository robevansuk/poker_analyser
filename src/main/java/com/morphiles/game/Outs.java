package com.morphiles.game;

import java.util.Hashtable;
import java.util.Map;

/**
 * The outs Class works by first
 * Taking a deck of cards,
 * remove any cards already shown,
 * then populating the "outs" Set with any relevant
 * ranks/suits left over in the deck.
 *
 * The outs class offers the option to add a
 * rank of cards from the deck (e.g. all remaining 4's)
 * or an entire suit (e.g. all hearts)
 *
 * Using a Set ensures no outs are counted twice.
 *
 * @author robevans
 */
public class Outs {

    Map<String, Card> deck;
    Map<String, Card> outs;

    public Outs(){
        deck = new Hashtable<>();
        outs = new Hashtable<>();

        for (Card card : Deck.getDeck()){
            deck.put(card.toString(), card);
        }
    }

    public Outs(Card[] hole, Card[] community){
        deck = new Hashtable<>();
        outs = new Hashtable<>();

        for (Card card : Deck.getDeck()){
            deck.put(card.toString().substring(0,1) + card.toString().substring(1,2).toLowerCase(), card);
        }

        remove(hole);
        remove(community);
    }

    // use this to remove a single card from the deck (for instance if the card has already been dealt)
    public void remove(Card card){
        deck.remove(card.toString());
    }

    public void remove(String card){
        if (deck.containsKey(card)) {
            deck.remove(card);
        }
    }

    // use this to remove more than a single card from the outs deck
    public void remove(Card[] cards){
        for (Card card : cards){
            deck.remove(card.toString());
        }
    }

    /**
     * shortcut to adding all of the remaining ranked cards
     * in the deck to the outs pile
     * @param rank
     */
    public void addRankAsOut(String rank){
        for(String card : deck.keySet()){
            if (card.toString().startsWith(rank)){
                outs.put(card.toString(), new Card(card.toString()));
            }
        }
    }

    /**
     * shortcut to adding all of the remaining ranked cards
     * in the deck to the outs pile
     * @param rank
     */
    public void addRankAsOut(Integer rank){
        for(String card : deck.keySet()){
            if (rank == 14) {
                if (card.toString().startsWith("A")){
                    outs.put(card.toString(), deck.get(card.toString()));
                }
            } else if (rank == 13){
                if (card.toString().startsWith("K")){
                    outs.put(card.toString(), deck.get(card.toString()));
                }
            }else if (rank == 12){
                if (card.toString().startsWith("Q")){
                    outs.put(card.toString(), deck.get(card.toString()));
                }
            } else if (rank == 11){
                if (card.toString().startsWith("J")){
                    outs.put(card.toString(), deck.get(card.toString()));
                }
            } else if (rank == 10) {
                if (card.toString().startsWith("T")) {
                    outs.put(card.toString(), deck.get(card.toString()));
                }
            } else if (card.toString().startsWith(rank+"")){
                outs.put(card.toString(), deck.get(card.toString()));
            }
        }
    }

    /**
     * shortcut to adding any of the remaining
     * suits to the outs pile
     * @param suit
     */
    public void addSuitAsOut(String suit){
        for (String card : deck.keySet()){
            if(card.toUpperCase().endsWith(suit.toUpperCase())){
                // add the suited card as an out if left in the deck
                outs.put(card, deck.get(card));
            }
        }
    }

    /**
     * Use to check if a car exists in the deck
     * or if it has already been dealt
     * @param card
     * @return
     */
    public boolean contains(Card card){
        return deck.containsValue(card);
    }

    public int getDeckSize(){
        return deck.size();
    }

    public int getOutsSize(){
        return outs.size();
    }

    public Card getFromDeck(String card){
        String c = card.toUpperCase();
        return deck.get(c);
    }

    public int getPercentageOfImprovement(){
        if (getOutsSize() != 0){
            Double outCount = new Double(getOutsSize());
            Double deckCount = new Double(getDeckSize());
            Double result = (outCount * 100) / deckCount;
            Integer intResult;
            if (result%1 >=0.5){
                intResult = new Integer(result.intValue()+1);
            } else {
                intResult = new Integer(result.intValue());
            }

            //double result = (getOutsSize()*100/ getDeckSize());
            return intResult;
        } else {
            return 0;
        }
    }

    public int getPercentageAgainstImprovement(){
        if (getOutsSize() != 0){
            Double outCount = new Double(getOutsSize());
            Double deckCount = new Double(getDeckSize());
            Double result = (100 - ((outCount * 100) / deckCount));
            Integer intResult;
            if (result%1 >=0.5){
                intResult = new Integer(result.intValue()+1);
            } else {
                intResult = new Integer(result.intValue());
            }
            return intResult;
        } else {
            return 0;
        }
    }
}
