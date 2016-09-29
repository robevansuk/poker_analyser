package com.morphiles.game;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    // store hole+community cards - needed to refer back to later
    // when counting outs. eg for twopair
    private Card[] hole;
    private Card[] community;

    private List<Integer> tableRanks;
    private List<String> tableSuits;

    private List<Integer> holeRanks;
    private List<String> holeSuits;

    private List<Integer> holeMatches;
    private List<Integer> matches;

    private List<List<Integer>> suited;

    private List<Integer> hearts;
    private List<Integer> diamonds;
    private List<Integer> clubs;
    private List<Integer> spades;


    private boolean hasPair;
    private boolean hasTwoPair;
    private boolean hasSet;
    private boolean hasFullHouse;
    private boolean hasQuads;
    private boolean hasGutshotDraw;
    private boolean hasOpenEndStraightDraw;
    private boolean hasStraight;
    private boolean hasFlushDraw;
    private boolean hasFlush;
    private boolean hasStraightFlush;
	private boolean hasRoyalFlush;

	private String flushSuit;

    private int highestSuitedRank;
    private int highestHoleSuitedRank; // represents highest card not on table for a given suit (the nut flush)
    private int communityCardsSize;

    // Table Card Analysis
    private int tablePairCount;
    private int tableSetCount;
    private boolean table;
    private boolean tableFullHousePresent;

    private int tableQuadsCount;
    private int tableStraightCount;
    private int tableFlushCount;

    // Pair Analysis
    private int firstPairRanking; // 1st, 2nd, 3rd, ..., bottom pair
    private boolean isConcealedOverpair;
    private boolean isConcealedUnderpair;
    private String firstPairType;

    // TWO PAIR
    private String twoPairType;
    private int secondPairRanking;
    private String secondPairType;

    // although third pairs are possible, they don't count for anything.

    // Set Analysis
    private int firstSetRanking;
    private int secondSetRanking;
    private int numSetCardsOnTable;
    private boolean isConcealedSet;
    private String firstSetType; // top set, 2nd set, 3rd set, ..., bottom set.

    // FullHouse Analysis
    private boolean isBigFullHouse; // you make top set on the flop - AA flop AKK
    private boolean isUnderFullHouse; // you make bottom set on flop - KK flop KAA
    private boolean isFullHouseWithConcealedOverPair; // overpair could be KK with JJJ on the flop
    private boolean isFullHouseWithConcealedUnderPair; // underpair could be QQ with AAA on the flop
    private int fhSetRank;
    private int fhPairRank;
    private String fullHouseHandType;

    // Quads analysis
    private int quadRank;
    private int numQuadCardsOnTable;
    private String quadType; // top quads, 2nd rank Quads, etc, lowest ranked Quads

    // used for straight analysis.
    private int highestPossibleStraight;
    private int maxConsecutive;
    private boolean isTableStraight;
    private boolean fourToStraightOnTable;
    private List<Integer> possibleOutsForAStraight;
    private Integer possibleOutForAGutshot;

    // Flush analysis
    private int highestFlushRank;
    private boolean isTableFlush;
    private boolean isFourToFlushOnTable;

    // Board has table matches
    private boolean tableQuadsPresent;
    private boolean tableSetPresent;
    private boolean tablePairPresent;

    private Outs outs;


	public Hand(Card[] hole, Card[] community)
	{
        this.hole = hole;
        this.community = community;
        /**
         * init the arrays with 0's so they can be populated
         * with useful analysis information.
         */
        initArrays();

        outs = new Outs(hole, community);

        /**
         * populate the arrays with the hole and community cards
         * so that analysis can begin
         */
        populateHoleRanks(hole);
        populateTableRanks(community);
        populateMatches(hole);
        populateMatches(community);
		populateSuitedCards(hole);
        populateSuitedCards(community);

		/**
		 * All variable array lists are now set up.
		 * The user of this module should now be able to iterate
		 * through various arrays to make comparisons and check
		 * for different types of hand, and calculate odds.
		 */
		
		checkMatching();
		matches.set(1, matches.get(14));
 		checkConsecutive();
        checkSuitedWithAcesPopulated();

	}

    private void initArrays(){

        tableRanks = new ArrayList<Integer>();
        tableSuits = new ArrayList<String>();

        holeRanks = new ArrayList<Integer>();
        holeSuits = new ArrayList<String>();

        holeMatches = new ArrayList<Integer>();

        matches = new ArrayList<Integer>();
        suited = new ArrayList<List<Integer>>();

        // prime the array lists for storing aggregated data
        for (int i = 0; i<15; i++)
        {
            holeMatches.add(new Integer(0));
            matches.add(new Integer(0));
        }

        hearts = new ArrayList<Integer>();
        diamonds = new ArrayList<Integer>();
        spades = new ArrayList<Integer>();
        clubs = new ArrayList<Integer>();

        suited.add(hearts);
        suited.add(diamonds);
        suited.add(spades);
        suited.add(clubs);

        // for all 4 suits add 14 placeholders to determine which are on/off
        for (int i=0; i<4; i++)
        {
            for (int j=0; j<15; j++)
            {
                suited.get(i).add(new Integer(0));
            }
        }

        // for all ranks add a placeholder for each.
        for (int i=0; i<15; i++)
        {
            // perhaps better to use Arrays.fill.
            tableRanks.add(new Integer(0));
            holeRanks.add(new Integer(0));
        }
    }

    private void populateHoleRanks(Card[] cards){
        // prime the holeRanks/holeSuits array with aggregated data
        for (Card c : cards)
        {
            holeRanks.set(c.getRank(), holeRanks.get(c.getRank()) + 1);
            holeSuits.add(c.getSuit());
        }
    }

    private void populateTableRanks(Card[] cards){
        // prime the holeRanks/holeSuits array with aggregated data
        for (Card c : cards)
        {
            tableRanks.set(c.getRank(), tableRanks.get(c.getRank()) + 1);
            tableSuits.add(c.getSuit());
        }
        communityCardsSize = cards.length;
    }

    private void populateMatches(Card[] cards){
        // prime the matches arraylist with matching cards between
        // hole and community cards only
        for (Card card : cards)
        {
            matches.set(card.getRank(), matches.get(card.getRank()) + 1);
        }
    }

    private void populateSuitedCards(Card[] cards){
        // prime the suited arraylist : with hole card suits
        for (int i=0; i<cards.length; i++)
        {
            if (cards[i].getSuit().toUpperCase().equals("H"))
            {
                suited.get(0).set(0, suited.get(0).get(0)+1);
                suited.get(0).set(cards[i].getRank(), new Integer(1));
            }
            else if (cards[i].getSuit().toUpperCase().equals("D"))
            {
                suited.get(1).set(0, suited.get(1).get(0)+1);
                suited.get(1).set(cards[i].getRank(), new Integer(1));
            }
            else if (cards[i].getSuit().toUpperCase().equals("S"))
            {
                suited.get(2).set(0, suited.get(2).get(0)+1);
                suited.get(2).set(cards[i].getRank(), new Integer(1));
            }
            else if (cards[i].getSuit().toUpperCase().equals("C"))
            {
                suited.get(3).set(0, suited.get(3).get(0)+1);
                suited.get(3).set(cards[i].getRank(), new Integer(1));
            }
        }
    }

    private void checkSuitedWithAcesPopulated(){
        // Set the 1st position element of the suited arrays to 1
        // so we can check for straight flushes.
        suited.get(0).set(1, suited.get(0).get(14));
        suited.get(1).set(1, suited.get(1).get(14));
        suited.get(2).set(1, suited.get(2).get(14));
        suited.get(3).set(1, suited.get(3).get(14));

        checkSuited();
    }

	/**
	 * Checks for the number of matching ranks
	 * and updates the has(hand) variables
	 * where appropriate.
	 */
	public void checkMatching()
	{
		for (int i=matches.size()-1; i>=2; i--)
		{
			if (matches.get(i).intValue() == 4)
			{
				analyseQuads(i);
                break;
			} 
			else if (matches.get(i).intValue() == 3)
			{
                if (!hasSet) {
				    analyseSetOrFullHouse(i);
                } else {
                    analyseFullHouse(i);
                }
			}
			else if (matches.get(i).intValue() == 2)
			{
				if (!hasQuads && !hasFullHouse){
					if (hasSet) {
                        analysePair(i);
                        analyseFullHouse(i);
					} else if (hasPair && !hasTwoPair) {
                        analyseTwoPair(i);
					} else {
                        analysePair(i);
					}
				}
			}
		}
	}

    private void analyseQuads(int i){
        hasQuads = true;
        quadRank = i;
        if (holeRanks.get(i)==2){
            quadType = "QUADS (concealed)";
        } else {
            quadType = "QUADS";
        }
        numQuadCardsOnTable = tableRanks.get(i);
    }

    /**
     * if you get to this code segment a set has been encountered
     * in the matches array.
     */
    private void analyseSetOrFullHouse(int i){

        if (!hasQuads) {
            if (matches.get(i)==3){
                analyseSet(i);
            } else {
                analysePair(i);
            }
            if (hasPair) {
                analyseFullHouse(i);
            }
        }
    }

    private void analyseFullHouse(int i){

        hasFullHouse = true;

        if ( tableRanks.get(firstSetRanking) == 3 ) {
            if (getCardRankingFromTableCards(firstPairRanking) == 1 && isConcealedOverpair){
                isFullHouseWithConcealedOverPair = true;

                fullHouseHandType = "BIG FULL w/Overpair (concealed)";

            } else if (getCardRankingFromTableCards(firstPairRanking) != 1 && isConcealedUnderpair) {
                isFullHouseWithConcealedUnderPair = true;
                fullHouseHandType = "BIG FULL w/Underpair (concealed)";
            } else {
                fullHouseHandType = "BIG FULL";
            }
        } else if (firstSetRanking > firstPairRanking){
            // SET RANK > PAIR RANK = big full house
            isBigFullHouse = true;
            if (isConcealedSet){
                fullHouseHandType = "BIG FULL (concealed)";
            } else {
                fullHouseHandType = "BIG FULL";
            }

        } else if ( firstPairRanking > firstSetRanking){
            // SET RANK < BIG PAIR RANK = under full house
            isUnderFullHouse = true;
            if (isConcealedSet){
                fullHouseHandType = "UNDER FULL (concealed)";
            } else {
                fullHouseHandType = "UNDER FULL";
            }
        }

        fhSetRank = firstSetRanking;
        fhPairRank = firstPairRanking;
    }

    private void analyseSet(int i){

        hasSet = true;

        if (firstSetRanking == 0){
            firstSetRanking = i;
            if (getCardRankingFromTableCards(firstSetRanking) == 1){
                firstSetType = "TOP SET";
            } else {
                firstSetType = "SET " + getCardRankingFromTableCards(firstSetRanking);
            }
        }

        numSetCardsOnTable = tableRanks.get(i);

        if (numSetCardsOnTable == 1){
            isConcealedSet = true;
            firstSetType = firstSetType + " (concealed)";
        }
    }

    private void analyseTwoPair(int i){
        hasTwoPair = true;
        secondPairRanking = i;

        // check if the second pair found is an underpair - it can only be that or an open pair.
        if (holeRanks.get(i)==2){
            isConcealedUnderpair = true;
        }

        checkForTablePair(i);

        if (isConcealedOverpair){
            if ((getCardRankingFromTableCards(firstPairRanking) == 1 && getCardRankingFromTableCards(secondPairRanking) ==2) ||
                    (getCardRankingFromTableCards(firstPairRanking) == 2 && getCardRankingFromTableCards(secondPairRanking) == 1)){
                twoPairType = "TOP 2PAIR W/Overpair";
            } else {
                twoPairType = "2PAIR W/Overpair";
            }
        } else if (isConcealedUnderpair){
            if ((getCardRankingFromTableCards(firstPairRanking) == 1 && getCardRankingFromTableCards(secondPairRanking) ==2) ||
                    (getCardRankingFromTableCards(firstPairRanking) == 2 && getCardRankingFromTableCards(secondPairRanking) ==1)){
                twoPairType = "TOP 2PAIR W/Underpair";
            } else {
                twoPairType = "2PAIR W/Underpair - (" + getCardRankingFromTableCards(firstPairRanking) + ", " + getCardRankingFromTableCards(secondPairRanking) + ")";
            }
        } else {
            if ((getCardRankingFromTableCards(firstPairRanking) == 1 && getCardRankingFromTableCards(secondPairRanking) == 2)){
                twoPairType = "TOP 2PAIR";
            } else {
                twoPairType = "2PAIR - (" + getCardRankingFromTableCards(firstPairRanking) + ", " + getCardRankingFromTableCards(secondPairRanking) + ")";
            }
        }


    }

    private void analysePair(int i){
        hasPair = true;
        firstPairRanking = i;
        // concealed pair

        checkForTablePair(i);

        if (holeRanks.get(i)==2) {
            int cardsOnFlopGreaterThanHolePair = getCardRankingFromTableCards(i);
            if (cardsOnFlopGreaterThanHolePair == 1){
                isConcealedOverpair = true;
                firstPairType = "OVERPAIR";
            } else {
                isConcealedUnderpair = true;
                firstPairType = "UNDERPAIR (concealed)" + getCardRankingFromTableCards(i);
            }
        } else {
            // PAIR 1 = TOP PAIR
            // PAIR 2 = 2ND PAIR
            // PAIR 3 = 3ND PAIR ... ETC.
            firstPairType = "PAIR " + getCardRankingFromTableCards(i);
        }
    }

    public void checkForTablePair(int i){
        if(tableRanks.get(i) == 2){
            tablePairPresent = true;
        }
    }

    private int getCardRankingFromTableCards(int i){

        int j = i;
        int cardRankOnTable = 0;
        while (j < 15) {
            if (tableRanks.get(j) > 0) {
                cardRankOnTable++;
            } else if (holeRanks.get(j) >1) { // only taken into account if there's a concealed pair
                cardRankOnTable++;
            }
            j++;
        }

        return cardRankOnTable;
    }

	
	/**
	 * Checks how many consecutive cards there are in the hand.
	 * If the hand has 3 or more consecutively ranked cards, 
	 * the hands will be classified as either ranked/unranked.  
	 */
	public void checkConsecutive(){
		int consCount = 0;
		int i; // must have full scope so analysis at end can be done if hasStraight

		for (i=matches.size()-1; i>4; i--)
		{
			for (int j=(i-4); j<=i; j++)
			{
				if (matches.get(j).intValue() > 0)
				{
					consCount++;
				}
			}
			
			// Check what types of straight are available.
			if (consCount == 3 && highestPossibleStraight == 0)
			{
				highestPossibleStraight = i;
			}
			else if (consCount == 4)
			{
                // if one card is missing at the top
                if ((matches.get(i-4).intValue() > 0 && matches.get(i).intValue() == 0))
                {
                    // add outs first then set hasOpenEndedStraightDraw to true as we only
                    // want to count the highest raw as the one with outs.
                    if(!hasOpenEndStraightDraw) {
                        possibleOutsForAStraight = new ArrayList<Integer>();

                        possibleOutsForAStraight.add(i);

                        //add the first card not present thats less than i
                        // this eliminates the need to check twice
                        for (int j=i-1; j>=1; j--){
                            if(matches.get(j)==0){
                                if (j==1){
                                    j = 14;
                                }
                                possibleOutsForAStraight.add(j);
                                break;
                            }
                        }
                    }

                    hasOpenEndStraightDraw = true;

                } else {

                    if(!hasGutshotDraw){
                        for (int j = (i - 4); j <= i; j++) {
                            if (matches.get(j).intValue() == 0) {
                                possibleOutForAGutshot = j;
                                break;
                            }
                        }
                    }

                    hasGutshotDraw = true;
                }
				
			}
			else if (consCount==5)
			{
				hasStraight = true;
			}

            // TODO - not sure this is necessary
			// Check what the highest possible straight is.
			if (maxConsecutive>=4 && i <= 12)
			{
				if (holeRanks.get(i).intValue() > 0 
				|| holeRanks.get(i-1).intValue() > 0
				|| holeRanks.get(i-2).intValue() > 0)
				{
					highestPossibleStraight = i; 
				}
				else
				{
					highestPossibleStraight = i + 2;
				}
			}
			else if (maxConsecutive >=4 && i >=13)
			{
				highestPossibleStraight = 14;
			}
			consCount = 0;
		}
	}
	
	/**
	 * Checks for flush based hands.
	 */
	public void checkSuited()
	{
		for (int i =0; i < suited.size(); i++)
		{
			if (suited.get(i).get(0).intValue() >= 5)
			{
				switch(i)
				{
					case 0:
						flushSuit = "Hearts";
						break;
					case 1:
						flushSuit = "Diamonds";
						break;
					case 2:
						flushSuit = "Spades";
						break;
					case 3:
						flushSuit = "Clubs";
						break;
				}

				for (int k=14; k>=5; k--)
				{
					if (suited.get(i).get(k).intValue() > 0 &&
							suited.get(i).get(k-1).intValue() > 0 &&
							suited.get(i).get(k-2).intValue() > 0 &&
							suited.get(i).get(k-3).intValue() > 0 &&
							suited.get(i).get(k-4).intValue() > 0)
					{
						if (k==14)
						{
							hasRoyalFlush = true;	
							break;
						}
						else
						{
							hasStraightFlush = true;
							
							break;
						}
					}
					if (suited.get(i).get(k) > highestSuitedRank)
					{
						hasFlush = true;
						highestSuitedRank = k;
					}
				}
			}
			else
			{
				if (suited.get(i).get(0).intValue() == 4)
				{
					hasFlushDraw = true;
					switch(i)
					{
						case 0:
							flushSuit = "Hearts";
                            break;
						case 1:
							flushSuit = "Diamonds";
                            break;
						case 2:
							flushSuit = "Spades";
                            break;
						case 3:
							flushSuit = "Clubs";
                            break;
					}

					for (int k=14; k>=5; k--)
					{
						if (suited.get(i).get(k).intValue() > 0)
						{
							highestSuitedRank = k;
							break;
						}
					}
				}
			}
			
			if (hasRoyalFlush || hasStraightFlush || hasFlush){
				break;
			}
		}
	}
	
	public String getHand(){
        String handType = "";


        if (hasRoyalFlush)
        {
            handType = "ROYAL FLUSH";
        }
        else if (hasStraightFlush)
        {
            handType = "STRAIGHT FLUSH";
        }
        else if (hasQuads)
        {
            handType = quadType;
        }
        else if (hasFullHouse)
        {
            handType = fullHouseHandType;
        }
        else {
            if (hasFlush) {
                handType = "FLUSH";

                // Check if you have the nut flush
                if (isFourToFlushOnTable) {
                    List<Integer> flushSuitCards;
                    switch (flushSuit.charAt(0)) {
                        case ('H'):
                            flushSuitCards = hearts;
                            break;
                        case ('D'):
                            flushSuitCards = diamonds;
                            break;
                        case ('C'):
                            flushSuitCards = clubs;
                            break;
                        default:
                            flushSuitCards = spades;
                            break;
                    }

                    boolean hasNutFlush = false;

                    // Check if if have the highest possible flush card not in your hole cards
                    for (int i = 14; i > 5; i--) {
                        if (flushSuitCards.get(i) > 0 &&
                                holeSuits.get(i).equals(flushSuit.substring(0,1))){
                            hasNutFlush = true;
                            break;
                        }
                    }

                    handType = (hasNutFlush ? "NUT " : "") + handType + (isFourToFlushOnTable ? " (counterfit)" : "");
                }
            } else if (hasStraight) {
                handType = "STRAIGHT";

                // you can always tie a nut straight but you can only win/tie with one, rather than lose
                boolean hasNutStraight = false;

                if (holeRanks.get(highestPossibleStraight - 1) > 0 || holeRanks.get(highestPossibleStraight - 2) > 0 ) {
                    hasNutStraight = true;
                }

                handType = (hasNutStraight ? "NUT " : "") + handType + (fourToStraightOnTable ? " (counterfit)" : "");
            } else if (hasSet) {
                handType = firstSetType;
            } else if (hasTwoPair) {
                handType = twoPairType;
            } else if (hasPair) {
                handType = firstPairType;
            } else {
                handType = "HIGH CARD";
            }
        }

        int i = 2;
        int j = 0;
        while (i <= 14)
        {
            j += tableRanks.get(i);

            i++;
        }

        if (j > 2 && communityCardsSize != 5) {
            if (hasFlushDraw && !hasFlush && !hasFullHouse && !hasQuads && !hasStraightFlush && !hasRoyalFlush) {
                if (handType.equals("HIGH CARD")) {
                    handType = "flushDraw";
                } else {
                    handType = handType + " +flushDraw";
                }
            }
            if (hasOpenEndStraightDraw && !hasStraight && !hasFlush && !hasFullHouse && !hasQuads && !hasStraightFlush && !hasRoyalFlush) {
                if (handType.equals("HIGH CARD")) {
                    handType = "straightDraw";
                } else {
                    handType = handType + " +straightDraw";
                }
            } else if (hasGutshotDraw && !hasStraight && !hasFlush && !hasFullHouse && !hasQuads && !hasStraightFlush && !hasRoyalFlush) {
                if (handType.equals("HIGH CARD")) {
                    handType = "gutshot";
                } else {
                    handType = handType + " +gutshot";
                }
            }
        }
        return handType;
    }

    /**
     * takes a parameter r to return the number of outs for a hand.
     * @return
     */
    public String getOuts(){
        return outs.getOutsSize() + "";
    }

    public static String getPreflopHandType(Card[] cards){

        if (cards[0].getRank() == cards[1].getRank()){
            return "Pair-" + cards[0].getStringRank() + cards[1].getStringRank();
        } else if (cards[0].getStringRank().equals("A")){
            if (cards[1].getStringRank().equals("K")){
                return "AK" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else if (cards[1].getStringRank().equals("Q")) {
                return "AQ" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else {
                return "AX" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            }
        } else if (cards[1].getStringRank().equals("A")) {
            if (cards[0].getStringRank().equals("K")){
                return "AK" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else if (cards[0].getStringRank().equals("Q")) {
                return "AQ" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else {
                return "AX" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            }
        } else if (cards[0].getStringRank().equals("K")){
           if (cards[1].getStringRank().equals("Q")) {
               return "KQ" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else if (cards[1].getStringRank().equals("J")) {
                return "KJ" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else {
                return "KX" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            }
        } else if (cards[1].getStringRank().equals("K")){
            if (cards[0].getStringRank().equals("Q")) {
                return "KQ" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else if (cards[0].getStringRank().equals("J")) {
                return "KJ" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            } else {
                return "KX" + (cards[0].getSuit().equals(cards[1].getSuit()) ? "s" : "");
            }
        } else if(Math.abs(cards[0].getRank()-cards[1].getRank())<4){
            if(cards[0].getSuit().equals(cards[1].getSuit())){
                return "SConns-" + Math.abs(cards[0].getRank()-cards[1].getRank());
            } else {
                return "Conns-" + Math.abs(cards[0].getRank()-cards[1].getRank());
            }
        } else if (cards[0].getSuit().equals(cards[1].getSuit())){
            return "Suited";
        } else if(Math.abs(cards[0].getRank()-cards[1].getRank())<4) {
            return "Conns-" + Math.abs(cards[0].getRank()-cards[1].getRank());
        } else {
            return "JUNK-" + (cards[0].getRank()>cards[1].getRank() ?
                    cards[0].getStringRank() + cards[1].getStringRank() :
                    cards[1].getStringRank() + cards[0].getStringRank());
        }
    }

    public boolean isOverCardToCommunityCards(int rank) {
        for (int i = rank; i<=14; i++) {
            if (tableRanks.get(i) > 0) {
                return false;
            }
        }
        return true;
    }
    public void getOutsForAnyOverCards() {
        // Check for over cards as outs
        if (isOverCardToCommunityCards(hole[0].getRank())) {
            outs.addRankAsOut(hole[0].getRank());
        }
        if (isOverCardToCommunityCards(hole[1].getRank())) {
            outs.addRankAsOut(hole[1].getRank());
        }
    }

    /**
     * Analyses the table cards to see what the possibilities are
     * @param cards
     */
    public void communityCardsAnalysis(Card[] cards){

        int largestDistance = 0;
        int largestRank = 0;
        int isOrdered = 0;

        ArrayList<Integer> rs = getRanksInArray(cards);
        ArrayList<Integer> ss = getSuitsInArray(cards);

        int highestMatchingPair = getHighestTablePair();
        int roundsBetweenMatchingRanks = roundsBetweenPairedRanks(cards, highestMatchingPair);
        int totalMatchingRanks = getNumberOfMatchedRanksOnDisplay(rs);

        int numberOfSequentialCardsOnShow = getSequentialCardsOnShow(rs);
        int highestPossibleStraight = getHighestPossibleStraight(rs);
//        int orderRating = getOrderRatingForStraight(cards, rs, highestPossibleStraight);

    }

    public int getHighestTablePair(){
        int result = 0;
        if (tablePairCount>0){
            for (Integer r : tableRanks){
                if (r==2){
                    result = r;
                    break;
                }
            }
        }
        return result;
    }

    public static int getNumberOfMatchedRanksOnDisplay(ArrayList<Integer> ranks){
        int totalMatchedRanks = 0;
        for (int i=14; i<ranks.size(); i--){
            // work in reverse order to find out the boards highest ranked
            // matching card(s)
            if(ranks.get(i)>1){
              totalMatchedRanks++;
            }
        }
        return totalMatchedRanks;
    }

    public static int roundsBetweenPairedRanks(Card[] cards, int highestMatchingRank) {

        if (cards.length==5){
            if (cards[5].getRank()==highestMatchingRank){ //river matches
                if(cards[4].getRank()==highestMatchingRank){ // turn
                    return 1;
                } else {
                    return 2; // else flop.
                }
            } else if (cards[4].getRank()==highestMatchingRank){ //turn
                return 1; // matches flop
            } else {
                return 0; // both cards must be on the flop
            }
        } else if (cards.length==4){
            if (cards[4].getRank()==highestMatchingRank){ //turn
                return 1; // matches flop
            } else {
                return 0; // both cards must be on the flop
            }
        } else {
            return 0;
        }
    }

    public static ArrayList<Integer> getRanksInArray(Card[] cards){
        ArrayList<Integer> rs = new ArrayList<Integer>(); // ranks

        for(int i=0; i<15; i++){
            rs.add(0);
        }

        for (Card c : cards){
            if(c.getRank()==14){
                rs.set(c.getRank(), rs.get(c.getRank()) + 1);
                rs.set(1, rs.get(1) + 1);
            } else {
                rs.set(c.getRank(), rs.get(c.getRank()) + 1);
            }
        }

        return rs;
    }

    public static ArrayList<Integer> getSuitsInArray(Card[] cards){
        ArrayList<Integer> ss = new ArrayList<Integer>(); // suits

        for(int i=0; i<4; i++){
            ss.add(0);
        }

        for (Card c : cards){
            if (c.getSuit().equals("H")){
                ss.set(0, ss.get(0) + 1);
            } else if(c.getSuit().equals("D")){
                ss.set(1, ss.get(1) + 1);
            } else if(c.getSuit().equals("S")){
                ss.set(2, ss.get(2) + 1);
            } else if(c.getSuit().equals("C")){
                ss.set(3, ss.get(3) + 1);
            }
        }

        return ss;
    }

    public static int getSequentialCardsOnShow(ArrayList<Integer> ranks){
        int maxConsec = 0;
        for(int i=14; i>1; i--){
            maxConsec = 0;
            int consecutive =0;
            for(int j=i-4; j<i; j++){
                if (ranks.get(j)>0){
                    consecutive++;
                }
            }
            if (consecutive > maxConsec){
                maxConsec = consecutive;
            }
        }
        return maxConsec;
    }

    public static int getHighestPossibleStraight(ArrayList<Integer> ranks){
        int maxConsec = 0;
        for(int i=14; i>1; i--){
            maxConsec = 0;
            for(int j=i-4; j<i; j++){
                if (ranks.get(j)>0){
                    maxConsec++;
                }
            }
            if (maxConsec>=3){
                return i;
            }
        }
        return -1;
    }

    /**
     * compute as an 'absolute error' rating. if a card is 1 position out of sync to a perfect
     * straight, then it is either +/- 1 from its position - take the sum of squares as a measure.
     *
     * @return
     */
//    public static int getOrderRatingForStraight(Card[] cards, ArrayList<Integer> ranks, int highestPossibleStraight){
//        // computes the euclidean distance of the ordering from the ideal ordering
//
//
//    }

    public int calculateOuts(){
        if (hasFullHouse)
        {
            outs.addRankAsOut(firstSetRanking);
            if (secondSetRanking>0){
                outs.addRankAsOut(secondSetRanking);
            }
        }
        else if (hasFlush || hasStraight|| hasQuads|| hasStraightFlush){
            // no outs
        }
        else {
            if (hasSet) {
                if (isConcealedSet) {
                    for (Card c : community) {
                        outs.addRankAsOut(c.getStringRank());
                    }
                } else {
                    for (Card c : hole) {
                        outs.addRankAsOut(c.getStringRank());
                    }
                }
            } else if (hasTwoPair) {
                if (!tablePairPresent) {
                    outs.addRankAsOut(firstPairRanking);
                    outs.addRankAsOut(secondPairRanking);
                } else {
                    if (isConcealedOverpair || isConcealedUnderpair) {
                        outs.addRankAsOut(hole[0].toString().substring(0,1));
                    }
                }
            } else if (hasPair) {
                outs.addRankAsOut(firstPairRanking); // +2
                if (!isConcealedUnderpair || !isConcealedOverpair) {
                    if (firstPairRanking == hole[0].getRank()) {
                        outs.addRankAsOut(hole[1].getRank());
                    } else {
                        outs.addRankAsOut(hole[0].getRank());
                    }
                }
            }
        }

        int i = 2;
        int j = 0;

        // Count how many cards have been dealt
        while (i <= 14) { // for 2..14 (2h to 13/K, 14/A high)
            j += tableRanks.get(i); // count the number of cards on display to me.
            i++;
        }

        // If I see more than just my 2 hole cards && we're not at the RIVER yet...
        if (j > 2 && communityCardsSize != 5) {
            // flushDraw +9 outs.
            if (hasFlushDraw && !hasFlush && !hasFullHouse && !hasQuads && !hasStraightFlush && !hasRoyalFlush) {
                outs.addSuitAsOut(flushSuit.substring(0, 1));
            }
            // open-end straight draw +8 outs
            if (hasOpenEndStraightDraw
                    && !hasStraight
                    && !hasFlush
                    && !hasFullHouse
                    && !hasQuads
                    && !hasStraightFlush
                    && !hasRoyalFlush) {

                if (fourToStraightOnTable){
                    if (highestPossibleStraight == 14) {
                        if(tableRanks.get(13)>0) {
                            outs.addRankAsOut("A");
                            outs.addRankAsOut("9");
                        } else {
                            outs.addRankAsOut("K");
                            outs.addRankAsOut("8");
                        }
                    } else if (highestPossibleStraight == 7) {
                        if(tableRanks.get(6)>0) { // if I can see a 6 then my outs are..
                            outs.addRankAsOut("7");
                            outs.addRankAsOut("2");
                        } else { // my outs are
                            outs.addRankAsOut("6");
                            outs.addRankAsOut("A");
                        }
                    } else {
                        outs.addRankAsOut(highestPossibleStraight-2);
                        outs.addRankAsOut(highestPossibleStraight-5);
                    }
                } else {
                    outs.addRankAsOut(possibleOutsForAStraight.get(0));
                    outs.addRankAsOut(possibleOutsForAStraight.get(1));
                }

            // check for a gutshot draw if no open end draw available - +4 outs
            } else if (hasGutshotDraw
                    && !hasStraight
                    && !hasFlush
                    && !hasFullHouse
                    && !hasQuads
                    && !hasStraightFlush
                    && !hasRoyalFlush) {
                outs.addRankAsOut(possibleOutForAGutshot);
            }
        }

        // Check for over cards that can be used as outs - 6 for AK, for example.
        if (!(hasFlush || hasStraight || hasQuads || hasStraightFlush)) {
            getOutsForAnyOverCards();
        }

        return outs.getOutsSize();
    }

    public String communityHandType(){
        String result = "";
        return result;
    }
}