import java.lang.Math.abs

fun main() {
    Day5.part1(ClassLoader.getSystemResource("day5_full.in").readText())
//    Day5_1.part1(ClassLoader.getSystemResource("day5_example.in").readText())
}

object Day5 {

    fun part1(input: String) {
        val fields = mutableMapOf<Pair<Int, Int>, Int>()

        input.trim()
            .split("\r\n")
            .map { it.split(" -> ") }
            .map { Pair(it[0], it[1]) }
            .map {
                val first = it.first.split(",")
                val second = it.second.split(",")
                Pair(
                    Pair(first[0].toInt(), first[1].toInt()),
                    Pair(second[0].toInt(), second[1].toInt()),
                )
            }
//            .filter { it.first.first == it.second.first || it.first.second == it.second.second } // lol, for part 1 just enable this line
            .forEach {
                var xDiff = it.second.first - it.first.first
                var yDiff = it.second.second - it.first.second
                if(xDiff != 0) xDiff /= abs(xDiff)
                if(yDiff != 0) yDiff /= abs(yDiff)
                var x = it.first.first
                var y = it.first.second
                fields.merge(Pair(x, y), 1, Int::plus)
                do {
                    x += xDiff
                    y += yDiff

                    fields.merge(Pair(x, y), 1, Int::plus)
                }while (x != it.second.first || y != it.second.second)
            }

        println(fields
            .filter { it.value > 1 }
            .count())
    }

}

