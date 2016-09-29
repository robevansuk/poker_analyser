package com.morphiles.game;

public class Deck {
	
	static Card[] deck = new Card[52];
	
	static{
		int index = 0;
		for (int i=0; i<=3; i++){
			for (int j=2; j<=14; j++){
				
				switch(i) {
					case(0):
						deck[index] = new Card(j, "H");
						break;
					case(1):
						deck[index] = new Card(j, "D");
						break;
					case(2):
						deck[index] = new Card(j, "C");
						break;
					case(3):
						deck[index] = new Card(j, "S");
						break;
				}
				
				// For testing: 
				//System.out.println("Card[" + index + "]: " + deck[index]);
				index++;
			}
		}
	}
	
	public static void shuffle(){
		Card temp;
		int j;
		
		for (int i=0; i<52; i++){
			temp = deck[i];
			j = (int) (Math.random() * 52); 
			deck[i] = deck[j];
			deck[j] = temp;
		}
	}
    public static Card[] getDeck(){
        return deck;
    }

	//public static void main(String[] args){
	//	new Deck();
	//	shuffle();
	//	for (int i=0; i<52; i++){
	//		System.out.println(deck[i]);
	//	}
	//}
	
}
