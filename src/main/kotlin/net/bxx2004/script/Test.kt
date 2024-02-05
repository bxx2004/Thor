package net.bxx2004.script

import net.bxx2004.script.javascript.JSFunction
import net.bxx2004.script.javascript.JSShell
import net.bxx2004.script.simple.Parameter
import net.bxx2004.script.source.StringSource

fun main() {
    val shell = JSShell()
    shell.injectFunction(
        JSFunction(
            "sum",
            arrayListOf(Parameter.single("a")),
            "return a+1;"
        )
    )

    shell.eval(StringSource("exports.sum = sum;"))
}
