import kotlin.streams.toList
import kotlin.system.measureTimeMillis

typealias Coord = Pair<Int, Int>

fun main() {
    val time = measureTimeMillis { Day20().run() }
    println("\ntook ms: $time")
}

class Day20 {

    private val field = String(ClassLoader.getSystemResourceAsStream("in.txt")!!.readAllBytes()).split("\n")
            .map { it.chars().toList() }
    private val coordToPortal = parseCoordToPortal()
    private val portalToCoord = flipCoordToPortalMap()

    private val alreadyVisitedCoords = mutableSetOf<Coord>()

    fun run() {
        var count = 0
        val startCoord = portalToCoord["AA"]!!.iterator().next()
        val endCoord = portalToCoord["ZZ"]!!.iterator().next()
        alreadyVisitedCoords.add(startCoord)
        var currentCoords = setOf(startCoord)
        while (currentCoords.isNotEmpty()) {
            count++
            val nextCoords = mutableSetOf<Coord>()
            for (coord in currentCoords) {
                val newCoords = getNextCoords(coord)
                for (newCoord in newCoords) {
                    if (!alreadyVisitedCoords.contains(newCoord)) {
                        if (newCoord == endCoord) {
                            println(count)
                            return
                        }
                        alreadyVisitedCoords.add(newCoord)
                        nextCoords.add(newCoord)
                    }
                }
            }
            currentCoords = nextCoords
        }
        println("fuck")
    }

    private fun getNextCoords(coord: Coord): Set<Coord> {
        val nextCoords = mutableSetOf<Coord>()
        setOf(
                Coord(coord.first + 1, coord.second),
                Coord(coord.first - 1, coord.second),
                Coord(coord.first, coord.second + 1),
                Coord(coord.first, coord.second - 1),
        ).filter { isPossibleMove(it) }.forEach { nextCoords.add(it) }
        if (coordToPortal.containsKey(coord)) {
            val portal = coordToPortal[coord]!!
            if (portal != "ZZ" && portal != "AA") {
                nextCoords.add(portalToCoord[portal]!!.filter { it != coord }.first())
            }
        }
        return nextCoords
    }

    private fun isPossibleMove(it: Pair<Int, Int>): Boolean {
        return field[it.second][it.first] == '.'.toInt()
    }

    private fun parseCoordToPortal(): Map<Coord, String> {
        val map = mutableMapOf<Coord, String>()
        for (y in 2..field.size - 2) {
            for (x in 2..field[y].size - 2) {
                if (field[y][x] == '.'.toInt()) {
                    var label: String? = null
                    if (field[y - 1][x] in 'A'.toInt()..'Z'.toInt()) {
                        label = field[y - 2][x].toChar().toString() + field[y - 1][x].toChar().toString()
                    } else if (field[y + 1][x] in 'A'.toInt()..'Z'.toInt()) {
                        label = field[y + 1][x].toChar().toString() + field[y + 2][x].toChar().toString()
                    } else if (field[y][x - 1] in 'A'.toInt()..'Z'.toInt()) {
                        label = field[y][x - 2].toChar().toString() + field[y][x - 1].toChar().toString()
                    } else if (field[y][x + 1] in 'A'.toInt()..'Z'.toInt()) {
                        label = field[y][x + 1].toChar().toString() + field[y][x + 2].toChar().toString()
                    }
                    if (label != null) {
                        map[Pair(x, y)] = label
                    }
                }
            }
        }
        return map
    }

    private fun flipCoordToPortalMap(): Map<String, Set<Coord>> {
        val map = mutableMapOf<String, MutableSet<Coord>>()
        coordToPortal.entries.forEach {
            if (!map.containsKey(it.value)) {
                map[it.value] = mutableSetOf()
            }
            map[it.value]!!.add(it.key)
        }
        return map
    }
}
