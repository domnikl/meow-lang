package org.meow

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream

class MeowLexerTest {
    @Test
    fun lineComments() {
        val tokens = tokensFromText(" 34 // this is a comment")

        assertEquals(2, tokens.size)
        assertEquals(MeowLexer.INTEGER_LITERAL, tokens[0].type)
        assertEquals(MeowLexer.EOF, tokens[1].type)
    }

    @Test
    fun multiLineComments() {
        val tokens = tokensFromText("/* this is a comment \n and it goes on in this line */  1")

        assertEquals(2, tokens.size)
        assertEquals(MeowLexer.INTEGER_LITERAL, tokens[0].type)
        assertEquals(MeowLexer.EOF, tokens[1].type)
    }

    @Test
    fun tokens() {
        val tokens = mapOf(
            " " to -1,
            "\n" to -1,
            "\r" to -1,
            "\r\n" to -1,
            ";" to -1
        )

        tokens.forEach { (token, lexerToken) ->
            assertEquals(lexerToken, tokensFromText(token)[0].type)
        }
    }

    @Test
    fun integerLiterals() {
        val tokens = tokensFromText("1; 0; 42")

        assertEquals(MeowLexer.INTEGER_LITERAL, tokens[0].type)
        assertEquals("1", tokens[0].text)

        assertEquals(MeowLexer.INTEGER_LITERAL, tokens[1].type)
        assertEquals("0", tokens[1].text)

        assertEquals(MeowLexer.INTEGER_LITERAL, tokens[2].type)
        assertEquals("42", tokens[2].text)
    }

    private fun tokensFromText(txt: String): List<Token> {
        val iStream = ByteArrayInputStream(txt.toByteArray())
        val cStream = ANTLRInputStream(iStream)
        val lex = MeowLexer(cStream)
        lex.addErrorListener(NoErrorListener)

        val tokenStream = CommonTokenStream(lex)
        tokenStream.fill()

        return tokenStream.tokens
    }
}
