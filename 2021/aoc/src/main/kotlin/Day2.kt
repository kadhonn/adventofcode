fun main() {
    Day2.part2(ClassLoader.getSystemResource("day2_full.in").readText())
//    Day2.part2(ClassLoader.getSystemResource("day2_example.in").readText())
}

object Day2 {
    fun part1(input: String) {
        var (position, depth) = input.trim().split("\r\n")
            .map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }
            .fold(Pair(0, 0)) { position, movement ->
                when (movement.first) {
                    "forward" -> Pair(position.first + movement.second, position.second)
                    "up" -> Pair(position.first, position.second - movement.second)
                    "down" -> Pair(position.first, position.second + movement.second)
                    else -> throw RuntimeException("wat")
                }
            }

        println(position * depth)
    }

    fun part2(input: String) {
        var (position, depth) = input.trim().split("\r\n")
            .map { it.split(" ") }
            .map { Pair(it[0], it[1].toInt()) }
            .fold(Triple(0, 0, 0)) { position, movement ->
                when (movement.first) {
                    "forward" -> Triple(position.first + movement.second, position.second + movement.second * position.third, position.third)
                    "up" -> Triple(position.first, position.second, position.third - movement.second)
                    "down" -> Triple(position.first, position.second, position.third + movement.second)
                    else -> throw RuntimeException("wat")
                }
            }

        println(position * depth)
    }

}