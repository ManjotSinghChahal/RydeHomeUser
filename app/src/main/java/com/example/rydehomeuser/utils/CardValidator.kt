package com.example.rydehomeuser.utils

import android.text.TextUtils
import android.util.Log


class CardValidator
{

    val VISA: Byte = 0
    val MASTERCARD: Byte = 1
    val AMEX: Byte = 2
    val DINERS_CLUB: Byte = 3
    val CARTE_BLANCHE: Byte = 4
    val DISCOVER: Byte = 5
    val ENROUTE: Byte = 6
    val JCB: Byte = 7


    fun validate(credCardNumber: String, type: Byte): Boolean {
        val creditCard = credCardNumber.trim { it <= ' ' }
        var applyAlgo = false
        when (type) {
            VISA ->
                // VISA credit cards has length 13 - 15
                // VISA credit cards starts with prefix 4
                if (creditCard.length >= 13 && creditCard.length <= 16
                    && creditCard.startsWith("4")
                ) {
                    applyAlgo = true
                }
            MASTERCARD ->
                // MASTERCARD has length 16
                // MASTER card starts with 51, 52, 53, 54 or 55
                if (creditCard.length == 16) {
                    val prefix = Integer.parseInt(creditCard.substring(0, 2))
                    if (prefix >= 51 && prefix <= 55) {
                        applyAlgo = true
                    }
                }
            AMEX ->
                // AMEX has length 15
                // AMEX has prefix 34 or 37
                if (creditCard.length == 15 && (creditCard.startsWith("34") || creditCard
                        .startsWith("37"))
                ) {
                    applyAlgo = true
                }
            DINERS_CLUB, CARTE_BLANCHE ->
                // DINERSCLUB or CARTEBLANCHE has length 14
                // DINERSCLUB or CARTEBLANCHE has prefix 300, 301, 302, 303, 304,
                // 305 36 or 38
                if (creditCard.length == 14) {
                    val prefix = Integer.parseInt(creditCard.substring(0, 3))
                    if (prefix >= 300 && prefix <= 305
                        || creditCard.startsWith("36")
                        || creditCard.startsWith("38")
                    ) {
                        applyAlgo = true
                    }
                }
            DISCOVER ->
                // DISCOVER card has length of 16
                // DISCOVER card starts with 6011
                if (creditCard.length == 16 && creditCard.startsWith("6011")) {
                    applyAlgo = true
                }
            ENROUTE ->
                // ENROUTE card has length of 16
                // ENROUTE card starts with 2014 or 2149
                if (creditCard.length == 16 && (creditCard.startsWith("2014") || creditCard
                        .startsWith("2149"))
                ) {
                    applyAlgo = true
                }
            JCB ->
                // JCB card has length of 16 or 15
                // JCB card with length 16 starts with 3
                // JCB card with length 15 starts with 2131 or 1800
                if (creditCard.length == 16 && creditCard.startsWith("3") || creditCard.length == 15 && (creditCard
                        .startsWith("2131") || creditCard
                        .startsWith("1800"))
                ) {
                    applyAlgo = true
                }
            else -> throw IllegalArgumentException()
        }
        return if (applyAlgo) {
            validate(creditCard)
        } else false
    }

    fun validate(creditCard: String): Boolean {
        // 4 9 9 2 7 3 9 8 7 1 6
        // 6
        // 1 x 2 = 2  = (0 + 2) = 2
        // 7
        // 8 x 2 = 16 = (1 + 6) = 7
        // 9
        // 3 x 2 = 6 = (0 + 6) = 6
        // 7
        // 2 x 2 = 4 = (0 + 4) = 4
        // 9
        // 9 X 2 = 18 = (1 + 8) = 9
        // 4
        // 6+2+7+7+9+6+7+4+9+9+4 = 70
        // return 0 == (70 % 10)
        var sum = 0
        val length = creditCard.length
        for (i in 0 until creditCard.length) {
            if (0 == i % 2) {
                sum += creditCard[length - i - 1] - '0'
            } else {
                sum += sumDigits((creditCard[length - i - 1] - '0') * 2)
            }
        }
        return 0 == sum % 10
    }

    private fun sumDigits(i: Int): Int {
        return i % 10 + i / 10
    }

    fun getCardType(number : String) : String
    {
       // val listOfPattern : ArrayList<String> = ArrayList<String>()

        val ptVisa : String= "^4[0-9]{6,}$"
     //   listOfPattern.add(ptVisa)
        val ptMasterCard : String= "^5[1-5][0-9]{5,}$"
     //   listOfPattern.add(ptMasterCard);
        val ptAmeExp : String = "^3[47][0-9]{5,}$"
     //   listOfPattern.add(ptAmeExp);
        val ptDinClb : String= "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$"
     //   listOfPattern.add(ptDinClb)
        val ptDiscover : String = "^6(?:011|5[0-9]{2})[0-9]{3,}$"
      //  listOfPattern.add(ptDiscover)
        val ptJcb : String = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$"
      //  listOfPattern.add(ptJcb)


        if (number.matches(ptVisa.toRegex()))
            return "Visa"
        else if (number.matches(ptMasterCard.toRegex()))
            return "MasterCard"
        else if (number.matches(ptAmeExp.toRegex()))
            return "American Express"
        else if (number.matches(ptDinClb.toRegex()))
            return "Diners Club"
        else if (number.matches(ptDiscover.toRegex()))
            return "Discover"
        else if (number.matches(ptJcb.toRegex()))
            return "JCB"

        return ""
    }



}