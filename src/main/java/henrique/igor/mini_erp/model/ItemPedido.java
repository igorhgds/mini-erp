package henrique.igor.mini_erp.model;

import java.math.BigDecimal;

public class ItemPedido {
	private Pedido nuNota;
	private int sequencia;
	private Produto produto;
	private BigDecimal qtdNeg;
	private BigDecimal vlrUnit;
	private BigDecimal vlrTot;
	
	public ItemPedido(){}

	public Pedido getNuNota() {return nuNota;}
	public void setNuNota(Pedido nota) {this.nuNota = nota;}

	public int getSequencia() {return sequencia;}
	public void setSequencia(int sequencia) {this.sequencia = sequencia;}

	public BigDecimal getQtdNeg() {return qtdNeg;}
	public void setQtdNeg(BigDecimal qtdNeg) {this.qtdNeg = qtdNeg;}

	public BigDecimal getVlrUnit() {return vlrUnit;}

	public void setVlrUnit(BigDecimal vlrUnit) {
		if(vlrUnit == null || vlrUnit.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O valor deve ser maior que zero");
		}
		this.vlrUnit = vlrUnit;
	}

	public Produto getProduto() {return produto;}
	public void setProduto(Produto produto) {this.produto = produto;}
	
	public BigDecimal getVlrTot() {return vlrTot;}
	
	public void calcularTotal() {
		this.vlrTot = this.qtdNeg.multiply(vlrUnit);
	}
}
