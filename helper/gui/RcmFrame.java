
package helper.gui;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import rcm.main.Settings;

/**
 *
 * @author Krishna Sapkota, 18-Nov-2011,   12:29:02
 * A PhD project at Oxford Brookes University
 */
public class RcmFrame extends JFrame{

    public RcmFrame() {
        super("RegCMantic, Semantic Frameworks for Regulatory Compliance");
        Image icon = Toolkit.getDefaultToolkit().getImage(Settings.IMAGE_PATH+"report_check_small.png");
        setIconImage(icon);
        
    }



}
