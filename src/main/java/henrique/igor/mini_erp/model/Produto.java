package henrique.igor.mini_erp.model;

import java.math.BigDecimal;

public class Produto {
	private int codProd;
	private String descrProd;
	private BigDecimal vlrVenda;
	
	public Produto(int codProd, String descrProd, BigDecimal vlrVenda) {
		this.codProd = codProd;
		this.descrProd = descrProd;
		this.vlrVenda = vlrVenda;
	}
	
	public Produto() {}
	
	public int getCodProd() {return this.codProd;}
	public void setCodProd(int codProd) {this.codProd = codProd;}
	
	public String getDescProd() { return this.descrProd;}
	
	public BigDecimal getVlrVenda() {return this.vlrVenda;}
	
	public void setDescProd(String descProd) {this.descrProd = descProd;}
	
	public void setVlrVenda(BigDecimal vlrVenda) {
		if(vlrVenda == null || vlrVenda.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O valor deve ser maior que zero");
		}
		this.vlrVenda = vlrVenda;
	}

	@Override
	public String toString() {
		return "Produto [codProd=" + codProd + ", descrProd=" + descrProd + ", vlrVenda=" + vlrVenda + "]";
	}
}
