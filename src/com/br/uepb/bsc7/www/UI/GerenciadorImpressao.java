/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.uepb.bsc7.www.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import static org.apache.poi.hssf.usermodel.HeaderFooter.page;

/**
 *
 * @author Diego
 */
public class GerenciadorImpressao implements Printable{

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        System.out.println("Página atual: " + page);
        
        if (page > 0) {
            System.out.println("NO_SUCH_PAGE");
         return NO_SUCH_PAGE;
    }

    // User (0,0) is typically outside the
    // imageable area, so we must translate
    // by the X and Y values in the PageFormat
    // to avoid clipping.
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate(pf.getImageableX(), pf.getImageableY());

    // Now we perform our rendering
    g.drawString("Hello world!", 100, 100);

    // tell the caller that this page is part
    // of the printed document
        System.out.println("PAGE_EXISTS");
    return PAGE_EXISTS;
        
    }
    
}
