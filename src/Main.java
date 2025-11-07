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
        AFD af = new AFD();
        System.out.println(af.insertTransicion("q0", "0", "q1")); // true
        System.out.println(af.insertTransicion("q0", "0", "q2")); // true
        System.out.println(af.insertTransicion("q0", "0", "q1")); // false
        System.out.println(af.insertTransicion("q1", "1", "q1")); // true
        System.out.println();
        System.out.println(af);
    }
}
