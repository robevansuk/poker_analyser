package com.morphiles.processors;

import com.morphiles.views.DataTable;
import com.morphiles.models.PokerDataModel;

import javax.swing.*;

/**
 * Serves as a display when no data is available for a hand history.
 *
 * @author robevans
 */
public class BlankHandHistory extends HandHistoryProcessor {

    public BlankHandHistory(DataTable table, PokerDataModel model){
        super(table, model);
    }

    @Override
    public void addData(String data, int handId, String gameId) {
        // Do nothing - this table is just for show.
    }
}
