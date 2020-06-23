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

    @Test
    fun canBuildAstForFunctionCall() {
        val arg1 = createSimpleNumberExprContext(42)
        val arg2 = createSimpleNumberExprContext(21)

        val ctx = mockk<MeowParser.FuncCallExprContext>()
        ctx.identifier = mockk()

        every { ctx.identifier.text } returns "printLine"
        every { ctx.exprList().expr() } returns listOf(arg1, arg2)

        val expected = listOf(
            IntegerLiteralExpressionNode(42),
            IntegerLiteralExpressionNode(21)
        )

        assertEquals("printLine", visitor.visitFuncCallExpr(ctx).name)
        assertEquals(expected, visitor.visitFuncCallExpr(ctx).args)
    }

    private fun createSimpleNumberExprContext(value: Int): MeowParser.IntegerLiteralExprContext {
        val ctx = mockk<MeowParser.IntegerLiteralExprContext>()
        every { ctx.text } returns value.toString()
        every { ctx.accept(any<BuildAstVisitor>()) } returns IntegerLiteralExpressionNode(value)

        return ctx
    }
}
