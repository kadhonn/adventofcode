import java.util.*

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val map = mutableMapOf<Point, Cell>()
    var currentLocation = Point(0, 0)
    var lastHeadedDirection: Direction = Direction.NORTH
    var steps = 0
    var isReturn = false
    var openMovesStack: Deque<Step> = LinkedList()
    openMovesStack.add(Step(null))

    IntCompDay14(
        input.next().split(",").map { it.toLong() },
        {
            steps++
            if (steps % 100 == 0) {
                paint(map, currentLocation)
            }
            isReturn = false
            var nextStep: Direction? = null
            val currentStep = openMovesStack.peek()
            if (currentStep.openDirections.isEmpty()) {
                nextStep = when (currentStep.direction) {
                    Direction.NORTH -> Direction.SOUTH
                    Direction.SOUTH -> Direction.NORTH
                    Direction.WEST -> Direction.EAST
                    Direction.EAST -> Direction.WEST
                    null -> null
                }
                openMovesStack.pop()
                isReturn = true
            } else {
                nextStep = currentStep.openDirections.iterator().next()
                currentStep.openDirections.remove(nextStep)
            }
            if (nextStep == null) {
                -1
            } else {
                lastHeadedDirection = nextStep
                lastHeadedDirection.ordinal + 1L
            }
        },
        {
            when (it) {
                0L -> map[newLocation(currentLocation, lastHeadedDirection)] = Cell(CellState.WALL, steps)
                1L -> {
                    currentLocation = newLocation(currentLocation, lastHeadedDirection)
                    map[currentLocation] = Cell(CellState.AIR, steps)
                    if (!isReturn) {
                        openMovesStack.push(Step(lastHeadedDirection, allNeededDirectionsSet(currentLocation, map)))
                    }
                }
                2L -> {
                    currentLocation = newLocation(currentLocation, lastHeadedDirection)
                    map[currentLocation] = Cell(CellState.OXYGENSYSTEM, steps)
                    if (!isReturn) {
                        openMovesStack.push(Step(lastHeadedDirection, allNeededDirectionsSet(currentLocation, map)))
                    }
                }
            }
//            paint(map, currentLocation)
        }).run()

    paint(map, currentLocation)

    val flood = flood(map)
    println(flood)
}

fun flood(map: MutableMap<Point, Cell>): Int {
    var beginPoint = map.entries.filter { it.value.state == CellState.OXYGENSYSTEM }.first().key

    val alreadyVisitedPoints = mutableSetOf(beginPoint)
    var count = 0
    var currentPoints = mutableSetOf(beginPoint)

    while (true) {
        if (currentPoints.isEmpty()) {
            return count
        }
        count++
        val nextPoints = mutableSetOf<Point>()

        for (point in currentPoints) {
            for (direction in allDirectionsSet()) {
                val newPoint = newLocation(point, direction)
                if (alreadyVisitedPoints.contains(newPoint)) {
                    continue
                }
                if (map[newPoint]!!.state == CellState.AIR) {
                    nextPoints.add(newPoint)
                }
            }
        }

        alreadyVisitedPoints.addAll(nextPoints)
        currentPoints = nextPoints
    }
}

fun allNeededDirectionsSet(currentLocation: Point, map: MutableMap<Point, Cell>): MutableSet<Direction> {
    val directions = mutableSetOf<Direction>()
    if (!map.containsKey(newLocation(currentLocation, Direction.NORTH))) {
        directions.add(Direction.NORTH)
    }
    if (!map.containsKey(newLocation(currentLocation, Direction.SOUTH))) {
        directions.add(Direction.SOUTH)
    }
    if (!map.containsKey(newLocation(currentLocation, Direction.EAST))) {
        directions.add(Direction.EAST)
    }
    if (!map.containsKey(newLocation(currentLocation, Direction.WEST))) {
        directions.add(Direction.WEST)
    }
    return directions
}

data class Step(val direction: Direction?, val openDirections: MutableSet<Direction> = allDirectionsSet())

private fun allDirectionsSet() = mutableSetOf(Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH)

fun newLocation(currentLocation: Point, lastHeadedDirection: Direction): Point {
    return when (lastHeadedDirection) {
        Direction.NORTH -> Point(currentLocation.x, currentLocation.y - 1)
        Direction.SOUTH -> Point(currentLocation.x, currentLocation.y + 1)
        Direction.EAST -> Point(currentLocation.x + 1, currentLocation.y)
        Direction.WEST -> Point(currentLocation.x - 1, currentLocation.y)
    }
}

fun paint(map: Map<Point, Cell>, currentLocation: Point) {
    val minX = map.keys.map { it.x }.min()!!
    val minY = map.keys.map { it.y }.min()!!
    val maxX = map.keys.map { it.x }.max()!!
    val maxY = map.keys.map { it.y }.max()!!

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (Point(x, y) == currentLocation) {
                print("D")
            } else {
                when (map[Point(x, y)]?.state) {
                    CellState.AIR -> print(".")
                    CellState.OXYGENSYSTEM -> print("*")
                    CellState.WALL -> print("#")
                    null -> print(" ")
                }
            }
        }
        println()
    }
    println()
    println()
    println()
    println()
}

data class Cell(
    val state: CellState,
    val steps: Int
)

enum class CellState {
    WALL,
    AIR,
    OXYGENSYSTEM
}

enum class Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST
}

class IntCompDay14(
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
