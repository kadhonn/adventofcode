import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis { Day18().run() }
    println("\ntook ms: $time")
}

class Day18 {
    private val field = parseField()
    private val allKeys = findAllKeys()

    private val alreadyFoundStates = mutableSetOf<State>()

    fun run() {
        val startCoord = findStartCoord()
        val startState = State(listOf(
                Robo(Coord(startCoord.x - 1, startCoord.y - 1), countKeys(0, 0, startCoord.x - 1, startCoord.y - 1)),
                Robo(Coord(startCoord.x + 1, startCoord.y + 1), countKeys(startCoord.x + 1, startCoord.y + 1, field[0].size - 1, field.size - 1)),
                Robo(Coord(startCoord.x - 1, startCoord.y + 1), countKeys(0, startCoord.y + 1, startCoord.x - 1, field.size - 1)),
                Robo(Coord(startCoord.x + 1, startCoord.y - 1), countKeys(startCoord.x + 1, 0, field[0].size - 1, startCoord.y - 1))
        ), emptySet(), 0)
        alreadyFoundStates.add(startState)
        var currentStates = setOf(startState)
        var steps = 1

        while (currentStates.isNotEmpty()) {
            if (steps % 50 == 0) {
                println("steps: " + steps + " currentStates: " + currentStates.size)
            }
//            if (steps == 73) {
//            printAllStates(steps, currentStates)
//                val max = currentStates.map { it.keys.size }.maxOrNull()
//                val maxStates = currentStates.filter { it.keys.size == max }
//                printAllStates(steps, maxStates.toSet())
//                throw java.lang.IllegalStateException()
//            }
            val nextStates = mutableSetOf<State>()

            currentStates.forEach { state ->
                for (nextState in getNextStates(state)) {
                    if (!alreadyFoundStates.contains(nextState)) {
                        if (hasWon(nextState)) {
                            println(steps)
                            return
                        }
                        alreadyFoundStates.add(nextState)
                        nextStates.add(nextState)
                    }
                }
            }

            currentStates = nextStates
            steps++
        }
        println("fuck")
    }

    private fun printAllStates(step: Int, currentStates: Set<State>) {
        println("Step: " + step)
        println()
        for (state in currentStates) {
            printState(state)
            println()
        }
        println()
        println()
    }

    private fun printState(state: State) {
        println("Keys: " + state.keys.sorted().joinToString(""))
        println("Remaining Key Counts: " + state.robos.map { it.remainingKeyCount }.joinToString(","))
        for (y in field.indices) {
            for (x in field[y].indices) {
                var toPrint = field[y][x]
                for (i in state.robos.indices) {
                    if (state.robos[i].coord.x == x && state.robos[i].coord.y == y) {
                        toPrint = if (i == state.currentRobo) {
                            '@'
                        } else {
                            i.toString()[0]
                        }
                        break
                    }
                }
                print(toPrint)
            }
            println()
        }
    }

    private fun countKeys(startX: Int, startY: Int, endX: Int, endY: Int): Int {
        var count = 0
        for (y in startY..endY) {
            for (x in startX..endX) {
                if (field[y][x] in 'a'..'z') {
                    count++
                }
            }
        }
        return count
    }

    private fun getNextStates(state: State): Set<State> {
        if (state.currentRobo == -1) {
            return emptySet()
        }
        val currentRobo = state.robos[state.currentRobo]
        val coord = currentRobo.coord

        val allMoves = setOf(
                Coord(coord.x + 1, coord.y),
                Coord(coord.x - 1, coord.y),
                Coord(coord.x, coord.y + 1),
                Coord(coord.x, coord.y - 1)
        )
        val possibleNextStates = mutableSetOf<State>()
        for (move in allMoves) {
            if (!isPossibleMove(move)) {
                continue
            }
            val nextState = applyNewMove(state, move)

            if (nextState != null) {
                possibleNextStates.add(nextState)
            }
        }
        return possibleNextStates
    }

    private fun applyNewMove(state: State, move: Coord): State? {
        val robos = state.robos.toMutableList()
        var remainingKeyCount = robos[state.currentRobo].remainingKeyCount
        val keys = state.keys.toMutableSet()

        val currentField = field[move.y][move.x]

        if (currentField in 'a'..'z' && !keys.contains(currentField)) {
            keys.add(currentField)
            remainingKeyCount--
        }
        robos[state.currentRobo] = Robo(move, remainingKeyCount)

        var currentRobo: Int = state.currentRobo
        if (remainingKeyCount == 0 || (currentField in 'A'..'Z' && !state.keys.contains(currentField.toLowerCase()))) {
            currentRobo = findNextRobo(robos, currentRobo, keys)
        }
        return State(robos, keys, currentRobo)
    }

    private fun findNextRobo(robos: List<Robo>, currentRobo: Int, keys: Set<Char>): Int {
        for (i in 1..robos.size) {
            val robo = robos[(currentRobo + i) % robos.size]
            if (canMove(robo, keys)) {
                return (currentRobo + i) % robos.size
            }
        }
        return -1
    }

    private fun canMove(robo: Robo, keys: Set<Char>): Boolean {
        if (robo.remainingKeyCount == 0) {
            return false
        }
        val currentField = field[robo.coord.y][robo.coord.x]
        if (currentField in 'A'..'Z') {
            return keys.contains(currentField.toLowerCase())
        }
        return true
    }

    private fun isPossibleMove(coord: Coord): Boolean {
        return field[coord.y][coord.x] != '#'
    }

    private fun hasWon(state: State): Boolean {
        return state.keys.size == allKeys.size
    }

    private fun findAllKeys(): Set<Char> {
        val keys = mutableSetOf<Char>()
        field.forEach { list ->
            list.forEach { c ->
                if (c in 'a'..'z') {
                    keys.add(c);
                }
            }
        }
        return keys
    }

    private fun findStartCoord(): Coord {
        field.forEachIndexed { y, list ->
            list.forEachIndexed { x, c ->
                if (c == '@') {
                    return Coord(x, y);
                }
            }
        }
        throw IllegalStateException()
    }

    private fun parseField(): List<List<Char>> {
        val input = String(ClassLoader.getSystemResourceAsStream("in.txt")!!.readAllBytes())

        return input.split("\n").map {
            it.toCharArray().toList()
        }
    }

    data class State(val robos: List<Robo>, val keys: Set<Char>, val currentRobo: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (robos != other.robos) return false
            if (keys != other.keys) return false

            return true
        }

        override fun hashCode(): Int {
            var result = robos.hashCode()
            result = 31 * result + keys.hashCode()
            return result
        }
    }

    data class Robo(val coord: Coord, val remainingKeyCount: Int)

    data class Coord(val x: Int, val y: Int)
}