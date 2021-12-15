fun main() {
    Day15.part1(ClassLoader.getSystemResource("day15_full.in").readText().trim())
//    Day15.part1(ClassLoader.getSystemResource("day15_example.in").readText().trim())
}

object Day15 {

    fun part1(input: String) {
        val field = input.split("\r\n").map {
            it
                .trim()
                .toCharArray()
                .map { it.toString().toInt() }
                .toTypedArray()
        }.toTypedArray()

        val end = Pair(field.size * 5 - 1, field[0].size * 5 - 1)

        val reachedPoints = mutableSetOf<Pair<Int, Int>>()
        val reachablePoints = mutableListOf<Triple<Int, Int, Int>>()

        reachablePoints.add(Triple(0, 0, 0))

        while (reachablePoints.isNotEmpty()) {
            val min = reachablePoints.minByOrNull { it.third }!!
            reachablePoints.remove(min)
            val coords = Pair(min.first, min.second)
            if (reachedPoints.contains(coords)) {
                continue
            }
            reachedPoints.add(coords)

            if (coords == end) {
                println(min.third)
                return
            }

            val nextPossiblePoints = listOf(
                Pair(coords.first + 1, coords.second),
                Pair(coords.first - 1, coords.second),
                Pair(coords.first, coords.second + 1),
                Pair(coords.first, coords.second - 1)
            )

            nextPossiblePoints.filter {
                it.first >= 0 && it.first < field.size * 5 && it.second >= 0 && it.second < field[0].size * 5
            }.forEach {
                reachablePoints.add(Triple(it.first, it.second, min.third + getCost(field, it.first, it.second)))
            }
        }

        println("oh no")
    }

    private fun getCost(field: Array<Array<Int>>, row: Int, col: Int): Int {
        val origCost = field[row % field.size][col % field[0].size]

        return ((origCost + (row / field.size) + (col / field[0].size)) - 1) % 9 + 1
    }

}

