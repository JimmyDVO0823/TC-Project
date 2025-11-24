package AutomataPila;

import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Kevin
 */
public class Condicion {

    String letra;
    String ecuacion;
    ArrayList<Termino> terminos;

    public Condicion(String input) {
        terminos = new ArrayList<Termino>();
        recibirInput(input);
    }

    // 3 + n/2 - k + m * 2
    public boolean recibirInput(String input) {
        // Asegurarse de que la expresión empieza con un signo
        input = input.trim();

        // Lista de operadores posibles
        String[] operadores = {">=", "<=", ">", "<", "="};
        ecuacion = null;
        int indexOp = -1;

        // Buscar el operador en el input
        for (String op : operadores) {
            indexOp = input.indexOf(op);
            if (indexOp != -1) {
                ecuacion = op;
                break;
            }
        }

        if (ecuacion == null) {
            System.out.println("No se encontró operador válido.");
            return false;
        }

        // Parte izquierda: caracter anterior al operador
        letra = input.substring(0, indexOp).trim();
        if (letra.length() != 1) {
            System.out.println("La parte izquierda debe ser un solo carácter.");
            return false;
        }

        // Parte derecha: expresión algebraica
        String derecha = input.substring(indexOp + ecuacion.length()).trim();

        // --- Ahora procesamos la parte derecha como antes ---
        if (!derecha.startsWith("+") && !derecha.startsWith("-")) {
            derecha = "+" + derecha;
        }

        // Separar por términos, manteniendo el signo al inicio de cada término
        String regex = "(?=[+-])"; // lookahead para mantener el signo
        String[] partes = derecha.split(regex);

        for (String parte : partes) {
            if (parte.isEmpty()) {
                continue;
            }

            boolean signo = parte.startsWith("+");
            String term = parte.substring(1); // quitar el signo

            String valor = term;
            String valor2 = "";
            boolean producto = false;
            boolean division = false;

            if (term.contains("*")) {
                producto = true;
                String[] vals = term.split("\\*");
                valor = vals[0];
                valor2 = vals[1];
            } else if (term.contains("/")) {
                division = true;
                String[] vals = term.split("/");
                valor = vals[0];
                valor2 = vals[1];
            }

            Termino t = new Termino(valor, valor2, signo, producto, division);
            terminos.add(t);
        }

        // Aquí ya tienes la lista de términos separados y con sus propiedades
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(letra).append(" ").append(ecuacion).append(" ");

        for (int i = 0; i < terminos.size(); i++) {
            sb.append(terminos.get(i).toString());
            if (i < terminos.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
