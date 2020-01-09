import java.util.*
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis { Day16.run() }
    println("\ntook ms: $time")
}

object Day16 {
    fun run() {
        val input =
            Scanner(ClassLoader.getSystemResourceAsStream("in2")!!).nextLine().toList().map { it.toString().toInt() }
        val signal = mutableListOf<Int>()
        for (i in 1..10000) {
            signal.addAll(input)
        }

        val offset = signal.take(7).joinToString(separator = "") { it.toString() }.toInt()

        for (i in 1..100) {
            applyFFT(offset, signal)
        }

        for (i in offset..offset + 7) {
            print(signal[i])
        }
    }

    private fun applyFFT(offset: Int, signal: MutableList<Int>) {
        var sum = 0L
        for (i in signal.size - 1 downTo offset) {
            sum += signal[i]
            signal[i] = (sum.absoluteValue % 10).toInt()
        }
    }
}