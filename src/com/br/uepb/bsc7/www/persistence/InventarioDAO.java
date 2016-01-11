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

        usuario = user;
        senha = pswd;
        conexao = cBD.getConnection(usuario, senha);

        boolean status = cBD.statusConnection();

        if (status) {

            JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
            criaTabelas();
            objInventarioUI.travaLogin();

        } else {
            //Código para insucesso da conexão
        }
        return status;
    }

    public void fechaConexao() {

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

    public void criaTabelas() {
        criaAcervoEstante();
        criaAcervoSIABI();
    }

    public void criaAcervoEstante() {
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
        } finally {
            cBD.closeConnection();
        }
    }

    public void criaAcervoSIABI() {
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
        } finally {
            cBD.closeConnection();
        }
    }

    public void deletaSIABI() {
        String sql = "DELETE FROM acervo_siabi;";

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
        } finally {
            cBD.closeConnection();
        }
    }

    public void insereLinha(ArrayList<String> valores) {

        ArrayList<String> valoresBD = valores;
        int comprLinha = valoresBD.size();
        PreparedStatement st = null;
        //System.out.println(toString(valoresBD));
        numSeq = getNumLinhas();
        numSeq++;

        try {
            conexao = cBD.getConnection(usuario, senha);

            //Inserção na tabela acervo_estante apenas com código de barras
            if (comprLinha == 1) {
                String sql = "INSERT INTO acervo_estante (seq, cod_barras) VALUES"
                        + "(" + numSeq + "," + toString(valoresBD) + ");";

                st = conexao.prepareStatement(sql);
                st.executeUpdate(sql);

                //Inserção na tabela acervo_estante com três colunas (PADRÃO)
            } else if (comprLinha > 0 && comprLinha <= 3) {
                String sql = "INSERT INTO acervo_estante (seq, cod_barras, verificar, obs) VALUES"
                        + "(" + numSeq + ", " + toString(valoresBD) + ");";

                st = conexao.prepareStatement(sql);
                st.executeUpdate(sql);
            } //Inserção na tabela acervo_siabi  
            else if (comprLinha >= 4) {
                //Se não for a primeira linha da planilha original do SIABI
                if (!"SEQ.".equals(valoresBD.get(0))) {
                    /*PARA PODER UTILIZAR A PLANILHA SIABI SEM EDITÁ-LA:
                    Retira o primeiro valor (SEQ), converte em int e insere direto no BD*/

                    seq = Integer.parseInt(valoresBD.get(0));

                    StringBuilder sb = new StringBuilder();

                    String temp = toString(valoresBD);

                    StringBuilder primValor = sb.append("'").append(valoresBD.get(0)).append("'").append(",");

                    String s = temp.replaceFirst(primValor.toString(), Integer.toString(seq) + ",");

                    String sql = "INSERT INTO acervo_siabi (seq, patrimonio, tombo, localizacao, autor, titulo, edicao, ano, volume, tomo, valor, nota_fiscal, empenho, rb, situacao) VALUES "
                            + "(" + s + ");";

                    st = conexao.prepareStatement(sql);

                    st.executeUpdate(sql);

                    //Tentativa de exibir na UI os valores de sequência sendo inseridos:
                    InventarioUI.labelPorc.setText("Registro " + seq + " inserido!");
                    objInventarioUI.setBarraProg(seq);
                    /*InventarioUI.labelPorc.setText("Registro " + seq + " inserido!");
                    InventarioUI.labelPorc.repaint();*/
                    //objInventarioUI.setLabelPorc(seq);
                    
                }

            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Não foi possível inserir dados no Banco de Dados! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            ManipulaXLS.continua = false;
            return;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            ManipulaXLS.continua = false;
            return;
        } finally {
            cBD.closeConnection();
        }

    }

    public void removerUltimaLinha() {
        String sql = "DELETE FROM acervo_estante WHERE ("
                + " SELECT seq FROM (SELECT COUNT(*) ultimo FROM acervo_estante) AS tamanho WHERE seq = ultimo);";
        if (getNumLinhas() != 0) {

            try {
                conexao = cBD.getConnection(usuario, senha);
                Statement st = conexao.prepareStatement(sql);
                st.execute(sql);
                JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Não foi possíel desfazer a última inserção!");
            } finally {
                cBD.closeConnection();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Não há valores a serem removidos", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    //Retorna o número de linhas de acervo_estante
    public int getNumLinhas() {
        int tamTab = 0;
        String sql = "Select count(*) as total from acervo_estante";

        try {

            conexao = cBD.getConnection(usuario, senha);
            Statement st = conexao.prepareStatement(sql);
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            rs.last();//Vai para a última linha
            tamTab = rs.getInt(1);//Retorna o valor da linha na primeira coluna
    
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();

        } finally {
            cBD.closeConnection();
        }
        
        return tamTab;
    }

    //Por fazer ainda
    public ArrayList<String> getLinha(int index) {
        ArrayList<String> linha = null;
        return linha;
    }

    //Retorna todos dos encontrados nos dois acervos, sem considerar a situacao.
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
            
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }
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
            
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }
        return null;
    }

    public TabelaRelatorio getNaoCadastrados() {
        String sql = "SELECT tombo, localizacao, "
                + " FROM (SELECT cod_barras"
                + " FROM acervo_estante"
                + " WHERE ((seq = " + seq + " - 2) AS ant2"
                + " OR (seq =  " + seq + " - 1) AS ant1"
                + " OR (seq = " + seq + "+ 1) AS post2"
                + " OR (seq =  " + seq + " +2) AS post1)) AS vizinhos, acervo_siabi"
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
    }

    //Seleciona os que foram localizados nas estantes, mas "est�o" emprestados
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
       
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }
        return null;
    }

    //Retorna o valor da última linha
    public int getUltSeq() {
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
        } finally {
            cBD.closeConnection();
        }
        return 0;
    }

    
    //Retorna cdd e tombo dos dois últimos valores lidos, se estes estiverem catalogados no SIABI
    public TabelaRelatorio getLocalizacaoDosVizinhos(int seq) {
    
        String sql = "SELECT cod_barras, localizacao"
                + " FROM (SELECT cod_barras"
                + " FROM acervo_estante"
                + " WHERE ((seq = " + seq + " - 1)"
                + " OR (seq =  " + seq + "))) AS vizinhos;";
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
    }

    //Retorna as três últimas linhas da tabela acervo_estante
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
            
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();
        }
        return null;
    }

    //Retorna os itens marcados para serem verificados
    public TabelaRelatorio getItensParaVerificacao() {
        String sql = "SELECT *"
                + " FROM acervo_estante"
                + " WHERE verificar IS 'SIM';";
        try {
            conexao = cBD.getConnection(usuario, senha);
            Statement st = conexao.prepareStatement(sql);
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
    }
}
