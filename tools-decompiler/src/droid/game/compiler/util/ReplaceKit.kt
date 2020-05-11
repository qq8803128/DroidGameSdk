package droid.game.compiler.util

class ReplaceKit {
    companion object {
        fun r(s:String,s1: String,s2: String): String = s.replace(s1,s2)
    }
}