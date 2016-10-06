package com.morphiles.processors;

import com.morphiles.game.Card;
import com.morphiles.game.Player;
import com.morphiles.views.DataTable;
import com.morphiles.models.PokerDataModel;

import java.math.BigDecimal;

/**
 *
 * @author robevans
 */
public class PartyPokerHhProcessorImpl extends HandHistoryProcessor {

    // TODO export this all to a config file, then its easier to change without re-compile later on.
    private static final String PREFLOP_STR = "** Dealing down cards **";
    private static final String FLOP_STR = "** Dealing Flop **";
    private static final String TURN_STR = "** Dealing Turn ** ";
    private static final String RIVER_STR = "** Dealing River ** ";

    private static final  String SHOW1 = " shows [ ";
    private static final String SHOW2 = " doesn't show [ ";
    private static final String WINS = " wins ";

    private static final String FOLD = " folds";
    private static final String CHECK = " checks";
    private static final String CALL = " calls";
    private static final String BETS = " bets ";
    private static final String RAISE = " raises ";
    private static final String ALLIN = " is all-In  ";

    private static final String BIG_BLIND0 = " posts big blind";
    private static final String BIG_BLIND1 = "Blinds(";
    private static final String BIG_BLIND2 = "Stakes(";
    private static final String BIG_BLIND3 = " posts big blind ["; // redundant since if BB0 is found this won't be
    private static final String BIG_BLIND4 = " posts big blind + dead [";
    private static final String BIG_BLIND5 = "Blinds-Antes(";
    private static final String SMALL_BLIND = " posts small blind";

    private static final String DISCON1 = "Connection Lost due to some reason";
    private static final String DISCON2 = "The hand history for this hand number is not available here.";
    private static final String DISCON3 = "Table Closed";

    private static final String TIMEBANK = " will be using his time bank for this hand";

    private static final String LEFT_TABLE = " has left the table.";
    private static final String FINISHED = " finished in ";
    private static final String MOVED_TABLE = " has been moved from table ";
    private static final String JOINED_TABLE = " has joined the table.";

    private static final String SEAT = "Seat";
    private static final String HH_FIRST_LINE_STARTS = "Game #";
    private static final String IS_THE_BUTTON = "is the button";
    private static final String TOTAL_PLAYER_COUNT = "Total number of players : ";
    private static final String TABLE_STR = "Table ";
    private static final String REAL_MONEY = "Real Money";
    private static final String TOURNY = "Trny";
    private static final String RING_GAME = "Ring";

    public PartyPokerHhProcessorImpl(DataTable table, PokerDataModel model){
        super(table, model);
        setSETUP(-1);
        setROUND(PREFLOP);
        setPREFLOP_TXT(PREFLOP_STR);
        setFLOP_TXT(FLOP_STR);
        setTURN_TXT(TURN_STR);
        setRIVER_TXT(RIVER_STR);
        setSHOW1(SHOW1);
        setSHOW2(SHOW2);
        setWINS(WINS);
        setFOLD(FOLD);
        setCHECK(CHECK);
        setCALL(CALL);
        setBETS(BETS);
        setRAISE(RAISE);
        setALLIN(ALLIN);
        setBIG_BLIND0(BIG_BLIND0);
        setBIG_BLIND1(BIG_BLIND1);
        setBIG_BLIND2(BIG_BLIND2);
        setBIG_BLIND3(BIG_BLIND3);
        setBIG_BLIND4(BIG_BLIND4);
        setBIG_BLIND5(BIG_BLIND5);
        setSMALL_BLIND(SMALL_BLIND);
        setDISCON1(DISCON1);
        setDISCON2(DISCON2);
        setDISCON3(DISCON3);
        setTIMEBANK(TIMEBANK);
        setLEFT_TABLE(LEFT_TABLE);
        setFINISHED(FINISHED);
        setMOVED_TABLE(MOVED_TABLE);
        setJOINED_TABLE(JOINED_TABLE);
        setSEAT(SEAT);
        setHH_FIRST_LINE_STARTS(HH_FIRST_LINE_STARTS);
        setIS_THE_BUTTON(IS_THE_BUTTON);
        setTOTAL_PLAYER_COUNT(TOTAL_PLAYER_COUNT);
        setTABLE_TXT(TABLE_STR);
        setREAL_MONEY(REAL_MONEY);
        setTOURNY(TOURNY);
        setRING_GAME(RING_GAME);
    }

    @Override
    public void addData(String data, int handId, String gameId)
    {
        try{
            // SET THE ROUND
            if (data.contains(getHH_FIRST_LINE_STARTS())){
                setROUND(getSETUP());
            }
            else if (data.contains(PREFLOP_TXT)){
                setROUND(PREFLOP);

            }
            else if (data.contains(FLOP_TXT))
            {
                setROUND(FLOP);
            }
            else if (data.contains(TURN_TXT))
            {
                setROUND(TURN);
            }
            else if (data.contains(RIVER_TXT))
            {
                setROUND(RIVER);
            }
            else if (data.contains(getSHOW1()) || data.contains(getSHOW2()))
            {
                setROUND(SHOWDOWN);
            }

            if (data.contains(getHH_FIRST_LINE_STARTS()) || data.contains("\u0000")
                    && !getDisconnected())
            {
                if (players.size() > 0 && !getDisconnected())
                {
                    printData(handId);
                    setPrinted(true);
                }

                getPlayers().removeAll(getPlayers());
                getPlayerIndex().clear();
                setGameType("");
                setTableType("");
                setReal(false);
                setDisconnected(false);
                setWhoAmI("");
                setPrinted(false);

            }
            else if (data.contains(getDISCON1())
                    || data.contains(getDISCON2())
                    || data.contains(getDISCON3()))
            {
                setDisconnected(true);

            }
            else if (data.contains(getSEAT())
                    && data.contains(getIS_THE_BUTTON()))
            {

                setButtonSeat(data);

            }
            else if (data.contains(getTOTAL_PLAYER_COUNT()))
            {
                setTotalPlayerCount(getPlayerCount(data));
                setTotalSeatCount(getSeatCount(data));

                for (int i=0; i<getTotalSeatCount(); i++)
                {
                    getPlayers().add(new Player(i+1));
                }
            }
            else if (data.matches(getSEAT() + ".[0-9]+:.*(.*)")){
                // Get player data and add to array
                String playerId = getPlayerId(data);
                String currency = getCurrency(data);
                BigDecimal  stack    = getStack(data);
                int    seatId   = getSeatId(data);

                // players are put into the players array at index: SeatID-1
                getPlayers().set(seatId-1, new Player(playerId, stack, seatId, currency));



                // also put player indexes into hashtable so they can be accessed by name.
                getPlayerIndex().put(playerId, new Integer(seatId));

                if (getNewJoiners().get(playerId)!=null)
                {
                    getPlayers().get(getPlayerIndex().get(playerId)-1).setJoinedTable(true);
                    getNewJoiners().remove(playerId);
                }
            }
            else if (data.contains("Texas Hold'em "))
            {

                if (data.substring(0,"Texas Hold'em".length()).equals("Texas Hold'em")
                        || data.contains("FL")){
                    setLimitType("FL");
                } else {
                    if (data.contains("NL") || data.contains("No Limit")){
                        setLimitType("NL");
                    } else if (data.contains("PL") || data.contains("Pot Limit")){
                        setLimitType("PL");
                    } else {
                        setLimitType("FL"); // formely setTableType
                    }
                }

                if (data.contains("Buy-in") || data.matches("[$€£][0-9]+")) {
                    String curr = getCurrency(data);
                    if (curr == "USD"){
                        curr = "$";
                    } else if (curr == "EUR") {
                        curr = "€";
                    } else if (curr == "GBP"){
                        curr = "£";
                    }
                    if (curr!=null && curr.length() == 1) {
                        String bi = data.substring(data.indexOf(curr) + 1);
                        setBuyIn(bi.substring(0, bi.indexOf(" ")));
                    }
                } else if (data.contains(getTOURNY()) && !data.contains("Buy-in")){
                    // assume this is a freeroll
                    setBuyIn("0.00");
                }

                if(data.matches(".*[0-9][0-9]:[0-9][0-9]:[0-9][0-9].*")) { //time hh:mm:ss
                    setDateTime(data);
                }
            }
            else if (data.contains(getTABLE_TXT()) && !data.contains("Congratulations to player "))
            {
                //System.out.println(getGameType() + " " + getREAL_MONEY());
                setReal(data.contains(getREAL_MONEY()));

                // If the line after the declaration of the game contains a table '#'
                // then the game is a tourny so we should extract what we can from these
                // two lines. Table ID - Level.

                // reassign gameType once we know whether this is a real money or free game.
                if (getGameType().contains(TOURNY+": ") || data.contains("Freeroll") || data.contains("Table #")) {
                    if (data.contains("Freeroll")){
                        setBuyIn("0.00");
                    }
                    if (data.contains("#")){
                        setTableName(data.substring(6, data.indexOf("(", data.indexOf("#"))));
                    } else {
                        setTableName(data);
                    }
                    setGameType(TOURNY);

                    //Set the buyin to freeroll so that, unless otherwise stated, we know the game had no entry fee.
                    setFreeroll(true);

                    // set the buy in level for the game also
                    if (data.contains("Buy-in")) {
                        setFreeroll(false);
                        String currencySymbol = null;
                        if (data.contains("$")){
                            currencySymbol = "$";
                        } else if (data.contains("£")) {
                            currencySymbol = "£";
                        } else if (data.contains("€")) {
                            currencySymbol = "€";
                        }
                        if (currencySymbol != null) {
                            String buyIn = data.substring(data.indexOf(currencySymbol)+1);
                            buyIn = buyIn.substring(buyIn.indexOf(" ")).replace(",", "").trim();
                            getModel().setStakesLevel(buyIn);
                        }
                    }
                }
                else
                {
                    setTableType(data.substring(6, data.indexOf(" (")));
                    setGameType(RING_GAME);
                }

            }
            else if (data.contains("Dealt to "))
            {
                String player = getPlayerId(data);
                setWhoAmI(player);
                getModel().setPlayerID(player);
                Card[] holeCards = getHoleCards(data);

                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setHoleCards(holeCards);
                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setButtonSeat(getButtonSeat());
                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setTotalPlayerCount(getTotalPlayerCount());
                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setTotalSeatCount(getTotalSeatCount());

            }
            else if (data.contains(getFLOP_TXT()))
            {
                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setCommunityCards(1, data);

            }
            else if (data.contains(getTURN_TXT()))
            {
                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setCommunityCards(2, data);

            }
            else if (data.contains(getRIVER_TXT()))
            {
                getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setCommunityCards(3, data);

            }
            else if (data.contains(getSHOW1()) || data.contains(getSHOW2()))
            {
                String playerId = getPlayerId(data);
                getPlayers().get(getPlayerIndex().get(playerId)-1).setHoleCards(getHoleCards(data));

                //StatsHolder.storeData(playerId, getPlayers().get(getPlayerIndex().get(playerId) - 1).getHoleCardsAsString(), -1, -1, -1, -1);

            }
            else if (data.contains(getSMALL_BLIND()))
            {
                String playerId = getPlayerId(data);
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateContributions(0, getBetAmount(data));
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateActions(0, data);

                getPlayers().get(getPlayerIndex().get(playerId)-1).setSmallBlind(true);
                setSb_amount(getBetAmount(data).toString());
                if (!getModel().isFreeroll()) {
                    setStakesLevel(getBetAmount(data).toString(), "SB");
                }
            }
            else if (data.contains(getBIG_BLIND0()))
            {
                String playerId = getPlayerId(data);
                if (getPlayerIndex().containsKey(playerId)){
                    getPlayers().get(getPlayerIndex().get(playerId)-1).updateContributions(0, getBetAmount(data));
                    getPlayers().get(getPlayerIndex().get(playerId) - 1).updateActions(0, data);
                    getPlayers().get(getPlayerIndex().get(playerId)-1).setBigBlind(true);
                    setBb_amount(getBetAmount(data).toString());
                } else {
                    getNewJoiners().put(playerId, new Boolean(true));
                }
                if (!getModel().isFreeroll()) {
                    setStakesLevel(getBetAmount(data).toString(), "BB");
                }

            }
            else if (data.contains(getBIG_BLIND1()) && !data.contains(" ")) {

            }
            else if((data.contains(getBIG_BLIND1()) && data.contains(" "))
                    || data.contains(getBIG_BLIND2())
                    || data.contains(getBIG_BLIND3()) || data.contains(getBIG_BLIND4())
                    || data.contains(getBIG_BLIND5())){ // USED FOR DEAD BLINDS etc.
                String playerId = getPlayerId(data);
                if (playerId.equals("")){
                    int buttonSeat = super.getButtonSeat();
                    // if button seat is the first player then
                    // the index for the player will be
                    // last player - 1 so set last player to totalPlayerCount index.
                    if (buttonSeat==0){
                        buttonSeat = getTotalPlayerCount();
                    }
                    for (Player playaa : getPlayers()){
                           if(playaa.getSeatId()==buttonSeat){
                               playerId = playaa.getPlayerId();
                           }
                    }
                }
                if (data.contains(getBIG_BLIND1()) || data.contains(getBIG_BLIND2())){
                    String dataSubstring = data.substring(data.indexOf("(")+1);
                    dataSubstring = dataSubstring.substring(0, dataSubstring.indexOf(")"));
                    String[] blinds = dataSubstring.split("/");
                    setSb_amount(blinds[0].replace(",", ""));
                    setBb_amount(blinds[1].replace(",", ""));
                }
                if (data.contains(getBIG_BLIND3()) || data.contains(getBIG_BLIND4())){
                    setBb_amount(getBetAmount(data).toString());
                }
                if (data.contains(getBIG_BLIND5())){
                    String ante = data.substring(data.indexOf("(")+1);
                    ante = ante.substring(ante.indexOf("-")+1);
                    ante = ante.substring(0,ante.length()-1);
                    setAnte(ante);
                }
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateContributions(0, getBetAmount(data));
            }
            else if (getPlayerIndex().get(getPlayerId(data)) != null
                    && (data.contains(getCALL())
                    || data.contains(getBETS())
                    || data.contains(getRAISE())
                    || data.contains(getALLIN())))
            {
                String playerId = getPlayerId(data);
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateContributions(getROUND(), getBetAmount(data));
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateActions(getROUND(), data);

            }
            else if (getPlayerIndex().get(getPlayerId(data)) != null &&
                    (data.contains(FOLD) || data.contains(CHECK)))
            {
                String playerId = getPlayerId(data);
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateActions(getROUND(), data);

            }
            else if (data.contains(getTIMEBANK()))
            {
                String playerId = getPlayerId(data);
                getPlayers().get(getPlayerIndex().get(playerId)-1).updateTimeBank(getROUND());

            }
            else if (data.contains(getWINS()) && !data.contains(":"))
            {
                String playerId = getPlayerId(data);
                //System.out.println(handId +", "+ playerId);
                if (getPlayers().get(getPlayerIndex().get(playerId)-1).getPlayerId() != null &&
                        !getPlayers().get(getPlayerIndex().get(playerId)-1).getPlayerId().equals(""))
                {
                    BigDecimal potWon = getBetAmount(data);
                    getPlayers().get(getPlayerIndex().get(playerId)-1).updateProfitAmount(potWon);
                    getPlayers().get(getPlayerIndex().get(playerId)-1).setWinner(true);
                }
            }
            else if (data.contains(getJOINED_TABLE())
                    || data.contains(getMOVED_TABLE()) )
            {
                String playerId = getPlayerId(data);
                // Signal new joiners at the table
                getNewJoiners().put(playerId, new Boolean(true));
            }
            else if (data.contains(getLEFT_TABLE())
                    || data.contains(getFINISHED()) )
            {

                String playerId = getPlayerId(data);
                if(getPlayerIndex().containsKey(playerId)){
                    getPlayers().get(getPlayerIndex().get(playerId)-1).setLeftTable(true);
                } else if (getNewJoiners().containsKey(playerId)){
                    getNewJoiners().remove(playerId);
                }
            }
            else
            {
                //System.out.println("HandId " + (handId+1) + " Line: "  + data);
            }
        } catch (Exception e){
            e.printStackTrace();
            String msg = "CANNOT PARSE [" + gameId + ", " + handId + ", " + data + "]";
            System.out.println(msg);
            //JOptionPane.showMessageDialog(null, msg, "Line cannot be parsed.", JOptionPane.WARNING_MESSAGE);
        }

    }

    public String getPlayerId(String data)
    {
        //System.out.println(data);
        String temp = "";
        if (data.contains("Dealt to "))
        {
            temp = data.substring(9, data.indexOf("[")-1);
        }
        else if (data.contains("Seat ") && data.contains(":"))
        {
            temp = data.substring(data.indexOf(":")+2, data.indexOf(" ("));
        }
        else if ((data.contains(" posts ") && data.contains("blind")))
        {
            temp = data.substring(0, data.indexOf(" posts"));
        }
        else if(data.contains(getSHOW1()))
        {
            temp = data.substring(0, data.indexOf(getSHOW1()));
        }
        else if (data.contains(getSHOW2()))
        {
            temp = data.substring(0, data.indexOf(getSHOW2()));
        }
        else if (data.contains(getFOLD()))
        {
            temp = data.substring(0, data.indexOf(getFOLD()));
        }
        else if (data.contains(getCHECK()))
        {
            temp = data.substring(0, data.indexOf(getCHECK()));
        }
        else if( data.contains(getCALL()))
        {
            temp = data.substring(0, data.indexOf(getCALL()));
        }
        else if(data.contains(getBETS()))
        {
            temp = data.substring(0, data.indexOf(getBETS()));
        }
        else if (data.contains(getRAISE()))
        {
            temp = data.substring(0, data.indexOf(getRAISE()));
        }
        else if (data.contains(getALLIN()))
        {
            temp = data.substring(0, data.indexOf(getALLIN()));
        }
        else if (data.contains(getWINS()))
        {
            temp = data.substring(0, data.indexOf(getWINS()));
        }
        else if(data.contains(getTIMEBANK()))
        {
            temp = data.substring(0, data.indexOf(getTIMEBANK()));
        }
        else if(data.contains(getLEFT_TABLE()))
        {
            temp = data.substring(0, data.indexOf(getLEFT_TABLE()));
        }
        else if (data.contains(getFINISHED()))
        {
            //System.out.println(data);
            temp = data.substring(data.indexOf(getFINISHED()));
        }
        else if ( data.contains(getMOVED_TABLE()))
        {
            temp = data.substring(0, data.indexOf(getMOVED_TABLE()));
        }

        return temp;
    }

    public int getSeatId(String data)
    {
        String temp = data.substring(5,7).replace(":","");
        return new Integer(temp).intValue();
    }

    public BigDecimal getStack(String data){

        String temp = data.substring(data.indexOf("(")+2, data.indexOf(")")-1).replace(",","");
        if (temp.contains("$") || temp.contains("€") || temp.contains("£"))
        {
            temp = temp.substring(1, temp.indexOf(" "));
        }

        BigDecimal stack = new BigDecimal(temp);
        return stack;
    }

    public int getPlayerCount(String data){
        String temp ="";
        if (data.contains("/")){
            temp = data.substring(data.indexOf(":")+2, data.indexOf("/"));
        } else {
            temp = data.substring(data.indexOf(":")+1).trim();
        }
        return new Integer(temp).intValue();
    }

    public int getSeatCount(String data){
        String temp = "";
        if (data.contains("/")){
            temp = data.substring(data.indexOf("/")+1).replace(" ","");
        } else {
            temp = "10";
        }

        return new Integer(temp).intValue();
    }

    public Card[] getHoleCards(String data)
    {
        String temp = "";
        String card1 = "";
        String card2 = "";

        if (data.contains(getSHOW1()) || data.contains(getSHOW2()))
        {
            temp  = data.substring(data.indexOf(" [ ")+3, data.indexOf("]")-1);
            card1 = temp.substring(0, 2);
            card2 = temp.substring(4);
        }
        else
        {
            temp = data.substring(data.indexOf(" [ ")+4, data.indexOf("]")-1);
            card1 = temp.substring(0, 2);
            card2 = temp.substring(3);
        }

        Card[] holeCards = { new Card(card1), new Card(card2) };
        return holeCards;
    }

    public BigDecimal getBetAmount(String data)
    {
        BigDecimal betAmount = new BigDecimal(0.0);
        String temp = "";
        if (data.contains(getCHECK()) || data.contains(getFOLD()))
        {
            // no-op - TODO - can this be removed - create a test
        }
        else if (data.contains(getBETS()) || data.contains(getRAISE()) ||
                data.contains(getCALL()) || data.contains(getALLIN()) ||
                data.contains(" posts small blind ") || data.contains(" posts big blind "))
        {
            if (getGameType().equals(getRING_GAME()))
            {
                temp = data.substring(data.indexOf("[")+1,
                        data.indexOf("]")).replace(",","").trim();
                if (data.contains("$") || data.contains("£") || data.contains("€")){
                    temp = temp.substring(1);
                    if (temp.contains(" ")){
                        temp = temp.substring(0, temp.indexOf(" "));
                    }
                }
            }
            else
            {
                temp = data.substring(data.indexOf("[")+1,
                        data.indexOf("]")).replace(",","").trim();
                if (data.contains("$") || data.contains("£") || data.contains("€")){
                    temp = temp.substring(1);
                    if (temp.contains(" ")) {
                        temp = temp.substring(0, temp.indexOf(" "));
                    }
                }
            }
            betAmount = new BigDecimal(temp);
        } else if (data.contains(getWINS()) && ! data.contains(":")) {
            int start = data.indexOf(getWINS()) + getWINS().length()-1;
            String[] dataArray = data.split(" ");
            int i = 0;
            for (String datum : dataArray) {
                if (datum.equals(getWINS())) {
                    break;
                }
                i++;
            }
            if (dataArray[i+1].equals("chips")) {
//                start += 2;
            }

            temp = temp.substring(0, temp.indexOf(" "));
            betAmount = new BigDecimal(temp);
        }

        return betAmount;
    }

    public void setButtonSeat(String data)
    {
        String temp = data.substring(5,7).trim();
        super.setButtonSeat(new Integer(temp));
    }

    public void printLastHand(int handId){
        if(!isPrinted())
        {
            printData(handId);
        }
    }
}
