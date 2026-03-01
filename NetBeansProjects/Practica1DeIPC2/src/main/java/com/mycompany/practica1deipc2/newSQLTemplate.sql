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
    FOREIGN KEY (id_jugador) REFERENCES usuario(id_usuario)
);

CREATE TABLE estadistica (
    id_estadistica INT AUTO_INCREMENT PRIMARY KEY,
    total_partidas INT,
    total_puntos INT
);