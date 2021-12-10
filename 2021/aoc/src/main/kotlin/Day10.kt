import java.util.*

fun main() {
    Day10.part1(ClassLoader.getSystemResource("day10_full.in").readText().trim())
//    Day10.part1(ClassLoader.getSystemResource("day10_example.in").readText().trim())
}

object Day10 {

    val scores = mutableMapOf(
        Pair(')', 3),
        Pair(']', 57),
        Pair('}', 1197),
        Pair('>', 25137)
    )
    val chunkPairs = mutableMapOf(
        Pair('(', ')'),
        Pair('[', ']'),
        Pair('{', '}'),
        Pair('<', '>')
    )

    fun part1(input: String) {
        val lines = input.split("\r\n")

        var sum = 0
        for (line in lines) {
            val ret = checkLine(line)
            if (ret != null) {
                sum += scores[ret]!!
            }
        }
        println(sum)
    }

    private fun checkLine(line: String): Char? {
        val stack: Deque<Char> = LinkedList()

        for (i in line.chars()) {
            val c = i.toChar()
            if (chunkPairs.keys.contains(c)) {
                stack.push(c)
            } else {
                val openingChar = stack.pop()
                if (c != chunkPairs[openingChar]!!) {
                    return c
                }
            }
        }
        return null
    }


}

