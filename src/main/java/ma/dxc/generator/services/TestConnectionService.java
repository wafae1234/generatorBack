package ma.dxc.generator.services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import ma.dxc.generator.model.TestConnection;

@Service
public class TestConnectionService {
	
	public String  teste(TestConnection t) {
		String r;
		String dbType = t.getTypebasededonne().toUpperCase();
		String username = t.getNomutilisateur();
		String password = t.getMotdepasseutilisateur();
		String prefix = "";
		
		if(dbType.equals(Enumerateur.MYSQL.name())) {
			prefix = Enumerateur.MYSQL.getPrefix();
		}	
		else if(dbType.equals(Enumerateur.POSTGRESQL.name())) {
			prefix = Enumerateur.POSTGRESQL.getPrefix();
		}		

		String url = prefix + "//"+t.getLienserveur()+"/"+t.getNombd();
		System.out.println("Connecting database...");

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
		   r=("Database connected!");
		   //r=("CC");
		   
		   System.out.println(r);
		} catch (SQLException e) {
		   r="Cannot connect the database!";
		   // r="NN";
		    System.out.println(r);
		}
		
		return r;
	}

}
