/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fx;
import java.io.InputStreamReader;
import java.util.Date;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class FXTest {
    public static void main(String[] args) throws Exception {
        // set up script:
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("fx");
            
        engine.put("java.util.Date", new Date());           
        InputStreamReader reader = 
               new InputStreamReader(FXTest.class
               .getResourceAsStream("HelloWorld.fx"));
        Object scriptReturnValue = engine.eval(reader);
        System.out.println(scriptReturnValue);
    }
}