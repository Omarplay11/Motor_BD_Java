package motor.algebra;
import motor.modelo.Relacion;

public interface OperacionBinaria {
    Relacion ejecutar(Relacion r1, Relacion r2);
}
