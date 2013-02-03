/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper.gui;

import java.awt.Color;
import javax.swing.text.*;

/**
 *
 * @author Krishna Sapkota,  Mar 30, 2012,  7:19:28 PM
 * A PhD project at Oxford Brookes University
 */
public class TextStyles {
    StyledDocument document = new DefaultStyledDocument();

    public TextStyles() {
    }

    public StyledDocument getDocument() {
        return document;
    }

    public void setDocument(StyledDocument document) {
        this.document = document;
    }
    
    
    
    public void normal(String text){
        Style style = document.addStyle("normal", null);
        StyleConstants.setFontSize(style, 14);
        insert(text, style);
        
    }
    public void emphasis(String text){
        Style style = document.addStyle("normal", null);
        StyleConstants.setFontSize(style, 14);
        StyleConstants.setItalic(style, true);
        insert(text, style);
        
    }
    
    public void strong(String text){
        Style style = document.addStyle("strong", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setFontFamily(style, "Cambria");
        StyleConstants.setFontSize(style, 18);
        insert(text, style);
        
    }
    
    public void h1(String text){
        Style style = document.addStyle("strong", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setFontFamily(style, "Cambria");
        StyleConstants.setFontSize(style, 35);
        insert(text, style);
        
    }
    
    public void h2(String text){
        Style style = document.addStyle("strong", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setFontFamily(style, "Cambria");
        StyleConstants.setFontSize(style, 30);
        insert(text, style);
        
    }
   public void h3(String text){
        Style style = document.addStyle("strong", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setFontFamily(style, "Cambria");
        StyleConstants.setFontSize(style, 25);       
        insert(text, style);
        
    }
    
    
    private void insert(String text, Style style){
        try {
            document.insertString(document.getLength(), text, style);
        } catch (BadLocationException badLocationException) {
            System.err.println("Bad insert");
        }
    }

    
}
