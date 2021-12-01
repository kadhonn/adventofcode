fun main() {
    Day1.doRun(ClassLoader.getSystemResourceAsStream("day1_full.in").readAllBytes().decodeToString())
}

object Day1 {
    fun doRun(input: String) {
        val lines = input.trim().split("\r\n").filter { !it.isNullOrEmpty() }.map { it.toInt() }

        var count = 0
        var last = lines[0]

        for (line in lines.drop(1)) {
            if (line > last) {
                count++
            }
            last = line
        }

        println(count)
    }
}