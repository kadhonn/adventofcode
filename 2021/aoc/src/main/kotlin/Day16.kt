import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis {
        Day16.part1(ClassLoader.getSystemResource("day16_full.in").readText().trim())
//        Day16.part1(ClassLoader.getSystemResource("day16_example.in").readText().trim())
    })
}

object Day16 {

    var bitPos = 0
    var messageBytes = ByteArray(0)
    var versionSum = 0
    fun part1(input: String) {
        messageBytes = input.decodeHex()

        while (true) {
            if (bitPos + 8 >= messageBytes.size * 8) {
                break
            }
            val version = readVersion()
            versionSum += version
            val type = readType()
            print("new packet with version $version and type $type: ")
            when (type) {
                4 -> {
                    // literal
                    var lastPacket = false
                    var value = 0L
                    while (!lastPacket) {
                        lastPacket = readBits(1) == 0
                        value = value.shl(4).or(readBits(4).toLong())
                    }
                    println("literal: $value")
                }
                else -> {
                    //operator
                    val lengthType = readBits(1)
                    if (lengthType == 0) {
                        val subpacketsLength = readBits(15)
                    } else {
                        val subpacketsCount = readBits(11)
                    }
                }
            }
        }

        println("versionSum: $versionSum")
    }

    private fun readVersion(): Int {
        return readBits(3)
    }

    private fun readType(): Int {
        return readBits(3)
    }

    private fun readBits(count: Int): Int {
        var result = 0
        for (i in bitPos until bitPos + count) {
            val currentByte = messageBytes[i / 8]
            val currentBit = currentByte.toInt().shr(7 - (i % 8)).and(1)
            result = result.shl(1).or(currentBit)
        }
        bitPos += count
        return result
    }


    //thanks https://stackoverflow.com/questions/66613717/kotlin-convert-hex-string-to-bytearray
    fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}

