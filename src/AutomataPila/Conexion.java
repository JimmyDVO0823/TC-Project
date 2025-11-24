/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AutomataPila;

import java.util.ArrayList;

/**
 *
 * @author Kevin
 */
public class Conexion {
    String letra;
    String pop;
    ArrayList<String> push;
    Estado inicio;
    Estado fin;

    public Conexion(String letra, String pop, ArrayList<String> push, Estado inicio, Estado fin) {
        this.letra = letra;
        this.pop = pop;
        this.push = push;
        this.inicio = inicio;
        this.fin = fin;
    }
 
}
