package motor.algebra;
import java.util.ArrayList;
import java.util.List;

import motor.modelo.Relacion;

public class Proyeccion implements OperacionUnaria {
    private List<String> atributosSeleccionados;
    public Proyeccion(List<String> atributosSeleccionados){
        if(atributosSeleccionados == null || atributosSeleccionados.isEmpty()){
            throw new IllegalArgumentException("Debe especificar al menos un atributo.");
        }
        this.atributosSeleccionados = new ArrayList<>(atributosSeleccionados);
    }
    @Override
    public Relacion ejecutar(Relacion relacion){
        if(relacion == null){
            throw new IllegalArgumentException("La relación no puede ser null.");
        }
        List<Integer> posiciones = new ArrayList<>();
        for(String atributo : atributosSeleccionados){
            posiciones.add(relacion.posicionAtributo(atributo));
        }
        Relacion resultado = new Relacion(atributosSeleccionados);
        for(List<String> tupla : relacion.getTuplas()){
            List<String> nuevaTupla = new ArrayList<>();
            for(Integer posicion : posiciones){
                nuevaTupla.add(tupla.get(posicion));
            }
            resultado.agregarTupla(nuevaTupla);
        }
        return resultado;
    }
}