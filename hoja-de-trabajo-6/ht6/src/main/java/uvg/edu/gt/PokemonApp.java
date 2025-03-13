package uvg.edu.gt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aplicación principal para manejar el almacenamiento y búsqueda de Pokémon.
 */
public class PokemonApp {
    // Mapa donde se almacenarán los Pokémon cargados desde el archivo CSV.
    private static Map<String, Pokemon> pokemonMap;

    /**
     * Método auxiliar para inicializar `pokemonMap` en pruebas unitarias.
     * En producción, esto lo hace el usuario al seleccionar el tipo de `Map`.
     */
    public static void initializePokemonMapForTesting() {
        pokemonMap = new HashMap<>();  // Se usa HashMap por defecto en pruebas
    }


    // Lista donde el usuario puede agregar los Pokémon que desee.
    private static List<Pokemon> userCollection = new ArrayList<>(); // Se usa ArrayList por su acceso rápido O(1)

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Se solicita al usuario que seleccione el tipo de Mapa que desea utilizar.
        System.out.println("Seleccione el tipo de Map: 1) HashMap 2) TreeMap 3) LinkedHashMap");
        int option = scanner.nextInt();
        scanner.nextLine(); // Se limpia el buffer

        // Se usa el patrón Factory para seleccionar el tipo de Mapa en tiempo de ejecución.
        PokemonMapFactory factory;
        switch (option) {
            case 1 -> factory = new HashMapFactory();  // HashMap no tiene un orden específico.
            case 2 -> factory = new TreeMapFactory();  // TreeMap ordena las claves de forma natural.
            case 3 -> factory = new LinkedHashMapFactory(); // LinkedHashMap mantiene el orden de inserción.
            default -> {
                System.out.println("Opción inválida, usando HashMap por defecto.");
                factory = new HashMapFactory();
            }
        }

        // Se crea el Mapa con la implementación seleccionada.
        pokemonMap = factory.createMap();

        // Se carga la información de los Pokémon desde el archivo CSV ubicado en /resources.
        try {
            loadPokemonData("pokemon_data_pokeapi.csv");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        // Se ejecuta el menú interactivo para el usuario.
        while (true) {
            System.out.println("\nMenú:");
            System.out.println("1. Agregar Pokémon a colección");
            System.out.println("2. Mostrar datos de un Pokémon");
            System.out.println("3. Mostrar colección ordenada por tipo1");
            System.out.println("4. Mostrar todos los Pokémon ordenados por tipo1");
            System.out.println("5. Buscar Pokémon por habilidad");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Se limpia el buffer

            // Se ejecuta la opción seleccionada por el usuario.
            switch (choice) {
                case 1 -> {
                    System.out.println("Ingrese el nombre del Pokémon a agregar:");
                    String name = scanner.nextLine();
                    addPokemonToCollection(name);
                }
                case 2 -> {
                    System.out.println("Ingrese el nombre del Pokémon:");
                    String name = scanner.nextLine();
                    showPokemonData(name);
                }
                case 3 -> showUserCollectionByType();
                case 4 -> showAllPokemonByType();
                case 5 -> {
                    System.out.println("Ingrese la habilidad:");
                    String ability = scanner.nextLine();
                    searchPokemonByAbility(ability).forEach(System.out::println);
                }
                case 6 -> {
                    System.out.println("Saliendo...");
                    return;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Carga los datos de los Pokémon desde un archivo CSV en la carpeta resources.
     * @param fileName Nombre del archivo CSV.
     * @throws IOException Si hay un problema al leer el archivo.
     */
    public static void loadPokemonData(String fileName) throws IOException {
        // Se obtiene el archivo CSV desde el classpath del proyecto.
        InputStream inputStream = PokemonApp.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("Archivo CSV no encontrado en resources: " + fileName);
        }

        // Se lee el archivo línea por línea y se almacena en una lista.
        List<String> lines = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.toList());

        // Se elimina la primera línea si contiene encabezados.
        if (!lines.isEmpty()) {
            lines.remove(0);
        }

        for (String line : lines) {
            // Se divide la línea en columnas respetando las comillas dobles en "Abilities".
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            // Se valida si la línea tiene la cantidad correcta de columnas.
            if (parts.length < 10) {
                System.out.println("Línea ignorada por formato incorrecto: " + line);
                continue;
            }

            try {
                // Se extraen y convierten los valores de cada columna.
                String name = parts[0].trim();
                int pokedexNumber = Integer.parseInt(parts[1].trim());
                String type1 = parts[2].trim();
                String type2 = parts[3].trim();
                String classification = parts[4].trim();
                double height = Double.parseDouble(parts[5].trim());
                double weight = Double.parseDouble(parts[6].trim());

                // Se manejan correctamente las habilidades eliminando comillas y separando valores.
                String abilitiesRaw = parts[7].replaceAll("\"", "").trim();
                List<String> abilities = Arrays.asList(abilitiesRaw.split(", "));

                int generation = Integer.parseInt(parts[8].trim());
                boolean isLegendary = parts[9].trim().equalsIgnoreCase("Yes");

                // Se almacena el Pokémon en el mapa con su nombre como clave.
                pokemonMap.put(name, new Pokemon(name, pokedexNumber, type1, type2, classification, height, weight, abilities, generation, isLegendary));

            } catch (NumberFormatException e) {
                System.out.println("Error al procesar línea: " + line);
                System.out.println("Motivo: " + e.getMessage());
            }
        }

        System.out.println("Archivo CSV cargado correctamente. Se encontraron " + pokemonMap.size() + " Pokémon.");
    }

    /** Agrega un Pokémon directamente al mapa (usado en pruebas unitarias). */
    public static void addPokemonDirectly(Pokemon pokemon) {
        if (pokemonMap == null) {
            pokemonMap = new HashMap<>();
        }
        pokemonMap.put(pokemon.getName(), pokemon);
    }

    /** Agrega un Pokémon a la colección del usuario.
    public static boolean addPokemonToCollection(String name) {
        if (!pokemonMap.containsKey(name)) return false;
        Pokemon pokemon = pokemonMap.get(name);
        if (userCollection.contains(pokemon)) return false;
        userCollection.add(pokemon);
        return true;
    }*/
    public static boolean addPokemonToCollection(String name) {
        if (!pokemonMap.containsKey(name)) {
            System.out.println("Pokémon no encontrado.");
            return false; // No se puede agregar si no existe en el dataset
        }

        Pokemon pokemon = pokemonMap.get(name);

        // Verificar si el Pokémon ya está en la colección
        if (userCollection.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name))) {
            System.out.println("Este Pokémon ya está en tu colección.");
            return false; // Retorna falso si el Pokémon ya existe
        }

        userCollection.add(pokemon);
        System.out.println("Pokémon agregado a la colección.");
        return true;
    }


    /** Muestra los datos de un Pokémon específico. */
    public static void showPokemonData(String name) {
        System.out.println(pokemonMap.getOrDefault(name, new Pokemon("No encontrado", 0, "", "", "", 0, 0, List.of(), 0, false)));
    }

    /** Muestra la colección del usuario ordenada por el tipo primario del Pokémon. */
    public static void showUserCollectionByType() {
        userCollection.stream()
                .sorted(Comparator.comparing(Pokemon::getType1))
                .forEach(p -> System.out.println(p.getName() + " - " + p.getType1()));
    }

    /** Muestra todos los Pokémon del dataset ordenados por tipo primario. */
    public static void showAllPokemonByType() {
        pokemonMap.values().stream()
                .sorted(Comparator.comparing(Pokemon::getType1))
                .forEach(p -> System.out.println(p.getName() + " - " + p.getType1()));
    }

    /** Busca Pokémon por habilidad y devuelve una lista con sus nombres. */
    public static List<String> searchPokemonByAbility(String ability) {
        return pokemonMap.values().stream()
                .filter(p -> p.getAbilities().contains(ability))
                .map(Pokemon::getName)
                .toList();
    }
}

/** Interfaz para el patrón Factory que permite seleccionar diferentes tipos de Map. */
interface PokemonMapFactory {
    Map<String, Pokemon> createMap();
}

/** Implementación de HashMap como Factory. */
class HashMapFactory implements PokemonMapFactory {
    public Map<String, Pokemon> createMap() { return new HashMap<>(); }
}

/** Implementación de TreeMap como Factory. */
class TreeMapFactory implements PokemonMapFactory {
    public Map<String, Pokemon> createMap() { return new TreeMap<>(); }
}

/** Implementación de LinkedHashMap como Factory. */
class LinkedHashMapFactory implements PokemonMapFactory {
    public Map<String, Pokemon> createMap() { return new LinkedHashMap<>(); }
}

