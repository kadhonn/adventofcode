import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day18_2.part1(ClassLoader.getSystemResource("day18_full.in").readText().trim())
//        Day18_2.part1(ClassLoader.getSystemResource("day18_example.in").readText().trim())
    })
}
typealias CrabNumber = MutableList<Pair<Int, Int>>

object Day18_2 {


    fun part1(input: String) {
        val lines = input.split("\r\n")

//        var number = parseNumber(lines[0])
//        for (i in 1 until lines.size) {
//            number = add(number, parseNumber(lines[i]))
//            println(number)
//        }

        var max = -1
        for (i in 0 until lines.size) {
            for (j in 0 until lines.size) {
                if (i == j) {
                    continue
                }
                max = max(max, magnitude(add(parseNumber(lines[i]), parseNumber(lines[j]))))
            }
        }
        println(max)
    }

    private fun magnitude(number: CrabNumber): Int {
        val toMagnify = number.toMutableList()
        outer@
        while (toMagnify.size != 1) {
            for (i in 0 until toMagnify.size - 1) {
                if (toMagnify[i].second == toMagnify[i + 1].second) {
                    toMagnify[i] = Pair(toMagnify[i].first * 3 + toMagnify[i + 1].first * 2, toMagnify[i].second - 1)
                    toMagnify.removeAt(i + 1)
                    continue@outer
                }
            }
            break
        }
        return toMagnify[0].first
    }

    private fun add(n1: CrabNumber, n2: CrabNumber): CrabNumber {
        val newNumber = mutableListOf<Pair<Int, Int>>()
        newNumber.addAll(n1)
        newNumber.addAll(n2)

        return reduce(newNumber.map { Pair(it.first, it.second + 1) }.toMutableList())
    }

    private fun reduce(number: CrabNumber): CrabNumber {
        outer@
        while (true) {
            //explode
            for (i in number.indices) {
                val digit = number[i]
                if (digit.second == 5) {
                    if (i - 1 >= 0) {
                        val leftDigit = number[i - 1]
                        number[i - 1] = Pair(leftDigit.first + digit.first, leftDigit.second)
                    }
                    if (i + 2 < number.size) {
                        val rightDigit = number[i + 2]
                        number[i + 2] = Pair(rightDigit.first + number[i + 1].first, rightDigit.second)
                    }
                    number.removeAt(i)
                    number[i] = Pair(0, 4)
                    continue@outer
                }
            }
            //split
            for (i in number.indices) {
                val digit = number[i]
                if (digit.first >= 10) {
                    number[i] = Pair(floor(digit.first.toDouble() / 2F).toInt(), digit.second + 1)
                    number.add(i + 1, Pair(ceil(digit.first.toDouble() / 2F).toInt(), digit.second + 1))
                    continue@outer
                }
            }
            break
        }

        return number
    }

    private fun parseNumber(s: String): CrabNumber {
        val number = mutableListOf<Pair<Int, Int>>()

        var currentDepth = 0
        for (c in s.trim().toCharArray()) {
            when (c) {
                '[' -> currentDepth++
                ']' -> currentDepth--
                ',' -> {}
                else -> {
                    number.add(Pair(c.digitToInt(), currentDepth))
                }
            }
        }

        return number
    }

}

