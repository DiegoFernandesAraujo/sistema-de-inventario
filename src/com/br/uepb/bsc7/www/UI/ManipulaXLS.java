/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.uepb.bsc7.www.UI;

import com.br.uepb.bsc7.www.persistence.InventarioDAO;
import com.br.uepb.bsc7.www.persistence.TesteInsereBD;
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

/**
 *
 * @author Diego
 */
public class ManipulaXLS {
    
    HSSFWorkbook wb; 
    ArrayList<String> dados = new ArrayList<>();
    InventarioDAO dao = null;
    public static boolean continua;
    
    //Tem acesso à Pasta de Trabalho
    /*private static HSSFWorkbook leArquivoXLS(String filename) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(filename);
    try {
    return new HSSFWorkbook(fileInputStream);
    //Colocar um catch aqui?
    } finally {
    fileInputStream.close();
    }
    }*/

    public ManipulaXLS(InventarioDAO obj) {
        dao = obj;
    }

    public ManipulaXLS() {
    }
    
    
    
    
    
    public void setRefInventarioDAO(InventarioDAO obj){
        dao = obj;
    }
    
    public boolean ehSIABI(String filename) throws IOException{
        boolean siabi = false;
        FileInputStream fileInputStream = new FileInputStream(filename);
        try {
            wb = new HSSFWorkbook(fileInputStream);
            //Obtem acesso à planilha Plan1
            HSSFSheet s = wb.getSheet("Plan1");

            Row linha = s.getRow(0);
            //Só aceita com 15 colunas
            if (linha.getLastCellNum() == 14) {
                siabi = true;
            } else {
                JOptionPane.showMessageDialog(null, "Deve ser carregado um arquivo padrão gerado pelo SIABI!", null, JOptionPane.ERROR_MESSAGE);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipulaXLS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return siabi;
    }
    
    public /*static*/ void leXLS(String filename) throws IOException {
        System.out.println("Método leXLS chamado!");
        FileInputStream fileInputStream = new FileInputStream(filename);
        continua = true;
        try {
            //Obtem acesso à pasta de trabalho
            wb = new HSSFWorkbook(fileInputStream);
            //Obtem acesso à planilha Plan1
            HSSFSheet s = wb.getSheet("Plan1");

            Iterator<Row> rowIterator = s.rowIterator();

            while (rowIterator.hasNext()) {

                if (continua == true) {

                    System.out.println("Nova linha!");
                    //Obtem acesso a cada linha de Plan1
                    Row linha = rowIterator.next();

                    Iterator<Cell> cellIterator = linha.cellIterator();

                    while (cellIterator.hasNext()) {
                        
                        Cell celula = cellIterator.next();
                        
                        //Obtem acesso a cada célula de cada linha de Plan1
                        
                        //System.out.println(celula.getStringCellValue());

                        //Adiciona o valor de cada célula ao ArrayList que será passado a DAO
                        //O ERRO ESTÁ AQUI
                        try {
                            
                            if ((celula.equals(null))) {
                                System.out.println("Vazio");
                                dados.add("-");
                            } else {
                                dados.add(celula.getStringCellValue());
                                System.out.println("Cheio");
                            }

                        } catch (Exception ex) {
                            System.out.println(celula.getNumericCellValue());
                            try {
                                Double temp = celula.getNumericCellValue();

                                dados.add((String) temp.toString());

                            } catch (Exception ex2) {
                                System.out.println("Nem numérico nem textual");
                            }
                         
                        
                        
                        }    //dados.add(celula.get);
                        //
                    }

                    //Chamada ao método do BD que recebe o ArrayList (Deve estar em DAO)
                    dao.insereLinha(dados);
                    //Limpa o ArrayLista para preenchê-lo novamente
                    dados.clear();
                }else{break;}
            }
            if (continua == true) {
                JOptionPane.showMessageDialog(null, "Arquivo carregado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(null, "Não foi possível carregar o arquivo!\nleXLS()", null, JOptionPane.ERROR_MESSAGE);
            }
            //Corrigir este catch com algo mais eficiente
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar o arquivo!\ncatch - leXLS()", null, JOptionPane.ERROR_MESSAGE);
        }

    }
    //Versão com JTable
    public /*static*/ void criaXLS(JTable tabela, String arqSaida, String nomePlan) throws IOException, NullPointerException {
        int numLinhas = tabela.getRowCount();
        int numCelulas = tabela.getColumnCount();

        //Cria pasta de trabalho
        wb = new HSSFWorkbook();
        //Cria planilha
        HSSFSheet s = wb.createSheet(nomePlan);
        //wb.setSheetName(0, nomePlan);

        //Alterar para a quantidade de linhas retornada por cada relatório
        for (int i = 0; i < numLinhas; i++) {

            //Cria a linha
            HSSFRow linha = s.createRow(i);

            //Alterar para a quantidade de células retornada por cada relatório
            for (int j = 0; j < numCelulas; j++) {
                HSSFCell celula = linha.createCell(j);

                /*O valor passado deve ser de acordo com aquele recebido por cada relatório, 
                                provavelmente String;*/
                if (tabela.getValueAt(i, j) instanceof Integer) {
                    Integer valorInteger = (Integer) tabela.getValueAt(i, j);
                    double valor = valorInteger.doubleValue();
                    //celula.setCellValue(/*(String)*/ tabela.getValueAt(i, j).toString());
                    celula.setCellValue(valor);
                } else {
                    celula.setCellValue((String) tabela.getValueAt(i, j));
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(arqSaida+".xls");
        try {
            wb.write(fileOutputStream);
            //Fecha o fileOutputStream
        //Melhorar este catch    
        }catch(IOException ex){
            System.out.println("Teste");
        }finally{
            fileOutputStream.close();
            wb.close(); //Fecha a pasta de trabalho
        }
    }
    
    public static void main(String[] args){
        ManipulaXLS obj = new ManipulaXLS();
        String nomeArquivo = JOptionPane.showInputDialog("Digite o endereço");
        try {
            obj.leXLS(nomeArquivo);
        } catch (IOException ex) {
            Logger.getLogger(ManipulaXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
