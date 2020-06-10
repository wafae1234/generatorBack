package ma.dxc.generator.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfigurationImpl;
import org.apache.openjpa.jdbc.meta.ReverseMappingTool;
import org.apache.openjpa.lib.util.Options;

public class EntitiesGenerator {
	
	private String connectionURL;
	private String host;
	private String driverName;
	private String databaseName;
	private String userName;
	private String password;

	JDBCConfiguration result = new JDBCConfigurationImpl();
	
	public EntitiesGenerator(String host, String driverName, String databaseName, String userName,
			String password, String prefix) {
		super();
		this.host = host;
		this.driverName = driverName;
		this.databaseName = databaseName;
		this.userName = userName;
		this.password = password;	
		buildURL(prefix);
	}

	/**
	 * 
	 * 
	 * @param prefix
	 */
	private void buildURL(String prefix){

		if (prefix.equals(Enumerateur.POSTGRESQL.getPrefix())) {
			this.connectionURL = Enumerateur.POSTGRESQL.getPrefix() + "//" + host + "/" + this.databaseName;
		}
		
		else if(prefix.equals(Enumerateur.MYSQL.getPrefix())) {
			this.connectionURL = Enumerateur.MYSQL.getPrefix() + "//" + host + "/" + this.databaseName;
		}
	
		result.setConnectionURL(this.connectionURL); 
		result.setConnectionDriverName(this.driverName); 
		result.setConnectionUserName(this.userName); 
		result.setConnectionPassword(this.password);

	}

	public void generateEntities(String directory, String pakage, boolean annotation) {
		Options rmOpts = new Options(); 
		String argString = "-Log DefaultLevel=ERROR -metaDataFactory jpa() -metadata none"; 

		rmOpts.setProperty("directory", directory);
		rmOpts.setProperty("package", pakage);
		rmOpts.setProperty("annotations", annotation);
		rmOpts.setProperty("useGenericCollections", true);
		rmOpts.setProperty("primaryKeyOnJoin", true);
		rmOpts.setProperty("nullableAsObject", true);
		rmOpts.setFromCmdLine(argString.split(" "));

		try {
			
			ReverseMappingTool.run(result, new String[0], rmOpts);
			System.out.println("okeey");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			result.close();
        }
		
	}
	
}
