fun main() {
    Day14.part1(ClassLoader.getSystemResource("day14_full.in").readText().trim())
//    Day14.part1(ClassLoader.getSystemResource("day14_example.in").readText().trim())
}

object Day14 {

    val counts = mutableMapOf<Char, Int>()
    val mappings = mutableMapOf<String, Char>()
    fun part1(input: String) {
        val lines = input.split("\r\n")
        val template = lines[0]

        lines.drop(2).forEach {
            val splitted = it.trim().split(" -> ")
            mappings.put(splitted[0], splitted[1][0])
            counts[splitted[1][0]] = 0
        }

        template.chars().forEach { counts[it.toChar()] = counts[it.toChar()]!! + 1 }

        for (i in 0..template.length - 2) {
            recursiveCount(template[i], template[i + 1], 0)
        }

        val max = counts.values.maxOrNull()!!
        val min = counts.values.minOrNull()!!
        println(max - min)
    }

    val MAX_DEPTH = 10
    private fun recursiveCount(first: Char, second: Char, depth: Int) {
        if (depth == MAX_DEPTH) {
            return
        }
        val newElement = mappings[first.toString() + second.toString()]!!
        counts[newElement] = counts[newElement]!! + 1

        recursiveCount(first, newElement, depth + 1)
        recursiveCount(newElement, second, depth + 1)
    }

}

