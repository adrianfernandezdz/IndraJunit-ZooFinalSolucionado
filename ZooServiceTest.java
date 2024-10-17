package Taller7.Zoo.tests;

import Taller7.Zoo.repositories.ZooRepository;
import Taller7.Zoo.services.CalculadoraGastos;
import Taller7.Zoo.services.ComidaService;
import Taller7.Zoo.services.LimpiezaService;
import Taller7.Zoo.services.ZooService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ZooServiceTest {

    @Mock
    private LimpiezaService limpiezaService;

    @Mock
    private ComidaService comidaService;

    @Mock
    private ZooRepository zooRepository;

    @Mock
    private CalculadoraGastos calculadoraGastos;

    @InjectMocks
    private ZooService zooService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test del método abrirZoo con mocks y verifies
    @Test
    void testAbrirZooExito() throws Exception {
        when(limpiezaService.isZooLimpio()).thenReturn(true);
        when(comidaService.hayComida()).thenReturn(true);
        when(zooRepository.guardarEventoEnBdd(anyString())).thenReturn("EventoGuardado");

        boolean resultado = zooService.abrirZoo(150);

        assertTrue(resultado);
        verify(limpiezaService).isZooLimpio(); // Verifica que zooLimpio fue llamado
        verify(comidaService).hayComida(); // Verifica que hayComida fue llamado
        verify(zooRepository).guardarEventoEnBdd("AbrirZoo"); // Verifica el evento guardado en la BD
    }

    // Test del método abrirZoo con excepción por falta de comida
    @Test
    void testAbrirZooSinComida() throws Exception {
        when(limpiezaService.isZooLimpio()).thenReturn(true);
        when(comidaService.hayComida()).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> zooService.abrirZoo(150));

        assertEquals("No hay comida y no se puede abrir", exception.getMessage());
    }

    // Test del método abrirZoo cuando el zoo está sucio
    @Test
    void testAbrirZooZooSucio() throws Exception {
        when(limpiezaService.isZooLimpio()).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> zooService.abrirZoo(150));

        assertEquals("El zoo está sucio y no se puede abrir", exception.getMessage());
    }

    // Test del método cerrarZoo
    @Test
    void testCerrarZooExito() throws Exception {
        boolean resultado = zooService.cerrarZoo(20);

        assertTrue(resultado);
    }

    // Test del método cerrarZoo con hora menor a 20
    @Test
    void testCerrarZooHoraTemprana() throws Exception {
        boolean resultado = zooService.cerrarZoo(18);

        assertFalse(resultado); // No debería cerrarse el zoo si la hora es menor a 20
    }

    // Test del método cerrarZoo con excepción si ya está cerrado
    @Test
    void testCerrarZooHoraTardia() {
        Exception exception = assertThrows(Exception.class, () -> zooService.cerrarZoo(21));
        assertEquals("El zoo ya debería estar cerrado", exception.getMessage());
    }

    // Test del método calcularGananciasDia usando ArgumentCaptor
    @Test
    void testCalcularGananciasDia() {
        // 90 adultos * 20 + 10 menores * 10 = 1800 + 100 = 1900
        int ingresosEsperados = 1900;

        // Configura la simulación para devolver 1000.0 cuando se llame al método con esos argumentos específicos
        when(calculadoraGastos.calcularGananciasOPerdidas("Lunes", ingresosEsperados)).thenReturn(1000.0);

        // Ejecuta el método que estamos probando
        double resultado = zooService.calcularGananciasDia("Lunes", 100, 10);

        // Verifica que el resultado sea 1000.0 como se configuró en el mock
        assertEquals(1000.0, resultado);

        ArgumentCaptor<String> captorDia = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> captorIngresos = ArgumentCaptor.forClass(Double.class);

        // Verifica que se llame al método calcularGananciasOPerdidas con los argumentos capturados
        verify(calculadoraGastos).calcularGananciasOPerdidas(captorDia.capture(), captorIngresos.capture());

        assertEquals("Lunes", captorDia.getValue());
        assertEquals(ingresosEsperados, captorIngresos.getValue());
    }
    // Test del método limpiarZoo con éxito
    @Test
    void testLimpiarZooExito() throws Exception {
        when(limpiezaService.limpiarZoo()).thenReturn(true);

        boolean resultado = zooService.limpiarZoo(21);

        assertTrue(resultado);
        verify(limpiezaService).limpiarZoo();
    }

    // Test del método limpiarZoo con excepción si el zoo está abierto
    @Test
    void testLimpiarZooFallaPorZooAbierto() {
        Exception exception = assertThrows(Exception.class, () -> zooService.limpiarZoo(19));
        assertEquals("No se puede limpiar al zoo porque está abierto", exception.getMessage());
    }

    // Test del método alimentarAnimales con éxito
    @Test
    void testAlimentarAnimalesExito() throws Exception {
        when(comidaService.alimentarAnimales(anyString())).thenReturn(true);

        boolean resultado = zooService.alimentarAnimales(21, "León");

        assertTrue(resultado);
        verify(comidaService).alimentarAnimales("León");
    }

    // Test del método alimentarAnimales con excepción si el zoo está abierto
    @Test
    void testAlimentarAnimalesZooAbierto() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> zooService.alimentarAnimales(15, "Tigre"));
        assertEquals("No se puede alimentar a los animales porque el zoo está abierto", exception.getMessage());
    }

}
