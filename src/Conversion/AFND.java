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


/**
 * Representa un Autómata Finito No Determinista (AFND), que hereda de la clase AF.
 * Permite insertar transiciones no deterministas y proporciona un método para
 * convertir el AFND en su equivalente AFD.
 * 
 * Hereda atributos y métodos de la clase abstracta AF.
 * 
 * @author Kevin
 */
public class AFND extends AF {

    /**
 * Inserta una transición en el autómata no determinista.
 * Internamente llama al método de la clase padre indicando que es no determinista.
 *
 * @param estadoInicial Estado desde el cual parte la transición.
 * @param letra Símbolo de entrada de la transición.
 * @param estadoFinal Estado al cual llega la transición.
 * @return true si la transición fue insertada correctamente, false si ya existía.
 */
    public boolean insertTransicion(String estadoInicial, String letra, String estadoFinal) {
        return super.insertTransicion(estadoInicial, letra, estadoFinal, false);
    }
/**
 * Convierte este autómata finito no determinista (AFND) en su equivalente
 * autómata finito determinista (AFD) utilizando el algoritmo de subconjuntos.
 * 
 * El método realiza los siguientes pasos:
 * <ol>
 *   <li>Crea un nuevo AFD vacío y copia el alfabeto del AFND.</li>
 *   <li>Inicia la construcción del AFD a partir del estado inicial del AFND.</li>
 *   <li>Utiliza una cola para recorrer los conjuntos de estados generados (subconjuntos).</li>
 *   <li>Para cada conjunto de estados:
 *       <ul>
 *           <li>Determina si el nuevo estado es terminal si alguno de los estados originales lo es.</li>
 *           <li>Para cada símbolo del alfabeto, combina todas las transiciones posibles desde cada estado del conjunto.</li>
 *           <li>Elimina duplicados y ordena los estados resultantes para generar un nombre único de estado.</li>
 *           <li>Inserta la transición resultante en el AFD.</li>
 *           <li>Si el nuevo conjunto de estados aún no ha sido procesado, se añade a la cola.</li>
 *       </ul>
 *   </li>
 *   <li>El proceso se repite hasta que todos los subconjuntos posibles han sido procesados.</li>
 * </ol>
 * 
 * @return Un objeto {@link AFD} equivalente al AFND actual, con todos los estados
 *         y transiciones deterministas generados a partir del algoritmo de subconjuntos.
 */
    public AFD conversionAFD() {
        AFD afd = new AFD();
        Map<String, Transicion> transicionesAFND = super.transiciones;
        ArrayList<String> alfabeto = super.alfabeto;
        afd.alfabeto = alfabeto;

        String inicial = super.estadoInicial;
        if (inicial == null) {
            return afd;
        }

        Queue<Transicion> cola = new LinkedList<>();
        cola.add(new Transicion(inicial));
        while (!cola.isEmpty()) {
            Transicion actual = cola.peek();
            afd.transiciones.put(actual.estado, actual);
            String estado = actual.estado;
            for (String letra : alfabeto) {
                String s = "";
                for (int i = 0; i < estado.length(); i++) {
                    String a = "" + estado.charAt(i);
                    Transicion t = transicionesAFND.get(a);
                    if(!actual.terminal){
                        if (t.terminal) {
                            actual.terminal = true;
                        }
                    }
                    ArrayList<String> arr = t.transiciones.get(letra);
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
                actual.insertTransicion(letra, destino, true);  // true porque es determinista
                if (!afd.transiciones.containsKey(destino)) {
                    cola.add(new Transicion(destino));
                }
            }
            cola.poll();
        }

        return afd;
    }
}
