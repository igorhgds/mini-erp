package henrique.igor.mini_erp.model;

import java.math.BigDecimal;

public class Produto {
	private int codProd;
	private String descrProd;
	private BigDecimal vlrVenda;
	
	public Produto() {}
	
	public int getCodProd() {return this.codProd;}
	
	public String getDescProd() { return this.descrProd;}
	
	public BigDecimal getValor() {return this.vlrVenda;}
	
	public void setDescProd(String descProd) {this.descrProd = descProd;}
	
	public void setVlrVenda(BigDecimal valor) {
		if(valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O valor deve ser maior que zero");
		}
		this.vlrVenda = valor;
	}

	@Override
	public String toString() {
		return "Produto [codProd=" + codProd + ", descrProd=" + descrProd + ", vlrVenda=" + vlrVenda + "]";
	}
}
