/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

import conexion.DBConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author fernan
 */
public class PartidaDAO {
    public void guardarPartida(int idJugador, int idSucursal, int puntaje, int nivel, int pedidosCompletados){
        try (Connection con = DBConexion.getConnection()){
            String sql = """
                         INSERT INTO partida
                         (id_jugador, id_sucursal, puntaje_total, nivel_alcanzado, pedidos_completados)
                         VALUES (?,?,?,?,?)
                         """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ps.setInt(2, idSucursal);
            ps.setInt(3, puntaje);
            ps.setInt(4, nivel);
            ps.setInt(5, pedidosCompletados);
            
            ps.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
