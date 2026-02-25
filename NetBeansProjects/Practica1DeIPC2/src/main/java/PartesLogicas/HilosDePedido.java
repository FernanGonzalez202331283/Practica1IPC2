/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

import java.util.Random;

/**
 *
 * @author fernan
 */
public class HilosDePedido extends Thread{
    private int numeroPedido;
    private String[] productos;

    public HilosDePedido(int numeroPedido, String[] productos) {
        this.numeroPedido = numeroPedido;
        this.productos = productos;
    }
    
    @Override
    public void run(){
         
        Random random = new Random();
        int tiempoDeEntrega = 60;
        System.out.println("Peiddo #"+numeroPedido+" en preparacion ....");
        System.out.println("tiempo restante #"+tiempoDeEntrega+" segundos ....");
        
        try {
            Thread.sleep(tiempoDeEntrega * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Pedido #"+ numeroPedido+ "ENTREGADO");
    }
    
    
}
