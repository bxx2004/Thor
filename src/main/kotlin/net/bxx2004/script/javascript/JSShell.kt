package net.bxx2004.script.javascript

import net.bxx2004.script.*
import net.bxx2004.script.error.ScriptTypeException
import net.bxx2004.script.source.ThorSource
import org.openjdk.nashorn.api.scripting.NashornScriptEngine
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList
import javax.script.CompiledScript
import javax.script.ScriptContext
import javax.script.ScriptEngineManager


class JSShell(val options: ThorOptions = ThorOptions.default()):ThorShell {
    private val caches=CopyOnWriteArrayList<JSFunction>()
    private val engine = ScriptEngineManager().getEngineByName("nashorn") as NashornScriptEngine
    private fun compileCache():String{
        var result = ""
        caches.forEach {
            result += it.compile() + "\n"
        }
        return result
    }

    override fun options(): ThorOptions {
        return options
    }
    override fun getVariable(name: String): Any? {
        return engine.getBindings(ScriptContext.ENGINE_SCOPE)[name]
    }
    override fun eval(source: ThorSource, variable: Map<String, Any?>, executor: ThorExecutor): Any? {
        if (options.ENABLE_CACHE_FUNCTION){
            source.inject(compileCache())
        }
        val bindings = engine.createBindings()
        variable.forEach { t, u ->
            bindings[t] = u
        }
        bindings["executor"] = executor
        return engine.eval(source.reader(),bindings)
    }

    override fun invokeFunction(
        compile: ThorCompile?,
        name: String,
        args: List<Any?>,
        variable: Map<String, Any?>
    ): Any? {
        if (compile != null){
            if (compile.adapt() !is CompiledScript){
                throw ScriptTypeException("ThorCompile is not a valid CompiledScript.")
            }
            return engine.invokeMethod(compile.adapt(),name,*args.toTypedArray())
        }else{
            return engine.invokeFunction(name,*args.toTypedArray())
        }
    }

    override fun injectVariable(variable: Map<String, Any?>) {
        variable.forEach{t,u->
            engine.getBindings(ScriptContext.ENGINE_SCOPE)[t] = u
        }
    }

    override fun removeVariable(name: String) {
        engine.getBindings(ScriptContext.ENGINE_SCOPE).remove(name)
    }
    override fun compile(source: ThorSource, variable: Map<String, Any?>): ThorCompile {
        if (options.ENABLE_CACHE_FUNCTION){
            source.inject(compileCache())
        }
        injectVariable(variable)
        val result = ThorCompile.make(engine.compile(source.reader()),type(),source.toString())
        variable.forEach{t,_ ->
            removeVariable(t)
        }
        return result
    }
    override fun injectFunction(function: ThorFunction,cache:Boolean) {
        if (function !is JSFunction){
            throw ScriptTypeException("ThorFunction is not a valid JSFunction")
        }
        if (cache) caches.addIfAbsent(function)
        engine.eval(function.compile())
    }

    override fun close() {
        engine.getBindings(ScriptContext.ENGINE_SCOPE).clear()
    }

    override fun type(): ThorType {
        return JSType
    }
}