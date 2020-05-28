package com.org.server.model;

import java.util.*;
import javax.persistence.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@Entity
@Table(name="registre")
public class Registre {
	@Basic
	@Column(name="date_entre")
	private Date dateEntre;

	@Basic
	@Column(name="date_sortie")
	private Date dateSortie;

	@Id
	@Column(name="id_registre")
	private long idRegistre;

	@Basic
	@Column(name="Nom_user")
	private String nomUser;


	public Registre() {
	}

	public Registre(long idRegistre) {
		this.idRegistre = idRegistre;
	}

	public Date getDateEntre() {
		return dateEntre;
	}

	public void setDateEntre(Date dateEntre) {
		this.dateEntre = dateEntre;
	}

	public Date getDateSortie() {
		return dateSortie;
	}

	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
	}

	public long getIdRegistre() {
		return idRegistre;
	}

	public void setIdRegistre(long idRegistre) {
		this.idRegistre = idRegistre;
	}

	public String getNomUser() {
		return nomUser;
	}

	public void setNomUser(String nomUser) {
		this.nomUser = nomUser;
	}
}