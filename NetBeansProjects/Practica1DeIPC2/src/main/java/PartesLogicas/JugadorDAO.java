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
public class JugadorDAO {
   public void actualizarJugador(int idJugador, int puntaje, int nivel){
       try (Connection con = DBConexion.getConnection()){
           String sql = """
                        UPDATE jugador
                        SET 
                            puntos = puntos + ?,
                            partidas_jugadas = partidas_jugadas + 1,
                            nivel = GREATEST(nivel, ?),
                            mejor_puntaje = GREATEST(mejor_puntaje, ?)
                        WHERE id_jugador = ?
                        """;
           PreparedStatement ps = con.prepareStatement(sql);
           
           ps.setInt(1, puntaje);
           ps.setInt(2, nivel);
           ps.setInt(3, puntaje);
           ps.setInt(4, idJugador);
           ps.executeUpdate();
           
           con.close();
           
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    
}
