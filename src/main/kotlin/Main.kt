import java.io.File
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis


fun assemblyFormat(retVal : String): String {
    return """
            .global _main
        _main:
            .cfi_startproc
            mov w0, #$retVal
            ret
            .cfi_endproc
        """.trimIndent()
}

fun compile(sourceFileName: String) {
    val inFile = File(sourceFileName)

    val source = inFile.readText()

    val sourceRegex = Regex("""int main\s*\(\s*\)\s*\{\s*return\s+([0-9]+)\s*;\s*\}""")

    val matches = sourceRegex.find(source)

    // Exit if unable to parse the C file.
    if (matches == null) {
        println("Unable to parse program.")
        exitProcess(1)
    }

    // groupValues[0] was the entire match. 1 -> n are the captured values
    val retVal = matches.groupValues[1]

    // Write the assembly to the file
    val assemblyFileName = "res/compiled_${inFile.nameWithoutExtension}.s"
    val outFile = File(assemblyFileName)
    outFile.writeText(assemblyFormat(retVal))
}

fun main(args: Array<String>) {

//    for (entry in TokenType.entries) {
//        println(entry.pattern)
//    }

    val source = """
        int main() {
            return 2;
        }
    """.trimIndent()

    var tokens: List<Token>
    val timeToLex = measureTimeMillis {
        tokens = lex(source)
    }

    tokens.forEach {
        println(it)
    }

    println("Time to lex $timeToLex ms")



//    if (args.size != 1) {
//        println("Usage: java -jar assembly-format.jar <program name>")
//        exitProcess(1)
//    }
//
//    compile(args[0])
}