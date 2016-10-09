package com.morphiles.game;

public class Card {

	private int rank;
	private String namedRank;
	private String suit;
	
	/**
	 * A is stored as a 14 so manually
	 * check array's for A = 1 as well
	 * when checking.
	 * @param r
	 * @param s
	 */
	public Card(int r, String s){
		this.rank = r;
		this.namedRank = getRankFor(r);
		this.suit = s;
    }
	
	public Card(String s)
	{
		this.namedRank = s.substring(0,1);
		this.rank = getRankFor(namedRank);
		this.suit = s.substring(1,2);
	}
	
	public int getRankFor(String rank) {
		switch(rank) {
			case ("T"):
				return 10;
			case ("J"):
				return 11;
			case ("Q"):
				return 12;
			case ("K"):
				return 13;
			case ("A"):
				return 14;
			default:
				return new Integer(rank);
		}
	}

	public String getRankFor(int rank) {
		switch(rank) {
			case (10):
				return "T";
			case (11):
				return "J";
			case (12):
				return "Q";
			case (13):
				return "K";
			case (14):
				return "A";
			default:
				return rank + "";
		}
	}

	public String getSuit()
	{

		return suit;
	}

	public int getRank()
	{
		return rank;
	}

	public String getNamedRank() {
		return namedRank;
	}
	
	public String toString() {
		return namedRank + suit;
	}
	
}
