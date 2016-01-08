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
    String[] nomesBD = {"tombo", "titulo", "autor", "localizacao", "situacao", "seq", "cod_barras", "obs", "verificar"};
    String[] nomesExibicao = {"Tombo", "Título", "Autor", "Localização (CDD)", "Situação", "Sequência", "Tombo", "Observação", "Verificar"};

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
    
    public String getColumnName(int coluna) {
        
        try {
            
            String nomeColuna = metadados.getColumnName(coluna + 1);
            
            for (int i = 0; i < nomesBD.length; i++) {
                
                if(nomeColuna.equals(nomesBD[i])){
                    return nomesExibicao[i];
                }
            }
            
            //return metadados.getColumnName(coluna + 1);
        } catch (SQLException ex) {
            Logger.getLogger(TabelaRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
