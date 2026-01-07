/**
 * AKA The Scanner or Tokenizer
 *
 * Tokens our lexer needs to recognize
 *
 * Open brace \{
 * Close brace \}
 * Open parenthesis \(
 * Close parenthesis \)
 * Semicolon ;
 * Int keyword int
 * Return keyword return
 * Identifier [a-zA-Z]\w*
 * Integer literal [0-9]+
 */

enum class TokenType(val pattern: String, val regex: Regex = Regex(pattern)) {
    OpenBrace("""\{"""),
    CloseBrace("""\}"""),
    OpenParen("""\("""),
    CloseParen("""\)"""),
    Semicolon(""";"""),
    KeywordInt("""int"""), // If we want we could have a single keyword token rather than different tokens for each keyword
    KeywordReturn("""return"""),
    Identifier("""[a-zA-Z]\w*"""),
    IntegerLiteral("""[0-9]+"""),
}

data class Token(val type: TokenType, val value: String, val lineNumber: Int, val columnNumber: Int)

fun lex(source: String): List<Token> {
    val tokens = mutableListOf<Token>()

    // TODO: Scanner class? Contains row,column,tokenStart

    var row = 0
    var col = 0
    var tokenStart = 0
    var isParsing = false

    for (line in source.lines()) {
        col = 0
        tokenStart = 0

        for (rune in line) {
//            println("Line: ${row + 1} Token Start: ${tokenStart + 1} Column: ${col + 1} Rune: $rune")

            val tokenText = line.substring(tokenStart, col)

            // Whitespace ENDS any current parsing and generates the token.
            // Special characters END current parsing and generates the current token. It also adds the special character Token
            if (rune.isWhitespace()) {
                // Finish token when we encounter a whitespace character
                if (isParsing) {
                    isParsing = false
                    println("TOKEN is $tokenText")
                }

                tokenStart = col

//            } else if (TokenType.OpenBrace.regex.matches(tokenText)) {
            } else if (rune == '{') {

                if (isParsing) {
                    isParsing = false
                    println("TOKEN IS ${line.substring(tokenStart, col)}")
                }

                tokenStart = col

                //
                isParsing = true

//            } else if (TokenType.CloseBrace.regex.matches(tokenText)) {
            } else if (rune == '}') {
                if (isParsing) {
                    isParsing = false
                    println("TOKEN IS ${line.substring(tokenStart, col)}")
                }

                tokenStart = col

                isParsing = true

//            } else if (TokenType.OpenParen.regex.matches(tokenText)) {
            } else if (rune == '(') {
                if (isParsing) {
                    isParsing = false
                    println("TOKEN IS ${line.substring(tokenStart, col)}")
                }

                tokenStart = col

                isParsing = true

//            } else if (TokenType.CloseParen.regex.matches(tokenText)) {
            } else if (rune == ')') {
                if (isParsing) {
                    isParsing = false
                    println("TOKEN IS ${line.substring(tokenStart, col)}")
                }

                tokenStart = col

                isParsing = true

//            } else if (TokenType.Semicolon.regex.matches(tokenText)) {
            } else if (rune == ';') {
                if (isParsing) {
                    isParsing = false
                    println("TOKEN IS ${line.substring(tokenStart, col)}")
                }

                tokenStart = col

                isParsing = true

            } else {
                if (!isParsing) {
                    isParsing = true
                    tokenStart = col
                }
            }

            col++
        }

        // Right before EOL, Finish parsing
        if (isParsing) {
            isParsing = false
            println("TOKEN IS ${line.substring(tokenStart, col)}")
        }

        row++
    }

    return tokens
}


