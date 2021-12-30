import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day21.part1(ClassLoader.getSystemResource("day21_full.in").readText().trim())
//        Day21.part1(ClassLoader.getSystemResource("day21_example.in").readText().trim())
    })
}

object Day21 {

    data class Game(
        val pos1: Int,
        val pos2: Int,
        val curPlayerIsFirst: Boolean = true,
        val score1: Int = 0,
        val score2: Int = 0
    )

    val cache = mutableMapOf<Game, Pair<Long, Long>>()
    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }
        val pos1 = lines[0].substring(28).toInt()
        val pos2 = lines[1].substring(28).toInt()

        val game = Game(pos1, pos2)
        val wins = findWins(game)
        println(wins)
        if (wins.first > wins.second) {
            println(wins.first)
        } else {
            println(wins.second)
        }
    }

    private val scoreNeeded = 21
    private fun findWins(game: Game): Pair<Long, Long> {
        if (cache.containsKey(game)) {
            return cache.get(game)!!
        }
        if (game.score1 >= scoreNeeded) {
            return Pair(1, 0)
        } else if (game.score2 >= scoreNeeded) {
            return Pair(0, 1)
        }
        var wins = Pair(0L, 0L)
        for (i in 1..3) {
            for (j in 1..3) {
                for (k in 1..3) {
                    val afterMoveGame = move(game, i + j + k)
                    val afterMoveWins = findWins(afterMoveGame)
                    wins = Pair(wins.first + afterMoveWins.first, wins.second + afterMoveWins.second)
                }
            }
        }
        cache.put(game, wins)
        return wins
    }

    private fun move(game: Game, i: Int): Game {
        val pos1 = if (!game.curPlayerIsFirst) game.pos1 else (game.pos1 - 1 + i) % 10 + 1
        val score1 = if (!game.curPlayerIsFirst) game.score1 else game.score1 + pos1
        val pos2 = if (game.curPlayerIsFirst) game.pos2 else (game.pos2 - 1 + i) % 10 + 1
        val score2 = if (game.curPlayerIsFirst) game.score2 else game.score2 + pos2
        return Game(
            pos1, pos2, !game.curPlayerIsFirst, score1, score2
        )
    }

}

