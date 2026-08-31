package motor.modelo;
import java.util.ArrayList;
import java.util.List;

public class Relacion {

    private List<String> atributos;
    private List<List<String>> tuplas;

    public Relacion(List<String> atributos){
        if(atributos == null){
            throw new IllegalArgumentException("Los atributos no pueden ser null.");
        }
        this.atributos = new ArrayList<>(atributos);
        this.tuplas = new ArrayList<>();
    }

    public void agregarTupla(List<String> tupla){
        if(tupla == null){
            throw new IllegalArgumentException("La tupla no puede ser null.");
        }
        if(tupla.size() != atributos.size()){
            throw new IllegalArgumentException("La cantidad de valores de la tupla debe coincidir con la cantidad de atributos.");
        }
        List<String> nuevaTupla = new ArrayList<>(tupla);
        // Las relaciones se comportan como conjuntos,
        // por lo que no se permiten tuplas duplicadas.
        if(!tuplas.contains(nuevaTupla)){
            tuplas.add(nuevaTupla);
        }
    }

    public List<String> getAtributos(){
        return new ArrayList<>(atributos);
    }

    public List<List<String>> getTuplas(){
        List<List<String>> copia = new ArrayList<>();
        for (List<String> tupla : tuplas){
            copia.add(new ArrayList<>(tupla));
        }
        return copia;
    }

    public int cardinalidad(){
        return tuplas.size();
    }

    public boolean tieneAtributo(String atributo){
        return atributos.contains(atributo);
    }

    public int posicionAtributo(String atributo){
        int posicion = atributos.indexOf(atributo);
        if (posicion == -1) {
            throw new IllegalArgumentException("El atributo '" + atributo + "' no existe en la relación.");
        }
        return posicion;
    }
}