import java.util.regex.Pattern

fun main() {
    Day4.part1(ClassLoader.getSystemResource("day4_full.in").readText())
//    Day4.part1(ClassLoader.getSystemResource("day4_example.in").readText())
}

object Day4 {

    const val FIELD_SIZE = 5

    fun part1(input: String) {
        val lines = input.trim().split("\r\n")

        val numbers = lines[0].split(",").map { it.toInt() }
        val fields = mutableListOf<Pair<MutableSet<Int>, MutableList<MutableSet<Int>>>>()
        var i = 2
        while (i < lines.size) {
            fields.add(parseField(lines, i))
            i += FIELD_SIZE + 1
        }

        for (number in numbers) {
            for (field in fields) {
                field.first.remove(number)
                for (set in field.second) {
                    set.remove(number)
                    if (set.isEmpty()) {
                        println(field.first.sum() * number)
                        return
                    }
                }
            }
        }
    }

    private fun parseField(lines: List<String>, startRow: Int): Pair<MutableSet<Int>, MutableList<MutableSet<Int>>> {
        val fieldNumbers = mutableSetOf<Int>()
        val numberSets = mutableListOf<MutableSet<Int>>()
        val cols = mutableListOf<MutableSet<Int>>()
        for (i in 1..FIELD_SIZE) {
            cols.add(mutableSetOf())
        }
        for (i in startRow until (startRow + FIELD_SIZE)) {
            val rowNumbers = lines[i].trim().split(Pattern.compile(" +")).map { it.toInt() }
            val row = mutableSetOf<Int>()
            for (j in 0 until FIELD_SIZE) {
                val curNum = rowNumbers[j]

                fieldNumbers.add(curNum)
                row.add(curNum)
                cols[j].add(curNum)
            }
            numberSets.add(row)
        }
        numberSets.addAll(cols)
        return Pair(fieldNumbers, numberSets)
    }

    fun part2(input: String) {
    }
}

