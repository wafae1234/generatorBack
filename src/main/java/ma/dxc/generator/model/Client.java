package ma.dxc.generator.model;

import javax.persistence.*;

/**
 * Auto-generated by:
 * org.apache.openjpa.jdbc.meta.ReverseMappingTool$AnnotatedCodeGenerator
 */
@Entity
@Table(name="client")
public class Client {
	@Id
	private long id;

	@Basic
	@Column(nullable=false, length=20)
	private String nomcli;

	@Basic
	@Column(nullable=false, length=20)
	private String ville;


	public Client() {
	}

	public Client(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomcli() {
		return nomcli;
	}

	public void setNomcli(String nomcli) {
		this.nomcli = nomcli;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}
}