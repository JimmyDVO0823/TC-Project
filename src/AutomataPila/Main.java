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
        AFP afp = new AFP();

// Estas se procesarán en el orden correcto automáticamente
afp.agregarCondicion("n", "n > 0");        // Se procesa PRIMERO
afp.agregarCondicion("m", "m = n + 3");    // Se procesa DESPUÉS
afp.agregarEstructura("a", "n");
afp.agregarEstructura("b", "m");

afp.construir();
afp.imprimirAutomata();
    }
}
