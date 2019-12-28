import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val inputQueue = ArrayBlockingQueue<Long>(2)
    val outputQueue = ArrayBlockingQueue<Long>(2)
    val thread = thread {
        IntCompDay9(input.next().split(",").map { it.toLong() }.toList(), inputQueue, outputQueue).run()
    }

    val game = mutableMapOf<Point, Long>()
    try {
        while (true) {
            val x = outputQueue.poll(1, TimeUnit.SECONDS)!!
            val y = outputQueue.take()
            val tile = outputQueue.take()
            game[Point(x,y)] = tile
        }
    } catch (e: Exception) {
        //we are finished!
    }

    println(game.values.filter { it == 2L }.count())


}

data class Point(
    val x: Long,
    val y: Long
)
