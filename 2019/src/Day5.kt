import java.util.*

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val memory = input.next().split(",").map { it.toInt() }.toMutableList()

    IntCompDay5(memory).run()
}

class IntCompDay5(memoryTemplate: MutableList<Int>) {

    var ip = 0
    var memory = ArrayList(memoryTemplate)
    var modes: List<Int> = ArrayList()

    fun run() {
        while (true) {
            calcModes()
            when (memory[ip] % 100) {
                1 -> {
                    memory[memory[ip + 3]] = p(1) + p(2)
                    ip += 4
                }
                2 -> {
                    memory[memory[ip + 3]] = p(1) * p(2)
                    ip += 4
                }
                3 -> {
                    memory[memory[ip + 1]] = 5
                    ip += 2
                }
                4 -> {
                    println(p(1))
                    ip += 2
                }
                5 -> {
                    if (p(1) != 0) {
                        ip = p(2)
                    } else {
                        ip += 3
                    }
                }
                6 -> {
                    if (p(1) == 0) {
                        ip = p(2)
                    } else {
                        ip += 3
                    }
                }
                7 -> {
                    if (p(1) < p(2)) {
                        memory[memory[ip + 3]] = 1
                    } else {
                        memory[memory[ip + 3]] = 0
                    }
                    ip += 4
                }
                8 -> {
                    if (p(1) == p(2)) {
                        memory[memory[ip + 3]] = 1
                    } else {
                        memory[memory[ip + 3]] = 0
                    }
                    ip += 4
                }
                99 ->
                    return
                else -> throw IllegalStateException("illegal opcode ${memory[ip]} at position $ip")
            }
        }
    }

    private fun p(parameterNumber: Int): Int {
        val memoryIndex = ip + parameterNumber
        when (modes[parameterNumber - 1]) {
            0 -> {
                return memory[memory[memoryIndex]]
            }
            1 -> {
                return memory[memoryIndex]
            }
        }
        throw java.lang.IllegalStateException("illegal mode ${modes[memoryIndex]}")
    }

    private fun calcModes() {
        val instruction = memory[ip]
        modes = listOf((instruction / 100) % 10, (instruction / 1000) % 10, (instruction / 10000) % 10)
    }

}
