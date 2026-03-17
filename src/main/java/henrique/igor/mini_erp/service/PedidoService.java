package henrique.igor.mini_erp.service;

import java.math.BigDecimal;

import henrique.igor.mini_erp.dao.PedidoDAO;
import henrique.igor.mini_erp.exception.RegraNegocioException;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Pedido;

public class PedidoService {
	private static final String ATIVO = "S";
	
	private PedidoDAO dao;
	
	public PedidoService(PedidoDAO dao) {
		this.dao = dao;
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
				
		dao.salvar(pedido);
	}

}
