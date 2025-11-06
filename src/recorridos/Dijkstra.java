package recorridos;

import java.util.*;
import interfaces.INodoGrafo;
import modelo.Arista;
import modelo.Grafo;

/**
 * Dijkstra: calcula distancias mínimas desde un origen al resto.
 */
public class Dijkstra<T> {

    /**
     * Ejecuta Dijkstra y muestra las distancias desde el origen.
     * @param grafo grafo de trabajo
     * @param origen nodo origen
     * @param nodos mapa de nodos
     */
    public static <T> void ejecutar(Grafo<T> grafo, T origen, Map<T, INodoGrafo<T>> nodos) {
        // Validar que el nodo origen existe
        if (origen == null || !nodos.containsKey(origen)) {
            System.out.println("[!] El nodo de origen no existe en el grafo");
            return;
        }

        // Mapa para guardar las distancias mínimas desde el origen
        Map<T, Integer> distancias = new HashMap<>();

        // Conjunto de nodos ya visitados
        Set<T> visitados = new HashSet<>();

        // Cola de prioridad para procesar nodos por distancia mínima
        PriorityQueue<NodoDistancia<T>> cola = new PriorityQueue<>();

        // Inicializar todas las distancias en infinito
        for (T id : nodos.keySet()) {
            distancias.put(id, Integer.MAX_VALUE);
        }

        // La distancia al nodo origen es 0
        distancias.put(origen, 0);

        // Agregar el nodo origen a la cola
        cola.add(new NodoDistancia<>(origen, 0));

        // Procesar nodos mientras haya elementos en la cola
        while (!cola.isEmpty()) {
            // Obtener el nodo con menor distancia
            NodoDistancia<T> actual = cola.poll();

            // Si ya fue visitado, continuar con el siguiente
            if (visitados.contains(actual.id)) {
                continue;
            }

            // Marcar como visitado
            visitados.add(actual.id);

            // Obtener el nodo actual del grafo
            INodoGrafo<T> nodoActual = nodos.get(actual.id);

            // Obtener todas las aristas (vecinos) del nodo actual
            List<Arista<T>> aristas = nodoActual.getVecinos();

            // Recorrer cada vecino
            for (Arista<T> arista : aristas) {
                T vecinoId = arista.getDestino().getValor();
                int peso = arista.getPeso();

                // Calcular la nueva distancia pasando por el nodo actual
                int nuevaDistancia = distancias.get(actual.id) + peso;

                // Si encontramos un camino más corto, actualizamos
                if (nuevaDistancia < distancias.get(vecinoId)) {
                    distancias.put(vecinoId, nuevaDistancia);
                    cola.add(new NodoDistancia<>(vecinoId, nuevaDistancia));
                }
            }
        }

        // Mostrar los resultados
        imprimirResultados(origen, distancias);
    }

    /** Imprime las distancias mínimas desde el origen. */
    private static <T> void imprimirResultados(T origen, Map<T, Integer> distancias) {
        System.out.println("[Dijkstra] Desde: " + obtenerEtiqueta(origen));
        
        for (Map.Entry<T, Integer> entry : distancias.entrySet()) {
            String distancia = entry.getValue() == Integer.MAX_VALUE 
                ? "INF" 
                : String.valueOf(entry.getValue());
            System.out.println("  → " + obtenerEtiqueta(entry.getKey()) + ": " + distancia);
        }
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

    /** Nodo con su distancia acumulada (para la cola de prioridad). */
    private static class NodoDistancia<T> implements Comparable<NodoDistancia<T>> {
        T id;
        int distancia;

        NodoDistancia(T id, int distancia) {
            this.id = id;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(NodoDistancia<T> otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }
    }
}