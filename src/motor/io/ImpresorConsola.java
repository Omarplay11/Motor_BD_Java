package motor.io;
import motor.modelo.Relacion;
import java.util.ArrayList;
import java.util.List;

public class ImpresorConsola {

    public static void mostrarRelacion(String titulo, Relacion relacion) {
        if(relacion == null){
            System.out.println("\n[!] La relación '" + titulo + "' es nula o no fue cargada correctamente.");
            return;
        }
        List<String> atributos = relacion.getAtributos();
        List<List<String>> tuplas = relacion.getTuplas();

        // 1. Calcular el ancho máximo por cada columna
        List<Integer> anchos = new ArrayList<>();
        for (int i = 0; i < atributos.size(); i++) {
            int maxAncho = atributos.get(i).length();
            for (List<String> tupla : tuplas) {
                if (i < tupla.size()) {
                    maxAncho = Math.max(maxAncho, tupla.get(i).length());
                }
            }
            // Agregamos un margen de espacio
            anchos.add(maxAncho + 2);
        }
        // 2. Construir la línea divisoria
        StringBuilder lineaDivisoria = new StringBuilder("+");
        for (int ancho : anchos) {
            lineaDivisoria.append("-".repeat(ancho + 2)).append("+");
        }
        System.out.println("\n" + lineaDivisoria);
        System.out.println("  RELACIÓN / RESULTADO: " + titulo.toUpperCase());
        System.out.println(lineaDivisoria);

        // 3. Imprimir Atributos (Encabezado)
        StringBuilder encabezado = new StringBuilder("| ");
        for (int i = 0; i < atributos.size(); i++) {
            encabezado.append(String.format("%-" + anchos.get(i) + "s | ", atributos.get(i)));
        }
        System.out.println(encabezado);
        System.out.println(lineaDivisoria);

        // 4. Imprimir Tuplas
        if(tuplas.isEmpty()){
            System.out.println("| (Relación vacía / Sin registros)");
        }else {
            for (List<String> tupla : tuplas) {
                StringBuilder fila = new StringBuilder("| ");
                for (int i = 0; i < tupla.size(); i++) {
                    fila.append(String.format("%-" + anchos.get(i) + "s | ", tupla.get(i)));
                }
                System.out.println(fila);
            }
        }

        // 5. Impresión formal de la cardinalidad
        System.out.println(lineaDivisoria);
        System.out.println("  Cardinalidad (|R|): " + relacion.cardinalidad() + " tupla(s).");
        System.out.println(lineaDivisoria + "\n");
    }


    public static String obtenerRelacionComoString(String titulo, Relacion relacion) {
        if(relacion == null){
            return "\n[!] La relación '" + titulo + "' es nula.\n";
        }
        
        List<String> atributos = relacion.getAtributos();
        List<List<String>> tuplas = relacion.getTuplas();
        StringBuilder sb = new StringBuilder();

        List<Integer> anchos = new java.util.ArrayList<>();
        for (int i = 0; i < atributos.size(); i++) {
            int maxAncho = atributos.get(i).length();
            for (List<String> tupla : tuplas) {
                if (i < tupla.size()) {
                    maxAncho = Math.max(maxAncho, tupla.get(i).length());
                }
            }
            anchos.add(maxAncho + 2); 
        }

        StringBuilder lineaDivisoria = new StringBuilder("+");
        for (int ancho : anchos) {
            lineaDivisoria.append("-".repeat(ancho + 2)).append("+");
        }
        
        sb.append("\n").append(lineaDivisoria).append("\n");
        sb.append(" RELACIÓN / RESULTADO: ").append(titulo.toUpperCase()).append("\n");
        sb.append(lineaDivisoria).append("\n");

        StringBuilder encabezado = new StringBuilder("| ");
        for (int i = 0; i < atributos.size(); i++) {
            encabezado.append(String.format("%-" + anchos.get(i) + "s | ", atributos.get(i)));
        }
        sb.append(encabezado).append("\n");
        sb.append(lineaDivisoria).append("\n");

        for (List<String> tupla : tuplas) {
            StringBuilder fila = new StringBuilder("| ");
            for (int i = 0; i < atributos.size(); i++) {
                String valor = (i < tupla.size()) ? tupla.get(i) : "";
                fila.append(String.format("%-" + anchos.get(i) + "s | ", valor));
            }
            sb.append(fila).append("\n");
        }
        sb.append(lineaDivisoria).append("\n");

        return sb.toString();
    }
}