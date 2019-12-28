import java.util.*
import kotlin.math.abs

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)
    input.useDelimiter("\n")

    var moonsInit = mutableListOf<Moon>()
    val pastUniverses = mutableSetOf<Universe>()

    while (input.hasNext()) {
        val line = input.next()
        moonsInit.add(parseLine(line!!))
    }

    var moons = moonsInit.toList()

    var time = 0L
    while (true) {
        val newUniverse = Universe(moons)
        if (pastUniverses.contains(newUniverse)) {
            break
        }
        pastUniverses.add(newUniverse)
        moons = calcVelocity(moons)
        moons = applyVelocity(moons)
        time++
        if (time % 100000L == 0L) {
            println(time)
        }
    }

    println(time)
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

data class Universe(val moons: List<Moon>)

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
