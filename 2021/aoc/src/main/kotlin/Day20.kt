import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day20.part1(ClassLoader.getSystemResource("day20_full.in").readText().trim())
//        Day20.part1(ClassLoader.getSystemResource("day20_example.in").readText().trim())
    })
}

object Day20 {

    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }

        var algo = ""
        var i = 0
        while (lines[i].isNotBlank()) {
            algo += lines[i++]
        }

        var map = mutableMapOf<Pair<Int, Int>, Char>()
        var y = 0
        i++
        var min = 0
        var max = lines[i].length - 1
        while (i < lines.size) {
            for (x in lines[i].indices) {
                map[Pair(x, y)] = lines[i][x]
            }
            y++
            i++
        }


        var unknown = '.'
        for (i in 1..50) {
            val newMap = mutableMapOf<Pair<Int, Int>, Char>()
            min--
            max++

            for (x in min..max) {
                for (y in min..max) {
                    var n = 0
                    for (j in y - 1..y + 1) {
                        for (i in x - 1..x + 1) {
                            n = n.shl(1) + if (map.getOrDefault(Pair(i, j), unknown) == '.') 0 else 1
                        }
                    }
                    newMap[Pair(x, y)] = algo[n]
                }
            }

            unknown = if (unknown == '.') algo[0] else algo[511]
            map = newMap
        }

        println(map.values.filter { it == '#' }.count())
    }

}

