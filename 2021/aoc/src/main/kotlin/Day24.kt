import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day24.part1(ClassLoader.getSystemResource("day24_full.in").readText().trim())
//        Day24.part1(ClassLoader.getSystemResource("day24_example.in").readText().trim())
    })
}

object Day24 {

    /*
    inp w
mul x 0		x = 0
add x z		x = z
mod x 26	x = z % 26
div z 1		z = z
add x 14	x = x + 14
eql x w
eql x 0		x = 1 if x != w
mul y 0		y = 0
add y 25	y = 25
mul y x		y = y * 0/1
add y 1		y = y + 1
mul z y		z = z * 1/26
mul y 0		y = 0
add y w		y = w
add y 8		y = w + 8
mul y x		y = y * 0/1
add z y		z = z + y



x = z % 26 + ?

z = z / 26 || z
if x == w nothing changes
else z = z * 26 + w + ?



     */
    fun part1(input: String) {
        var startingPositions = mutableMapOf(Pair(0L, 0L))
        val lines = input.split("\r\n").map { it.trim() }
        for (nr in 0..13) {
            val newStartingPositions = mutableMapOf<Long, Long>()
            for (startingPosition in startingPositions) {
                for (input in 9 downTo 1) {
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
            val wantedKeys = newStartingPositions.keys.sorted().take(20000).toSet()
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