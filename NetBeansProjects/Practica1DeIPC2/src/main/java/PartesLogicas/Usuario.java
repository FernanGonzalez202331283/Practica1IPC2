/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

/**
 *
 * @author fernan
 */
public class Usuario {
    private int id;
    private String nombre;
    private String username;
    private String rol;

    public Usuario(int id, String nombre, String username, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return nombre;
    }
   
}
