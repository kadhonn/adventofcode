import kotlin.math.absoluteValue

fun main() {
    Day7.part1(ClassLoader.getSystemResource("day7_full.in").readText().trim())
//    Day7.part1(ClassLoader.getSystemResource("day7_example.in").readText().trim())
}

object Day7 {

    fun part1(input: String) {
        val crabs = input.split(",")
            .map { it.toInt() }

        val min = (crabs.minOrNull()!!..crabs.maxOrNull()!!).map { cost(crabs, it) }.minOrNull()

        println(min)

    }

    private fun cost(crabs: List<Int>, middle: Int): Int {
        return crabs.map { ((it - middle).absoluteValue downTo 1).sum()  }.sum()
    }

}

