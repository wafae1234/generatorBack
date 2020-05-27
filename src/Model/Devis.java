package Model;

import java.util.*;
import javax.persistence.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@Entity
@Table(name="devis")
public class Devis {
	@Basic
	@Column(length=19)
	private Date date;

	@Basic
	@Column(name="id_commande")
	private Long idCommande;

	@Id
	@Column(name="id_devis")
	private long idDevis;

	@Basic
	@Column(name="id_produit")
	private Long idProduit;

	@Basic
	@Column(name="prix_total_devis")
	private Long prixTotalDevis;

	@Basic
	private Long quantite;


	public Devis() {
	}

	public Devis(long idDevis) {
		this.idDevis = idDevis;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getIdCommande() {
		return idCommande;
	}

	public void setIdCommande(Long idCommande) {
		this.idCommande = idCommande;
	}

	public long getIdDevis() {
		return idDevis;
	}

	public void setIdDevis(long idDevis) {
		this.idDevis = idDevis;
	}

	public Long getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(Long idProduit) {
		this.idProduit = idProduit;
	}

	public Long getPrixTotalDevis() {
		return prixTotalDevis;
	}

	public void setPrixTotalDevis(Long prixTotalDevis) {
		this.prixTotalDevis = prixTotalDevis;
	}

	public Long getQuantite() {
		return quantite;
	}

	public void setQuantite(Long quantite) {
		this.quantite = quantite;
	}
}