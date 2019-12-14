import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val memoryTemplate = input.next().split(",").map { it.toInt() }.toMutableList()

    for (i in 0 until 99) {
        for (j in 0 until 99) {
            val memory = ArrayList(memoryTemplate)
            memory[1] = i
            memory[2] = j
            runProgram(memory)
            if (memory[0] == 19690720) {
                println(100 * i + j)
                return
            }
        }
    }
}

fun runProgram(memory: MutableList<Int>) {
    var ip = 0
    while (true) {
        when (memory[ip]) {
            1 -> {
                memory[memory[ip + 3]] = memory[memory[ip + 1]] + memory[memory[ip + 2]]
                ip += 4
            }
            2 -> {
                memory[memory[ip + 3]] = memory[memory[ip + 1]] * memory[memory[ip + 2]]
                ip += 4
            }
            99 ->
                return
            else -> throw IllegalStateException("illegal opcode ${memory[ip]} at position $ip")
        }
    }
}
