import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis { Day19().run() }
    println("\ntook ms: $time")
}

class Day19 {

    private val input = String(ClassLoader.getSystemResourceAsStream("in.txt")!!.readAllBytes()).split(",").map { it.toLong() }
    fun run() {

        var x = 0L
        var y = 0L
        var count = 0

        while (y < 50) {
            val result = eval(x, y)

            if (result == 1L) {
                count++
                print('#')
            } else {
                print('.')
            }
            x++
            if (x == 50L) {
                println()
                y++
                x = 0
            }
        }

        println(count)
    }

    private fun eval(x: Long, y: Long): Long {
        var result = 0L
        var readXNext = true
        IntCompDay19(input,
                {
                    if (readXNext) {
                        readXNext = false
                        x
                    } else {
                        y
                    }
                },
                {
                    result = it
                }).run()
        return result
    }

}


class IntCompDay19(
        memoryTemplate: List<Long>,
        private val input: () -> Long,
        private val output: (Long) -> Unit
) {

    var ip = 0L
    var rBO = 0L
    var memory = memoryTemplate.mapIndexed { index, e -> index.toLong() to e }.toMap().toMutableMap()
    var modes: List<Long> = ArrayList()

    fun run() {
        while (true) {
            calcModes()
            when (memory[ip]!! % 100) {
                1L -> {
                    memory[pos(3)] = para(1) + para(2)
                    ip += 4
                }
                2L -> {
                    memory[pos(3)] = para(1) * para(2)
                    ip += 4
                }
                3L -> {
                    memory[pos(1)] = input()
                    ip += 2
                }
                4L -> {
                    output(para(1))
                    ip += 2
                }
                5L -> {
                    if (para(1) != 0L) {
                        ip = para(2)
                    } else {
                        ip += 3
                    }
                }
                6L -> {
                    if (para(1) == 0L) {
                        ip = para(2)
                    } else {
                        ip += 3
                    }
                }
                7L -> {
                    if (para(1) < para(2)) {
                        memory[pos(3)] = 1
                    } else {
                        memory[pos(3)] = 0
                    }
                    ip += 4
                }
                8L -> {
                    if (para(1) == para(2)) {
                        memory[pos(3)] = 1
                    } else {
                        memory[pos(3)] = 0
                    }
                    ip += 4
                }
                9L -> {
                    rBO += para(1)
                    ip += 2
                }
                99L ->
                    return
                else -> throw IllegalStateException("illegal opcode ${memory[ip]} at position $ip")
            }
        }
    }

    private fun pos(parameterNumber: Long): Long {
        val memoryIndex = ip + parameterNumber
        when (modes[(parameterNumber - 1).toInt()]) {
            0L -> {
                return memory[memoryIndex]!!
            }
            1L -> {
                return memoryIndex
            }
            2L -> {
                return memory[memoryIndex]!! + rBO
            }
        }
        throw java.lang.IllegalStateException("illegal mode ${modes[memoryIndex.toInt()]}")
    }

    private fun para(parameterNumber: Long): Long {
        return memory.getOrDefault(pos(parameterNumber), 0)
    }

    private fun calcModes() {
        val instruction = memory[ip]!!
        modes = listOf((instruction / 100) % 10, (instruction / 1000) % 10, (instruction / 10000) % 10)
    }

}