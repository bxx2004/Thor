package net.bxx2004.script.source

import java.io.Reader

class StringSource(var s:String):ThorSource() {
    override fun reader(): Reader {
        return s.reader()
    }

    override fun inject(s: String) {
        this.s +="\n"+s
    }

    override fun toString(): String {
        return s
    }
}