package henrique.igor.mini_erp;

import henrique.igor.mini_erp.dao.PedidoDAO;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Pedido;

public class BuscaPedidoTest {
	public static void main(String[] args) {
        PedidoDAO dao = new PedidoDAO();
        
        Pedido pedido = dao.buscarPorId(1);
        
        if (pedido != null) {
            System.out.println("=== PEDIDO ENCONTRADO ===");
            System.out.println("ID (NuNota): " + pedido.getNuNota());
            System.out.println("Nº Nota: " + pedido.getNumNota());
            System.out.println("Valor Total: " + pedido.getVlrNota());
            System.out.println("ID do Parceiro: " + pedido.getParceiro().getCodParc());
            
            System.out.println("--- ITENS DO PEDIDO ---");
            for (ItemPedido item : pedido.getItens()) {
                System.out.println("Seq: " + item.getSequencia() + 
                                   " | Produto ID: " + item.getProduto().getCodProd() + 
                                   " | Qtd: " + item.getQtdNeg() + 
                                   " | Vlr. Unit: " + item.getVlrUnit() +
                                   " | Vlr. Tot: " + item.getVlrTot());
            }
        } else {
            System.out.println("Pedido não encontrado no banco de dados.");
        }
    }
}
