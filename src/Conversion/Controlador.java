/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conversion;

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
        this.afnd = new AFND();
        inicializarComponentes();
    }

    public void inicializarComponentes() {
        // columnas iniciales
        String[] columnas = {"Estado", "a", "b"};
        // Crear modelo con columnas y 1 fila vacía
        modelo = new DefaultTableModel(columnas, 0) {
            // Opcional: hago editable todas las celdas excepto la columna "Estado" (si quieres)
            @Override
            public boolean isCellEditable(int row, int column) {
                // SOLO SE MODIFICA SI NO ES LA COLUMNA ESTADOS O LA FILA LETRAS
                return (column != 0 && row != 0);
            }
        };
        // Asignar modelo a la tabla de la vista
        vista.getTblAFND().setModel(modelo);
        // Ajustes visuales opcionales
        vista.getTblAFND().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        String[] opciones = {"Agregar estado", "Eliminar estado",
            "Agregar letra", "Eliminar letra"};

        // modelo y asignación al combo
        DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(opciones);
        vista.getCbxAFND().setModel(comboModel);

        // escuchar cambios de selección
        vista.getCbxAFND().addActionListener(e -> {
            // Se ejecuta cuando cambia la selección (o se vuelve a seleccionar)
            String seleccion = (String) vista.getCbxAFND().getSelectedItem();
            manejarOpcion(seleccion);
        });
    }

    /* ---------------------
       OPERACIONES SOBRE FILAS (ESTADOS)
       --------------------- */
    /**
     * Añade un nuevo estado (fila) con nombre estadoNombre en la primera
     * columna. Rellena las demás columnas con cadena vacía.
     *
     * @param estadoNombre nombre del estado (valor de la columna "Estado")
     * @return true si se añadió correctamente
     */
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

        // Aquí podrías sincronizar con tu modelo AFND, por ejemplo:
        // afnd.insertTransicion(estadoNombre, /*letra*/ "", /*dest*/ "", false);
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
        vista.getTxtInsertarTexto().setText("");  // limpiar textField
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
    /**
     * Añade una nueva letra (columna) al final con nombre 'letra'.
     *
     * @param letra nombre de la columna
     * @return true si se añadió; false si nombre inválido o ya existe
     */
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

        String[] newCols = obtenerNombreColumnas(colIndex, oldCols);
                DefaultTableModel nuevoModelo = new DefaultTableModel(newCols, 0);
        for (int r = 0; r < rows; r++) {
            String[] nuevaFila = new String[oldCols-1];
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
        vista.getTxtInsertarTexto().setText(""); // limpiar

        return true;
    }
    
    public String[] obtenerNombreColumnas(int excluir, int size){
        String[] newCols = new String[size-1]; // Excluir una fila
        for (int c = 0; c < size; c++) {
            if (c == excluir) {
                continue; // Evitar fila
            }
            newCols[c] = (modelo.getColumnName(c));
        }
        return newCols;
    }

    /* ---------------------
       MÉTODOS AUXILIARES DE CONSULTA
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

        return true;
    }

    /**
     * Modifica el nombre del estado en la fila indicada.
     *
     * @param rowIndex índice de fila (0-based)
     * @param nuevoNombre nuevo nombre del estado
     * @return true si se modificó correctamente
     */
    /*
    public boolean modifyEstado(int rowIndex, String nuevoNombre) {
        if (rowIndex < 0 || rowIndex >= modelo.getRowCount()) return false;
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return false;
        // Evitar duplicados de nombre de estado en otra fila
        for (int r = 0; r < modelo.getRowCount(); r++) {
            if (r == rowIndex) continue;
            Object val = modelo.getValueAt(r, 0);
            if (val != null && nuevoNombre.equals(val.toString())) return false;
        }
        modelo.setValueAt(nuevoNombre, rowIndex, 0);
        // Si quieres sincronizar con AFND: actualizar llave del estado en el map, etc.
        return true;
    }
     */
    /**
     * Modifica el nombre de la columna en la posición colIndex.
     *
     * @param colIndex índice de columna (0-based)
     * @param nuevoNombre nuevo nombre de la columna
     * @return true si se modificó; false si índice inválido o nombre duplicado
     */
    /*
    public boolean modifyLetra(int colIndex, String nuevoNombre) {
        if (colIndex < 0 || colIndex >= modelo.getColumnCount()) return false;
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return false;
        // evitar renombrar a un nombre que ya exista en otra columna
        int existing = modelo.findColumn(nuevoNombre);
        if (existing != -1 && existing != colIndex) return false;

        // Reconstruir identificadores de columnas
        int cols = modelo.getColumnCount();
        Vector<String> ids = new Vector<>(cols);
        for (int c = 0; c < cols; c++) {
            if (c == colIndex) ids.add(nuevoNombre);
            else ids.add(modelo.getColumnName(c));
        }
        modelo.setColumnIdentifiers(ids);
        return true;
    }
     */
}
