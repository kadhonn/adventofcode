fun main() {
    val min = 168630
    val max = 718098
    var count = 0

    for (pass in min..max) {
        if (check(pass)) {
            count++
        }
    }

    println(count)
    println(check(111111))
    println(check(223450))
    println(check(123789))
    println(check(112233))
    println(check(123444))
    println(check(111122))
}

private fun check(pass: Int): Boolean {
    var decrease = true
    var doubleFound = false
    var doubleFoundCount = 1
    var chopped = pass
    var last = chopped % 10
    chopped /= 10
    while (chopped > 0) {
        val new = chopped % 10
        if (new == last) {
            doubleFoundCount++
        } else {
            if (doubleFoundCount == 2) {
                doubleFound = true
            }
            doubleFoundCount = 1
        }
        if (new > last) {
            decrease = false
            break
        }
        last = new
        chopped /= 10
    }
    return (doubleFound || doubleFoundCount == 2) && decrease
}
