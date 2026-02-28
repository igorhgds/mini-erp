package henrique.igor.mini_erp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import henrique.igor.mini_erp.jdbc.ConnectionFactory;
import henrique.igor.mini_erp.model.Parceiro;

public class ParceiroDAO {
	
	public void salvar(Parceiro parceiro) {
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO TGFPAR (CODPARC, NOMEPARC, TIPPESSOA, ATIVO) ");
		sql.append(" VALUES (?, ?, ?, ?) ");
		
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql.toString())){
			
			//stmt.setObject(1, parceiro.getCodParc(), Types.INTEGER);
			stmt.setInt(1, parceiro.getCodParc());
			stmt.setString(2, parceiro.getNomeParc());
			stmt.setString(3, parceiro.getTipPessoa());
			stmt.setBoolean(4, parceiro.getAtivo());
			
			stmt.executeUpdate();
			System.out.println("Parceiro adicionado com sucesso!");
			
		} catch (SQLException ex) {
			System.err.println("Erro ao salvar parceiro " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public Parceiro buscarParceiro(Integer codParc) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CODPARC, NOMEPARC, TIPPESSOA, ATIVO ");
		sql.append(" FROM TGFPAR ");
		sql.append(" WHERE CODPARC = ? ");
		
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql.toString())){
			
			stmt.setInt(1, codParc);
			
			try (ResultSet rs = stmt.executeQuery()){
				if (rs.next()) {
					return new Parceiro(
					rs.getInt("CODPARC"),
					rs.getString("NOMEPARC"),
					rs.getString("TIPPESSOA"),
					rs.getBoolean("ATIVO"));
				}	
			}					
		} catch (SQLException e) {
			System.err.println("Erro ao buscar parceiro: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
