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

    private fun createSimpleNumberExprContext(value: Int): MeowParser.IntegerLiteralExprContext {
        val ctx = mockk<MeowParser.IntegerLiteralExprContext>()
        every { ctx.text } returns value.toString()
        every { ctx.accept(any<BuildAstVisitor>()) } returns IntegerLiteralExpressionNode(value)

        return ctx
    }
}
