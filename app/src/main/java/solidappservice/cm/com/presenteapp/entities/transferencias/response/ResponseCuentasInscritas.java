package solidappservice.cm.com.presenteapp.entities.transferencias.response;

public class ResponseCuentasInscritas {
	
	private String aanumnit;
	private String nnasocia;
    private String k_cuenta;
    private String n_numcta;
    private long f_fecape;
    private String i_estado;
    private String k_idterc_tit;
    private String k_idterc;
    private String nnasocia_o;
    private String aanumnit_o;
    
	public String getAanumnit() {
		return aanumnit;
	}
	public String getNnasocia() {
		return nnasocia;
	}
	public String getK_cuenta() {
		return k_cuenta;
	}
	public String getN_numcta() {
		return n_numcta;
	}
	public long getF_fecape() {
		return f_fecape;
	}
	public String getI_estado() {
		return i_estado;
	}
	public String getK_idterc_tit() {
		return k_idterc_tit;
	}
	public String getK_idterc() {
		return k_idterc;
	}
	public String getNnasocia_o() {
		return nnasocia_o;
	}
	public String getAanumnit_o() {
		return aanumnit_o;
	}
	public void setAanumnit(String aanumnit) {
		this.aanumnit = aanumnit;
	}
	public void setNnasocia(String nnasocia) {
		this.nnasocia = nnasocia;
	}
	public void setK_cuenta(String k_cuenta) {
		this.k_cuenta = k_cuenta;
	}
	public void setN_numcta(String n_numcta) {
		this.n_numcta = n_numcta;
	}
	public void setF_fecape(long f_fecape) {
		this.f_fecape = f_fecape;
	}
	public void setI_estado(String i_estado) {
		this.i_estado = i_estado;
	}
	public void setK_idterc_tit(String k_idterc_tit) {
		this.k_idterc_tit = k_idterc_tit;
	}
	public void setK_idterc(String k_idterc) {
		this.k_idterc = k_idterc;
	}
	public void setNnasocia_o(String nnasocia_o) {
		this.nnasocia_o = nnasocia_o;
	}
	public void setAanumnit_o(String aanumnit_o) {
		this.aanumnit_o = aanumnit_o;
	}
	
	@Override
	public String toString() {
		return nnasocia + " ("+n_numcta+")";
	}
}
