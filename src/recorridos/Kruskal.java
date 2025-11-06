package recorridos;

import java.util.*;
import interfaces.INodoGrafo;
import modelo.Arista;
import modelo.Grafo;

/** Kruskal: obtiene un MST ordenando aristas por peso y evitando ciclos. */
public class Kruskal<T> {

    /**
     * Ejecuta Kruskal y muestra el árbol de expansión mínima y su peso.
     * @param grafo grafo de trabajo
     * @param nodos mapa de nodos
     */
    public static <T> void ejecutar(Grafo<T> grafo, Map<T, INodoGrafo<T>> nodos) {
        // Validar que el grafo no esté vacío
        if (nodos.isEmpty()) {
            System.out.println("[!] El grafo está vacío");
            return;
        }

        // Lista para almacenar todas las aristas del grafo
        List<AristaKruskal<T>> aristas = new ArrayList<>();

        // Estructura Union-Find para detectar ciclos
        UnionFind<T> uf = new UnionFind<>(nodos.keySet());

        // Conjunto para evitar agregar aristas duplicadas
        Set<String> vistas = new HashSet<>();

        // Recolectar todas las aristas del grafo
        for (Map.Entry<T, INodoGrafo<T>> entry : nodos.entrySet()) {
            T origen = entry.getKey();
            INodoGrafo<T> nodo = entry.getValue();
            List<Arista<T>> aristasNodo = nodo.getVecinos();

            for (Arista<T> arista : aristasNodo) {
                T destino = arista.getDestino().getValor();
                int peso = arista.getPeso();

                // Generar clave única para la arista (sin importar dirección)
                String clave = generarClave(origen, destino);

                // Agregar arista si no fue vista
                if (!vistas.contains(clave)) {
                    vistas.add(clave);
                    aristas.add(new AristaKruskal<>(origen, destino, peso));
                }
            }
        }

        // Ordenar aristas por peso ascendente
        aristas.sort(Comparator.comparingInt(a -> a.peso));

        // Lista para el árbol de expansión mínima
        List<AristaKruskal<T>> mst = new ArrayList<>();
        int pesoTotal = 0;

        // Procesar aristas ordenadas
        for (AristaKruskal<T> arista : aristas) {
            // Agregar arista solo si no forma ciclo
            if (uf.union(arista.origen, arista.destino)) {
                mst.add(arista);
                pesoTotal += arista.peso;
            }
        }

        // Mostrar resultados
        imprimirResultados(mst, pesoTotal);
    }

    /** Genera una clave única para una arista, independiente de la dirección. */
    private static <T> String generarClave(T origen, T destino) {
        int hashOrigen = origen.hashCode();
        int hashDestino = destino.hashCode();
        
        // Ordenar por hash para que la clave sea consistente
        if (hashOrigen < hashDestino) {
            return origen.toString() + "-" + destino.toString();
        } else {
            return destino.toString() + "-" + origen.toString();
        }
    }

    /** Imprime el MST resultante y su peso. */
    private static <T> void imprimirResultados(List<AristaKruskal<T>> mst, int pesoTotal) {
        System.out.println("[Kruskal] Aristas del MST:");
        
        for (AristaKruskal<T> a : mst) {
            System.out.println("  " + obtenerEtiqueta(a.origen) + 
                             " - " + obtenerEtiqueta(a.destino) + 
                             " (peso=" + a.peso + ")");
        }
        
        System.out.println("[Kruskal] Peso total: " + pesoTotal);
    }

    /** Devuelve una etiqueta legible del nodo para imprimir. */
    private static <T> String obtenerEtiqueta(T nodo) {
        String str = nodo.toString();
        
        // Si es una Persona, extraer solo el nombre
        if (str.contains("Nombre:")) {
            int inicio = str.indexOf("Nombre:") + 7;
            int fin = str.indexOf("\n", inicio);
            if (fin > inicio) {
                return str.substring(inicio, fin).trim();
            }
        }
        
        return str.trim();
    }

    /** Arista para Kruskal con origen, destino y peso. */
    private static class AristaKruskal<T> {
        T origen;
        T destino;
        int peso;

        AristaKruskal(T origen, T destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }

    /** Estructura Union-Find (Disjoint Set) para detectar ciclos. */
    private static class UnionFind<T> {
        Map<T, T> padre;

        UnionFind(Set<T> nodos) {
            padre = new HashMap<>();
            for (T nodo : nodos) {
                padre.put(nodo, nodo);
            }
        }

        T find(T x) {
            if (!padre.get(x).equals(x)) {
                padre.put(x, find(padre.get(x))); // Compresión de camino
            }
            return padre.get(x);
        }

        boolean union(T x, T y) {
            T raizX = find(x);
            T raizY = find(y);
            
            // Si tienen la misma raíz, están en el mismo conjunto
            if (raizX.equals(raizY)) {
                return false;
            }
            
            // Unir los conjuntos
            padre.put(raizX, raizY);
            return true;
        }
    }
}