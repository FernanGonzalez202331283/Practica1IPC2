/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

import conexion.DBConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fernan
 */
public class ReportesDao {
    public ResultSet obtenerPartidasSucursal(int idSucursal) throws SQLException{
        Connection con = DBConexion.getConnection();
        
        String sql = """
                     SELECT
                     u.nombre,
                     p.puntaje_total,
                     p.nivel_alcanzado,
                     p.pedidos_completados,
                     p.pedidos_cancelados,
                     p.pedidos_no_entregados
                     FROM partida p
                     JOIN usuario u ON p.id_jugador = u.id_usuario
                     WHERE p.id_sucursal = ?
                     """;
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSucursal);
            
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet obtenerRankingSucursal(int idSucursal) throws SQLException{
        Connection con = DBConexion.getConnection();
        String sql = """
                     SELECT 
                     u.nombre,
                     j.puntos,
                     j.nivel,
                     j.partidas_jugadas,
                     j.mejor_puntaje
                     FROM jugador j
                     JOIN usuario u ON j.id_jugador = u.id_usuario
                     WHERE u.id_sucursal = ?
                     ORDER BY j.puntos DESC
                     """;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSucursal);
            
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet obtenerEstadisticasGlobales() throws SQLException{
         ResultSet rs = null;

    try{

        Connection con = DBConexion.getConnection();

        String sql = """
                      SELECT
                      COUNT(*) AS total_partidas,
                      AVG(puntaje_total) AS promedio_puntos,
                      MAX(puntaje_total) AS mejor_puntaje,
                      SUM(pedidos_completados) AS total_pedidos_completados,
                      SUM(pedidos_cancelados) AS total_pedidos_cancelados,
                      SUM(pedidos_no_entregados) AS total_pedidos_no_entregados
                      FROM partida
                     """;

        PreparedStatement ps = con.prepareStatement(sql);

        rs = ps.executeQuery();

    }catch(Exception e){
        e.printStackTrace();
    }

    return rs;
    }
    
    public ResultSet obtenerRankinGlobal() throws SQLException{
        Connection con = DBConexion.getConnection();
        
        String sql = """
                     SELECT
                     u.nombre,
                     s.nombre AS sucursal,
                     j.puntos,
                     j.nivel,
                     j.partidas_jugadas
                     FROM jugador j
                     JOIN usuario u ON j.id_jugador = u.id_usuario
                     JOIN sucursal s ON u.id_sucursal = s.id_sucursal
                     ORDER BY j.puntos DESC
                     """;
        
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
