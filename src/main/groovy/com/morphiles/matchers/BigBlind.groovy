package com.morphiles.matchers

import com.morphiles.game.Player

/**
 * Matcher class and logic for what to do with Big Blind data. This covers data such as...
 * big blind on first entering a hand  when you've just sat down
 * big blind when a player is in the big blind
 * big blind + dead blind when a player sits out, then sits back in.
 */
class BigBlind {

    private static final String[] BIG_BLINDS = [" posts big blind",
        "Blinds(",
        "Stakes(",
        " posts big blind + dead [", // for this to work the check must be done prior to BB3/0
        " posts big blind [", // redundant since if BB0 is found this won't be
        "Blinds-Antes("]

    BigDecimal amount;
    Player playerId;

    public BigBlind(String data) {
        // depending on the big blind value found,
    }
}
