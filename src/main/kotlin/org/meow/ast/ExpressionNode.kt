package org.meow.ast

sealed class ExpressionNode

data class CompilationUnitNode(val expr: List<ExpressionNode>): ExpressionNode()

data class IntegerLiteralExpressionNode(val value: Int): ExpressionNode()
