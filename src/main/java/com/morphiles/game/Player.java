package com.morphiles.game;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player {
	
	private String playerId;
	private BigDecimal stack;
	private int seatId;
	private Card[] holeCards;
	private Hand hand;

	private static String currency;

    private static BigDecimal BB_amount;
    private static BigDecimal SB_amount;
    private static BigDecimal ante;

    private static Date date; // not thread safe - get joda time to avoid issues later on.
    private static String time;
    private static String limitType;

	private BigDecimal[] contributions;
	private String[] actions;
	private BigDecimal profitAmount;
	
	private static int[] playersPerRound;
	private static int tablePlayerCount;
	private static int totalSeatCount;
	
	private static int buttonSeat;

    private String preflopHand;
	
	private boolean isBigBlind;
	private boolean isSmallBlind;
	private boolean isWinner;
	
	private boolean hasLeft; // 1 - leaves table
	private boolean hasJoined; // 1 - new joiner 
	
	
	// the variables below belong to all players so are staic.
	// this makes updating them easier.
	private static BigDecimal[] totalPot;
	private BigDecimal[] myPot; // TODO - not sure this is actually used by anything yet. Intention was to ensure a player's maximum put was limited to only the amount they could contribute to the pot.
	
	// belong to all players hands.
	private static List<Card> communityCards;
	
	// this array stores the nut hand for each round.
	// only shows for the flop the turn and the river.
	public static String[] nutsForRound;
	
	
	public Player(int seatId) {
		this.seatId = seatId;
		playerId = "";
		stack = new BigDecimal(0.0);
		currency="";
		communityCards = new ArrayList<>();
		
		initTotals();
	}
	
	// not really used except in the games.
	public Player(String name, BigDecimal stack, int seatId, String currency) {
		this.playerId = name;
		this.stack = stack;
		this.seatId = seatId;
		this.currency = currency;
		communityCards = new ArrayList<>();
		
		initTotals();
	}
	
	public Card[] getHoleCards()
	{
		return holeCards;
	}
	
	public String getHoleCardsAsString() {
		String temp = "";
		if (holeCards!=null) {
			temp = holeCards[0].toString() + " " + holeCards[1].toString();
		} else {
			temp = "";
		}
		return temp;
	}

	public void setHoleCards(Card[] holeCards) {
		this.holeCards = holeCards;
        this.preflopHand = Hand.getPreflopHandType(holeCards);
	}
	
	public String getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(String playerId)
	{
		this.playerId = playerId;
	}
	public BigDecimal getStack()
	{
		return stack;
	}
	public void setStack(BigDecimal stack)
	{
		this.stack = stack;
	}
	public int getSeatId()
	{
		return seatId;
	}
	public void setSeatId(int seatId)
	{
		this.seatId = seatId;
	}
	
	public String currencyType()
	{
		return currency;
	}
	
	public void setCurrency(String curr)
	{
		this.currency = curr;
	}
	
	public void updateContributions(int round, BigDecimal amount) {
		// Contributions is calculated per round.
		contributions[round] = contributions[round].add(amount);

		// TODO the scope of totalPot is wrong - it should be set at a table/game level
		// TODO rather than at a player level as this gets very confusing.
		// Total pot is a static variable so any additional bets are added to the
		// pot for all players.

		if (totalPot[round].equals(new BigDecimal(0.0)) && round != 0) {
			totalPot[round] = totalPot[round-1].add(amount);
			// Once a player puts in money beyond the first round, the total amount from
			// the previous round's pot total must be carried forward -
			// that's what this check is about...
			// it carries the bets from the previous rounds forward
			// so that the total for each round is cumulative.
		} else {
		    totalPot[round] = totalPot[round].add(amount);
		}
		
		// profitAmount is a total of all contributions to a pot
		// and will be used at the end to calculate any overall profit
		profitAmount = profitAmount.subtract(amount);
		
		myPot[round] = totalPot[round];
	}
	
	public BigDecimal getContributions(int round) {
		return contributions[round];
	}
	
	public void updateActions(int round, String data) {
		if (actions[round].equals("")) {
			actions[round] = getAction(round, data).replace(",","");
		} else {
			actions[round] = actions[round] + "-" + getAction(round, data).replace(",","");
		}
	}
	
	public String getActions(int round) {
		for (int i=0; i<round; i++) {
			if (actions[i].contains("all-In")) {
				// all in will be the last move so just take
				// substring from position of 'all-in' to end
				if (playersPerRound[round]>=2){
					return actions[i].substring(actions[i].indexOf("all-In"));
				} else {
					return "";
				}
			}
		}
		if (actions[round] == null) {
			return "";
		}
		return actions[round];
	}

	/**
	 * WARNING: This doesnt do what you think..
	 * It gets the total pot for the player based on their action... if the
	 * player's move for this round was all-In then
	 * the value returned will be something besides: totalPot[round]
	 * @param round
	 * @return
	 */
	public BigDecimal getTotalPot(int round) {
		if (round==0) {
			if (actions[round].contains("all-In")) {
				if (playersPerRound[round]>=2) {
					return myPot[round];
				} else {
					return new BigDecimal(0.0);
				}
			}
		} else {
			for (int i=0; i<=round; i++) {
				if (actions[i].contains("all-In") && playersPerRound[round]>=2) {
					// all in will be the last move so just take
					// substring from position of 'all-in' to end
					return myPot[i];
				}
			}
		}
		
		// If there's no all in continue to process data so 
		// 0 output goes out.
		if (actions[round].contains("fold") || actions[round].equals("")) {
			return new BigDecimal(0.0);
		} else {
			return totalPot[round];
		}
	}

	public BigDecimal getTotalPotAmount(int round) {
		return totalPot[round];
	}
	
	public String getAction(int round, String data)
	{
		String thisAction= "";
		if (data.contains(" folds")) {
			thisAction = "fold";
			updatePlayerCount(round);
		}
		if (data.contains(" checks")) {
			thisAction = "check";
		}
		if (data.contains(" calls ")) {
			thisAction = data.substring(data.indexOf(" ")+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€")) {
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
		if (data.contains(" bets ")) {
			thisAction = data.substring(data.indexOf(" ")+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€")) {
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
		if (data.contains(" raises ")) {
			thisAction = data.substring(data.indexOf(" ")+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€")) {
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
		if (data.contains(" is all-In ")) {
			thisAction = data.substring(data.indexOf(" ",  data.indexOf(" ", data.indexOf("is")+2))+1).replace("[","").replace("]","");
			if (thisAction.contains("$") || thisAction.contains("£") || thisAction.contains("€")) {
				thisAction = thisAction.substring(0, thisAction.length()-4);
			}
		}
        if (data.contains("posts big blind + dead ")) {
            thisAction = "BB+DB " + getBetAmount(data);
        } else if (data.contains("posts big blind")) {
			thisAction = "BB " + getBetAmount(data);
		} else if (data.contains("posts small blind")) {
			thisAction = "SB " + getBetAmount(data);
		}
		
		return thisAction;
	}

	public String getBetAmount(String data) {
		String[] tempArray1 = data.split("\\[");
		String temp =  tempArray1[1];
		String[] tempArray2 = temp.split("\\]");
		temp = tempArray2[0].replace(",", "");
		String[] tempArray3 = temp.split(" ");
		return tempArray3[0];
	}
	
	public void setCommunityCards(int round, String data) {
		if (round == 1) {
			String tempFlop = data.substring(data.indexOf("[")+1, data.indexOf("]")).replace(",","").trim();
			
			communityCards.add(new Card(tempFlop.substring(0, tempFlop.indexOf(" "))));
			communityCards.add(new Card(tempFlop.substring(tempFlop.indexOf(" ")+1,tempFlop.lastIndexOf(" "))));
			communityCards.add(new Card(tempFlop.substring(tempFlop.lastIndexOf(" ")+1)));
		}
		else // turn & river. - not same for both - hence Trim(). difference = 1.
		{
			String temp = data.substring(data.indexOf("[")+1,data.indexOf("]")).trim();
			communityCards.add(new Card(temp));
		}
	}
	
	public String getCommunityCards(int round) {
		switch(round){
			case(1):
				if (communityCards.size()>=3) {
					return communityCards.get(0) + ", "
							+ communityCards.get(1) + ", "
							+ communityCards.get(2);
				}
			case(2):
				if (communityCards.size()>=4) {
					return communityCards.get(3) + "";
				}
			default:
				if (communityCards.size()>=5) {
					return communityCards.get(4) + "";
				}
		}
		//noop - this will never be returned.
		return "";
	}
	
	public void setTotalPlayerCount(int value) {
		playersPerRound[0] = value;
		playersPerRound[1] = value;
		playersPerRound[2] = value;
		playersPerRound[3] = value;
		
		tablePlayerCount = value;
		
		// these will be updated per round...
		// this will happen by reducing all subsequent rounds 
		// by 1 player each time a player folds.
	}
	
	public static void updatePlayerCount(int round) {
		for (int i=3; i>=round; i--) {
			playersPerRound[i] -= 1;
		}
	}
	
	public String getPlayerCountForRound(int round) {
		for (int i=0; i<round; i++) {
			if (playersPerRound[i]<=1) {
				return "";
			}
		}
		return playersPerRound[round]+"";
	}
	
	public int getTablePlayerCount()
	{
		return tablePlayerCount;
	}
	
	public String getSeatPosition(int seat) {
		//TODO double check this is correct
		String position = "-1";
		if (seat == buttonSeat) {
			position = "BTN";
		} else if (isBigBlind) {
			position = "BB";
		} else if (isSmallBlind) {
			position = "SB";
		} else {
			position = "" + (seat%(tablePlayerCount+1));
		}
		return position;
	}
	
	public int getButtonSeat()
	{
		return this.buttonSeat;
	}
	
	public void setButtonSeat(int value)
	{
		this.buttonSeat = value;
	}
	
	public int getSeatCount()
	{
		return this.totalSeatCount;
	}
	
	public void setTotalSeatCount(int value)
	{
		this.totalSeatCount = value;
	}
	
	public void setBigBlind(boolean isBB)
	{
		isBigBlind = isBB;
	}

    public boolean isBigBlind()
    {
        return isBigBlind;
    }
	
	public void setSmallBlind(boolean isSB)
	{
		isSmallBlind = isSB;
	}
	
	public boolean isSmallBlind()
	{
		return isSmallBlind;
	}
	
	public void updateProfitAmount(BigDecimal amount){
		profitAmount = profitAmount.add(amount);
	}
	
	
	public BigDecimal getProfit(){
		return profitAmount;
	}
	
	public void setWinner(boolean win)
	{
		isWinner = win;
	}
	
	public void updateTimeBank(int round) {
		if (actions[round].equals("")) {
			actions[round] = "(T)";
		} else {
			actions[round] = actions[round] + "-(T)";
		}
	}
	
	public String getWinner() {
		if (isWinner) {
			return "Win";
		} else {
			return "";
		}
	}
	
	public void setLeftTable(boolean left)
	{
		hasLeft = left;
	}
	
	public void setJoinedTable(boolean joined)
	{
		hasJoined = joined;
	}
	
	
	public String getLeftTable() {
		if (hasLeft) {
			return "Left";
		} else if (hasJoined) {
			return "Joined";
		} else {
			return "";
		}
	}
	
	
	public Card[] getCardsUpto(int round){
		Card[] cs = {};
		switch(round) {
			case(1):
				cs = new Card[3];
				break;
			case(2):
				cs = new Card[4];
				break;
			case(3):
				cs = new Card[5];
				break;
		}		
		
		for (int i=0; i<cs.length; i++) {
			cs[i] = communityCards.get(i);
		}
		
		return cs;
	}
	
	public Card[] getCardsUptoTurn() {
		Card[] t = new Card[4];
		t[0] = communityCards.get(0);
		t[1] = communityCards.get(1);
		t[2] = communityCards.get(2);
		t[3] = communityCards.get(3);
		return t;
	}

    public String getPreflopHand(){
        return preflopHand;
    }
	
	public void initTotals() {
		contributions = new BigDecimal[4];
		actions = new String[4];
		totalPot = new BigDecimal[4];
		myPot = new BigDecimal[4];
		playersPerRound = new int[4];
		profitAmount = new BigDecimal(0);
				
		reset();
	}
	
	public void reset() {
		communityCards.clear();
		buttonSeat = -1;
		tablePlayerCount = -1;
		totalSeatCount = -1;
		isBigBlind = false;
		isSmallBlind = false;
		isWinner = false;
		hasLeft = false;
        preflopHand = "";

		for (int i=0; i<4; i++) {
			contributions[i] = new BigDecimal(0);
			actions[i] = "";
			totalPot[i] = new BigDecimal(0);
			myPot[i] = new BigDecimal(0);
			playersPerRound[i] = 0;
			profitAmount = new BigDecimal(0);
		}
	}
}