package henrique.igor.mini_erp;

import java.math.BigDecimal;
import java.time.LocalDate;

import henrique.igor.mini_erp.dao.PedidoDAO;
import henrique.igor.mini_erp.dao.ProdutoDAO;
import henrique.igor.mini_erp.exception.RegraNegocioException;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Parceiro;
import henrique.igor.mini_erp.model.Pedido;
import henrique.igor.mini_erp.model.Produto;
import henrique.igor.mini_erp.service.PedidoService;

public class CriaPedidoTest {

	public static void main(String[] args) {
		System.out.println("--- INICIANDO SISTEMA ---");
		
		Parceiro parceiro = new Parceiro(1, "Igor (Fornecedor Teste)", "F", "S");
		
		Produto produto = new Produto(2, "Teclado QWERTY", new BigDecimal("199.00"));		
		
		Pedido pedido = new Pedido();
		pedido.setNumNota(1);
		pedido.setDtNeg(java.sql.Date.valueOf(LocalDate.now()));
		pedido.setParceiro(parceiro);
		pedido.setStatus("S");
		
		ItemPedido item1 = new ItemPedido();
		item1.setSequencia(1);
		item1.setProduto(produto);
		item1.setQtdNeg(new BigDecimal("5"));
		item1.setVlrUnit(produto.getVlrVenda());
		
		pedido.getItens().add(item1);
		pedido.calcularValorNota();
		
		System.out.println("--- DADOS MONTADOS NA MEMÓRIA. ENVIANDO PARA O SERVICE... ---");
		
		PedidoDAO pedidoDao = new PedidoDAO();
		ProdutoDAO produtoDao = new ProdutoDAO();
		PedidoService service = new PedidoService(pedidoDao, produtoDao);
		
		try {
			service.realizarPedido(pedido);
			System.out.println("SUCESSO: Pedido salvo no banco de dados com sucesso!");
		} catch (RegraNegocioException e) {
			System.err.println("ERRO DE VALIDAÇÃO: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("ERRO CRÍTICO NO SISTEMA: " + e.getMessage());
            e.printStackTrace();
		}
	}
}