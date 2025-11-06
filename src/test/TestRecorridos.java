package test;

import modelo.Grafo;
import modelo.Persona;
import interfaces.INodoGrafo;
import java.util.Map;

// Importamos todos los algoritmos del nuevo paquete
import recorridos.Dijkstra;
import recorridos.AEstrella;
import recorridos.Prim;
import recorridos.Kruskal;
import recorridos.FloydWarshall;

public class TestRecorridos {

    public static void main(String[] args) {

        // --- 1. CREACIÓN DE NODOS (Personas) ---
        Persona p1 = new Persona("123", "Nico", "Perez", 30);
        Persona p2 = new Persona("456", "Carolina", "Gomez", 25);
        Persona p3 = new Persona("789", "Luis", "Vega", 40);
        Persona p4 = new Persona("101", "Lucia", "Sol", 22);

        
        // --- 2. PRUEBAS EN GRAFO DIRIGIDO (para Dijkstra y A*) ---
        System.out.println("=================================================");
        System.out.println("  PRUEBAS CON GRAFO DIRIGIDO");
        System.out.println("=================================================");
        Grafo<Persona> gDirigido = new Grafo<>(true); // true = Dirigido
        gDirigido.agregarNodo(p1);
        gDirigido.agregarNodo(p2);
        gDirigido.agregarNodo(p3);
        gDirigido.agregarNodo(p4);

        gDirigido.agregarArista(p1, p2, 5);  // Juan  -> Ana   (5)
        gDirigido.agregarArista(p1, p3, 10); // Juan  -> Luis  (10)
        gDirigido.agregarArista(p2, p4, 7);  // Ana   -> Maria (7)
        gDirigido.agregarArista(p3, p4, 2);  // Luis  -> Maria (2)
        
        // uso de getter de nodos
        Map<Persona, INodoGrafo<Persona>> nodosDirigidos = gDirigido.getNodos();
        
        System.out.println("\n--- Ejecutando Dijkstra (desde Juan) ---");
        Dijkstra.ejecutar(gDirigido, p1, nodosDirigidos);
       
        System.out.println("\n--- Ejecutando A* (de Juan a Maria) ---");
        
        AEstrella.Heuristica<Persona> heuristicaCero = (nodoActual, nodoDestino) -> {
            return 0; // Heurística simple: no estima nada.
        };
        AEstrella.ejecutar(gDirigido, p1, p4, heuristicaCero, nodosDirigidos);

        // --- 3. PRUEBAS EN GRAFO NO DIRIGIDO (para Prim y Floyd) ---
        System.out.println("=================================================");
        System.out.println("  PRUEBAS CON GRAFO NO DIRIGIDO");
        System.out.println("=================================================");
        Grafo<Persona> gNoDirigido = new Grafo<>(false); // false = No Dirigido
        gNoDirigido.agregarNodo(p1);
        gNoDirigido.agregarNodo(p2);
        gNoDirigido.agregarNodo(p3);
        gNoDirigido.agregarNodo(p4);

        gNoDirigido.agregarArista(p1, p2, 5);  // Juan <-> Ana   (5)
        gNoDirigido.agregarArista(p1, p3, 10); // Juan <-> Luis  (10)
        gNoDirigido.agregarArista(p2, p4, 7);  // Ana  <-> Maria (7)
        gNoDirigido.agregarArista(p3, p4, 2);  // Luis <-> Maria (2)
        gNoDirigido.agregarArista(p2, p3, 1);  // Ana  <-> Luis  (1)

        // Usamos el getter de nuevo
        Map<Persona, INodoGrafo<Persona>> nodosNoDirigidos = gNoDirigido.getNodos();

        // --- Prueba Prim (Árbol de Expansión Mínima) ---
        System.out.println("\n--- Ejecutando Prim (MST) ---");
        Prim.ejecutar(gNoDirigido, nodosNoDirigidos);

        // --- Prueba Kruskal (Árbol de Expansión Mínima) ---
        System.out.println("\n--- Ejecutando Kruskal (MST) ---");
        Kruskal.ejecutar(gNoDirigido, nodosNoDirigidos);

        // --- Prueba Floyd-Warshall (Todos los Pares) ---
        System.out.println("\n--- Ejecutando Floyd-Warshall (Todos los pares) ---");
        FloydWarshall.ejecutar(gNoDirigido, nodosNoDirigidos);
    }
}