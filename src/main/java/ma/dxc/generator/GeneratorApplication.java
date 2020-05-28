package ma.dxc.generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

 

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

 

import ma.dxc.generator.services.BaseConstants;
import ma.dxc.generator.services.EntitiesGenerator;
import ma.dxc.generator.services.RenameToDTO;


@SpringBootApplication
public class GeneratorApplication {
	 
	
	public static void main(String[] args) {
		
		//SpringApplication.run(GeneratorApplication.class, args);
		  
		
		String appName = "Crud";
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
		
		File projectDirectory = new File(directory);
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
	

		

        
		String pomPath = directory + "\\pom.xml";
		try {
			replaceFileString("Contact", appName, pomPath);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void replaceFileString(String oldString, String newString, String filePath) throws IOException {
	Path path = Paths.get(filePath);
	Charset charset = StandardCharsets.UTF_8;

	String content = new String(Files.readAllBytes(path), charset);
	content = content.replaceAll(oldString, newString);
	Files.write(path, content.getBytes(charset));
	System.out.println("Finding and replacing done");
	
	}
	}


