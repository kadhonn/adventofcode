import java.util.*
import kotlin.math.abs

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)
    input.useDelimiter("\n")

    val moons = mutableListOf<Moon>()

    while (input.hasNext()) {
        val line = input.next()
        moons.add(parseLine(line!!))
    }

    for (i in 1..1000) {
        calcVelocity(moons)
        applyVelocity(moons)
    }

    val sum = moons.map { it.kineticEnergie() * it.potentialEnergy() }
        .sum()
    println(sum)
}

fun applyVelocity(moons: MutableList<Moon>) {
    for (moon in moons) {
        moon.applyVelocity()
    }
}

fun calcVelocity(moons: MutableList<Moon>) {
    for (moon1 in moons) {
        for (moon2 in moons.filterNot { moon -> moon === moon1 }) {
            moon1.applyGravity(moon2)
        }
    }
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

data class Moon(
    var location: Vector,
    var velocity: Vector = Vector(0, 0, 0)
) {
    fun applyGravity(other: Moon) {
        velocity = velocity.add(
            calcGravityVector(other.location)
        )
    }

    private fun calcGravityVector(otherLocation: Vector): Vector {
        return Vector(
            if (otherLocation.x > location.x) 1 else if (otherLocation.x < location.x) -1 else 0,
            if (otherLocation.y > location.y) 1 else if (otherLocation.y < location.y) -1 else 0,
            if (otherLocation.z > location.z) 1 else if (otherLocation.z < location.z) -1 else 0
        )
    }

    fun applyVelocity() {
        location = location.add(velocity)
    }

    fun kineticEnergie(): Int = velocity.absoluteSum()

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
