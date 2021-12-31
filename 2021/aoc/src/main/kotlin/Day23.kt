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
        Pair(4, listOf(3, 5, 15)),
        Pair(5, listOf(4, 6)),
        Pair(6, listOf(5, 7, 19)),
        Pair(7, listOf(6, 8)),
        Pair(8, listOf(7, 9, 23)),
        Pair(9, listOf(8, 10)),
        Pair(10, listOf(9)),

        Pair(11, listOf(2, 12)),
        Pair(12, listOf(11, 13)),
        Pair(13, listOf(12, 14)),
        Pair(14, listOf(13)),

        Pair(15, listOf(4, 16)),
        Pair(16, listOf(15, 17)),
        Pair(17, listOf(16, 18)),
        Pair(18, listOf(17)),

        Pair(19, listOf(6, 20)),
        Pair(20, listOf(19, 21)),
        Pair(21, listOf(20, 22)),
        Pair(22, listOf(21)),

        Pair(23, listOf(8, 24)),
        Pair(24, listOf(23, 25)),
        Pair(25, listOf(24, 26)),
        Pair(26, listOf(25)),
    )

    fun part1(input: String) {
        val field = MutableList<Char?>(27) { null }
        //example
//        field[11] = 'B'
//        field[12] = 'D'
//        field[13] = 'D'
//        field[14] = 'A'
//
//        field[15] = 'C'
//        field[16] = 'C'
//        field[17] = 'B'
//        field[18] = 'D'
//
//        field[19] = 'B'
//        field[20] = 'B'
//        field[21] = 'A'
//        field[22] = 'C'
//
//        field[23] = 'D'
//        field[24] = 'A'
//        field[25] = 'C'
//        field[26] = 'A'

        //my
        field[11] = 'B'
        field[12] = 'D'
        field[13] = 'D'
        field[14] = 'C'

        field[15] = 'D'
        field[16] = 'C'
        field[17] = 'B'
        field[18] = 'D'

        field[19] = 'C'
        field[20] = 'B'
        field[21] = 'A'
        field[22] = 'B'

        field[23] = 'A'
        field[24] = 'A'
        field[25] = 'C'
        field[26] = 'A'

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
                printHierarchy(position)
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

    private fun printHierarchy(position: Triple<Int, List<Char?>, Triple<Any, Any, Any>?>) {
        if (position.third != null) {
            printHierarchy(position.third as Triple<Int, List<Char?>, Triple<Any, Any, Any>?>)
        }
        printField(position)
    }

    private fun printField(position: Triple<Int, List<Char?>, Triple<Any, Any, Any>?>) {
        println("cost: ${position.first}")
        val field = position.second
        for (i in 0..10) {
            print(field[i] ?: '.')
        }
        println()
        for (i in 0..3) {
            print(" ")
            for (j in 0..3) {
                print(" ")
                print(field[11 + i + 4 * j] ?: '.')
            }
            println()
        }
    }

    private fun getTestPosition(): List<Char?> {
        val field = MutableList<Char?>(27) { null }
        field[0] = 'A'
        field[14] = 'A'
        field[21] = 'A'
        field[26] = 'A'

        field[11] = 'B'
        field[17] = 'B'
        field[19] = 'B'
        field[20] = 'B'

        field[15] = 'C'
        field[16] = 'C'
        field[22] = 'C'
        field[25] = 'C'

        field[10] = 'D'
        field[12] = 'D'
        field[13] = 'D'
        field[18] = 'D'
        return field
    }

    private fun isWinning(field: List<Char?>): Boolean {
        return field[11] == 'A' && field[12] == 'A' && field[13] == 'A' && field[14] == 'A' &&
                field[15] == 'B' && field[16] == 'B' && field[17] == 'B' && field[18] == 'B' &&
                field[19] == 'C' && field[20] == 'C' && field[21] == 'C' && field[22] == 'C' &&
                field[23] == 'D' && field[24] == 'D' && field[25] == 'D' && field[26] == 'D'
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
//                                null
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
                    nextMove in 11..14 && (11..14).filter { field[it] != 'A' && field[it] != null }.isEmpty()
                }
                'B' -> {
                    nextMove in 15..18 && (15..18).filter { field[it] != 'B' && field[it] != null }.isEmpty()
                }
                'C' -> {
                    nextMove in 19..22 && (19..22).filter { field[it] != 'C' && field[it] != null }.isEmpty()
                }
                'D' -> {
                    nextMove in 23..26 && (23..26).filter { field[it] != 'D' && field[it] != null }.isEmpty()
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
                    i in 11..14 && (11..14).filter { field[it] != 'A' && field[it] != null }.isEmpty()
                }
                'B' -> {
                    i in 15..18 && (15..18).filter { field[it] != 'B' && field[it] != null }.isEmpty()
                }
                'C' -> {
                    i in 19..22 && (19..22).filter { field[it] != 'C' && field[it] != null }.isEmpty()
                }
                'D' -> {
                    i in 23..26 && (23..26).filter { field[it] != 'D' && field[it] != null }.isEmpty()
                }
                else -> {
                    throw RuntimeException("invalid char ${field[i]}")
                }
            }
            if (incorrect) {
                return false
            }
        }
        //extra 2
        if (nextMove > 10) {
            val c = field[i]
            val start = when (c) {
                'A' -> {
                    14
                }
                'B' -> {
                    18
                }
                'C' -> {
                    22
                }
                'D' -> {
                    26
                }
                else -> {
                    throw RuntimeException("invalid char $c")
                }
            }
            for (end in start downTo nextMove + 1) {
                if (field[end] != c) {
                    return false
                }
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