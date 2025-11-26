package Conversion;


import Conversion.Transicion;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase abstracta que representa un Autómata Finito (determinista o no determinista).
 * Contiene el alfabeto, el estado inicial y las transiciones del autómata.
 * Proporciona métodos para agregar transiciones, establecer estados iniciales y terminales,
 * y obtener información del autómata.
 *
 * @author Kevin
 */
public abstract class AF {
    /**
    * Lista de símbolos que conforman el alfabeto del autómata.
    */
    public ArrayList<String> alfabeto = new ArrayList<>();
    /**
    * Estado inicial del autómata.
    */
    public String estadoInicial;
    /**
    * Mapa de transiciones del autómata.
    * La clave es el nombre del estado y el valor es el objeto Transicion asociado.
    */
    public Map<String, Transicion> transiciones = new LinkedHashMap<>();

    /**
 * Inserta una transición en el autómata.
 * Si el estado de inicio no existe, se crea una nueva Transicion.
 *
 * @param inicio Estado desde el cual parte la transición.
 * @param letra Símbolo de entrada de la transición.
 * @param destino Estado al cual llega la transición.
 * @param determinista Indica si la transición pertenece a un autómata determinista.
 * @return true si la transición fue insertada correctamente, false si ya existía.
 */
    public boolean insertTransicion(String inicio, String letra, String destino, boolean determinista) {
        // Crea y pone una Transicion sólo si no existía
        transiciones.putIfAbsent(inicio, new Transicion(inicio));
        // Ahora obtenemos la Transicion (ya existe)
        Transicion t = transiciones.get(inicio);
        // Delegamos la inserción al objeto Transicion
        return t.insertTransicion(letra, destino, determinista);
    }
    /**
 * Establece el estado inicial del autómata.
 *
 * @param estado Nombre del estado que se desea establecer como inicial.
 * @return true si el estado existe y se estableció correctamente, false si el estado no existe.
 */
    public boolean setEstadoInicial(String estado) {
    if (transiciones.containsKey(estado)) {  // Verifica si el estado existe
        estadoInicial = estado;         // Establece el nuevo estado inicial
        return true;               // Éxito
    } else {
        return false;              // El estado no existe
    }
    }
    
    /**
 * Establece si un estado es terminal o no.
 *
 * @param estado Nombre del estado a modificar.
 * @param terminal true para marcarlo como terminal, false para no terminal.
 * @return true si el estado existe y se modificó correctamente, false si el estado no existe.
 */
    public boolean setEstadoTerminal(String estado, boolean terminal) {
    // Verifica si el estado existe dentro del mapa de transiciones
    if (transiciones.containsKey(estado)) {
        // Obtiene la transición y cambia su estado terminal
        transiciones.get(estado).terminal = terminal;
        return true; // Estado encontrado y modificado
    } else {
        return false; // Estado no existe
    }
    }
/**
 * Obtiene una lista con los nombres de todos los estados terminales del autómata.
 *
 * @return ArrayList con los nombres de los estados terminales.
 */
    public ArrayList<String> getEstadosTerminales(){
        ArrayList<String> e = new ArrayList<>();
        for(String key: transiciones.keySet()){
            if(transiciones.get(key).terminal){
                e.add(key);
            }
        }
        return e;
    }
    
    /**
 * Agrega un símbolo al alfabeto del autómata.
 *
 * @param s Símbolo que se desea agregar.
 */
    public void addLetra(String s){
        this.alfabeto.add(s);
    }
    
      /**
 * Genera una representación en cadena del autómata.
 * Incluye el estado inicial y todas las transiciones.
 *
 * @return Cadena que representa el autómata.
 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Imprimiendo");
        System.out.println("ESTADO INICIAL "+estadoInicial);
        for (Map.Entry<String, Transicion> e : transiciones.entrySet()) {
            sb.append(e.getValue().toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
