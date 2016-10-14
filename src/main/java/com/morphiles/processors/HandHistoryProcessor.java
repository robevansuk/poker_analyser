package com.morphiles.processors;

import com.morphiles.game.Card;
import com.morphiles.game.Hand;
import com.morphiles.game.Player;
import com.morphiles.views.DataTable;
import com.morphiles.models.PokerDataModel;

import javax.swing.table.TableColumnModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Copyright (c) 2002-2013 morphiles.com, L.P. All rights reserved.
 *
 * This class will act as the parent class for any hand history
 * processors.
 *
 * @author robevans
 * @date 16/06/2013
 */
public abstract class HandHistoryProcessor {


    private Hashtable<String, Integer> columns = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> playerIndex = new Hashtable<String, Integer>();
    private Hashtable<String, Boolean> newJoiners = new Hashtable<String, Boolean>();

    private static int ROUND;
    private int SETUP = -1;

    protected final static int PREFLOP = 0;
    protected final static int FLOP = 1;
    protected final static int TURN = 2;
    protected final static int RIVER = 3;
    protected final static int SHOWDOWN = 4;

    protected ArrayList<Player> players = new ArrayList<>();

    private String gameType; //
    private boolean isReal; // is real money
    private String tableType; // same as game type?
    private String limitType;
    private String tableName;
    private boolean disconnected;
    private String whoAmI;
    private int buttonSeat;
    private int totalSeatCount;
    private int totalPlayerCount;

    private String bb_amount;
    private String sb_amount;
    private String ante;

    private String time;
    private String date;

    public String PREFLOP_TXT;
    public String FLOP_TXT;
    public String TURN_TXT;
    public String RIVER_TXT;

    private String SHOW1;
    private String SHOW2;
    private String SHOW3;
    private String WINS;

    private String BETS;
    private String RAISE;
    private String FOLD;
    private String CHECK;
    private String CALL;
    private String ALLIN;

    private String SIDE_POT;
    private String MAIN_POT;

    private String BIG_BLIND0;
    private String BIG_BLIND1;
    private String BIG_BLIND2;
    private String BIG_BLIND3;
    private String BIG_BLIND4;
    private String BIG_BLIND5;
    private String SMALL_BLIND;

    private String DISCON1;
    private String DISCON2;
    private String DISCON3;

    private String TIMEBANK;

    private String LEFT_TABLE;
    private String FINISHED;
    private String MOVED_TABLE;
    private String JOINED_TABLE;
    private String SEAT;
    private String HH_FIRST_LINE_STARTS;
    private String IS_THE_BUTTON;
    private String TOTAL_PLAYER_COUNT;
    private String TABLE_TXT;
    private String REAL_MONEY;
    private String TOURNY;
    private String RING_GAME;

    private DataTable dataTable;
    private PokerDataModel model;

    private String buyIn;

    private boolean printed;


    // abstract methods all subclasses must implement
    public abstract void addData(String data, int handId, String gameId);


    public HandHistoryProcessor(DataTable dataTable){
        // should call the constructor below.
        setDataTable(dataTable);
        setModel(new PokerDataModel());
        setColumns();

        printed=false;
    }

    public HandHistoryProcessor(DataTable table, PokerDataModel model){
        setDataTable(table);
        setModel(model);
        setColumns();
    }

    public void printLastHandId(int handId){
        if (!isPrinted()){
            printData(handId);
        }
    }

    public boolean isPrinted(){
        return printed;
    }

    public void setPrinted(boolean printed){
        this.printed = printed;
    }

    // override if necessary but this should suffice
    public String getCurrency(String data)
    {
        String currency = "";
        if (data.contains("$")){
            currency = "USD";
        } else if (data.contains("€")) {
            currency = "EUR";
        } else if (data.contains("£")) {
            currency = "GBP";
        } else {
            currency = "chips";
        }
        return currency;
    }

    public void printLastHand(int handId){
        printData(handId);
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
            if (getPlayers().get(i).getPlayerId() != ""){
                int rowOut = maxRow + k;
                // Print hand data that is the same for every player
                String hId;
                if(handId<10){
                    hId = "00" + handId;
                } else if (handId < 100) {
                    hId = "0" + handId;
                } else {
                    hId = handId + "";
                }
                getModel().setValueAt((hId) + "", rowOut, getColumns().get("Id"));



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
                        Hand hand = getHand(getPlayers().get(i).getHoleCards(), getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCardsUpto(1));
                        getModel().setValueAt(hand.getHand(), rowOut, getColumns().get(prefix + "Hand"));
                        getModel().setValueAt(hand.calculateOuts()+"", rowOut, getColumns().get(prefix + "Outs"));
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
                        Hand hand = getHand(getPlayers().get(i).getHoleCards(), getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCardsUpto(2));
                        getModel().setValueAt(hand.getHand(), rowOut, getColumns().get(prefix + "Hand"));
                        getModel().setValueAt(hand.calculateOuts()+"", rowOut, getColumns().get(prefix + "Outs"));
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
                        Hand hand = getHand(getPlayers().get(i).getHoleCards(), getPlayers().get(getPlayerIndex().get(getWhoAmI()) - 1).getCardsUpto(3));
                        getModel().setValueAt(hand.getHand(), rowOut, getColumns().get(prefix + "Hand"));
                    }
                }
                //  Print currency in relevant positions
                // 0, 1, 2, 3 - represent preflop, flop, turn river

                getModel().setValueAt(getPlayers().get(i).getTablePlayerCount() + "", rowOut, getColumns().get("Total Plyr Cnt"));
                getModel().setValueAt(getPlayers().get(i).getSeatPosition(k + 1), rowOut, getColumns().get("Position"));
                getModel().setValueAt((isReal() ? "Real" : "Play"), rowOut, getColumns().get("Real-Play"));
                getModel().setValueAt(getGameType(), rowOut, getColumns().get("Gm Type"));
                getModel().setValueAt(getTableName(), rowOut, getColumns().get("Tbl Id"));
                getModel().setValueAt(getLimitType(), rowOut, getColumns().get("LimitType"));
                getModel().setValueAt(getTime(), rowOut, getColumns().get("Time"));
                getModel().setValueAt(getDate(), rowOut, getColumns().get("Date"));
                getModel().setValueAt(getSmallBlindAmount(), rowOut, getColumns().get("SB"));
                getModel().setValueAt(getBigBlindAmount(), rowOut, getColumns().get("BB"));
                getModel().setValueAt(getAnte(), rowOut, getColumns().get("Ante"));
                getModel().setValueAt(getBuyIn(), rowOut, getColumns().get("BuyIn"));


                // Print player in relevant positions
                getModel().setValueAt(getPlayers().get(i).getPlayerId(), rowOut, getColumns().get("Player"));

                // Print seat ID in relevant positions
                getModel().setValueAt((k + 1) + "", rowOut, getColumns().get("Seat"));

                getModel().setValueAt(getMoney(getPlayers().get(i).getStack()), rowOut, getColumns().get("Stack"));

                //  Print currency in relevant positions
                getModel().setValueAt(getPlayers().get(i).currencyType(), rowOut, getColumns().get("Currency"));

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

    // TODO should be BigDecimal data type for the monetary value passed in here.
    public String getMoney(BigDecimal value)
    {
        // Print stack with relevant ".00" or extra 0.
        String stackString = value.toString();

        if (stackString.indexOf(".")>0 && getGameType().equals(getRING_GAME()))
        {
            if (stackString.length()-stackString.indexOf(".") == 2)
            {
                stackString = stackString + "0";
            }
        } else if (getGameType().equals(getRING_GAME())){
            stackString = stackString + ".00";
        }
        if (getGameType().equals(getRING_GAME())) {
            stackString = stackString.substring(0, stackString.indexOf(".")+3);
        } else {
            if (stackString.contains(".") && stackString != "0.00") {
                stackString = stackString.substring(0,stackString.indexOf("."));
            }
        }
        if (stackString.equals("0") || stackString.equals("0.0") || stackString.equals("0.00")){
            stackString="";
        }
        return stackString;
    }

    public Hand getHand(Card[] hole, Card[] community)
    {
        Hand handType = new Hand(hole, community);
        return handType;
    }

    /**
     * GETTERS
     */

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public DataTable getTable(){
        return dataTable;
    }

    public PokerDataModel getModel() {
        return model;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getPREFLOP_TXT() {
        return PREFLOP_TXT;
    }

    public String getFLOP_TXT() {
        return FLOP_TXT;
    }

    public String getTURN_TXT() {
        return TURN_TXT;
    }

    public String getRIVER_TXT() {
        return RIVER_TXT;
    }

    public boolean getDisconnected() {
        return disconnected;
    }


    public void setReal(boolean real) {
        isReal = real;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public void setWhoAmI(String whoAmI) {
        this.whoAmI = whoAmI;
    }

    public void setButtonSeat(int buttonSeat) {
        this.buttonSeat = buttonSeat;
    }

    public void setTotalPlayerCount(int totalPlayerCount) {
        this.totalPlayerCount = totalPlayerCount;
    }

    public void setTotalSeatCount(int totalSeatCount) {
        this.totalSeatCount = totalSeatCount;
    }

    public Hashtable<String, Integer> getColumns() {
        return columns;
    }

    public Hashtable<String, Integer> getPlayerIndex() {
        return playerIndex;
    }

    public Hashtable<String, Boolean> getNewJoiners() {
        return newJoiners;
    }

    public static int getROUND() {
        return ROUND;
    }

    /**
     * SETTERS
     */

    public void setModel(PokerDataModel model) {
        this.model = model;
    }

    public void setColumns(Hashtable<String, Integer> columns) {
        this.columns = columns;
    }

    public void setPlayerIndex(Hashtable<String, Integer> playerIndex) {
        this.playerIndex = playerIndex;
    }

    public void setNewJoiners(Hashtable<String, Boolean> newJoiners) {
        this.newJoiners = newJoiners;
    }

    public static void setROUND(int ROUND) {
        HandHistoryProcessor.ROUND = ROUND;
    }

    public void setPREFLOP_TXT(String PREFLOP_TXT) {
        this.PREFLOP_TXT = PREFLOP_TXT;
    }

    public void setFLOP_TXT(String FLOP_TXT) {
        this.FLOP_TXT = FLOP_TXT;
    }

    public void setTURN_TXT(String TURN_TXT) {
        this.TURN_TXT = TURN_TXT;
    }

    public void setRIVER_TXT(String RIVER_TXT) {
        this.RIVER_TXT = RIVER_TXT;
    }

    /**
     * This lot below is all the getters and setters for everything
     */

    public String getSHOW1() {
        return SHOW1;
    }

    public void setSHOW1(String SHOW1) {
        this.SHOW1 = SHOW1;
    }

    public String getSHOW2() {
        return SHOW2;
    }

    public void setSHOW2(String SHOW2) {
        this.SHOW2 = SHOW2;
    }

    public String getWINS() {
        return WINS;
    }

    public void setWINS(String WINS) {
        this.WINS = WINS;
    }

    public String getBETS() {
        return BETS;
    }

    public void setBETS(String BETS) {
        this.BETS = BETS;
    }

    public String getRAISE() {
        return RAISE;
    }

    public void setRAISE(String RAISE) {
        this.RAISE = RAISE;
    }

    public String getFOLD() {
        return FOLD;
    }

    public void setFOLD(String FOLD) {
        this.FOLD = FOLD;
    }

    public String getCHECK() {
        return CHECK;
    }

    public void setCHECK(String CHECK) {
        this.CHECK = CHECK;
    }

    public String getCALL() {
        return CALL;
    }

    public void setCALL(String CALL) {
        this.CALL = CALL;
    }

    public String getALLIN() {
        return ALLIN;
    }

    public void setALLIN(String ALLIN) {
        this.ALLIN = ALLIN;
    }

    public String getSIDE_POT() {
        return SIDE_POT;
    }

    public void setSIDE_POT(String SIDE_POT) {
        this.SIDE_POT = SIDE_POT;
    }

    public String getMAIN_POT() {
        return MAIN_POT;
    }

    public void setMAIN_POT(String MAIN_POT) {
        this.MAIN_POT = MAIN_POT;
    }

    public String getBIG_BLIND0() {
        return BIG_BLIND0;
    }

    public void setBIG_BLIND0(String BIG_BLIND0) {
        this.BIG_BLIND0 = BIG_BLIND0;
    }

    public String getBIG_BLIND1() {
        return BIG_BLIND1;
    }

    public void setBIG_BLIND1(String BIG_BLIND1) {
        this.BIG_BLIND1 = BIG_BLIND1;
    }

    public String getBIG_BLIND2() {
        return BIG_BLIND2;
    }

    public void setBIG_BLIND2(String BIG_BLIND2) {
        this.BIG_BLIND2 = BIG_BLIND2;
    }

    public String getBIG_BLIND3() {
        return BIG_BLIND3;
    }

    public void setBIG_BLIND3(String BIG_BLIND3) {
        this.BIG_BLIND3 = BIG_BLIND3;
    }

    public String getBIG_BLIND4() {
        return BIG_BLIND4;
    }

    public void setBIG_BLIND4(String BIG_BLIND4) {
        this.BIG_BLIND4 = BIG_BLIND4;
    }

    public String getBIG_BLIND5() {
        return BIG_BLIND5;
    }

    public void setBIG_BLIND5(String BIG_BLIND5) {
        this.BIG_BLIND5 = BIG_BLIND5;
    }

    public String getSMALL_BLIND() {
        return SMALL_BLIND;
    }

    public void setSMALL_BLIND(String SMALL_BLIND) {
        this.SMALL_BLIND = SMALL_BLIND;
    }

    public String getDISCON1() {
        return DISCON1;
    }

    public void setDISCON1(String DISCON1) {
        this.DISCON1 = DISCON1;
    }

    public String getDISCON2() {
        return DISCON2;
    }

    public void setDISCON2(String DISCON2) {
        this.DISCON2 = DISCON2;
    }

    public String getDISCON3() {
        return DISCON3;
    }

    public void setDISCON3(String DISCON3) {
        this.DISCON3 = DISCON3;
    }

    public String getTIMEBANK() {
        return TIMEBANK;
    }

    public void setTIMEBANK(String TIMEBANK) {
        this.TIMEBANK = TIMEBANK;
    }

    public String getLEFT_TABLE() {
        return LEFT_TABLE;
    }

    public void setLEFT_TABLE(String LEFT_TABLE) {
        this.LEFT_TABLE = LEFT_TABLE;
    }

    public String getFINISHED() {
        return FINISHED;
    }

    public void setFINISHED(String FINISHED) {
        this.FINISHED = FINISHED;
    }

    public String getMOVED_TABLE() {
        return MOVED_TABLE;
    }

    public void setMOVED_TABLE(String MOVED_TABLE) {
        this.MOVED_TABLE = MOVED_TABLE;
    }

    public String getJOINED_TABLE() {
        return JOINED_TABLE;
    }

    public void setJOINED_TABLE(String JOINED_TABLE) {
        this.JOINED_TABLE = JOINED_TABLE;
    }

    public int getSETUP() {
        return SETUP;
    }

    public void setSETUP(int SETUP) {
        this.SETUP = SETUP;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public String getGameType() {
        return gameType;
    }

    public boolean isReal() {
        return isReal;
    }

    public String getTableType() {
        return tableType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public String getWhoAmI() {
        return whoAmI;
    }

    public int getButtonSeat() {
        return buttonSeat;
    }

    public int getTotalSeatCount() {
        return totalSeatCount;
    }

    public int getTotalPlayerCount() {
        return totalPlayerCount;
    }

    public String getSEAT() {
        return SEAT;
    }

    public void setSEAT(String SEAT) {
        this.SEAT = SEAT;
    }

    public String getHH_FIRST_LINE_STARTS() {
        return HH_FIRST_LINE_STARTS;
    }

    public void setHH_FIRST_LINE_STARTS(String HH_FIRST_LINE_STARTS) {
        this.HH_FIRST_LINE_STARTS = HH_FIRST_LINE_STARTS;
    }

    public String getIS_THE_BUTTON() {
        return IS_THE_BUTTON;
    }

    public void setIS_THE_BUTTON(String IS_THE_BUTTON) {
        this.IS_THE_BUTTON = IS_THE_BUTTON;
    }

    public String getTOTAL_PLAYER_COUNT() {
        return TOTAL_PLAYER_COUNT;
    }

    public void setTOTAL_PLAYER_COUNT(String TOTAL_PLAYER_COUNT) {
        this.TOTAL_PLAYER_COUNT = TOTAL_PLAYER_COUNT;
    }

    public String getTABLE_TXT() {
        return TABLE_TXT;
    }

    public void setTABLE_TXT(String TABLE_TXT) {
        this.TABLE_TXT = TABLE_TXT;
    }

    public String getREAL_MONEY() {
        return REAL_MONEY;
    }

    public void setREAL_MONEY(String REAL_MONEY) {
        this.REAL_MONEY = REAL_MONEY;
    }

    public String getTOURNY() {
        return TOURNY;
    }

    public void setTOURNY(String TOURNY) {
        this.TOURNY = TOURNY;
    }

    public String getRING_GAME() {
        return RING_GAME;
    }

    public void setRING_GAME(String RING_GAME) {
        this.RING_GAME = RING_GAME;
    }

    public String getSHOW3() {
        return SHOW3;
    }

    public void setSHOW3(String SHOW3) {
        this.SHOW3 = SHOW3;
    }

    public void setColumns()
    {
        TableColumnModel cols = dataTable.getJTable().getColumnModel();
        for (int i=0; i<cols.getColumnCount(); i++)
        {
            String id = (String) cols.getColumn(i).getHeaderValue();
            columns.put(id, i);
        }
    }

    public void setStakesLevel(String stakes, String blind){
        if (getModel().getStakesLevel()==null && blind.equals("SB")) {
            getModel().setStakesLevel(stakes);
        } else if (blind.equals("BB") && !getModel().getStakesLevel().contains("/")){
            getModel().setStakesLevel(stakes + "/" + getModel().getStakesLevel());
        }
    }

    public boolean isFreeroll(){
        return getModel().isFreeroll();
    }

    public void setFreeroll(boolean yn){
        getModel().setIsFreeroll(yn);
    }

    public String getLimitType(){
        return limitType;
    }

    public void setDateTime(String dateTime){
        String[] items = dateTime.split(",");
        if(items.length == 3){
            String timeStr = items[items.length-1];
            if (timeStr.contains(" ")) {
                setTime(timeStr.substring(0, timeStr.indexOf(" ")));
            }

            String[] yearElements = items[items.length-1].split(" ");
            String[] dateElements = items[items.length-2].split(" ");
            String dateStr = dateElements[1] + " " + dateElements[0] + " ";
            String year = yearElements[yearElements.length-1];
            setDate(dateStr + year);
        }
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setBb_amount(String bb_amount) {
        this.bb_amount = bb_amount;
    }

    public void setSb_amount(String sb_amount) {
        this.sb_amount = sb_amount;
    }

    public void setAnte(String ante) {
        this.ante = ante;
    }

    public String getBigBlindAmount()
    {
        return bb_amount;
    }

    public String getSmallBlindAmount()
    {
        return sb_amount;
    }

    public String getAnte()
    {
        return ante;
    }

    public void setBuyIn(String buyIn){
        this.buyIn = buyIn;
    }

    public String getBuyIn(){
        return buyIn;
    }
}