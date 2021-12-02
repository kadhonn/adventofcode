fun main() {
    Day1.doRun(ClassLoader.getSystemResource("day1_full.in").readText())
//    Day1.doRun(ClassLoader.getSystemResourceAsStream("day1_example.in").readText())
}

object Day1 {
    fun doRun(input: String) {
        val lines = input.trim().split("\r\n").filter { !it.isNullOrEmpty() }.map { it.toInt() }


        var count = 0
        var last = sum(lines, 0)

        for (i in 1..lines.size - 3) {
            val sum = sum(lines, i)
            if (sum > last) {
                count++
            }
            last = sum
        }

        println(count)
    }

    private fun sum(lines: List<Int>, i: Int): Int {
        return lines.subList(i, i + 3).sum()
    }
}