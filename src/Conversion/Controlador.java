/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conversion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador de la interfaz para la conversión de AFND a AFD.
 * Gestiona la interacción entre la vista (tabla y componentes gráficos)
 * y los modelos de autómatas (AFND y AFD).
 * Permite agregar/eliminar estados y letras, definir estados iniciales y terminales,
 * validar el AFND y llenar la tabla de AFD resultante.
 * 
 * Mantiene referencias a:
 * - {@link AFND} afnd: autómata no determinista.
 * - {@link AFD} afd: autómata determinista resultante.
 * - {@link Vista} vista: componentes gráficos.
 * - {@link DefaultTableModel} modeloAFND: modelo de la tabla AFND.
 * 
 * @author Kevin
 */
public class Controlador {
/** Lista de nombres de los estados terminales seleccionados en la interfaz */
    public ArrayList<String> estadosTerminales = new ArrayList<>();
    /** Nombre del estado inicial seleccionado en la interfaz */
    public String estadoInicial = "";
    /** Objeto AFND asociado al controlador */
    public AFND afnd;
    /** Objeto AFD generado a partir del AFND */
    public AFD afd;
    /** Objeto de la vista que contiene tablas y componentes gráficos */
    public Vista vista;
    /** Modelo de tabla utilizado para representar el AFND en la interfaz */
    public DefaultTableModel modeloAFND;

    /**
 * Inicializa el controlador con la vista proporcionada y configura los
 * componentes iniciales (tablas, combo box y listeners).
 *
 * @param v Instancia de {@link Vista} asociada al controlador.
 */
    public Controlador(Vista v) {
        this.vista = v;
        inicializarComponentes();
    }
/**
 * Configura los componentes gráficos iniciales:
 * - Inicializa la tabla AFND con columnas "Estado", "a", "b".
 * - Desactiva la edición de la primera columna.
 * - Configura el combo box de acciones disponibles.
 * - Agrega listener para cambios de selección del combo box.
 */
    public void inicializarComponentes() {

        String[] columnas = {"Estado", "a", "b"}; // columnas iniciales
        modeloAFND = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column != 0); // SOLO SE MODIFICA SI NO ES LA COLUMNA ESTADOS
            }
        };
 
        vista.getTblAFD().setModel(new DefaultTableModel());
        vista.getTblAFND().setModel(modeloAFND);
        vista.getTblAFND().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTblAFND().getTableHeader().setReorderingAllowed(false);

        String[] opciones = {"Agregar estado", "Eliminar estado",
            "Agregar letra", "Eliminar letra", "Elegir estado inicial", "Elegir estado terminal"};
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(opciones);
        vista.getCbxAFND().setModel(comboModel);

        // escuchar cambios de selección
        vista.getCbxAFND().addActionListener(e -> {
            String seleccion = (String) vista.getCbxAFND().getSelectedItem();
            listenerNombreTXT(seleccion);
        });
    }

    /* ---------------------
       OPERACIONES SOBRE FILAS (ESTADOS)
       --------------------- */
    /**
 * Agrega un nuevo estado a la tabla AFND si no existe y no está vacío.
 *
 * @param estado Nombre del estado a agregar.
 * @return true si se agregó correctamente, false si ya existía o era vacío.
 */
    public boolean addEstado(String estado) {
        // Evitar nombres vacíos o nulos
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }

        // Opcional: comprobar si ya existe un estado con ese nombre en la columna 0
        if (existeEstado(estado)) {
            return false;
        }

        int cols = modeloAFND.getColumnCount();
        Object[] fila = new Object[cols];
        fila[0] = estado;
        for (int i = 1; i < cols; i++) {
            fila[i] = ""; // valor por defecto para transiciones
        }
        modeloAFND.addRow(fila);
        return true;
    }
/**
 * Elimina un estado de la tabla AFND por su nombre.
 *
 * @param estado Nombre del estado a eliminar.
 * @return true si se eliminó correctamente, false si no se encontró.
 */
    public boolean deleteEstado(String estado) {
        // buscar fila por el valor en la columna 0
        int filas = modeloAFND.getRowCount();
        int filaEncontrada = -1;
        for (int r = 0; r < filas; r++) {
            Object val = modeloAFND.getValueAt(r, 0);
            if (val != null && estado.equals(val.toString())) {
                filaEncontrada = r;
                break;
            }
        }
        modeloAFND.removeRow(filaEncontrada);
        vista.getTblAFND().setModel(modeloAFND); // actualizar la tabla
        return true;
    }
/** Verifica si un estado ya existe en la tabla AFND */
    private boolean existeEstado(String estado) {
        for (int r = 0; r < modeloAFND.getRowCount(); r++) {
            Object v = modeloAFND.getValueAt(r, 0);
            if (v != null && estado.equals(v.toString())) {
                return true;
            }
        }
        return false;
    }


    /**
 * Agrega una letra (columna) a la tabla AFND si no existe y no está vacía.
 *
 * @param letra Símbolo a agregar como columna.
 * @return true si se agregó correctamente, false si ya existía o era vacío.
 */
    public boolean addLetra(String letra) {
        if (letra == null || letra.trim().isEmpty()) {
            return false;
        }
        // comprobar duplicado
        if (modeloAFND.findColumn(letra) != -1) {
            return false;
        }
        modeloAFND.addColumn(letra);
        return true;
    }
/**
 * Elimina una letra (columna) de la tabla AFND por su nombre.
 *
 * @param letra Símbolo a eliminar.
 * @return true si se eliminó correctamente, false si no se encontró o es la columna de estado.
 */
    public boolean deleteLetra(String letra) {
        // buscar índice de la columna por nombre
        int colIndex = modeloAFND.findColumn(letra);
        if (colIndex <= 0) {
            return false;         // columna no encontrada / no eliminar la col inicial
        }
        int oldCols = modeloAFND.getColumnCount();
        int rows = modeloAFND.getRowCount();

        String[] newCols = excluirColumnaID(colIndex, oldCols);
        DefaultTableModel nuevoModelo = new DefaultTableModel(newCols, 0);
        for (int r = 0; r < rows; r++) {
            String[] nuevaFila = new String[newCols.length];
            int count = 0;
            for (int c = 0; c < oldCols; c++) {
                if (c == colIndex) {
                    continue;
                }
                nuevaFila[count] = (String) modeloAFND.getValueAt(r, c);
                count++;
            }
            nuevoModelo.addRow(nuevaFila);
        }
        modeloAFND = nuevoModelo;
        vista.getTblAFND().setModel(modeloAFND); // asigna nuevo modeloAFND
        vista.getTblAFND().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTblAFND().getTableHeader().setReorderingAllowed(false);
        return true;
    }
/**
 * Crea un arreglo de nombres de columnas excluyendo una columna específica.
 *
 * @param excluir Índice de la columna a excluir.
 * @param size Cantidad total de columnas.
 * @return Array de nombres de columnas sin la columna excluida.
 */
    public String[] excluirColumnaID(int excluir, int size) {
        String[] newCols = new String[size - 1]; // Excluir una fila
        int count = 0;
        for (int c = 0; c < size; c++) {
            if (c == excluir) {
                continue; // Evitar fila
            }
            newCols[count] = ((String) modeloAFND.getColumnName(c));
            count++;
        }
        return newCols;
    }

    /**
 * Actualiza la etiqueta de acción y limpia el campo de texto según
 * la opción seleccionada en el combo box.
 *
 * @param metodo Acción seleccionada en la interfaz.
 */
    private void listenerNombreTXT(String metodo) {
        vista.getLblAccion().setText(metodo);
        vista.getTxtInsertarTexto().setText("");

    }
/**
 * Ejecuta la acción seleccionada en el combo box (agregar/eliminar estado o letra,
 * elegir estado inicial o terminal).
 *
 * @return true si la acción se ejecutó correctamente, false si hubo error.
 */
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
/**
 * Marca un estado como inicial en la tabla AFND/AFD.
 * Desmarca el estado inicial anterior si existía.
 *
 * @param input Nombre del estado a establecer como inicial.
 * @param tabla Tabla donde se realiza la modificación.
 * @return true si se actualizó correctamente, false si no se encontró.
 */
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
/**
 * Marca múltiples estados como terminales en la tabla AFND/AFD.
 *
 * @param input Lista de estados a establecer como terminales.
 * @param tabla Tabla donde se realiza la modificación.
 * @return true si se actualizaron correctamente.
 */
    public boolean setEstadoTerminal(ArrayList<String> input, JTable tabla){
        for(String s: input){
            setEstadoTerminal(s, tabla);
        }
        return true;
    }
 /**
 * Marca o desmarca un estado como terminal en la tabla AFND/AFD.
 *
 * @param input Nombre del estado a marcar/desmarcar.
 * @param tabla Tabla donde se realiza la modificación.
 * @return true si se actualizó correctamente, false si no se encontró.
 */
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

            String raw = cleanInput(celda);

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
/** Limpia los prefijos y corchetes de un valor de celda */
    private String cleanInput(String cellText) {
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
/**
 * Valida la tabla AFND, crea el objeto {@link AFND} correspondiente,
 * establece el estado inicial y terminales, y genera el {@link AFD} resultante.
 *
 * @return true si la validación y conversión se realizaron correctamente.
 */
    public boolean validarAFND() {
        this.afnd = new AFND();
        this.afnd.alfabeto = nombrarColumnas();
        LinkedHashMap<String, ArrayList<String>> t = leerTransicionesTablaAFND();
        for (String clave : t.keySet()) {
            ArrayList<String> valores = t.get(clave);
            for (int i = 0; i < afnd.alfabeto.size(); i++) {
                //System.out.println(clave+" "+afnd.getAlfabeto().get(i)+" "+valores.get(i));
                this.afnd.insertTransicion(clave, afnd.alfabeto.get(i), valores.get(i));
            }
        }
        this.afnd.setEstadoInicial(estadoInicial);
        for(String s:estadosTerminales) afnd.setEstadoTerminal(s, true);
        this.afd = afnd.conversionAFD();
        vista.getBtnConvertir().setEnabled(true);
        return true;
    }
/**
 * Llena la tabla de la vista con los estados y transiciones del {@link AFD}.
 * También marca el estado inicial y los terminales correspondientes.
 *
 * @return true si la tabla se llenó correctamente.
 */
    public boolean llenarTablaAFD() {
        ArrayList<String> fila = new ArrayList<>();
        ArrayList<String> filaInicial = new ArrayList<>();
        filaInicial.add("Estados");
        filaInicial.addAll(afd.alfabeto);

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(filaInicial.toArray());

        for (String key : afd.transiciones.keySet()) {
            if(key.equals("") || key.equals(" ")) continue;
            fila.add(key);
            Map<String, ArrayList<String>> t = afd.transiciones.get(key).transiciones;
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
/**
 * Obtiene los nombres de las columnas (alfabeto) de la tabla AFND,
 * excluyendo la primera columna de estados.
 *
 * @return Lista de nombres de símbolos.
 */
    public ArrayList<String> nombrarColumnas() {
        ArrayList<String> cols = new ArrayList<>();
        int n = modeloAFND.getColumnCount();
        for (int c = 1; c < n; c++) {
            cols.add(modeloAFND.getColumnName(c));
            //System.out.println(modeloAFND.getColumnName(c));
        }
        return cols;
    }
/**
 * Lee todas las transiciones de la tabla AFND y las convierte en un
 * {@link LinkedHashMap} donde la clave es el estado y el valor
 * es la lista de transiciones por cada símbolo.
 *
 * @return Mapa con las transiciones de la tabla AFND.
 */
    public LinkedHashMap<String, ArrayList<String>> leerTransicionesTablaAFND() {
        LinkedHashMap<String, ArrayList<String>> mapa = new LinkedHashMap<>();

        int filas = vista.getTblAFND().getRowCount();
        int columnas = vista.getTblAFND().getColumnCount();

        for (int i = 0; i < filas; i++) {
            // Clave = primera columna
            String clave = cleanInput(vista.getTblAFND().getValueAt(i, 0).toString());
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

}
