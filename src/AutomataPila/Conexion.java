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
public class Conexion implements Comparable<Conexion> {
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
    
    @Override
    public int compareTo(Conexion otra) {
        // Comparar primero por letra
        int cmpLetra = this.letra.compareTo(otra.letra);
        if (cmpLetra != 0) return cmpLetra;
        
        // Luego por pop
        int cmpPop = this.pop.compareTo(otra.pop);
        if (cmpPop != 0) return cmpPop;
        
        // Luego por push (comparar tamaño primero, luego contenido)
        int cmpPushSize = Integer.compare(this.push.size(), otra.push.size());
        if (cmpPushSize != 0) return cmpPushSize;
        
        // Comparar contenido de push
        for (int i = 0; i < this.push.size(); i++) {
            int cmpPushItem = this.push.get(i).compareTo(otra.push.get(i));
            if (cmpPushItem != 0) return cmpPushItem;
        }
        
        // Finalmente por nombre de estado fin
        return this.fin.nombre.compareTo(otra.fin.nombre);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(letra).append(" ").append(pop).append(" ; ").append(push.toString()).append(" -> ").append(fin.nombre);
        return sb.toString();
    }
 
}