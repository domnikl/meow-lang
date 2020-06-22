package org.meow.compiler

import org.meow.ast.CompilationUnitNode
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

class Compiler(private val className: String) {
    private val cw = ClassWriter(COMPUTE_MAXS)
    private lateinit var fv: FieldVisitor
    private lateinit var mv: MethodVisitor

    private val values = mutableMapOf<String, Descriptor>()

    fun compile(ast: CompilationUnitNode): ByteArray {
        ast.visit(this)

        return cw.toByteArray()
    }

    fun addField(name: String, descriptor: Descriptor, value: Any) {
        // TODO: check if currently in class or method scope!
        fv = cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, name, descriptor.identifier, null, value)
        fv.visitEnd()

        values[name] = descriptor
    }

    fun beginFile() {
        cw.visit(
            52,
            ACC_PUBLIC + ACC_FINAL + ACC_SUPER,
            className,
            null,
            "java/lang/Object",
            null
        )
    }

    fun finishFile() {
        // static main
        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        mv.visitCode()

        // TODO: remove printing every created value ...
        values.forEach { (name, type) ->
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitFieldInsn(GETSTATIC, className, name, type.identifier)
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(${type.identifier})V", false);
        }

        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd()
    }

    enum class Descriptor(val identifier: String) {
        INTEGER("I")
    }
}
