/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.pdf;


import com.steadystate.css.parser.CSSOMParser;
import helper.util.Print;
import java.io.*;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.*;



public class HelloCssParser {

String cssfile = "c:\\pg_0001.htm";

    public HelloCssParser() {



            if (parse(cssfile)) {

                System.out.println("Parsing completed OK");

            } else {

                System.out.println("Unable to parse CSS");

            }   
    }


     public  boolean parse(String cssfile) 
     {

         FileOutputStream out = null; 
         PrintStream ps = null; 
         boolean rtn = false;

         try
         {

               
//                 InputStream stream = this.getClass().getResourceAsStream(cssfile);
                
                 InputStream stream = new FileInputStream(cssfile);
                
Print.prln(" stream ok");
                 // overwrites and existing file contents
                 out = new FileOutputStream("c:\\parser_log.txt");

                 if (out != null)
                 {
                     //log file
                     ps = new PrintStream( out );
                     System.setErr(ps); //redirects stderr to the log file as well

                 } else {

                     return rtn; 

                }

Print.prln(" logfile ok");
                InputSource source = new InputSource(new InputStreamReader(stream));
                Print.prln(" source ok");
                CSSOMParser parser = new CSSOMParser();
                Print.prln(" stream source ok");
                // parse and create a stylesheet composition
                CSSStyleSheet stylesheet = parser.parseStyleSheet(source);

                //ANY ERRORS IN THE DOM WILL BE SENT TO STDERR HERE!!
                // now iterate through the dom and inspect.

                CSSRuleList ruleList = stylesheet.getCssRules();

                Print.prln("Number of rules: " + ruleList.getLength());
                ps.println("Number of rules: " + ruleList.getLength());


               for (int i = 0; i < ruleList.getLength(); i++) 
               {
                 CSSRule rule = ruleList.item(i);
                 if (rule instanceof CSSStyleRule) 
                 {
                     CSSStyleRule styleRule=(CSSStyleRule)rule;
                     ps.println("selector:" + i + ": " + styleRule.getSelectorText());
                     CSSStyleDeclaration styleDeclaration = styleRule.getStyle();


                     for (int j = 0; j < styleDeclaration.getLength(); j++) 
                     {
                          String property = styleDeclaration.item(j);
                          ps.println("property: " + property);
                          ps.println("value: " + styleDeclaration.getPropertyCSSValue(property).getCssText());
                          ps.println("priority: " + styleDeclaration.getPropertyPriority(property));   
                     }



                  }// end of StyleRule instance test
                } // end of ruleList loop

               if (out != null) out.close();
               if (stream != null) stream.close();
               rtn = true;
            }
            catch (IOException ioe)
            {
                System.err.println ("IO Error: " + ioe);
            }
            catch (Exception e)
            {
                System.err.println ("Error: " + e);

            }
            finally
            {
                if (ps != null) ps.close(); 
            }

            return rtn;

    }

}