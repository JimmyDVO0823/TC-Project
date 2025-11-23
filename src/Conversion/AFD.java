package Conversion;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Kevin
 */
public class AFD extends AF{
    public boolean insertTransicion(String estadoInicial, String letra, String estadoFinal) {
        System.out.println("Insertar AFD "+estadoInicial+ " letra: "+letra+" destino: "+estadoFinal);
        return super.insertTransicion(estadoInicial, letra, estadoFinal, true);
    }
    
}
