package net.bxx2004.script

import net.bxx2004.script.javascript.JSFunction
import net.bxx2004.script.javascript.JSShell
import net.bxx2004.script.simple.Parameter
import net.bxx2004.script.source.StringSource

fun main() {
    var shell = JSShell()
    shell.injectFunction(
        JSFunction("sum",
        args =arrayListOf(Parameter.single("a"),Parameter.single("b"))
        , expression = "return a*b;"),true
    )
}