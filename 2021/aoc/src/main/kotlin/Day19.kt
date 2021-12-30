import java.lang.Math.max
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day19.part1(ClassLoader.getSystemResource("day19_full.in").readText().trim())
//        Day19.part1(ClassLoader.getSystemResource("day19_example.in").readText().trim())
    })
}

object Day19 {

    private val COMPARATOR = Comparator.comparingInt<Vector> { it.x }.thenComparingInt { it.y }
        .thenComparingInt { it.z }

    data class Vector(val x: Int, val y: Int, val z: Int) : Comparable<Vector> {
        constructor() : this(0, 0, 0)

        override fun toString(): String {
            return "($x,$y,$z)"
        }

        fun turn(): Vector {
            return Vector(-y, x, z)
        }

        fun roll(): Vector {
            return Vector(x, z, -y)
        }

        fun minus(vector: Vector): Vector {
            return Vector(x - vector.x, y - vector.y, z - vector.z)
        }

        fun plus(vector: Vector): Vector {
            return Vector(x + vector.x, y + vector.y, z + vector.z)
        }

        fun reverse(): Vector {
            return Vector(-x, -y, -z)
        }

        override fun compareTo(other: Vector): Int {
            return COMPARATOR.compare(this, other)
        }

        fun manhattan(other: Vector): Int {
            return abs(other.x - x) + abs(other.y - y) + abs(other.z - z)
        }
    }

    data class RotatableVector(val vector: Vector, val rotation: Int = 0) {
        fun getAllRotations(): List<RotatableVector> {
            val allRotations = mutableListOf(this)
            var rotated = this.rotate()
            while (rotated.rotation != this.rotation) {
                allRotations.add(rotated)
                rotated = rotated.rotate()
            }
            return allRotations
        }

        fun rotateTo(newRotation: Int): RotatableVector {
            var rotated = this
            while (rotated.rotation != newRotation) {
                rotated = rotated.rotate()
            }
            return rotated
        }

        fun rotate(): RotatableVector {
            var newVector = if (rotation % 4 == 0) {
                vector.roll()
            } else {
                vector.turn()
            }
            newVector = if ((rotation + 1) % 12 == 0) {
                newVector.roll().turn().roll()
            } else {
                newVector
            }
            return RotatableVector(newVector, (rotation + 1) % 24)
        }

        fun getMinimalRotation(): RotatableVector {
            return getAllRotations()
                .minByOrNull { it.vector }!!
        }
    }

    class Scanner(beaconVectors: List<Vector>) {
        var offset: Vector? = null
        var rotation: Int? = null
        val beacons: List<Beacon>
        var distances: List<Distance>

        init {
            this.beacons = createBeacons(beaconVectors)
            this.distances = calcDistances()
        }

        private fun calcDistances(): List<Distance> {
            val distances = mutableListOf<Distance>()
            for (from in beacons) {
                for (to in beacons) {
                    if (from == to) {
                        continue
                    }
                    distances.add(calcDistance(from, to))
                }
            }
            return distances
        }

        private fun calcDistance(from: Beacon, to: Beacon): Distance {
            val vector = to.rotatableVector.vector.minus(from.rotatableVector.vector)
            val minimalVector = RotatableVector(vector).getMinimalRotation()
            return Distance(from, vector, minimalVector.vector)
        }

        private fun createBeacons(beaconVectors: List<Vector>): List<Beacon> {
            val beacons = mutableListOf<Beacon>()
            for (vector in beaconVectors) {
                beacons.add(Beacon(RotatableVector(vector), this))
            }
            return beacons
        }

        fun matched() {
            for (beacon in beacons) {
                beacon.getAbsolutePosition()
            }
            distances = calcDistances()
        }
    }

    class Beacon(var rotatableVector: RotatableVector, val scanner: Scanner) {
        fun getAbsolutePosition(): Vector {
            rotatableVector = rotatableVector.rotateTo(scanner.rotation!!)
            return rotatableVector.vector.plus(scanner.offset!!)
        }
    }

    data class Distance(val from: Beacon, val origDistance: Vector, val distance: Vector) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Distance

            if (distance != other.distance) return false

            return true
        }

        override fun hashCode(): Int {
            return distance.hashCode()
        }
    }

    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }
        val scanners = parseScanners(lines)
        matchScanners(scanners)
        countBeacons(scanners)
        printMaxDistance(scanners)
    }

    private fun printMaxDistance(scanners: Map<Int, Scanner>) {
        var max = 0
        for (s1 in scanners.values) {
            for (s2 in scanners.values) {
                max = max(max, s1.offset!!.manhattan(s2.offset!!))
            }
        }
        println(max)
    }

    private fun countBeacons(scanners: Map<Int, Scanner>) {
        val positions = mutableSetOf<Vector>()
        for (scanner in scanners.values) {
            for (beacon in scanner.beacons) {
                positions.add(beacon.getAbsolutePosition())
            }
        }
        println(positions.size)
    }

    private fun matchScanners(scanners: Map<Int, Scanner>) {
        val doneScanners = mutableSetOf(0)
        val readyScanners = mutableSetOf(0)
        val scannerZero = scanners[0]!!
        scannerZero.rotation = 0
        scannerZero.offset = Vector(0, 0, 0)
        while (readyScanners.isNotEmpty()) {
            val nextScannerNumber = readyScanners.first()
            val nextScanner = scanners[nextScannerNumber]!!
            readyScanners.remove(nextScannerNumber)

            if (nextScanner.rotation == null) {
                throw RuntimeException("trying from from not matched scanner: $nextScannerNumber")
            }
            for (scannerNumber in scanners.keys) {
                if (nextScannerNumber != scannerNumber && !doneScanners.contains(scannerNumber)) {
                    val scanner = scanners[scannerNumber]!!
                    if (scanner.rotation != null) {
                        throw RuntimeException("trying to from already matched scanner: $scannerNumber")
                    }
//                    println("trying to match scanners $nextScannerNumber and $scannerNumber")
                    if (tryMatch(nextScanner, scanner)) {
//                        println("matched scanners $nextScannerNumber and $scannerNumber")
                        readyScanners.add(scannerNumber)
                        doneScanners.add(scannerNumber)
                    }
                }
            }
        }
        if (doneScanners.size != scanners.size) {
            throw RuntimeException("uh no, did not match all scanners")
        }
    }

    private fun tryMatch(from: Scanner, to: Scanner): Boolean {
        val matches = getCommonDistances(from, to)
        if (matches.size < 11 * 12 / 2) {
            return false
        }
        for (match in matches) {
            val fromMatch = match.first
            val toMatch = match.second
            val matchingRotations = getAllMatchingRotations(fromMatch, toMatch)
            for (rotation in matchingRotations) {
                if (tryMatchRotation(fromMatch.from, toMatch.from, rotation)) {
                    return true
                }
            }
        }
        return false
    }

    private fun tryMatchRotation(from: Beacon, to: Beacon, rotation: Int): Boolean {
        val fromScanner = from.scanner
        val toScanner = to.scanner
        toScanner.rotation = rotation
        toScanner.offset =
            from.getAbsolutePosition().plus(to.rotatableVector.rotateTo(rotation).vector.reverse())

        var count = 0

        outer@ for (fromBeacon in fromScanner.beacons) {
            for (toBeacon in toScanner.beacons) {
                if (fromBeacon.getAbsolutePosition() == toBeacon.getAbsolutePosition()) {
                    count++
                    continue@outer
                }
            }
        }

        val result = count >= 12
        if (!result) {
            toScanner.rotation = null
            toScanner.offset = null
        } else {
            toScanner.matched()
        }

        return result
    }

    private fun getAllMatchingRotations(fromMatch: Distance, toMatch: Distance): List<Int> {
        return RotatableVector(toMatch.origDistance).getAllRotations()
            .filter { it.vector == fromMatch.origDistance }
            .map { it.rotation }
    }

    private fun getCommonDistances(from: Scanner, to: Scanner): List<Pair<Distance, Distance>> {
        val commonDistances = mutableListOf<Pair<Distance, Distance>>()
        for (distance in from.distances) {
            for (toDistance in to.distances.filter { it == distance }) {
                commonDistances.add(Pair(distance, toDistance))
            }
        }
        return commonDistances
    }

    private fun parseScanners(lines: List<String>): Map<Int, Scanner> {
        val scanners = mutableMapOf<Int, Scanner>()
        var currentScannerNumber = parseScannerNumber(lines[0])
        var currentScannerBeacons = mutableListOf<Vector>()
        for (line in lines.drop(1)) {
            if (line.isEmpty()) {
                continue
            }
            if (line.contains("scanner")) {
                scanners.put(currentScannerNumber, Scanner(currentScannerBeacons))
                currentScannerBeacons = mutableListOf()
                currentScannerNumber = parseScannerNumber(line)
            } else {
                currentScannerBeacons.add(parseVector(line))
            }
        }
        scanners.put(currentScannerNumber, Scanner(currentScannerBeacons))
        return scanners
    }

    private fun parseVector(s: String): Vector {
        val splitted = s.split(",")
        return Vector(splitted[0].toInt(), splitted[1].toInt(), splitted[2].toInt())
    }

    private fun parseScannerNumber(s: String): Int {
        return s.substring(12, s.length - 4).toInt()
    }

}

