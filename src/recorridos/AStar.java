package recorridos;

import java.util.*;
import interfaces.INodoGrafo;
import modelo.Arista;
import modelo.Grafo;

/**
 * A* (A estrella): encuentra un camino corto desde un inicio hasta un objetivo
 * guiándose con una heurística (g + h).
 */
public class AStar<T> {

    /** Heurística: estima la distancia del nodo actual al objetivo. */
    public interface Heuristica<T> {
        int estimar(T nodoActual, T nodoDestino);
    }

    /**
     * Ejecuta A* y muestra el camino encontrado (si existe).
     * @param grafo grafo de trabajo
     * @param inicio nodo origen
     * @param objetivo nodo destino
     * @param heuristica función heurística
     * @param nodos mapa de nodos del grafo
     */
    public static <T> void ejecutar(Grafo<T> grafo, T inicio, T objetivo,
                                    Heuristica<T> heuristica, Map<T, INodoGrafo<T>> nodos) {
        // Validar que los nodos existen
        if (inicio == null || objetivo == null || 
            !nodos.containsKey(inicio) || !nodos.containsKey(objetivo)) {
            System.out.println("[!] El nodo de inicio o destino no existe en el grafo");
            return;
        }

        final int INF = 1_000_000; // Valor grande para representar infinito
        
        // gCost: costo real desde el inicio hasta el nodo
        Map<T, Integer> gCost = new HashMap<>();
        
        // fCost: costo estimado total (gCost + heurística)
        Map<T, Integer> fCost = new HashMap<>();
        
        // Mapa para reconstruir el camino
        Map<T, T> padres = new HashMap<>();
        
        // Conjunto de nodos ya procesados
        Set<T> cerrados = new HashSet<>();

        // Inicializar todos los costos en infinito
        for (T id : nodos.keySet()) {
            gCost.put(id, INF);
            fCost.put(id, INF);
        }

        // El costo para llegar al inicio es 0
        gCost.put(inicio, 0);
        fCost.put(inicio, heuristica.estimar(inicio, objetivo));

        // Cola de prioridad ordenada por fCost
        PriorityQueue<NodoCosto<T>> cola = new PriorityQueue<>();
        cola.add(new NodoCosto<>(inicio, fCost.get(inicio)));

        // Procesar nodos mientras haya elementos en la cola
        while (!cola.isEmpty()) {
            NodoCosto<T> actual = cola.poll();
            T idActual = actual.id;

            // Si ya fue procesado, continuar
            if (cerrados.contains(idActual)) {
                continue;
            }

            // Si llegamos al objetivo, reconstruir y mostrar el camino
            if (idActual.equals(objetivo)) {
                reconstruirCamino(inicio, objetivo, padres, gCost);
                return;
            }

            // Marcar como procesado
            cerrados.add(idActual);

            // Obtener el nodo actual
            INodoGrafo<T> nodoActual = nodos.get(idActual);
            List<Arista<T>> aristas = nodoActual.getVecinos();

            // Explorar los vecinos
            for (Arista<T> arista : aristas) {
                T vecinoId = arista.getDestino().getValor();
                
                // Si ya fue procesado, saltar
                if (cerrados.contains(vecinoId)) {
                    continue;
                }

                // Calcular el costo tentativo pasando por el nodo actual
                int tentativeG = gCost.get(idActual) + arista.getPeso();
                
                // Si encontramos un camino mejor
                if (tentativeG < gCost.get(vecinoId)) {
                    // Actualizar el padre y los costos
                    padres.put(vecinoId, idActual);
                    gCost.put(vecinoId, tentativeG);
                    
                    // Calcular el costo total estimado
                    int h = heuristica.estimar(vecinoId, objetivo);
                    fCost.put(vecinoId, tentativeG + h);

                    // Agregar a la cola
                    cola.add(new NodoCosto<>(vecinoId, fCost.get(vecinoId)));
                }
            }
        }

        // Si llegamos aquí, no se encontró camino
        System.out.println("[A*] No hay camino desde " +
                obtenerEtiqueta(inicio) + " hasta " + obtenerEtiqueta(objetivo));
    }

    /** Reconstruye y muestra el camino desde inicio hasta objetivo. */
    private static <T> void reconstruirCamino(T inicio, T objetivo, 
                                             Map<T, T> padres, Map<T, Integer> gCost) {
        // Reconstruir el camino desde el objetivo hacia el inicio
        List<T> camino = new ArrayList<>();
        T temp = objetivo;
        
        while (padres.containsKey(temp)) {
            camino.add(temp);
            temp = padres.get(temp);
        }
        camino.add(inicio);
        
        // Invertir para mostrar desde inicio a objetivo
        Collections.reverse(camino);
        
        // Mostrar el resultado (salida simple)
        System.out.print("[A*] Camino: ");
        for (int i = 0; i < camino.size(); i++) {
            System.out.print(obtenerEtiqueta(camino.get(i)));
            if (i < camino.size() - 1) {
                System.out.print(" → ");
            }
        }
        System.out.println();
        System.out.println("[A*] Costo total: " + gCost.get(objetivo));
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

    /** Nodo de la cola con su costo total estimado (f = g + h). */
    private static class NodoCosto<T> implements Comparable<NodoCosto<T>> {
        T id;
        int fCost;

        NodoCosto(T id, int fCost) {
            this.id = id;
            this.fCost = fCost;
        }

        @Override
        public int compareTo(NodoCosto<T> otro) {
            return Integer.compare(this.fCost, otro.fCost);
        }
    }
}