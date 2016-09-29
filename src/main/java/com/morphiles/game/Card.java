package com.morphiles.game;

public class Card {

	private int rank;
	private String suit;
	
	/**
	 * A is stored as a 14 so manually
	 * check array's for A = 1 as well
	 * when checking.
	 * @param r
	 * @param s
	 */
	public Card(int r, String s){
		rank = r;
		suit = s;
    }
	
	public Card(String s)
	{
		
		rank = getRankFor(s.substring(0,1));
		suit = s.substring(1,2);
		
		//System.out.print("RANK: " + rank);
		//System.out.println(" SUIT: " + suit);
	}
	
	public String getSuit()
	{
		return suit;
	}
	public void setSuit(String suit)
	{
		this.suit = suit;
	}
	public int getRank() 
	{
		return rank;
	}
	public void setRank(int rank) 
	{
		this.rank = rank;
	}
	public int getRankFor(String rank)
	{
        if (rank.equals("T"))
        {
            return 10;
        }
        else if (rank.equals("J"))
        {
            return 11;
        }
        else if (rank.equals("Q"))
        {
            return 12;
        }
        else if (rank.equals("K"))
        {
            return 13;
        }
        else if (rank.equals("A"))
        {
            return 14;
        }
        else
        {
            return new Integer(rank).intValue();
        }
	}

    public String getStringRank(){
        if (rank == 10)
        {
            return "T";
        }
        else if (rank == 11)
        {
            return "J";
        }
        else if (rank == 12)
        {
            return "Q";
        }
        else if (rank == 13)
        {
            return "K";
        }
        else if (rank == 14)
        {
            return "A";
        }
        else
        {
            return rank + "";
        }
    }
	
	public String toString(){
		switch (rank) {
			case(10):
				return "T" + suit;
			case(11):
				return "J" + suit;
			case(12):
				return "Q" + suit;
			case(13):
				return "K" + suit;
			case(14):
				return "A" + suit;
			default:
				return rank + suit;
		}
	}
	
	
}
