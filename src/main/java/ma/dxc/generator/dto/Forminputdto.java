package ma.dxc.generator.dto;

public class Forminputdto {
	private String nomprojet;
	private String nompackage;
	private String typebasededonne;
	private String lienserveur;
	private String portbd;
	private String nomutilisateur;
	private String motdepasseutilisateur;
	public String getNomprojet() {
		return nomprojet;
	}
	public void setNomprojet(String nomprojet) {
		this.nomprojet = nomprojet;
	}
	public String getNompackage() {
		return nompackage;
	}
	public void setNompackage(String nompackage) {
		this.nompackage = nompackage;
	}
	public String getTypebasededonne() {
		return typebasededonne;
	}
	public void setTypebasededonne(String typebasededonne) {
		this.typebasededonne = typebasededonne;
	}
	public String getLienserveur() {
		return lienserveur;
	}
	public void setLienserveur(String lienserveur) {
		this.lienserveur = lienserveur;
	}
	public String getPortbd() {
		return portbd;
	}
	public void setPortbd(String portbd) {
		this.portbd = portbd;
	}
	public String getNomutilisateur() {
		return nomutilisateur;
	}
	public void setNomutilisateur(String nomutilisateur) {
		this.nomutilisateur = nomutilisateur;
	}
	public String getMotdepasseutilisateur() {
		return motdepasseutilisateur;
	}
	public void setMotdepasseutilisateur(String motdepasseutilisateur) {
		this.motdepasseutilisateur = motdepasseutilisateur;
	}
	public Forminputdto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Forminputdto(String nomprojet, String nompackage, String typebasededonne, String lienserveur, String portbd,
			String nomutilisateur, String motdepasseutilisateur) {
		super();
		this.nomprojet = nomprojet;
		this.nompackage = nompackage;
		this.typebasededonne = typebasededonne;
		this.lienserveur = lienserveur;
		this.portbd = portbd;
		this.nomutilisateur = nomutilisateur;
		this.motdepasseutilisateur = motdepasseutilisateur;
	}
	
}
