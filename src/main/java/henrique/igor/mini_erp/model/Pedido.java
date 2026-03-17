package henrique.igor.mini_erp.model;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Date;

public class Pedido {
	private int nuNota;
	private int numNota;
	private Date dtNeg;
	private Parceiro parceiro;
	private BigDecimal vlrNota;
	private String status;
	private List<ItemPedido> itens = new ArrayList<>();

	public Pedido() {}

	public int getNuNota() {return nuNota;}
	public void setNuNota(int nuNota) {this.nuNota = nuNota;}

	public int getNumNota() {return numNota;}
	public void setNumNota(int numNota) {this.numNota = numNota;}

	public Date getDtNeg() {return dtNeg;}
	public void setDtNeg(Date dtNeg) {this.dtNeg = dtNeg;}

	public BigDecimal getVlrNota() {return vlrNota;}

	public void setVlrNota(BigDecimal vlrNota) {
		if (vlrNota == null || vlrNota.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("O valor deve ser maior que zero");
		}
		this.vlrNota = vlrNota;
	}

	public String getStatus() {return status;}

	public void setStatus(String status) {this.status = status;}

	public List<ItemPedido> getItens() {return itens;}

	public void setItens(List<ItemPedido> itens) {this.itens = itens;}

	public Parceiro getParceiro() {return parceiro;}
	public void setParceiro(Parceiro parceiro) {this.parceiro = parceiro;}
}
