package com.morphiles.game

import spock.lang.Specification

/**
 * some basic unit testing for the Card object using Spock.
 */
class CardTest extends Specification {

    def "when I request the rank of a card by string name then I get the corresponding integer value"() {
        when:
        Card testObject = new Card(expectedResult, "h")

        then:
        testObject.getRankFor(rank) == expectedResult

        where:
        rank | expectedResult
        "2"  | 2
        "3"  | 3
        "4"  | 4
        "5"  | 5
        "6"  | 6
        "7"  | 7
        "8"  | 8
        "9"  | 9
        "T"  | 10
        "J"  | 11
        "Q"  | 12
        "K"  | 13
        "A"  | 14
    }

    def "when I request the card's string value then I should get the corresponding rank returned"() {
        when:
        Card testObject = new Card(rank, "h")

        then:
        testObject.getNamedRank() == expectedResult

        where:
        rank | expectedResult
        2    | "2"
        3    | "3"
        4    | "4"
        5    | "5"
        6    | "6"
        7    | "7"
        8    | "8"
        9    | "9"
        10   | "T"
        11   | "J"
        12   | "Q"
        13   | "K"
        14   | "A"
    }

    def "when I get a card of a particular suit then the suit is set correctly"(){
        when:
        Card testObject = new Card(14, expectedSuit)

        then:
        testObject.getSuit() == expectedSuit

        where:
        expectedSuit << "h, s, d, c".toList()
    }
}
