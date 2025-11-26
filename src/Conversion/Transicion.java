package Conversion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Representa las transiciones de un estado dentro de un autómata.
 * Cada objeto Transicion corresponde a un estado y contiene sus posibles
 * transiciones hacia otros estados según los símbolos del alfabeto.
 * También almacena si el estado es terminal o no.
 * 
 * @author Kevin
 */
public class Transicion {
/**
 * Nombre del estado asociado a esta transición.
 */
    public String estado;
/**
 * Indica si el estado es terminal (true) o no (false).
 */
    public boolean terminal;
    /**
 * Mapa de transiciones: la clave es un símbolo del alfabeto y el valor
 * es una lista de estados destino alcanzables desde este estado con ese símbolo.
 */
    public Map<String, ArrayList<String>> transiciones = new LinkedHashMap<>();

    /**
 * Crea un objeto Transicion para un estado dado.
 * Inicialmente, el estado no es terminal y no tiene transiciones.
 * 
 * @param estado Nombre del estado a representar.
 */
    public Transicion(String estado) {
        this.estado = estado;
        this.terminal = false;
    }

    /**
 * Inserta una transición desde este estado hacia otro estado mediante un símbolo.
 * Evita duplicados y permite diferenciar entre autómatas deterministas y no deterministas.
 *
 * @param letra Símbolo de entrada de la transición.
 * @param destino Estado al que se desea transicionar.
 * @param determinista true si la transición pertenece a un autómata determinista,
 *                     false si es no determinista.
 * @return true si la transición fue insertada correctamente, false si no se pudo agregar.
 */
    public boolean insertTransicion(String letra, String destino, boolean determinista) {
        // Asegura que exista la lista para esa letra
        transiciones.putIfAbsent(letra, new ArrayList<>());

        ArrayList<String> destinos = transiciones.get(letra);
        // Evitamos duplicados

        if (destinos.contains(destino)) {
            return false;
        }

        if (determinista && destinos.isEmpty()) {
            destinos.add(destino);
            return true;
        } else if (!determinista) {
            destinos.add(destino);
            return true;
        }

        return false;

    }
/**
 * Genera una representación en cadena del estado y sus transiciones.
 * Indica si el estado es terminal y lista todas sus transiciones en formato:
 * "símbolo -> [destinos]; ".
 *
 * @return Cadena que representa el estado y sus transiciones.
 */
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
