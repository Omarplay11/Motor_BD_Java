import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ProductoCartesiano implements OperacionBinaria {
    
    @Override
    public Relacion ejecutar(Relacion relacion1, Relacion relacion2) {
        
        List<String> nuevosAtributos = new ArrayList<>();
        nuevosAtributos.addAll(relacion1.getAtributos());
        nuevosAtributos.addAll(relacion2.getAtributos());

        Set<List<String>> nuevasTuplas = new LinkedHashSet<>();

        for (List<String> tupla1 : relacion1.getTuplas()) {

            for (List<String> tupla2 : relacion2.getTuplas()) {

                List<String> nuevaTupla = new ArrayList<>();
                nuevaTupla.addAll(tupla1);
                nuevaTupla.addAll(tupla2);

                nuevasTuplas.add(nuevaTupla);
            }
        }

        return new Relacion(nuevosAtributos, nuevasTuplas);
    }
}