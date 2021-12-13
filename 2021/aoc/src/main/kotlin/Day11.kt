fun main() {
    Day11.part1(ClassLoader.getSystemResource("day11_full.in").readText().trim())
//    Day11.part1(ClassLoader.getSystemResource("day11_example.in").readText().trim())
}

object Day11 {


    fun part1(input: String) {
        val lines = input.split("\r\n")
        val field = Array(10) { row ->
            Array(10) { col ->
                lines[row][col].digitToInt()
            }
        }


        var count = 1
        while (true) {
            val turn = turn(field)
            if (turn == 100) {
                break
            }
            count++
        }
        println(count)
    }

    private fun turn(field: Array<Array<Int>>): Int {
        val flashes = mutableSetOf<Pair<Int, Int>>()

        for (row in 0..9) {
            for (col in 0..9) {
                field[row][col]++
                if (field[row][col] == 10) {
                    flashes.add(Pair(row, col))
                }
            }
        }

        while (flashes.isNotEmpty()) {
            val flashing = flashes.iterator().next()
            flashes.remove(flashing)
            for (row in Math.max(flashing.first - 1, 0)..Math.min(flashing.first + 1, 9)) {
                for (col in Math.max(flashing.second - 1, 0)..Math.min(flashing.second + 1, 9)) {
                    if (row == flashing.first && col == flashing.second) {
                        continue
                    }

                    field[row][col]++
                    if (field[row][col] == 10) {
                        flashes.add(Pair(row, col))
                    }
                }
            }
        }

        var count = 0
        for (row in 0..9) {
            for (col in 0..9) {
                if (field[row][col] > 9) {
                    field[row][col] = 0
                    count++
                }
            }
        }
        return count
    }


}

