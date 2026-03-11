/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  fernan
 * Created: 24 feb 2026
 */
CREATE DATABASE Pizzeria;

USE Pizzeria;

CREATE TABLE sucursal(
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150),
    estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol ENUM('JUGADOR', 'ADMIN', 'SUPERADMIN') NOT NULL,
    id_sucursal INT,
    FOREIGN KEY(id_sucursal) REFERENCES sucursal(id_sucursal)
);

CREATE TABLE jugador (
    id_jugador INT PRIMARY KEY,
    puntos INT DEFAULT 0,
    nivel INT DEFAULT 1,
    partidas_jugadas INT DEFAULT 0,
    mejor_puntaje INT DEFAULT 0,
    FOREIGN KEY (id_jugador) REFERENCES usuario(id_usuario)
);

CREATE TABLE estadistica (
    id_estadistica INT AUTO_INCREMENT PRIMARY KEY,
    total_partidas INT,
    total_puntos INT
);

CREATE TABLE producto(
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(100)
);

CREATE TABLE sucursal_producto (
    id_sucursal INT,
    id_producto INT,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id_sucursal, id_producto),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal),
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TABLE partida (
    id_partida INT AUTO_INCREMENT PRIMARY KEY,
    id_jugador INT,
    id_sucursal INT,
    puntaje_total INT,
    nivel_alcanzado INT,
    pedidos_completados INT,
    pedidos_cancelados INT DEFAULT 0,
    pedidos_no_entregados INT DEFAULT 0,
    tiempo_total_preparacion INT DEFAULT 0,
    FOREIGN KEY(id_jugador) REFERENCES jugador(id_jugador),
    FOREIGN KEY(id_sucursal) REFERENCES sucursal(id_sucursal)
);

CREATE TABLE parametros_juego(
    id_parametro INT AUTO_INCREMENT PRIMARY KEY,
    id_sucursal INT,
    tiempo_nivel1 INT,
    tiempo_nivel2 INT,
    tiempo_nivel3 INT,
    tiempo_generacion_pedido INT,
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);


ALTER TABLE partida
ADD pedidos_cancelados INT DEFAULT 0,
ADD pedidos_no_entregados INT DEFAULT 0,
ADD tiempo_total_preparacion INT DEFAULT 0;