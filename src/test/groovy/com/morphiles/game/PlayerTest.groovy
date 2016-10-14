package com.morphiles.game

import spock.lang.Specification

/**
 * Created by ren7881 on 10/7/16.
 */
class PlayerTest extends Specification {

  def "when a player object is created with a seat ID"() {
    when:
    Player testObject = new Player(1);

    then:
    with(testObject) {
      getSeatId() == 1
      playerId == ""
      stack == new BigDecimal(0.0)
      currencyType == ""

      contributions.size() == 4
      actions.size() == 4
      totalPot.size() == 4
      myPot.size() == 4
      playersPerRound.size() == 4
      profitAmount == new BigDecimal(0);

      communityCards.isEmpty()
      buttonSeat == -1
      tablePlayerCount == -1
      totalSeatCount == -1
      isBigBlind == false
      isSmallBlind == false
      isWinner == false
      hasLeft == false
      preflopHand == ""
    }
  }

  def "when a player is instantiated with multiple variables then they are set accordingly" () {
    when:
    Player testObject = new Player(screenName, stackAmount, seatId, currencyType)

    then:
    with(testObject) {
      getSeatId() == seatId
      playerId == screenName
      stack == stackAmount
      currency == currencyType

      contributions.size() == 4
      contributions.each{ assert it == new BigDecimal(0.0) }
      actions.size() == 4
      actions.each { assert it == "" }
      totalPot.size() == 4
      totalPot.each { assert it == new BigDecimal(0.0) }
      myPot.size() == 4
      myPot.each { assert it == new BigDecimal(0.0) }
      playersPerRound.size() == 4
      playersPerRound.each { assert it == 0 }
      profitAmount == new BigDecimal(0);

      communityCards.isEmpty()
      buttonSeat == -1
      tablePlayerCount == -1
      totalSeatCount == -1
      isBigBlind == false
      isSmallBlind == false
      isWinner == false
      hasLeft == false
      preflopHand == ""
    }

    where:
    screenName | stackAmount             | seatId | currencyType
    "someName" | new BigDecimal(3000.01) | 1      | "chips"
    "pkrOD"    | new BigDecimal(0.0)     | 10     | "£"
    "pkrOD99"  | new BigDecimal(25.0)    | 5      | "€"
    "anything" | new BigDecimal(3.68)    | 2      | '$'
  }

}