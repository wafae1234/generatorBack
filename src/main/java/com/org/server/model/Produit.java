package com.org.server.model;

import java.util.*;
import javax.persistence.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@Entity
@Table(name="produit")
public class Produit {
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.MERGE)
	@JoinColumn
	private Categorie categorie;

	@OneToMany(targetEntity=com.org.server.model.Commande.class, mappedBy="produit", cascade=CascadeType.MERGE)
	private Set<Commande> commandes = new HashSet<Commande>();

	@Basic
	@Column(name="date_entre")
	private Date dateEntre;

	@Basic
	@Column(name="dure_vie")
	private String dureVie;

	@Basic
	private String etat;

	@Id
	@Column(name="id_produit")
	private long idProduit;

	@OneToMany(targetEntity=com.org.server.model.Lot.class, mappedBy="produit", cascade=CascadeType.MERGE)
	private Set<Lot> lots = new HashSet<Lot>();

	@Basic
	private String nom;

	@Basic
	@Column(name="Prix_achat")
	private Long prixAchat;

	@Basic
	@Column(name="prix_total_fct")
	private Long prixTotalFct;

	@Basic
	@Column(name="Prix_vente")
	private Long prixVente;

	@Basic
	@Column(name="seuil_max")
	private Long seuilMax;

	@Basic
	@Column(name="seuil_min")
	private Long seuilMin;

	@Basic
	private Long total;


	public Produit() {
	}

	public Produit(long idProduit) {
		this.idProduit = idProduit;
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

	public long getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(long idProduit) {
		this.idProduit = idProduit;
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