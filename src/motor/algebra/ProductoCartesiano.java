package motor.algebra;
import java.util.ArrayList;
import java.util.List;

import motor.modelo.Relacion;

public class ProductoCartesiano implements OperacionBinaria {

    @Override
    public Relacion ejecutar(Relacion relacion1,Relacion relacion2) {
        if(relacion1 == null || relacion2 == null){
            throw new IllegalArgumentException("Las relaciones no pueden ser null.");
        }
        List<String> nuevosAtributos = new ArrayList<>();
        nuevosAtributos.addAll(relacion1.getAtributos());
        nuevosAtributos.addAll(relacion2.getAtributos());

        Relacion resultado = new Relacion(nuevosAtributos);
        for(List<String> tupla1 : relacion1.getTuplas()){
            for(List<String> tupla2 : relacion2.getTuplas()){
                List<String> nuevaTupla = new ArrayList<>();
                nuevaTupla.addAll(tupla1);
                nuevaTupla.addAll(tupla2);

                resultado.agregarTupla(nuevaTupla);
            }
        }
        return resultado;
    }
}