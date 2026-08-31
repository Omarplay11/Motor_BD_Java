package motor.algebra;

import motor.modelo.Relacion;
import java.util.List;

public class Union implements OperacionBinaria {

    @Override
    public Relacion ejecutar(Relacion relacion1,Relacion relacion2){
        if(relacion1 == null || relacion2 == null){
            throw new IllegalArgumentException("Las relaciones no pueden ser null.");
        }
        if(!relacion1.getAtributos().equals(relacion2.getAtributos())){
            throw new IllegalArgumentException("Las relaciones deben tener los mismos atributos para realizar la unión.");
        }
        Relacion resultado = new Relacion(relacion1.getAtributos());
        for(List<String> tupla : relacion1.getTuplas()){
            resultado.agregarTupla(tupla);
        }
        for(List<String> tupla : relacion2.getTuplas()){
            resultado.agregarTupla(tupla);
        }
        return resultado;
    }
}