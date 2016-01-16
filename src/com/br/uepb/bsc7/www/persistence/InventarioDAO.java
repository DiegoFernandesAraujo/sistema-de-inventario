package com.br.uepb.bsc7.www.persistence;

import com.br.uepb.bsc7.www.UI.InventarioUI;
import com.br.uepb.bsc7.www.UI.ManipulaXLS;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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

    public void fazBackup() {

        String dir = "cd /d C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin";
        String dir86 = "cd /d C:\\Program Files(x86)\\MySQL\\MySQL Server 5.7\\bin";

        File bck = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivo SQL", "sql");
        fileChooser.setFileFilter(filtro);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            bck = fileChooser.getSelectedFile();
        }
        String arqBck = bck.getAbsolutePath() + ".sql";
        
        //Talvez seja melhor impedir de criar caso tenha espaço
        if (arqBck.contains(" ") || arqBck.contains("/") || arqBck.contains("\\") || arqBck.contains(":") || arqBck.contains("<") || arqBck.contains(">")) {
            JOptionPane.showMessageDialog(null, "O nome do arquivo não pode conter os caracteres: / \\ : * ? < > | ou espaços em branco.\nDigite um nome de arquivo válido.", null, JOptionPane.ERROR_MESSAGE);
            fazBackup();
        }
        /*System.out.println("Path: " + bck.getAbsolutePath());
        System.out.println("arqBck: " + arqBck);*/
        //Abre o prompt, executa os comandos e finaliza
        String comandoDir = "cmd /c " + dir + " && mysqldump.exe -u" + usuario + " -p" + senha + " " + "inventario_uepb" + " > " + arqBck;
        String comandoDir86 = "cmd /c " + dir86 + " && mysqldump.exe -u" + usuario + " -p" + senha + " " + "inventario_uepb" + " > " + arqBck;

        System.out.println("comandoDir: " + comandoDir);
        System.out.println("comandoDir32: " + comandoDir86);

        try {
            Runtime.getRuntime().exec(comandoDir);
            JOptionPane.showMessageDialog(null, "Backup realizado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            try {
                Runtime.getRuntime().exec(comandoDir86);
                JOptionPane.showMessageDialog(null, "Backup realizado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex2) {
                JOptionPane.showMessageDialog(null, "Erro ao tentar realizar backup! \n" + ex2.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void restauraBackup() {

        String dir = "cd /d C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin";
        String dir86 = "cd /d C:\\Program Files(x86)\\MySQL\\MySQL Server 5.7\\bin";

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            JFileChooser fileChooser = new JFileChooser();

            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivo SQL", "sql");
            fc.setFileFilter(filtro);

            File arq = fc.getSelectedFile();
            String endArq = arq.getAbsolutePath();

            String comandoDir = "cmd /c " + dir + " && mysql.exe -u" + usuario + " -p" + senha + " " + "inventario_uepb" + " < " + endArq;
            String comandoDir86 = "cmd /c " + dir86 + " && mysql.exe -u" + usuario + " -p" + senha + " " + "inventario_uepb" + " < " + endArq;
            //System.out.println("comando de restauração: " + comando2);

            try {
                Runtime.getRuntime().exec(comandoDir);
                JOptionPane.showMessageDialog(null, "Backup restaurado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                try {
                    Runtime.getRuntime().exec(comandoDir86);
                    JOptionPane.showMessageDialog(null, "Backup restaurado com sucesso!", null, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex2) {
                    JOptionPane.showMessageDialog(null, "Erro ao tentar restaurar o  backup! \n" + ex2.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    public void criaTabelas() {
        criaAcervoEstante();
        criaAcervoSIABI();
        criaTabelaExcluidos();
    }

    public void criaAcervoEstante() {
        String sql = "CREATE TABLE IF NOT EXISTS acervo_estante (\n"
                + "	 seq  int not null,\n"
                + "	 cod_barras  varchar (250) NOT NULL,\n"
                + "     verificar  varchar (5) NOT NULL,\n"
                + "     obs  varchar (250) NOT NULL,\n"
                + "	primary key (seq)\n"
                + ");";

        PreparedStatement st = null;
        //Statement st = null;

        try {

            conexao = cBD.getConnection(usuario, senha);
            st = conexao.prepareStatement(sql);
            //st = conexao.createStatement();
            //  st.executeUpdate(use);
            //st.executeUpdate(sql);
            st.execute();
            //st.close();

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

        //PreparedStatement st = null;
        Statement st = null;

        try {

            conexao = cBD.getConnection(usuario, senha);
            //st = conexao.prepareStatement(sql);
            st = conexao.createStatement();
            //    st.executeUpdate(use);
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
    
    //Cria tabela para armazenar linhas excluídas de acervo_estante a partir do método removeLinhaEstante()
    public void criaTabelaExcluidos() {
        String sql = "create table if not exists excluidos_estante (\n"
                + "seq int,\n"
                + "cod_barras varchar(250),\n"
                + "verificar varchar(5),\n"
                + "obs varchar(250),\n"
                + "primary key(seq));";

        PreparedStatement st = null;
        //Statement st = null;

        try {

            conexao = cBD.getConnection(usuario, senha);
            st = conexao.prepareStatement(sql);
            //st = conexao.createStatement();
            //  st.executeUpdate(use);
            //st.executeUpdate(sql);
            st.execute();
            //st.close();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Não foi possível criar Tabela excluidos_estante! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
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

            JOptionPane.showMessageDialog(null, "Não foi possível deletar Tabela acervo_siabi! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            cBD.closeConnection();
        }
    }
    
    public boolean deletaSIABI(String senha) {
        String sql = "DELETE FROM acervo_siabi;";

        PreparedStatement st = null;

        try {

            conexao = cBD.getConnection(usuario, senha);
            st = conexao.prepareStatement(sql);
            st.executeUpdate(sql);
            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Não foi possível deletar Tabela acervo_siabi! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            cBD.closeConnection();
        }
    }
    
    public boolean deletaAcervoEstante(String senha) {
        String sql = "DELETE FROM acervo_estante;";

        PreparedStatement st = null;

        try {

            conexao = cBD.getConnection(usuario, senha);
            st = conexao.prepareStatement(sql);
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Não foi possível deletar Tabela acervo_estante! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            cBD.closeConnection();
        }
    }
    
    public boolean deletaExcEstante(String senha) {
        String sql = "DELETE FROM excluidos_estante;";

        PreparedStatement st = null;

        try {

            conexao = cBD.getConnection(usuario, senha);
            st = conexao.prepareStatement(sql);
            st.executeUpdate(sql);
            return true;

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Não foi possível deletar Tabela excluidos_estante! \n Erro MYSQL " + e.getErrorCode() + "\n" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Arquivo não carregado!\ncatch - insereLinha()", null, JOptionPane.ERROR_MESSAGE);
            return false;
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
        String sql = "SELECT acervo_estante.seq, ac1.tombo, ac1.titulo, ac1.autor, ac1.localizacao"
                + " FROM acervo_estante, acervo_siabi ac1"
                + " WHERE acervo_estante.cod_barras = ac1.tombo ORDER BY acervo_estante.seq;";

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

    public TabelaRelatorio getItensLidos() {
        String sql = "SELECT acervo_estante.seq, ac1.tombo, ac1.titulo, ac1.autor, ac1.localizacao"
                + " FROM acervo_estante, acervo_siabi ac1"
                + "  ORDER BY acervo_estante.seq;";

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

    public TabelaRelatorio getExemplaresLidos() {
        String sql = "(SELECT DISTINCT *\n"
                + "FROM (SELECT acervo_estante.seq AS seq, cod_barras, localizacao, titulo, autor, ano, edicao, volume, patrimonio, rb, situacao  FROM acervo_estante LEFT JOIN acervo_siabi ON cod_barras = tombo) AS nao_cadastrados\n"
                + "WHERE localizacao IS NULL) \n"
                + "UNION\n"
                + "(SELECT DISTINCT *\n"
                + "FROM (SELECT acervo_estante.seq AS seq, tombo, localizacao, titulo, autor, ano, edicao, volume, patrimonio, rb, situacao FROM acervo_estante LEFT JOIN acervo_siabi ON cod_barras = tombo) AS cadastrados\n"
                + "WHERE localizacao IS NOT NULL) \n"
                + " ORDER BY seq;";

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
    
    public TabelaRelatorio getExemplaresLidosBKP() {
        String sql = "SELECT * FROM acervo_estante;";

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
        String sql = "SELECT asi.tombo, asi.patrimonio, asi.situacao, asi.titulo, asi.localizacao, asi.autor"
                + " FROM acervo_siabi asi"
                + " WHERE (asi.situacao <> \"02 - Emprestado\""
                + " AND asi.situacao <>  \"07 - Perdido por Leitor\""
                + " AND asi.situacao <> \"11 - Extraviado\""
                + " AND asi.situacao <> \"14 - Inexistente\""
                + " AND asi.situacao <> \"13 - Exemplar Excluído\")"
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
        /*String sql = "SELECT tombo, localizacao, "
        + " FROM (SELECT cod_barras"
        + " FROM acervo_estante"
        + " WHERE ((seq = " + seq + " - 2) AS ant2"
        + " OR (seq =  " + seq + " - 1) AS ant1"
        + " OR (seq = " + seq + "+ 1) AS post2"
        + " OR (seq =  " + seq + " +2) AS post1)) AS vizinhos, acervo_siabi"
        + " WHERE cod_barras = tombo;";*/
        String sqlx = null, sql0 = null, sql1 = null, sql2 = null, sql3 = null, sql4 = null, sql5 = null, sql6 = null;

        try {

            conexao = cBD.getConnection(usuario, senha);

            String sqlAntiga = "SELECT seq, cod_barras FROM acervo_estante"
                    + " WHERE cod_barras NOT IN (SELECT tombo FROM acervo_siabi);";

            PreparedStatement stAntiga = conexao.prepareStatement(sqlAntiga);
            stAntiga.execute(sqlAntiga);

            ResultSet rs1 = stAntiga.getResultSet();
            rs1.last();
            int tam = rs1.getRow();
            rs1.beforeFirst();
            int seqInt = 0;

            sqlx = "DROP TEMPORARY TABLE IF EXISTS vizinhos;";
            PreparedStatement stx = conexao.prepareStatement(sqlx);
            stx.execute(sqlx);

            sql0 = "Create  temporary table IF NOT EXISTS vizinhos(\n"
                    + "anterior2 varchar(45) default '-',\n"
                    + "anterior1 varchar(45) default '-',\n"
                    + "item_nao_cad varchar(45) default '-',\n"
                    + "posterior1 varchar(45) default '-',\n"
                    + "posterior2 varchar(45) default '-'\n"
                    + ");\n"
                    + "\n";

            PreparedStatement st0 = conexao.prepareStatement(sql0);
            st0.execute(sql0);

            //for (int i = 1; i <= tam; i++) {
            while (rs1.next()) {
                seqInt = rs1.getInt(1);
                System.out.println("seq: " + seqInt);

                sql1 = "set @anterior2:= (select localizacao\n"
                        + "FROM (select seq, cod_barras\n"
                        + "from acervo_estante\n"
                        + "where seq = " + (seqInt - 2) + ") sequenciais\n"
                        + "left join acervo_siabi\n"
                        + "on sequenciais.cod_barras = tombo order by sequenciais.seq); \n"
                        + "\n"
                        + "\n";
                PreparedStatement st1 = conexao.prepareStatement(sql1);
                st1.execute(sql1);

                sql2 = "set @anterior1:= (select localizacao\n"
                        + "FROM (select seq, cod_barras\n"
                        + "from acervo_estante\n"
                        + "where seq = " + (seqInt - 1) + ")  sequenciais\n"
                        + "left join acervo_siabi\n"
                        + "on sequenciais.cod_barras = tombo order by sequenciais.seq);\n"
                        + "\n"
                        + "\n";
                PreparedStatement st2 = conexao.prepareStatement(sql2);
                st2.execute(sql2);

                sql3 = "set @posterior1:= (select localizacao\n"
                        + "FROM (select seq, cod_barras\n"
                        + "from acervo_estante\n"
                        + "where seq = " + (seqInt + 1) + ") sequenciais\n"
                        + "left join acervo_siabi\n"
                        + "on sequenciais.cod_barras = tombo order by sequenciais.seq);\n"
                        + "\n";
                PreparedStatement st3 = conexao.prepareStatement(sql3);
                st3.execute(sql3);

                sql4 = "set @posterior2:= (select localizacao\n"
                        + "FROM (select seq, cod_barras\n"
                        + "from acervo_estante\n"
                        + "where seq = " + (seqInt + 2) + ") sequenciais\n"
                        + "left join acervo_siabi\n"
                        + "on sequenciais.cod_barras = tombo order by sequenciais.seq);\n"
                        + "\n";
                PreparedStatement st4 = conexao.prepareStatement(sql4);
                st4.execute(sql4);

                sql5 = "set @seqJava:= (select cod_barras\n"
                        + "FROM (select seq, cod_barras\n"
                        + "from acervo_estante\n"
                        + "where seq = " + (seqInt) + ") sequenciais\n"
                        + "left join acervo_siabi\n"
                        + "on sequenciais.cod_barras = tombo order by sequenciais.seq);\n";
                PreparedStatement st5 = conexao.prepareStatement(sql5);
                st5.execute(sql5);

                sql6 = "insert into vizinhos (anterior2, anterior1, item_nao_cad, posterior1, posterior2) values (@anterior2, @anterior1, @seqJava, @posterior1, @posterior2);";
                PreparedStatement st6 = conexao.prepareStatement(sql6);
                st6.execute(sql6);

            }//O for foi deposto

            String sql7 = "SELECT * FROM vizinhos;";

            PreparedStatement st7 = conexao.prepareStatement(sql7);
            st7.execute(sql7);

            ResultSet rs = st7.getResultSet();
            ResultSetMetaData metadados = rs.getMetaData();
            return new TabelaRelatorio(rs, metadados);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }
        return null;
    }

    //Deve retornar a informação  de que o exemplar está cadastrado no SIABI assim que o código seja lido
    public boolean estahCadastradoNoSIABI(String codigo) {

        String sql = "SELECT tombo FROM acervo_siabi WHERE '" + codigo + "' = tombo;";

        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql);
            st.execute(sql);
            Object obj = null;
            try {
                ResultSet rs = st.getResultSet();
                rs.last();
                obj = rs.getObject(1);
                rs.close();
            } catch (SQLException ex) {
                System.out.println(ex.getErrorCode());
                ex.printStackTrace();
            }

            return obj != null;

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();

        }
        return false;
    }
    
    public void deletaTabelas(String senha){
        
        if( deletaAcervoEstante(senha) && deletaSIABI(senha) && deletaExcEstante(senha)){
            JOptionPane.showMessageDialog(null, "Todos os dados foram apagados!", null, JOptionPane.INFORMATION_MESSAGE);
        }
    
    }
    
    
    public void removeLinhaEstante(int seq) {

       //Armazena na tabela excluidos_estante a linha a ser removida de acervo_estante para fins de possível restauração dos valores excluídos
        String sql = "INSERT INTO excluidos_estante (SELECT * FROM acervo_estante WHERE seq = " + seq + ");";
        String sql2 = "DELETE FROM acervo_estante WHERE seq = " + seq + ");";
        
        try {
            conexao = cBD.getConnection(usuario, senha);
            
            PreparedStatement st = conexao.prepareStatement(sql);
            st.execute(sql);
            
            PreparedStatement st2 = conexao.prepareStatement(sql2);
            st.execute(sql2);
       
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();
        }
       
    }
    
    public void restauraLinhaEstante(int seq) {

       //Recupera da tabela excluidos_estante a linha antes removida de acervo_estante
        String sql = "INSERT INTO acervo_estante (SELECT * FROM excluidos_estante WHERE seq = " + seq + ");";
        String sql2 = "DELETE FROM excluidos_estante WHERE seq = " + seq + ");";
        
        try {
            conexao = cBD.getConnection(usuario, senha);
            
            PreparedStatement st = conexao.prepareStatement(sql);
            st.execute(sql);
            
            PreparedStatement st2 = conexao.prepareStatement(sql2);
            st.execute(sql2);
       
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            e.printStackTrace();
            cBD.closeConnection();
        }
       
    }
    
    public TabelaRelatorio getExcluidosEstante() {
        String sql = "SELECT * FROM excluidos_estante;";
        
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

        //Acho que tem que ordenar e limitar a quantidade exibida ainda
        String sql2 = "SELECT DISTINCT cod_barras, IF (tombo = cod_barras, localizacao, '') AS localizacao "
                + "FROM (SELECT tombo, localizacao, cod_barras "
                + "FROM acervo_siabi, acervo_estante "
                + "WHERE ((acervo_estante.seq = " + seq + " - 1) OR (acervo_estante.seq = " + seq + "))) AS ultimos LIMIT 2;";
        //System.out.println(sql);

        String sql3 = "SELECT ultimos.cod_barras, localizacao "
                + "FROM (SELECT cod_barras, seq "
                + "FROM acervo_estante ae, (SELECT COUNT(*) AS tamanho FROM acervo_estante) total "
                + "WHERE (ae.seq = " + (seq - 3) + " OR ae.seq = " + (seq - 2) + " OR ae.seq = " + (seq - 1) + " OR ae.seq = " + seq + ")) ultimos "
                + "LEFT JOIN acervo_siabi "
                + "ON ultimos.cod_barras = tombo ORDER BY ultimos.seq;";

        String sql = "SELECT tombo, localizacao"
                + " FROM (SELECT cod_barras"
                + " FROM acervo_estante"
                + " WHERE ((seq = " + seq + " - 1)"
                + " OR (seq =  " + seq + "))) AS vizinhos, acervo_siabi"
                + " WHERE cod_barras = tombo;";
        try {
            conexao = cBD.getConnection(usuario, senha);
            PreparedStatement st = conexao.prepareStatement(sql3);
            st.execute(sql3);
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
                + " WHERE verificar = 'SIM';";
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
