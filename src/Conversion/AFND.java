package Conversion;


import Conversion.Transicion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Kevin
 */
public class AFND extends AF {

    public boolean insertTransicion(String estadoInicial, String letra, String estadoFinal) {
        return super.insertTransicion(estadoInicial, letra, estadoFinal, false);
    }

    public AFD conversionAFD() {
        AFD afd = new AFD();
        Map<String, Transicion> transicionesAFND = super.getTransiciones();
        ArrayList<String> alfabeto = super.getAlfabeto();

        String inicial = super.getEstadoInicial();
        if (inicial == null) {
            return afd;
        }
        Queue<Transicion> cola = new LinkedList<>();
        cola.add(new Transicion(inicial));

        while (!cola.isEmpty()) {
            Transicion actual = cola.peek();
            String estado = actual.getEstado();
            System.out.println("Estado actual:" +estado);
            for (String letra : alfabeto) {
                String s = "";
                for (int i = 0; i < estado.length(); i++) {
                    String a = "" + estado.charAt(i);
                    Transicion t = transicionesAFND.get(a);
                    ArrayList<String> arr = t.getTransiciones().get(letra);
                    for (String caracter : arr) {
                        s = s + caracter;
                    }
                }
                char[] chars = s.toCharArray();
                Set<Character> set = new HashSet<>(); // Elimina duplicados
                for (char c : chars) set.add(c); 
                ArrayList<Character> lista = new ArrayList<>(set);   // Para ordenarla
                Collections.sort(lista, Collections.reverseOrder());
                String destino = "";
                for(char c:lista) destino = destino + c;
                System.out.println("ola");

                afd.insertTransicion(estado, letra, destino);
                if(!afd.getTransiciones().containsKey(destino)){
                    cola.add(new Transicion(destino));
                }
                actual.addTransicion(letra, destino, true);  // true porque es determinista
            }
            cola.poll();
        }

        return afd;
    }
}
