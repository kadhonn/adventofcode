fun main() {
    Day9.part1(ClassLoader.getSystemResource("day9_full.in").readText().trim())
//    Day9.part1(ClassLoader.getSystemResource("day9_example.in").readText().trim())
}

object Day9 {

    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }

        val basins = mutableListOf<Int>()
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (isLowSpot(lines, x, y)) {
                    basins.add(calcBasinSize(lines, x, y))
                }
            }
        }
        basins.sortDescending()
        println(basins.take(3).fold(1, Int::times))
    }

    private fun calcBasinSize(lines: List<String>, startX: Int, startY: Int): Int {
        val donePoints = mutableSetOf<Pair<Int, Int>>()
        val toDoPoints = mutableSetOf<Pair<Int, Int>>()
        toDoPoints.add(Pair(startX, startY))
        var count = 0
        while (toDoPoints.isNotEmpty()) {
            val currentPoint = toDoPoints.iterator().next()
            val x = currentPoint.first
            val y = currentPoint.second
            toDoPoints.remove(currentPoint)
            donePoints.add(currentPoint)
            count++

            for (i in Math.max(y - 1, 0)..Math.min(y + 1, lines.size - 1)) {
                if (lines[i][x].digitToInt() != 9) {
                    val newPoint = Pair(x, i)
                    if (!donePoints.contains(newPoint)) {
                        toDoPoints.add(newPoint)
                    }
                }
            }
            for (j in Math.max(x - 1, 0)..Math.min(x + 1, lines[y].length - 1)) {
                if (lines[y][j].digitToInt() != 9) {
                    val newPoint = Pair(j, y)
                    if (!donePoints.contains(newPoint)) {
                        toDoPoints.add(newPoint)
                    }
                }
            }
        }
        return count
    }

    private fun isLowSpot(lines: List<String>, x: Int, y: Int): Boolean {
        val current = lines[y][x].digitToInt()
        for (i in Math.max(y - 1, 0)..Math.min(y + 1, lines.size - 1)) {
            if (i != y && lines[i][x].digitToInt() <= current) {
                return false
            }
        }
        for (j in Math.max(x - 1, 0)..Math.min(x + 1, lines[y].length - 1)) {
            if (j != x && lines[y][j].digitToInt() <= current) {
                return false
            }
        }
        return true
    }


}

