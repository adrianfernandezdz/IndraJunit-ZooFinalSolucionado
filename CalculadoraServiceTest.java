package Taller7.Zoo.tests;

import Taller7.Zoo.services.CalculadoraGastos;
import Taller7.Zoo.services.ZooService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraServiceTest {

    private CalculadoraGastos calculadoraGastos = new CalculadoraGastos();

    @ParameterizedTest(name = "Test {index}: \"{0}\" testeando ganancias {2} para ingresos {1}")
    @CsvSource({
            "Lunes, 1500, 500",
            "Martes, 500, 0",
            "Miércoles, 3500, 500",
            "Jueves, 800, 0",
            "Viernes, 4500, 500",
            "Sábado, 4500, 0",
            "Domingo, 6000, 1000"
    })
    void testCalcularGananciasOPerdidasCsvSource(String dia, double ingresosDia, double expected) {
        double resultado = calculadoraGastos.calcularGananciasOPerdidas(dia, ingresosDia);
        assertEquals(expected, resultado, "El resultado no es el esperado para el día " + dia);
    }
}
