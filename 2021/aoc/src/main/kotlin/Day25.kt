import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day25.part1(ClassLoader.getSystemResource("day25_full.in").readText().trim())
//        Day25.part1(ClassLoader.getSystemResource("day25_example.in").readText().trim())
    })
}

object Day25 {

    fun part1(input: String) {
        val cucumbers = input.split("\r\n").map { it.toCharArray().toMutableList() }

        var count = 0
        var moved = true
        while (moved) {
            moved = false
            for (y in cucumbers.indices) {
                val firstOne = cucumbers[y][0]
                val lastOne = cucumbers[y][cucumbers[y].size - 1]
                var x = 0
                while (x < cucumbers[y].size - 1) {
                    val nextX = x + 1
                    if (cucumbers[y][x] == '>' && cucumbers[y][nextX] == '.') {
                        cucumbers[y][x] = '.'
                        cucumbers[y][nextX] = '>'
                        x++
                        moved = true
                    }
                    x++
                }
                if (lastOne == '>' && firstOne == '.') {
                    cucumbers[y][cucumbers[y].size - 1] = '.'
                    cucumbers[y][0] = '>'
                    moved = true
                }
            }
            for (x in cucumbers[0].indices) {
                val firstOne = cucumbers[0][x]
                val lastOne = cucumbers[cucumbers.size - 1][x]
                var y = 0
                while (y < cucumbers.size - 1) {
                    val nextY = y + 1
                    if (cucumbers[y][x] == 'v' && cucumbers[nextY][x] == '.') {
                        cucumbers[y][x] = '.'
                        cucumbers[nextY][x] = 'v'
                        y++
                        moved = true
                    }
                    y++
                }
                if (lastOne == 'v' && firstOne == '.') {
                    cucumbers[cucumbers.size - 1][x] = '.'
                    cucumbers[0][x] = 'v'
                    moved = true
                }
            }
            count++
//            println()
//            println(count)
//            println(cucumbers.toString().replace("], [", "]\n[").replace("[", "").replace("]", "").replace(",", ""))
        }
        println(count)
    }

}