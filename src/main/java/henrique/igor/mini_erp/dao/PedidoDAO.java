package henrique.igor.mini_erp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import henrique.igor.mini_erp.jdbc.ConnectionFactory;
import henrique.igor.mini_erp.model.ItemPedido;
import henrique.igor.mini_erp.model.Pedido;

public class PedidoDAO {
	
	public void salvar(Pedido pedido) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()){
			conn.setAutoCommit(false);
			
			StringBuilder queryCabecalho = new StringBuilder();
			queryCabecalho.append("INSERT INTO TGFCAB (NUNOTA, NUMNOTA, DTNEG, CODPARC, VLRNOTA, STATUS) ");
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
				
				conn.commit();
			
				} catch (SQLException e) {
					conn.rollback();
					System.err.println("Transação falhou, efetuando rollback...");
					throw new RuntimeException("Erro ao salvar", e);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
