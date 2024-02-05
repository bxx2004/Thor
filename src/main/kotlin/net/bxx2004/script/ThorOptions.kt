package net.bxx2004.script

class ThorOptions {
    var ENABLE_CACHE_FUNCTION = true
    companion object{
        @JvmStatic
        fun default():ThorOptions{
            return ThorOptions()
        }
    }
}