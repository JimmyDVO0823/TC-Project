package Conversion;

/**
 * Representa un Autómata Finito Determinista (AFD), que hereda de la clase AF.
 * Esta clase asegura que todas las transiciones insertadas sean deterministas.
 * Hereda los métodos y atributos de la clase abstracta AF.
 * 
 * @author Kevin
 */
public class AFD extends AF{
    /**
 * Inserta una transición en el autómata determinista.
 * Internamente llama al método de la clase padre indicando que es determinista.
 *
 * @param inicio Estado desde el cual parte la transición.
 * @param letra Símbolo de entrada de la transición.
 * @param destino Estado al cual llega la transición.
 * @return true si la transición fue insertada correctamente, false si ya existía.
 */
    public boolean insertTransicion(String inicio, String letra, String destino) {
        return super.insertTransicion(inicio, letra, destino, true);
    }
    
}
