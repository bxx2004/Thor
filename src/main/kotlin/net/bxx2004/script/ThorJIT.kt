package net.bxx2004.script

import net.bxx2004.script.jit.SourceType

interface ThorJIT {
    fun compile(type:String,source:String):ThorCompile
}