import java.lang.Math.max
import java.lang.Math.min
import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day22.part1(ClassLoader.getSystemResource("day22_full.in").readText().trim())
//        Day22.part1(ClassLoader.getSystemResource("day22_example.in").readText().trim())
    })
}

object Day22 {

    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }

        val reactor = Array(101) {
            Array(101) {
                Array(101) { false }
            }
        }

        for (line in lines) {
            val splitted = line.split(" ")
            val on = splitted[0] == "on"
            val splittedCoord = splitted[1].split(",")
            val minX = splittedCoord[0].substring(2).split("..")[0].toInt()
            val maxX = splittedCoord[0].substring(2).split("..")[1].toInt()
            val minY = splittedCoord[1].substring(2).split("..")[0].toInt()
            val maxY = splittedCoord[1].substring(2).split("..")[1].toInt()
            val minZ = splittedCoord[2].substring(2).split("..")[0].toInt()
            val maxZ = splittedCoord[2].substring(2).split("..")[1].toInt()

            for (x in max(-50, minX)..min(50, maxX)) {
                for (y in max(-50, minY)..min(50, maxY)) {
                    for (z in max(-50, minZ)..min(50, maxZ)) {
                        reactor[x + 50][y + 50][z + 50] = on
                    }
                }
            }

        }

        var count = 0
        for (x in reactor.indices) {
            for (y in reactor[x].indices) {
                for (z in reactor[x][y].indices) {
                    if (reactor[x][y][z]) {
                        count++
                    }
                }
            }
        }
        println(count)
    }
}