package com.br.uepb.bsc7.www.persistence;

import com.br.uepb.bsc7.www.UI.InventarioUI;
import com.br.uepb.bsc7.www.UI.ManipulaXLS;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexaoBD1 {

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
    private String url3 = null;
    private final String url4 = null;
    private String url = null;

    public ConexaoBD1() {
        driver = "org.h2.Driver";
        url = "jdbc:h2:./db/inventario_uepb";

    }

    public boolean statusConnection() {
        return status;
    }

    public Connection getConnection(String user, String pswd) {
        usuario = user;
        senha = pswd;
        usuario = "root";
        senha = "diego";

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException!");
            Logger.getLogger(ConexaoBD1.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        try {
            System.out.println(url);
            conexao = DriverManager.getConnection(url, usuario, senha);
            status = true;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            if (e.getErrorCode() == 28000) { //Caso usuário ou senha estejam inválidos
                JOptionPane.showMessageDialog(null, "Insira um nome de usuário e senha válidos!", null, JOptionPane.ERROR_MESSAGE);

            }

        }

        System.out.println("Status da conxão: " + status);
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
