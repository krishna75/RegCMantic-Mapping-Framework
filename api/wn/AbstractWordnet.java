
package api.wn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota, 13-Dec-2011,   10:40:08
 * A PhD project at Oxford Brookes University
 */
public abstract class AbstractWordnet {
    String wnFile = Settings.WN_CONFIG_FILE;
    public AbstractWordnet(){
        init();
    }
    private void init(){
        try {
            // Initialises java wordnet
            JWNL.initialize(new FileInputStream(wnFile));
        } catch (JWNLException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
             ex.printStackTrace();
        }
    }
}
