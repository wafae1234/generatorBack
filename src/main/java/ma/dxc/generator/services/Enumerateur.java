package ma.dxc.generator.services;

public enum Enumerateur {
	MYSQL("jdbc:mysql:"),
	POSTGRESQL("jdbc:postgresql:");

	private String prefix = "";

	private Enumerateur(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}


}
