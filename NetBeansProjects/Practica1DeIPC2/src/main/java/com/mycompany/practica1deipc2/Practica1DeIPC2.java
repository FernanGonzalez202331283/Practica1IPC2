/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practica1deipc2;

import InterfazGrafica.VistaDeInicio;
import conexion.DBConexion;
import javax.swing.SwingUtilities;

/**
 *
 * @author fernan
 */
public class Practica1DeIPC2 {

    public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
            DBConexion connection = new DBConexion();
            
            VistaDeInicio frameprincipal = new VistaDeInicio();
            frameprincipal.setVisible(true);
            
            
        });
    }
}
