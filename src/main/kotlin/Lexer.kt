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
// If I use a sealed class I can use chars for some and strings/regexes for others
// OR Should I use an enum class without the members?
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
    Error("""$^""")
}

data class Token(val type: TokenType, val value: String, val lineNumber: Int, val columnNumber: Int)

// if starts with number then check if its an integer literal
// if starts with letter, check if keyword
// if not a keyword, then it must be an identifier
fun determineTokenTypeFromString(tokenText: String) =  when (tokenText) {
    "" -> null
    "{" -> TokenType.OpenBrace
    "}" -> TokenType.CloseBrace
    "(" -> TokenType.OpenParen
    ")" -> TokenType.CloseParen
    ";" -> TokenType.Semicolon
    "int" -> TokenType.KeywordInt
    "return" -> TokenType.KeywordReturn
    else -> {
        if (tokenText.matches(TokenType.Identifier.regex))
            TokenType.Identifier
        else if (tokenText.matches(TokenType.IntegerLiteral.regex))
            TokenType.IntegerLiteral
        else
            null
    }
}

fun determineTokenTypeFromRune(rune: Char) = when (rune) {
    '{' -> TokenType.OpenBrace
    '}' -> TokenType.CloseBrace
    '(' -> TokenType.OpenParen
    ')' -> TokenType.CloseParen
    ';' -> TokenType.Semicolon
    else -> null
}

fun Char.endsTokenParsing() =
    this.isWhitespace() ||
            this == '{' ||
            this == '}' ||
            this == '(' ||
            this == ')' ||
            this == ';'

fun lex(source: String): List<Token> {
    val tokens = mutableListOf<Token>()

    // TODO: Scanner class? Contains row,column,tokenStart

    var row = 0
    var col = 0
    var tokenStart = 0
    var isParsing = false
    var currentTokenType: TokenType? = null

    for (line in source.lines()) {
        col = 0
        tokenStart = 0

        for (rune in line) {
//            println("Line: ${row + 1} Token Start: ${tokenStart + 1} Column: ${col + 1} Rune: $rune")

            val tokenText = line.substring(tokenStart, col)

            // Whitespace ENDS any current parsing and generates the token.
            // Special characters END current parsing and generates the current token. It also adds the special character Token
            when {
                 rune.endsTokenParsing() -> {
                     // These special characters will end any parsing that is happening
                    if (isParsing) {
                        isParsing = false
                        if (currentTokenType == null) {
                            currentTokenType = determineTokenTypeFromString(tokenText)
                        }

                        currentTokenType?.let {
                            tokens.add(
                                Token(
                                    type = it,
                                    value = tokenText,
                                    lineNumber = row,
                                    columnNumber = tokenStart
                                )
                            )
                        } ?: println("Error! Invalid Parse on $tokenText")
                        // Invalid Parse! (could add an invalid token type) (throw an error?)
                        currentTokenType = null
                    }

                    tokenStart = col
                    currentTokenType = determineTokenTypeFromRune(rune)

                    // If the current rune is a special character we are parsing again...
                    if (currentTokenType != null) {
                        isParsing = true
                    }
                }
                else -> {
                    // Not Whitespace or any special characters
                    if (!isParsing) {
                        isParsing = true
                        tokenStart = col
                    }
                }
            }

            col++
        }

        // Right before EOL, Finish parsing
        if (isParsing) {
            isParsing = false
            val tokenText = line.substring(tokenStart, col)
            if (currentTokenType == null) {
                currentTokenType = determineTokenTypeFromString(tokenText)
            }

            currentTokenType?.let {
                tokens.add(
                    Token(
                        type = it,
                        value = tokenText,
                        lineNumber = row,
                        columnNumber = tokenStart
                    )
                )
            } ?: println("Error! Invalid Parse on $tokenText")
            // Invalid Parse! (could add an invalid token type) (throw an error?)
            currentTokenType = null
        }

        row++
    }

    return tokens
}





fun tmp(){


}