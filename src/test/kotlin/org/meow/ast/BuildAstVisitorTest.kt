package org.meow.ast

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import org.meow.BuildAstVisitor
import org.meow.MeowParser

class BuildAstVisitorTest {
    private val visitor = BuildAstVisitor()

    @Test
    fun canBuildAstForIntegerLiteralExpr() {
        assertEquals(2, visitor.visitIntegerLiteralExpr(createSimpleNumberExprContext(2)).value)
    }

    @Test
    fun canBuildAstForDefineValue() {
        val expr = createSimpleNumberExprContext(42)
        val value = visitor.visitIntegerLiteralExpr(expr)
        val ctx = mockk<MeowParser.DefineValueContext>()
        every { ctx.IDENTIFIER().text } returns "a"
        every { ctx.expr() } returns expr

        assertEquals("a", visitor.visitDefineValue(ctx).name)
        assertEquals(value, visitor.visitDefineValue(ctx).value)
    }

    private fun createSimpleNumberExprContext(value: Int): MeowParser.IntegerLiteralExprContext {
        val ctx = mockk<MeowParser.IntegerLiteralExprContext>()
        every { ctx.text } returns value.toString()
        every { ctx.accept(any<BuildAstVisitor>()) } returns IntegerLiteralExpressionNode(value)

        return ctx
    }
}
