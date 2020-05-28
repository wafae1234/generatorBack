package ma.dxc.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ma.dxc.generator.services.BaseConstants;
import ma.dxc.generator.services.EntitiesGenerator;
import ma.dxc.generator.services.RenameToDTO;


@SpringBootApplication
public class GeneratorApplication {

	public static void main(String[] args) {
		
		///////////////////////////////////////////////////////////////////////////////////
		String newPackage ="com.cdg.business";
		
		String newPackagePath ="com\\cdg\\business\\";
		
		String newTestPackagePath ="com\\cdg\\business\\test\\";
		
		String NewProjectName = "Business";
		
		String orginPathProjectMain = "C:\\Users\\MB\\eclipse-workspace4\\GeneratedProject\\src\\main\\java\\";
		
		String orginPathProjectTest = "C:\\Users\\MB\\eclipse-workspace4\\GeneratedProject\\src\\test\\java\\";
		
		String originModel = orginPathProjectMain+"model";
		
		String originDto = orginPathProjectMain+"dto";
		
		String pathProject = orginPathProjectMain+"ma\\dxc\\";
		
		String originMa = orginPathProjectMain+"ma";
		
		String originTestMa = orginPathProjectTest+"ma";
		
		String pathTestProject = orginPathProjectTest+"ma\\dxc\\test\\";
		
		String newPathProject = orginPathProjectMain + newPackagePath;
		
		String newTestPathProject = orginPathProjectTest + newTestPackagePath;
		
		String newMainClassName = NewProjectName+"Application.java";
		
		String newTestClassName = NewProjectName+"ApplicationTests.java";
		
		String newMainClassPath = pathProject+newMainClassName;
		
		String newTestClassPath = pathTestProject+newTestClassName;
		
		String aspectPackage = newPathProject+"Aspect\\";
		
		String configPackage = newPathProject+"config\\";
		
		String dtoPackage = newPathProject+"dto\\";
		
		String modelPackage = newPathProject+"model\\";
		
		String orchestrationPackage = newPathProject+"orchestration\\";
		
		String repositoryPackage = newPathProject+"repository\\";
		
		String repositorySpecPackage = newPathProject+"repository\\specs\\";
		
		String restPackage = newPathProject+"rest\\";
		
		String securityPackage = newPathProject+"security\\";
		
		String servicePackage = newPathProject+"service\\";
		///////////////////////////////////////////////////////////////////////////////////
		
		String appName = "Crud";
		String packageName = "ma.dxc";
		String packagePath = "ma\\dxc";
		String directory = "C:\\Users\\MB\\eclipse-workspace4\\GeneratedProject\\";
		String host = "localhost:3306"; 
		String driverName = "com.mysql.cj.jdbc.Driver"; 
		String username = "root"; 
		String databaseName = "stock"; 
		String password = ""; 
		String prefix = "jdbc:mysql:";
		String directoryEntity = directory + BaseConstants.MAIN_SRC_DIR;
		String packageEntity = packageName+".model"; 
		String directoryDTO = directory + BaseConstants.MAIN_SRC_DIR;
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
		/////////////////////////////////////////////////////////////////////////////////////////////
		EntitiesGenerator generator = new EntitiesGenerator(
		host, driverName, databaseName+"?serverTimezone=UTC", username, password, prefix);
		
		generator.generateEntities(directoryEntity,"model",true);
		generator.generateEntities(directoryDTO,"dto",false);
		
		RenameToDTO dto = new RenameToDTO();
		List<String> liens = dto.lister(directoryDTO + ""+"/dto" );
		dto.renamFileName(liens);
		////////////////////////////////////////////////////////////////////////////////////////////
		
		// rename project main class
				renameProjectMainClass(pathProject,newMainClassName);
				
				// rename project Test class
				renameProjectTestClass(pathTestProject,newTestClassName);
				
				// replace old package with new package
				modifyFile(newMainClassPath, "ma.dxc", newPackage);
				
				modifyFile(newTestClassPath, "ma.dxc", newPackage);
				
				// replace old ClassName with new Class name
				modifyFile(newMainClassPath, "Contact", NewProjectName);
				
				modifyFile(newTestClassPath, "Contact", NewProjectName);
				
				moveProjectToNewRep(pathProject,newPathProject);
				
				moveProjectToNewRep(pathTestProject,newTestPathProject);
				
				List<String> classs =new ArrayList<String>();
				classs = ListNameOfFilesinDirectory(orginPathProjectMain+"model");
				createNewAspectClasses(classs,aspectPackage,newPackage);
				createNewOrchestrationClasses(classs,orchestrationPackage,newPackage);
				createNewRepositoryClasses(classs,repositoryPackage,newPackage);
				createNewSpecificationClasses(classs,repositorySpecPackage,newPackage);
				createNewRestServiceClasses(classs,restPackage,newPackage);
				createNewServiceClasses(classs,servicePackage,newPackage);
				renamePackagetoNewPackage(modelPackage,newPackage);
				renamePackagetoNewPackage(dtoPackage,newPackage);
				renamePackagetoNewPackage(configPackage,newPackage);
				renamePackagetoNewPackage(securityPackage,newPackage);
				
				renameModelPackagetoNewPackage(originModel,newPackage);
				renameDtoPackagetoNewPackage(originDto,newPackage);
				
				moveFilesToNewFolder(orginPathProjectMain+"model",modelPackage);
				
				moveFilesToNewFolder(orginPathProjectMain+"dto",dtoPackage);
				
				
				File directoryDto = new File(originDto);
				File directoryMa = new File(originMa);
				File directoryModel = new File(originModel);
				File directoryTestMa = new File(originTestMa);

		    	//make sure directory exists
		    	if(!directoryDto.exists() || !directoryMa.exists() || !directoryModel.exists() || !directoryTestMa.exists() ){

		           System.out.println("Directory does not exist.");
		           System.exit(0);

		        }else{

		           try{

		               delete(directoryDto);
		               delete(directoryMa);
		               delete(directoryModel);
		               delete(directoryTestMa);
		               

		           }catch(IOException e){
		               e.printStackTrace();
		               System.exit(0);
		           }
		        }

		    	System.out.println("Done");
				
			}
			
			static void renameProjectMainClass(String pathProject, String newMainClassName) {
				String newFileName = pathProject+newMainClassName;
				File oldfile =new File(pathProject+"ContactApplication.java");
				File newfile =new File(newFileName);

				if(oldfile.renameTo(newfile)){
					System.out.println("Rename succesful");
				}else{
					System.out.println("Rename failed");
				}
			}
			
			static void renameProjectTestClass(String pathProject, String newMainClassName) {
				String newFileName = pathProject+newMainClassName;
				File oldfile =new File(pathProject+"ContactApplicationTests.java");
				File newfile =new File(newFileName);

				if(oldfile.renameTo(newfile)){
					System.out.println("Rename succesful");
				}else{
					System.out.println("Rename failed");
				}
			}
			
			static void renameClass(String oldfilePath, String newfilePath) {
				File oldfile =new File(oldfilePath);
				File newfile =new File(newfilePath);

				if(oldfile.renameTo(newfile)){
					System.out.println("Rename succesful");
				}else{
					System.out.println("Rename failed");
				}
			}
			
			static void modifyFile(String filePath, String oldString, String newString)
		    {
		        File fileToBeModified = new File(filePath);
		        String oldContent = "";
		        BufferedReader reader = null;
		        FileWriter writer = null;
		        try
		        {
		            reader = new BufferedReader(new FileReader(fileToBeModified));
		            //Reading all the lines of input text file into oldContent
		            String line = reader.readLine();
		            while (line != null) 
		            {
		                oldContent = oldContent + line + System.lineSeparator();
		                line = reader.readLine();
		            }
		             
		            //Replacing oldString with newString in the oldContent
		            String newContent = oldContent.replaceAll(oldString, newString);
		             
		            //Rewriting the input text file with newContent
		            writer = new FileWriter(fileToBeModified);
		            writer.write(newContent);
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		        }
		        finally
		        {
		            try
		            {
		                //Closing the resources
		                 
		                reader.close();
		                writer.close();
		                System.out.println("done");
		            } 
		            catch (IOException e) 
		            {
		                e.printStackTrace();
		            }
		        }
		    }
			
			static void modifyFileToFixDTOPackage(String filePath, String oldString, String packageName, String importText)
		    {
		        File fileToBeModified = new File(filePath);
		        String oldContent = "";
		        BufferedReader reader = null;
		        FileWriter writer = null;
		        try
		        {
		            reader = new BufferedReader(new FileReader(fileToBeModified));
		            //Reading all the lines of input text file into oldContent
		            String line = reader.readLine();
		            while (line != null) 
		            {
		                oldContent = oldContent + line + System.lineSeparator();
		                line = reader.readLine();
		            }
		             
		            //chaking if oldString exists in the oldContent
		            boolean ifExists = oldContent.contains(oldString);
		            
		            String newContent = oldContent.replaceAll(packageName, packageName+"\n"+importText);
		            
		            
		             
		            //Rewriting the input text file with newContent
		            writer = new FileWriter(fileToBeModified);
		            writer.write(newContent);
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		        }
		        finally
		        {
		            try
		            {
		                //Closing the resources
		                 
		                reader.close();
		                writer.close();
		                System.out.println("done");
		            } 
		            catch (IOException e) 
		            {
		                e.printStackTrace();
		            }
		        }
		    }
			
			static void copyFile(String oldFileName, String newFileName) {
				InputStream inStream = null;
				OutputStream outStream = null;
				try{

		    	    File afile =new File(oldFileName);
		    	    File bfile =new File(newFileName);

		    	    inStream = new FileInputStream(afile);
		    	    outStream = new FileOutputStream(bfile);

		    	    byte[] buffer = new byte[1024];

		    	    int length;
		    	    //copy the file content in bytes
		    	    while ((length = inStream.read(buffer)) > 0){

		    	    	outStream.write(buffer, 0, length);

		    	    }

		    	    inStream.close();
		    	    outStream.close();

		    	    System.out.println("File is copied successful!");

		    	}catch(IOException e){
		    		e.printStackTrace();
		    	}
			}

			static void createRepository(String repository) {
				File files = new File(repository);
		        if (!files.exists()) {
		            if (files.mkdirs()) {
		                System.out.println("Multiple directories are created!");
		            } else {
		                System.out.println("Failed to create multiple directories!");
		            }
		        }
			}
			
			static void moveProjectToNewRep(String oldPath,String newPath) {
				String source = oldPath;
		        File srcDir = new File(source);

		        String destination = newPath;
		        File destDir = new File(destination);

		        try {
		            // Move the source directory to the destination directory.
		            // The destination directory must not exists prior to the
		            // move process
		            FileUtils.moveDirectory(srcDir, destDir);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}

			static void moveFilesToNewFolder(String oldPath,String newPath) {
				InputStream inStream = null;
				OutputStream outStream = null;
				
				List<String> classs = ListNameOfFilesinDirectory(oldPath);
				for (String string : classs) {
					
					try{

			    	    File afile =new File(oldPath+"\\"+string);
			    	    File bfile =new File(newPath+"\\"+string);

			    	    inStream = new FileInputStream(afile);
			    	    outStream = new FileOutputStream(bfile);

			    	    byte[] buffer = new byte[1024];

			    	    int length;
			    	    //copy the file content in bytes
			    	    while ((length = inStream.read(buffer)) > 0){

			    	    	outStream.write(buffer, 0, length);

			    	    }

			    	    inStream.close();
			    	    outStream.close();

			    	    //delete the original file
			    	    afile.delete();

			    	    System.out.println("File is copied successful!");

			    	}catch(IOException e){
			    	    e.printStackTrace();
			    	}
					
				}

			    	
			}
			
			static List<String> listFilesInFolder(String FolderPath) {
				
				try (Stream<Path> walk = Files.walk(Paths.get(FolderPath))) {

					List<String> result = walk.map(x -> x.toString())
							.filter(f -> f.endsWith(".java")).collect(Collectors.toList());

					result.forEach(System.out::println);
					return result;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			static List<String> ListNameOfFilesinDirectory(String FolderPath) {
				String[] pathnames;
				List<String> names = new ArrayList<String>();
				File f = new File(FolderPath);

				// This filter will only include files ending with .py
				FilenameFilter filter = new FilenameFilter() {
				        @Override
				        public boolean accept(File f, String name) {
				            return name.endsWith(".java");
				        }
				    };
				    
				    pathnames = f.list(filter);
				    for (String string : pathnames) {
				    	names.add(string);
					}
				    return names;
			}
			
		 	static void createNewAspectClasses(List<String> newClassNames , String packagePath, String newPackage) {
				for (String newClassName : newClassNames) {
					newClassName=newClassName.replace(".java", "");
					copyFile(packagePath+"ContactAspect.java",packagePath+newClassName+"Aspect.java");
					modifyFile(packagePath+newClassName+"Aspect.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"Aspect.java","contact",newClassName.toLowerCase());
				}
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
				
			}
			
			static void createNewOrchestrationClasses(List<String> newClassNames , String packagePath, String newPackage) {
				for (String newClassName : newClassNames) {
					newClassName=newClassName.replace(".java", "");
					copyFile(packagePath+"ContactMapper.java",packagePath+newClassName+"Mapper.java");
					copyFile(packagePath+"ContactMapperImpl.java",packagePath+newClassName+"MapperImpl.java");
					copyFile(packagePath+"ContactOrchestration.java",packagePath+newClassName+"Orchestration.java");
					modifyFile(packagePath+newClassName+"Mapper.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"MapperImpl.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"Orchestration.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"Mapper.java","contact",newClassName.toLowerCase());
					modifyFile(packagePath+newClassName+"MapperImpl.java","contact",newClassName.toLowerCase());
					modifyFile(packagePath+newClassName+"Orchestration.java","contact",newClassName.toLowerCase());
				}
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
			}

			static void createNewRepositoryClasses(List<String> newClassNames , String packagePath, String newPackage) {
				for (String newClassName : newClassNames) {
					newClassName=newClassName.replace(".java", "");
					copyFile(packagePath+"ContactRepository.java",packagePath+newClassName+"Repository.java");
					modifyFile(packagePath+newClassName+"Repository.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"Repository.java","contact",newClassName.toLowerCase());
				}
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
			}

			static void createNewSpecificationClasses(List<String> newClassNames , String packagePath, String newPackage) {
				for (String newClassName : newClassNames) {
					newClassName=newClassName.replace(".java", "");
					copyFile(packagePath+"ContactSpecification.java",packagePath+newClassName+"Specification.java");
					modifyFile(packagePath+newClassName+"Specification.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"Specification.java","contact",newClassName.toLowerCase());
				}
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
			}

			static void createNewRestServiceClasses(List<String> newClassNames , String packagePath, String newPackage) {
				for (String newClassName : newClassNames) {
					newClassName=newClassName.replace(".java", "");
					copyFile(packagePath+"ContactRestService.java",packagePath+newClassName+"RestService.java");
					modifyFile(packagePath+newClassName+"RestService.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"RestService.java","contact",newClassName.toLowerCase());
				}
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
			}

			static void createNewServiceClasses(List<String> newClassNames , String packagePath, String newPackage) {
				for (String newClassName : newClassNames) {
					newClassName=newClassName.replace(".java", "");
					copyFile(packagePath+"ContactService.java",packagePath+newClassName+"Service.java");
					copyFile(packagePath+"ContactServiceImpl.java",packagePath+newClassName+"ServiceImpl.java");
					modifyFile(packagePath+newClassName+"Service.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"ServiceImpl.java","Contact",newClassName);
					modifyFile(packagePath+newClassName+"Service.java","contact",newClassName.toLowerCase());
					modifyFile(packagePath+newClassName+"ServiceImpl.java","contact",newClassName.toLowerCase());
				}
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
			}
			
			static void renamePackagetoNewPackage(String packagePath, String newPackage) {
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "ma.dxc", newPackage);
				}
			}
			
			static void renameDtoPackagetoNewPackage(String packagePath, String newPackage) {
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "package dto", "package "+newPackage+".dto");
					List<String> classs = ListNameOfFilesinDirectory(packagePath);
					for (String string : classs) {
						string = string.replace("DTO.java", "");
						//modifyFile(oldClassName, string, newPackage+".model."+string);
						String importTxt = "import "+newPackage+".model."+string+";";
						String packageText = "package "+newPackage+".dto;";
						modifyFileToFixDTOPackage(oldClassName,string,packageText,importTxt);
					}
				}
			}
			
			static void renameModelPackagetoNewPackage(String packagePath, String newPackage) {
				List<String> oldClassNames = listFilesInFolder(packagePath);
				for (String oldClassName : oldClassNames) {
					modifyFile(oldClassName, "package model", "package "+newPackage+".model");
					List<String> classs = ListNameOfFilesinDirectory(packagePath);
					for (String string : classs) {
						string = string.replace(".java", "");
						modifyFile(oldClassName, "model."+string, newPackage+".model."+string);
					}
				}
			}
			
			
			public static void delete(File file)
			    	throws IOException{

			    	if(file.isDirectory()){

			    		//directory is empty, then delete it
			    		if(file.list().length==0){

			    		   file.delete();
			    		   System.out.println("Directory is deleted : "
			                                                 + file.getAbsolutePath());

			    		}else{

			    		   //list all the directory contents
			        	   String files[] = file.list();

			        	   for (String temp : files) {
			        	      //construct the file structure
			        	      File fileDelete = new File(file, temp);

			        	      //recursive delete
			        	     delete(fileDelete);
			        	   }

			        	   //check the directory again, if empty then delete it
			        	   if(file.list().length==0){
			           	     file.delete();
			        	     System.out.println("Directory is deleted : "
			                                                  + file.getAbsolutePath());
			        	   }
			    		}

			    	}else{
			    		//if file, then delete it
			    		file.delete();
			    		System.out.println("File is deleted : " + file.getAbsolutePath());
			    	}
			}
			

}
