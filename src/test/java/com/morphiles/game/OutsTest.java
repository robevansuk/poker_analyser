package com.morphiles.game;

import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author robevans
 */
public class OutsTest extends TestCase {

    @Test
    public void testDeckInitialisesCorrectly(){
        Outs outs = new Outs();

        // assert that outs contains all 52 the cards from the deck.
        for (Card card : Deck.getDeck()){
            outs.contains(card);
        }

    }

    @Test
    public void testRemovingCardsFromDeck(){
        Outs outs = new Outs();
        Card[] cards = { new Card("Ah"),
                new Card("Jd") };
        outs.remove(cards);
        assertFalse(outs.contains(cards[0]));
        assertFalse(outs.contains(cards[1]));
    }

    @Test
    public void testPercentageOfImprovement(){
        Outs outs = new Outs();
        Card[] cards = { new Card("Ah"),
                new Card("Jd"),
                new Card("Qs"),
                new Card("5s"),
                new Card("6s")};
        outs.remove(cards);
        outs.addRankAsOut("A"); // 3 left in the deck.
        outs.addSuitAsOut("c"); // 13 left in the deck.

        // the expected result is 16 outs / (52-5) = 34

        assertEquals("Percentage for improvement based on outs does not work.", 31, outs.getPercentageOfImprovement());
    }

    @Test
    public void testPercentageAgainstImprovement(){
        Outs outs = new Outs();
        Card[] cards = { new Card("Ah"),
                new Card("Jd"),
                new Card("Qs"),
                new Card("5s"),
                new Card("6s")};
        outs.remove(cards);
        outs.addRankAsOut("A"); // 3 left in the deck.
        outs.addSuitAsOut("c"); // 13 left in the deck.

        // the expected result is 16 outs / (52-5) = 34

        assertEquals("Percentage against improvement based on outs does not work.", 69, outs.getPercentageAgainstImprovement());
    }

    @Test
    public void testRemovingACard(){
        Outs outs = new Outs();
        outs.remove(outs.getFromDeck("9C"));
        assertEquals("Outs not the correct size after removing a single card", 51, outs.getDeckSize());
    }


    Card[] hole = new Card[2];
    Card[] flop = new Card[3];
    Card[] turn = new Card[4];
    Card[] river = new Card[5];


    public void setHole(String card1, String card2)
    {
        hole[0] = new Card(card1);
        hole[1] = new Card(card2);
    }

    public void setFlop(String card1, String card2, String card3)
    {
        flop[0] = new Card(card1);
        flop[1] = new Card(card2);
        flop[2] = new Card(card3);
    }

    public void setTurn(String card1, String card2, String card3, String card4)
    {
        turn[0] = new Card(card1);
        turn[1] = new Card(card2);
        turn[2] = new Card(card3);
        turn[3] = new Card(card4);
    }

    public void setRiver(String card1, String card2, String card3, String card4, String card5)
    {
        river[0] = new Card(card1);
        river[1] = new Card(card2);
        river[2] = new Card(card3);
        river[3] = new Card(card4);
        river[4] = new Card(card5);
    }

    @Test
    public void testOutsForNoOvercardsHighCardedHandOnly()
    {
        setHole("2c", "7s");

        setFlop("Qs", "9d", "8h");
        setTurn("Ks", "9h", "8h", "3h");
        setRiver("Js", "Tc", "6h", "5h", "Qh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 0, flopHand.calculateOuts());
        assertEquals("TURN", 0, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForNoOvercardsGutshotOnly()
    {
        setHole("2c", "7s");

        setFlop("5s", "9d", "8h");
        setTurn("Ks", "6h", "4h", "3h");
        setRiver("Js", "Tc", "6h", "5h", "9h");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 4, flopHand.calculateOuts());
        assertEquals("TURN", 4, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForNoOvercardsGutshotAndFlushDraw()
    {
        setHole("2s", "7s");

        setFlop("5s", "9s", "8h");
        setTurn("Ks", "6h", "4h", "3s");
        setRiver("Js", "Tc", "6h", "5h", "9s");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 12, flopHand.calculateOuts());
        assertEquals("TURN", 12, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForNoOvercardsOpenEndedStraightDraw()
    {
        setHole("2s", "7c");

        setFlop("6s", "9s", "8h");
        setTurn("3s", "5h", "4h", "9s");
        setRiver("Js", "8c", "6h", "5h", "3s");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 8, flopHand.calculateOuts());
        assertEquals("TURN", 8, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForNoOvercardsOpenEndedStraightDrawAtUpperLimit()
    {
        setHole("Js", "Qc");

        setFlop("6s", "Ks", "Th");
        setTurn("Ts", "5h", "4h", "Ks");
        setRiver("Ks", "8c", "6h", "5h", "Ts");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 8, flopHand.calculateOuts());
        assertEquals("TURN", 8, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForNoOvercardsOpenEndedStraightDrawAndFlushDraw()
    {
        setHole("Jh", "Qh");

        setFlop("6h", "Ks", "Th");
        setTurn("Ts", "5h", "4h", "Ks");
        setRiver("Ks", "8c", "6h", "5h", "Ts");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 15, flopHand.calculateOuts());
        assertEquals("TURN", 15, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForNoOvercardsFlushDraw()
    {
        setHole("Jh", "7h");

        setFlop("6h", "Ks", "Ah");
        setTurn("Qs", "5h", "4h", "Ks");
        setRiver("Ks", "2c", "6h", "5h", "Ts");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 9, flopHand.calculateOuts());
        assertEquals("TURN", 9, turnHand.calculateOuts());
        assertEquals("RIVER", 0, riverHand.calculateOuts());
    }


    @Test
    public void testOutsForTwoOvercards()
    {
        setHole("Ac", "Ks");

        setFlop("Ts", "9d", "8h");
        setTurn("Qs", "9h", "8h", "7h");
        setRiver("Js", "7c", "6h", "5h", "2h");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 6, flopHand.calculateOuts());
        assertEquals("TURN", 6, turnHand.calculateOuts());
        assertEquals("RIVER", 6, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForOneOvercard() {
        setHole("2c", "As");

        setFlop("Ts", "9d", "8h");
        setTurn("Qs", "9h", "8h", "7h");
        setRiver("Js", "7c", "6h", "5h", "Kh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 3, flopHand.calculateOuts());
        assertEquals("TURN", 3, turnHand.calculateOuts());
        assertEquals("RIVER", 3, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForOneMoreOvercard() {
        setHole("Ac", "2s");

        setFlop("Ts", "9d", "8h");
        setTurn("Qs", "9h", "8h", "7h");
        setRiver("Js", "7c", "6h", "5h", "Kh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 3, flopHand.calculateOuts());
        assertEquals("TURN", 3, turnHand.calculateOuts());
        assertEquals("RIVER", 3, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForOneOverPair() {
        setHole("Ac", "As");

        setFlop("Ts", "9d", "8h");
        setTurn("Qs", "9h", "8h", "7h");
        setRiver("Js", "7c", "6h", "5h", "Kh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 2, flopHand.calculateOuts());
        assertEquals("TURN", 2, turnHand.calculateOuts());
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForOneUnderPair() {
        setHole("2c", "2s");

        setFlop("Ts", "9d", "8h");
        setTurn("Qs", "9h", "8h", "7h");
        setRiver("Js", "7c", "6h", "5h", "Kh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 2, flopHand.calculateOuts());
        assertEquals("TURN", 2, turnHand.calculateOuts());
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
     public void testOutsForTopPairAndAnUnpairedKicker() {
        setHole("Qc", "Kd");

        setFlop("Ks", "9d", "8h");
        setTurn("3s", "9h", "Kh", "7h");
        setRiver("Js", "7c", "6h", "5h", "Kh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 5, flopHand.calculateOuts());
        assertEquals("TURN", 5, turnHand.calculateOuts());
        assertEquals("RIVER", 5, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTopPairAndAnUnpairedOvercardKicker() {
        setHole("Qc", "Kd");

        setFlop("Qs", "9d", "8h");
        setTurn("3s", "9h", "Qh", "7h");
        setRiver("Js", "7c", "6h", "5h", "Qh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 5, flopHand.calculateOuts());
        assertEquals("TURN", 5, turnHand.calculateOuts());
        assertEquals("RIVER", 5, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForAnyPairAndAGutshotDraw() {
        setHole("Qc", "Kd");

        setFlop("Qs", "9d", "Th");
        setTurn("3s", "9h", "Qh", "Th");
        setRiver("Js", "Ac", "6h", "5h", "Qh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 9, flopHand.calculateOuts());
        assertEquals("TURN", 9, turnHand.calculateOuts());
        // cannot draw on the river so any draw outs aren't counted.
        assertEquals("RIVER", 5, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForAnyPairAndAGutshotDrawAtLimits() {
        setHole("Qc", "Ad");

        setFlop("Qs", "Kd", "Th");
        setTurn("3s", "2h", "Qh", "5h");
        setRiver("Js", "Ac", "6h", "Th", "Qh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 9, flopHand.calculateOuts());
        assertEquals("TURN", 9, turnHand.calculateOuts());
        // cannot draw on the river so any draw outs aren't counted.
        assertEquals("RIVER", 4, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForAnyPairAndAnOpenEndedStraightDraw() {
        setHole("Qc", "Td");

        setFlop("Js", "Kd", "Th");
        setTurn("3s", "Jh", "Th", "9h");
        setRiver("7s", "6c", "9h", "Th", "Jh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 13, flopHand.calculateOuts());
        assertEquals("TURN", 13, turnHand.calculateOuts());
        // cannot draw on the river so any draw outs aren't counted.
        assertEquals("RIVER", 5, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForAnyPairAndAnOpenEndedStraightDrawAtLimits() {
        setHole("Jc", "Kd");

        setFlop("Ts", "Jd", "Qh");
        setTurn("3s", "4h", "5h", "2h");
        setRiver("3s", "4c", "2h", "5h", "Jh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 13, flopHand.calculateOuts());
        // Note: this example hand is not strictly a pair - it is also four to an OESD.
        assertEquals("TURN", 14, turnHand.calculateOuts());
        // cannot draw on the river so any draw outs aren't counted.
        assertEquals("RIVER", 5, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForAnyPairAndAFlushDraw() {
        setHole("Jc", "Kc");

        setFlop("Tc", "2c", "7h");
        setTurn("3s", "4c", "7d", "Ac");
        setRiver("Ts", "4c", "2h", "5c", "Jh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 15, flopHand.calculateOuts());
        assertEquals("TURN", 9, turnHand.calculateOuts());
        // cannot draw on the river so any draw outs aren't counted.
        assertEquals("RIVER", 5, riverHand.calculateOuts());
    }


    @Test
    public void testOutsForTopTwoPairWithOverpairAndTablePair() {
        setHole("Kc", "Kd");

        setFlop("Qs", "Qd", "8h");
        setTurn("3s", "Ah", "Qh", "Ah");
        setRiver("Js", "7c", "6h", "5h", "Jh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 2, flopHand.calculateOuts());
        assertEquals("TURN", 2, turnHand.calculateOuts());
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithUnderpairAndTablePair() {
        setHole("5c", "5d");

        setFlop("8s", "Qd", "8h");
        setTurn("3s", "Ah", "Qh", "Ah");
        setRiver("Js", "7c", "6h", "2h", "Jh");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 2, flopHand.calculateOuts());
        assertEquals("TURN", 2, turnHand.calculateOuts());
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithAnyTwoPairOnTheTable() {
        setHole("Ac", "Kd");

        setFlop("As", "Kh", "8h");
        setTurn("3s", "Kc", "4h", "Ah");
        setRiver("As", "7c", "6h", "2h", "Ks");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 4, flopHand.calculateOuts());
        assertEquals("TURN", 4, turnHand.calculateOuts());
        assertEquals("RIVER", 4, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithOverPairAndAnyPairThatIsntTopPair() {
        setHole("Kc", "Kd");

        setFlop("8s", "Qd", "8h");
        setTurn("3s", "4c", "Qh", "4h");
        setRiver("Js", "7c", "6h", "2h", "6s");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 2, flopHand.calculateOuts());
        assertEquals("TURN", 2, turnHand.calculateOuts());
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithOverPairAndGutshotDraw() {
        setHole("Kc", "Kd");

        setTurn("9s", "9c", "Qh", "Jh");
        setRiver("As", "Jc", "Th", "6h", "Ts");

        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        turnHand.getHand();
        riverHand.getHand();

        assertEquals("TURN", 6, turnHand.calculateOuts());
        // no draw outs included on the river
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithOverPairAndGutshotAndFlushDraw() {
        setHole("Kc", "Kd");

        setTurn("9s", "9c", "Qc", "Jc");
        setRiver("Ad", "Jd", "Th", "6h", "Td");

        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        turnHand.getHand();
        riverHand.getHand();

        assertEquals("TURN", 14, turnHand.calculateOuts());
        // no draw outs included on the river
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithOverPairAndOpenEndedStraightDraw() {
        setHole("Kc", "Kd");

        setTurn("Ts", "Tc", "Qd", "Jc");
        setRiver("Qc", "Jd", "Th", "6h", "Td");

        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        turnHand.getHand();
        riverHand.getHand();

        assertEquals("TURN", 10, turnHand.calculateOuts());
        // no draw outs included on the river
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }
    @Test
    public void testOutsForTwoPairWithOverPairAndOpenEndedStraightDrawAndFlushDraw() {
        setHole("Kc", "Kd");

        setTurn("Ts", "Tc", "Qc", "Jc");
        setRiver("Qc", "Jd", "Th", "6h", "Td");

        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        turnHand.getHand();
        riverHand.getHand();

        assertEquals("TURN", 17, turnHand.calculateOuts());
        // no draw outs included on the river
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTwoPairWithOverPairAndFlushDraw() {
        setHole("Kc", "Kd");

        setTurn("6s", "6c", "Qc", "Jc");
        setRiver("Qc", "Jd", "6s", "6d", "Td");

        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        turnHand.getHand();
        riverHand.getHand();

        assertEquals("TURN", 11, turnHand.calculateOuts());
        // no draw outs included on the river
        assertEquals("RIVER", 2, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTopSetConcealed() {
        setHole("Kc", "Kd");

        setFlop("Ks", "5d", "4c");
        setTurn("6s", "Kh", "Qc", "Jc");
        setRiver("Qc", "Jd", "Ks", "6d", "8d");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 7, flopHand.calculateOuts());
        assertEquals("TURN", 10, turnHand.calculateOuts());
        // the three outs on the river don't really count for anything but might as well test it.
        assertEquals("RIVER", 13, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForTopSetUnConcealed() {
        setHole("Kc", "Jd");

        setFlop("Ks", "Kd", "4c");
        setTurn("6s", "Kh", "Qc", "Ks");
        setRiver("Qc", "Kd", "Ks", "6d", "8d");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 4, flopHand.calculateOuts());
        assertEquals("TURN", 4, turnHand.calculateOuts());
        // the three outs on the river don't really count for anything but might as well test it.
        assertEquals("RIVER", 4, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForStraight() {
        setHole("Kc", "Jd");

        setFlop("As", "Qd", "Tc");
        setTurn("Ts", "9h", "2c", "Qs");
        setRiver("Qc", "Kd", "Ts", "6d", "9d");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 0, flopHand.calculateOuts());
        assertEquals("TURN", 0, turnHand.calculateOuts());
        // the three outs on the river don't really count for anything but might as well test it.
        assertEquals("RIVER",0, riverHand.calculateOuts());
    }

    @Test
    public void testOutsForFlush() {
        setHole("Kc", "Jc");

        setFlop("2c", "Qc", "Tc");
        setTurn("Ts", "9c", "2c", "Qc");
        setRiver("Qc", "Kd", "Tc", "6c", "9c");

        Hand flopHand = new Hand(hole, flop);
        Hand turnHand = new Hand(hole, turn);
        Hand riverHand = new Hand(hole, river);

        flopHand.getHand();
        turnHand.getHand();
        riverHand.getHand();

        assertEquals("FLOP", 0, flopHand.calculateOuts());
        assertEquals("TURN", 0, turnHand.calculateOuts());
        // the three outs on the river don't really count for anything but might as well test it.
        assertEquals("RIVER",0, riverHand.calculateOuts());
    }
}
