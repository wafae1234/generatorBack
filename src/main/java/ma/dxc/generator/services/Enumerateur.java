package ma.dxc.generator.services;

public enum Enumerateur {
	MYSQL("jdbc:mysql:","com.mysql.cj.jdbc.Driver"),
	POSTGRESQL("jdbc:postgresql:","org.postgresql.Driver");

	private String prefix = "";
	private String driverName = "";

	private Enumerateur(String prefix, String driverName) {
		this.prefix = prefix;
		this.driverName = driverName;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public String getDriverName() {
		return driverName;
	}


}
