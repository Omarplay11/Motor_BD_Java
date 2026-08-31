import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Proyeccion implements OperacionUnaria {
    
    private final List<String> atributosSeleccionados;

    public Proyeccion(List<String> atributosSeleccionados) {
        this.atributosSeleccionados = atributosSeleccionados;
    }

    @Override
    public Relacion ejecutar(Relacion relacion) {
        List<Integer> posiciones = new ArrayList<>();
        for (String atributo : atributosSeleccionados) {
            int posicion = relacion.getAtributos().indexOf(atributo);
            if (posicion == -1) {
                throw new IllegalArgumentException(
                    "El atributo no existe: " + atributo
                );
            }
            posiciones.add(posicion);
        }

        Set<List<String>> nuevasTuplas = new LinkedHashSet<>();
        for (List<String> tupla : relacion.getTuplas()) {
            List<String> nuevaTupla = new ArrayList<>();
            for (int posicion : posiciones) {
                nuevaTupla.add(tupla.get(posicion));
            }
            nuevasTuplas.add(nuevaTupla);
        }

        return new Relacion(new ArrayList<>(atributosSeleccionados), nuevasTuplas);
    }
}