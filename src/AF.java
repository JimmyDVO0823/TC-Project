
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

    private ArrayList<String> alfabeto = new ArrayList<>();
    private String estadoInicial;
    private Map<String, Transicion> transiciones = new TreeMap<>();

    public boolean insertTransicion(String estadoInicial, String letra, String estadoFinal, boolean determinista) {
        // Crea y pone una Transicion sólo si no existía
        transiciones.putIfAbsent(estadoInicial, new Transicion(estadoInicial));
        // Ahora obtenemos la Transicion (ya existe)
        Transicion t = transiciones.get(estadoInicial);
        // Delegamos la inserción al objeto Transicion
        return t.addTransicion(letra, estadoFinal, determinista);
    }
    
    public boolean setEstadoInicial(String s) {
    if (transiciones.containsKey(s)) {  // Verifica si el estado existe
        estadoInicial = s;         // Establece el nuevo estado inicial
        return true;               // Éxito
    } else {
        return false;              // El estado no existe
    }
    }
    
    public boolean setEstadoTerminal(String estado, boolean terminal) {
    // Verifica si el estado existe dentro del mapa de transiciones
    if (transiciones.containsKey(estado)) {
        // Obtiene la transición y cambia su estado terminal
        transiciones.get(estado).setTerminal(terminal);
        return true; // Estado encontrado y modificado
    } else {
        return false; // Estado no existe
    }
    }

    
    public Map<String, Transicion> getTransiciones() {
        return transiciones;
    }
    
    public String getEstadoInicial(){
        return estadoInicial;
    }
    
    public ArrayList<String> getAlfabeto(){
        return this.alfabeto;
    }
    
    public void addLetra(String s){
        this.alfabeto.add(s);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        System.out.println("ola");
        for (Map.Entry<String, Transicion> e : transiciones.entrySet()) {
            sb.append(e.getValue().toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
