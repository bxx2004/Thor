package net.bxx2004.script

class ThorOptions {
    var ENABLE_CACHE = true
    var PATH = "D://"
    companion object{
        @JvmStatic
        fun default():ThorOptions{
            return ThorOptions()
        }
    }
}