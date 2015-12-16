/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.uepb.bsc7.www.persistence;

import java.util.ArrayList;

/**
 *
 * @author Diego
 */
public class TesteInsereBD {
    
    
    public void insereLinha(ArrayList<String> valores, int tam) {

        ArrayList<String> valoresBD = valores;
        System.out.println("Método insereLinha chamado!");
        
        if (tam == 3) {
            for (String valor : valoresBD) {
                //Insere valor na tabela Acervo no índice 0
                System.out.println(valor);
            }

        } else if (tam >= 4) {
            int indice = 0;
            for (String valor : valoresBD) {
                //Insere valor na tabela SIABI no índice atual
                //indice++;
                System.out.println(valor);
            }

        }
    }


    
}
