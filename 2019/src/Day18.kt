import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis { Day18().run() }
    println("\ntook ms: $time")
}

class Day18 {
    val field = parseField()
    val allKeys = findAllKeys()

    val alreadyFoundStates = mutableSetOf<State>()

    fun run() {
        val startCoord = findAndRemoveStartCoord()
        val startState = State(startCoord, emptySet())
        alreadyFoundStates.add(startState)
        var currentStates = setOf(startState)
        var steps = 1

        while (currentStates.isNotEmpty()) {
            var nextStates = mutableSetOf<State>()

            currentStates.forEach { state ->
                for (neighbour in getPossibleNeighbours(state)) {
                    if (!alreadyFoundStates.contains(neighbour)) {
                        if (hasWon(neighbour)) {
                            println(steps)
                            return
                        }
                        alreadyFoundStates.add(neighbour)
                        nextStates.add(neighbour);
                    }
                }
            }

            currentStates = nextStates
            steps++
        }
    }

    private fun getPossibleNeighbours(state: State): Set<State> {
        var coord = state.coord

        return setOf(
                State(Coord(coord.x + 1, coord.y), state.keys),
                State(Coord(coord.x - 1, coord.y), state.keys),
                State(Coord(coord.x, coord.y + 1), state.keys),
                State(Coord(coord.x, coord.y - 1), state.keys)
        )
                .filter { isPossibleMove(it) }
                .map { updateKeys(it) }
                .toSet()
    }

    private fun updateKeys(state: State): State {
        val currentField = field[state.coord.y][state.coord.x]

        if (currentField in 'a'..'z') {
            return State(state.coord, state.keys.toMutableSet().union(setOf(currentField)))
        }
        return state
    }

    private fun isPossibleMove(state: State): Boolean {
        return when (val currentField = field[state.coord.y][state.coord.x]) {
            '.', '@' -> {
                true
            }
            '#' -> {
                false;
            }
            in 'a'..'z' -> {
                true
            }
            in 'A'..'Z' -> {
                state.keys.contains(currentField.toLowerCase())
            }
            else -> throw IllegalStateException()
        }
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

    private fun findAndRemoveStartCoord(): Coord {
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
        val input = String(ClassLoader.getSystemResourceAsStream("in")!!.readAllBytes())

        return input.split("\n").map {
            it.toCharArray().toList()
        }
    }

    data class State(val coord: Coord, val keys: Set<Char>)

    data class Coord(val x: Int, val y: Int)
}