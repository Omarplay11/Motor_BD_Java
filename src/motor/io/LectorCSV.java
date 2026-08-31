package motor.io;
import motor.modelo.Relacion;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    public static Relacion cargarRelacion(String rutaArchivo) {
        Relacion relacion = null;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean esPrimeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                // Separar valores considerando comas (puedes cambiar a ";" si tu CSV usa punto y coma)
                String[] valores = linea.split(",");
                List<String> fila = new ArrayList<>();
                for(String v : valores){
                    fila.add(v.trim());
                }
                if(esPrimeraLinea){
                    // Instancia la relación con la primera fila como lista de atributos
                    relacion = new Relacion(fila);
                    esPrimeraLinea = false;
                } else {
                    // Agrega la tupla; la clase Relacion se encarga de validar el tamaño y evitar duplicados
                    if (relacion != null) {
                        relacion.agregarTupla(fila);
                    }
                }
            }
        }catch (IOException e){
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return relacion;
    }
}
