package com.morphiles.matchers

import com.morphiles.game.Player

/**
 * Matcher class and logic for what to do with Big Blind data. This covers data such as...
 * big blind on first entering a hand  when you've just sat down
 * big blind when a player is in the big blind
 * big blind + dead blind when a player sits out, then sits back in.
 */
class BigBlind {

    BigDecimal amount;
    Player playerId;

    public BigBlind(String data) {

    }
}
