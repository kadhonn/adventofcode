import kotlin.system.measureTimeMillis

fun main() {
    println("time: " + measureTimeMillis {
        Day22.part1(ClassLoader.getSystemResource("day22_full.in").readText().trim())
//        Day22.part1(ClassLoader.getSystemResource("day22_example.in").readText().trim())
    })
}

object Day22 {
    class Region(
        val on: Boolean,
        val dimens: Array<Array<Int>>//[[minX, maxX],[minY, maxY],[minZ, maxZ]]
    ) {
        fun copy(): Region {
            return Region(
                on,
                arrayOf(
                    arrayOf(dimens[0][0], dimens[0][1]),
                    arrayOf(dimens[1][0], dimens[1][1]),
                    arrayOf(dimens[2][0], dimens[2][1])
                )
            )
        }
    }

    fun part1(input: String) {
        val lines = input.split("\r\n").map { it.trim() }
            .map { parseLine(it) }

        var regions = mutableListOf<Region>()
        for (line in lines) {
            regions = removeAllIntersections(regions, line)
            if (line.on) {
                regions.add(line)
            }
        }

        var count = 0L
        for (region in regions) {
            count +=
                (region.dimens[0][1].toLong() - region.dimens[0][0].toLong() + 1L) * (region.dimens[1][1].toLong() - region.dimens[1][0].toLong() + 1L) * (region.dimens[2][1].toLong() - region.dimens[2][0].toLong() + 1L)
        }
        println(count)
    }

    private fun removeAllIntersections(regions: List<Region>, newRegion: Region): MutableList<Region> {
        val newRegions = mutableListOf<Region>()
        for (region in regions.toList()) {
            newRegions.addAll(intersect(region, newRegion))
        }
        return newRegions
    }

    private fun intersect(region: Region, newRegion: Region): Collection<Region> {
        val remainingRegions = mutableListOf<Region>()

        val regionToSplit = region.copy()
        for (dim in 0..2) {
            val oldMin = regionToSplit.dimens[dim][0]
            val oldMax = regionToSplit.dimens[dim][1]
            val newMin = newRegion.dimens[dim][0]
            val newMax = newRegion.dimens[dim][1]

            if (newMax < oldMin || newMin > oldMax) {
                //no overlap happened
                return listOf(region)
            }

            if (newMin > oldMin) {
                val remainingRegion = regionToSplit.copy()
                remainingRegion.dimens[dim][1] = newMin - 1
                remainingRegions.add(remainingRegion)
                regionToSplit.dimens[dim][0] = newMin
            }

            if (newMax < oldMax) {
                val remainingRegion = regionToSplit.copy()
                remainingRegion.dimens[dim][0] = newMax + 1
                remainingRegions.add(remainingRegion)
                regionToSplit.dimens[dim][1] = newMax
            }
        }

        return remainingRegions
    }

    private fun parseLine(line: String): Region {
        val splitted = line.split(" ")
        val on = splitted[0] == "on"
        val splittedCoord = splitted[1].split(",")
        val minX = splittedCoord[0].substring(2).split("..")[0].toInt()
        val maxX = splittedCoord[0].substring(2).split("..")[1].toInt()
        val minY = splittedCoord[1].substring(2).split("..")[0].toInt()
        val maxY = splittedCoord[1].substring(2).split("..")[1].toInt()
        val minZ = splittedCoord[2].substring(2).split("..")[0].toInt()
        val maxZ = splittedCoord[2].substring(2).split("..")[1].toInt()
        return Region(on, arrayOf(arrayOf(minX, maxX), arrayOf(minY, maxY), arrayOf(minZ, maxZ)))
    }
}