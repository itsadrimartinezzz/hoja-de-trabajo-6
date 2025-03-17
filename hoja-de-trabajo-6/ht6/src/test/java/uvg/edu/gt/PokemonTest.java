package uvg.edu.gt;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;

class PokemonTest {

    @BeforeEach
    void setUp() throws IOException {
        // Inicializar el mapa antes de cargar datos para evitar NullPointerException
        PokemonApp.initializePokemonMapForTesting();
        PokemonApp.loadPokemonData("pokemon_data_pokeapi.csv");
    }

    @Test
    void testAddPokemonToCollection() {
        // Primera vez que se agrega "Bulbasaur", debe ser exitoso (true)
        assertTrue(PokemonApp.addPokemonToCollection("Bulbasaur"),
                "No se pudo agregar Bulbasaur la primera vez.");

        // Segunda vez que se intenta agregar "Bulbasaur", debe fallar (false)
        assertFalse(PokemonApp.addPokemonToCollection("Bulbasaur"),
                "Bulbasaur debería ya estar en la colección del usuario.");

        // Verificar que "Bulbasaur" esté efectivamente en la colección del usuario
        assertTrue(PokemonApp.getUserCollection().stream()
                        .anyMatch(p -> p.getName().equalsIgnoreCase("Bulbasaur")),
                "Bulbasaur no está en la colección del usuario después de agregarlo.");

        // Intentar agregar un Pokémon inexistente como "FakePokemon", debe fallar (false)
        assertFalse(PokemonApp.addPokemonToCollection("FakePokemon"),
                "FakePokemon no debería poder ser agregado, ya que no existe en los datos.");

        // Agregar "Pikachu" a la colección, debe ser exitoso (true)
        assertTrue(PokemonApp.addPokemonToCollection("Pikachu"),
                "No se pudo agregar Pikachu a la colección del usuario.");

        // Verificar que "Pikachu" esté efectivamente en la colección del usuario
        assertTrue(PokemonApp.getUserCollection().stream()
                        .anyMatch(p -> p.getName().equalsIgnoreCase("Pikachu")),
                "Pikachu no está en la colección del usuario después de agregarlo.");
    }




    @Test
    void testSearchPokemonByAbility() {
        // Buscar Pokémon por la habilidad "Chlorophyll"
        List<String> result = PokemonApp.searchPokemonByAbility("Chlorophyll");

        // Se espera que al menos un Pokémon tenga esta habilidad
        assertFalse(result.isEmpty());

        // Bulbasaur debería estar en la lista si los datos del CSV son correctos
        assertTrue(result.contains("Bulbasaur"));
    }
}
