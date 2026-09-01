package motor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.swing.*;
import motor.algebra.*;
import motor.io.LectorCSV;
import motor.modelo.Relacion;

public class MainGUI {
    private static Map<String, Relacion> baseDeDatos = new HashMap<>();

    public static void main(String[] args) {
        // 1. CARGAR DATOS DESDE CSV
        baseDeDatos.put("estudiantes", LectorCSV.cargarRelacion("datos/estudiantes.csv"));
        baseDeDatos.put("estudiantes_nuevos", LectorCSV.cargarRelacion("datos/estudiantes_nuevos.csv"));
        baseDeDatos.put("cursos", LectorCSV.cargarRelacion("datos/cursos.csv"));

        // 2. CREAR LA VENTANA PRINCIPAL
        JFrame frame = new JFrame("Motor de Base de Datos - Álgebra Relacional");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 700);
        frame.setLayout(new BorderLayout());

        // 3. CONTROLES SUPERIORES (Organizados con GridBagLayout)
        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> comboOperacion = new JComboBox<>(new String[]{
            "Proyección", "Unión", "Producto Cartesiano", "Selección (Filtro Dinámico)"
        });

        String[] nombresTablas = baseDeDatos.keySet().toArray(new String[0]);
        JComboBox<String> comboTabla1 = new JComboBox<>(nombresTablas);
        JComboBox<String> comboTabla2 = new JComboBox<>(nombresTablas);
        JLabel labelTabla2 = new JLabel("Tabla 2 (Operación Binaria):");

        if (nombresTablas.length > 0) {
            comboTabla1.setSelectedItem("estudiantes");
            comboTabla2.setSelectedItem("estudiantes_nuevos");
        }

        // --- CAMPOS DE ENTRADA ---
        JTextField campoAtributosMostrar = new JTextField("*");
        JLabel labelAtributos = new JLabel("Atributos a Mostrar (* o lista):");

        JComboBox<String> comboAtributosTabla1 = new JComboBox<>();
        JComboBox<String> comboOperador = new JComboBox<>(new String[]{"=", ">", "<", ">=", "<=", "!="});
        JTextField campoValorFiltro = new JTextField();
        JLabel labelFiltro = new JLabel("Condición de Selección:");

        // Panel agrupador para el filtro de selección
        JPanel panelFiltroSeleccion = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSel = new GridBagConstraints();
        gbcSel.fill = GridBagConstraints.HORIZONTAL;
        gbcSel.insets = new Insets(0, 2, 0, 2);

        gbcSel.weightx = 0.4; gbcSel.gridx = 0; panelFiltroSeleccion.add(comboAtributosTabla1, gbcSel);
        gbcSel.weightx = 0.2; gbcSel.gridx = 1; panelFiltroSeleccion.add(comboOperador, gbcSel);
        gbcSel.weightx = 0.4; gbcSel.gridx = 2; panelFiltroSeleccion.add(campoValorFiltro, gbcSel);

        JButton botonEjecutar = new JButton("Ejecutar Consulta");

        // --- ORGANIZACIÓN DE COMPONENTES ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelControles.add(new JLabel("Operación:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.9; gbc.gridwidth = 3;
        panelControles.add(comboOperacion, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelControles.add(new JLabel("Tabla 1:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        panelControles.add(comboTabla1, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        panelControles.add(labelTabla2, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        panelControles.add(comboTabla2, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        panelControles.add(labelAtributos, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.9; gbc.gridwidth = 3;
        panelControles.add(campoAtributosMostrar, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.1;
        panelControles.add(labelFiltro, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.9; gbc.gridwidth = 3;
        panelControles.add(panelFiltroSeleccion, gbc);

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonEjecutar);

        // 4. ÁREA DE RESULTADOS
        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaResultados);

        // --- LÓGICA DE ACTUALIZACIÓN DE ATRIBUTOS EN EL COMBOBOX ---
        Runnable actualizarAtributosTabla1 = () -> {
            comboAtributosTabla1.removeAllItems();
            String tablaSeleccionada = (String) comboTabla1.getSelectedItem();
            if (tablaSeleccionada != null && baseDeDatos.containsKey(tablaSeleccionada)) {
                Relacion rel = baseDeDatos.get(tablaSeleccionada);
                if (rel != null && rel.getAtributos() != null) {
                    for (String atr : rel.getAtributos()) {
                        comboAtributosTabla1.addItem(atr);
                    }
                    if (comboAtributosTabla1.getItemCount() > 0) {
                        comboAtributosTabla1.setSelectedIndex(0);
                    }
                }
            }
            comboAtributosTabla1.revalidate();
            comboAtributosTabla1.repaint();
        };

        // Escuchador dinámico de Operación
        comboOperacion.addActionListener(e -> {
            String op = (String) comboOperacion.getSelectedItem();

            boolean esBinaria = "Unión".equals(op) || "Producto Cartesiano".equals(op);
            boolean esSeleccion = "Selección (Filtro Dinámico)".equals(op);

            labelTabla2.setEnabled(esBinaria);
            comboTabla2.setEnabled(esBinaria);

            labelFiltro.setEnabled(esSeleccion);
            comboAtributosTabla1.setEnabled(esSeleccion);
            comboOperador.setEnabled(esSeleccion);
            campoValorFiltro.setEnabled(esSeleccion);

            actualizarAtributosTabla1.run();
        });

        comboTabla1.addActionListener(e -> {
            actualizarAtributosTabla1.run();
        });

        actualizarAtributosTabla1.run();
        
        comboTabla2.setEnabled(false);
        labelTabla2.setEnabled(false);
        labelFiltro.setEnabled(false);
        comboAtributosTabla1.setEnabled(false);
        comboOperador.setEnabled(false);
        campoValorFiltro.setEnabled(false);

        // 5. LÓGICA DE EJECUCIÓN
        botonEjecutar.addActionListener(e -> {
            String operacion = (String) comboOperacion.getSelectedItem();
            String nomTabla1 = (String) comboTabla1.getSelectedItem();
            String nomTabla2 = (String) comboTabla2.getSelectedItem();

            Relacion t1 = baseDeDatos.get(nomTabla1);
            Relacion t2 = baseDeDatos.get(nomTabla2);

            if (t1 == null) {
                areaResultados.setText("Error: La Tabla 1 '" + nomTabla1 + "' no existe.");
                return;
            }

            try {
                Relacion resultado = null;

                switch (operacion) {
                    case "Proyección":
                        resultado = aplicarProyeccionOpcional(t1, campoAtributosMostrar.getText().trim());
                        break;

                    case "Unión":
                        if (t2 == null) throw new IllegalArgumentException("La Tabla 2 '" + nomTabla2 + "' no existe.");
                        OperacionBinaria union = new Union();
                        resultado = union.ejecutar(t1, t2);
                        resultado = aplicarProyeccionOpcional(resultado, campoAtributosMostrar.getText().trim());
                        break;

                    case "Producto Cartesiano":
                        if (t2 == null) throw new IllegalArgumentException("La Tabla 2 '" + nomTabla2 + "' no existe.");
                        OperacionBinaria cartesiano = new ProductoCartesiano();
                        resultado = cartesiano.ejecutar(t1, t2);
                        resultado = aplicarProyeccionOpcional(resultado, campoAtributosMostrar.getText().trim());
                        break;

                    case "Selección (Filtro Dinámico)":
                        String atributoSel = (String) comboAtributosTabla1.getSelectedItem();
                        String operadorSel = (String) comboOperador.getSelectedItem();
                        String valorBuscado = campoValorFiltro.getText().trim();

                        if (atributoSel == null || valorBuscado.isEmpty()) {
                            throw new IllegalArgumentException("Debe seleccionar un atributo e ingresar un valor para filtrar.");
                        }

                        int posAtributo = t1.posicionAtributo(atributoSel);

                        Predicate<List<String>> condicion = tupla -> {
                            String valorTupla = tupla.get(posAtributo);
                            return evaluarCondicion(valorTupla, operadorSel, valorBuscado);
                        };

                        OperacionUnaria seleccion = new Seleccion(condicion);
                        resultado = seleccion.ejecutar(t1);
                        resultado = aplicarProyeccionOpcional(resultado, campoAtributosMostrar.getText().trim());
                        break;
                }

                if (resultado != null) {
                    String textoSalida = generarStringResultadoOrdenado(operacion, resultado);
                    areaResultados.setText(textoSalida);
                }
            } catch (Exception ex) {
                areaResultados.setText("Error al ejecutar: " + ex.getMessage());
            }
        });

        // 6. ENSAMBLAR VENTANA
        frame.add(panelControles, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        frame.add(panelBoton, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static Relacion aplicarProyeccionOpcional(Relacion rel, String parametros) {
        if (parametros == null || parametros.isEmpty() || "*".equals(parametros)) {
            return rel;
        }
        List<String> atributos = Arrays.asList(parametros.split("\\s*,\\s*"));
        OperacionUnaria proyeccion = new Proyeccion(atributos);
        return proyeccion.ejecutar(rel);
    }

    /**
     * Calcula anchos de columnas en base al texto más largo y formatea el resultado alineado.
     */
    private static String generarStringResultadoOrdenado(String operacion, Relacion rel) {
        List<List<String>> tuplas = new ArrayList<>(rel.getTuplas());
        List<String> attrs = rel.getAtributos();

        if (attrs == null || attrs.isEmpty()) {
            return "El resultado no contiene columnas.";
        }

        // 1. Ordenar tuplas numéricamente por la primera columna (ej: ID)
        Collections.sort(tuplas, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> t1, List<String> t2) {
                if (t1.isEmpty() || t2.isEmpty()) return 0;
                String v1 = t1.get(0);
                String v2 = t2.get(0);
                try {
                    Double n1 = Double.parseDouble(v1);
                    Double n2 = Double.parseDouble(v2);
                    return n1.compareTo(n2);
                } catch (NumberFormatException e) {
                    return v1.compareToIgnoreCase(v2);
                }
            }
        });

        // 2. Calcular el ancho máximo requerido para cada columna
        int numCols = attrs.size();
        int[] anchosColumna = new int[numCols];

        for (int i = 0; i < numCols; i++) {
            anchosColumna[i] = attrs.get(i).length();
        }

        for (List<String> tupla : tuplas) {
            for (int i = 0; i < Math.min(numCols, tupla.size()); i++) {
                if (tupla.get(i) != null) {
                    anchosColumna[i] = Math.max(anchosColumna[i], tupla.get(i).length());
                }
            }
        }

        // 3. Formatear la salida con espacios fijos
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESULTADO DE LA OPERACIÓN: ").append(operacion.toUpperCase()).append(" ===\n\n");

        // Construir fila de encabezados
        StringBuilder lineaCabecera = new StringBuilder();
        StringBuilder lineaSeparadora = new StringBuilder();

        for (int i = 0; i < numCols; i++) {
            String formato = "%-" + (anchosColumna[i] + 3) + "s";
            lineaCabecera.append(String.format(formato, attrs.get(i)));
            lineaSeparadora.append("-".repeat(anchosColumna[i] + 3));
        }

        sb.append(lineaCabecera).append("\n");
        sb.append(lineaSeparadora).append("\n");

        // Construir filas de datos
        for (List<String> tupla : tuplas) {
            StringBuilder lineaTupla = new StringBuilder();
            for (int i = 0; i < numCols; i++) {
                String valor = (i < tupla.size() && tupla.get(i) != null) ? tupla.get(i) : "";
                String formato = "%-" + (anchosColumna[i] + 3) + "s";
                lineaTupla.append(String.format(formato, valor));
            }
            sb.append(lineaTupla).append("\n");
        }

        sb.append("\nTotal de tuplas: ").append(tuplas.size()).append("\n");
        return sb.toString();
    }

    private static boolean evaluarCondicion(String valorTupla, String operador, String valorBuscado) {
        try {
            double numTupla = Double.parseDouble(valorTupla);
            double numBuscado = Double.parseDouble(valorBuscado);

            switch (operador) {
                case "=":  return numTupla == numBuscado;
                case ">":  return numTupla > numBuscado;
                case "<":  return numTupla < numBuscado;
                case ">=": return numTupla >= numBuscado;
                case "<=": return numTupla <= numBuscado;
                case "!=": return numTupla != numBuscado;
                default:   return false;
            }
        } catch (NumberFormatException e) {
            int comp = valorTupla.compareToIgnoreCase(valorBuscado);

            switch (operador) {
                case "=":  return comp == 0;
                case ">":  return comp > 0;
                case "<":  return comp < 0;
                case ">=": return comp >= 0;
                case "<=": return comp <= 0;
                case "!=": return comp != 0;
                default:   return false;
            }
        }
    }
}