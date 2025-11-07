
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
public abstract class AF {

    private Map<String, Transicion> transiciones = new TreeMap<>();

    public boolean insertTransicion(String estadoInicial, String letra, String estadoFinal, boolean determinista) {
        // Crea y pone una Transicion sólo si no existía
        transiciones.putIfAbsent(estadoInicial, new Transicion(estadoInicial));
        // Ahora obtenemos la Transicion (ya existe)
        Transicion t = transiciones.get(estadoInicial);
        // Delegamos la inserción al objeto Transicion
        return t.addTransicion(letra, estadoFinal, determinista);
    }
    
    public Map<String, Transicion> getTransiciones() {
        return transiciones;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Transicion> e : transiciones.entrySet()) {
            sb.append(e.getValue().toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
