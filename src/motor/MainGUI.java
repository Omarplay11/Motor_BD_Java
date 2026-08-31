package motor;

import motor.algebra.*;
import motor.io.ImpresorConsola;
import motor.io.LectorCSV;
import motor.modelo.Relacion;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class MainGUI {
    // Almacenamiento en memoria de tus tablas
    private static Map<String, Relacion> baseDeDatos = new HashMap<>();

    public static void main(String[] args) {
        // 1. CARGAR DATOS DESDE CSV
        baseDeDatos.put("estudiantes", LectorCSV.cargarRelacion("datos/estudiantes.csv"));
        baseDeDatos.put("estudiantes_nuevos", LectorCSV.cargarRelacion("datos/estudiantes_nuevos.csv"));
        baseDeDatos.put("cursos", LectorCSV.cargarRelacion("datos/cursos.csv"));

        // 2. CREAR LA VENTANA PRINCIPAL
        JFrame frame = new JFrame("Motor de Base de Datos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 600);
        frame.setLayout(new BorderLayout());

        // 3. PANEL DE CONTROLES SUPERIOR
        JPanel panelControles = new JPanel(new GridLayout(2, 4, 5, 5));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> comboOperacion = new JComboBox<>(new String[]{"Proyección", "Unión", "Producto Cartesiano", "Selección (Prueba Edad)"});
        
        // --- CAMBIO AQUÍ: Obtenemos los nombres de las tablas cargadas automáticamente ---
        String[] nombresTablas = baseDeDatos.keySet().toArray(new String[0]);
        JComboBox<String> comboTabla1 = new JComboBox<>(nombresTablas);
        JComboBox<String> comboTabla2 = new JComboBox<>(nombresTablas);
        
        // Seleccionamos opciones por defecto para que sea más fácil probar
        if (nombresTablas.length > 0) {
            comboTabla1.setSelectedItem("estudiantes");
            comboTabla2.setSelectedItem("estudiantes_nuevos");
        }
        
        JTextField campoParametros = new JTextField("nombre, edad"); 
        JButton botonEjecutar = new JButton("Ejecutar Consulta");

        panelControles.add(new JLabel("Operación:"));
        panelControles.add(comboOperacion);
        panelControles.add(new JLabel("Tabla 1:"));
        panelControles.add(comboTabla1);
        panelControles.add(new JLabel("Tabla 2 (Para Unión/Cartesiano):"));
        panelControles.add(comboTabla2);
        panelControles.add(new JLabel("Atributos (Para Proyección):"));
        panelControles.add(campoParametros);
        
        JPanel panelBoton = new JPanel();
        panelBoton.add(botonEjecutar);

        // 4. ÁREA CENTRAL PARA LOS RESULTADOS
        JTextArea areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13)); 
        JScrollPane scroll = new JScrollPane(areaResultados);

        // 5. LÓGICA DE CONEXIÓN CON TUS CLASES
        botonEjecutar.addActionListener(e -> {
            String operacion = (String) comboOperacion.getSelectedItem();
            
            // --- CAMBIO AQUÍ: Capturamos el dato seleccionado del menú en lugar del texto escrito ---
            String nomTabla1 = (String) comboTabla1.getSelectedItem();
            String nomTabla2 = (String) comboTabla2.getSelectedItem();
            String parametros = campoParametros.getText().trim();

            Relacion t1 = baseDeDatos.get(nomTabla1);
            Relacion t2 = baseDeDatos.get(nomTabla2);

            if (t1 == null) {
                areaResultados.setText("Error: La Tabla 1 '" + nomTabla1 + "' no existe en memoria.");
                return;
            }

            try {
                Relacion resultado = null;

                switch (operacion) {
                    case "Proyección":
                        List<String> atributos = Arrays.asList(parametros.split("\\s*,\\s*"));
                        OperacionUnaria proyeccion = new Proyeccion(atributos);
                        resultado = proyeccion.ejecutar(t1);
                        break;

                    case "Unión":
                        if (t2 == null) throw new IllegalArgumentException("La Tabla 2 '" + nomTabla2 + "' no existe.");
                        OperacionBinaria union = new Union();
                        resultado = union.ejecutar(t1, t2);
                        break;

                    case "Producto Cartesiano":
                        if (t2 == null) throw new IllegalArgumentException("La Tabla 2 '" + nomTabla2 + "' no existe.");
                        OperacionBinaria cartesiano = new ProductoCartesiano();
                        resultado = cartesiano.ejecutar(t1, t2);
                        break;

                    case "Selección (Prueba Edad)":
                        int posEdad = t1.posicionAtributo("edad");
                        Predicate<List<String>> condicion = tupla -> {
                            try {
                                return Integer.parseInt(tupla.get(posEdad)) >= 18;
                            } catch (Exception ex) { return false; }
                        };
                        OperacionUnaria seleccion = new Seleccion(condicion);
                        resultado = seleccion.ejecutar(t1);
                        break;
                }

                if (resultado != null) {
                    String textoSalida = ImpresorConsola.obtenerRelacionComoString(operacion, resultado);
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
}