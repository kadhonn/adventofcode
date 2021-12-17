import java.lang.Math.min
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day17.part1(ClassLoader.getSystemResource("day17_full.in").readText().trim())
//        Day17.part1(ClassLoader.getSystemResource("day17_example.in").readText().trim())
    })
}

object Day17 {
    val PATTERN = Pattern.compile("""target area: x=(-?\d+\.\.-?\d+), y=(-?\d+\.\.-?\d+)""")

    fun part1(input: String) {
        val matcher = PATTERN.matcher(input)
        matcher.matches()
        val xMatch = matcher.group(1)
        val yMatch = matcher.group(2)
        val y1 = yMatch.split("..")[0].toInt()
        val y2 = yMatch.split("..")[1].toInt()

        val yMin = min(y1, y2)
        println(((Math.abs(yMin)-1) downTo 1).sum())
    }

}

