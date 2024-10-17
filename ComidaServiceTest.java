package Taller7.Zoo.tests;

import java.util.stream.Stream;

import Taller7.Zoo.repositories.ComidaRepository;
import Taller7.Zoo.repositories.LimpiezaZooRepository;
import Taller7.Zoo.services.ComidaService;
import Taller7.Zoo.services.LimpiezaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class ComidaServiceTest {

    @Mock
    private ComidaRepository comidaRepository;

    @InjectMocks
    private ComidaService comidaService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    //test parametrizado para testear alimentarAnimales
    @ParameterizedTest(name = "Test {index}, probando test alimentar animal con \"{0}\" como palabra")
    @ValueSource(strings = {"Gorila", "Leon"})
    public void testAlimentarTipoAnimalPeligroso(String tipoAnimal) {
        boolean alimentarAnimal = comidaService.alimentarAnimales(tipoAnimal);
        verifyNoInteractions(comidaRepository);
        assertFalse(alimentarAnimal);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestData")
    public void testAlimentarAnimalesAnimalNoPeligroso(String testName, String tipoAnimal, boolean hayComida, boolean hayCuidadores, boolean resultadoEsperado) {
        when(comidaRepository.hayComida()).thenReturn(hayComida);
        when(comidaRepository.hayCuidadoresDisponibles()).thenReturn(hayCuidadores);
        boolean animalesAlimentados = comidaService.alimentarAnimales(tipoAnimal);
        assertEquals(animalesAlimentados, resultadoEsperado);
        verify(comidaRepository, times(1)).hayComida();
        verify(comidaRepository, times(1)).hayCuidadoresDisponibles();
    }


    // Método que provee los argumentos para el test
    static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("Test alimentar animales para Cabra, hay comida y hay cuidadores", "Cabra", true, true, true),
                Arguments.of("Test alimentar animales para Cabra, hay comida y no hay cuidadores", "Cabra", true, false, false),
                Arguments.of("Test alimentar animales para Cabra, no hay comida y no hay cuidadores", "Cabra", false, false, false)
        );
    }
}
