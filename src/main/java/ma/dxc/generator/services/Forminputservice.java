package ma.dxc.generator.services;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import ma.dxc.generator.model.Forminput;
@Service
public class Forminputservice {
	String appName ;
	String packageName = "com.org.server";
	String directory = "C:\\Eclipse\\workspace\\GeneratedProject";
	String host = "localhost:3306"; 
	String driverName = "com.mysql.cj.jdbc.Driver"; 
	String username = "root"; 
	String databaseName = "stock"; 
	String password = ""; 
	String prefix = "jdbc:mysql:";
	String directoryEntity = BaseConstants.MAIN_SRC_DIR;
	String packageEntity = packageName+".model"; 
	String directoryDTO = BaseConstants.MAIN_SRC_DIR;
	String packageDTO = packageName+".dto";
	public void generate(Forminput f)
	{
		appName=f.getNomprojet().toString();
		packageName=f.getNompackage().toString();
		
		 System.out.println(f.getNompackage().toString()+"maroc");
		/*File projectDirectory = new File(directory);
		if (projectDirectory.mkdirs()) { 
            System.out.println("Directory is created"); 
        } 
        else { 
            System.out.println("Directory cannot be created"); 
        } 
		
		String repoUrl = "https://github.com/chaalidiae/ServerSideCrudDxc.git";
		String cloneDirectoryPath = projectDirectory.getPath(); 
		try {
		    System.out.println("Cloning "+repoUrl+" into "+cloneDirectoryPath);
		    Git.cloneRepository()
		        .setURI(repoUrl)
		        .setDirectory(Paths.get(cloneDirectoryPath).toFile())
		        .call();
		    System.out.println("Completed Cloning");
		} catch (GitAPIException e) {
		    System.out.println("Exception occurred while cloning repo");
		    e.printStackTrace();
		}
			
		
		
		EntitiesGenerator generator = new EntitiesGenerator(host, driverName, databaseName, username, password, prefix);
		
		generator.generateEntities(directoryEntity,packageEntity,true);
		generator.generateEntities(directoryDTO,packageDTO,false);
		
		RenameToDTO dto = new RenameToDTO();
		List<String> liens = dto.lister(directoryDTO);
		dto.renamFileName(liens);
	
		
	}*/
	
	}
		

}
