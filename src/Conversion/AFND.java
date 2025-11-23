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
        afd.setAlfabeto(alfabeto);

        String inicial = super.getEstadoInicial();
        if (inicial == null) {
            return afd;
        }

        Queue<Transicion> cola = new LinkedList<>();
        cola.add(new Transicion(inicial));
        while (!cola.isEmpty()) {
            Transicion actual = cola.peek();
            afd.getTransiciones().put(actual.getEstado(), actual);
            String estado = actual.getEstado();
            for (String letra : alfabeto) {
                String s = "";
                for (int i = 0; i < estado.length(); i++) {
                    String a = "" + estado.charAt(i);
                    Transicion t = transicionesAFND.get(a);
                    if(!actual.isTerminal()){
                        if (t.isTerminal()) {
                            actual.setTerminal(true);
                        }
                    }
                    ArrayList<String> arr = t.getTransiciones().get(letra);
                    for (String caracter : arr) {
                        s = s + caracter;
                    }
                }
                char[] chars = s.toCharArray();
                Set<Character> set = new HashSet<>(); // Elimina duplicados
                for (char c : chars) {
                    if(!Character.isLetterOrDigit(c)) continue;
                    if (transicionesAFND.containsKey(String.valueOf(c))) {
                        set.add(c);
                    } else {
                        System.out.println("ERROR, CLAVE NO ENCONTRADA");
                        return new AFD();
                    }
                }
                ArrayList<Character> lista = new ArrayList<>(set);   // Para ordenarla
                Collections.sort(lista, Collections.reverseOrder());
                String destino = "";
                for (char c : lista) {
                    destino = destino + c;
                }
                actual.addTransicion(letra, destino, true);  // true porque es determinista
                if (!afd.getTransiciones().containsKey(destino)) {
                    cola.add(new Transicion(destino));
                }
            }
            cola.poll();
        }

        return afd;
    }
}
