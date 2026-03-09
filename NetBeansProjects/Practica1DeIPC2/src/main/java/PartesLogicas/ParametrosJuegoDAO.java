/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

import conexion.DBConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class ParametrosJuegoDAO {

    public ParametrosJuego obtenerParametros(int idSucursal) {
        ParametrosJuego p = null;
        try {
            Connection con = DBConexion.getConnection();

            String sql = "SELECT * FROM parametros_juego WHERE id_sucursal=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSucursal);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new ParametrosJuego();

                p.setTiempoNivel1(rs.getInt("tiempo_nivel1"));
                p.setTiempoNivel2(rs.getInt("tiempo_nivel2"));
                p.setTiempoNivel3(rs.getInt("tiempo_nivel3"));
                p.setTiempoGeneracionPedidos(rs.getInt("tiempo_generacion_pedido"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // si no existen parametros
        if (p == null) {
            p = new ParametrosJuego();
            p.setTiempoNivel1(60);
            p.setTiempoNivel2(45);
            p.setTiempoNivel3(30);
            p.setTiempoGeneracionPedidos(5);
        }
        return p;
    }
    
    public void guardarOActualizarParametros(int idSucursal, int t1, int t2, int t3){
        try {
            Connection con = DBConexion.getConnection();
            
            String verificar = "SELECT * FROM parametros_juego WHERE id_sucursal = ?";
            PreparedStatement ps = con.prepareStatement(verificar);
            ps.setInt(1, idSucursal);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                String update = "UPDATE parametros_juego SET "
                        + "tiempo_nivel1 = ?,"
                        + "tiempo_nivel2 = ?,"
                        + "tiempo_nivel3 = ?,"
                        + "tiempo_generacion_pedido = ? "
                        + "WHERE id_sucursal = ?";
                
                PreparedStatement ps2 = con.prepareStatement(update);
                ps2.setInt(1, t1);
                ps2.setInt(2, t2);
                ps2.setInt(3, t3);
                ps2.setInt(4, 4);
                ps2.setInt(5, idSucursal);
              
                ps2.executeUpdate();
                
            }else{
                String insert = "INSERT INTO  parametros_juego"
                        + "(id_sucursal,tiempo_nivel1,tiempo_nivel2,tiempo_nivel3,tiempo_generacion_pedido)"
                        + " VALUES (?,?,?,?,?)";
                
                PreparedStatement ps3 = con.prepareStatement(insert);
                
                ps3.setInt(1,idSucursal);
                ps3.setInt(2,t1);
                ps3.setInt(3,t2);
                ps3.setInt(4,t3);
                ps3.setInt(5, 5);

                ps3.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
