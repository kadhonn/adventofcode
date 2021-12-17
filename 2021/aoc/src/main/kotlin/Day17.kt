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
        val x1 = xMatch.split("..")[0].toInt()
        val x2 = xMatch.split("..")[1].toInt()
        val y1 = yMatch.split("..")[0].toInt()
        val y2 = yMatch.split("..")[1].toInt()

        val startX = findStartX(x1)
        val endX = x2
        val startY = y1
        val endY = Math.abs(y1) - 1

        var count = 0
        for (x in startX..endX) {
            for (y in startY..endY) {
                if (works(x, y, x1, x2, y1, y2)) {
                    println("x: $x y: $y")
                    count++
                }
            }
        }
        println(count)
    }

    private fun works(startX: Int, startY: Int, x1: Int, x2: Int, y1: Int, y2: Int): Boolean {
        var velX = startX
        var velY = startY
        var x = 0
        var y = 0
        while (x <= x2 && y >= y1) {
            if (x1 <= x && x <= x2 && y1 <= y && y <= y2) {
                return true
            }
            x += velX
            y += velY
            velX = Math.max(0, velX - 1)
            velY--
        }
        return false
    }


    private fun findStartX(toInt: Int): Int {
        var count = 1
        var i = 1
        while (count < toInt) {
            i++
            count += i
        }
        return i
    }

}

