package modelo;

import interfaces.INodoGrafo;

public class Arista<T> {
    public INodoGrafo<T> destino;
    public int peso;

    public Arista(INodoGrafo<T> destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public INodoGrafo<T> getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }
}