package AutomataPila;

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
        String s = "n = 3 + n/2 - k + m * 2";
        Condicion c = new Condicion(s);
        System.out.println(c);
    }
}
