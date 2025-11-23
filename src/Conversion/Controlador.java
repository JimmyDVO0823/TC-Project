/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conversion;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kevin
 */
public class Controlador {

    private ArrayList<String> estadosTerminales = new ArrayList<>();
    private String estadoInicial = "";
    private AFND afnd;
    private AFD afd;
    private Vista vista;
    private DefaultTableModel modelo;

    public Controlador(Vista v) {
        this.vista = v;
        inicializarComponentes();
    }

    public void inicializarComponentes() {

        String[] columnas = {"Estado", "a", "b"}; // columnas iniciales
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column != 0); // SOLO SE MODIFICA SI NO ES LA COLUMNA ESTADOS
            }
        };
        
        
        
        vista.getTblAFD().setModel(new DefaultTableModel());
        vista.getTblAFND().setModel(modelo);
        vista.getTblAFND().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTblAFND().getTableHeader().setReorderingAllowed(false);

        String[] opciones = {"Agregar estado", "Eliminar estado",
            "Agregar letra", "Eliminar letra", "Elegir estado inicial", "Elegir estado terminal"};
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(opciones);
        vista.getCbxAFND().setModel(comboModel);

        // escuchar cambios de selección
        vista.getCbxAFND().addActionListener(e -> {
            String seleccion = (String) vista.getCbxAFND().getSelectedItem();
            manejarOpcion(seleccion);
        });
    }

    /* ---------------------
       OPERACIONES SOBRE FILAS (ESTADOS)
       --------------------- */
    public boolean addEstado(String estadoNombre) {
        // Evitar nombres vacíos o nulos
        if (estadoNombre == null || estadoNombre.trim().isEmpty()) {
            return false;
        }

        // Opcional: comprobar si ya existe un estado con ese nombre en la columna 0
        if (existeEstado(estadoNombre)) {
            return false;
        }

        int cols = modelo.getColumnCount();
        Object[] fila = new Object[cols];
        fila[0] = estadoNombre;
        for (int i = 1; i < cols; i++) {
            fila[i] = ""; // valor por defecto para transiciones
        }
        modelo.addRow(fila);
        return true;
    }

    public boolean deleteEstado(String nombreEstado) {
        // buscar fila por el valor en la columna 0
        boolean deleteExitoso = false;
        int filas = modelo.getRowCount();
        int filaEncontrada = -1;
        for (int r = 0; r < filas; r++) {
            Object val = modelo.getValueAt(r, 0);
            if (val != null && nombreEstado.equals(val.toString())) {
                filaEncontrada = r;
                deleteExitoso = true;
                break;
            }
        }
        modelo.removeRow(filaEncontrada);
        vista.getTblAFND().setModel(modelo); // actualizar la tabla
        return true;
    }

    private boolean existeEstado(String nombre) {
        for (int r = 0; r < modelo.getRowCount(); r++) {
            Object v = modelo.getValueAt(r, 0);
            if (v != null && nombre.equals(v.toString())) {
                return true;
            }
        }
        return false;
    }

    /* ---------------------
       OPERACIONES SOBRE COLUMNAS (LETRAS)
       --------------------- */
    public boolean addLetra(String letra) {
        if (letra == null || letra.trim().isEmpty()) {
            return false;
        }
        // comprobar duplicado
        if (modelo.findColumn(letra) != -1) {
            return false;
        }
        modelo.addColumn(letra);
        return true;
    }

    public boolean deleteLetra(String nombreLetra) {
        // buscar índice de la columna por nombre
        int colIndex = modelo.findColumn(nombreLetra);
        if (colIndex <= 0) {
            return false;         // columna no encontrada / no eliminar la col inicial
        }
        int oldCols = modelo.getColumnCount();
        int rows = modelo.getRowCount();

        String[] newCols = excluirNombreColumnas(colIndex, oldCols);
        DefaultTableModel nuevoModelo = new DefaultTableModel(newCols, 0);
        for (int r = 0; r < rows; r++) {
            String[] nuevaFila = new String[newCols.length];
            int count = 0;
            for (int c = 0; c < oldCols; c++) {
                if (c == colIndex) {
                    continue;
                }
                nuevaFila[count] = (String) modelo.getValueAt(r, c);
                count++;
            }
            nuevoModelo.addRow(nuevaFila);
        }
        modelo = nuevoModelo;
        vista.getTblAFND().setModel(modelo); // asigna nuevo modelo
        vista.getTblAFND().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTblAFND().getTableHeader().setReorderingAllowed(false);
        return true;
    }

    public String[] excluirNombreColumnas(int excluir, int size) {
        String[] newCols = new String[size - 1]; // Excluir una fila
        int count = 0;
        for (int c = 0; c < size; c++) {
            if (c == excluir) {
                continue; // Evitar fila
            }
            newCols[count] = ((String) modelo.getColumnName(c));
            count++;
        }
        return newCols;
    }

    /* ---------------------
        MÉTODOS AUXILIARES 
       --------------------- */
    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void manejarOpcion(String metodo) {
        vista.getLblAccion().setText(metodo);
        vista.getTxtInsertarTexto().setText("");

    }

    public boolean realizarAccion() {
        vista.getBtnConvertir().setEnabled(false);
        String opcion = (String) vista.getCbxAFND().getSelectedItem();
        if (opcion == null) {
            return false;
        }
        String texto = vista.getTxtInsertarTexto().getText().trim();
        if (opcion.equals("Agregar estado")) {
            addEstado(texto);
        } else if (opcion.equals("Eliminar estado")) {
            deleteEstado(texto);
        } else if (opcion.equals("Agregar letra")) {
            addLetra(texto);
        } else if (opcion.equals("Eliminar letra")) {
            deleteLetra(texto);
        } else if (opcion.equals("Elegir estado inicial")) {
            setEstadoInicial(texto, vista.getTblAFND());
        } else if (opcion.equals("Elegir estado terminal")) {
            setEstadoTerminal(texto, vista.getTblAFND());
        } else {
            return false;
        }
        vista.getTxtInsertarTexto().setText("");  // limpiar textField
        return true;
    }

    public boolean setEstadoInicial(String input, JTable tabla) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();

        if (!estadoInicial.equals("")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String valor = model.getValueAt(i, 0).toString();
                if (valor.startsWith("->")) {
                    model.setValueAt(valor.substring(2), i, 0);     // quitar "->"
                    break;
                }
            }
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            String valor = model.getValueAt(i, 0).toString();
            if (valor.equals(input)) {
                model.setValueAt("->" + valor, i, 0);   // poner "->" al inicio
                estadoInicial = input;                 // actualizar variable clase
                return true;
            }
        }
        return false;
    }

    public boolean setEstadoTerminal(ArrayList<String> input, JTable tabla){
        for(String s: input){
            setEstadoTerminal(s, tabla);
        }
        return true;
    }
    
    public boolean setEstadoTerminal(String input, JTable tabla) {
        if (input == null) {
            return false;
        }

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        int filas = model.getRowCount();

        for (int i = 0; i < filas; i++) {
            Object o = model.getValueAt(i, 0);
            String celda = o != null ? o.toString() : "";

            // Si la fila está marcada por marcarFila (prefijo "->") no la toques
            if (celda.startsWith("->")) {
                continue;
            }

            String raw = rawContent(celda);

            if (raw.equals(input)) {
                // determinar si ya está marcada con [ ... ]
                boolean estaMarcada = celda.startsWith("[") && celda.endsWith("]");

                if (estaMarcada) {
                    // desmarcar: quitar corchetes
                    model.setValueAt(raw, i, 0);
                    // quitar de la lista si estaba
                    estadosTerminales.remove(raw);
                } else {
                    // marcar: encerrar entre corchetes
                    String marcado = "[" + raw + "]";
                    model.setValueAt(marcado, i, 0);
                    // añadir a la lista si no estaba ya (evitar duplicados)
                    if (!estadosTerminales.contains(raw)) {
                        estadosTerminales.add(raw);
                    }
                }
                return true; // procesamos solo la primera coincidencia
            }
        }
        return false;
    }

    private String rawContent(String cellText) {
        if (cellText == null) {
            return "";
        }
        String s = cellText;
        if (s.startsWith("->")) {
            s = s.substring(2);
        }
        if (s.startsWith("[") && s.endsWith("]") && s.length() >= 2) {
            s = s.substring(1, s.length() - 1);
        }
        return s.trim();
    }

    public boolean validar() {
        this.afnd = new AFND();
        this.afnd.setAlfabeto(nombrarColumnas());
        TreeMap<String, ArrayList<String>> t = obtenerTransiciones();
        for (String clave : t.keySet()) {
            ArrayList<String> valores = t.get(clave);
            for (int i = 0; i < afnd.getAlfabeto().size(); i++) {
                //System.out.println(clave+" "+afnd.getAlfabeto().get(i)+" "+valores.get(i));
                this.afnd.insertTransicion(clave, afnd.getAlfabeto().get(i), valores.get(i));
            }
        }
        this.afnd.setEstadoInicial(estadoInicial);
        for(String s:estadosTerminales) afnd.setEstadoTerminal(s, true);
        this.afd = afnd.conversionAFD();
        vista.getBtnConvertir().setEnabled(true);
        return true;
    }

    public boolean llenarTablaAFD() {
        ArrayList<String> fila = new ArrayList<>();
        ArrayList<String> filaInicial = new ArrayList<>();
        filaInicial.add("Estados");
        filaInicial.addAll(afd.getAlfabeto());

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(filaInicial.toArray());

        for (String key : afd.getTransiciones().keySet()) {
            fila.add(key);
            Map<String, ArrayList<String>> t = afd.getTransiciones().get(key).getTransiciones();
            for (String key2 : t.keySet()) {
                String s = t.get(key2).toString();
                s = s.replaceAll("\\[", "");
                s = s.replaceAll("\\]", "");
                fila.add(s);
            }
            model.addRow(fila.toArray());
            fila = new ArrayList<>();
        }
        vista.getTblAFD().setModel(model);
        setEstadoInicial(estadoInicial ,vista.getTblAFD());
        System.out.println(afd.getEstadosTerminales());
        setEstadoTerminal(afd.getEstadosTerminales(), vista.getTblAFD());
        return true;
    }

    public ArrayList<String> nombrarColumnas() {
        ArrayList<String> cols = new ArrayList<>();
        int n = modelo.getColumnCount();
        for (int c = 1; c < n; c++) {
            cols.add(modelo.getColumnName(c));
            //System.out.println(modelo.getColumnName(c));
        }
        return cols;
    }

    public TreeMap<String, ArrayList<String>> obtenerTransiciones() {
        TreeMap<String, ArrayList<String>> mapa = new TreeMap<>();

        int filas = vista.getTblAFND().getRowCount();
        int columnas = vista.getTblAFND().getColumnCount();

        for (int i = 0; i < filas; i++) {
            // Clave = primera columna
            String clave = rawContent(vista.getTblAFND().getValueAt(i, 0).toString());
            // Valor = resto de columnas en un ArrayList
            ArrayList<String> valores = new ArrayList<>();

            for (int j = 1; j < columnas; j++) {
                Object valor = vista.getTblAFND().getValueAt(i, j);
                valores.add(valor != null ? valor.toString() : "");
            }
            mapa.put(clave, valores);
        }

        return mapa;
    }

    // String Estado, ArrayList <String>
}
