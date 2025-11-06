package modelo;

import interfaces.INodoGrafo;
import java.util.ArrayList;
import java.util.List;

public class NodoGrafo<T> implements INodoGrafo<T> {

    private T valor;
    private List<Arista<T>> vecinos = new ArrayList<>();

    public NodoGrafo(T valor) {
        this.valor = valor;
    }

    @Override
    public T getValor() {
        return valor;
    }

    @Override
    public void agregarVecino(INodoGrafo<T> destino, int peso) {
        vecinos.add(new Arista<>(destino, peso));
    }

    @Override
    public List<Arista<T>> getVecinos() {
        return vecinos;
    }
}