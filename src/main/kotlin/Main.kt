package org.example

open class EnigmaMachine(
    private val rotors: List<Rotor>,
    private val reflector: Reflector
) {
    private fun encryptThroughRotors(inputCharIndex: Int, direction: Boolean, rotors: List<Rotor>): Int {
        val rotorSequence = if (direction) rotors else rotors.reversed()
        return rotorSequence.fold(inputCharIndex) { index, rotor ->
            rotor.encrypt(index, direction)
        }
    }

    fun encryptRotorsWithReflector(inputCharIndex: Int): Int {
        val forwardIndex = encryptThroughRotors(inputCharIndex, true, rotors)
        val reflectorIndex = reflector.reflection(forwardIndex)
        return encryptThroughRotors(reflectorIndex, false, rotors)
    }


}

fun <T> shiftList(list: List<T>, shift: Int): List<T> {
    val size = list.size
    if (size == 0) return list
    val actualShift = (shift % size + size) % size
    return list.drop(actualShift) + list.take(actualShift)
}

fun Char.charToIndex(alphabetList: List<Char>): Int = this - alphabetList.first()
fun Int.intToChar(alphabetList: List<Char>): Char = alphabetList.first() + this

fun List<String>.toMirrorMap(): Map<Char, Char> {
    return buildMap {
        this@toMirrorMap.forEach { pair ->
            require(pair.length == 2) {
                "Each element must be a pair of characters, but got \"$pair\""
            }

            val (first, second) = pair.toCharArray()
            put(first, second)
            put(second, first)
        }
    }
}

class Rotor(
    private val wiring: String,
    private val notch: Char,
    alphabetList: List<Char> = ('A'..'Z').toList(),
    initPosition: Char = alphabetList.first()
) {
    private val alphabetList = alphabetList.map { it.uppercaseChar() }
    private var position = initPosition.charToIndex(alphabetList)

    fun setPosition(defaultPosition: Char) {
        position = defaultPosition.charToIndex(alphabetList)
    }

    fun encrypt(inputIndex: Int, forward: Boolean): Int {
        val shiftedList = shiftList(alphabetList, position)

        val shiftedChar = shiftedList[inputIndex]
        val index = if (forward) alphabetList.indexOf(shiftedChar) else wiring.indexOf(shiftedChar)

        val resultChar = if (forward) wiring[index] else alphabetList[index]
        return shiftedList.indexOf(resultChar)
    }

    fun rotate(): Boolean {
        position = (position + 1) % alphabetList.size
        return position == notch.charToIndex(alphabetList) + 1
    }
}

class Reflector(
    initWiringPairList: List<String>,
    alphabetList: List<Char> = ('A'..'Z').toList()
) {
    private var wiringPairList = initWiringPairList
    private val alphabetList = alphabetList.map { it.uppercaseChar() }
    fun reflection(inputIndex: Int): Int {
        val inputChar = alphabetList[inputIndex]
        val reflectedChar = wiringPairList.toMirrorMap().getValue(inputChar)
        return alphabetList.indexOf(reflectedChar)
    }

    fun setWiring(newWiringPairList: List<String>) {
        wiringPairList = newWiringPairList
    }
}

class Plugboard(
    initWiringPairList: List<String> = emptyList(),
    alphabetList: List<Char> = ('A'..'Z').toList()
) {
    private val alphabetList = alphabetList.map { it.uppercaseChar() }
    private var wiringPairList = initWiringPairList
    fun substitution(alphabetCharIndex: Int): Int {
        val inputChar = alphabetList[alphabetCharIndex]
        val substitutedChar = wiringPairList.toMirrorMap()[inputChar.uppercaseChar()] ?: inputChar
        return alphabetList.indexOf(substitutedChar)
    }
    fun setWiring(newWiringPairList: List<String>) {
        wiringPairList = newWiringPairList
    }
}

fun main() {
    while (true) {
        val rotorI = Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q')
        val rotorII = Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E')
        val rotorIII = Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V')
        val reflectorWiringList = listOf(
            "BR","CU","DH","EQ","FS","GL","IP","JX","KN","MO","TZ","VW","AY"
        )
        val plugboardWiringList = listOf(
            "FJ"
        )

        val reflector = Reflector(reflectorWiringList)
        val plugboard = Plugboard(plugboardWiringList)


        val outputCharList = mutableListOf<Char>()
        val inputCharList = mutableListOf<Char>()

        val inputChar = readlnOrNull()?.uppercase()?.trim()?.toCharArray() ?: continue
        inputChar.forEach {
            val plugboardOutCharIndex = plugboard.substitution(it - 'A')
            inputCharList.add(it)

            if (rotorI.rotate()) {
                if (rotorII.rotate()) {
                    rotorIII.rotate()
                }
            }

            EnigmaMachine(listOf(rotorI, rotorII, rotorIII), reflector).encryptRotorsWithReflector(plugboardOutCharIndex)
        }
        print("Вывод: ")
        outputCharList.forEach { print(it) }
        println()
    }
}
