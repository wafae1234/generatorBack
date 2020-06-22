package ma.dxc.model;

public class GeneratorForm {
	
	private String diroctoryproject;
	private String nomprojet;
	private String nompackage;
	private String port;
	private String typebasededonne;
	private String lienserveur;
	private String nombd;
	private String nomutilisateur;
	private String motdepasseutilisateur;

	
	public String getDiroctoryproject() {
		return diroctoryproject;
	}
	public void setDiroctoryproject(String diroctoryproject) {
		this.diroctoryproject = diroctoryproject;
	}
	public String getNombd() {
		return nombd;
	}
	public void setNombd(String nombd) {
		this.nombd = nombd;
	}
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
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
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
	
	public GeneratorForm() {
		super();
	}
	public GeneratorForm(String nomprojet, String nompackage,String port, String typebasededonne, String lienserveur, String nombd,
			String nomutilisateur, String motdepasseutilisateur) {
		super();
		this.nomprojet = nomprojet;
		this.nompackage = nompackage;
		this.port=port;
		this.typebasededonne = typebasededonne;
		this.lienserveur = lienserveur;
		this.nombd = nombd;
		this.nomutilisateur = nomutilisateur;
		this.motdepasseutilisateur = motdepasseutilisateur;
	}
	
	
	
}