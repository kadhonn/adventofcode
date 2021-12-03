fun main() {
    Day3.part1(ClassLoader.getSystemResource("day3_full.in").readText())
//    Day3.part1(ClassLoader.getSystemResource("day3_example.in").readText())
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

    }

}