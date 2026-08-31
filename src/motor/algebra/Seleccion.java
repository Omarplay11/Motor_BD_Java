package motor.algebra;

import motor.modelo.Relacion;
import java.util.List;
import java.util.function.Predicate;

public class Seleccion implements OperacionUnaria {
    private Predicate<List<String>> predicado;
    public Seleccion(Predicate<List<String>> predicado){
        if (predicado == null) {
            throw new IllegalArgumentException("El predicado no puede ser null.");
        }
        this.predicado = predicado;
    }

    @Override
    public Relacion ejecutar(Relacion relacion){
        if(relacion == null){
            throw new IllegalArgumentException("La relación no puede ser null.");
        }
        Relacion resultado = new Relacion(relacion.getAtributos());
        for (List<String> tupla : relacion.getTuplas()){
            if(predicado.test(tupla)){
                resultado.agregarTupla(tupla);
            }
        }
        return resultado;
    }
}