/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conversion;

import java.util.ArrayList;
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

    private AFND afnd;
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
                // SOLO SE MODIFICA SI NO ES LA COLUMNA ESTADOS
                return (column != 0);
            }
        };

        vista.getTblAFND().setModel(modelo);
        vista.getTblAFND().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTblAFND().getTableHeader().setReorderingAllowed(false);

        String[] opciones = {"Agregar estado", "Eliminar estado",
            "Agregar letra", "Eliminar letra"};
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
            String[] nuevaFila = new String[oldCols - 1];
            for (int c = 0; c < oldCols; c++) {
                if (c == colIndex) {
                    continue;
                }
                nuevaFila[c] = (String) modelo.getValueAt(r, c);
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
        for (int c = 0; c < size; c++) {
            if (c == excluir) {
                continue; // Evitar fila
            }
            newCols[c] = (modelo.getColumnName(c));
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
        } else {
            return false;
        }
        vista.getTxtInsertarTexto().setText("");  // limpiar textField
        return true;
    }

    public boolean validar() {
        this.afnd = new AFND();
        ArrayList<String> rows = nombrarEstados();
        ArrayList<String> cols = nombrarColumnas();

        
        
    }
    
    
    
    
    public ArrayList<String> nombrarColumnas() {
        ArrayList<String> cols = new ArrayList<>();
        int n = modelo.getColumnCount();
        for (int c = 0; c < n; c++) {
            cols.add(modelo.getColumnName(c));
            System.out.println(modelo.getColumnName(c));
        }
        return cols;
    }
    
    public  ArrayList<String> nombrarEstados() {
        ArrayList<String> rows = new ArrayList<>();
        int n = modelo.getRowCount();
        for (int c = 0; c < n; c++) {
            rows.add(modelo.getColumnName(c));
            System.out.println(modelo.getColumnName(c));
        }
        
        for (int r = 0; r < n; r++) {
            String[] nuevaFila = new String[oldCols - 1];
            for (int c = 0; c < oldCols; c++) {
                if (c == colIndex) {
                    continue;
                }
                nuevaFila[c] = (String) modelo.getValueAt(r, c);
            }
            nuevoModelo.addRow(nuevaFila);
        }
        return rows;
    }

}
