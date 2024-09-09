package org.example

class Rotor(private val wiring: String, private val notch: Char) {
    private var position: Int = 0
    private val alphabetList = ('A'..'Z').toList()

    fun forward(inputIndex: Int): Int {
        val shiftedListIn = shiftList(alphabetList, position)

        val firstShiftedChar = shiftedListIn[inputIndex]
        val wiringIndex = alphabetList.indexOf(firstShiftedChar)
        val encryptedChar = wiring[wiringIndex]

        return shiftedListIn.indexOf(encryptedChar)
    }

    fun rotate(): Boolean {
        position = (position + 1) % 26
        return position == (notch - 'A' + 1)
    }
}

fun <T> shiftList(list: List<T>, shift: Int): List<T> {
    val size = list.size
    if (size == 0) return list
    val actualShift = (shift % size + size) % size
    return list.drop(actualShift) + list.take(actualShift)
}

fun encryptThroughRotors(inputCharIndex: Int, vararg rotors: Rotor): Int {
    return rotors.fold(inputCharIndex) { index, rotor ->
         rotor.forward(index)
    }
}


fun main() {
    val rotorI = Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q')
    val rotorII = Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E')
    val rotorIII = Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V')

    while (true) {

        val inputChar = readln().uppercase().first()
        val inputIndex = inputChar - 'A'
        println("Входной символ: $inputChar")

        val encryptedChar = 'A' + encryptThroughRotors(inputIndex, rotorI, rotorII, rotorIII)
        println("Зашифрованный символ: $encryptedChar")

        if (rotorI.rotate()) {
            if (rotorII.rotate()) {
                rotorIII.rotate()
            }
        }

    }
}
