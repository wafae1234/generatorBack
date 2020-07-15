package ma.dxc.generator.model;

public class TestConnection {


	public TestConnection() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String lienserveur;
	private String nombd;
	private String typebasededonne;
	private String nomutilisateur;
	private String motdepasseutilisateur;
	
	
	
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
	public String getNombd() {
		return nombd;
	}
	public void setNombd(String nombd) {
		this.nombd = nombd;
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
	public TestConnection(String lienserveur, String nombd, String nomutilisateur, String motdepasseutilisateur,String typebasededonne) {
		super();
		this.lienserveur = lienserveur;
		this.nombd = nombd;
		this.nomutilisateur = nomutilisateur;
		this.motdepasseutilisateur = motdepasseutilisateur;
		this.typebasededonne = typebasededonne;
	}
	

}
