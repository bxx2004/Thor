package net.bxx2004.script

import net.bxx2004.script.javascript.JSShell
import net.bxx2004.script.source.source

fun main() {
    val shell = JSShell()
    var a = shell.compile(source("function test(a){print(1+1)}")).adapt()
    //println(shell.invokeFunction(name = "test", args = arrayListOf(1)))
}
