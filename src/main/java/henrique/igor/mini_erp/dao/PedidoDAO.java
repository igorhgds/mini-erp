package henrique.igor.mini_erp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import henrique.igor.mini_erp.jdbc.ConnectionFactory;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Parceiro;
import henrique.igor.mini_erp.model.Pedido;
import henrique.igor.mini_erp.model.Produto;

public class PedidoDAO {
	
	public void salvar(Connection conn, Pedido pedido) throws SQLException {
		
			StringBuilder queryCabecalho = new StringBuilder();
			queryCabecalho.append("INSERT INTO TGFCAB (NUNOTA, NUMNOTA, DTNEG, CODPARC, VLRNOTA ,STATUS) ");
			queryCabecalho.append(" VALUES (SEQ_TGFCAB.NEXTVAL, ?, ?, ?, ?, ?) ");
			
			StringBuilder queryItens = new StringBuilder();
			queryItens.append("INSERT INTO TGFITE (NUNOTA, SEQUENCIA, CODPROD, QTDNEG, VLRUNIT, VLRTOT) ");
			queryItens.append(" VALUES (?, ?, ?, ?, ?, ?) ");
			
			try (PreparedStatement cabecalho = conn.prepareStatement(queryCabecalho.toString(), new String[] {"NUNOTA"});
					PreparedStatement itensPedido = conn.prepareStatement(queryItens.toString())){
			
				//salvar cabeçalho(TGFCAB)
				cabecalho.setInt(1, pedido.getNumNota());
				cabecalho.setDate(2, pedido.getDtNeg());
				cabecalho.setObject(3, pedido.getParceiro().getCodParc());
				cabecalho.setBigDecimal(4, pedido.getVlrNota());
				cabecalho.setString(5, pedido.getStatus());
				
				cabecalho.executeUpdate();
				
				try(ResultSet rs = cabecalho.getGeneratedKeys()){	
					if(rs.next()) {
						int nuNotaGerado = rs.getInt(1);
						pedido.setNuNota(nuNotaGerado);
					}
				}
				
				//salvar itens(TGFITE)
				for(ItemPedido item : pedido.getItens()) {
					itensPedido.setInt(1, pedido.getNuNota());
					itensPedido.setInt(2, item.getSequencia());
					itensPedido.setInt(3, item.getProduto().getCodProd());
					itensPedido.setBigDecimal(4,  item.getQtdNeg());
					itensPedido.setBigDecimal(5, item.getVlrUnit());
					itensPedido.setBigDecimal(6, item.getVlrTot());
					
					itensPedido.addBatch();
				}
				itensPedido.executeBatch();
				}
		} 
	
	public Pedido buscarPorId(int nuNota) {
		Pedido pedidoEncontrado = null;
		
		String sqlCabecalho = "SELECT * FROM TGFCAB WHERE NUNOTA = ?";
		String sqlItens = "SELECT * FROM TGFITE WHERE NUNOTA = ?";
			
		try (Connection conn = ConnectionFactory.getInstance().getConnection()){
			
			try (PreparedStatement stmtCab = conn.prepareStatement(sqlCabecalho)){
				stmtCab.setInt(1, nuNota);
				
				try (ResultSet rsCab = stmtCab.executeQuery()) {
	                if (rsCab.next()) {
	                    pedidoEncontrado = new Pedido();
	                    
	                    pedidoEncontrado.setNuNota(rsCab.getInt("NUNOTA"));
	                    pedidoEncontrado.setNumNota(rsCab.getInt("NUMNOTA"));
	                    pedidoEncontrado.setDtNeg(rsCab.getDate("DTNEG"));
	                    //pedidoEncontrado.setVlrNota(rsCab.getBigDecimal("VLRNOTA"));
	                    pedidoEncontrado.setStatus(rsCab.getString("STATUS"));
	                    
	                    Parceiro parceiro = new Parceiro();
	                    parceiro.setCodParc(rsCab.getInt("CODPARC")); 
	                    pedidoEncontrado.setParceiro(parceiro);
	                }
				}
			}
			
			if (pedidoEncontrado != null) {
	            
	            try (PreparedStatement stmtIte = conn.prepareStatement(sqlItens)) {
	                stmtIte.setInt(1, nuNota);
	                
	                try (ResultSet rsIte = stmtIte.executeQuery()) {
	                    while (rsIte.next()) {
	                        ItemPedido item = new ItemPedido();
	                        item.setSequencia(rsIte.getInt("SEQUENCIA"));
	                        item.setQtdNeg(rsIte.getBigDecimal("QTDNEG"));
	                        item.setVlrUnit(rsIte.getBigDecimal("VLRUNIT"));

	                        Produto produto = new Produto();
	                        produto.setCodProd(rsIte.getInt("CODPROD"));
	                        item.setProduto(produto);

	                        pedidoEncontrado.getItens().add(item);
	                    }
	                }
	            }
	        }

	    } catch (SQLException e) {
	        System.err.println("Erro ao buscar pedido: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return pedidoEncontrado; 
	}
}

