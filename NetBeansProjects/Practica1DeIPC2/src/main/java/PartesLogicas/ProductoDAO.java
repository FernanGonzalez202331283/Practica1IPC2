/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

import conexion.DBConexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ProductoDAO {
    
    public void crearProductos(String nombre, String descripcion, int idSucursal){
        try(Connection con = DBConexion.getConnection()) {
            String sqlProducto = "INSERT INTO producto (nombre, descripcion) VALUES (?,?)";
            PreparedStatement ps1 = con.prepareStatement(sqlProducto, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, nombre);
            ps1.setString(2, descripcion);
            ps1.executeUpdate();
            
            ResultSet rs = ps1.getGeneratedKeys();
            
            if(rs.next()){
                int idProducto = rs.getInt(1);
                
                String sqlSucursalProducto = "INSERT INTO sucursal_producto (id_sucursal, id_producto, activo) VALUES (?,?,TRUE)";
                PreparedStatement ps2 = con.prepareStatement(sqlSucursalProducto);
                ps2.setInt(1, idSucursal);
                ps2.setInt(2, idProducto);
                ps2.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Producto> obtenerProductosPorSucursal(int idSucursal){
        List<Producto> lista = new ArrayList<>();
        try (Connection con = DBConexion.getConnection()){
            String sql = """
                        SELECT p.id_producto, p.nombre, p.descripcion, sp.activo
                         FROM producto p
                         JOIN sucursal_producto sp
                         ON p.id_producto = sp.id_producto
                         WHERE sp.id_sucursal = ?
                         """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSucursal);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Producto p = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBoolean("activo")
                );
                lista.add(p);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public void modificarProducto(int idProducto, String nuevoNombre, String nuevaDescripcion){
        try(Connection con = DBConexion.getConnection()) {
            String sql = "UPDATE producto SET nombre = ?, descripcion = ? WHERE id_producto = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoNombre);
            ps.setString(2, nuevaDescripcion);
            ps.setInt(3, idProducto);
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cambiarEstadoDeProducto(int idSucursal, int idProducto, boolean nuevoEstado){
        try (Connection con= DBConexion.getConnection()){
            String sql = "UPDATE sucursal_producto SET activo = ? WHERE id_sucursal = ? AND id_producto = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, nuevoEstado);
            ps.setInt(2, idSucursal);
            ps.setInt(3, idProducto);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public  List<Producto>  obtenerProductosActivosPorSucursal(int idSucursal){
        List<Producto> lista = new ArrayList<>();
        try (Connection con = DBConexion.getConnection()){
            String sql = """
                          SELECT p.id_producto, p.nombre, p.descripcion
                          FROM producto p
                          JOIN sucursal_producto sp
                          ON p.id_producto = sp.id_producto
                          WHERE sp.id_sucursal = ?
                          AND sp.activo = TRUE
                         """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSucursal);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Producto p = new Producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    true
                );
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            return lista;
        }
}
