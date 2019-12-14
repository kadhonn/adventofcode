import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.collections.ArrayList

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val memory = input.next().split(",").map { it.toLong() }.toMutableList()

    val inputQueue = ArrayBlockingQueue<Long>(200)
    inputQueue.put(2)
    val outputQueue = ArrayBlockingQueue<Long>(200)
    IntCompDay9(memory, inputQueue, outputQueue).run()
    while (!outputQueue.isEmpty()) println(outputQueue.take())
}

class IntCompDay9(
    memoryTemplate: List<Long>,
    private val input: BlockingQueue<Long>,
    private val output: BlockingQueue<Long>
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
                    memory[pos(1)] = input.take()
                    ip += 2
                }
                4L -> {
                    output.put(para(1))
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
