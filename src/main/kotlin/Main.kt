package org.example

class Rotor(private val wiring: String, private val notch: Char) {
    private var position: Int = 0

    fun forward(inputChar: Char): Char {
        val inputIndex = (inputChar.uppercaseChar() - 'A' + position) % 26
        println((inputChar.uppercaseChar() - 'A' + position) % 26)
        println(inputIndex)
        val wiredChar = wiring[inputIndex]
        return wiredChar
    }

    fun backward(inputChar: Char): Char {
        val wiredIndex = wiring.indexOf(inputChar.uppercaseChar())
        val outputIndex = (wiredIndex - position + 26) % 26
        return 'A' + outputIndex
    }


    fun rotate(): Boolean {
        position = (position + 1) % 26
        return position == (notch - 'A')
    }
}

fun main() {

    val rotor = Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q')

    while (true) {
        val inputChar = readln().toCharArray().first()
        println("Входной символ: $inputChar")

        val encryptedChar = rotor.forward(inputChar)
        println("Зашифрованный символ: $encryptedChar")

        val decryptedChar = rotor.backward(encryptedChar)
        println("Расшифрованный символ: $decryptedChar")
        rotor.rotate()
    }
}

