package org.example

class Rotor(private val wiring: String, private val notch: Char, initPosition: Char = 'A') {
    private var position = initPosition - 'A'
    private val alphabetList = ('A'..'Z').toList()

    fun forward(inputIndex: Int): Int {
        val shiftedList = shiftList(alphabetList, position)

        val firstShiftedChar = shiftedList[inputIndex]
        val alphabetCharIndex = alphabetList.indexOf(firstShiftedChar)
        val wiringChar = wiring[alphabetCharIndex]

        return shiftedList.indexOf(wiringChar)
    }

    fun backward(inputIndex: Int): Int {
        val shiftedList = shiftList(alphabetList, position)

        val firstShiftedChar = shiftedList[inputIndex]
        val wiringCharIndex = wiring.indexOf(firstShiftedChar)
        val alphabetChar = alphabetList[wiringCharIndex]

        return shiftedList.indexOf(alphabetChar)
    }

    fun rotate(): Boolean {
        position = (position + 1) % 26
        return position == notch - 'A' + 1
    }
}

class Reflector(private val wiringPairList: List<Pair<Char, Char>>) {
    private val alphabetList = ('A'..'Z').toList()
    fun substitution(inputIndex: Int): Int {
        val inputChar = alphabetList[inputIndex]
        val substitutedChar = wiringPairList.first {
            it.first == inputChar || it.second == inputChar
        }.let {
            if (it.first == inputChar) it.second else it.first
        }
        return alphabetList.indexOf(substitutedChar)
    }
}

fun <T> shiftList(list: List<T>, shift: Int): List<T> {
    val size = list.size
    if (size == 0) return list
    val actualShift = (shift % size + size) % size
    return list.drop(actualShift) + list.take(actualShift)
}

fun String.toPair(): Pair<Char, Char> {
    require(this.length == 2) { "Строка должна состоять из двух символов" }
    return this[0] to this[1]
}

fun encryptThroughRotors(inputCharIndex: Int, vararg rotors: Rotor): Int {
    return rotors.fold(inputCharIndex) { index, rotor ->
         rotor.forward(index)
    }
}

fun decryptThroughRotors(inputCharIndex: Int, vararg rotors: Rotor): Int {
    return rotors.fold(inputCharIndex) { index, rotor ->
        rotor.backward(index)
    }
}

fun main() {
    val rotorI = Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q')
    val rotorII = Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E')
    val rotorIII = Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V')
    val reflectorWiringList = listOf(
        "BR","CU","DH","EQ","FS","GL","IP","JX","KN","MO","TZ","VW","AY"
    ).map { it.toPair() }

    val reflector = Reflector(reflectorWiringList)

    val outputCharList = mutableListOf<Char>()
    val inputCharList = mutableListOf<Char>()


    while (true) {

        val inputChar = readln().uppercase().first()
        inputCharList.add(inputChar)
        val inputIndex = inputChar - 'A'

        if (rotorI.rotate()) {
            if (rotorII.rotate()) {
                rotorIII.rotate()
            }
        }

        val encryptedForwardRotorIndex = encryptThroughRotors(inputIndex, rotorI, rotorII, rotorIII)
        val reflectorEncryptedCharIndex = reflector.substitution(encryptedForwardRotorIndex)
        val res = 'A' + decryptThroughRotors(reflectorEncryptedCharIndex, rotorIII, rotorII, rotorI)
        outputCharList.add(res)


        if (outputCharList.size == 50) {
            inputCharList.forEach { print(it) }
            println("\n")
            outputCharList.forEach { print(it) }
        }

    }
}
//AUKADFGDAGVCDFGWERTDSAFCVBNGHTEDFGHASDFGHBVCXZZASD

//FBYMCUMGILKUYKYQOGFNYYPZMPPTYLAWQJGGRWKMOSYUEBFHWM
//--------------------------------------------------
//FBYMCUMGILKUYKYQOGFNYYPZMPPTYLAWQJGGRWKMOSYUEBFHWM
//
//AUKADFGDAGVCDFGWERTDSAFCVBNGHTEDFGHASDFGHBVCXZZASD
