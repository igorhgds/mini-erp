package henrique.igor.mini_erp.model;

public class Parceiro {
	private int codParc;
	private String nomeParc;
	private String tipPessoa;
	private boolean ativo;
	
	public Parceiro(Integer codParc, String nomeParc, String tipPessoa, boolean ativo) {
		this.codParc = codParc;
		this.nomeParc = nomeParc;
		this.tipPessoa = tipPessoa;
		this.ativo = ativo;
	}
	
	public Parceiro() {}
	
	public Integer getCodParc() {return codParc;}

	public String getNomeParc() {return nomeParc;}

	public void setNomeParc(String nomeParc) {this.nomeParc = nomeParc;}

	public String getTipPessoa() {return tipPessoa;}

	public void setTipPessoa(String tipPessoa) {this.tipPessoa = tipPessoa;}

	public boolean getAtivo() {return ativo;}

	public void setAtivo(boolean ativo) {this.ativo = ativo;}

	@Override
	public String toString() {
		return "Parceiro [codParc=" + codParc + ", nomeParc=" + nomeParc + ", tipPessoa=" + tipPessoa + ", ativo="
				+ ativo + "]";
	}
}
