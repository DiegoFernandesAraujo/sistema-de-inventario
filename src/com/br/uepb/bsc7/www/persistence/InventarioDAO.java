package com.br.uepb.bsc7.www.persistence;

import com.br.uepb.bsc7.www.UI.InventarioUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class InventarioDAO {

    TabelaRelatorio relatorio = null;
    Connection conexao = null;
    static ConexaoBD cBD = null;
    String usuario = "";
    String senha = "";
    private InventarioUI objInventarioUI = null;

    public InventarioDAO() {
        cBD = new ConexaoBD();
    }

    public void setRefInventarioUI(InventarioUI obj) {
        objInventarioUI = obj;
    }

    //InventarioUI deve bloquear ou liberar componentes de acordo com o sucesso da conexão,
    //dessa forma login() deve retornar um booleano
    public boolean login(String user, String pswd) {
        //fazConexao();
        usuario = user;
        senha = pswd;
        conexao = cBD.getConnection(usuario, senha);

        boolean status = cBD.statusConnection();

        if (cBD.statusConnection()) {

            JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
            objInventarioUI.travaLogin();

        } else {
            //Código para insucesso da conexão
        }
        cBD.closeConnection();
        return status;
    }

    /*private void fazConexao(){
    //System.out.println("Conexão aberta!");
    conexao = cBD.getConnection(usuario, senha);
    
    }*/
    public void fechaConexao() {
        System.out.println("Fecha Conexão!");
        cBD.closeConnection();

    }

    //Retorna os valores da lista no formato ('Item1', 'Item2', 'Item3')
    public String toString(ArrayList<String> valores) {
        StringBuilder sb = new StringBuilder();
        for (String s : valores) {
            sb.append(",").append("'").append(s).append("'");
        }
        return sb.toString().replaceFirst(",", "");
    }

    //Por fazer ainda
    public void insereLinha(ArrayList<String> valores) {

        ArrayList<String> valoresBD = valores;
        int comprLinha = valoresBD.size();
        PreparedStatement st = null;
        //String sql = null;

        System.out.println("Método insereLinha chamado!");
        System.out.println(toString(valoresBD));

        try {
            //fazConexao();
            conexao = cBD.getConnection(usuario, senha);

            if (comprLinha > 0 && comprLinha <= 3) {
                //int index = 0;
                //for (String valor : valoresBD) {
                //Insere valor na tabela Acervo no índice 0
                //System.out.println(valor);
                String sql = "insert into acervo (tombo, verf, obs) VALUES"
                        + "(" + toString(valoresBD) + ");";

                conexao.prepareStatement(sql);

                st.executeUpdate(sql);
                //cBD.closeConnection();
            } else if (comprLinha >= 4) {
                int indice = 0;
                for (String valor : valoresBD) {
                    //Insere valor na tabela SIABI no índice atual
                    //indice++;
                    System.out.println(valor);
                }
            }

        } catch (SQLException e) {

            cBD.closeConnection();
            System.out.println("Deu erro!");
            System.out.println("SQL Exception em insereLinha");
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public TabelaRelatorio mostraTabelaAcervo() {
        String sql = "SELECT * FROM acervo;";
        try {
            //System.out.println("try do mostraTabelaAcertvo()");
            conexao = cBD.getConnection(usuario, senha);
            //System.out.println("Passou!");
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while ( rs.next() )
            {
            for ( int i = 1; i <= 2; i++ )
            System.out.printf( "%-8s\t", rs.getObject( i ) );
            System.out.println();*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }

    public TabelaRelatorio mostraTabelaAcervoParcial() {
        String sql = "SELECT pos FROM acervo WHERE MOD(pos,2)=0;";
        try {
            //System.out.println("try do mostraTabelaAcertvo()");
            conexao = cBD.getConnection(usuario, senha);
            //System.out.println("Passou!");
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while ( rs.next() )
            {
            for ( int i = 1; i <= 2; i++ )
            System.out.printf( "%-8s\t", rs.getObject( i ) );
            System.out.println();*/
            return new TabelaRelatorio(rs, metadados);

            //cBD.closeConnection();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }

    //Por fazer ainda
    /**
     *
     * @return
     */
    public int getNumLinhas() {
        return 0;
    }

    //Por fazer ainda
    public ArrayList<String> getLinha(int index) {
        ArrayList<String> linha = null;
        return linha;
    }

    //retorna todos dos encontrados nos dois acervos, sem considerar a situa��o.
    public TabelaRelatorio getItensLocalizados() {
        String sql = "SELECT ac1.tombo, ac1.titulo, ac1.autor, ac1.localizacao"
                + " FROM acervo_estante, acervo_siabi ac1"
                + " WHERE acervo_estante.cod_barras = ac1.tombo;";

        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while (rs.next()) {
            System.out.print(rs.getString(1) + " | ");
            System.out.print(rs.getString(2) + " | ");
            System.out.print(rs.getString(3) + " | ");
            System.out.println(rs.getString(4));
            }*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }

    public TabelaRelatorio getNaoLocalizados() {
        String sql = "SELECT asi.tombo, asi.situacao, asi.titulo, asi.localizacao, asi.autor"
                + " FROM acervo_siabi asi"
                + " where (asi.situacao <> \"02 - Emprestado\""
                + " AND asi.situacao <>  \"07 - Perdido por Leitor\""
                + " AND asi.situacao <> \"11 - Extraviado\""
                + " AND asi.situacao <> \"14 - Inexistente\""
                + " AND asi.situacao <> \"13 - Exemplar Exclu�do\")"
                + " AND asi.tombo NOT IN (SELECT cod_barras"
                + " FROM acervo_estante);";

        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while (rs.next()) {
            System.out.print(rs.getString(1) + " | ");
            System.out.print(rs.getString(2) + " | ");
            System.out.print(rs.getString(3) + " | ");
            System.out.println(rs.getString(4));
            }*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }

    //seleciona os que foram localizados nas estantes, mas "est�o" emprestados
    public TabelaRelatorio getLocalizadosMasEmprestados() {
        String sql = "SELECT ac1.tombo, ac1.titulo, ac1.autor, ac1.localizacao"
                + " FROM acervo_estante, acervo_siabi ac1"
                + " WHERE (ac1.situacao = '02 - Emprestado'"
                + " AND ac1.tombo = acervo_estante.cod_barras);";
        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while (rs.next()) {
            System.out.print(rs.getString(1) + " | ");
            System.out.print(rs.getString(2) + " | ");
            System.out.print(rs.getString(3) + " | ");
            System.out.println(rs.getString(4));
            }*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }
    
    //Retorna o valor da última linha
    public int getUltSeq(){
        String sql = "SELECT * FROM acervo_estante;";
        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            rs.last();
            return rs.getRow();
            
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            }
        finally{
            cBD.closeConnection();
            }
        return 0;
    }

    //alterado dia 22/12/2015
    //retorna as cdds dos livros vizinhos
    //adequar para os nomes das tabelas criadas
    public TabelaRelatorio getLocalizacaoDosVizinhos(/*int seq*/) {
        int seq = getUltSeq();
        String sql = "SELECT localizacao"
                + " FROM (select cod_barras"
                + " from acervo_estante2"
                + " where ((seq = " + seq + " + 1)"
                + " OR (seq =  " + seq + " + 2)"
                + " OR (seq =  " + seq + " - 2)"
                + " OR (seq =  " + seq + " - 1))) as vizinhos, acervo_siabi"
                + " WHERE cod_barras = tombo;";
        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while (rs.next()) {
            System.out.print(rs.getString(1) + " | ");
            System.out.print(rs.getString(2) + " | ");
            System.out.print(rs.getString(3) + " | ");
            System.out.println(rs.getString(4));
            }*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }

    // assume que todas as tabelas passadas como parametro possuem um numero de sequencia
    public TabelaRelatorio getTresUltimasLinhas(String nome_tabela) {
        String sql = "SELECT *"
                + " FROM (select count(*) as ult"
                + " from " + nome_tabela + ") as ultimo, "
                + nome_tabela + " where seq > (ult - 3);";

        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while (rs.next()) {
            System.out.print(rs.getString(1) + " | ");
            System.out.print(rs.getString(2) + " | ");
            System.out.print(rs.getString(3) + " | ");
            System.out.println(rs.getString(4));
            }*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }

    //lembrar de fechar as conexões, resultsets etc...
    public TabelaRelatorio getItensParaVerificacao() {
        String sql = "select *"
                + " from acervo_estante"
                + " where verificar is not null;";
        try {
            conexao = cBD.getConnection(usuario, senha);
            Statement st = conexao.prepareStatement(sql);
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            /*while (rs.next()) {
            System.out.print(rs.getString(1) + " | ");
            System.out.print(rs.getString(2) + " | ");
            System.out.print(rs.getString(3) + " | ");
            System.out.println(rs.getString(4));
            }*/
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }/*
        finally{
            
            }*/
        return null;
    }
}
