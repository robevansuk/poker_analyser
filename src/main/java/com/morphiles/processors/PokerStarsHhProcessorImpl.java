package com.morphiles.processors;

import com.morphiles.game.Card;
import com.morphiles.game.Hand;
import com.morphiles.game.Player;
import com.morphiles.views.DataTable;
import com.morphiles.models.PokerDataModel;

import java.math.BigDecimal;

/**
 * Copyright (c) 2002-2013 morphiles.com, L.P. All rights reserved.
 *
 * @author robevans
 */
public class PokerStarsHhProcessorImpl extends HandHistoryProcessor {

    private int totalPlayerCount;
    private static final String PREFLOP_STR = "*** HOLE CARDS ***";
    private static final String FLOP_STR = "*** FLOP *** [";
    private static final String TURN_STR = "*** TURN *** [";
    private static final String RIVER_STR = "*** RIVER *** [";

    private static final  String SHOW1 = " showed [";
    private static final String SHOW2 = " mucked [";
    private static final String SHOW3 = " shows [";
    private static final String WINS = " collected ";

    private static final String FOLD = ": folds";
    private static final String CHECK = ": checks";
    private static final String CALL = ": calls ";
    private static final String BETS = ": bets ";
    private static final String RAISE = ": raises ";
    private static final String ALLIN = " all-in";

    private static final String BIG_BLIND0 = ": posts big blind ";
    private static final String BIG_BLIND1 = " - Level ";
    private static final String BIG_BLIND2 = ": posts the ante ";
    //private static final String BIG_BLIND3 = " posts big blind [";
    //private static final String BIG_BLIND4 = " posts big blind + dead [";
//    private static final String BIG_BLIND5 = ": posts the ante ";
    private static final String SMALL_BLIND = ": posts small blind ";

    private static final String DISCON1 = " has timed out while being disconnected";
//    private static final String DISCON2 = "The hand history for this hand number is not available here.";
//    private static final String DISCON3 = "Table Closed";

    private static final String TIMEBANK = " will be using his time bank for this hand";

    private static final String LEFT_TABLE = " is sitting out";
    private static final String FINISHED = " finished the tournament in ";
    private static final String MOVED_TABLE = "(moved from another table";
    private static final String JOINED_TABLE = " has joined the table.";

    private static final String SEAT = "Seat";
    private static final String HH_FIRST_LINE_STARTS = "*********** # ";
    private static final String IS_THE_BUTTON = "is the button";
    private static final String TOTAL_PLAYER_COUNT = "-max ";
    private static final String TABLE_STR = "Table ";
    private static final String REAL_MONEY = "$";
    private static final String TOURNY = " Tournament ";

    public PokerStarsHhProcessorImpl(DataTable table, PokerDataModel model){
        super(table, model);
        setSETUP(-1);
        setROUND(HandHistoryProcessor.PREFLOP);
        setPREFLOP_TXT(PREFLOP_STR);
        setFLOP_TXT(FLOP_STR);
        setTURN_TXT(TURN_STR);
        setRIVER_TXT(RIVER_STR);
        setSHOW1(SHOW1);
        setSHOW2(SHOW2);
        setSHOW3(SHOW3);
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
//        setBIG_BLIND3(BIG_BLIND3);
//        setBIG_BLIND4(BIG_BLIND4);
//        setBIG_BLIND5(BIG_BLIND5);
        setSMALL_BLIND(SMALL_BLIND);
        setDISCON1(DISCON1);
//        setDISCON2(DISCON2);
//        setDISCON3(DISCON3);
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
//        setRING_GAME(RING_GAME);
    }

    @Override
    public void addData(String data, int handId, String gameId)
    {
        // SET THE ROUND
        if (data.contains("\u0000")||data.contains(getHH_FIRST_LINE_STARTS()))
        {
            setROUND(getSETUP());
        }
        else if (data.contains(PREFLOP_TXT))
        {
            setROUND(HandHistoryProcessor.PREFLOP);

        }
        else if (data.contains(FLOP_TXT))
        {
            setROUND(HandHistoryProcessor.FLOP);
        }
        else if (data.contains(TURN_TXT))
        {
            setROUND(HandHistoryProcessor.TURN);
        }
        else if (data.contains(RIVER_TXT))
        {
            setROUND(HandHistoryProcessor.RIVER);
        }
        else if (data.contains(getSHOW1()) || data.contains(getSHOW2()))
        {
            setROUND(HandHistoryProcessor.SHOWDOWN);
        }

        if (data.contains(getHH_FIRST_LINE_STARTS()))//||data.contains("Game #"))
        {
            if (getPlayers().size() > 0)
            {
                printData(handId);
            }

            getPlayers().removeAll(getPlayers());
            getPlayerIndex().clear();
            setGameType("");
            setTableType("");
            setReal(false);
            setDisconnected(false);
            setWhoAmI("");
            totalPlayerCount = 0;

        }
        else if (data.contains(getDISCON1()))
        {
            setDisconnected(true);

        }
        else if (data.contains(getSEAT())
                && data.contains(getIS_THE_BUTTON()))
        {

            setButtonSeat(data);

            //setTotalPlayerCount(getPlayerCount(data));

            setSeatCount(data);
            for (int i=0; i<getTotalSeatCount(); i++)
            {
                getPlayers().add(new Player(i+1));
            }
        }
        else if (data.contains(getSEAT())
                && data.contains(":")
                && data.contains("(")
                && data.contains(")")
                && data.contains(" in chips"))
        {
            // Get player data and add to array
            String playerId = getPlayerId(data);
            String currency = getCurrency(data);
            Float  stack    = getStack(data);
            int    seatId   = getSeatId(data);

            totalPlayerCount++;

            // players are put into the players array at index: SeatID-1
            getPlayers().set(seatId-1, new Player(playerId, new BigDecimal(stack), seatId, currency));


            // also put player indexes into hashtable so they can be accessed by name.
            getPlayerIndex().put(playerId, new Integer(seatId));

            if (getNewJoiners().get(playerId)!=null)
            {
                getPlayers().get(getPlayerIndex().get(playerId)-1).setJoinedTable(true);
                getNewJoiners().remove(playerId);
            }
        }
        else if (data.contains(" Hold'em "))
        {
            setGameType(data);

        }
        else if (data.contains(getTABLE_TXT()))
        {
            if (getGameType().contains(getREAL_MONEY())
                    ||   data.contains(getREAL_MONEY()))
            {
                setReal(true);
            }
            else
            {
                setReal(false);
            }
            // If the line after the declaration of the game contains a table '#'
            // then the game is a tourny so we should extract what we can from these
            // two lines. Table ID - Level.

            // reassign gameType once we know whether this is a real money or free game.
            if (getGameType().contains(getTOURNY()+": "))
            {
                setTableType(data.substring(6, data.indexOf("(", data.indexOf("#"))));
                setGameType(getTOURNY());
            }

        }
        else if (data.contains("Dealt to"))
        {
            String player = getPlayerId(data);
            setWhoAmI(player);
            getModel().setPlayerID(player);
            Card[] holeCards = getHoleCards(data);

            getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setHoleCards(holeCards);
            getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setButtonSeat(getButtonSeat());
            getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setTotalPlayerCount(getTotalPlayerCount());
            getPlayers().get(getPlayerIndex().get(getWhoAmI())-1).setTotalSeatCount(getTotalSeatCount());



            //StatsHolder.storeData(player, holeCards[0].toString() + " " + holeCards[1].toString(), -1, -1, -1, -1);

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
        else if (data.contains(getSHOW1()) || data.contains(getSHOW2())
                || data.contains(getSHOW3()))
        {
            String playerId = getPlayerId(data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).setHoleCards(getHoleCards(data));

//            StatsHolder.storeData(playerId, getPlayers().get(getPlayerIndex().get(playerId) - 1).getHoleCardsAsString(), -1, -1, -1, -1);

        }
        else if (data.contains(getSMALL_BLIND()))
        {
            setTotalPlayerCount(totalPlayerCount);

            String playerId = getPlayerId(data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).updateContributions(0, getBetAmount(data));
            getPlayers().get(getPlayerIndex().get(playerId)-1).updateActions(0, data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).setSmallBlind(true);
            setStakesLevel(getBetAmount(data).toString(), "SB");
        }
        else if (data.contains(getBIG_BLIND0()))
        {
            String playerId = getPlayerId(data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).updateContributions(0, getBetAmount(data));
            getPlayers().get(getPlayerIndex().get(playerId) - 1).updateActions(0, data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).setBigBlind(true);
            setStakesLevel(getBetAmount(data).toString(), "BB");
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
                (data.contains(getFOLD())
                        || data.contains(getCHECK())))
        {
            String playerId = getPlayerId(data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).updateActions(getROUND(), data);

        }
        else if (data.contains(getTIMEBANK()))
        {
            String playerId = getPlayerId(data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).updateTimeBank(getROUND());

        }
        else if (data.contains(getWINS()))
        {
            String playerId = getPlayerId(data);
            if (getPlayers().get(getPlayerIndex().get(playerId)-1).getPlayerId() != null &&
                    !getPlayers().get(getPlayerIndex().get(playerId)-1).getPlayerId().equals(""))
            {
                //data.indexOf(getWINS()) + getWINS().length();
                //data.indexOf(" ", start);

                String temp = data.substring(data.indexOf(getWINS())+getWINS().length()).replace(",", "").replace("(","").replace(")","");
                if(temp.contains(" ")){
                    temp = temp.substring(0, temp.indexOf(" "));
                }

                BigDecimal potWon = new BigDecimal(temp);
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
            setDisconnected(true);
            String playerId = getPlayerId(data);
            getPlayers().get(getPlayerIndex().get(playerId)-1).setLeftTable(true);
        }
        else
        {
            //System.out.println("HandId " + (handId+1) + " Line: "  + data);
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
        else if (data.contains(getWINS()))
        {
            if (data.contains("Seat") && data.contains(":")){
                temp = data.substring(data.indexOf(":")+2);
                temp = temp.substring(0, temp.indexOf(getWINS()));
            }
            else if (data.contains(getWINS()))
            {
                temp = data.substring(0, data.indexOf(getWINS()));
            }
            else
            {
                temp = data.substring(data.indexOf(":")+1);
                temp = temp.substring(0, temp.indexOf(" "));
            }

            if (temp.contains(" (small blind)"))
            {
                temp = temp.substring(0, temp.indexOf(" (small blind)"));
            }
            else if (temp.contains(" (big blind)"))
            {
                temp = temp.substring(0, temp.indexOf(" (big blind)"));
            }
            else if (temp.contains(" (button)"))
            {
                temp = temp.substring(0, temp.indexOf(" (button)"));
            }

        }

        else if(data.contains(getSHOW3()))
        {
            temp = data.substring(0,data.indexOf(":"));
        }
        else if (data.contains(getSHOW1()) || data.contains(getSHOW2()))
        {
            temp = data.substring(data.indexOf(":")+2);
            temp = temp.substring(0, temp.indexOf(" ["));
            if(temp.contains(" (big blind)"))
            {
                temp = temp.substring(0,temp.indexOf(" ("));
            }
            else if(temp.contains(" (small blind)"))
            {
                temp = temp.substring(0,temp.indexOf(" ("));
            }
            else if (temp.contains(" (button)"))
            {
                temp = temp.substring(0,temp.indexOf(" (button)"));
            }
            else if (temp.contains(" showed"))
            {
                temp = temp.substring(0,temp.indexOf(" showed"));
            }
            else if (temp.contains(" mucked"))
            {
                temp = temp.substring(0,temp.indexOf(" mucked"));
            }
        }
        else if (data.contains("Seat ") && data.contains(":") && data.contains(" in chips"))
        {
            temp = data.substring(data.indexOf(":")+2);
            temp = temp.substring(0, temp.indexOf(" ("));
        }
        else if((data.contains(" posts ") && (data.contains("blind")||data.contains("ante")))
                || data.contains(getFOLD()) || data.contains(getCHECK())
                || data.contains(getCALL()) || data.contains(getFOLD())
                || data.contains(getBETS()) || data.contains(getRAISE())
                || data.contains(getALLIN()) || data.contains(getSHOW3()))
        {
            temp = data.substring(0, data.indexOf(":"));
        }
        else if (data.contains(getFINISHED()))
        {
            temp = data.substring(0, data.indexOf(getFINISHED()));
        }
        else if(data.contains(getLEFT_TABLE()))
        {
            temp = data.substring(0, data.indexOf(getLEFT_TABLE()));
        }
        else if(data.contains(getTIMEBANK()))
        {
            temp = data.substring(0, data.indexOf(getTIMEBANK()));
        }
        else if (data.contains(getMOVED_TABLE()))
        {
            temp = data.substring(data.indexOf(" ")+1, data.indexOf(" ", 8));
        }

        //System.out.println("PLAYERID :" + temp + ":" + data );

        return temp;
    }

    public int getSeatId(String data)
    {
        String temp = data.substring(5,7).replace(":","");
        return new Integer(temp).intValue();
    }

    public float getStack(String data){

        String temp = data.substring(data.indexOf("(")+1, data.indexOf(" in chips)")).replace(",","");
        if (temp.contains("$") || temp.contains("€") || temp.contains("£"))
        {
            temp = temp.substring(1, temp.indexOf(" "));
        }

        float stack = new Float(temp).floatValue();
        return stack;
    }

    public int getPlayerCount(String data){
        String temp = data.substring(data.indexOf(getTOTAL_PLAYER_COUNT())-2, data.indexOf(getTOTAL_PLAYER_COUNT())).trim();
        return new Integer(temp).intValue();
    }


    public void setSeatCount(String data){
        String temp = data.substring(data.indexOf(getTOTAL_PLAYER_COUNT())-2, data.indexOf(getTOTAL_PLAYER_COUNT())).trim();
        setTotalSeatCount(new Integer(temp));
    }

    public String getMoney(float value)
    {
        // Print stack with relevant ".00" or extra 0.
        String stackString = value + "";


        if (stackString.contains(".") && stackString != "0.00") {
            stackString = stackString.substring(0,stackString.indexOf("."));
        }

        if (stackString.equals("0") || stackString.equals("0.0") || stackString.equals("0.00")){
            stackString="";
        }
        return stackString;
    }

    public Card[] getHoleCards(String data)
    {
        String temp = "";
        String card1 = "";
        String card2 = "";

//        if (data.contains(getSHOW2()) || data.contains(getSHOW3()))
//        {
//            temp  = data.substring(data.indexOf(" [")+2, data.indexOf("]"));
//            card1 = temp.substring(0, 2);
//            card2 = temp.substring(3);
//        }
//        else
//        {
            temp = data.substring(data.indexOf(" [")+2, data.indexOf("]"));
            card1 = temp.substring(0, 2);
            card2 = temp.substring(3);
//        }

        Card[] holeCards = { new Card(card1), new Card(card2) };
        return holeCards;
    }

    public BigDecimal getBetAmount(String data)
    {
        BigDecimal betAmount = new BigDecimal(0.0);
        String temp = "";
        if (data.contains(getCHECK()) || data.contains(getFOLD()))
        {
            betAmount = new BigDecimal(0.0);
        }
        else if (data.contains(getALLIN()))
        {
            if(data.contains(getRAISE())){
                temp = data.substring(0, data.indexOf(" and is all-in"));
                temp = temp.substring(0, temp.lastIndexOf(" "));
                temp = temp.substring(0, temp.lastIndexOf(" "));
                temp = temp.substring(temp.lastIndexOf(" "));
            } else {
                temp = data.substring(0, data.indexOf(" and is all-in"));
                temp = temp.substring(temp.lastIndexOf(" ")+1);
            }
            betAmount = new BigDecimal(temp);
        }
        else if (data.contains(getBETS())
               || data.contains(getRAISE()) || data.contains(getCALL())
               || data.contains(getSMALL_BLIND()) || data.contains(getBIG_BLIND0())
               || data.contains(getBIG_BLIND1()) || data.contains(getBIG_BLIND2()))
        {
            temp = data.substring(data.lastIndexOf(" ")+1).replace(",","").trim();
            betAmount = new BigDecimal(temp);
        }

        return betAmount;
    }

    public void setButtonSeat(String data)
    {
        String temp = data.substring(data.indexOf("#")+1, data.indexOf("#")+3).trim();
        setButtonSeat(new Integer(temp));
    }


    public void printData(int handId){
        int maxRow = getTable().getRowCount();

        // need a counter that is independent to 'i'
        int k = 0;




        for (int i=0; i<getPlayers().size(); i++)
        {
            if (getPlayers().get(i).getActions(0).equals(""))
            {
                if (!getPlayers().get(i).getPlayerId().equals(""))
                {
                    //System.out.println(" no hand action");
                    Player.updatePlayerCount(0);
                }
            }
        }

        for (int i=0; i<getPlayers().size(); i++)
        {
            if (!getPlayers().get(i).getLeftTable().equals("Left") && getPlayers().get(i).getPlayerId() != ""){
                int rowOut = maxRow + k;
                // Print hand data that is the same for every player
                getModel().setValueAt((handId) + "", rowOut, getColumns().get("Id"));



                getModel().setValueAt(getMoney(getPlayers().get(i).getContributions(0)), rowOut, getColumns().get("Pf Contribution"));
                getModel().setValueAt(getPlayers().get(i).getActions(0), rowOut, getColumns().get("Pf Mv"));
                getModel().setValueAt(getMoney(getPlayers().get(i).getTotalPot(0)) + "", rowOut, getColumns().get("Pf Pot"));
                getModel().setValueAt(getPlayers().get(i).getPlayerCountForRound(0) + "", rowOut, getColumns().get("Pf Plyr Cnt"));

                if (getPlayers().get(i).getHoleCards()!=null && getPlayers().get(i).getHoleCards().length!=0){
                    getModel().setValueAt(Hand.getPreflopHandType(getPlayers().get(i).getHoleCards()) + "", rowOut, getColumns().get("Pf Hand"));
                }

                // FLOP
                if (!getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCommunityCards(1).equals("")){
                    String prefix = "F ";
                    getModel().setValueAt(getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCommunityCards(1), rowOut, getColumns().get("Flop"));
                    getModel().setValueAt(getMoney(getPlayers().get(i).getContributions(1)), rowOut, getColumns().get(prefix + "Contribution"));
                    getModel().setValueAt(getPlayers().get(i).getActions(1), rowOut, getColumns().get(prefix + "Mv"));
                    getModel().setValueAt(getMoney(getPlayers().get(i).getTotalPot(1)) + "", rowOut, getColumns().get(prefix + "Pot"));
                    getModel().setValueAt(getPlayers().get(i).getPlayerCountForRound(1) + "", rowOut, getColumns().get(prefix + "Plyr Cnt"));
                    if (!getPlayers().get(i).getHoleCardsAsString().equals("")){
//                        getModel().setValueAt(getHand(getPlayers().get(i).getHoleCards(),
//                                getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCardsUpto(1)), rowOut, getColumns().get(prefix + "Hand"));
                    }
                }
                //TURN
                if (!getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCommunityCards(2).equals("")){
                    String prefix = "T ";
                    getModel().setValueAt(getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCommunityCards(2), rowOut, getColumns().get("Turn"));
                    getModel().setValueAt(getMoney(getPlayers().get(i).getContributions(2)), rowOut, getColumns().get(prefix + "Contribution"));
                    getModel().setValueAt(getPlayers().get(i).getActions(2), rowOut, getColumns().get(prefix + "Mv"));
                    getModel().setValueAt(getMoney(getPlayers().get(i).getTotalPot(2)) + "", rowOut, getColumns().get(prefix + "Pot"));
                    getModel().setValueAt(getPlayers().get(i).getPlayerCountForRound(2) + "", rowOut, getColumns().get(prefix + "Plyr Cnt"));
                    if (!getPlayers().get(i).getHoleCardsAsString().equals("")){
//                        getModel().setValueAt(getHand(getPlayers().get(i).getHoleCards(),
//                                getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCardsUpto(2)), rowOut, getColumns().get(prefix + "Hand"));
                    }
                }
                // RIVER
                if (!getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCommunityCards(3).equals("")){
                    String prefix = "R ";
                    getModel().setValueAt(getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCommunityCards(3), rowOut, getColumns().get("River"));
                    getModel().setValueAt(getMoney(getPlayers().get(i).getContributions(3)), rowOut, getColumns().get(prefix + "Contribution"));
                    getModel().setValueAt(getPlayers().get(i).getActions(3), rowOut, getColumns().get(prefix + "Mv"));
                    getModel().setValueAt(getMoney(getPlayers().get(i).getTotalPot(3)) + "", rowOut, getColumns().get(prefix + "Pot"));
                    getModel().setValueAt(getPlayers().get(i).getPlayerCountForRound(3) + "", rowOut, getColumns().get(prefix + "Plyr Cnt"));
                    if (!getPlayers().get(i).getHoleCardsAsString().equals("")){
//                        getModel().setValueAt(getHand(getPlayers().get(i).getHoleCards(),
//                                getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCardsUpto(3)), rowOut, getColumns().get(prefix + "Hand"));
                    }
                }
                //  Print currency in relevant positions
                // 0, 1, 2, 3 - represent preflop, flop, turn river

                getModel().setValueAt(getPlayers().get(i).getTablePlayerCount() + "", rowOut, getColumns().get("Total Plyr Cnt"));
                getModel().setValueAt(getPlayers().get(i).getSeatPosition(k + 1), rowOut, getColumns().get("Position"));
                getModel().setValueAt((isReal() ? "Real" : "Play"), rowOut, getColumns().get("Real-Play"));
                getModel().setValueAt(getGameType(), rowOut, getColumns().get("Gm Type"));
                getModel().setValueAt(getTableType(), rowOut, getColumns().get("Tbl Id"));

                // Print player in relevant positions
                getModel().setValueAt(getPlayers().get(i).getPlayerId(), rowOut, getColumns().get("Player"));

                // Print seat ID in relevant positions
                getModel().setValueAt((k + 1) + "", rowOut, getColumns().get("Seat"));

                getModel().setValueAt(getMoney(getPlayers().get(i).getStack()), rowOut, getColumns().get("Stack"));

                //  Print currency in relevant positions
                getModel().setValueAt(getPlayers().get(i).getCurrency(), rowOut, getColumns().get("Currency"));

                //  Print currency in relevant positions
                getModel().setValueAt(getPlayers().get(i).getHoleCardsAsString(), rowOut, getColumns().get("Cards"));

                // Print the total of the pot won. (from all pots/side pots etc)
                getModel().setValueAt(getMoney(getPlayers().get(i).getProfit()), rowOut, getColumns().get("Profit"));

                // Print out the Winner.
                getModel().setValueAt(getPlayers().get(i).getWinner() + "", rowOut, getColumns().get("Win"));

                // Print out whether the player just joined or left the table
                getModel().setValueAt(getPlayers().get(i).getLeftTable() + "", rowOut, getColumns().get("Joined"));



                // increment the k value only when a player is sitting in the seat
                k += 1;
            }
        }
    }
}
