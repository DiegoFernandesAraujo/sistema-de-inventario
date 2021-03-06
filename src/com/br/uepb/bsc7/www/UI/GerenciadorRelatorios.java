package com.br.uepb.bsc7.www.UI;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import static com.br.uepb.bsc7.www.UI.InventarioUI.getNomeRelatorio;
import com.br.uepb.bsc7.www.persistence.InventarioDAO;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JOptionPane;

/**
 *
 * @author Diego
 */
public class GerenciadorRelatorios extends javax.swing.JDialog {

  
    TableModel modeloTabela = null;

    /**
     * Creates new form GerenciadorRelatorios
     */
    InventarioUI objInventarioUI;
    InventarioDAO dao;
    //private String titRelatorio = "Relatório";

    /**
     *
     * @param parent
     * @param modal
     * @param modelo
     */
    
    //Construtor original
    public GerenciadorRelatorios(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        botPDF.setVisible(false);
    }
    
    public GerenciadorRelatorios(java.awt.Frame parent, boolean modal, TableModel modelo, InventarioDAO objDAO) {
        super(parent, modal);
        modeloTabela = modelo;
        dao = objDAO;
        initComponents();
        if (modelo.getRowCount() > 1) {
            quantitativos.setText(modelo.getRowCount() + " registros encontrados.");
        } else if (modelo.getRowCount() < 1) {
            quantitativos.setText("Nenhum registro encontrado.");
        } else {
            quantitativos.setText(modelo.getRowCount() + " registro encontrado.");
        }
        
        checkEmprest.setVisible(false);
        checkExtrav.setVisible(false);
        redimensionaTabela();
        botPDF.setVisible(false);
    }

    private void redimensionaTabela(){
        int linhas = modeloTabela.getRowCount();
        int colunas = modeloTabela.getColumnCount();
        String valor = null;
        Font font = getFont();
        FontMetrics metrics = this.getFontMetrics(font);
        int tam = 0;
        int largPainel = 0;
        
        int[] maxDimColunas = new int[colunas];//Armazena as dimensões dos maiores conteúdos de cada coluna
        
        for (int i = 0; i < colunas; i++) {
            int max = 0;
            
            for (int j = 0; j < linhas; j++) {
                    
                    if (modeloTabela.getValueAt(j, i) instanceof Integer) {
                        Integer valorInteger = (Integer) modeloTabela.getValueAt(j, i);
                        valor = valorInteger.toString();
                        
                    } else {
                        valor = (String) modeloTabela.getValueAt(j, i);
                        
                    }
                    
                try {
                    tam = metrics.stringWidth(valor);
                } catch (NullPointerException e) {
                    tam = 0;
                }
                
                
                if (tam > max) {
                    
                    max = tam;
                    
                }
            }
            maxDimColunas[i] = max + 30;
        }
        //Altera tamanhos das colunas
        for (int k = 0; k < colunas; k++) {
            if(maxDimColunas[k] > 200 && maxDimColunas[k] <= 1500){
                
                tabRelat.getColumnModel().getColumn( k ).setPreferredWidth( maxDimColunas[k] );
                largPainel = largPainel + maxDimColunas[k];
            
            }else{
               
                tabRelat.getColumnModel().getColumn( k ).setPreferredWidth( 100 );
                largPainel = largPainel + 150;
            }
        }
        //Redimensiona o painel      
        GerenciadorRelatorios.this.setSize(largPainel, 300);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelBotoes = new javax.swing.JPanel();
        botImprimir = new javax.swing.JButton();
        botXLS = new javax.swing.JButton();
        botPDF = new javax.swing.JButton();
        painelRelatorio = new javax.swing.JPanel();
        painelInfo = new javax.swing.JPanel();
        quantitativos = new javax.swing.JLabel();
        painelTabela = new javax.swing.JPanel();
        painelCheck = new javax.swing.JPanel();
        checkEmprest = new javax.swing.JCheckBox();
        checkExtrav = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabRelat = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relatórios");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        botImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/br/uepb/bsc7/www/images/icon_impressora2.png"))); // NOI18N
        botImprimir.setMnemonic(KeyEvent.VK_I);
        botImprimir.setText("Imprimir");
        botImprimir.setMaximumSize(new java.awt.Dimension(107, 25));
        botImprimir.setMinimumSize(new java.awt.Dimension(107, 25));
        botImprimir.setPreferredSize(new java.awt.Dimension(107, 25));
        botImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botImprimirActionPerformed(evt);
            }
        });

        botXLS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/br/uepb/bsc7/www/images/icon_xls.gif"))); // NOI18N
        botXLS.setText("Gerar XLS");
        botXLS.setMnemonic(KeyEvent.VK_X);
        botXLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botXLSActionPerformed(evt);
            }
        });

        botPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/br/uepb/bsc7/www/images/icon_pdf.png"))); // NOI18N
        botPDF.setText("Gerar PDF");
        botPDF.setMnemonic(KeyEvent.VK_P);
        botPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botPDFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelBotoesLayout = new javax.swing.GroupLayout(painelBotoes);
        painelBotoes.setLayout(painelBotoesLayout);
        painelBotoesLayout.setHorizontalGroup(
            painelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelBotoesLayout.createSequentialGroup()
                .addComponent(botImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(botXLS)
                .addGap(0, 0, 0)
                .addComponent(botPDF))
        );
        painelBotoesLayout.setVerticalGroup(
            painelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelBotoesLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(painelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botXLS)
                    .addComponent(botPDF)))
        );

        painelRelatorio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, getNomeRelatorio(), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        quantitativos.setText("Informações xxxxxxxxxx");

        javax.swing.GroupLayout painelInfoLayout = new javax.swing.GroupLayout(painelInfo);
        painelInfo.setLayout(painelInfoLayout);
        painelInfoLayout.setHorizontalGroup(
            painelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(quantitativos)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        painelInfoLayout.setVerticalGroup(
            painelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelInfoLayout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addComponent(quantitativos))
        );

        checkEmprest.setText("Emprestados");
        checkEmprest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkEmprestActionPerformed(evt);
            }
        });

        checkExtrav.setText("Extraviados");

        tabRelat.setModel(/*TabelaRelatorio.getTabela()*/modeloTabela);
        tabRelat.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(tabRelat);

        javax.swing.GroupLayout painelCheckLayout = new javax.swing.GroupLayout(painelCheck);
        painelCheck.setLayout(painelCheckLayout);
        painelCheckLayout.setHorizontalGroup(
            painelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelCheckLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(checkEmprest)
                .addGap(30, 30, 30)
                .addComponent(checkExtrav)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        painelCheckLayout.setVerticalGroup(
            painelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelCheckLayout.createSequentialGroup()
                .addGroup(painelCheckLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkEmprest)
                    .addComponent(checkExtrav))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        painelCheckLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {checkEmprest, checkExtrav});

        javax.swing.GroupLayout painelTabelaLayout = new javax.swing.GroupLayout(painelTabela);
        painelTabela.setLayout(painelTabelaLayout);
        painelTabelaLayout.setHorizontalGroup(
            painelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        painelTabelaLayout.setVerticalGroup(
            painelTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTabelaLayout.createSequentialGroup()
                .addComponent(painelCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout painelRelatorioLayout = new javax.swing.GroupLayout(painelRelatorio);
        painelRelatorio.setLayout(painelRelatorioLayout);
        painelRelatorioLayout.setHorizontalGroup(
            painelRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(painelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        painelRelatorioLayout.setVerticalGroup(
            painelRelatorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelRelatorioLayout.createSequentialGroup()
                .addComponent(painelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(painelTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addGap(29, 29, 29)
                    .addComponent(painelBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addComponent(painelRelatorio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(painelBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addComponent(painelRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botXLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botXLSActionPerformed
        File arq = null;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivo XLSX", "xlsx");
        fileChooser.setFileFilter(filtro);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            arq = fileChooser.getSelectedFile();
        }
        ManipulaXLS objManipula = new ManipulaXLS();
        try {
            objManipula.criaXLSX(getTabelaRelat(), arq.getAbsolutePath(), "Plan1");
            JOptionPane.showMessageDialog(null, "Relatório salvo!", null, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(GerenciadorRelatorios.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Relatório não pôde ser salvo!", null, JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ex){
            JOptionPane.showMessageDialog(null, "Relatório não pôde ser salvo!", null, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botXLSActionPerformed

    //Retorna a tabela em exibição
    public JTable getTabelaRelat(){
        return tabRelat;
    }
    
    private void botPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botPDFActionPerformed
        //new GerenciadorPDF().geraPDF(tabRelat);
    }//GEN-LAST:event_botPDFActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        dao.fechaConexao();
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void checkEmprestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkEmprestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkEmprestActionPerformed

    private void botImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botImprimirActionPerformed
        try {
            /*PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new GerenciadorImpressao());
            boolean ok = job.printDialog();
            if (ok) {
            try {
            job.print();
            } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível imprimir!", null, JOptionPane.ERROR_MESSAGE);
            }
            }*/
            tabRelat.print();
        } catch (PrinterException ex) {
            Logger.getLogger(GerenciadorRelatorios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botImprimirActionPerformed

    
    //Recebe a instância de InventarioUI para poder chamar seus métodos a partir desta
    public void setRefInventarioUI(InventarioUI obj){
        objInventarioUI = obj;
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botImprimir;
    private javax.swing.JButton botPDF;
    private javax.swing.JButton botXLS;
    private javax.swing.JCheckBox checkEmprest;
    private javax.swing.JCheckBox checkExtrav;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JPanel painelCheck;
    private javax.swing.JPanel painelInfo;
    private javax.swing.JPanel painelRelatorio;
    private javax.swing.JPanel painelTabela;
    private javax.swing.JLabel quantitativos;
    private javax.swing.JTable tabRelat;
    // End of variables declaration//GEN-END:variables
}
