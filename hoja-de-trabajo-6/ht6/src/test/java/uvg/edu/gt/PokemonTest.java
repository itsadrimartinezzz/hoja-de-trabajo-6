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
        // Primera vez que se agrega, debe ser exitoso (true)
        assertTrue(PokemonApp.addPokemonToCollection("Bulbasaur"));

        // Segunda vez que se intenta agregar el mismo, debe fallar (false)
        assertFalse(PokemonApp.addPokemonToCollection("Bulbasaur"));

        // Intentar agregar un Pokémon que no está en el dataset, debe fallar (false)
        assertFalse(PokemonApp.addPokemonToCollection("Pikachu")); // Asegúrate de que Pikachu no esté en el CSV
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
