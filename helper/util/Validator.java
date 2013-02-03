
package helper.util;

/**
 *
 * @author Krishna Sapkota, 06-Dec-2011,   17:49:08
 * A PhD project at Oxford Brookes University
 */
public class Validator {

public static boolean isValidString(String aString){
    boolean valid = false;
    if (aString != null & !aString.equals("")){
        valid = true;
    }
    return valid;
}

}
