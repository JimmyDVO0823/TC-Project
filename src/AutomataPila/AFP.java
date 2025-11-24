package AutomataPila;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Kevin
 */
public class AFP {

    TreeMap<String, String> estructura;
    TreeMap<String, Condicion> condiciones;
    String estadoInicial = "";
    ArrayList<String> estadosTerminales;
    TreeMap<String, Stack<String>> mapPilas;
    TreeMap<String, TreeMap<String, String>> transiciones;
    

    public AFP() {
        this.estructura = new TreeMap<>();
        this.condiciones = new TreeMap<>();
        this.estadosTerminales = new ArrayList<>();
        this.mapPilas = new TreeMap<>();
        this.transiciones = new TreeMap<>();
        condiciones.put("e", new Condicion("e = 1"));
    }

    public void insertTransicion(){};
    
    
    public void agregarEstructura(String base, String exp) {
        estructura.put(base, exp);
    }

    public void agregarCondicion(String variable, String ecuacionCompleta) {
        condiciones.put(variable, new Condicion(ecuacionCompleta));
    }

    public boolean setEstadoTerminal(String estado, boolean terminal) {
        if (transiciones.containsKey(estado)) {
            estadosTerminales.add(estado);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setEstadoInicial(String s) {
    if (transiciones.containsKey(s)) {  
        estadoInicial = s;         
        return true;               
    } else {
        return false;              
    }
    }

    public ArrayList<String> getEstadosTerminales(){
        return estadosTerminales;
    }
    
    public TreeMap<String, TreeMap<String, String>> getTransiciones() {
        return transiciones;
    }
    
    public String getEstadoInicial(){
        return estadoInicial;
    }

    
}
