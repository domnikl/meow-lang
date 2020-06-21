package org.meow

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.FileInputStream
import kotlin.system.exitProcess

fun main(argv: Array<String>) {
    if (argv.isEmpty()) {
        System.err.println("Please specify input file.")
        exitProcess(1)
    }

    val stream = ANTLRInputStream(FileInputStream(argv[0]))
    val lexer = org.meow.MeowLexer(stream);

    // TODO: error handling!
    //lexer.removeErrorListeners()
    //lexer.addErrorListener(ErrorListener)

    val parser = org.meow.MeowParser(CommonTokenStream(lexer))

    // TODO: error handling!
    //parser.removeErrorListeners()
    //parser.addErrorListener()

    val rootCompileUnit = parser.compileUnit()
    val ast = BuildAstVisitor().visitCompileUnit(rootCompileUnit)

    // TODO: option to print AST
    println(ast)

    // TODO: call compiler
}
