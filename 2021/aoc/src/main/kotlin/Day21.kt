import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day21.part1(ClassLoader.getSystemResource("day21_full.in").readText().trim())
//        Day21.part1(ClassLoader.getSystemResource("day21_example.in").readText().trim())
    })
}

object Day21 {

    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }
        var pos1 = lines[0].substring(28).toInt()
        var pos2 = lines[1].substring(28).toInt()
        var score1 = 0
        var score2 = 0
        var dice = 1
        var diceCount = 0

        while (score1 < 1000 && score2 < 1000) {
            var threeDice = 0
            for (i in 1..3) {
                diceCount++
                threeDice += dice++
                if (dice == 101) {
                    dice = 1
                }
            }
            pos1 += threeDice
            while (pos1 > 10) {
                pos1 -= 10
            }
            score1 += pos1
            if (score1 >= 1000) {
                break
            }

            threeDice = 0
            for (i in 1..3) {
                diceCount++
                threeDice += dice++
                if (dice == 101) {
                    dice = 1
                }
            }
            pos2 += threeDice
            while (pos2 > 10) {
                pos2 -= 10
            }
            score2 += pos2
        }
        println(diceCount * if (score2 >= 1000) score1 else score2)
    }

}

