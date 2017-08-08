/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uos.inf.did.abbozza.calliope;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import org.fife.ui.rtextarea.*;

/**
 *
 * @author mbrinkmeier
 */
public class AbbozzaCalliopeTooltipSupplier implements ToolTipSupplier {

    private HashMap<Integer,String> tooltips;
    
    public AbbozzaCalliopeTooltipSupplier() {
        tooltips = new HashMap<Integer,String>();
    }
    
    @Override
    public String getToolTipText(RTextArea rta, MouseEvent me) {
        int my = me.getY();
        Set<Integer> lines = tooltips.keySet();
        for ( Integer line : lines ) {
            int y;
            try {
                y = rta.yForLine(line.intValue());
                if ( (my >= y) && (my < y + rta.getLineHeight() ) ) {
                    return tooltips.get(line);
                }
            } catch (BadLocationException ex) {
            }
        }
        return null;
    }

    public void addTooltip(int line, String tip) {
        tooltips.put(new Integer(line), tip);
    }

    public String getTooltip(int line) {
        return tooltips.get(new Integer(line));
    }
    
    public void clear() {
        tooltips.clear();
    }

}
