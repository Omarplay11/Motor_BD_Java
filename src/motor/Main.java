package motor;

import motor.modelo.Relacion;
import motor.algebra.*;
import motor.io.LectorCSV;
import motor.io.ImpresorConsola;
import java.util.Arrays;

import java.util.Arrays;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== MOTOR DE BASE DE DATOS EN MEMORIA (ÁLGEBRA RELACIONAL) ===");

        // 1. CARGA DE RELACIONES DESDE CSV
        Relacion estudiantes = LectorCSV.cargarRelacion("datos/estudiantes.csv");
        Relacion estudiantesNuevos = LectorCSV.cargarRelacion("datos/estudiantes_nuevos.csv");
        Relacion cursos = LectorCSV.cargarRelacion("datos/cursos.csv");

        // Mostrar relaciones originales
        ImpresorConsola.mostrarRelacion("Estudiantes (Original)", estudiantes);
        ImpresorConsola.mostrarRelacion("Estudiantes Nuevos (Original)", estudiantesNuevos);
        ImpresorConsola.mostrarRelacion("Cursos (Original)", cursos);

        if(estudiantes != null){
            // 2. PRUEBA DE SELECCIÓN (σ_edad >= 18)
            int posEdad = estudiantes.posicionAtributo("edad");

            Predicate<java.util.List<String>> condicionEdad = tupla -> {
                try {
                    int edad = Integer.parseInt(tupla.get(posEdad));
                    return edad >= 18;
                } catch (NumberFormatException e) {
                    return false;
                }
            };

            System.out.println("\n---> Ejecutando Operación: Selección (edad >= 18)...");
            OperacionUnaria seleccion = new Seleccion(condicionEdad);
            Relacion resultadoSeleccion = seleccion.ejecutar(estudiantes);
            ImpresorConsola.mostrarRelacion("Selección (edad >= 18)", resultadoSeleccion);

            // 3. PRUEBA DE PROYECCIÓN (π_nombre, edad)
            System.out.println("\n---> Ejecutando Operación: Proyección (nombre, edad)...");
            OperacionUnaria proyeccion = new Proyeccion(Arrays.asList("nombre", "edad"));
            Relacion resultadoProyeccion = proyeccion.ejecutar(estudiantes);
            ImpresorConsola.mostrarRelacion("Proyección (nombre, edad)", resultadoProyeccion);
        }

        if (estudiantes != null && estudiantesNuevos != null) {
            // 4. PRUEBA DE UNIÓN (Estudiantes ∪ Estudiantes Nuevos)
            System.out.println("\n---> Ejecutando Operación: Unión (Estudiantes U Estudiantes Nuevos)...");
            OperacionBinaria union = new Union();
            Relacion resultadoUnion = union.ejecutar(estudiantes, estudiantesNuevos);
            ImpresorConsola.mostrarRelacion("Unión (Estudiantes U Estudiantes Nuevos)", resultadoUnion);
        }

        if (estudiantes != null && cursos != null) {
            // 5. PRUEBA DE PRODUCTO CARTESIANO (Estudiantes × Cursos)
            System.out.println("\n---> Ejecutando Operación: Producto Cartesiano...");
            OperacionBinaria productoCartesiano = new ProductoCartesiano();
            Relacion resultadoProducto = productoCartesiano.ejecutar(estudiantes, cursos);
            ImpresorConsola.mostrarRelacion("Producto Cartesiano (Estudiantes x Cursos)", resultadoProducto);
        }

        System.out.println("\n=== PRUEBAS FINALIZADAS CON ÉXITO ===");
    }
}