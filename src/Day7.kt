import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val memory = input.next().split(",").map { it.toInt() }.toMutableList()

    val (output, inputs) = permutations(listOf(5, 6, 7, 8, 9)).map { calcDay7(memory, it) to it }.maxBy { it.first }!!
    println(inputs)
    println(output)
}

fun calcDay7(memory: List<Int>, phases: List<Int>): Int {
    val compCount = phases.size - 1
    val threads = mutableListOf<Thread>()
    val queues = mutableListOf<BlockingQueue<Int>>()
    for (i in 0..compCount) {
        queues.add(ArrayBlockingQueue<Int>(5))
        queues[i].put(phases[i])
    }
    for (i in 0..compCount) {
        threads.add(thread { IntCompDay7(memory, queues[i], queues[(i + 1) % (compCount + 1)]).run() })
    }
    queues[0].put(0)
    for (i in 0..compCount) {
        threads[i].join()
    }
    return queues[0].take()
}

fun permutations(elements: List<Int>): List<List<Int>> {
    if (elements.size == 1) {
        return listOf(listOf(elements[0]))
    }
    val results = ArrayList<List<Int>>()
    for (current in elements) {
        val tail = ArrayList(elements)
        tail.remove(current)
        for (subResult in permutations(tail)) {
            val result = ArrayList<Int>()
            result.add(current)
            result.addAll(subResult)
            results.add(result)
        }
    }
    return results
}

class IntCompDay7(
    memoryTemplate: List<Int>,
    private val input: BlockingQueue<Int>,
    private val output: BlockingQueue<Int>
) {

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
                    memory[memory[ip + 1]] = input.take()
                    ip += 2
                }
                4 -> {
                    output.put(p(1))
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
