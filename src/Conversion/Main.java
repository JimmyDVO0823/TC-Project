package Conversion;


import Conversion.AFND;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Kevin
 */
public class Main {
    public static void main(String[] args) {
        AFND afnd = new AFND();
        afnd.addLetra("a");
        afnd.addLetra("b");
        System.out.println(afnd.insertTransicion("X", "a", "Y")); 
        System.out.println(afnd.insertTransicion("X", "b", "Z")); 
        System.out.println(afnd.insertTransicion("Y", "a", "W")); 
        System.out.println(afnd.insertTransicion("Y", "b", "P")); 
        System.out.println(afnd.insertTransicion("Z", "a", "Y,P")); 
        System.out.println(afnd.insertTransicion("Z", "b", "Y")); 
        System.out.println(afnd.insertTransicion("W", "a", "Y")); 
        System.out.println(afnd.insertTransicion("W", "b", "W")); 
        System.out.println(afnd.insertTransicion("P", "a", "W")); 
        System.out.println(afnd.insertTransicion("P", "b", "P")); 
        afnd.setEstadoInicial("X");
        afnd.setEstadoTerminal("Y", true);
        
        AFD afd = afnd.conversionAFD();
        
        System.out.println();
        System.out.println(afnd);
        System.out.println("--------------------------------------");
        System.out.println(afd);
    }
}
