package monitorx.service;

import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Component
public class JavascriptEngine {

    public Object executeScript(String scriptContext, String script) throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        try {
            engine.eval(scriptContext);
            engine.eval("function getValue(){ " + script + "}");
            Invocable inv = (Invocable) engine;
            return inv.invokeFunction("getValue");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object executeScript(String script) throws ScriptException {
        return executeScript("", script);
    }
}
