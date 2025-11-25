package AutomataPila;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

public class AFP {

    ArrayList<String[]> estructura;
    TreeMap<String, Condicion> condiciones;
    ArrayList<Estado> estados;
    int numEstado = 0;

    String estadoInicial = "";
    ArrayList<String> estadosTerminales;
    TreeMap<String, Stack<String>> mapPilas;
    TreeMap<String, TreeMap<String, String>> transiciones;

    public AFP() {
        this.estructura = new ArrayList<>();
        this.condiciones = new TreeMap<>();
        this.estados = new ArrayList<>();
        condiciones.put("e", new Condicion("e = 1"));

        this.estadosTerminales = new ArrayList<>();
        this.mapPilas = new TreeMap<>();
        this.transiciones = new TreeMap<>();
    }

    public void construir() {
        if (estructura.isEmpty()) {
            System.out.println("No hay estructura definida");
            return;
        }

        // Ordenar estructura por prioridad de condiciones
        ArrayList<String[]> estructuraOrdenada = ordenarEstructuraPorDependencias();
        
        Estado estadoActual = addEstado();
        estadoInicial = estadoActual.nombre;

        // Procesar cada potencia en la estructura ordenada
        for (int i = 0; i < estructuraOrdenada.size(); i++) {
            String[] potencia = estructuraOrdenada.get(i);
            String base = potencia[0];
            String exp = potencia[1];
            
            Condicion c = condiciones.get(exp);
            if (c == null) {
                System.out.println("No existe condición para: " + exp);
                continue;
            }

            String relacion = c.ecuacion;
            
            System.out.println("Procesando: " + base + "^" + exp + " con condición: " + c.toString());
            
            // Procesar según el tipo de relación
            if (relacion.equals(">")) {
                estadoActual = manejarMayorQue(estadoActual, base, exp, c);
            } else if (relacion.equals(">=")) {
                estadoActual = manejarMayorIgualQue(estadoActual, base, exp, c);
            } else if (relacion.equals("=")) {
                estadoActual = manejarIgualdad(estadoActual, base, exp, c);
            } else if (relacion.equals("<")) {
                estadoActual = manejarMenorQue(estadoActual, base, exp, c);
            } else if (relacion.equals("<=")) {
                estadoActual = manejarMenorIgualQue(estadoActual, base, exp, c);
            }
        }

        // Verificar que todas las pilas estén vacías
        Estado estadoFinal = addEstado();
        ArrayList<String> emptyPush = new ArrayList<>();
        emptyPush.add("Z0");
        estadoActual = estadoActual.agregarEstado(estadoFinal, "ε", "Z0", emptyPush);
        
        // El último estado es terminal
        estadoFinal.terminal = true;
        estadosTerminales.add(estadoFinal.nombre);
    }
    
    // Ordena la estructura priorizando condiciones con constantes sobre variables
    private ArrayList<String[]> ordenarEstructuraPorDependencias() {
        ArrayList<String[]> conConstantes = new ArrayList<>();
        ArrayList<String[]> conVariables = new ArrayList<>();
        
        for (String[] potencia : estructura) {
            String exp = potencia[1];
            Condicion c = condiciones.get(exp);
            
            if (c == null) {
                conVariables.add(potencia);
                continue;
            }
            
            // Verificar si la condición depende solo de constantes
            boolean soloConstantes = true;
            for (Termino t : c.terminos) {
                if (esVariable(t.valor) || esVariable(t.valor2)) {
                    soloConstantes = false;
                    break;
                }
            }
            
            if (soloConstantes) {
                conConstantes.add(potencia);
            } else {
                conVariables.add(potencia);
            }
        }
        
        // Combinar: primero las de constantes, luego las de variables
        ArrayList<String[]> resultado = new ArrayList<>();
        resultado.addAll(conConstantes);
        resultado.addAll(conVariables);
        
        return resultado;
    }

    // variable1 > constante
    private Estado manejarMayorQue(Estado actual, String base, String exp, Condicion c) {
        for (Termino t : c.terminos) {
            if (esNumerico(t.valor)) {
                int valorMin = Integer.parseInt(t.valor);
                String simboloPila = exp.toUpperCase();
                
                // Fase 1: Apilar símbolos leyendo 'base'
                ArrayList<String> pushVar = new ArrayList<>();
                pushVar.add(simboloPila);
                
                Estado estadoApilar = addEstado();
                actual = actual.agregarEstado(estadoApilar, base, "Z0", pushVar);
                
                // Auto-loop para seguir apilando
                estadoApilar = estadoApilar.agregarEstado(estadoApilar, base, simboloPila, pushVar);
                
                // Fase 2: Verificar que haya al menos valorMin + 1 elementos
                // Desapilar exactamente valorMin elementos
                ArrayList<String> popVar = new ArrayList<>();
                popVar.add(simboloPila); // Mantener el símbolo en push para simular que no se pierde
                
                Estado estadoVerificar = estadoApilar;
                for (int i = 0; i < valorMin; i++) {
                    Estado siguiente = addEstado();
                    estadoVerificar = estadoVerificar.agregarEstado(siguiente, "ε", simboloPila, new ArrayList<>());
                    estadoVerificar = siguiente;
                }
                
                // Debe quedar al menos 1 elemento más
                Estado estadoFinal = addEstado();
                estadoVerificar = estadoVerificar.agregarEstado(estadoFinal, "ε", simboloPila, new ArrayList<>());
                
                // Desapilar todos los elementos restantes
                estadoFinal = estadoFinal.agregarEstado(estadoFinal, "ε", simboloPila, new ArrayList<>());
                
                // Verificar pila vacía
                Estado estadoVacio = addEstado();
                ArrayList<String> mantenerZ0 = new ArrayList<>();
                mantenerZ0.add("Z0");
                estadoFinal = estadoFinal.agregarEstado(estadoVacio, "ε", "#" + simboloPila, mantenerZ0);
                
                return estadoVacio;
            }
        }
        return actual;
    }

    private Estado manejarMayorIgualQue(Estado actual, String base, String exp, Condicion c) {
        for (Termino t : c.terminos) {
            if (esNumerico(t.valor)) {
                int valorMin = Integer.parseInt(t.valor);
                String simboloPila = exp.toUpperCase();
                
                ArrayList<String> pushVar = new ArrayList<>();
                pushVar.add(simboloPila);
                
                Estado estadoApilar = addEstado();
                actual = actual.agregarEstado(estadoApilar, base, "Z0", pushVar);
                estadoApilar = estadoApilar.agregarEstado(estadoApilar, base, simboloPila, pushVar);
                
                Estado estadoVerificar = estadoApilar;
                for (int i = 0; i < valorMin; i++) {
                    Estado siguiente = addEstado();
                    estadoVerificar = estadoVerificar.agregarEstado(siguiente, "ε", simboloPila, new ArrayList<>());
                    estadoVerificar = siguiente;
                }
                
                // Desapilar todos los elementos restantes (puede ser 0)
                estadoVerificar = estadoVerificar.agregarEstado(estadoVerificar, "ε", simboloPila, new ArrayList<>());
                
                // Verificar pila vacía
                Estado estadoVacio = addEstado();
                ArrayList<String> mantenerZ0 = new ArrayList<>();
                mantenerZ0.add("Z0");
                estadoVerificar = estadoVerificar.agregarEstado(estadoVacio, "ε", "#" + simboloPila, mantenerZ0);
                
                return estadoVacio;
            }
        }
        return actual;
    }

    // variable2 = expresión
    private Estado manejarIgualdad(Estado actual, String base, String exp, Condicion c) {
        // Analizar la expresión del lado derecho
        String variableBase = null;
        int constanteTotal = 0;
        int multiplicador = 1;
        int divisor = 1;
        boolean esMultiplicacion = false;
        boolean esDivision = false;
        
        for (Termino t : c.terminos) {
            if (esNumerico(t.valor) && !t.producto && !t.division) {
                // Término constante: +3, -5
                int valor = Integer.parseInt(t.valor);
                constanteTotal += t.signo ? valor : -valor;
            } else if (esVariable(t.valor)) {
                variableBase = t.valor;
                if (t.producto && esNumerico(t.valor2)) {
                    multiplicador = Integer.parseInt(t.valor2);
                    esMultiplicacion = true;
                } else if (t.division && esNumerico(t.valor2)) {
                    divisor = Integer.parseInt(t.valor2);
                    esDivision = true;
                }
            } else if (esNumerico(t.valor) && t.producto && esVariable(t.valor2)) {
                // Caso: 2*m
                variableBase = t.valor2;
                multiplicador = Integer.parseInt(t.valor);
                esMultiplicacion = true;
            }
        }
        
        // Caso 1: variable2 = k (constante pura)
        if (variableBase == null && constanteTotal > 0) {
            return manejarIgualdadConstante(actual, base, exp, constanteTotal);
        }
        
        // Caso 2: variable2 = variable1 (igualdad directa)
        if (variableBase != null && !esMultiplicacion && !esDivision && constanteTotal == 0) {
            return manejarIgualdadDirecta(actual, base, exp, variableBase);
        }
        
        // Caso 3: variable2 = variable1 + k o variable2 = variable1 - k
        if (variableBase != null && !esMultiplicacion && !esDivision && constanteTotal != 0) {
            return manejarIgualdadConConstante(actual, base, exp, variableBase, constanteTotal);
        }
        
        // Caso 4: variable2 = variable1 * k
        if (variableBase != null && esMultiplicacion) {
            return manejarIgualdadMultiplicacion(actual, base, exp, variableBase, multiplicador);
        }
        
        // Caso 5: variable2 = variable1 / k
        if (variableBase != null && esDivision) {
            return manejarIgualdadDivision(actual, base, exp, variableBase, divisor);
        }
        
        return actual;
    }
    
    // variable2 = k (constante)
    private Estado manejarIgualdadConstante(Estado actual, String base, String exp, int k) {
        String simboloPila = exp.toUpperCase();
        ArrayList<String> pushVar = new ArrayList<>();
        pushVar.add(simboloPila);
        
        // Apilar exactamente k veces leyendo 'base'
        Estado estadoActual = actual;
        for (int i = 0; i < k; i++) {
            Estado siguiente = addEstado();
            String popSimbolo = (i == 0) ? "Z0" : simboloPila;
            estadoActual = estadoActual.agregarEstado(siguiente, base, popSimbolo, pushVar);
            estadoActual = siguiente;
        }
        
        // Desapilar exactamente k veces leyendo 'base'
        for (int i = 0; i < k; i++) {
            Estado siguiente = addEstado();
            estadoActual = estadoActual.agregarEstado(siguiente, base, simboloPila, new ArrayList<>());
            estadoActual = siguiente;
        }
        
        // Verificar pila vacía
        Estado estadoVacio = addEstado();
        ArrayList<String> mantenerZ0 = new ArrayList<>();
        mantenerZ0.add("Z0");
        estadoActual = estadoActual.agregarEstado(estadoVacio, "ε", "#" + simboloPila, mantenerZ0);
        
        return estadoVacio;
    }
    
    // variable2 = variable1 (igualdad directa)
    private Estado manejarIgualdadDirecta(Estado actual, String base, String exp, String variable1) {
        String simboloPila1 = variable1.toUpperCase();
        String simboloPila2 = exp.toUpperCase();
        String pilaAux = simboloPila1 + "*";
        
        // Paso 1: Copiar pila de variable1 a pila auxiliar variable1*
        // Desapilar de variable1 y apilar en variable1*
        ArrayList<String> pushAux = new ArrayList<>();
        pushAux.add(pilaAux);
        
        Estado estadoCopiar = addEstado();
        actual = actual.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        estadoCopiar = estadoCopiar.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        
        // Verificar que variable1 esté vacía
        Estado estadoVariable1Vacia = addEstado();
        ArrayList<String> mantenerZ0 = new ArrayList<>();
        mantenerZ0.add("Z0");
        estadoCopiar = estadoCopiar.agregarEstado(estadoVariable1Vacia, "ε", "#" + simboloPila1, mantenerZ0);
        
        // Paso 2: Leer 'base' y apilar en variable2, desapilando de variable1*
        ArrayList<String> pushVar2 = new ArrayList<>();
        pushVar2.add(simboloPila2);
        
        Estado estadoApilar = addEstado();
        actual = estadoVariable1Vacia.agregarEstado(estadoApilar, base, pilaAux, pushVar2);
        estadoApilar = estadoApilar.agregarEstado(estadoApilar, base, pilaAux, pushVar2);
        
        // Verificar que variable1* esté vacía
        Estado estadoFinal = addEstado();
        estadoApilar = estadoApilar.agregarEstado(estadoFinal, "ε", "#" + pilaAux, mantenerZ0);
        
        return estadoFinal;
    }
    
    // variable2 = variable1 + k o variable2 = variable1 - k
    private Estado manejarIgualdadConConstante(Estado actual, String base, String exp, String variable1, int constante) {
        String simboloPila1 = variable1.toUpperCase();
        String simboloPila2 = exp.toUpperCase();
        String pilaAux = simboloPila1 + "*";
        
        // Paso 1: Copiar variable1 a variable1*
        ArrayList<String> pushAux = new ArrayList<>();
        pushAux.add(pilaAux);
        
        Estado estadoCopiar = addEstado();
        actual = actual.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        estadoCopiar = estadoCopiar.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        
        // Verificar variable1 vacía
        Estado estadoVariable1Vacia = addEstado();
        ArrayList<String> mantenerZ0 = new ArrayList<>();
        mantenerZ0.add("Z0");
        estadoCopiar = estadoCopiar.agregarEstado(estadoVariable1Vacia, "ε", "#" + simboloPila1, mantenerZ0);
        
        // Paso 2: Apilar en variable2 desde variable1*
        ArrayList<String> pushVar2 = new ArrayList<>();
        pushVar2.add(simboloPila2);
        
        Estado estadoApilar = addEstado();
        actual = estadoVariable1Vacia.agregarEstado(estadoApilar, base, pilaAux, pushVar2);
        estadoApilar = estadoApilar.agregarEstado(estadoApilar, base, pilaAux, pushVar2);
        
        // Verificar variable1* vacía
        Estado estadoVariable1AuxVacia = addEstado();
        estadoApilar = estadoApilar.agregarEstado(estadoVariable1AuxVacia, "ε", "#" + pilaAux, mantenerZ0);
        
        if (constante > 0) {
            // variable2 = variable1 + k: Apilar k elementos adicionales
            Estado estadoActual = estadoVariable1AuxVacia;
            for (int i = 0; i < constante; i++) {
                Estado siguiente = addEstado();
                estadoActual = estadoActual.agregarEstado(siguiente, "ε", simboloPila2, pushVar2);
                estadoActual = siguiente;
            }
            return estadoActual;
            
        } else {
            // variable2 = variable1 - k: Desapilar k elementos
            int valorDesapilar = Math.abs(constante);
            Estado estadoActual = estadoVariable1AuxVacia;
            for (int i = 0; i < valorDesapilar; i++) {
                Estado siguiente = addEstado();
                estadoActual = estadoActual.agregarEstado(siguiente, "ε", simboloPila2, new ArrayList<>());
                estadoActual = siguiente;
            }
            return estadoActual;
        }
    }
    
    // variable2 = variable1 * k
    private Estado manejarIgualdadMultiplicacion(Estado actual, String base, String exp, String variable1, int k) {
        String simboloPila1 = variable1.toUpperCase();
        String simboloPila2 = exp.toUpperCase();
        String pilaAux = simboloPila1 + "*";
        
        // Paso 1: Copiar variable1 a variable1*
        ArrayList<String> pushAux = new ArrayList<>();
        pushAux.add(pilaAux);
        
        Estado estadoCopiar = addEstado();
        actual = actual.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        estadoCopiar = estadoCopiar.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        
        // Verificar variable1 vacía
        Estado estadoVariable1Vacia = addEstado();
        ArrayList<String> mantenerZ0 = new ArrayList<>();
        mantenerZ0.add("Z0");
        estadoCopiar = estadoCopiar.agregarEstado(estadoVariable1Vacia, "ε", "#" + simboloPila1, mantenerZ0);
        
        // Paso 2: Por cada 'base' leída, desapilar 1 de variable1* y apilar k en variable2
        Estado estadoLeerBase = addEstado();
        actual = estadoVariable1Vacia.agregarEstado(estadoLeerBase, base, pilaAux, new ArrayList<>());
        
        // Apilar k veces en variable2
        ArrayList<String> pushVar2 = new ArrayList<>();
        pushVar2.add(simboloPila2);
        
        Estado estadoActual = estadoLeerBase;
        for (int i = 0; i < k; i++) {
            Estado siguiente = addEstado();
            String popSimbolo = (i == 0) ? "Z0" : simboloPila2;
            estadoActual = estadoActual.agregarEstado(siguiente, "ε", popSimbolo, pushVar2);
            estadoActual = siguiente;
        }
        
        // Auto-loop: volver a leer base
        estadoActual = estadoActual.agregarEstado(estadoLeerBase, base, pilaAux, new ArrayList<>());
        
        // Verificar variable1* vacía
        Estado estadoFinal = addEstado();
        estadoLeerBase = estadoLeerBase.agregarEstado(estadoFinal, "ε", "#" + pilaAux, mantenerZ0);
        
        return estadoFinal;
    }
    
    // variable2 = variable1 / k
    private Estado manejarIgualdadDivision(Estado actual, String base, String exp, String variable1, int k) {
        String simboloPila1 = variable1.toUpperCase();
        String simboloPila2 = exp.toUpperCase();
        String pilaAux = simboloPila1 + "*";
        
        // Paso 1: Copiar variable1 a variable1*
        ArrayList<String> pushAux = new ArrayList<>();
        pushAux.add(pilaAux);
        
        Estado estadoCopiar = addEstado();
        actual = actual.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        estadoCopiar = estadoCopiar.agregarEstado(estadoCopiar, "ε", simboloPila1, pushAux);
        
        // Verificar variable1 vacía
        Estado estadoVariable1Vacia = addEstado();
        ArrayList<String> mantenerZ0 = new ArrayList<>();
        mantenerZ0.add("Z0");
        estadoCopiar = estadoCopiar.agregarEstado(estadoVariable1Vacia, "ε", "#" + simboloPila1, mantenerZ0);
        
        // Paso 2: Desapilar k elementos de variable1* y leer 1 'base'
        Estado estadoDesapilar = estadoVariable1Vacia;
        for (int i = 0; i < k; i++) {
            Estado siguiente = addEstado();
            estadoDesapilar = estadoDesapilar.agregarEstado(siguiente, base, pilaAux, new ArrayList<>());
            estadoDesapilar = siguiente;
        }
        
        // Apilar 1 en variable2
        ArrayList<String> pushVar2 = new ArrayList<>();
        pushVar2.add(simboloPila2);
        
        Estado estadoApilar = addEstado();
        String popSimbolo = "Z0";
        estadoDesapilar = estadoDesapilar.agregarEstado(estadoApilar, "ε", popSimbolo, pushVar2);
        
        // Auto-loop: repetir proceso (desapilar k de variable1* por cada 1 apilado en variable2)
        estadoApilar = estadoApilar.agregarEstado(estadoVariable1Vacia, base, pilaAux, new ArrayList<>());
        
        // Verificar variable1* vacía
        Estado estadoFinal = addEstado();
        estadoVariable1Vacia = estadoVariable1Vacia.agregarEstado(estadoFinal, "ε", "#" + pilaAux, mantenerZ0);
        
        return estadoFinal;
    }

    private Estado manejarMenorQue(Estado actual, String base, String exp, Condicion c) {
        for (Termino t : c.terminos) {
            if (esNumerico(t.valor)) {
                int valorMax = Integer.parseInt(t.valor);
                String simboloPila = exp.toUpperCase();
                
                ArrayList<String> pushVar = new ArrayList<>();
                pushVar.add(simboloPila);
                
                Estado estadoApilar = addEstado();
                actual = actual.agregarEstado(estadoApilar, base, "Z0", pushVar);
                
                // Apilar como máximo k-1 veces
                Estado estadoActual = estadoApilar;
                for (int i = 0; i < valorMax - 1; i++) {
                    Estado siguiente = addEstado();
                    estadoActual = estadoActual.agregarEstado(siguiente, base, simboloPila, pushVar);
                    estadoActual = siguiente;
                }
                
                // Verificar pila vacía
                Estado estadoVacio = addEstado();
                ArrayList<String> mantenerZ0 = new ArrayList<>();
                mantenerZ0.add("Z0");
                estadoActual = estadoActual.agregarEstado(estadoVacio, "ε", "#" + simboloPila, mantenerZ0);
                
                return estadoVacio;
            }
        }
        return actual;
    }

    private Estado manejarMenorIgualQue(Estado actual, String base, String exp, Condicion c) {
        for (Termino t : c.terminos) {
            if (esNumerico(t.valor)) {
                int valorMax = Integer.parseInt(t.valor);
                String simboloPila = exp.toUpperCase();
                
                ArrayList<String> pushVar = new ArrayList<>();
                pushVar.add(simboloPila);
                
                Estado estadoApilar = addEstado();
                actual = actual.agregarEstado(estadoApilar, base, "Z0", pushVar);
                
                // Apilar como máximo k veces
                Estado estadoActual = estadoApilar;
                for (int i = 0; i < valorMax; i++) {
                    Estado siguiente = addEstado();
                    estadoActual = estadoActual.agregarEstado(siguiente, base, simboloPila, pushVar);
                    estadoActual = siguiente;
                }
                
                // Verificar pila vacía
                Estado estadoVacio = addEstado();
                ArrayList<String> mantenerZ0 = new ArrayList<>();
                mantenerZ0.add("Z0");
                estadoActual = estadoActual.agregarEstado(estadoVacio, "ε", "#" + simboloPila, mantenerZ0);
                
                return estadoVacio;
            }
        }
        return actual;
    }

    public boolean esNumerico(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public boolean esVariable(String s) {
        if (s == null || s.isEmpty()) return false;
        return s.matches("[a-z]");
    }

    public Estado addEstado() {
        Estado e = new Estado("q" + String.valueOf(numEstado));
        numEstado++;
        estados.add(e);
        return e;
    }

    public void agregarEstructura(String base, String exp) {
        String[] s = {base, exp};
        estructura.add(s);
    }

    public void agregarCondicion(String variable, String ecuacionCompleta) {
        condiciones.put(variable, new Condicion(ecuacionCompleta));
    }

    public boolean setEstadoTerminal(String estado, boolean terminal) {
        for (Estado e : estados) {
            if (e.nombre.equals(estado)) {
                e.terminal = terminal;
                if (terminal && !estadosTerminales.contains(estado)) {
                    estadosTerminales.add(estado);
                }
                return true;
            }
        }
        return false;
    }

    public boolean setEstadoInicial(String s) {
        for (Estado e : estados) {
            if (e.nombre.equals(s)) {
                estadoInicial = s;
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getEstadosTerminales() {
        return estadosTerminales;
    }

    public TreeMap<String, TreeMap<String, String>> getTransiciones() {
        return transiciones;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void imprimirAutomata() {
        System.out.println("=== AUTÓMATA DE PILA ===");
        System.out.println("Estado inicial: " + estadoInicial);
        System.out.println("Estados terminales: " + estadosTerminales);
        System.out.println("\nEstados y transiciones:");
        for (Estado e : estados) {
            System.out.println(e.toString());
        }
    }
}