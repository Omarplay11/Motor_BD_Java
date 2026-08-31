import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
public class Relacion {
    private final List<String> atributos;
    private final Set<List<String>> tuplas;

    public Relacion(List<String> atributos, Set<List<String>> tuplas) {
        for (List<String> tupla : tuplas) {
            if (tupla.size() != atributos.size()) {
                throw new IllegalArgumentException("Tupla con número de valores distinto al de atributos");
            }
        }
        this.atributos = atributos;
        this.tuplas = tuplas;
    }

    public List<String> getAtributos() {
        return atributos;
    }

    public Set<List<String>> getTuplas() {
        return tuplas;
    }

    public int cardinalidad() {
        return tuplas.size();
    }
}
