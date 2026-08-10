package carrinho;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import produto.Produto;
import produto.ProdutoNaoEncontradoException;

@DisplayName("Testes da classe Carrinho")
public class CarrinhoTest {

	/** Tolerancia usada nas comparacoes de double (dinheiro). */
	private static final double DELTA = 0.001;

	private Carrinho carrinho;
	private Produto livro;
	private Produto caneta;

	@BeforeEach
	public void inicializa() {
		carrinho = new Carrinho();
		livro = new Produto("Introducao ao Teste de Software", 100.00);
		caneta = new Produto("Caneta", 2.50);
	}

	// ------------------------------------------------------------------
	// Estado inicial
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Carrinho recem-criado")
	class CarrinhoVazioTest {

		@Test
		@DisplayName("Comeca vazio e com valor total zero")
		void carrinhoNovoEstaVazio() {
			assertAll("carrinho novo",
					() -> assertEquals(0, carrinho.getQtdeItems()),
					() -> assertEquals(0.0, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Remover de um carrinho vazio lanca ProdutoNaoEncontradoException")
		void removerDeCarrinhoVazioLancaExcecao() {
			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(livro));
		}

		@Test
		@DisplayName("Esvaziar um carrinho ja vazio nao causa erro")
		void esvaziarCarrinhoVazioNaoFalha() {
			assertDoesNotThrow(() -> carrinho.esvazia());
			assertEquals(0, carrinho.getQtdeItems());
		}
	}

	// ------------------------------------------------------------------
	// addItem
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo addItem(item)")
	class AddItemTest {

		@Test
		@DisplayName("Adicionar um item aumenta a quantidade e o valor total")
		void adicionaUmItem() {
			carrinho.addItem(livro);

			assertAll("apos adicionar 1 item",
					() -> assertEquals(1, carrinho.getQtdeItems()),
					() -> assertEquals(100.00, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Adicionar varios itens soma os precos")
		void adicionaVariosItens() {
			carrinho.addItem(livro);
			carrinho.addItem(caneta);

			assertAll("apos adicionar 2 itens",
					() -> assertEquals(2, carrinho.getQtdeItems()),
					() -> assertEquals(102.50, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("O mesmo produto pode ser adicionado mais de uma vez")
		void permiteItensDuplicados() {
			carrinho.addItem(livro);
			carrinho.addItem(livro);

			assertAll("item duplicado",
					() -> assertEquals(2, carrinho.getQtdeItems()),
					() -> assertEquals(200.00, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Produto com preco zero nao altera o valor total")
		void produtoGratuito() {
			carrinho.addItem(new Produto("Brinde", 0.0));

			assertAll("brinde",
					() -> assertEquals(1, carrinho.getQtdeItems()),
					() -> assertEquals(0.0, carrinho.getValorTotal(), DELTA));
		}
	}

	// ------------------------------------------------------------------
	// removeItem
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo removeItem(item)")
	class RemoveItemTest {

		@BeforeEach
		void preencheCarrinho() {
			carrinho.addItem(livro);
			carrinho.addItem(caneta);
		}

		@Test
		@DisplayName("Remove o item e atualiza quantidade e valor total")
		void removeItemExistente() throws ProdutoNaoEncontradoException {
			carrinho.removeItem(livro);

			assertAll("apos remover o livro",
					() -> assertEquals(1, carrinho.getQtdeItems()),
					() -> assertEquals(2.50, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Remove todos os itens um a um ate esvaziar")
		void removeTodosOsItens() throws ProdutoNaoEncontradoException {
			carrinho.removeItem(livro);
			carrinho.removeItem(caneta);

			assertAll("carrinho esvaziado por remocoes",
					() -> assertEquals(0, carrinho.getQtdeItems()),
					() -> assertEquals(0.0, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Remover produto inexistente lanca ProdutoNaoEncontradoException")
		void removeItemInexistenteLancaExcecao() {
			Produto naoAdicionado = new Produto("Mochila", 80.00);

			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(naoAdicionado));
		}

		@Test
		@DisplayName("Remocao falha nao altera o estado do carrinho")
		void remocaoFalhaNaoAlteraEstado() {
			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(new Produto("Mochila", 80.00)));

			assertAll("estado preservado",
					() -> assertEquals(2, carrinho.getQtdeItems()),
					() -> assertEquals(102.50, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Remover apenas uma ocorrencia quando o item esta duplicado")
		void removeApenasUmaOcorrencia() throws ProdutoNaoEncontradoException {
			carrinho.addItem(livro);

			carrinho.removeItem(livro);

			assertAll("apenas uma ocorrencia removida",
					() -> assertEquals(2, carrinho.getQtdeItems()),
					() -> assertEquals(102.50, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Remover null lanca ProdutoNaoEncontradoException")
		void removeNullLancaExcecao() {
			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(null));
		}
	}

	// ------------------------------------------------------------------
	// getValorTotal
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo getValorTotal()")
	class ValorTotalTest {

		@Test
		@DisplayName("Soma corretamente precos com casas decimais")
		void somaPrecosDecimais() {
			carrinho.addItem(new Produto("A", 19.99));
			carrinho.addItem(new Produto("B", 5.01));
			carrinho.addItem(new Produto("C", 0.50));

			assertEquals(25.50, carrinho.getValorTotal(), DELTA);
		}

		@Test
		@DisplayName("Aceita preco negativo reduzindo o total")
		void aceitaPrecoNegativo() {
			carrinho.addItem(livro);
			carrinho.addItem(new Produto("Desconto", -10.00));

			assertEquals(90.00, carrinho.getValorTotal(), DELTA);
		}

		@Test
		@DisplayName("Chamar getValorTotal duas vezes devolve o mesmo resultado")
		void naoTemEfeitoColateral() {
			carrinho.addItem(livro);
			carrinho.addItem(caneta);

			double primeira = carrinho.getValorTotal();
			double segunda = carrinho.getValorTotal();

			assertAll("sem efeito colateral",
					() -> assertEquals(primeira, segunda, DELTA),
					() -> assertEquals(2, carrinho.getQtdeItems()));
		}
	}

	// ------------------------------------------------------------------
	// esvazia
	// ------------------------------------------------------------------
	@Nested
	@DisplayName("Metodo esvazia()")
	class EsvaziaTest {

		@Test
		@DisplayName("Remove todos os itens de uma vez")
		void esvaziaCarrinhoComItens() {
			carrinho.addItem(livro);
			carrinho.addItem(caneta);

			carrinho.esvazia();

			assertAll("apos esvaziar",
					() -> assertEquals(0, carrinho.getQtdeItems()),
					() -> assertEquals(0.0, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Carrinho continua utilizavel depois de esvaziado")
		void carrinhoReutilizavelAposEsvaziar() {
			carrinho.addItem(livro);
			carrinho.esvazia();
			carrinho.addItem(caneta);

			assertAll("reutilizacao",
					() -> assertEquals(1, carrinho.getQtdeItems()),
					() -> assertEquals(2.50, carrinho.getValorTotal(), DELTA));
		}

		@Test
		@DisplayName("Apos esvaziar, remover qualquer item lanca excecao")
		void removerAposEsvaziarLancaExcecao() {
			carrinho.addItem(livro);
			carrinho.esvazia();

			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(livro));
		}
	}

	// ------------------------------------------------------------------
	// Cenario integrado
	// ------------------------------------------------------------------
	@Test
	@DisplayName("Cenario completo: adicionar, remover e esvaziar")
	void cenarioCompletoDeCompra() throws ProdutoNaoEncontradoException {
		carrinho.addItem(livro);
		carrinho.addItem(caneta);
		carrinho.addItem(new Produto("Mochila", 80.00));

		assertEquals(182.50, carrinho.getValorTotal(), DELTA);

		carrinho.removeItem(caneta);

		assertAll("apos remover a caneta",
				() -> assertEquals(2, carrinho.getQtdeItems()),
				() -> assertEquals(180.00, carrinho.getValorTotal(), DELTA));

		carrinho.esvazia();

		assertAll("apos esvaziar",
				() -> assertEquals(0, carrinho.getQtdeItems()),
				() -> assertEquals(0.0, carrinho.getValorTotal(), DELTA));
	}
}