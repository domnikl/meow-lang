package org.meow.compiler

import org.meow.ast.CompilationUnitNode
import org.meow.ast.IntegerLiteralExpressionNode
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*

class CompilerVisitor {
    private val classWriter = ClassWriter(0)

    fun visitCompileUnit(ast: CompilationUnitNode): ByteArray {

        // TODO: convert file name to class name?

        classWriter.visit(
            52,
            ACC_PUBLIC + ACC_FINAL + ACC_SUPER,
            "Test",
            null,
            "java/lang/Object",
            null
        )

        // static main
        val methodVisitor = classWriter.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        methodVisitor.visitCode()

        var locals = 1

        ast.expr.forEach {
            when (it) {
                is IntegerLiteralExpressionNode -> {
                    methodVisitor.visitIntInsn(SIPUSH, it.value)
                    methodVisitor.visitVarInsn(ISTORE, locals)
                    // TODO: remove debug println
                    methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
                    methodVisitor.visitVarInsn(ILOAD, locals)
                    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false)
                    locals++
                }
            }
        }

        methodVisitor.visitInsn(RETURN)
        methodVisitor.visitMaxs(2, locals)
        methodVisitor.visitEnd()

        classWriter.visitEnd()

        return classWriter.toByteArray()
    }
}
