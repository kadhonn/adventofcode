import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day24.part1(ClassLoader.getSystemResource("day24_full.in").readText().trim())
//        Day24.part1(ClassLoader.getSystemResource("day24_example.in").readText().trim())
    })
}

object Day24 {

    fun part1(input: String) {
        var startingPositions = mutableMapOf(Pair(0L, 0L))
        val lines = input.split("\r\n").map { it.trim() }
        for (nr in 0..13) {
            val newStartingPositions = mutableMapOf<Long, Long>()
            for (startingPosition in startingPositions) {
                for (input in 1..9) {
                    val registers = mutableListOf(0L, 0L, 0L, startingPosition.key)
                    for (line in lines.subList(nr * 18, (nr + 1) * 18)) {
                        val s = line.split(" ")
                        when (s[0]) {
                            "inp" -> {
                                registers[r(s[1])] = input.toLong()
                            }
                            "add" -> {
                                registers[r(s[1])] = registers[r(s[1])] + v(registers, s[2])
                            }
                            "mul" -> {
                                registers[r(s[1])] = registers[r(s[1])] * v(registers, s[2])
                            }
                            "div" -> {
                                registers[r(s[1])] = registers[r(s[1])] / v(registers, s[2])
                            }
                            "mod" -> {
                                registers[r(s[1])] = registers[r(s[1])] % v(registers, s[2])
                            }
                            "eql" -> {
                                registers[r(s[1])] = if (registers[r(s[1])] == v(registers, s[2])) 1 else 0
                            }
                            else -> {
                                throw RuntimeException("wtf is ${s[0]}")
                            }
                        }
                    }
                    newStartingPositions.put(registers[3], startingPosition.value * 10 + input)
                }
            }
            val wantedKeys = newStartingPositions.keys.sorted().take(10000).toSet()
            startingPositions = newStartingPositions.filter { wantedKeys.contains(it.key) }.toMutableMap()
        }
        println(startingPositions[0])
    }

    private fun v(registers: MutableList<Long>, s: String): Long {
        if (s[0] in 'w'..'z') {
            return registers[r(s)]
        } else {
            return s.toLong()
        }
    }

    private fun r(registerName: String): Int {
        return registerName[0] - 'w'
    }

}