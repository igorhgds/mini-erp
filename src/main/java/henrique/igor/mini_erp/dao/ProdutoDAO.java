package henrique.igor.mini_erp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import henrique.igor.mini_erp.exception.RegraNegocioException;
import henrique.igor.mini_erp.jdbc.ConnectionFactory;
import henrique.igor.mini_erp.model.Produto;

public class ProdutoDAO {
	
	public void baixarEstoque(Connection conn, int codProd, BigDecimal quantidade) throws SQLException{
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE TGFPRO SET ESTOQUE = ESTOQUE - ? WHERE CODPROD = ?");		
		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())){
			
			stmt.setBigDecimal(1, quantidade);
			stmt.setInt(2, codProd);
			
			stmt.executeUpdate();
		}
	}
	
	public void salvar(Produto produto) {
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO TGFPRO (CODPROD, DESCRPROD, VLRVENDA, ESTOQUE) ");
		sql.append(" VALUES (SEQ_TGFPRO.NEXTVAL, ?, ?, ?) ");
		
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql.toString())){
			
			stmt.setString(1, produto.getDescrProd());
			stmt.setBigDecimal(2, produto.getVlrVenda());
			stmt.setBigDecimal(3, produto.getEstoque());
			
			stmt.executeUpdate();
			System.out.println("Produto inserido no banco com sucesso!");
			
		} catch (SQLException ex) {
			throw new RegraNegocioException("Erro fatal ao salvar produto: " + ex.getMessage());
		}
	}
	
	public Produto buscarProduto(int codProd) {
	    StringBuilder sql = new StringBuilder();
	    sql.append(" SELECT CODPROD, DESCRPROD, VLRVENDA, ESTOQUE ");
	    sql.append(" FROM TGFPRO ");
	    sql.append(" WHERE CODPROD = ? ");

	    try (Connection conn = ConnectionFactory.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

	        stmt.setInt(1, codProd);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                Produto p = new Produto();
	                p.setCodProd(rs.getInt("CODPROD"));
	                p.setDescrProd(rs.getString("DESCRPROD"));
	                p.setVlrVenda(rs.getBigDecimal("VLRVENDA"));
	                p.setEstoque(rs.getBigDecimal("ESTOQUE"));
	                return p;
	            }
	        }
	    } catch (SQLException e) {
	        throw new RegraNegocioException("Erro ao buscar produto ID " + codProd + ": " + e.getMessage());
	    }
	    return null;
	}
}
