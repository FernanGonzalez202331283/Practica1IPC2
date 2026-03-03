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
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author fernan
 */
public class UsuarioDAO {
    public void registrarUsuario(String nombre, String username, String password, String rol, Integer idSucursal){
        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psJugador = null;
        ResultSet rs = null;
        
        try {
            con = DBConexion.getConnection();
            con.setAutoCommit(false);
            // insertar en usuario
            String sqlUsuario = "INSERT INTO usuario(nombre, username, password, rol, id_sucursal) VALUES (?,?,?,?,?)";
            psUsuario = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, nombre);
            psUsuario.setString(2, username);
            psUsuario.setString(3, password);
            psUsuario.setString(4, rol);
            
            if(idSucursal !=null){
                psUsuario.setInt(5, idSucursal);
            }else{
                psUsuario.setNull(5, Types.INTEGER);
            }
            
            psUsuario.executeUpdate();
            rs = psUsuario.getGeneratedKeys();
            int idGenerado = 0;
            
            if(rs.next()){
                idGenerado = rs.getInt(1);
            }
            
            if(rol.equals("JUGADOR")){
                String sqlJugador = "INSERT INTO jugador(id_jugador) VALUES(?)";
                psJugador = con.prepareStatement(sqlJugador);
                psJugador.setInt(1, idGenerado);
                psJugador.executeUpdate();
            }
            con.commit();// se confirma
            JOptionPane.showMessageDialog(null, "usuario registrado correctamente");
        } catch (SQLException e) {
            try {
                if(con !=null){
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error en roll: "+ex.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error al registrar usuario "+ e.getMessage());
        } finally {
            try {
                if(rs!=null) rs.close();
                if(psUsuario != null)psUsuario.close();
                if(psJugador != null)psJugador.close();
                if(con !=null)con.close();
            } catch (SQLException e) {
                System.out.println("error cerrando recursos: "+e.getMessage());
            }
        }
    }
    public List <Usuario> obtenerUsuarioSinSucursal(){
        List<Usuario> lista = new ArrayList<>();
        
        try {
            Connection con = DBConexion.getConnection();
            String sql = "SELECT * FROM usuario WHERE id_Sucursal is NULL AND rol != 'SUPERADMIN'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Usuario u = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre"),
                    rs.getString("username"),
                    rs.getString("rol")
                );

                lista.add(u);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista; 
    }
}
