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

class Reflector(private val wiringPairList: List<String>) {
    private val alphabetList = ('A'..'Z').toList()
    fun substitution(inputIndex: Int): Int {
        val inputChar = alphabetList[inputIndex]
        val substitutedChar = wiringPairList.toMirrorMap().getValue(inputChar)
        return alphabetList.indexOf(substitutedChar)
    }
}

fun <T> shiftList(list: List<T>, shift: Int): List<T> {
    val size = list.size
    if (size == 0) return list
    val actualShift = (shift % size + size) % size
    return list.drop(actualShift) + list.take(actualShift)
}

fun List<String>.toMirrorMap(): Map<Char, Char> {
    val resultMap = mutableMapOf<Char, Char>()
    for (pair in this) {
        require(pair.length == 2) { "Each element must be a pair of characters." }

        val first = pair[0]
        val second = pair[1]

        resultMap[first] = second
        resultMap[second] = first
    }
    return resultMap
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
    )

    val reflector = Reflector(reflectorWiringList)


    while (true) {
        val outputCharList = mutableListOf<Char>()
        val inputCharList = mutableListOf<Char>()

        val inputChar = readlnOrNull()?.uppercase()?.trim()?.toCharArray() ?: continue
        inputChar.forEach {
            inputCharList.add(it)
            val inputIndex = it - 'A'

            if (rotorI.rotate()) {
                if (rotorII.rotate()) {
                    rotorIII.rotate()
                }
            }

            val encryptedForwardRotorIndex = encryptThroughRotors(inputIndex, rotorI, rotorII, rotorIII)
            val reflectorEncryptedCharIndex = reflector.substitution(encryptedForwardRotorIndex)
            val res = 'A' + decryptThroughRotors(reflectorEncryptedCharIndex, rotorIII, rotorII, rotorI)
            outputCharList.add(res)
        }
        print("Вывод: ")
        outputCharList.forEach { print(it) }
        println()
    }
}
