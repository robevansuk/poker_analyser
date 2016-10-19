package com.morphiles.analysis

/**
 * A series of that last 10 hands will be passed into this class
 * this class will then look for a situation like a loss (of any size)...
 * if a player experiences a sequence of losses,
 * they're liable to go on tilt.
 *   -  If they experience a big loss with a good hand, they're liable to go on tilt
 *   -  If they experience a sequence of losses (over the past 10 hands) they may be liable to go on tilt.
 *      especially if they were the original raiser for a number of the hands.
 *   -  If they experience a loss with a good hand that got lucked out on.
 *   -  etc. List any other checks for tilt done here.
 * Note: Each player handles a loss differently. Some players tighten up, others (less experienced) instantly lose
 * their composure and start off-loading all their chips in frustration. You're individual ability to spot these
 * situations and capitalise on them will lead to better play in yourself as well as more profit.
 */
class TiltAnalysis {
}
