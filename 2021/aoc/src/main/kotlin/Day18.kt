import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day18.part1(ClassLoader.getSystemResource("day18_full.in").readText().trim())
//        Day18.part1(ClassLoader.getSystemResource("day18_example.in").readText().trim())
    })
}

object Day18 {

    interface Number {
        var parent: Pair?
        fun sum(): Int
    }

    class Single(override var parent: Pair? = null, var n: Int) : Number {
        override fun sum(): Int {
            return n
        }

        override fun toString(): String {
            return "$n"
        }
    }

    class Pair(override var parent: Pair? = null, var n1: Number, var n2: Number) : Number {
        init {
            n1.parent = this
            n2.parent = this
        }

        override fun sum(): Int {
            return 3 * n1.sum() + 2 * n2.sum()
        }

        override fun toString(): String {
            return "[$n1,$n2]"
        }
    }

    fun part1(input: String) {
        val lines = input.split("\r\n")

        var max = -1
        for (i in 0 until lines.size) {
            for (j in 0 until lines.size) {
                if (i == j) {
                    continue
                }
                max = max(max, add(parseNumber(lines[i]), parseNumber(lines[j])).sum())
            }
        }
        println(max)
    }

    private fun add(n1: Number, n2: Number): Number {
        val afterAddition = Pair(null, n1, n2)
//        println(afterAddition)
        var newNumber = reduce(afterAddition)
//        println(newNumber.first)
        while (newNumber.second) {
            newNumber = reduce(newNumber.first)
//            println(newNumber.first)
        }
        return newNumber.first
    }

    fun reduce(number: Number): kotlin.Pair<Number, Boolean> {
        val newNumber = tryReduceExplode(number)
        if (newNumber.second) {
            return newNumber
        }
        return tryReduceSplit(number)
    }

    private fun tryReduceSplit(number: Number): kotlin.Pair<Number, Boolean> {
        if (number is Single && number.n >= 10) {
            return kotlin.Pair(reduceSplit(number), true)
        } else if (number is Pair) {
            var newNumber = tryReduceSplit(number.n1)
            if (newNumber.second) {
                return newNumber
            }
            newNumber = tryReduceSplit(number.n2)
            if (newNumber.second) {
                return newNumber
            }
        }
        return kotlin.Pair(getRoot(number), false)
    }

    private fun tryReduceExplode(number: Number, depth: Int = 0): kotlin.Pair<Number, Boolean> {
        if (number is Pair) {
            if (depth == 4) {
                return kotlin.Pair(reduceExplode(number), true)
            } else {
                var newNumber = tryReduceExplode(number.n1, depth + 1)
                if (newNumber.second) {
                    return newNumber
                }
                newNumber = tryReduceExplode(number.n2, depth + 1)
                if (newNumber.second) {
                    return newNumber
                }
            }
        }
        return kotlin.Pair(getRoot(number), false)
    }

    private fun reduceExplode(number: Pair): Number {
        findFirstLeftSingleAndAdd(number, (number.n1 as Single).n)
        findFirstRightSingleAndAdd(number, (number.n2 as Single).n)
        val parent = number.parent!!
        if (parent.n1 == number) {
            parent.n1 = Single(parent, 0)
        } else {
            parent.n2 = Single(parent, 0)
        }
        return getRoot(number)
    }

    private fun findFirstLeftSingleAndAdd(number: Pair, n: Int) {
        var current = number
        var parent = number.parent
        var firstLeftNeighbour: Number? = null
        while (parent != null) {
            if (parent.n2 == current) {
                firstLeftNeighbour = parent.n1
                break
            }
            current = parent
            parent = current.parent
        }
        if (firstLeftNeighbour != null) {
            while (firstLeftNeighbour !is Single) {
                firstLeftNeighbour = (firstLeftNeighbour as Pair).n2
            }
            firstLeftNeighbour.n += n
        }
    }

    private fun findFirstRightSingleAndAdd(number: Pair, n: Int) {
        var current = number
        var parent = number.parent
        var firstReightNeighbour: Number? = null
        while (parent != null) {
            if (parent.n1 == current) {
                firstReightNeighbour = parent.n2
                break
            }
            current = parent
            parent = current.parent
        }
        if (firstReightNeighbour != null) {
            while (firstReightNeighbour !is Single) {
                firstReightNeighbour = (firstReightNeighbour as Pair).n1
            }
            firstReightNeighbour.n += n
        }
    }

    private fun reduceSplit(number: Single): Number {
        val parent = number.parent!!
        val splitted = Pair(
            parent,
            Single(null, floor(number.n.toDouble() / 2F).toInt()),
            Single(null, ceil(number.n.toDouble() / 2F).toInt())
        )
        if (parent.n1 == number) {
            parent.n1 = splitted
        } else {
            parent.n2 = splitted
        }
        return getRoot(number)
    }

    private fun getRoot(number: Number): Number {
        var root = number
        while (root.parent != null) {
            root = root.parent!!
        }
        return root
    }

    fun parseNumber(s: String): Number {
        return if (s.startsWith("[")) {
            val pair = split(s)
            Pair(null, parseNumber(pair.first), parseNumber(pair.second))
        } else {
            Single(null, s.toInt())
        }
    }

    private fun split(s: String): kotlin.Pair<String, String> {
        var depth = 0
        for (i in s.indices) {
            val c = s[i]
            if (depth == 1 && c == ',') {
                return Pair(s.substring(1, i), s.substring(i + 1, s.length - 1))
            } else if (c == '[') {
                depth++
            } else if (c == ']') {
                depth--
            }
        }
        throw RuntimeException("cannot split $s")
    }

}

