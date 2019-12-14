import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun main() {
    val input = Scanner(ClassLoader.getSystemResourceAsStream("in2")!!)

    val map = HashMap<String, Pair<String, MutableCollection<String>>>()
    while (input.hasNext()) {
        val string = input.next()
        val orbitee = string.split(")")[0]
        val orbiter = string.split(")")[1]
        var orbiterPair = map.getOrDefault(orbiter, Pair(orbitee, ArrayList()))
        orbiterPair = Pair(orbitee, orbiterPair.second)
        map.put(orbiter, orbiterPair)
        val orbiteePair = map.getOrDefault(orbitee, Pair("", ArrayList()))
        orbiteePair.second.add(orbiter)
        map.put(orbitee, orbiteePair)
    }
    var arrayList = ArrayList<Any>()

    var currentPlanets = HashSet<String>()
    currentPlanets.add(map["YOU"]!!.first)
    val seenPlanets = HashSet<String>()
    val end = map["SAN"]!!.first
    var count = 0
    while (!currentPlanets.contains(end)) {
        seenPlanets.addAll(currentPlanets)
        count++
        val nextPlanets = HashSet<String>()
        for (planet in currentPlanets) {
            if (map.containsKey(planet)) {
                nextPlanets.add(map[planet]!!.first)
                nextPlanets.addAll(map[planet]!!.second)
            }
        }
        nextPlanets.removeAll(seenPlanets)
        currentPlanets = nextPlanets
    }
    println(count)
}