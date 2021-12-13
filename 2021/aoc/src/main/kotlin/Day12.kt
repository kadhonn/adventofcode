fun main() {
    Day12.part1(ClassLoader.getSystemResource("day12_full.in").readText().trim())
//    Day12.part1(ClassLoader.getSystemResource("day12_example.in").readText().trim())
}

object Day12 {

    fun part1(input: String) {
        val network = input.split("\r\n").fold(mutableMapOf<String, MutableSet<String>>()) { network, connection ->
            addConnection(network, connection)
        }

        val paths = mutableSetOf<List<String>>()

        recursivePath(network, paths, mutableListOf("start"), "start")

        println(paths.size)
    }

    private fun recursivePath(
        network: MutableMap<String, MutableSet<String>>,
        paths: MutableSet<List<String>>,
        currentPath: MutableList<String>,
        currentNode: String
    ) {
        for (node in network[currentNode]!!) {
            if (node != "start" &&
                (node[0].isUpperCase() || currentPath.groupBy { it }
                    .filter { it.key[0].isLowerCase() && it.value.size == 2 }
                    .count() == 0 || !currentPath.contains(node))
            ) {
                currentPath.add(node)
                if (node == "end") {
                    paths.add(currentPath.toList())
                } else {
                    recursivePath(network, paths, currentPath, node)
                }
                currentPath.removeAt(currentPath.size - 1)
            }
        }
    }

    private fun addConnection(
        network: MutableMap<String, MutableSet<String>>,
        connection: String
    ): MutableMap<String, MutableSet<String>> {
        val parts = connection.split("-")
        addConnection(network, parts[0], parts[1])
        addConnection(network, parts[1], parts[0])
        return network
    }

    private fun addConnection(network: MutableMap<String, MutableSet<String>>, from: String, to: String) {
        if (!network.containsKey(from)) {
            network.put(from, mutableSetOf())
        }
        network[from]!!.add(to)
    }


}

