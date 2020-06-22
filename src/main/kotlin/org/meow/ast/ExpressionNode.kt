package org.meow.ast

import org.meow.compiler.Compiler

sealed class ExpressionNode {
    abstract fun visit(compiler: Compiler)
}

data class CompilationUnitNode(val expr: List<ExpressionNode>): ExpressionNode() {
    override fun visit(compiler: Compiler) {
        compiler.beginFile()
        expr.forEach { it.visit(compiler) }
        compiler.finishFile()
    }
}

data class IntegerLiteralExpressionNode(val value: Int): ExpressionNode() {
    override fun visit(compiler: Compiler) {
        // do nothing
    }
}

data class DefineValueNode(val name: String, val value: ExpressionNode) : ExpressionNode() {
    override fun visit(compiler: Compiler) {
        when (value) {
            is IntegerLiteralExpressionNode -> compiler.addField(name, Compiler.Descriptor.INTEGER, value.value)
        }
    }
}
