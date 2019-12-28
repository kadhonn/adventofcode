import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)
    input.useDelimiter("\n")

    var moonsInit = mutableListOf<Moon>()
    val pastXes = HashSet<List<Pair<Int, Int>>>()
    val pastYes = HashSet<List<Pair<Int, Int>>>()
    val pastZes = HashSet<List<Pair<Int, Int>>>()
    var xesFound = false
    var yesFound = false
    var zesFound = false

    while (input.hasNext()) {
        val line = input.next()
        moonsInit.add(parseLine(line!!))
    }

    var moons = moonsInit.toList()

    var time = 0L
    while (!xesFound || !yesFound || !zesFound) {
        val newUniverse = Universe(moons)
        val xes = newUniverse.extract { it.x }
        val yes = newUniverse.extract { it.y }
        val zes = newUniverse.extract { it.z }
        if (!xesFound && pastXes.contains(xes)) {
            xesFound = true
            println("$time, ")
        }
        if (!yesFound && pastYes.contains(yes)) {
            yesFound = true
            println("$time, ")
        }
        if (!zesFound && pastZes.contains(zes)) {
            zesFound = true
            println("$time, ")
        }
        pastXes.add(xes)
        pastYes.add(yes)
        pastZes.add(zes)
        moons = calcVelocity(moons)
        moons = applyVelocity(moons)
        time++
    }

}

fun applyVelocity(moons: List<Moon>): List<Moon> = moons.map { it.applyVelocity() }

fun calcVelocity(moons: List<Moon>): List<Moon> = moons.map { moon1 ->
    moons.filterNot { it === moon1 }.fold(moon1) { a, b -> a.applyGravity(b) }
}

fun parseLine(line: String): Moon {
    val result = Regex("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>").find(line)!!
    return Moon(
        Vector(
            result.groupValues[1].toInt(),
            result.groupValues[2].toInt(),
            result.groupValues[3].toInt()
        )
    )
}

data class Universe(val moons: List<Moon>) {
    fun extract(extractor: (Vector) -> Int): List<Pair<Int, Int>> {
        return moons.map {
            Pair(extractor(it.location), extractor(it.velocity))
        }
    }
}

data class Moon(
    val location: Vector,
    val velocity: Vector = Vector(0, 0, 0)
) {
    fun applyGravity(other: Moon): Moon {
        return Moon(
            location,
            velocity.add(
                calcGravityVector(other.location)
            )
        )
    }

    private fun calcGravityVector(otherLocation: Vector): Vector {
        return Vector(
            if (otherLocation.x > location.x) 1 else if (otherLocation.x < location.x) -1 else 0,
            if (otherLocation.y > location.y) 1 else if (otherLocation.y < location.y) -1 else 0,
            if (otherLocation.z > location.z) 1 else if (otherLocation.z < location.z) -1 else 0
        )
    }

    fun applyVelocity(): Moon {
        return Moon(location.add(velocity), velocity)
    }

    fun kineticEnergy(): Int = velocity.absoluteSum()

    fun potentialEnergy(): Int = location.absoluteSum()

}

data class Vector(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun add(other: Vector): Vector {
        return Vector(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    fun absoluteSum(): Int {
        return abs(x) + abs(y) + abs(z)
    }
}
