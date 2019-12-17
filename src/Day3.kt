import java.util.*

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val field = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    val crosses = mutableSetOf<Int>()

    var cableNr = 0
    while (input.hasNext()) {
        var x = 0
        var y = 0
        val steps = input.next().split(",")
        var stepCount = 0
        for (step in steps) {
            val dir = step.substring(0, 1)
            val count = step.substring(1).toInt()
            for (i in 0 until count) {
                stepCount++
                when (dir) {
                    "R" -> x++
                    "L" -> x--
                    "U" -> y++
                    "D" -> y--
                }
                if (field.containsKey(Pair(x, y)) && field[Pair(x, y)]!!.first != cableNr) {
                    crosses.add(stepCount  + field[Pair(x, y)]!!.second)
                } else {
                    field[Pair(x, y)] = Pair(cableNr, stepCount )
                }
            }
        }
        cableNr++
    }

    val min = crosses.min()
    println(min)
}
