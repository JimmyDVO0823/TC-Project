package Conversion;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Kevin
 */
public class Transicion {

    private String estado;
    private boolean terminal;
    private Map<String, ArrayList<String>> transiciones = new TreeMap<>();

    public Transicion(String estadoInicial) {
        this.estado = estadoInicial;
        this.terminal = false;
    }

    public boolean addTransicion(String letra, String estadoFinal, boolean determinista) {
        // Asegura que exista la lista para esa letra
        transiciones.putIfAbsent(letra, new ArrayList<>());

        ArrayList<String> destinos = transiciones.get(letra);
        // Evitamos duplicados

        if (destinos.contains(estadoFinal)) {
            return false;
        }

        if (determinista && destinos.isEmpty()) {
            destinos.add(estadoFinal);
            return true;
        } else if (!determinista) {
            destinos.add(estadoFinal);
            return true;
        }

        return false;

    }

    public boolean isTerminal() {
        return terminal;
    }
    
    public void setTerminal(boolean b) {
        this.terminal = b;
    }

    public String getEstado() {
        return estado;
    }

    public Map<String, ArrayList<String>> getTransiciones() {
        return transiciones;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estado ");
        if (terminal) {
            sb.append("Terminal ");
        }
        sb.append(estado).append(": ");
        if (transiciones.isEmpty()) {
            sb.append("sin transiciones");
            return sb.toString();
        }
        for (Map.Entry<String, ArrayList<String>> e : transiciones.entrySet()) {
            sb.append(e.getKey()).append(" -> ").append(e.getValue()).append("; ");
        }
        return sb.toString();
    }

}
