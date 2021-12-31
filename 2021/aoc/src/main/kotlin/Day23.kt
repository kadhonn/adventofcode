import java.util.*
import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day23.part1(ClassLoader.getSystemResource("day23_full.in").readText().trim())
//        Day23.part1(ClassLoader.getSystemResource("day23_example.in").readText().trim())
    })
}

object Day23 {

    private val connections = mapOf(
        Pair(0, listOf(1)),
        Pair(1, listOf(0, 2)),
        Pair(2, listOf(1, 3, 11)),
        Pair(3, listOf(2, 4)),
        Pair(4, listOf(3, 5, 13)),
        Pair(5, listOf(4, 6)),
        Pair(6, listOf(5, 7, 15)),
        Pair(7, listOf(6, 8)),
        Pair(8, listOf(7, 9, 17)),
        Pair(9, listOf(8, 10)),
        Pair(10, listOf(9)),
        Pair(11, listOf(2, 12)),
        Pair(12, listOf(11)),
        Pair(13, listOf(4, 14)),
        Pair(14, listOf(13)),
        Pair(15, listOf(6, 16)),
        Pair(16, listOf(15)),
        Pair(17, listOf(8, 18)),
        Pair(18, listOf(17)),
    )

    fun part1(input: String) {
        val field = MutableList<Char?>(19) { null }
        //example
//        field[11] = 'B'
//        field[12] = 'A'
//        field[13] = 'C'
//        field[14] = 'D'
//        field[15] = 'B'
//        field[16] = 'C'
//        field[17] = 'D'
//        field[18] = 'A'

        //my
        field[11]='B'
        field[12]='C'
        field[13]='D'
        field[14]='D'
        field[15]='C'
        field[16]='B'
        field[17]='A'
        field[18]='A'

        val alreadySeenPositions = mutableSetOf<List<Char?>>()
        val toHandlePositions =
            PriorityQueue<Triple<Int, List<Char?>, Triple<Any, Any, Any>?>>(Comparator.comparing { it.first })
        toHandlePositions.add(Triple(0, field, null))

        while (toHandlePositions.isNotEmpty()) {
            val position = toHandlePositions.poll()!!
            if (alreadySeenPositions.contains(position.second)) {
                continue
            }
            alreadySeenPositions.add(position.second)
            if (isWinning(position.second)) {
                println("found winning position in: " + position.first)
                return
            }

            val nextPositions = getNextPositions(position)
            for (newPosition in nextPositions) {
//                if (newPosition.second == getTestPosition()) {
//                    println("oha")
//                }
                toHandlePositions.add(newPosition)
            }
        }
        println("uh oh")
    }

    private fun getTestPosition(): List<Char?> {
        val field = MutableList<Char?>(19) { null }
        field[12] = 'A'
        field[9] = 'A'
        field[13] = 'B'
        field[14] = 'B'
        field[15] = 'C'
        field[16] = 'C'
        field[17] = 'D'
        field[18] = 'D'
        return field
    }

    private fun isWinning(field: List<Char?>): Boolean {
        return field[11] == 'A' && field[12] == 'A' && field[13] == 'B' && field[14] == 'B' && field[15] == 'C' && field[16] == 'C' && field[17] == 'D' && field[18] == 'D'
    }

    private fun getNextPositions(position: Triple<Int, List<Char?>, Triple<Any, Any, Any>?>): Collection<Triple<Int, List<Char?>, Triple<Any, Any, Any>?>> {
        val nextPositions = mutableListOf<Triple<Int, List<Char?>, Triple<Any, Any, Any>?>>()

        val field = position.second
        for (i in field.indices) {
            if (field[i] != null) {
                addNextPositionsForFigure(nextPositions, position, i)
            }
        }
        return nextPositions
    }

    private fun addNextPositionsForFigure(
        nextPositions: MutableList<Triple<Int, List<Char?>, Triple<Any, Any, Any>?>>,
        position: Triple<Int, List<Char?>, Triple<Any, Any, Any>?>,
        i: Int
    ) {
        val field = position.second
        val cost = getCost(field[i])
        var steps = 1

        val alreadySeenMoves = mutableSetOf(i)
        var toHandleMoves = mutableSetOf<Int>()
        toHandleMoves.addAll(connections[i]!!)
        while (toHandleMoves.isNotEmpty()) {
            val nextToHandleMoves = mutableSetOf<Int>()
            while (toHandleMoves.isNotEmpty()) {
                val nextMove = toHandleMoves.first()
                toHandleMoves.remove(nextMove)
                if (alreadySeenMoves.contains(nextMove)) {
                    continue
                }
                alreadySeenMoves.add(nextMove)

                if (field[nextMove] == null) {
                    nextToHandleMoves.addAll(connections[nextMove]!!)
                    if (isValidMove(field, i, nextMove)) {
                        val newField = field.toMutableList()
                        newField[nextMove] = newField[i]
                        newField[i] = null
                        nextPositions.add(
                            Triple(
                                position.first + steps * cost,
                                newField,
                                position as Triple<Any, Any, Any>
                            )
                        )
                    }
                }
            }
            toHandleMoves = nextToHandleMoves
            steps++
        }
    }

    private fun isValidMove(field: List<Char?>, i: Int, nextMove: Int): Boolean {
        //rule 1
        if (nextMove == 2 || nextMove == 4 || nextMove == 6 || nextMove == 8) {
            return false
        }
        //rule 2
        if (nextMove > 10) {
            val correct = when (field[i]) {
                'A' -> {
                    nextMove == 12 || (nextMove == 11 && field[12] == 'A')
                }
                'B' -> {
                    nextMove == 14 || (nextMove == 13 && field[14] == 'B')
                }
                'C' -> {
                    nextMove == 16 || (nextMove == 15 && field[16] == 'C')
                }
                'D' -> {
                    nextMove == 18 || (nextMove == 17 && field[18] == 'D')
                }
                else -> {
                    throw RuntimeException("invalid char ${field[i]}")
                }
            }
            if (!correct) {
                return false
            }
        }
        //rule 3
        if (nextMove <= 10 && i <= 10) {
            return false
        }
        //extra
        if (i > 10) {
            val incorrect = when (field[i]) {
                'A' -> {
                    i == 12 || (i == 11 && field[12] == 'A')
                }
                'B' -> {
                    i == 14 || (i == 13 && field[14] == 'B')
                }
                'C' -> {
                    i == 16 || (i == 15 && field[16] == 'C')
                }
                'D' -> {
                    i == 18 || (i == 17 && field[18] == 'D')
                }
                else -> {
                    throw RuntimeException("invalid char ${field[i]}")
                }
            }
            if (incorrect) {
                return false
            }
        }
        return true
    }

    private fun getCost(c: Char?): Int {
        return when (c) {
            'A' -> 1
            'B' -> 10
            'C' -> 100
            'D' -> 1000
            else -> {
                throw RuntimeException("invalid char $c")
            }
        }
    }

}