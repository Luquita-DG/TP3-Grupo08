package test;

import modelo.Grafo;
import modelo.Persona;
import java.util.Map;

public class TestGrafo {
    public static void main(String[] args) {
        // Grafo dirigido para que Dijkstra tenga más sentido en un solo camino
        Grafo<Persona> g = new Grafo<>(true);

        // Nodos
        Persona p1 = new Persona("111", "Juan", "Perez", 30);
        Persona p2 = new Persona("222", "Ana", "Gomez", 25);
        Persona p3 = new Persona("333", "Luis", "Vega", 40);
        Persona p4 = new Persona("444", "Maria", "Sol", 22);

        g.agregarNodo(p1);
        g.agregarNodo(p2);
        g.agregarNodo(p3);
        g.agregarNodo(p4);

        // Aristas con pesos
        g.agregarArista(p1, p2, 5);
        g.agregarArista(p1, p3, 10);
        g.agregarArista(p2, p4, 7);
        g.agregarArista(p3, p4, 2);
        g.agregarArista(p2, p3, -2); // Un camino alternativo

        g.mostrarListaAdyacencia();
        System.out.println();
        g.bfs(p1);
        System.out.println();

        System.out.println("\n--- Dijkstra versión MAX (camino con mayor suma de pesos) ---");
        Map<Persona, Grafo.Camino<Persona>> resultado = g.dijkstraMax(p1);
        for (Map.Entry<Persona, Grafo.Camino<Persona>> e : resultado.entrySet()) {
            System.out.println("Desde " + p1.getNombre() + " hasta " + e.getKey().getNombre() + " -> " + e.getValue());
        }
        g.mostrarMatrizAdyacencia();
        System.out.println();
        g.dfs(p1);                   
        System.out.println();

        // --- PROBAR EL MANEJO DE ERRORES (try-catch) --
        System.out.println("\n--- Probando manejo de error (try-catch) ---");
        try {
            // Intentamos buscar un nodo que NO existe
            System.out.println("Intentando recorrer desde un nodo inexistente...");
            g.bfs(new Persona("999", "Inexistente", "", 0));
        } catch (IllegalArgumentException e) {
            System.err.println("¡Error capturado con éxito!");
            System.err.println(e.getMessage());
        }
    }
}



