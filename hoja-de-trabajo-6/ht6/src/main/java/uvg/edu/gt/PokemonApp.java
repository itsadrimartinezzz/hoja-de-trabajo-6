package uvg.edu.gt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class PokemonApp {

    // Mapa de Pokémon
    private static Map<String, Pokemon> pokemonMap;

    // Colección de Pokémon del usuario
    private static List<Pokemon> userCollection = new ArrayList<>();

    // Getter público para userCollection (para pruebas unitarias)
    public static List<Pokemon> getUserCollection() {
        return userCollection;
    }

    // Inicializa el mapa para pruebas unitarias
    public static void initializePokemonMapForTesting() {
        pokemonMap = new HashMap<>();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione el tipo de Map: 1) HashMap 2) TreeMap 3) LinkedHashMap");
        int option = scanner.nextInt();
        scanner.nextLine();

        PokemonMapFactory factory;
        switch (option) {
            case 1 -> factory = new HashMapFactory();
            case 2 -> factory = new TreeMapFactory();
            case 3 -> factory = new LinkedHashMapFactory();
            default -> {
                System.out.println("Opción inválida, usando HashMap por defecto.");
                factory = new HashMapFactory();
            }
        }

        // Crear el mapa según la opción del usuario
        pokemonMap = factory.createMap();

        try {
            loadPokemonData("pokemon_data_pokeapi.csv");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        // Menú interactivo
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
            scanner.nextLine();

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

    // Carga los datos del archivo CSV
    public static void loadPokemonData(String fileName) throws IOException {
        InputStream inputStream = PokemonApp.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("Archivo CSV no encontrado en resources: " + fileName);
        }

        List<String> lines = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.toList());

        if (!lines.isEmpty()) {
            lines.remove(0); // Elimina encabezados
        }

        for (String line : lines) {
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            if (parts.length < 10) {
                System.out.println("Línea ignorada por formato incorrecto: " + line);
                continue;
            }

            try {
                String name = parts[0].trim();
                int pokedexNumber = Integer.parseInt(parts[1].trim());
                String type1 = parts[2].trim();
                String type2 = parts[3].trim();
                String classification = parts[4].trim();
                double height = Double.parseDouble(parts[5].trim());
                double weight = Double.parseDouble(parts[6].trim());
                String abilitiesRaw = parts[7].replaceAll("\"", "").trim();
                List<String> abilities = Arrays.asList(abilitiesRaw.split(", "));
                int generation = Integer.parseInt(parts[8].trim());
                boolean isLegendary = parts[9].trim().equalsIgnoreCase("Yes");

                pokemonMap.put(name, new Pokemon(name, pokedexNumber, type1, type2, classification, height, weight, abilities, generation, isLegendary));
            } catch (NumberFormatException e) {
                System.out.println("Error al procesar línea: " + line);
                System.out.println("Motivo: " + e.getMessage());
            }
        }

        System.out.println("Archivo CSV cargado correctamente. Se encontraron " + pokemonMap.size() + " Pokémon.");
    }

    // Agrega un Pokémon al mapa directamente (usado en pruebas unitarias)
    public static void addPokemonDirectly(Pokemon pokemon) {
        if (pokemonMap == null) {
            pokemonMap = new HashMap<>();
        }
        pokemonMap.put(pokemon.getName(), pokemon);
    }

    // Agrega un Pokémon a la colección del usuario
    public static boolean addPokemonToCollection(String name) {
        if (!pokemonMap.containsKey(name)) {
            System.out.println("Pokémon no encontrado: " + name);
            return false;
        }

        Pokemon pokemon = pokemonMap.get(name);

        if (userCollection.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name))) {
            System.out.println("Este Pokémon ya está en tu colección: " + name);
            return false;
        }

        userCollection.add(pokemon);
        System.out.println("Pokémon agregado a la colección: " + name);
        return true;
    }

    // Muestra los datos de un Pokémon
    public static void showPokemonData(String name) {
        System.out.println(pokemonMap.getOrDefault(name, new Pokemon("No encontrado", 0, "", "", "", 0, 0, List.of(), 0, false)));
    }

    // Muestra la colección del usuario ordenada por tipo1
    public static void showUserCollectionByType() {
        userCollection.stream()
                .sorted(Comparator.comparing(Pokemon::getType1))
                .forEach(p -> System.out.println(p.getName() + " - " + p.getType1()));
    }

    // Muestra todos los Pokémon ordenados por tipo1
    public static void showAllPokemonByType() {
        pokemonMap.values().stream()
                .sorted(Comparator.comparing(Pokemon::getType1))
                .forEach(p -> System.out.println(p.getName() + " - " + p.getType1()));
    }

    // Busca Pokémon por habilidad
    public static List<String> searchPokemonByAbility(String ability) {
        return pokemonMap.values().stream()
                .filter(p -> p.getAbilities().contains(ability))
                .map(Pokemon::getName)
                .toList();
    }
}

// Factories para seleccionar diferentes implementaciones de Map
interface PokemonMapFactory {
    Map<String, Pokemon> createMap();
}

class HashMapFactory implements PokemonMapFactory {
    public Map<String, Pokemon> createMap() { return new HashMap<>(); }
}

class TreeMapFactory implements PokemonMapFactory {
    public Map<String, Pokemon> createMap() { return new TreeMap<>(); }
}

class LinkedHashMapFactory implements PokemonMapFactory {
    public Map<String, Pokemon> createMap() { return new LinkedHashMap<>(); }
}
