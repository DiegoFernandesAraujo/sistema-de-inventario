/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.inventariouepb;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Diego
 */
abstract public class TabelaRelatorio extends JTable{
    
    
    
    

    public static TableModel getModeloTabela() {
        JTable tabela = new JTable();
        
        int opcao = Integer.parseInt(JOptionPane.showInputDialog("1 ou 2?"));
        
        if(opcao == 1){
        
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
        }else{
            
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
        }
        return tabela.getModel();
    }
    
    
}
