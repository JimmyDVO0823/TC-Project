/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AutomataPila;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Kevin
 */
public class Estado {
    String nombre;
    TreeMap<Conexion, Estado> conexiones = new TreeMap<>();
    
    public Estado(String nombre){
        
    }
    
    public void agregarEstado(Estado estado, String letra, String pop, ArrayList<String> push){
        conexiones.put(new Conexion(letra, pop, push, this, estado), estado);
    }
}
