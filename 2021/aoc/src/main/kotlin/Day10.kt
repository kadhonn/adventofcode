import java.util.*

fun main() {
    Day10.part1(ClassLoader.getSystemResource("day10_full.in").readText().trim())
//    Day10.part1(ClassLoader.getSystemResource("day10_example.in").readText().trim())
}

object Day10 {

    val scores = mutableMapOf(
        Pair(')', 1),
        Pair(']', 2),
        Pair('}', 3),
        Pair('>', 4)
    )
    val chunkPairs = mutableMapOf(
        Pair('(', ')'),
        Pair('[', ']'),
        Pair('{', '}'),
        Pair('<', '>')
    )

    fun part1(input: String) {
        val lines = input.split("\r\n")

        val results = mutableListOf<Long>()
        for (line in lines) {
            val ret = checkLine(line)
            if (ret != null) {
                results.add(ret)
            }
        }
        println(results.sorted()[results.size / 2])
    }

    private fun checkLine(line: String): Long? {
        val stack: Deque<Char> = LinkedList()

        for (i in line.chars()) {
            val c = i.toChar()
            if (chunkPairs.keys.contains(c)) {
                stack.push(c)
            } else {
                val openingChar = stack.pop()
                if (c != chunkPairs[openingChar]!!) {
                    return null
                }
            }
        }
        var sum = 0L
        while (stack.isNotEmpty()) {
            sum *= 5
            sum += scores[chunkPairs[stack.pop()]!!]!!
        }
        return sum
    }


}

