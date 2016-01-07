/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.uepb.bsc7.www.UI;

import com.br.uepb.bsc7.www.persistence.InventarioDAO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Diego
 */
public class ManipulaXLS {

    HSSFWorkbook wb;
    XSSFWorkbook wbx;
    ArrayList<String> dados = new ArrayList<>();
    InventarioDAO dao = null;
    public static boolean continua;

    public ManipulaXLS(InventarioDAO obj) {
        dao = obj;
    }

    public ManipulaXLS() {
    }

    public void setRefInventarioDAO(InventarioDAO obj) {
        dao = obj;
    }

    public boolean ehSIABI(String filename) throws IOException {
        boolean siabi = false;
        FileInputStream fileInputStream = new FileInputStream(filename);
        try {

            wbx = new XSSFWorkbook(fileInputStream);
            //Obtem acesso à planilha Plan1

            XSSFSheet sx = wbx.getSheet("Plan1");
            Row linha = sx.getRow(0);

            //Só aceita com 15 colunas (quantidade de colunas da planilha SIABI passada)
            if (linha.getLastCellNum() == 15) {
                siabi = true;
            } else {
                JOptionPane.showMessageDialog(null, "Deve ser carregado um arquivo padrão gerado pelo SIABI!", null, JOptionPane.ERROR_MESSAGE);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaXLS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return siabi;
    }

    public void leXLS(String filename) throws IOException {
        System.out.println("Método leXLS chamado!");
        FileInputStream fileInputStream = new FileInputStream(filename);
        continua = true;

        try {
            //Obtem acesso à pasta de trabalho
            wbx = new XSSFWorkbook(fileInputStream);
            //Obtem acesso à planilha Plan1
            XSSFSheet sx = wbx.getSheet("Plan1");

            Iterator<Row> rowIterator = sx.rowIterator();

            while (rowIterator.hasNext()) {

                if (continua == true) {

                    //Obtem acesso a cada linha de Plan1
                    Row linha = rowIterator.next();

                    for (int countCel = 0; countCel < linha.getLastCellNum(); countCel++) {

                        Cell celula;
                        //Obtem acesso a cada célula de cada linha de Plan1
                        if (linha.getCell(countCel) == null) {
                            celula = linha.createCell(countCel);
                        } else {
                            celula = linha.getCell(countCel);
                        }

                        //Adiciona o valor de cada célula ao ArrayList que será passado a DAO
                        try {

                            dados.add(celula.getStringCellValue());
                        } catch (Exception ex) {

                            try {
                                Double temp = celula.getNumericCellValue();
                                int valor = temp.intValue();

                                dados.add((String) Integer.toString(valor));

                            } catch (Exception ex2) {
                                JOptionPane.showMessageDialog(null, "ERRO em leXLS");
                            }
                        }
                    }

                    //Insere os dados lidos no BD
                    dao.insereLinha(dados);
                    //Limpa o ArrayLista para preenchê-lo novamente
                    dados.clear();
                } else {
                    break;
                }
            }
            if (continua == true) {
                JOptionPane.showMessageDialog(null, "Arquivo carregado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível carregar o arquivo!\nleXLS()", null, JOptionPane.ERROR_MESSAGE);
            }
            //Corrigir este catch com algo mais eficiente
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar o arquivo!\ncatch - leXLS()", null, JOptionPane.ERROR_MESSAGE);
        }

    }

    public void criaXLS(JTable tabela, String arqSaida, String nomePlan) throws IOException, NullPointerException {
        int numLinhas = tabela.getRowCount();
        int numCelulas = tabela.getColumnCount();

        //Cria pasta de trabalho
        wb = new HSSFWorkbook();

        //Cria planilha
        HSSFSheet s = wb.createSheet(nomePlan);

        //Gera títulos na primeira linha da planilha
        HSSFRow linha0 = s.createRow(0);

        for (int k = 0; k < numCelulas; k++) {
            HSSFCell celula = linha0.createCell(k);
            celula.setCellValue(tabela.getColumnName(k));
        }

        for (int i = 0; i < numLinhas; i++) {

            //Cria a linha
            HSSFRow linha = s.createRow(i + 1);

            for (int j = 0; j < numCelulas; j++) {
                HSSFCell celula = linha.createCell(j);

                if (tabela.getValueAt(i, j) instanceof Integer) {
                    Integer valorInteger = (Integer) tabela.getValueAt(i, j);
                    double valor = valorInteger.doubleValue();

                    celula.setCellValue(valor);
                } else {
                    celula.setCellValue((String) tabela.getValueAt(i, j));
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(arqSaida + ".xls");
        try {
            wb.write(fileOutputStream);
            //Fecha o fileOutputStream
            //Melhorar este catch    
        } catch (IOException ex) {
            System.out.println("Teste");
        } finally {
            fileOutputStream.close();
            wb.close(); //Fecha a pasta de trabalho
        }
    }

    public void criaXLSX(JTable tabela, String arqSaida, String nomePlan) throws IOException, NullPointerException {
        int numLinhas = tabela.getRowCount();
        int numCelulas = tabela.getColumnCount();

        //Cria pasta de trabalho
        wbx = new XSSFWorkbook();
        //Cria planilha
        XSSFSheet sx = wbx.createSheet(nomePlan);

        //Gera títulos na primeira linha da planilha
        XSSFRow linha0 = sx.createRow(0);

        for (int k = 0; k < numCelulas; k++) {
            XSSFCell celula = linha0.createCell(k);
            celula.setCellValue(tabela.getColumnName(k));
        }

        for (int i = 0; i < numLinhas; i++) {

            //Cria a linha
            XSSFRow linha = sx.createRow(i + 1);

            for (int j = 0; j < numCelulas; j++) {

                XSSFCell celula = linha.createCell(j);

                /*O valor passado deve ser de acordo com aquele recebido por cada relatório, 
                                provavelmente String;*/
                if (tabela.getValueAt(i, j) instanceof Integer) {
                    Integer valorInteger = (Integer) tabela.getValueAt(i, j);
                    double valor = valorInteger.doubleValue();

                    celula.setCellValue(valor);
                } else {
                    celula.setCellValue((String) tabela.getValueAt(i, j));
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(arqSaida + ".xlsx");
        try {
            wbx.write(fileOutputStream);
            //Fecha o fileOutputStream
            //Melhorar este catch    
        } catch (IOException ex) {
            System.out.println("Teste");
        } finally {
            fileOutputStream.close();
            wbx.close(); //Fecha a pasta de trabalho
        }
    }

}
