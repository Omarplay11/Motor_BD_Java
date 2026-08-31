import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Union implements OperacionBinaria {
    
    @Override
    public Relacion ejecutar(Relacion relacion1, Relacion relacion2) {
        List<String> atributos1 = relacion1.getAtributos();
        List<String> atributos2 = relacion2.getAtributos();
        Set<String> conjunto1 = new HashSet<>(atributos1);
        Set<String> conjunto2 = new HashSet<>(atributos2);
        if (!conjunto1.equals(conjunto2)) {
            throw new IllegalArgumentException(
                "Las relaciones deben tener los mismos atributos para realizar la unión"
            );
        }

        List<Integer> mapeo = new ArrayList<>();
        for (String atributo : atributos1) {
            mapeo.add(atributos2.indexOf(atributo));
        }

        Set<List<String>> nuevasTuplas = new LinkedHashSet<>();

        nuevasTuplas.addAll(relacion1.getTuplas());

        for (List<String> tupla : relacion2.getTuplas()) {
            List<String> tuplaReordenada = new ArrayList<>();
            for (int posicion : mapeo) {
                tuplaReordenada.add(tupla.get(posicion));
            }
            nuevasTuplas.add(tuplaReordenada);
        }

        return new Relacion(atributos1, nuevasTuplas);
    }
}