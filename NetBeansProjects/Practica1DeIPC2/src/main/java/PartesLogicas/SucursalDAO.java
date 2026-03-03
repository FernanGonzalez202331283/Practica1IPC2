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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author fernan
 */
public class SucursalDAO {
     public List<Sucursal> obtenerSucursales(){
        
        List<Sucursal> lista = new ArrayList<>();
        String sql = "SELECT * FROM sucursal";

        try (Connection con = DBConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Sucursal s = new Sucursal(
                        rs.getInt("id_sucursal"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getBoolean("estado")
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener sucursales: " + e.getMessage());
        }

        return lista;
    } 
     
    public boolean crearSucursal(String nombre, String direccion){

        String sql = "INSERT INTO sucursal(nombre, direccion, estado) VALUES (?, ?, TRUE)";

        try (Connection con = DBConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, direccion);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear sucursal: " + e.getMessage());
            return false;
        }
    }
    
     public boolean modificarSucursal(int id, String nombre, String direccion, boolean estado){

        String sql = "UPDATE sucursal SET nombre=?, direccion=?, estado=? WHERE id_sucursal=?";

        try (Connection con = DBConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setBoolean(3, estado);
            ps.setInt(4, id);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar sucursal: " + e.getMessage());
            return false;
        }
    }
      public boolean desactivarSucursal(int id){

        String sql = "UPDATE sucursal SET estado = FALSE WHERE id_sucursal = ?";

        try (Connection con = DBConexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al desactivar sucursal: " + e.getMessage());
            return false;
        }
    }
}
