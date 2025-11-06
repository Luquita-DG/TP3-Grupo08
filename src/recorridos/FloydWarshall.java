package recorridos;

import java.util.*;
import interfaces.INodoGrafo;
import modelo.Arista;
import modelo.Grafo;

/** Floyd–Warshall: todas las pares, distancias mínimas. */
public class FloydWarshall<T> {

    /**
     * Ejecuta Floyd–Warshall y muestra la matriz de distancias.
     * @param grafo grafo de trabajo
     * @param nodos mapa de nodos
     */
    public static <T> void ejecutar(Grafo<T> grafo, Map<T, INodoGrafo<T>> nodos) {
        // Validar que el grafo no esté vacío
        if (nodos.isEmpty()) {
            System.out.println("[!] El grafo está vacío");
            return;
        }

        // Obtener lista de nodos para mantener orden consistente
        List<T> claves = new ArrayList<>(nodos.keySet());
        int n = claves.size();

        // Matriz de distancias inicializada con infinito
        int[][] dist = new int[n][n];
        final int INF = 1_000_000; // Valor grande para representar infinito

        // Inicializar matriz: diagonal en 0, resto en infinito
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0; // Distancia de un nodo a sí mismo es 0
        }

        // Llenar la matriz con las distancias directas del grafo
        for (int i = 0; i < n; i++) {
            INodoGrafo<T> nodo = nodos.get(claves.get(i));
            List<Arista<T>> aristas = nodo.getVecinos();

            // Para cada arista, actualizar la distancia directa
            for (Arista<T> arista : aristas) {
                int idxVecino = claves.indexOf(arista.getDestino().getValor());
                if (idxVecino != -1) {
                    dist[i][idxVecino] = arista.getPeso();
                }
            }
        }

        // Algoritmo de Floyd-Warshall
        // Para cada nodo k como posible nodo intermedio
        for (int k = 0; k < n; k++) {
            // Para cada par de nodos (i, j)
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Verificar si pasar por k mejora la distancia de i a j
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        int nuevaDistancia = dist[i][k] + dist[k][j];
                        if (nuevaDistancia < dist[i][j]) {
                            dist[i][j] = nuevaDistancia;
                        }
                    }
                }
            }
        }

        // Mostrar los resultados
        imprimirMatriz(claves, dist, INF);
    }

    /** Imprime la matriz de distancias mínimas. */
    private static <T> void imprimirMatriz(List<T> claves, int[][] dist, int INF) {
        int n = claves.size();
        System.out.println("[Floyd–Warshall] Matriz de distancias mínimas:\n");
        
        // Calcular ancho de columna
        int anchoColumna = 8;
        for (T nodo : claves) {
            anchoColumna = Math.max(anchoColumna, obtenerEtiqueta(nodo).length());
        }
        
        // Imprimir encabezado
        System.out.print(String.format("%" + anchoColumna + "s │", ""));
        for (T nodo : claves) {
            System.out.print(String.format(" %-" + anchoColumna + "s", obtenerEtiqueta(nodo)));
        }
        System.out.println();
        
        // Imprimir línea separadora
        for (int i = 0; i < anchoColumna; i++) {
            System.out.print("─");
        }
        System.out.print("─┼");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < anchoColumna + 1; j++) {
                System.out.print("─");
            }
        }
        System.out.println();
        
        // Imprimir filas de datos
        for (int i = 0; i < n; i++) {
            System.out.print(String.format("%" + anchoColumna + "s │", 
                                         obtenerEtiqueta(claves.get(i))));
            for (int j = 0; j < n; j++) {
                String valor = (dist[i][j] == INF) ? "INF" : String.valueOf(dist[i][j]);
                System.out.print(String.format(" %-" + anchoColumna + "s", valor));
            }
            System.out.println();
        }
        System.out.println();
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
}
