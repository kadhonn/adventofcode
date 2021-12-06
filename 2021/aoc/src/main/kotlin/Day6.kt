fun main() {
    Day6.part1(ClassLoader.getSystemResource("day6_full.in").readText())
//    Day6.part1(ClassLoader.getSystemResource("day6_example.in").readText())
}

object Day6 {

    fun part1(input: String) {
        val fishCount = Array(9) { 0L }

        input.trim().split(",").map { it.toInt() }.forEach {
            fishCount[it]++
        }

        for (day in 1..256 ) {
            val newFishes = fishCount[0]
            for (i in 1 until fishCount.size) {
                fishCount[i - 1] = fishCount[i]
            }
            fishCount[6] += newFishes
            fishCount[8] = newFishes
        }

        println(fishCount.sum())
    }

}

