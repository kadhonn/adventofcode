import kotlin.math.abs
import kotlin.math.atan2

fun main() {
    Day10.run()
}

object Day10 {
    fun run() {
        val input = String(ClassLoader.getSystemResourceAsStream("in2")!!.readAllBytes())

        val field = input.split("\n")
            .map { it.toCharArray() }
            .map { it.map { it == '#' } }
            .map { it.toList() }
            .toList()

        val coords = calc(field, 11, 11)

        println(coords)
    }

    private fun calc(field: List<List<Boolean>>, x: Int, y: Int): Pair<Int, Int> {
        val excluded = field.indices.flatMap { i -> field[i].indices.map { j -> Pair(i, j) } }
            .filter { field[it.first][it.second] }
            .filter { it.first != x || it.second != y }
            .sortedBy { abs(it.first - x) + abs(it.second - y) }
            .fold(mutableSetOf<Pair<Int, Int>>(), { excluded, nearestPoint ->
                if (!excluded.contains(nearestPoint)) {
                    val (dx, dy) = shrink(nearestPoint.first - x, nearestPoint.second - y)
                    var movingX = nearestPoint.first + dx
                    var movingY = nearestPoint.second + dy
                    while (movingX >= 0 && movingX < field.size && movingY >= 0 && movingY < field[movingX].size) {
                        if (field[movingX][movingY]) {
                            excluded.add(Pair(movingX, movingY))
                        }
                        movingX += dx
                        movingY += dy
                    }
                }
                excluded
            })

        return field.indices.flatMap { i -> field[i].indices.map { j -> Pair(i, j) } }
            .filter { field[it.first][it.second] }
            .filter { it.first != x || it.second != y }
            .filter { !excluded.contains(it) }
            .sortedBy { angle(Pair(x, y), it) }
            .reversed()
            .map { println(it);it }[199]
    }

    private fun angle(us: Pair<Int, Int>, asteroid: Pair<Int, Int>): Double {
        return atan2(asteroid.second.toDouble() - us.second, asteroid.first.toDouble() - us.first)
    }

    private val primes = listOf(2, 3, 5, 7, 11, 13, 17, 19, 21)

    private fun shrink(initDx: Int, initDy: Int): Pair<Int, Int> {
        var dx = initDx
        var dy = initDy
        var primeIndex = 0
        while (primes[primeIndex] <= abs(dx) || primes[primeIndex] <= abs(dy)) {
            if (dx % primes[primeIndex] == 0 && dy % primes[primeIndex] == 0) {
                dx /= primes[primeIndex]
                dy /= primes[primeIndex]
            } else {
                primeIndex++
            }
        }
        return Pair(dx, dy)
    }
}
