package org.meow

import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ListTokenSource
import org.antlr.v4.runtime.Token
import org.junit.Test

class MeowParserTest {
    @Test
    fun integerLiteralExpr() {
        val parser = createParserNoError(
            listOf(
                TestToken("42", MeowLexer.INTEGER_LITERAL)
            )
        )

        assert(parser.compileUnit().children[0] is MeowParser.IntegerLiteralExprContext)
    }

    @Test
    fun defineValue() {
        val parser = createParserNoError(
            listOf(
                TestToken("let", MeowLexer.KEYWORD_LET),
                TestToken("x", MeowLexer.IDENTIFIER),
                TestToken("=", MeowLexer.ASSIGNMENT_OPERATOR),
                TestToken("1", MeowLexer.INTEGER_LITERAL)
            )
        )

        assert(parser.compileUnit().children[0] is MeowParser.DefineValueContext)
    }

    private fun createParserNoError(tokens: List<TestToken>): MeowParser {
        return MeowParser(CommonTokenStream(ListTokenSource(tokens))).also { it.addErrorListener(NoErrorListener) }
    }

    private class TestToken (val _text: String, val tokenCode: Int) : Token {
        override fun getText() = _text
        override fun getType() = tokenCode
        override fun getLine(): Int = 0
        override fun getCharPositionInLine(): Int = 0
        override fun getChannel(): Int = 0
        override fun getTokenIndex(): Int = 0
        override fun getStartIndex(): Int = 0
        override fun getStopIndex(): Int = 0
        override fun getTokenSource() = null
        override fun getInputStream() = null
    }
}
