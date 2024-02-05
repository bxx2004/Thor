package net.bxx2004.script.javascript

import net.bxx2004.script.*
import net.bxx2004.script.source.ThorSource
import org.openjdk.nashorn.api.scripting.NashornScriptEngine
import java.io.Reader
import javax.script.CompiledScript
import javax.script.ScriptContext

class JSCompile(val source: ThorSource, val compile:CompiledScript):ThorCompile {
    override fun adapt(): Any {
        return compile
    }

    override fun source(): Reader {
        return source.reader()
    }

    override fun eval(variable: Map<String, Any?>): Any? {
        val binds = compile.engine.createBindings()
        binds.putAll(compile.engine.getBindings(ScriptContext.ENGINE_SCOPE))
        return compile.eval(binds)
    }

    override fun invokeFunction(
        name: String,
        args: List<Any?>
    ): Any? {
        return (compile.engine as NashornScriptEngine).invokeFunction(name,*args.toTypedArray())
    }
}