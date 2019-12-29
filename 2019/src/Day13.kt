import java.util.*

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val outputList = mutableListOf<Long>()
    val game = mutableMapOf<Point, Long>()
    var score = 0L

    IntCompDay13(
        input.next().split(",").map { it.toLong() }.toMutableList().also { it[0] = 2 },
        {
            //                paint(game)
            try {
                val ballPosition = game.entries.filter { it.value == 4L }.first().key
                val paddlePosition = game.entries.filter { it.value == 3L }.first().key
//                sleep(200)
                if (ballPosition.x < paddlePosition.x) -1L else if (ballPosition.x > paddlePosition.x) 1L else 0L
            } catch (e: Exception) {
//                    paint(game)
//                    println(score)
                e.printStackTrace()
                0
            }
        },
        {
            outputList.add(it)
            if (outputList.size == 3) {
                val x = outputList[0]
                val y = outputList[1]
                val tile = outputList[2]
                if (x == -1L && y == 0L) {
                    score = tile
                } else {
                    game[Point(x, y)] = tile
                }
                outputList.clear()
            }
        }).run()
    paint(game)
    println(score)
}

fun paint(game: Map<Point, Long>) {
    val minX = game.keys.map { it.x }.min()!!
    val minY = game.keys.map { it.y }.min()!!
    val maxX = game.keys.map { it.x }.max()!!
    val maxY = game.keys.map { it.y }.max()!!

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            when (game[Point(x, y)]) {
                0L -> print(" ")
                1L -> print("|")
                2L -> print("#")
                3L -> print("_")
                4L -> print("o")
            }
        }
        println()
    }
    println()
    println()
    println()
    println()
}

data class Point(
    val x: Long,
    val y: Long
)


class IntCompDay13(
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
