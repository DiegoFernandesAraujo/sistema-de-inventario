package com.br.uepb.bsc7.www.persistence;

import com.br.uepb.bsc7.www.UI.InventarioUI;
import com.br.uepb.bsc7.www.UI.ManipulaXLS;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ConexaoBD {

    private Connection conexao = null;
    private String host = null;
    private String url1 = null;
    private String driver = null;
    private String nomeBD = null;
    private String prefixoBD = null;
    private String portaBD = null;
    private String usuario = null;
    private String senha = "diego";
    private boolean status = false;
    private InventarioDAO dao = null;
    private String url2 = null;
                
    public ConexaoBD() {
        driver = "com.mysql.jdbc.Driver";
        prefixoBD = "jdbc:mysql://";
        host = "localhost";
        portaBD = "3306";
        nomeBD = "inventario_uepb";

       url1 = prefixoBD + host + ":" + portaBD + "/" + nomeBD;
       url2 = prefixoBD + host + ":" + portaBD + "?"; 
       //url = prefixoBD + host + "/" + nomeBD;

    }
    
    public boolean statusConnection(){
        return status;
    }
    
    public Connection getConnection(String user, String pswd) {
        usuario = user;
        senha = pswd;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException!");
            Logger.getLogger(ConexaoBD.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        //Caso o BD já exista    
        try {
            conexao = DriverManager.getConnection(url1, usuario, senha);
            status = true;
        } catch (SQLException e) {
            
            if (e.getErrorCode() == 1044 || e.getErrorCode() == 1045) {
                System.out.println("Conexão BD");
                JOptionPane.showMessageDialog(null, "Insira um nome de usuário e senha válidos!", null, JOptionPane.ERROR_MESSAGE);
                ManipulaXLS.continua = false;
                InventarioUI.setFocusUser();
            } else if (e.getErrorCode() == 1049) { //Caso o BD não exista    
                try {
                    conexao = DriverManager.getConnection(url2 + "user=" + usuario + "&" + "password=" + senha);
                    Statement stmt = conexao.createStatement();
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS inventario_uepb");
                    JOptionPane.showMessageDialog(null, "Banco de dados criado com sucesso!");
                    conexao = DriverManager.getConnection(url1, usuario, senha);
                    status = true;
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Não foi possível criar o banco de dados!");
                    Logger.getLogger(ConexaoBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível realizar conexão com Banco de Dados! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }

        }
        return conexao;
    }
    
    public void closeConnection() {
        if (conexao != null) {
            try {
                conexao.close();
                status = false;
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showConfirmDialog(null, "Não foi possível fechar a conexão!", null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

