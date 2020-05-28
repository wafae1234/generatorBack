package com.org.server.model;

import java.util.*;
import javax.persistence.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@Entity
@Table(name="facture")
public class Facture {
	@Basic
	private Date date;

	@Basic
	private String etat;

	@Basic
	private Long fournisseur;

	@Id
	@Column(name="id_facture")
	private long idFacture;

	@Basic
	@Column(name="prix_total_ttc")
	private Long prixTotalTtc;

	@Basic
	private Long produit;

	@Basic
	private Long quantite;


	public Facture() {
	}

	public Facture(long idFacture) {
		this.idFacture = idFacture;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Long getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(Long fournisseur) {
		this.fournisseur = fournisseur;
	}

	public long getIdFacture() {
		return idFacture;
	}

	public void setIdFacture(long idFacture) {
		this.idFacture = idFacture;
	}

	public Long getPrixTotalTtc() {
		return prixTotalTtc;
	}

	public void setPrixTotalTtc(Long prixTotalTtc) {
		this.prixTotalTtc = prixTotalTtc;
	}

	public Long getProduit() {
		return produit;
	}

	public void setProduit(Long produit) {
		this.produit = produit;
	}

	public Long getQuantite() {
		return quantite;
	}

	public void setQuantite(Long quantite) {
		this.quantite = quantite;
	}
}