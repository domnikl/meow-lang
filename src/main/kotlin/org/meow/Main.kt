package org.meow

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.meow.compiler.Compiler
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.system.exitProcess

fun main(argv: Array<String>) {
    if (argv.isEmpty()) {
        System.err.println("Please specify input file.")
        exitProcess(1)
    }

    val inputFile = File(argv[0])
    val stream = ANTLRInputStream(FileInputStream(inputFile))
    val lexer = MeowLexer(stream);

    // TODO: error handling!
    //lexer.removeErrorListeners()
    //lexer.addErrorListener(ErrorListener)

    val parser = MeowParser(CommonTokenStream(lexer))

    // TODO: error handling!
    //parser.removeErrorListeners()
    //parser.addErrorListener()

    val rootCompileUnit = parser.compileUnit()
    val ast = BuildAstVisitor().visitCompileUnit(rootCompileUnit)

    // TODO: option to print AST
    println(ast)

    val className = inputFile.nameWithoutExtension
    val outputFile = inputFile.parentFile.resolve("${className}.class")
    val byteCode = Compiler(className).compile(ast)

    FileOutputStream(outputFile).write(byteCode)
}
