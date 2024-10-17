package Taller7.Zoo.tests;

import java.util.stream.Stream;

import Taller7.Zoo.repositories.LimpiezaZooRepository;
import Taller7.Zoo.services.LimpiezaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LimpiezaServiceTest {

    @Mock
    private LimpiezaZooRepository limpiezaZooRepository;

    @InjectMocks
    private LimpiezaService limpiezaService;


    @BeforeEach
    public void setup(){
        limpiezaService.setZooLimpio(false);
    }

    //testear limpiarZoo cuando el zoo está limpio.
    @Test
    public void testLimpiarZooCuandoZooLimpio() throws Exception {
        limpiezaService.setZooLimpio(true);
        boolean zooLimpiado = limpiezaService.limpiarZoo();
        verifyNoInteractions(limpiezaZooRepository);
        assertFalse(zooLimpiado);
    }

    //test parametrizado para cuando el zoo esté sucio y haya que limpiarlo.
    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestData")
    public void testLimpiarZooCuandoZooSucio(String testName, boolean hayLimpiadores, boolean hayProductos, boolean resultadoEsperado) {
        assertFalse(limpiezaService.isZooLimpio());
        when(limpiezaZooRepository.hayProductosLimpieza()).thenReturn(hayProductos);
        // Cuando un mockeo no se vaya a llamar, podemos utilizar un lenient, pero no es recomendable porque enturbia muchos los tests.
        when(limpiezaZooRepository.hayLimpiadoresDisponibles()).thenReturn(hayLimpiadores);
        boolean zooLimpiado = limpiezaService.limpiarZoo();
        assertEquals(zooLimpiado, resultadoEsperado);
        verify(limpiezaZooRepository, times(1)).hayLimpiadoresDisponibles();
        verify(limpiezaZooRepository, times(1)).hayProductosLimpieza();
    }


    // Método que provee los argumentos para el test
    static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("Test Limpiar zoo sucio cuando hay limpiadores y productos", true, true, true),
                Arguments.of("Test Limpiar zoo sucio cuando hay limpiadores y no hay productos", true, false, false),
                Arguments.of("Test Limpiar zoo sucio cuando no hay limpiadores y no hay productos", false, false, false),
                Arguments.of("Test Limpiar zoo sucio cuando no hay limpiadores y hay productos", false, true, false)
        );
    }
}
