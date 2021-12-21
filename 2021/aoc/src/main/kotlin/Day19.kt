import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day19.part1(ClassLoader.getSystemResource("day19_full.in").readText().trim())
//        Day19.part1(ClassLoader.getSystemResource("day19_example.in").readText().trim())
    })
}

object Day19 {

    fun part1(input: String) {

    }

}

