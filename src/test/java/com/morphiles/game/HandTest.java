package com.morphiles.game;

import junit.framework.TestCase;
import org.junit.Test;


public class HandTest extends TestCase {

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
    public void testHoleCards()
    {
        setHole("Ah", "Ah");
        assertEquals("PREFLOP: " , "Pair-AA", Hand.getPreflopHandType(hole));

        setHole("Ah", "Kc");
        assertEquals("PREFLOP: " , "AK", Hand.getPreflopHandType(hole));
        setHole("Kd", "Ac");
        assertEquals("PREFLOP: " , "AK", Hand.getPreflopHandType(hole));
        setHole("Ah", "Kh");
        assertEquals("PREFLOP: " , "AKs", Hand.getPreflopHandType(hole));
        setHole("Ac", "Kc");
        assertEquals("PREFLOP: " , "AKs", Hand.getPreflopHandType(hole));
        setHole("As", "Ks");
        assertEquals("PREFLOP: " , "AKs", Hand.getPreflopHandType(hole));
        setHole("Kd", "Ad");
        assertEquals("PREFLOP: " , "AKs", Hand.getPreflopHandType(hole));
        setHole("Qh", "As");
        assertEquals("PREFLOP: " , "AQ", Hand.getPreflopHandType(hole));
        setHole("Ah", "Qs");
        assertEquals("PREFLOP: " , "AQ", Hand.getPreflopHandType(hole));
        setHole("Qh", "Ah");
        assertEquals("PREFLOP: " , "AQs", Hand.getPreflopHandType(hole));
        setHole("Ad", "Qd");
        assertEquals("PREFLOP: " , "AQs", Hand.getPreflopHandType(hole));
        setHole("Qs", "As");
        assertEquals("PREFLOP: " , "AQs", Hand.getPreflopHandType(hole));
        setHole("Ac", "Qc");
        assertEquals("PREFLOP: " , "AQs", Hand.getPreflopHandType(hole));

        setHole("Qh", "Kh");
        assertEquals("PREFLOP: " , "KQs", Hand.getPreflopHandType(hole));
        setHole("Ks", "Qh");
        assertEquals("PREFLOP: " , "KQ", Hand.getPreflopHandType(hole));

        setHole("7h", "6h");
        assertEquals("PREFLOP: " , "SConns-1", Hand.getPreflopHandType(hole));
        setHole("2c", "4c");
        assertEquals("PREFLOP: " , "SConns-2", Hand.getPreflopHandType(hole));
        setHole("2d", "5d");
        assertEquals("PREFLOP: " , "SConns-3", Hand.getPreflopHandType(hole));
        setHole("2d", "5d");
        assertEquals("PREFLOP: " , "SConns-3", Hand.getPreflopHandType(hole));
        setHole("Ad", "5d");
        assertEquals("PREFLOP: " , "AXs", Hand.getPreflopHandType(hole));
        setHole("Ad", "5d");
        assertEquals("PREFLOP: " , "AXs", Hand.getPreflopHandType(hole));
        setHole("5c", "Ac");
        assertEquals("PREFLOP: " , "AXs", Hand.getPreflopHandType(hole));
        setHole("As", "2s");
        assertEquals("PREFLOP: " , "AXs", Hand.getPreflopHandType(hole));

        setHole("Kh", "Qh");
        assertEquals("PREFLOP: " , "KQs", Hand.getPreflopHandType(hole));

        setHole("Qd", "Kd");
        assertEquals("PREFLOP: " , "KQs", Hand.getPreflopHandType(hole));
        setHole("Js", "Ks");
        assertEquals("PREFLOP: " , "KJs", Hand.getPreflopHandType(hole));
        setHole("Jh", "Qh");
        assertEquals("PREFLOP: " , "SConns-1", Hand.getPreflopHandType(hole));

        setHole("Jh", "7h");
        assertEquals("PREFLOP: " , "Suited", Hand.getPreflopHandType(hole));

        setHole("Js", "7h");
        assertEquals("PREFLOP: " , "JUNK-J7", Hand.getPreflopHandType(hole));

        setHole("2c", "7d");
        assertEquals("PREFLOP: " , "JUNK-72", Hand.getPreflopHandType(hole));

        setHole("Qc", "Kd");
        assertEquals("PREFLOP: " , "KQ", Hand.getPreflopHandType(hole));
        setHole("Qc", "Ks");
        assertEquals("PREFLOP: " , "KQ", Hand.getPreflopHandType(hole));

        setHole("Jc", "Kd");
        assertEquals("PREFLOP: " , "KJ", Hand.getPreflopHandType(hole));
        setHole("Tc", "Kd");
        assertEquals("PREFLOP: " , "KX", Hand.getPreflopHandType(hole));
        setHole("Td", "Kd");
        assertEquals("PREFLOP: " , "KXs", Hand.getPreflopHandType(hole));

        setHole("Qc", "Jd");
        assertEquals("PREFLOP: " , "Conns-1", Hand.getPreflopHandType(hole));
        setHole("Tc", "Qd");
        assertEquals("PREFLOP: " , "Conns-2", Hand.getPreflopHandType(hole));
        setHole("9h", "Qd");
        assertEquals("PREFLOP: " , "Conns-3", Hand.getPreflopHandType(hole));
        setHole("8h", "Qd");
        assertEquals("PREFLOP: " , "JUNK-Q8", Hand.getPreflopHandType(hole));
        setHole("2h", "Qd");
        assertEquals("PREFLOP: " , "JUNK-Q2", Hand.getPreflopHandType(hole));
        setHole("2h", "3d");
        assertEquals("PREFLOP: " , "Conns-1", Hand.getPreflopHandType(hole));
        setHole("5h", "3d");
        assertEquals("PREFLOP: " , "Conns-2", Hand.getPreflopHandType(hole));

    }


    @Test
    public void testHighCard()
    {
        setHole("8c", "9s");

        setFlop("As", "4d", "5h");
        setTurn("2s", "4h", "7h", "Qh");
        setRiver("2s", "3c", "Kh", "Ah", "6h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP " + handFlop.getHand(), "HIGH CARD", handFlop.getHand());
        assertEquals("TURN " + handTurn.getHand(), "HIGH CARD", handTurn.getHand());
        assertEquals("RIVER " + handRiver.getHand(), "HIGH CARD", handRiver.getHand());
    }

    @Test
    public void testGutshot()
    {
        setHole("8c", "3s");

        setFlop("As", "4d", "5h");
        setTurn("2s", "4h", "6h", "Th");
        setRiver("Qs", "Ac", "5h", "Th", "9h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", handFlop.getHand(), "gutshot");
        assertEquals("TURN ", handTurn.getHand(), "gutshot");

        // On the river your hand cannot be a draw - since there are no more draws.
        assertEquals("RIVER ", "HIGH CARD", handRiver.getHand());
    }

    @Test
    public void testStraightDraw()
    {
        setHole("6c", "3s");

        setFlop("As", "4d", "5h");
        setTurn("5s", "4h", "8h", "Th");
        setRiver("Qs", "Kc", "Jh", "7h", "Th");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "straightDraw", handFlop.getHand());
        assertEquals("TURN " , "straightDraw", handTurn.getHand());
        assertEquals("RIVER ", "HIGH CARD", handRiver.getHand());
    }

    @Test
    public void testFlushDraw()
    {
        setHole("6c", "3s");

        setFlop("As", "4s", "Ks");
        setTurn("5h", "Qh", "8h", "Th");
        setRiver("Ac", "Kc", "9h", "7c", "2d");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "flushDraw", handFlop.getHand());
        assertEquals("TURN " , "flushDraw", handTurn.getHand());
        assertEquals("RIVER ", "HIGH CARD", handRiver.getHand());
    }

    @Test
    public void testFlushDrawAndGutshot()
    {
        setHole("Ac", "4s");

        setFlop("Jc", "3c", "5c");
        setTurn("5d", "6d", "8d", "9d");
        setRiver("Qc", "Kc", "Jh", "7c", "2d");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "flushDraw +gutshot", handFlop.getHand());
        assertEquals("TURN " , "flushDraw +gutshot", handTurn.getHand());
        assertEquals("RIVER ", "HIGH CARD", handRiver.getHand());
    }

    @Test
    public void testFlushDrawAndStraightDraw()
    {
        setHole("Ac", "4s");

        setFlop("6c", "3c", "5c");
        setTurn("9d", "6d", "8d", "7d");
        setRiver("Qc", "Tc", "Jh", "9c", "2d");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "flushDraw +straightDraw", handFlop.getHand());
        assertEquals("TURN " , "flushDraw +straightDraw", handTurn.getHand());
        assertEquals("RIVER ", "HIGH CARD", handRiver.getHand());
    }

    @Test
    public void testPairAndGutshot()
    {
        setHole("Ac", "2s");

        setFlop("As", "4d", "5h");
        setTurn("2s", "Kh", "Qh", "Th");
        setRiver("5s", "3c", "6h", "Ah", "Jh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "PAIR 1 +gutshot", handFlop.getHand());
        assertEquals("TURN ", "PAIR 4 +gutshot", handTurn.getHand());
        assertEquals("RIVER ", "PAIR 1", handRiver.getHand());
    }

    @Test
    public void testPairAndStraightDraw()
    {
        setHole("6c", "3s");

        setFlop("6s", "4d", "5h");
        setTurn("5s", "4h", "3h", "Ah");
        setRiver("5s", "Ac", "3h", "4h", "Th");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "PAIR 1 +straightDraw", handFlop.getHand());
        assertEquals("TURN " , "PAIR 4 +straightDraw", handTurn.getHand());
        assertEquals("RIVER ", "PAIR 5", handRiver.getHand());
    }

    @Test
    public void testPairAndFlushDraw()
    {
        setHole("4c", "3s");

        setFlop("As", "4s", "Ks");
        setTurn("6h", "Qh", "Ah", "4h");
        setRiver("Ac", "Kc", "3h", "Qc", "9d");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "PAIR 3 +flushDraw", handFlop.getHand());
        assertEquals("TURN " , "PAIR 4 +flushDraw", handTurn.getHand());
        assertEquals("RIVER ", "PAIR 5", handRiver.getHand());
    }

    @Test
    public void testPairAndFlushDrawAndGutshot()
    {
        setHole("Ac", "4s");

        setFlop("4c", "3c", "5c");
        setTurn("5d", "6d", "4d", "2d");
        setRiver("Qc", "Kc", "Jh", "7c", "Ad");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "PAIR 2 +flushDraw +gutshot", handFlop.getHand());
        assertEquals("TURN " , "PAIR 3 +flushDraw +gutshot", handTurn.getHand());
        assertEquals("RIVER ", "PAIR 1", handRiver.getHand());
    }

    @Test
    public void testPairAndFlushDrawAndStraightDraw()
    {
        setHole("6c", "4s");

        setFlop("4c", "3c", "5c");
        setTurn("5d", "7d", "4d", "2d");
        setRiver("Qc", "Kc", "Jh", "4c", "Td");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP ", "PAIR 2 +flushDraw +straightDraw", handFlop.getHand());
        assertEquals("TURN " , "PAIR 3 +flushDraw +straightDraw", handTurn.getHand());
        assertEquals("RIVER ", "PAIR 5", handRiver.getHand());
    }

    @Test
    public void test2Pairs()
    {
        setHole("6h", "3s");

        setFlop("2s", "3d", "6h");
        setTurn("2s", "2d", "7d", "3h");
        setRiver("3s", "5d", "8h", "Ah", "Ad");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP " + handFlop.getHand(), "TOP 2PAIR", handFlop.getHand());
        assertEquals("TURN " + handTurn.getHand(), "2PAIR - (2, 3)", handTurn.getHand());
        assertEquals("RIVER " + handRiver.getHand(), "2PAIR - (1, 4)", handRiver.getHand());

    }
    @Test
    public void testSet()
    {
        setHole("6h", "6s");

        setFlop("2s", "3d", "6d");
        setTurn("2s", "3d", "7d", "6c");
        setRiver("Js", "8d", "9h", "Ah", "6d");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP " + handFlop.getHand(), "TOP SET (concealed)", handFlop.getHand());
        assertEquals("TURN " + handTurn.getHand(), "SET 2 (concealed)", handTurn.getHand());
        assertEquals("RIVER " + handRiver.getHand(), "SET 5 (concealed)", handRiver.getHand());
    }

    @Test
    public void testSetAndGutshot()
    {
        setHole("Ac", "As");

        setTurn("2s", "4h", "5h", "Ah");
        setRiver("5s", "3c", "6h", "Ah", "2h");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TURN ", "TOP SET (concealed) +gutshot", handTurn.getHand());
        assertEquals("RIVER ", "TOP SET (concealed)", handRiver.getHand());
    }

    @Test
    public void testSetAndStraightDraw()
    {
        setHole("3c", "3s");

        setTurn("5s", "4h", "3h", "2h");
        setRiver("5s", "6c", "3h", "4h", "Th");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TURN " , "SET 3 (concealed) +straightDraw", handTurn.getHand());
        assertEquals("RIVER ", "SET 5 (concealed)", handRiver.getHand());
    }

    @Test
    public void testSetAndFlushDraw()
    {
        setHole("9c", "9s");

        setTurn("6h", "Qh", "Ah", "9h");
        setRiver("Ac", "Kc", "3h", "Qc", "9d");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TURN " , "SET 3 (concealed) +flushDraw", handTurn.getHand());
        assertEquals("RIVER ", "SET 4 (concealed)", handRiver.getHand());
    }

    @Test
    public void testSetAndFlushDrawAndGutshot()
    {
        setHole("Kc", "Ks");

        setTurn("Jd", "Td", "9d", "Kd");
        setRiver("Qc", "Kc", "Jh", "7c", "Ad");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TURN " , "TOP SET (concealed) +flushDraw +gutshot", handTurn.getHand());
        assertEquals("RIVER ", "SET 2 (concealed)", handRiver.getHand());
    }

    @Test
    public void testSetAndFlushDrawAndStraightDraw()
    {
        setHole("6c", "6s");

        setTurn("5d", "7d", "4d", "6d");
        setRiver("Qc", "Kc", "Jh", "6c", "Td");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TURN " , "SET 2 (concealed) +flushDraw +straightDraw", handTurn.getHand());
        assertEquals("RIVER ", "SET 5 (concealed)", handRiver.getHand());
    }

    @Test
    public void testStraight()
    {
        setHole("3h", "2s");

        setFlop("As", "4d", "5h");
        setTurn("4s", "5d", "9d", "6h");
        setRiver("As", "5d", "4h", "7h", "5d");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("STRAIGHT Failure FLOP: " + handFlop.getHand(), "STRAIGHT", handFlop.getHand());
        assertEquals("STRAIGHT Failure TURN: " + handTurn.getHand(), "STRAIGHT", handTurn.getHand());
        assertEquals("STRAIGHT Failure RIVER: " + handRiver.getHand(), "STRAIGHT", handRiver.getHand());
    }

    @Test
    public void testStraightAndFlushDraw()
    {
        setHole("3s", "2S");

        setFlop("As", "4d", "5s");
        setTurn("4s", "5d", "9d", "6s");
        setRiver("Kc", "5c", "4c", "7h", "Ac");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP: ", "STRAIGHT +flushDraw", handFlop.getHand());
        assertEquals("TURN: " , "STRAIGHT +flushDraw", handTurn.getHand());
        assertEquals("RIVER: ", "STRAIGHT", handRiver.getHand());
    }

    @Test
    public void testStraightAndNoGutshot()
    {
        setHole("3s", "2h");

        setTurn("4s", "5d", "8d", "6s");
        setRiver("Kc", "5c", "4h", "7h", "Ac");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TURN: " , "STRAIGHT", handTurn.getHand());
        assertEquals("RIVER: ", "STRAIGHT", handRiver.getHand());
    }

    @Test
    public void testFlush()
    {
        setHole("Ah", "8h");

        setFlop("2h", "3h", "6h");
        setTurn("2h", "3h", "7d", "6h");
        setRiver("3s", "5h", "4h", "Ad", "6h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLUSH Failure FLOP: " + handFlop.getHand(), "FLUSH", handFlop.getHand());
        assertEquals("FLUSH Failure TURN: " + handTurn.getHand(), "FLUSH", handTurn.getHand());
        assertEquals("FLUSH Failure RIVER: " + handRiver.getHand(), "FLUSH", handRiver.getHand());
    }

    @Test
    public void testFullHouse()
    {
        setHole("Ah", "8h");

        setFlop("As", "8s", "8c");
        setTurn("8c","Ac", "7d", "8h");
        setRiver("3s", "5c", "8h", "Ad", "Ah");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FULL House Failure FLOP: " + handFlop.getHand(), "UNDER FULL", handFlop.getHand());
        assertEquals("FULL House Failure TURN: " + handTurn.getHand(), "UNDER FULL", handTurn.getHand());
        assertEquals("FULL House Failure RIVER: " + handRiver.getHand(), "BIG FULL", handRiver.getHand());
    }

    @Test
    public void testFullHouseAndNoFlushDraw()
    {
        setHole("Ah", "8h");

        setRiver("3h", "5h", "8c", "Ad", "As");

        Hand handRiver = new Hand(hole, river);

        assertEquals("RIVER: ", "BIG FULL", handRiver.getHand());
    }

    @Test
    public void testFullHouseAndNoStraightDraw()
    {
        setHole("Qh", "Kh");

        setRiver("Jh", "Th", "Qc", "Kd", "Ks");

        Hand handRiver = new Hand(hole, river);

        assertEquals("RIVER: ", "BIG FULL", handRiver.getHand());
    }

    @Test
    public void testFullHouseAndNoGutshot()
    {
        setHole("Qh", "Qh");

        setRiver("Jh", "9h", "Qc", "Kd", "Ks");

        Hand handRiver = new Hand(hole, river);

        assertEquals("RIVER: ", "UNDER FULL (concealed)", handRiver.getHand());
    }

    @Test
    public void testQuads()
    {
        setHole("Ah", "8h");

        setFlop("8d", "8s", "8c");
        setTurn("2c", "Ac", "Ad", "Ad");
        setRiver("8s", "Ac", "8d", "Ad", "8s");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP: ", "QUADS", handFlop.getHand());
        assertEquals("TURN: ", "QUADS", handTurn.getHand());
        assertEquals("RIVER: ", "QUADS", handRiver.getHand());
    }
    @Test
    public void testStraightFlush()
    {
        setHole("4h", "5h");

        setFlop("2H", "3H", "AH");
        setTurn("6H", "7H", "8C", "3H");
        setRiver("Th", "9h", "6h", "7h", "8h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP: ", "STRAIGHT FLUSH", handFlop.getHand());
        assertEquals("TURN: ", "STRAIGHT FLUSH", handTurn.getHand());
        assertEquals("RIVER: " , "STRAIGHT FLUSH", handRiver.getHand());
    }
    @Test
    public void testRoyalFlush()
    {
        setHole("Ah", "Qh");

        setFlop("TH", "JH", "KH");
        setTurn("5h", "Th", "Kh", "Jh");
        setRiver("Th", "9s", "Jh", "7h", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("FLOP: " , "ROYAL FLUSH", handFlop.getHand());
        assertEquals("TURN: ", "ROYAL FLUSH", handTurn.getHand());
        assertEquals("RIVER: ", "ROYAL FLUSH", handRiver.getHand());
    }

    @Test
    public void test1stPairType()
    {
        setHole("Ah", "Ks");

        setFlop("As", "Qd", "9h");
        setTurn("6s", "Td", "5d", "Ah");
        setRiver("2s", "7d", "8s", "Jh", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("PAIR Failure FLOP: " + handFlop.getHand(), "PAIR 1", handFlop.getHand());
        assertEquals("PAIR Failure TURN: " + handTurn.getHand(), "PAIR 1", handTurn.getHand());
        assertEquals("PAIR Failure RIVER: " + handRiver.getHand(), "PAIR 1", handRiver.getHand());
    }

    @Test
    public void test2ndPairType()
    {
        setHole("Ah", "Js");

        setFlop("Ks", "Jd", "5h");
        setTurn("6s", "7d", "Jd", "Kh");
        setRiver("2s", "7d", "Js", "3h", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("PAIR Failure FLOP: " + handFlop.getHand(), "PAIR 2", handFlop.getHand());
        assertEquals("PAIR Failure TURN: " + handTurn.getHand(), "PAIR 2", handTurn.getHand());
        assertEquals("PAIR Failure RIVER: " + handRiver.getHand(), "PAIR 2", handRiver.getHand());
    }

    @Test
    public void test3rdPairType()
    {
        setHole("Ah", "5s");

        setFlop("Ks", "Qd", "5h");
        setTurn("2s", "Td", "5d", "Kh");
        setRiver("Qs", "3d", "4s", "5h", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("PAIR Failure FLOP: " + handFlop.getHand(), "PAIR 3", handFlop.getHand());
        assertEquals("PAIR Failure TURN: " + handTurn.getHand(), "PAIR 3", handTurn.getHand());
        assertEquals("PAIR Failure RIVER: " + handRiver.getHand(), "PAIR 3", handRiver.getHand());
    }

    @Test
    public void test4thPairType()
    {
        setHole("2h", "5s");

        setTurn("2s", "Td", "6d", "Kh");
        setRiver("Qs", "3d", "9s", "5h", "Kh");

        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("PAIR Failure TURN: " + handTurn.getHand(), "PAIR 4", handTurn.getHand());
        assertEquals("PAIR Failure RIVER: " + handRiver.getHand(), "PAIR 4", handRiver.getHand());
    }

    @Test
    public void test5thPairType()
    {
        setHole("2h", "5s");

        setRiver("Qs", "3d", "2s", "7h", "Kh");

        Hand handRiver = new Hand(hole, river);

        assertEquals("PAIR Failure RIVER: " + handRiver.getHand(), "PAIR 5", handRiver.getHand());
    }

    @Test
    public void testTop2Pair()
    {
        setHole("Ah", "Ks");
        setFlop("Ks", "3d", "As");
        setTurn("4s", "Ad", "6s", "Kh");
        setRiver("As", "3d", "2s", "2h", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TOP TWO PAIR Failure FLOP: " + handFlop.getHand(), "TOP 2PAIR", handFlop.getHand());
        assertEquals("TOP TWO PAIR Failure TURN: " + handTurn.getHand(), "TOP 2PAIR", handTurn.getHand());
        assertEquals("TOP TWO PAIR Failure RIVER: " + handRiver.getHand(), "TOP 2PAIR", handRiver.getHand());
    }

    @Test
    public void testTop2PairWithConcealedOverpair()
    {
        setHole("Ah", "As");
        setFlop("Ks", "3d", "Ks");
        setTurn("Ks", "Kd", "5s", "2h");
        setRiver("8s", "3d", "2s", "Qh", "Qh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TOP TWO PAIR w/Overpair Failure FLOP: " + handFlop.getHand(), "TOP 2PAIR W/Overpair", handFlop.getHand());
        assertEquals("TOP TWO PAIR w/Overpair Failure TURN: " + handTurn.getHand(), "TOP 2PAIR W/Overpair", handTurn.getHand());
        assertEquals("TOP TWO PAIR w/Overpair Failure RIVER: " + handRiver.getHand(), "TOP 2PAIR W/Overpair", handRiver.getHand());
    }

    @Test
    public void test2PairWithConcealedOverpair()
    {
        setHole("Jh", "Js");
        setFlop("3s", "3d", "7s");
        setTurn("Ts", "6d", "9s", "9h");
        setRiver("8s", "3d", "2s", "2h", "9h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TWO PAIR w/Overpair Failure FLOP: " + handFlop.getHand(), "2PAIR W/Overpair", handFlop.getHand());
        assertEquals("TWO PAIR w/Overpair Failure TURN: " + handTurn.getHand(), "2PAIR W/Overpair", handTurn.getHand());
        assertEquals("TWO PAIR w/Overpair Failure RIVER: " + handRiver.getHand(), "2PAIR W/Overpair", handRiver.getHand());
    }

    @Test
    public void testTop2PairWithConcealedUnderpair()
    {
        setHole("Jh", "Js");
        setFlop("7s", "Ad", "As");
        setTurn("Ts", "6d", "Qs", "Qh");
        setRiver("8s", "3d", "Ks", "2h", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("TOP 2PAIR W/Underpair Failure FLOP: " + handFlop.getHand(), "TOP 2PAIR W/Underpair", handFlop.getHand());
        assertEquals("TOP 2PAIR W/Underpair Failure TURN: " + handTurn.getHand(), "TOP 2PAIR W/Underpair", handTurn.getHand());
        assertEquals("TOP 2PAIR W/Underpair Failure RIVER: " + handRiver.getHand(), "TOP 2PAIR W/Underpair", handRiver.getHand());
    }

    @Test
    public void test2PairWithConcealedUnderpair()
    {
        setHole("8h", "8s");
        setFlop("7s", "Ad", "7s");
        setTurn("Ts", "6d", "Qs", "Qh");
        setRiver("7s", "3d", "3s", "2h", "Kh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("2PAIR W/Underpair Failure FLOP: " + handFlop.getHand(), "2PAIR W/Underpair - (2, 3)", handFlop.getHand());
        assertEquals("2PAIR W/Underpair Failure TURN: " + handTurn.getHand(), "2PAIR W/Underpair - (1, 3)", handTurn.getHand());
        assertEquals("2PAIR W/Underpair Failure RIVER: " + handRiver.getHand(), "2PAIR W/Underpair - (2, 4)", handRiver.getHand());
    }

    @Test
    public void testAny2Pair()
    {
        setHole("8h", "4s");
        setFlop("8s", "4d", "9s");
        setTurn("2s", "2d", "3s", "3h");
        setRiver("Qs", "Jd", "Qs", "2h", "Jh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("2PAIR Any Failure FLOP: " + handFlop.getHand(), "2PAIR - (2, 3)", handFlop.getHand());
        assertEquals("2PAIR Any Failure TURN: " + handTurn.getHand(), "TOP 2PAIR", handTurn.getHand());
        assertEquals("2PAIR Any Failure RIVER: " + handRiver.getHand(), "TOP 2PAIR", handRiver.getHand());
    }

    @Test
    public void testOpenSet()
    {
        setHole("8h", "4s");
        setFlop("8s", "3d", "8s");
        setTurn("4s", "4d", "9s", "Qh");
        setRiver("8s", "Jd", "8s", "Qh", "Ah");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("SET Failure FLOP: " + handFlop.getHand(), "TOP SET", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "SET 3", handTurn.getHand());
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "SET 4", handRiver.getHand());
    }

    @Test
    public void testConcealedSet()
    {
        setHole("8h", "8s");
        setFlop("7s", "3d", "8s");
        setTurn("4s", "8d", "5s", "Jh");
        setRiver("2s", "3d", "8s", "Qh", "Ah");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("SET Failure FLOP: " + handFlop.getHand(), "TOP SET (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "SET 2 (concealed)", handTurn.getHand());
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "SET 3 (concealed)", handRiver.getHand());
    }

    @Test
    public void testSetOnTable()
    {
        setHole("8h", "9s");
        setFlop("Js", "Jd", "Js");
        setTurn("4s", "4d", "5s", "4h");
        setRiver("Qs", "3d", "Qs", "Qh", "Ah");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("SET Failure FLOP: " + handFlop.getHand(), "TOP SET", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "SET 2", handTurn.getHand());
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "SET 2", handRiver.getHand());
    }

    @Test
    public void testBigFullHouse()
    {
        setHole("8h", "9s");
        setFlop("9s", "9d", "8s");
        setTurn("Js", "Jd", "9s", "Jh");
        setRiver("As", "8d", "As", "Ah", "7h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "BIG FULL", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "BIG FULL", handTurn.getHand());
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "BIG FULL", handRiver.getHand());
    }

    @Test
    public void testBigFullHouseConcealedOverpair()
    {
        setHole("Kh", "Ks");
        setFlop("9s", "9d", "9s");
        setTurn("Js", "Jd", "2s", "Jh");
        setRiver("Qs", "Jd", "Qs", "7h", "Qh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "BIG FULL w/Overpair (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "BIG FULL w/Overpair (concealed)", handTurn.getHand());
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "BIG FULL w/Overpair (concealed)", handRiver.getHand());
    }

    @Test
    public void testBigFullHouseConcealedUnderpair()
    {
        setHole("2h", "2s");
        setFlop("9s", "9d", "9s");
        setTurn("Js", "Jd", "2s", "Jh");
        setRiver("Qs", "Jd", "Qs", "7h", "Qh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "BIG FULL w/Underpair (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "BIG FULL", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "BIG FULL w/Underpair (concealed)", handRiver.getHand());
    }

    @Test
    public void testBigFullHouseConcealed()
    {
        setHole("Kh", "Ks");
        setFlop("3s", "3d", "Ks");
        setTurn("Js", "Jd", "2s", "Kh");
        setRiver("2s", "Jd", "Ks", "7h", "2h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "BIG FULL (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "BIG FULL (concealed)", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "BIG FULL (concealed)", handRiver.getHand());
    }

    @Test
    public void testBigFullHouseWithBothHoleCards()
    {
        setHole("Jh", "Ts");
        setFlop("Js", "Jd", "Ts");
        setTurn("Ts", "Jd", "Js", "Th");
        setRiver("2s", "Jd", "Ks", "Jh", "Th");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "BIG FULL", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "BIG FULL", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "BIG FULL", handRiver.getHand());
    }

    @Test
    public void testUnderFullHouseConcealed()
    {
        setHole("6h", "6s");
        setFlop("6s", "Td", "Ts");
        setTurn("Ts", "Jd", "Js", "6h");
        setRiver("2s", "Jd", "Ks", "Jh", "6h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "UNDER FULL (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "UNDER FULL (concealed)", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "UNDER FULL (concealed)", handRiver.getHand());
    }

    @Test
    public void testQuadsConcealed()
    {
        setHole("6h", "6s");
        setFlop("6s", "6d", "Js");
        setTurn("Js", "Kd", "6s", "6h");
        setRiver("2s", "Jd", "Ks", "6h", "6h");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "QUADS (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "QUADS (concealed)", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "QUADS (concealed)", handRiver.getHand());
    }

    @Test
    public void testQuadsConcealedWithAces()
    {
        setHole("Ah", "As");
        setFlop("6s", "Ad", "As");
        setTurn("As", "Kd", "6s", "Ah");
        setRiver("2s", "Jd", "Ks", "Ah", "Ah");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "QUADS (concealed)", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "QUADS (concealed)", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "QUADS (concealed)", handRiver.getHand());
    }

    @Test
    public void testQuadsOpen()
    {
        setHole("Ah", "5s");
        setFlop("5s", "5d", "5s");
        setTurn("As", "Ad", "6s", "Ah");
        setRiver("2s", "Qd", "Qs", "Qh", "Qh");

        Hand handFlop = new Hand(hole, flop);
        Hand handTurn = new Hand(hole, turn);
        Hand handRiver = new Hand(hole, river);

        assertEquals("BIG FULL HOUSE Failure FLOP: " + handFlop.getHand(), "QUADS", handFlop.getHand());
        assertEquals("SET Failure TURN: " + handTurn.getHand(), "QUADS", handTurn.getHand()); // underpair doesn't count here since any 2 makes the full.
        assertEquals("SET Failure RIVER: " + handRiver.getHand(), "QUADS", handRiver.getHand());
    }
}
