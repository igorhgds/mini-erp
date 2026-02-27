package henrique.igor.mini_erp;

import java.sql.Connection;

import henrique.igor.mini_erp.jdbc.ConnectionFactory;

public class App 
{
    public static void main( String[] args ) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()){
			if(conn != null) {
				System.out.println("SUCESSO: Conexão realizado!");
				System.out.println("Driver: " + conn.getMetaData().getDriverName());
			}else {
				System.out.println("FALHA: Conexão veio nula.");
			}
		} catch (Exception ex) {
			System.err.println("ERRO CRÍTICO DE CONEXÃO");
			ex.printStackTrace();
		}
    }
}
