import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis {
        Day14.part1(ClassLoader.getSystemResource("day14_full.in").readText().trim())
        //    Day14.part1(ClassLoader.getSystemResource("day14_example.in").readText().trim())
    })
}

object Day14 {

    val mappings = mutableMapOf<String, Char>()
    val cache = mutableMapOf<Triple<Int, Char, Char>, Map<Char, Long>>()
    fun part1(input: String) {
        val lines = input.split("\r\n")
        val template = lines[0]

        var counts = mutableMapOf<Char, Long>()
        lines.drop(2).forEach {
            val splitted = it.trim().split(" -> ")
            mappings.put(splitted[0], splitted[1][0])
            counts[splitted[1][0]] = 0
        }

        template.chars().forEach { counts[it.toChar()] = counts[it.toChar()]!! + 1 }

        for (i in 0..template.length - 2) {
            val newCounts = recursiveCount(template[i], template[i + 1], 0)
            counts = mergeMap(counts, newCounts)
        }

        val max = counts.values.maxOrNull()!!
        val min = counts.values.minOrNull()!!
        println(max - min)
    }

    val MAX_DEPTH = 40
    private fun recursiveCount(first: Char, second: Char, depth: Int): Map<Char, Long> {
        if (depth == MAX_DEPTH) {
            return emptyMap()
        }
        val triple = Triple(depth, first, second)
        if (cache.containsKey(triple)) {
            return cache[triple]!!
        }
        val newElement = mappings[first.toString() + second.toString()]!!

        val firstCount = recursiveCount(first, newElement, depth + 1)
        val secondCount = recursiveCount(newElement, second, depth + 1)

        val mergeMap = mergeMap(mergeMap(firstCount, secondCount), mapOf(Pair(newElement, 1)))
        cache.put(triple, mergeMap)
        return mergeMap
    }

    private fun mergeMap(counts: Map<Char, Long>, newCounts: Map<Char, Long>): MutableMap<Char, Long> {
        val map = mutableMapOf<Char, Long>()

        map.putAll(counts)

        newCounts.forEach {
            map.merge(it.key, it.value, Long::plus)
        }

        return map
    }
}

