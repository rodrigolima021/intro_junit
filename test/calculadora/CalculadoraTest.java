package calculadora;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Testes da classe Calculadora")
public class CalculadoraTest {

	private Calculadora calc;

	@BeforeEach
	public void inicializa() {
		calc = new Calculadora();
	}

	// ------------------------------------------------------------------
	// soma
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo soma(a, b)")
	class SomaTest {

		@Test
		@DisplayName("Soma dois numeros positivos")
		void somaDoisPositivos() {
			assertEquals(9, calc.soma(4, 5));
		}

		@Test
		@DisplayName("Soma dois numeros negativos")
		void somaDoisNegativos() {
			assertEquals(-5, calc.soma(-2, -3));
		}

		@Test
		@DisplayName("Soma com zero devolve o proprio numero (elemento neutro)")
		void somaComZero() {
			assertEquals(7, calc.soma(7, 0));
		}

		@Test
		@DisplayName("A soma e comutativa")
		void somaEhComutativa() {
			assertEquals(calc.soma(3, 8), calc.soma(8, 3));
		}

		@ParameterizedTest(name = "soma({0}, {1}) deve ser {2}")
		@CsvSource({ "1, 1, 2", "0, 0, 0", "-4, 4, 0", "100, 250, 350", "-7, 3, -4" })
		void somaParametrizada(int a, int b, int esperado) {
			assertEquals(esperado, calc.soma(a, b));
		}

		@Test
		@DisplayName("Overflow: MAX_VALUE + 1 volta para MIN_VALUE (comportamento do int)")
		void somaComOverflow() {
			assertEquals(Integer.MIN_VALUE, calc.soma(Integer.MAX_VALUE, 1));
		}
	}

	// ------------------------------------------------------------------
	// subtracao
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo subtracao(a, b)")
	class SubtracaoTest {

		@Test
		@DisplayName("Resultado positivo quando a > b")
		void resultadoPositivo() {
			assertEquals(6, calc.subtracao(10, 4));
		}

		@Test
		@DisplayName("Resultado negativo quando a < b")
		void resultadoNegativo() {
			assertEquals(-6, calc.subtracao(4, 10));
		}

		@Test
		@DisplayName("Numeros iguais resultam em zero")
		void numerosIguais() {
			assertEquals(0, calc.subtracao(5, 5));
		}

		@ParameterizedTest(name = "subtracao({0}, {1}) deve ser {2}")
		@CsvSource({ "10, 3, 7", "0, 5, -5", "-5, -5, 0", "-2, 3, -5" })
		void subtracaoParametrizada(int a, int b, int esperado) {
			assertEquals(esperado, calc.subtracao(a, b));
		}
	}

	// ------------------------------------------------------------------
	// multiplicacao
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo multiplicacao(a, b)")
	class MultiplicacaoTest {

		@Test
		@DisplayName("Multiplica dois positivos")
		void doisPositivos() {
			assertEquals(12, calc.multiplicacao(3, 4));
		}

		@Test
		@DisplayName("Sinais diferentes produzem resultado negativo")
		void sinaisDiferentes() {
			assertEquals(-12, calc.multiplicacao(-3, 4));
		}

		@Test
		@DisplayName("Dois negativos produzem resultado positivo")
		void doisNegativos() {
			assertEquals(12, calc.multiplicacao(-3, -4));
		}

		@Test
		@DisplayName("Multiplicacao por zero e sempre zero")
		void multiplicacaoPorZero() {
			assertAll("zero absorvente",
					() -> assertEquals(0, calc.multiplicacao(0, 9)),
					() -> assertEquals(0, calc.multiplicacao(9, 0)),
					() -> assertEquals(0, calc.multiplicacao(0, 0)));
		}

		@Test
		@DisplayName("Multiplicacao por 1 devolve o proprio numero")
		void elementoNeutro() {
			assertEquals(42, calc.multiplicacao(42, 1));
		}
	}

	// ------------------------------------------------------------------
	// divisao
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo divisao(a, b)")
	class DivisaoTest {

		@Test
		@DisplayName("Divisao exata")
		void divisaoExata() {
			assertEquals(2, calc.divisao(8, 4));
		}

		@Test
		@DisplayName("Divisao inteira trunca a parte decimal (7/2 = 3)")
		void divisaoInteiraTrunca() {
			assertEquals(3, calc.divisao(7, 2));
		}

		@Test
		@DisplayName("Divisao com numerador negativo trunca em direcao a zero (-7/2 = -3)")
		void divisaoNegativaTruncaParaZero() {
			assertEquals(-3, calc.divisao(-7, 2));
		}

		@Test
		@DisplayName("Zero dividido por qualquer numero e zero")
		void zeroDivididoPorN() {
			assertEquals(0, calc.divisao(0, 5));
		}

		@Test
		@DisplayName("Divisao por zero lanca ArithmeticException")
		void divisaoPorZeroLancaExcecao() {
			ArithmeticException e = assertThrows(ArithmeticException.class,
					() -> calc.divisao(8, 0));
			assertEquals("/ by zero", e.getMessage());
		}

		@ParameterizedTest(name = "divisao({0}, {1}) deve ser {2}")
		@CsvSource({ "10, 2, 5", "9, 3, 3", "1, 2, 0", "-10, 5, -2", "10, -5, -2" })
		void divisaoParametrizada(int a, int b, int esperado) {
			assertEquals(esperado, calc.divisao(a, b));
		}
	}

	// ------------------------------------------------------------------
	// somatoria
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo somatoria(n)")
	class SomatoriaTest {

		@Test
		@DisplayName("somatoria(0) e zero (valor de fronteira)")
		void somatoriaDeZero() {
			assertEquals(0, calc.somatoria(0));
		}

		@Test
		@DisplayName("somatoria(1) e 1 (valor de fronteira)")
		void somatoriaDeUm() {
			assertEquals(1, calc.somatoria(1));
		}

		@Test
		@DisplayName("somatoria(5) = 1+2+3+4+5 = 15")
		void somatoriaDeCinco() {
			assertEquals(15, calc.somatoria(5));
		}

		@ParameterizedTest(name = "somatoria({0}) deve ser {1}")
		@CsvSource({ "2, 3", "3, 6", "10, 55", "100, 5050" })
		void somatoriaSegueFormulaDeGauss(int n, int esperado) {
			assertEquals(esperado, calc.somatoria(n));
			assertEquals(n * (n + 1) / 2, calc.somatoria(n));
		}

		@ParameterizedTest(name = "somatoria({0}) devolve 0")
		@ValueSource(ints = { -1, -5, -100 })
		@DisplayName("Para n negativo o laco nao executa e o retorno e 0")
		void somatoriaDeNegativoRetornaZero(int n) {
			assertEquals(0, calc.somatoria(n));
		}
	}

	// ------------------------------------------------------------------
	// ehPositivo
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo ehPositivo(n)")
	class EhPositivoTest {

		@ParameterizedTest(name = "ehPositivo({0}) deve ser true")
		@ValueSource(ints = { 1, 2, 1000, Integer.MAX_VALUE })
		void numerosPositivos(int n) {
			assertTrue(calc.ehPositivo(n));
		}

		@ParameterizedTest(name = "ehPositivo({0}) deve ser false")
		@ValueSource(ints = { -1, -2, -1000, Integer.MIN_VALUE })
		void numerosNegativos(int n) {
			assertFalse(calc.ehPositivo(n));
		}

		@Test
		@DisplayName("Fronteira: zero e tratado como positivo (n >= 0)")
		void zeroEhConsideradoPositivo() {
			assertTrue(calc.ehPositivo(0));
		}
	}

	// ------------------------------------------------------------------
	// compara
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo compara(a, b)")
	class ComparaTest {

		@Test
		@DisplayName("Devolve 0 quando a == b")
		void iguais() {
			assertEquals(0, calc.compara(2, 2));
		}

		@Test
		@DisplayName("Devolve 1 quando a > b")
		void primeiroMaior() {
			assertEquals(1, calc.compara(3, 2));
		}

		@Test
		@DisplayName("Devolve -1 quando a < b")
		void primeiroMenor() {
			assertEquals(-1, calc.compara(1, 2));
		}

		@ParameterizedTest(name = "compara({0}, {1}) deve ser {2}")
		@CsvSource({ "0, 0, 0", "-5, -5, 0", "0, -1, 1", "-1, 0, -1", "-3, -7, 1", "-7, -3, -1" })
		void comparaParametrizada(int a, int b, int esperado) {
			assertEquals(esperado, calc.compara(a, b));
		}

		@Test
		@DisplayName("Inverter os argumentos inverte o sinal do resultado")
		void resultadoEhAntissimetrico() {
			assertAll("antissimetria",
					() -> assertEquals(-calc.compara(5, 9), calc.compara(9, 5)),
					() -> assertEquals(-calc.compara(9, 5), calc.compara(5, 9)));
		}
	}
}

