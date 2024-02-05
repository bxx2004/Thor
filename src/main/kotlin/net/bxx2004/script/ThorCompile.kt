package net.bxx2004.script

import java.io.Reader

interface ThorCompile {
    fun adapt():Any
    fun type():ThorType
    fun source():Reader
    companion object{
        @JvmStatic
        fun make(value:Any,type:ThorType,soure:String):ThorCompile{
            return object :ThorCompile{
                override fun adapt(): Any {
                    return value
                }

                override fun type(): ThorType {
                    return type
                }

                override fun source(): Reader {
                    return soure.reader()
                }
            }
        }
    }
}