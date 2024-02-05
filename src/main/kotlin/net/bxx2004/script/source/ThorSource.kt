package net.bxx2004.script.source

import java.io.Reader

abstract class ThorSource {
    abstract fun reader():Reader
    abstract fun inject(s:String)
}