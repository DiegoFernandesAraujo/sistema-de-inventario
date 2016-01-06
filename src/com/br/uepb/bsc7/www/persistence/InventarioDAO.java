package com.br.uepb.bsc7.www.persistence;

import com.br.uepb.bsc7.www.UI.InventarioUI;
import com.br.uepb.bsc7.www.UI.ManipulaXLS;
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
    private int numSeq;
    private final String use = "use inventario_uepb;";
    private int seq;

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
            criaTabelas();
            objInventarioUI.travaLogin();

        } else {
            //Código para insucesso da conexão
        }
        //cBD.closeConnection();
        return status;
    }

    /*private void fazConexao(){
    //System.out.println("Conexão aberta!");
    conexao = cBD.getConnection(usuario, senha);
    
    }*/
    public void fechaConexao() {
        //System.out.println("Fecha Conexão!");
        cBD.closeConnection();
       

    }

    //Retorna os valores da lista no formato 'Item1', 'Item2', 'Item3'
    public String toString(ArrayList<String> valores) {
        StringBuilder sb = new StringBuilder();
        for (String s : valores) {
            s = s.replaceAll("'", "''");//Caso encontre algum nome que contenha apóstrofo
            sb.append(",").append("'").append(s).append("'");
        }
        return sb.toString().replaceFirst(",", "");
    }
        
    public void criaTabelas(){
        criaAcervoEstante();
        criaAcervoSIABI();
    }
    
    public void criaAcervoEstante(){
        String sql = "CREATE TABLE IF NOT EXISTS acervo_estante (\n"
                + "	 seq  int not null,\n"
                + "	 cod_barras  varchar (25) NOT NULL,\n"
                + "     verificar  varchar (5) NOT NULL,\n"
                + "     obs  varchar (30) NOT NULL,\n"
                + "	primary key (seq)\n"
                + ");";
        
        PreparedStatement st = null;
        
        try {
            
            conexao = cBD.getConnection(usuario, senha);

            
                st = conexao.prepareStatement(sql);

                st.executeUpdate(use);
                st.executeUpdate(sql);
                
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null, "Não foi possível criar Tabela acervo_estante! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            
            return;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            
            return;
        } finally{
            cBD.closeConnection();
        }
    }
    
    public void criaAcervoSIABI(){
        String sql = "   CREATE TABLE IF NOT EXISTS acervo_siabi (\n"
                + "	\n"
                + "	seq  integer not null,\n"
                + "    patrimonio  varchar(45) null,\n"
                + "    tombo  varchar(25) null,\n"
                + "    localizacao  varchar(45) null,\n"
                + "    autor  varchar(255) null,\n"
                + "    titulo  varchar(255) null,\n"
                + "    edicao  varchar(45) null,\n"
                + "    ano  varchar(45) null,\n"
                + "    volume  varchar(45) null,\n"
                + "    tomo  varchar(45) null,\n"
                + "    valor  varchar(25) null,\n"
                + "    nota_fiscal  varchar(45) null,\n"
                + "    empenho  varchar(45) null,\n"
                + "    rb  varchar(25) null,\n"
                + "    situacao  varchar(45) null,\n"
                + "   primary key ( seq )\n"
                + "   );";
 
        PreparedStatement st = null;
        
        try {
            
            conexao = cBD.getConnection(usuario, senha);

            
                st = conexao.prepareStatement(sql);

                st.executeUpdate(use);
                st.executeUpdate(sql);
            
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null, "Não foi possível criar Tabela acervo_siabi! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            
            return;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            
            return;
        } finally{
            cBD.closeConnection();
        }
    }
    
    public void deletaSIABI(){
        String sql = "DELETE FROM acervo_siabi;";
        
        System.out.println(sql);
        
        PreparedStatement st = null;
        
        try {
            
            conexao = cBD.getConnection(usuario, senha);

            
                st = conexao.prepareStatement(sql);

                st.executeUpdate(sql);
            
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null, "Não foi deletar Tabela acervo_siabi! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            
            return;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            
            return;
        } finally{
            cBD.closeConnection();
        }
    }

    //Por fazer ainda
    public void insereLinha(ArrayList<String> valores) {

        ArrayList<String> valoresBD = valores;
        int comprLinha = valoresBD.size();
        PreparedStatement st = null;
        //String sql = null;

        //System.out.println("Método insereLinha chamado!");
        //System.out.println(toString(valoresBD));
        numSeq = getNumLinhas();
        //System.out.println("Em insere linha:" + numSeq);
        numSeq++;
        
        try {
            //fazConexao();
            conexao = cBD.getConnection(usuario, senha);

            //Inserção na tabela acervo_estante apenas com código de barras
            if (comprLinha == 1) {
                //int index = 0;
                //for (String valor : valoresBD) {
                //Insere valor na tabela Acervo no índice 0
                //System.out.println(valor);
                String sql = "INSERT INTO acervo_estante (seq, cod_barras) VALUES"
                        + "(" + numSeq + "," + toString(valoresBD) + ");";

                st = conexao.prepareStatement(sql);

                st.executeUpdate(sql);
                //cBD.closeConnection();
              
              //Inserção na tabela acervo_estante com três colunas  
            }else if (comprLinha > 0 && comprLinha <= 3) {
                String sql = "INSERT INTO acervo_estante (seq, cod_barras, verificar, obs) VALUES"
                        + "(" + numSeq + ", " + toString(valoresBD) + ");";

                st = conexao.prepareStatement(sql);

                st.executeUpdate(sql);
            }
            //Inserção na tabela acervo_siabi  
            else if (comprLinha >= 4) {
                //System.out.println("Inserindo SIABI");
                //Se não for a primeira linha da planilha original do SIABI
                if (!"SEQ.".equals(valoresBD.get(0))) {
                    
                    //PARA PODER UTILIZAR A PLANILHA SIABI SEM EDITÁ-LA:
                    
                    //tentar retirar o primeiro valor (SEQ), converter em int e inserir direto no BD
                    //System.out.println(valoresBD.get(0));
                    seq = Integer.parseInt(valoresBD.get(0));
                    
                    //System.out.println(seq);
                    
                    /*st = conexao.prepareStatement(sql);
                    
                    st.executeUpdate(sql);*/
                    StringBuilder sb = new StringBuilder();
                    
                    String temp = toString(valoresBD);
                    //System.out.println(valoresBD.get(0));
                    //String valorSeq = sb.append("'").append((Integer) seq.)
                    /*StringBuilder primValor = sb.append("'").append(valoresBD.get(0)).append("'").append(",");
                    String s = temp.replaceFirst(primValor.toString(), "");*/
                    
                    StringBuilder primValor = sb.append("'").append(valoresBD.get(0)).append("'").append(",");
                    String s = temp.replaceFirst(primValor.toString(), Integer.toString(seq)+",");
                    
                    /*String sql2 = "INSERT INTO acervo_siabi2 (seq, patrimonio, tombo, localizacao, autor, titulo, edicao, ano, volume, tomo, valor, nota_fiscal, empenho, rb, situacao) VALUES"
                    + "(" + seq + "," + s + ");"; //tentar fazer toString menos primeiro elemento*/
                    
                    String sql2 = "INSERT INTO acervo_siabi (seq, patrimonio, tombo, localizacao, autor, titulo, edicao, ano, volume, tomo, valor, nota_fiscal, empenho, rb, situacao) VALUES "
                    + "(" + s + ");";
                    
                    //System.out.println(sql2);
                    st = conexao.prepareStatement(sql2);
                    
                    st.executeUpdate(sql2);
                    /*InventarioUI.labelPorc.setText("Registro " + seq + " inserido!");
                    InventarioUI.labelPorc.repaint();*/
                    //objInventarioUI.setLabelPorc(seq);
                }
                
            }
            
            //System.out.println("Em insere linha, após inserção:" + numSeq);

        } catch (SQLException e) {

            //System.out.println("Deu erro!");
            System.out.println("SQL Exception em insereLinha");
            //System.out.println(e.getErrorCode());
            JOptionPane.showMessageDialog(null, "Não foi possível inserir dados no Banco de Dados! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            /*System.out.println(e.getSQLState());
            System.out.println(e.getMessage());*/
            ManipulaXLS.continua = false;
            return;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            ManipulaXLS.continua = false;
            return;
        } finally{
            cBD.closeConnection();
        }

    }
    
    public void removerUltimaLinha(){
	String sql = "DELETE FROM acervo_estante WHERE ("
                   + " SELECT seq FROM (SELECT COUNT(*) ultimo FROM acervo_estante) AS tamanho WHERE seq = ultimo);";
        if(getNumLinhas() != 0){
        
        try {
            conexao = cBD.getConnection(usuario, senha);
            Statement st = conexao.prepareStatement(sql);
            st.execute(sql);
            JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possíel desfazer a última inserção!");
        }finally{
            cBD.closeConnection();
        }
        }else{
        JOptionPane.showMessageDialog(null, "Não há valores a serem removidos", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    /*public TabelaRelatorio mostraTabelaAcervo() {
    String sql = "SELECT * FROM acervo_estante;";
    try {
    //System.out.println("try do mostraTabelaAcertvo()");
    conexao = cBD.getConnection(usuario, senha);
    //System.out.println("Passou!");
    PreparedStatement st = conexao.prepareStatement(sql);
    st.executeQuery(sql);
    ResultSet rs = st.getResultSet();
    ResultSetMetaData metadados = rs.getMetaData();
    
    return new TabelaRelatorio(rs, metadados);
    
    } catch (SQLException e) {
    System.out.println(e.getErrorCode());
    e.printStackTrace();
    cBD.closeConnection();
    
    }
    return null;
    }*/

    /*public TabelaRelatorio mostraTabelaAcervoParcial() {
    String sql = "SELECT seq FROM acervo_estante WHERE MOD(pos,2)=0;";
    try {
    //System.out.println("try do mostraTabelaAcertvo()");
    conexao = cBD.getConnection(usuario, senha);
    //System.out.println("Passou!");
    PreparedStatement st = conexao.prepareStatement(sql);
    st.executeQuery(sql);
    ResultSet rs = st.getResultSet();
    ResultSetMetaData metadados = rs.getMetaData();
    
    return new TabelaRelatorio(rs, metadados);
    
    //cBD.closeConnection();
} catch (SQLException e) {
System.out.println(e.getErrorCode());
e.printStackTrace();
cBD.closeConnection();

}
return null;
}*/

    //Por fazer ainda
    /**
     *
     * @return
     */
    //Retorna o número de linhas de acervo_estante
    public int getNumLinhas() {
        int tamTab = 0;
        String sql = "Select count(*) as total from acervo_estante";

        try {

            conexao = cBD.getConnection(usuario, senha);
            Statement st = conexao.prepareStatement(sql);
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            rs.last();
            tamTab = rs.getInt(1);
            //numSeq++;

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();

        } finally {
            cBD.closeConnection();
        }
        System.out.println(tamTab);
        return tamTab;
    }

    //Por fazer ainda
    public ArrayList<String> getLinha(int index) {
        ArrayList<String> linha = null;
        return linha;
    }

    //retorna todos dos encontrados nos dois acervos, sem considerar a situa��o.
    public TabelaRelatorio getLocalizados() {
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
                + " WHERE (asi.situacao <> \"02 - Emprestado\""
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
    
    public TabelaRelatorio getNaoCadastrados(){
		String sql = "select seq, cod_barras from acervo_estante"  
				+ " where cod_barras not in (select tombo from acervo_siabi);";
		
		try{
		conexao = cBD.getConnection(usuario, senha);	
                    PreparedStatement st = conexao.prepareStatement(sql);
			st.execute(sql);
			ResultSet rs = st.getResultSet();
                         ResultSetMetaData metadados = rs.getMetaData();
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
    public TabelaRelatorio getLocalizacaoDosVizinhos(int seq) {
        //System.out.println(seq);
        String sql = "SELECT tombo, localizacao"
                + " FROM (SELECT cod_barras"
                + " FROM acervo_estante"
                + " WHERE ((seq = " + seq + " - 1)"
                + " OR (seq =  " + seq + "))) AS vizinhos, acervo_siabi"
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
    
    /*public TabelaRelatorio getTomboDosVizinhos(int seq) {
    
    String sql = "SELECT tombo"
    + " FROM (SELECT cod_barras"
    + " FROM acervo_estante"
    + " WHERE ((seq = " + seq + " - 2)"
    + " OR (seq =  " + seq + " - 1))) AS vizinhos, acervo_siabi"
    + " WHERE cod_barras = tombo;";
    try {
    conexao = cBD.getConnection(usuario, senha);
    PreparedStatement st = conexao.prepareStatement(sql);
    st.execute(sql);
    ResultSet rs = st.getResultSet();
    ResultSetMetaData metadados = rs.getMetaData();
    
    return new TabelaRelatorio(rs, metadados);
    
    } catch (SQLException e) {
    System.out.println(e.getErrorCode());
    e.printStackTrace();
    cBD.closeConnection();
    
    }
    return null;
    }*/

    // assume que todas as tabelas passadas como parametro possuem um numero de sequencia
    public TabelaRelatorio getTresUltimasLinhas() {
        String sql = "SELECT cod_barras, verificar, obs"
                + " FROM (SELECT COUNT(*) AS ult"
                + " FROM acervo_estante) AS ultimo, "
                + "acervo_estante WHERE seq > (ult - 3);";

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
        String sql = "SELECT *"
                + " FROM acervo_estante"
                + " WHERE verificar IS NOT NULL;";
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
