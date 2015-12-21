/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.uepb.bsc7.www.persistence;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Diego
 */

//Tem que fechar o BD depois que utilizar o ResultSet e MetaData!!!!!!!!!!

public class TabelaRelatorio extends AbstractTableModel{

    ResultSet resultSet = null;
    ResultSetMetaData metadados = null;

    public TabelaRelatorio(ResultSet resultSet, ResultSetMetaData metadados) {
        this.resultSet = resultSet;
        this.metadados = metadados;
    }
    
    public int getRowCount(){
        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException ex) {
            Logger.getLogger(TabelaRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public int getColumnCount(){
        try {
            return metadados.getColumnCount();
        } catch (SQLException ex) {
            Logger.getLogger(TabelaRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Object getValueAt(int linha, int coluna) {

        try {
            resultSet.absolute(linha + 1);
            return resultSet.getObject(coluna + 1);
        } catch (SQLException ex) {
            Logger.getLogger(TabelaRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";

    }
    
    public String getColumnName(int coluna){
        try {
            return metadados.getColumnName(coluna + 1);
        } catch (SQLException ex) {
            Logger.getLogger(TabelaRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    
    
    
    
    public static TableModel getModeloTabela() {
        JTable tabela = new JTable();
        
        /*int opcao = Integer.parseInt(JOptionPane.showInputDialog("1 ou 2?"));
        
        if(opcao == 1){*/
        
        tabela.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {"Aqui", "Ali", "Acolá", "Cá"},
                    {"Foi", "Fui", "Fomos", "Fumamos"},
                    {"1", "2", "3", "4"},
                    {"8", "7", "6", "5"}
                },
                new String[]{
                    "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        /*}else{
        
        tabela.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][]{
        {"Oxi", "Pronto", "Agora Lascou", "Vôti"},
        {"Sei", "Çei", "Çabemos", "Sabões"},
        {"9", "9", "9", "9"},
        },
        new String[]{
        "Title 1", "Title 2", "Title 3", "Title 4"
        }
        ));
        }*/
        return tabela.getModel();
    }
    
    
}
