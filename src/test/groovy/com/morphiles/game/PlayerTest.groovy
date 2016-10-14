package com.morphiles.game

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
/**
 * Created on 10/7/16.
 */
class PlayerTest extends Specification {

  @Shared
  Player testObject

  def setupSpec() {
    testObject = new Player("pkrOD", new BigDecimal(15.11), 4, '$')
  }

  def "when a player object is created with a seat ID"() {
    when:
    testObject = new Player(1);

    then:
    with(testObject) {
      getSeatId() == 1
      playerId == ""
      stack == new BigDecimal(0.0)
      currency == ""

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

  @Unroll
  def "when a player is instantiated with multiple variables then they are set accordingly"() {
    when:
    testObject = new Player(screenName, stackAmount, seatId, currencyType)

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

  // Do not use @Unroll here as this treats each tuple of parameterised
  // test input as a single test but we want the tests to share the state of the
  // testObject between those tests.
  def "when a player makes contributions to the pot then the potContributions is updated correctly"() {
    when:
    testObject.updateContributions(roundId, new BigDecimal(potContribution))

    then:
    testObject.getContributions(roundId) == new BigDecimal(cumulativeTotal)
    testObject.getTotalPot(roundId) == new BigDecimal(cumulativePotTotal)

    where:
    potContribution | roundId | cumulativeTotal | cumulativePotTotal
    1.0             | 0       | 1.0             | 1.0
    0.0             | 0       | 1.0             | 1.0
    3.55            | 0       | 4.55            | 4.55
    11.20           | 1       | 11.20           | 15.75
    11.30           | 1       | 22.50           | 38.20
  }

//  def "when  then "() {
//
//  }
}