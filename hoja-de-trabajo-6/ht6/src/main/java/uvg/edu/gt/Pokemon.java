package uvg.edu.gt;

import java.util.List;

/**
 * Clase que representa un Pokémon con sus atributos principales.
 */
public class Pokemon {
    // Nombre del Pokémon
    private String name;

    // Número en la Pokédex Nacional
    private int pokedexNumber;

    // Tipo primario del Pokémon (Ejemplo: Agua, Fuego, Planta)
    private String type1;

    // Tipo secundario del Pokémon, si lo tiene (Ejemplo: Volador, Hielo). Puede estar vacío.
    private String type2;

    // Clasificación del Pokémon (Ejemplo: "Seed Pokémon" para Bulbasaur)
    private String classification;

    // Altura del Pokémon en metros
    private double height;

    // Peso del Pokémon en kilogramos
    private double weight;

    // Lista de habilidades que puede tener el Pokémon (Ejemplo: "Overgrow", "Chlorophyll")
    private List<String> abilities;

    // Número de generación en la que apareció por primera vez este Pokémon
    private int generation;

    // Indica si el Pokémon es legendario o no (true si es legendario, false si no lo es)
    private boolean isLegendary;

    /**
     * Constructor de la clase `Pokemon`, el cual recibe los datos que describen a un Pokémon.
     *
     * @param name           Nombre del Pokémon
     * @param pokedexNumber  Número de la Pokédex
     * @param type1          Tipo primario del Pokémon
     * @param type2          Tipo secundario del Pokémon (si tiene)
     * @param classification Clasificación según su especie
     * @param height         Altura en metros
     * @param weight         Peso en kilogramos
     * @param abilities      Lista de habilidades del Pokémon
     * @param generation     Generación a la que pertenece el Pokémon
     * @param isLegendary    Indica si el Pokémon es legendario
     */
    public Pokemon(String name, int pokedexNumber, String type1, String type2, String classification,
                   double height, double weight, List<String> abilities, int generation, boolean isLegendary) {
        this.name = name;
        this.pokedexNumber = pokedexNumber;
        this.type1 = type1;
        this.type2 = type2;
        this.classification = classification;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
        this.generation = generation;
        this.isLegendary = isLegendary;
    }

    // Métodos Getters para acceder a los atributos privados

    /**
     * Retorna el nombre del Pokémon.
     * @return Nombre del Pokémon
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna el tipo primario del Pokémon.
     * @return Tipo primario del Pokémon
     */
    public String getType1() {
        return type1;
    }

    /**
     * Retorna la lista de habilidades del Pokémon.
     * @return Lista de habilidades
     */
    public List<String> getAbilities() {
        return abilities;
    }

    /**
     * Retorna una representación en cadena del objeto Pokémon con todos sus datos.
     * Se utiliza para imprimir los detalles completos de un Pokémon.
     * @return Cadena con la información detallada del Pokémon
     */
    @Override
    public String toString() {
        return String.format("Name: %s, Pokedex: %d, Type1: %s, Type2: %s, Class: %s, Height: %.2f m, Weight: %.2f kg, Abilities: %s, Gen: %d, Legendary: %b",
                name, pokedexNumber, type1, type2, classification, height, weight, abilities, generation, isLegendary);
    }
}
