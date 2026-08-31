import java.util.function.Predicate;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
public class Seleccion implements OperacionUnaria{
    
    private final Predicate<List<String>> predicado;

    public Seleccion(Predicate<List<String>> predicado) {
        this.predicado = predicado;
    }

    @Override
    public Relacion ejecutar(Relacion relacion) {

        Set<List<String>> resultado = new LinkedHashSet<>();

        for (List<String> tupla : relacion.getTuplas()) {
            if (predicado.test(tupla)) {
                resultado.add(tupla);
            }
        }

        return new Relacion(relacion.getAtributos(), resultado);
    }

}