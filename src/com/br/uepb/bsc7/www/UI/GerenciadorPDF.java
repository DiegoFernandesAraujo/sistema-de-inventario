/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.uepb.bsc7.www.UI;

import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.JTable;

/**
 *
 * @author Diego
 */
public class GerenciadorPDF {

    public void geraPDF(JTable tabela) {
// criação do documento 
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("D:\\PDF_DevMedia.pdf"));
            document.open();
            // adicionando um parágrafo no documento 
            //document.add(new Paragraph("Gerando PDF - Java"));
            
            document.add((Element) tabela);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        document.close();
    }

    
}
