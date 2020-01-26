import java.io.File
import java.util.*


fun main(args: Array<String>) {
    if (args.size != 1) return error("Usage: brainy [filename]")
    val fileName = args.first()
    val fileContents = File(fileName).readText()
    val pair = tokenize(fileContents)
    interpret(pair.first, pair.second)
}

fun interpret(tokens: List<Token>, matchingParantheses: MatchingParantheses) {
    val scanner = Scanner(System.`in`)
    val tapeLength = 30000 // In the original implementation, the tape was 30,000 cells long.

    // Brainfuck operates on an array of memory cells, also referred to as the tape,
    // each initially set to zero.
    val zeroByte = UByte.MIN_VALUE.toByte()
    val tape = ByteArray(tapeLength) { zeroByte }

    var pointer = 0 // There is a pointer, initially pointing to the first memory cell.
    var cursor = 0

    while (cursor < tokens.size) {
        when (tokens[cursor]) {
            Token.INCREMENT_PTR -> pointer++
            Token.DECREMENT_PTR -> pointer--
            Token.INCREMENT_VAL -> tape[pointer]++
            Token.DECREMENT_VAL -> tape[pointer]--
            Token.JUMP_FORWARD -> if (tape[pointer] == zeroByte) cursor = matchingParantheses[cursor] ?: error("Unexpected error.")
            Token.JUMP_BACK -> if (tape[pointer] != zeroByte) cursor = matchingParantheses[cursor] ?: error("Unexpected error.")
            Token.PRINT_CHAR -> print(tape[pointer].toChar())
            Token.GET_CHAR -> tape[pointer] = scanner.next()[0].toByte()
        }
        cursor++
    }
}

private typealias MatchingParantheses = Map<Int, Int>

fun tokenize(fileContents: String): Pair<List<Token>, MatchingParantheses> {
    val tokenList = fileContents.mapNotNull { char -> Token.values().firstOrNull { it.char == char } }
    return Pair(tokenList, findMatchingParantheses(tokenList))
}

fun findMatchingParantheses(tokens: List<Token>): MatchingParantheses {
    return tokens.asSequence().mapIndexedNotNull { index, token ->
        if (token != Token.JUMP_BACK) return@mapIndexedNotNull null
        return@mapIndexedNotNull findMatchingParanthesis(tokens, index)
            ?: error("Invalid program. Parantheses do not match.")
    }.flatten().toMap()
}

fun findMatchingParanthesis(tokens: List<Token>, closedParanthesisPosition: Int): List<Pair<Int, Int>>? {
    var openPos = closedParanthesisPosition
    var counter = 1
    while (counter > 0) {
        val token = tokens.getOrNull(--openPos) ?: return null
        if (token == Token.JUMP_FORWARD) {
            counter--
        } else if (token == Token.JUMP_BACK) {
            counter++
        }
    }
    return listOf(Pair(openPos, closedParanthesisPosition), Pair(closedParanthesisPosition, openPos))
}