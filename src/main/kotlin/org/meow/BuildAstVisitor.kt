package org.meow

import org.meow.ast.CompilationUnitNode
import org.meow.ast.ExpressionNode
import org.meow.ast.IntegerLiteralExpressionNode

class BuildAstVisitor : MeowBaseVisitor<ExpressionNode>() {
    override fun visitCompileUnit(ctx: MeowParser.CompileUnitContext?): CompilationUnitNode {
        return CompilationUnitNode(ctx!!.children.map { visit(it) })
    }

    override fun visitIntegerLiteralExpr(ctx: MeowParser.IntegerLiteralExprContext?): IntegerLiteralExpressionNode {
        return IntegerLiteralExpressionNode(ctx!!.text.toInt())
    }
}
