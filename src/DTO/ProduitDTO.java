package DTO;

import java.util.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$ReverseCodeGenerator
 */
public class ProduitDTO {
	private Categorie categorie;

	private Set<Commande> commandes = new HashSet<Commande>();

	private Date dateEntre;

	private String dureVie;

	private String etat;

	private long idProduitDTO;

	private Set<Lot> lots = new HashSet<Lot>();

	private String nom;

	private Long prixAchat;

	private Long prixTotalFct;

	private Long prixVente;

	private Long seuilMax;

	private Long seuilMin;

	private Long total;


	public ProduitDTO() {
	}

	public ProduitDTO(long idProduitDTO) {
		this.idProduitDTO = idProduitDTO;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public Set<Commande> getCommandes() {
		return commandes;
	}

	public void setCommandes(Set<Commande> commandes) {
		this.commandes = commandes;
	}

	public Date getDateEntre() {
		return dateEntre;
	}

	public void setDateEntre(Date dateEntre) {
		this.dateEntre = dateEntre;
	}

	public String getDureVie() {
		return dureVie;
	}

	public void setDureVie(String dureVie) {
		this.dureVie = dureVie;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public long getIdProduitDTO() {
		return idProduitDTO;
	}

	public void setIdProduitDTO(long idProduitDTO) {
		this.idProduitDTO = idProduitDTO;
	}

	public Set<Lot> getLots() {
		return lots;
	}

	public void setLots(Set<Lot> lots) {
		this.lots = lots;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Long getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(Long prixAchat) {
		this.prixAchat = prixAchat;
	}

	public Long getPrixTotalFct() {
		return prixTotalFct;
	}

	public void setPrixTotalFct(Long prixTotalFct) {
		this.prixTotalFct = prixTotalFct;
	}

	public Long getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(Long prixVente) {
		this.prixVente = prixVente;
	}

	public Long getSeuilMax() {
		return seuilMax;
	}

	public void setSeuilMax(Long seuilMax) {
		this.seuilMax = seuilMax;
	}

	public Long getSeuilMin() {
		return seuilMin;
	}

	public void setSeuilMin(Long seuilMin) {
		this.seuilMin = seuilMin;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
