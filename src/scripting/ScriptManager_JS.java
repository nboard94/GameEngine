package scripting;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

public class ScriptManager_JS {

    private static ScriptManager_JS instance = new ScriptManager_JS();
    private static ScriptEngine engine_JS = new ScriptEngineManager().getEngineByName("JavaScript");
    private static Invocable invokable_JS = (Invocable) engine_JS;

    private ScriptManager_JS() {

    }

    public static ScriptManager_JS getInstance() {
        return instance;
    }

    public static void bindArgument(String name, Object obj) {
        engine_JS.put(name, obj);
    }

    public static void loadScript(String script_name) {
        try {
            engine_JS.eval(new java.io.FileReader(script_name));
        } catch(ScriptException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeScript() {
        try {
            invokable_JS.invokeFunction("update");
        } catch(ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void executeScript(Object... args) {
        try {
            invokable_JS.invokeFunction("update", args);
        } catch(ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
