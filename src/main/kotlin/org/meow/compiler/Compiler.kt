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

        // static main
        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        mv.visitCode()
    }

    fun finishFile() {
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        cw.visitEnd()
    }

    fun addFunctionCall(name: String, args: List<Pair<Int, Descriptor>>) {
        if (name == "printLine" && args.size == 1 && args[0].second == Descriptor.INTEGER) {
            args.forEach { (value, type) ->
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                // TODO: this is for integers only
                mv.visitIntInsn(SIPUSH, value)
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(${type.identifier})V", false);
            }
        } else {
            throw IllegalArgumentException("Unknown function $name(${args.joinToString(",") { it.second.identifier }})")
        }
    }

    enum class Descriptor(val identifier: String) {
        INTEGER("I")
    }
}
