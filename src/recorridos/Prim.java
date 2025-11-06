package recorridos;

import java.util.*;
import interfaces.INodoGrafo;
import modelo.Arista;
import modelo.Grafo;

/** Prim: construye un MST creciendo desde un nodo con aristas mínimas. */
public class Prim<T> {

    /**
     * Ejecuta Prim y muestra el MST y su peso total.
     * @param grafo grafo de trabajo
     * @param nodos mapa de nodos
     */
    public static <T> void ejecutar(Grafo<T> grafo, Map<T, INodoGrafo<T>> nodos) {
        // Validar que el grafo no esté vacío
        if (nodos.isEmpty()) {
            System.out.println("[!] El grafo está vacío");
            return;
        }

        // Conjunto de nodos ya visitados
        Set<T> visitados = new HashSet<>();
        
        // Cola de prioridad para seleccionar la arista de menor peso
        PriorityQueue<AristaPrim<T>> cola = new PriorityQueue<>(
            Comparator.comparingInt(a -> a.peso)
        );
        
        // Lista para almacenar las aristas del MST
        List<AristaPrim<T>> mst = new ArrayList<>();

        // Comenzar desde el primer nodo disponible
        T inicio = nodos.keySet().iterator().next();
        visitados.add(inicio);

        // Agregar todas las aristas del nodo inicial a la cola
        agregarAristas(nodos.get(inicio), visitados, cola);

        int pesoTotal = 0;

        // Procesar mientras haya aristas y no se complete el MST
        while (!cola.isEmpty() && visitados.size() < nodos.size()) {
            // Obtener la arista de menor peso
            AristaPrim<T> arista = cola.poll();

            // Si el destino ya fue visitado, descartar (evita ciclos)
            if (visitados.contains(arista.destino)) {
                continue;
            }

            // Agregar arista al MST
            mst.add(arista);
            pesoTotal += arista.peso;
            visitados.add(arista.destino);

            // Agregar las aristas del nuevo nodo visitado
            agregarAristas(nodos.get(arista.destino), visitados, cola);
        }

        // Mostrar resultados
        imprimirResultados(mst, pesoTotal);
    }

    /** Agrega a la cola las aristas del nodo hacia no visitados. */
    private static <T> void agregarAristas(INodoGrafo<T> nodo, Set<T> visitados, 
                                          PriorityQueue<AristaPrim<T>> cola) {
        List<Arista<T>> aristas = nodo.getVecinos();
        T origen = nodo.getValor();

        for (Arista<T> arista : aristas) {
            T destino = arista.getDestino().getValor();
            int peso = arista.getPeso();
            
            // Solo agregar aristas hacia nodos no visitados
            if (!visitados.contains(destino)) {
                cola.add(new AristaPrim<>(origen, destino, peso));
            }
        }
    }

    /** Imprime el MST resultante y su peso total. */
    private static <T> void imprimirResultados(List<AristaPrim<T>> mst, int pesoTotal) {
        System.out.println("[Prim] Aristas del MST:");
        
        for (AristaPrim<T> a : mst) {
            System.out.println("  " + obtenerEtiqueta(a.origen) + 
                             " - " + obtenerEtiqueta(a.destino) + 
                             " (peso=" + a.peso + ")");
        }
        
        System.out.println("[Prim] Peso total: " + pesoTotal);
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

    /** Arista para Prim con origen, destino y peso. */
    private static class AristaPrim<T> {
        T origen;
        T destino;
        int peso;

        AristaPrim(T origen, T destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }
}