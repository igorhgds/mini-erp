package henrique.igor.mini_erp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
