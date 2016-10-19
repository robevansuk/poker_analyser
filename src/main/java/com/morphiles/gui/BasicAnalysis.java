package com.morphiles.gui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.morphiles.game.Card;
import com.morphiles.game.Hand;

public class BasicAnalysis {

	private static String BASE_URL;
	private static final String filePath =  "C:\\Users\\rob\\Desktop\\";
	private final static int CASH = 0;
	private final static int TRNY = 1;

	private final static String BIG_BLIND1 = "Blinds(";
	private final static String BIG_BLIND2 = "Stakes(";
	private final static String BIG_BLIND3 = " posts big blind [";
	private final static String BIG_BLIND4 = " Blinds-Antes(";

	private final static String DEALT_TO = "Dealt to ";
	private final static String FLOP = "** Dealing Flop ** [ ";
	private final static String TURN = "** Dealing Turn ** [ ";
	private final static String RIVER = "** Dealing River ** [ ";
	private final static String DISCON1 = "Connection Lost due to some reason";
	private final static String DISCON2 = "The hand history for this hand number is not available here.";
	private final static String EOH = "\u0000";

	private final static String SHOW1 = " shows [ ";
	private final static String SHOW2 = " doesn't show [ ";
	private final static String WINS = " wins ";

	private FileOutputStream fos;
	private OutputStreamWriter out;
	private List<String> files;
	private List<File> fs;

	private int typeOfGame;
	private Card[] holeCards;
	private List<Card> communityCards;

	private String playerID;
	private BigDecimal playerStack;
	private BigDecimal bigBlind;
	private String BB_temp;
	private BigDecimal currentBet;
	private BigDecimal totalBetSoFar;
	private BigDecimal preflopLoss;
	private BigDecimal flopLoss;
	private BigDecimal turnLoss;
	private BigDecimal ante;
	private String hole;
	private String flop;
	private String turn;
	private String river;
	private BigDecimal totalWon;
	private List<BigDecimal> wins;
	private List<BigDecimal> losses;

	private boolean start;
	private boolean stop;
	private boolean disconnected;

	private String currency;
	private List<BigDecimal> profit;
	private List<BigDecimal> loss;
	private Hand hand;
	private List<String> stacks;

	private Hand flopHand;
	private Hand turnHand;

	private String handShown;

	private int handID;

	/**
	 * Executes a basic Analysis of no limit hold'em poker games
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public BasicAnalysis() throws FileNotFoundException, UnsupportedEncodingException
	{
		files = new ArrayList<String>();
		fs = new ArrayList<File>();

		File fileList = new File(getBaseURL());
		

		for (File f : fileList.listFiles()) {
			fs.add(f);
			files.add(f.getPath());
			System.out.println(files.get(files.size()-1));	
		}

		holeCards = new Card[2];
		communityCards = new ArrayList<>();
		wins = new ArrayList<>();
		stacks = new ArrayList<>();

		totalBetSoFar = new BigDecimal(0);

		process();
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getBaseURL(){
		if (BASE_URL == null) {
			BASE_URL = filePath;
		} 
		return BASE_URL;
	}
	
	public void setBaseURL(String url){
		BASE_URL=url;
	}

	/**
	 * Processes the files once they have been instantiated via the
	 * constructor. 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public void process() throws FileNotFoundException, UnsupportedEncodingException{
		handID = 0;
		fos = new FileOutputStream(filePath + "test.csv"); 
		out = new OutputStreamWriter(fos, "UTF-8");
		for (File f : fs)
		{
			FileInputStream fstream;
			try
			{
				fstream = new FileInputStream(f);
				// Get the object of DataInputStream

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine ;

				// Read File Line By Line
				try 
				{
					start = true;
					stop = false;
					disconnected = false;

					while ((strLine = br.readLine()) != null)
					{
						if (strLine.indexOf(BIG_BLIND1) >= 0 || strLine.indexOf(BIG_BLIND2) >= 0)
						{
							start = true;
							stop = false;

							typeOfGame = TRNY;
							bigBlind = getBigBlind(strLine); 

							// If this player puts in the blind,
							// add the blind amount to the total entered
							// into the pot so far
							if (playerID != null)
							{
								if (strLine.indexOf(playerID) >=0 && strLine.indexOf(":") < 0)
								{
									currentBet = bigBlind;
									totalBetSoFar = totalBetSoFar.add(bigBlind);
								}
							}
						} 
						if (strLine.indexOf(BIG_BLIND3) >= 0)
						{	
							typeOfGame = CASH;
							bigBlind = getBigBlind(strLine);
						}
						if (strLine.indexOf(BIG_BLIND4) >= 0)
						{
							typeOfGame = TRNY;
							bigBlind = getBigBlind(strLine);
							ante = getAnte(strLine);
						}
						if (strLine.indexOf(DEALT_TO) >= 0)
						{
							start = true;
							disconnected = false;

							setPlayerID(strLine);
						}
						if (strLine.indexOf(FLOP) >= 0)
						{
							setFlop(strLine);
							if (!flop.equals(""))
							{
								Card[] tableCards = new Card[communityCards.size()];

								// Copy over the tableCards whether
								// just a flop,
								// a flop and a turn,
								// or a flop, a turn and a river card 
								// are showing
								for (int i=0; i<communityCards.size(); i++)
								{
									tableCards[i] = communityCards.get(i);
								}
								flopHand = new Hand(holeCards, tableCards);
								preflopLoss = totalBetSoFar;
							}
						}
						if (strLine.indexOf(TURN) >= 0)
						{
							setTurn(strLine);
							if (!turn.equals(""))
							{
								Card[] tableCards = new Card[communityCards.size()];

								// Copy over the tableCards whether
								for (int i=0; i<communityCards.size(); i++)
								{
									tableCards[i] = communityCards.get(i);
								}
								turnHand = new Hand(holeCards, tableCards);
								flopLoss = totalBetSoFar.subtract(preflopLoss);
							}
						}
						if (strLine.indexOf(RIVER) >= 0)
						{
							setRiver(strLine);
							turnLoss = totalBetSoFar.subtract(flopLoss).subtract(preflopLoss);
						}
						if (strLine.indexOf(SHOW1) >= 0 || strLine.indexOf(SHOW2) >= 0)
						{
							getHandShown(strLine);
						}
						if (strLine.indexOf(WINS) >= 0)
						{
							while ((strLine = br.readLine()).indexOf(WINS) >= 0){
								getProfit(strLine);
							}
						}
						if (strLine.indexOf(":") <0 &&
								(strLine.indexOf(" calls ")>=0 || 
										strLine.indexOf(" raises ")>=0 ||
										strLine.indexOf(" is all-In ")>=0))
						{
							if (playerID != null)
							{
								if (strLine.indexOf(playerID) >=0)
								{
									totalBetSoFar = totalBetSoFar.add(getCurrentBet(strLine));
								}
							}
						}
						if (strLine.indexOf("Seat ") >= 0 && strLine.indexOf(":") >=0 
							&& strLine.indexOf("( ") >= 0 && strLine.indexOf(" )") >=0)
						{
							stacks.add(strLine);
						}
						if (strLine.indexOf(DISCON1)>=0 || strLine.indexOf(DISCON2) >= 0)
						{
							disconnected = true;
						}
						if (strLine.equals(""))
						{
							start = false;
							stop = true;
							if (!disconnected)
							{
								if (communityCards.size() != 0)
								{
									Card[] tableCards = new Card[communityCards.size()];

									// Copy over the tableCards whether
									// just a flop,
									// a flop and a turn,
									// or a flop, a turn and a river card 
									// are showing
									for (int i=0; i<communityCards.size(); i++)
									{
										tableCards[i] = communityCards.get(i);
									}

									hand = new Hand(holeCards, tableCards);
								}
								handID++;
							}
							resetHandVariables();
						}
					}
				} 
				catch (IOException e) 
				{
					System.out.println("ERROR: Cannot read line from " +  f.getName());
					e.printStackTrace();
				}

			} 
			catch (FileNotFoundException e)
			{
				System.out.println("ERROR: Cannot read " + f.getName());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the player ID given the "Dealt to" line
	 * @param line
	 */
	public void setPlayerID(String line)
	{
		holeCards[0] = null;
		holeCards[1] = null;

		communityCards.clear();

		playerID = line.substring(DEALT_TO.length(), line.indexOf(" ", DEALT_TO.length()));

		holeCards[0] = new Card(line.substring(line.indexOf("[ ")+ 3, line.indexOf("[ ")+ 5));
		holeCards[1] = new Card(line.substring(line.indexOf("[") + 6, line.indexOf("[") + 8));

		hole = holeCards[0] + " " + holeCards[1];
	}

	/**
	 * sets the flop
	 * @param line
	 */
	public void setFlop(String line)
	{
		flop = line.substring(FLOP.length(), FLOP.length() + 10);

		communityCards.add(new Card(flop.substring(0,2)));
		communityCards.add(new Card(flop.substring(4,6)));
		communityCards.add(new Card(flop.substring(8,10)));
		currentBet = new BigDecimal(0.0);
	}

	/**
	 * sets the turn
	 * @param line
	 */
	public void setTurn(String line)
	{
		turn = line.substring(TURN.length(), TURN.length() + 2);
		communityCards.add(new Card(turn));
		currentBet = new BigDecimal(0.0);
	}

	/**
	 * sets the river
	 * @param line
	 */
	public void setRiver(String line)
	{
		river = line.substring(RIVER.length(), RIVER.length() + 2);
		communityCards.add(new Card(river));
		currentBet = new BigDecimal(0.0);
	}

	/**
	 * returns the hand shown
	 * @param line
	 */
	public void getHandShown(String line)
	{
		if (playerID != null) 
		{
			if (line.indexOf(playerID) >= 0)
			{
				handShown = handShown + ", " + line;
			}
		}
	}

	/**
	 * Increments the profit variable for each pot won.
	 * @param line
	 */
	public void getProfit(String line)
	{
		if (playerID != null) 
		{
			if (line.indexOf(playerID) >= 0)
			{
				int startMark = WINS.length() + playerID.length();
				String value = line.substring(startMark, line.indexOf(" ", startMark));
				if (value.indexOf(",") >= 0)
				{
					value = value.replace(",", "");
				}
				if (currency != null)
				{
					if (value.indexOf(currency) >= 0)
					{
						value = value.substring(1,value.length());
					}
				}
				totalWon = totalWon.add(new BigDecimal(value));
			}
		}
	}

	public String getHoleCardHandType(String pocket){
		String result = "";

		if (pocket.substring(0,1).equals(pocket.substring(3,4)))
		{
			result = "PAIR";
		}
		else if (pocket.substring(1,2).equals(pocket.substring(4,5)))
		{
			result = "SUITED";
		}
		else if ((pocket.substring(0,1).equals("A") && pocket.substring(3,4).equals("K")) ||
				(pocket.substring(0,1).equals("K") && pocket.substring(3,4).equals("A")))
		{
			result = "AK";
		}

		return result;
	}

	/**
	 * fetches the big blind value 
	 * @param line
	 * @return
	 */
	public BigDecimal getBigBlind(String line)
	{
		BigDecimal bigBlind = new BigDecimal(0.0);

		if (typeOfGame == CASH)
		{
			currency = getCurrency(line);
			bigBlind = new BigDecimal(line.substring(
					line.indexOf(currency)+1,
					line.indexOf(" ", line.indexOf(currency))).replace(",", ""));
		}
		else
		{	
			if (line.indexOf(",")>=0)
			{	
				if (line.indexOf(BIG_BLIND4) >= 0){

					BB_temp = line.substring(
							line.indexOf("/")+1,
							line.indexOf(" -"));
				}
				else 	
				{
					BB_temp = line.substring(
							line.indexOf("/")+1,
							line.indexOf(")"));
				}
			}
			else
			{
				BB_temp = line.substring(
						line.indexOf("/")+1,
						line.indexOf(")"));
			}

			if (BB_temp.indexOf(",") >= 0)
			{
				BB_temp = BB_temp.replace(",","");
			}
			bigBlind = new BigDecimal(BB_temp);
		}
		//System.out.println("BB: " + line + " - *" + bigBlind + "*");

		return bigBlind;
	}

	/**
	 * gets the Ante
	 */
	public BigDecimal getAnte(String line)
	{
		BigDecimal theAnte;
		//System.out.println(line);
		if ( line.substring(
				line.indexOf("-", line.indexOf("-Ante")+1)+1,
				line.indexOf(")")).indexOf(",") >= 0)
		{
			theAnte = new BigDecimal(
					line.substring(
							line.indexOf("-", line.indexOf("-Ante")+1)+1,
							line.indexOf(")")
					).replace(",", ""));
		}
		else
		{
			theAnte = new BigDecimal(
					line.substring(
							line.indexOf("-", line.indexOf("-Ante")+1)+1,
							line.indexOf(")")
					));
		}

		return theAnte;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	public BigDecimal getCurrentBet(String line){
		if (typeOfGame == CASH)
		{
			String currency = getCurrency(line);
			//System.out.println(line);
			//System.out.println(line.substring(
			//				line.indexOf(currency)+1,
			//				line.indexOf(" ", line.indexOf(currency))).replace(",", ""));
			return new BigDecimal(
					line.substring(
							line.indexOf(currency)+1,
							line.indexOf(" ", line.indexOf(currency))).replace(",", "")
			);
		}
		else if (line.indexOf("[") >=0)
		{
			return new BigDecimal(
					line.substring(
							line.indexOf("[")+1,
							line.indexOf("]")).replace(",", "")
			);
		}
		else if (line.indexOf(WINS) >= 0)
		{
			return new BigDecimal(
					line.substring(
							line.indexOf(WINS) + 7, 
							line.indexOf(" ", line.indexOf(WINS) + 7)).replace(",", "")
			);
		} 
		else
		{
			System.out.println("ERROR: " + line);
			return new BigDecimal(0); // should never be returned.
		}

	}

	public String getCurrency(String line)
	{
		String currency = "";
		if (line.indexOf("$")>=0)
		{
			currency = "$";
		}
		else if (line.indexOf("£")>=0)
		{
			currency = "£";
		}
		else if (line.indexOf("€")>=0)
		{
			currency = "€";
		}
		return currency;
	}

	/**
	 * If any money is put in the pot by the hand history
	 * owner, get the bet amount and set the bet amount variable
	 * then increments the totalBetSoFar variable.
	 * 
	 * @param line
	 */
	public void getAmountPutIntoPot(String line)
	{
		if (playerID != null)
		{
			if (line.indexOf(playerID)>=0 && line.indexOf(":") <0)
			{
				currentBet = getCurrentBet(line);
				totalBetSoFar = totalBetSoFar.add(currentBet);
			}
		}
	}
	
	public BigDecimal getPlayerStack(String playerID)
	{
		String stackTemp = "";
		int i = 0;
		while (stacks.get(i).indexOf(playerID) < 0)
		{
			i++;
		}
		
		stackTemp = stacks.get(i);
		stackTemp = stackTemp.substring(
				stackTemp.indexOf("(")+2,
				stackTemp.indexOf(")") - 1);
		
		if (stackTemp.indexOf(",") >= 0)
		{
			stackTemp = stackTemp.replace(",", "");
		}
		
		if (stackTemp.indexOf(" ") >= 0)
		{
			stackTemp = stackTemp.substring(1, stackTemp.length() - 4);
		}
		
		//System.out.println("*" + stackTemp + "*");
		
		return new BigDecimal(stackTemp);
	}

	/**
	 * resets the variables
	 * @throws IOException 
	 */
	public void resetHandVariables() throws IOException{

		// print header for first hand.
		if (handID == 1){
			out.write("HandID,GameType,Stack,Player,BigBlind,Ante,Hole,HoleType," +
					"BetToCall,F,F,F,CurrBet,Hand,Turn,CurrBet,Hand,River," +
					"CurrBet,Hand,PROFIT,LOSS" +					
					"\n");
		}
		
		
		if (playerID != null)
		{
			if (playerID != "")
			{

				out.write("" + handID);
				
				if (typeOfGame == CASH)
				{
					out.write(",CASH");
				}
				else
				{
					out.write(",TRNY");
				}
				
				
				out.write("," + getPlayerStack(playerID));
				out.write("," + playerID);
				
				out.write("," + bigBlind);

				if (ante != null)
				{
					if (ante.equals(new BigDecimal(0)))
					{
						out.write(",");
					}
					else
					{
						out.write("," + ante.toString());
					}
				}
				else
				{
					out.write(",");
				}
				
				
				if (!hole.equals(""))
				{
					out.write("," + hole);
					if (hole != null)
					{
						if (hole != "")
						{

							out.write("," + getHoleCardHandType(hole));
						}
						else
						{
							out.write(",");
						}
					}
					else
					{
						out.write(",");
					}
				}
				else
				{
					out.write(",,");
				}
				
				
				if (preflopLoss.equals(new BigDecimal(0)))
				{
					out.write(",");
				}
				else
				{
					out.write("," + preflopLoss);
				}
				
				// FLOP HAND
				if (!flop.equals(""))
				{
					out.write("," + flop);
					
					// Cost of the flop (if > 0.0)
					if (flopLoss.equals(new BigDecimal(0)))
					{
						out.write(",");
					}
					else
					{
						out.write("," + flopLoss);
					}
					
					if (flopHand != null){
						out.write("," + flopHand.getHand());
					}
					else
					{
						out.write(",,");
					}
				}
				else 
				{
					out.write(",,,,,");
				}

				
				if (!turn.equals(""))
				{
					out.write("," + turn);
					
					// Cost of turn if > 0
					if (turnLoss.equals(new BigDecimal(0)))
					{
						out.write(",");
					}
					else
					{
						out.write("," + turnLoss);
					}
					
					if (turnHand != null){
						out.write("," + turnHand.getHand());
					}
					else
					{
						out.write(",");
					}
				}
				else 
				{
					out.write(",,,");
				}
				
				if (!river.equals(""))
				{
					out.write("," + river);
					
					// Cost of River
					if (totalBetSoFar.equals(new BigDecimal(0)))
					{
						out.write(",");
					}
					else
					{
						out.write("," + (totalBetSoFar.subtract(turnLoss)));
					}
					
					// Hand made
					if(hand != null)
					{
						out.write("," + hand.getHand());
					}
					else
					{
						out.write(",");
					}
				}
				else 
				{
					out.write(",,,");
				}

				
				
				if (totalWon.equals(new BigDecimal(0)))
				{
					if (!totalBetSoFar.equals(new BigDecimal(0)))
					{
						out.write(",," + (totalBetSoFar.multiply(new BigDecimal(-1))) + "\n");
					}
					else{
						out.write(",,\n");
					}
				}
				else
				{
					out.write("," + totalWon + ",\n");
				}
				

			}
		}
		playerID = "";
		playerStack = new BigDecimal(0);
		bigBlind = new BigDecimal(0);
		ante = new BigDecimal(0);
		BB_temp = "";
		currentBet = new BigDecimal(0);
		totalBetSoFar = new BigDecimal(0);
		preflopLoss = new BigDecimal(0);
		flopLoss = new BigDecimal(0);
		turnLoss = new BigDecimal(0);
		hole = "";
		flop = "";
		turn = "";
		river = "";
		totalWon = new BigDecimal(0);
		flopHand = null;
		turnHand = null;
		hand = null;
		handShown = "";
		stacks.clear();
		communityCards.clear();
	}

	public static void main(String[] args){
		try {
			new BasicAnalysis();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String getPlayerId(){
		return playerID;
	}
	
	public String getFlop(){
		return flop;
	}
	public String getTurn(){
		return turn;
	}
	public String getRiver(){
		return river;
	}
	public String getHandShown(){
		return handShown;
	}
	public FileOutputStream getFos() {
		return fos;
	}

	public void setFos(FileOutputStream fos) {
		this.fos = fos;
	}

	public OutputStreamWriter getOut() {
		return out;
	}

	public void setOut(OutputStreamWriter out) {
		this.out = out;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public List<File> getFs() {
		return fs;
	}

	public void setFs(List<File> fs) {
		this.fs = fs;
	}

	public int getTypeOfGame() {
		return typeOfGame;
	}

	public void setTypeOfGame(int typeOfGame) {
		this.typeOfGame = typeOfGame;
	}

	public Card[] getHoleCards() {
		return holeCards;
	}

	public void setHoleCards(Card[] holeCards) {
		this.holeCards = holeCards;
	}

	public List<Card> getCommunityCards() {
		return communityCards;
	}

	public void setCommunityCards(List<Card> communityCards) {
		this.communityCards = communityCards;
	}

	public BigDecimal getPlayerStack() {
		return playerStack;
	}

	public void setPlayerStack(BigDecimal playerStack) {
		this.playerStack = playerStack;
	}

	public BigDecimal getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(BigDecimal bigBlind) {
		this.bigBlind = bigBlind;
	}

	public String getBB_temp() {
		return BB_temp;
	}

	public void setBB_temp(String bB_temp) {
		BB_temp = bB_temp;
	}

	public BigDecimal getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(BigDecimal currentBet) {
		this.currentBet = currentBet;
	}

	public BigDecimal getTotalBetSoFar() {
		return totalBetSoFar;
	}

	public void setTotalBetSoFar(BigDecimal totalBetSoFar) {
		this.totalBetSoFar = totalBetSoFar;
	}

	public BigDecimal getPreflopLoss() {
		return preflopLoss;
	}

	public void setPreflopLoss(BigDecimal preflopLoss) {
		this.preflopLoss = preflopLoss;
	}

	public BigDecimal getFlopLoss() {
		return flopLoss;
	}

	public void setFlopLoss(BigDecimal flopLoss) {
		this.flopLoss = flopLoss;
	}

	public BigDecimal getTurnLoss() {
		return turnLoss;
	}

	public void setTurnLoss(BigDecimal turnLoss) {
		this.turnLoss = turnLoss;
	}

	public BigDecimal getAnte() {
		return ante;
	}

	public void setAnte(BigDecimal ante) {
		this.ante = ante;
	}

	public String getHole() {
		return hole;
	}

	public void setHole(String hole) {
		this.hole = hole;
	}

	public BigDecimal getTotalWon() {
		return totalWon;
	}

	public void setTotalWon(BigDecimal totalWon) {
		this.totalWon = totalWon;
	}

	public List<BigDecimal> getWins() {
		return wins;
	}

	public void setWins(List<BigDecimal> wins) {
		this.wins = wins;
	}

	public List<BigDecimal> getLosses() {
		return losses;
	}

	public void setLosses(List<BigDecimal> losses) {
		this.losses = losses;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}

	//public Map getGuiHandLocation() {
	//	return guiHandLocation;
	//}

	//public void setGuiHandLocation(Map guiHandLocation) {
	//	this.guiHandLocation = guiHandLocation;
	//}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<BigDecimal> getProfit() {
		return profit;
	}

	public void setProfit(List<BigDecimal> profit) {
		this.profit = profit;
	}

	public List<BigDecimal> getLoss() {
		return loss;
	}

	public void setLoss(List<BigDecimal> loss) {
		this.loss = loss;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public List<String> getStacks() {
		return stacks;
	}

	public void setStacks(List<String> stacks) {
		this.stacks = stacks;
	}

	public Hand getFlopHand() {
		return flopHand;
	}

	public void setFlopHand(Hand flopHand) {
		this.flopHand = flopHand;
	}

	public Hand getTurnHand() {
		return turnHand;
	}

	public void setTurnHand(Hand turnHand) {
		this.turnHand = turnHand;
	}

	public int getHandID() {
		return handID;
	}

	public void setHandID(int handID) {
		this.handID = handID;
	}

	public void setHandShown(String handShown) {
		this.handShown = handShown;
	}	
}