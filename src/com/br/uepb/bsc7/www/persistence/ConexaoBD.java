package com.br.uepb.bsc7.www.persistence;

import com.br.uepb.bsc7.www.UI.InventarioUI;
import com.br.uepb.bsc7.www.UI.ManipulaXLS;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexaoBD {

    private Connection conexao = null;
    private String host = null;
    private String url = null;
    private String driver = null;
    private String nomeBD = null;
    private String prefixoBD = null;
    private String portaBD = null;
    private String usuario = null;
    private String senha = "diego";
    private boolean status = false;

    public ConexaoBD() {
        driver = "com.mysql.jdbc.Driver";
        prefixoBD = "jdbc:mysql://";
        host = "localhost";
        portaBD = "3306";
        nomeBD = "inventario_uepb";

       url = prefixoBD + host + ":" + portaBD + "/" + nomeBD;
       //url = prefixoBD + host + "/" + nomeBD;
    }
    
    public boolean statusConnection(){
        return status;
    }

    public Connection getConnection(String user, String pswd) {
        usuario = user;
        senha = pswd;
        /*System.out.println("Usuário: "+usuario);
        System.out.println("Senha: "+senha);*/
        
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexão aberta!");
            status = true;
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == 1044 || e.getErrorCode() == 1045){
                JOptionPane.showMessageDialog(null, "Insira um nome de usuário e senha válidos!", null, JOptionPane.ERROR_MESSAGE);
                ManipulaXLS.continua = false;
                InventarioUI.setFocusUser();
                return null;
                
            }
            //Criar método que retorna uma mensagem de erro de acordo com o erro lançado
            System.out.println("SQLException em getConnection!");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException!");
            System.out.println("Fudeu!");
            Logger.getLogger(ConexaoBD.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return conexao;
    }
    
    public void closeConnection() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão fechada!");
                status = false;
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println("Não foi possível fechar a conexão!");
            }
        }
    }
}

