package henrique.igor.mini_erp.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import henrique.igor.mini_erp.dao.PedidoDAO;
import henrique.igor.mini_erp.dao.ProdutoDAO;
import henrique.igor.mini_erp.exception.RegraNegocioException;
import henrique.igor.mini_erp.jdbc.ConnectionFactory;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Pedido;

public class PedidoService {
	private static final String ATIVO = "S";

	private PedidoDAO pedidoDao;
	private ProdutoDAO produtoDao;

	public PedidoService(PedidoDAO pedido, ProdutoDAO produto) {
		this.pedidoDao = pedido;
		this.produtoDao = produto;
	}

	public void realizarPedido(Pedido pedido) {
		if (pedido == null || pedido.getItens() == null || pedido.getItens().isEmpty()) {
			throw new RegraNegocioException("O pedido não pode estar vazio!");
		}

		if (pedido.getParceiro() == null || !ATIVO.equals(pedido.getParceiro().getAtivo())) {
			throw new RegraNegocioException("Não é possível vender para parceiro inativo!");
		}

		BigDecimal soma = BigDecimal.ZERO;

		for (ItemPedido item : pedido.getItens()) {
			if (item == null || item.getVlrTot() == null) {
				throw new RegraNegocioException("Item inválido no pedido");
			}
			soma = soma.add(item.getVlrTot());
		}

		if (pedido.getVlrNota() == null || soma.compareTo(pedido.getVlrNota()) != 0) {
			throw new RegraNegocioException("A soma dos itens é diferente do valor da nota");
		}

		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			conn.setAutoCommit(false);

			try {
				pedidoDao.salvar(conn, pedido);

				for (ItemPedido it : pedido.getItens()) {
					produtoDao.baixarEstoque(conn, it.getProduto().getCodProd(), it.getQtdNeg());
				}

				conn.commit();

			} catch (SQLException e) {
				try {
			        if (conn != null) {
			            conn.rollback();
			            System.err.println("Rollback executado com sucesso.");
			        }
			    } catch (SQLException ex) {
			        System.err.println("Erro crítico ao tentar executar o rollback: " + ex.getMessage());
			    }
				throw new RuntimeException("Erro na transação de banco de dados. Rollback efetuado.", e);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao conectar no banco.", e);
		}
	}
}
