/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PartesLogicas;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author fernan
 */
public class ProduccionAleatoria {

    Scanner entrada = new Scanner(System.in);
    int repetirPedido = 10;

    public void ProducirPedidosAleatorios() {
        String[] productos = {
            "Pizza de Peperoni",
            "Gaseosa",
            "Papas Fritas",
            "Pizza con Extra queso"};
        System.out.println("Realizando pedido");
        System.out.println(" ");
        Random pedido = new Random();

        for (int numPedido = 1; numPedido <= repetirPedido; numPedido++) {
            System.out.println("Pedido # " + numPedido);
            int cantidad = pedido.nextInt(productos.length) + 1;
            System.out.println("Cantidad de productos del pedido " + cantidad);
            boolean[] usados = new boolean[productos.length];
            String[] productosSeleccionados = new String[cantidad];
            for (int i = 0; i < cantidad; i++) {
                int indice;
                do {
                    indice = pedido.nextInt(productos.length);
                } while (usados[indice]);
                usados[indice] = true;
                productosSeleccionados[i] = productos[indice];
            }
            HilosDePedido nuevoPedido = new HilosDePedido(numPedido, productosSeleccionados);
            nuevoPedido.start();
        }

    }
}
