import java.lang.Math.min

fun main() {
    Day3.part2(ClassLoader.getSystemResource("day3_full.in").readText())
//    Day3.part2(ClassLoader.getSystemResource("day3_example.in").readText())
}

object Day3 {
    fun part1(input: String) {
        val lines = input.trim().split("\r\n")

        var gamma = 0L
        var epsilon = 0L

        for (i in 0 until lines[0].length) {
            var oneCount = 0
            for (j in lines.indices) {
                if (lines[j][i] == '1') {
                    oneCount++
                }
            }
            gamma = gamma.shl(1)
            epsilon = epsilon.shl(1)
            if (oneCount >= lines.size / 2) {
                gamma = gamma.or(1L)
            } else {
                epsilon = epsilon.or(1L)
            }
        }

        println(gamma * epsilon)


    }

    fun part2(input: String) {
        val lines = input.trim().split("\r\n").sorted()

        val oxygen = find(lines, 0, lines.size - 1, 0, true)
        val co2 = find(lines, 0, lines.size - 1, 0, false)
        println(oxygen * co2)
    }

    private fun find(lines: List<String>, start: Int, end: Int, depth: Int, oxygen: Boolean): Long {
        if (start == end) {
            return lines[start].toLong(2)
        }
        var oneCount = 0
        var firstOne = end
        for (j in start..end) {
            if (lines[j][depth] == '1') {
                oneCount++
                firstOne = min(firstOne, j)
            }
        }
        if ((oneCount.toFloat() >= (end - start + 1) .toFloat() / 2F) == oxygen) {
            //1
            return find(lines, firstOne, end, depth + 1, oxygen)
        } else {
            //0
            return find(lines, start, firstOne - 1, depth + 1, oxygen)
        }
    }
}

