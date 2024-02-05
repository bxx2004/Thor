package net.bxx2004.script.javascript

import com.github.alanger.commonjs.FilesystemFolder
import com.github.alanger.commonjs.ModuleCache
import com.github.alanger.commonjs.nashorn.NashornModule
import net.bxx2004.script.*
import net.bxx2004.script.error.ScriptTypeException
import net.bxx2004.script.source.ThorSource
import org.openjdk.nashorn.api.scripting.NashornScriptEngine
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory
import java.io.File
import javax.script.CompiledScript
import javax.script.ScriptContext


class JSShell(val options: ThorOptions = ThorOptions.default()):ThorShell {
    private val engine = NashornScriptEngineFactory().scriptEngine as NashornScriptEngine
    init {
        val bindings = engine.getBindings(100)
        val rootFolder = FilesystemFolder.create(File(options.PATH), "UTF-8")
        val module = engine.eval("({})")
        val exports = engine.eval("({})")
        val created = NashornModule(engine,rootFolder, ModuleCache(),"<main>",module,exports,null,null)
        bindings.put("require",created)
        bindings.put("module",module)
        bindings.put("exports",exports)
    }

    override fun options(): ThorOptions {
        return options
    }
    override fun getVariable(name: String): Any? {
        return engine.getBindings(ScriptContext.ENGINE_SCOPE)[name]
    }
    override fun eval(source: ThorSource, variable: Map<String, Any?>, executor: ThorExecutor): Any? {
        val bindings = engine.createBindings()
        if (options.ENABLE_CACHE) {
            bindings.putAll(engine.getBindings(ScriptContext.ENGINE_SCOPE))
        }
        variable.forEach { t, u ->
            bindings[t] = u
        }
        bindings["executor"] = executor
        return engine.eval(source.reader(), bindings)
    }

    override fun invokeFunction(
        compile: ThorCompile?,
        name: String,
        args: List<Any?>,
        variable: Map<String, Any?>
    ): Any? {
        if (compile != null){
            if (compile.adapt() !is JSCompile){
                throw ScriptTypeException("ThorCompile is not a valid CompiledScript.")
            }
            return compile.invokeFunction(name,args)
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
        injectVariable(variable)
        val result = JSCompile(source,engine.compile(source.reader()))
        variable.forEach{t,_ ->
            removeVariable(t)
        }
        return result
    }
    override fun injectFunction(function: ThorFunction,cache:Boolean) {
        if (function !is JSFunction){
            throw ScriptTypeException("ThorFunction is not a valid JSFunction")
        }
        engine.eval(function.compile())
    }

    override fun close() {
        engine.getBindings(ScriptContext.ENGINE_SCOPE).clear()
    }

    override fun type(): ThorType {
        return JSType
    }
}