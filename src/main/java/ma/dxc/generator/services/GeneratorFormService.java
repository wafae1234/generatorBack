package ma.dxc.generator.services;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import ma.dxc.model.GeneratorForm;

@Service
public class GeneratorFormService {

	public void generate(GeneratorForm f) {


		String newPackage = f.getNompackage().toString();
		String newPackagePath = newPackage.replace(".","\\")+"\\";
		
		String directory = f.getDiroctoryproject().toString();
		String directoryBack = directory + "\\backend";
		
		String newSrcPackagePath = newPackagePath + "\\src\\";
		String newTestPackagePath = newPackagePath + "\\test\\";

		String NewProjectName = f.getNomprojet();

		String orginPathProjectMain = directoryBack + BaseConstants.MAIN_SRC_DIR;

		String orginPathProjectTest = directoryBack + BaseConstants.TEST_SRC_DIR;

		String originModel = orginPathProjectMain+"model\\";

		String originDto = orginPathProjectMain+"dto\\";

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

		String aspectPackage = newPathProject+"aspect\\";

		String configPackage = newPathProject+"config\\";

		String dtoPackage = newPathProject+"dto\\";

		String modelPackage = newPathProject+"model\\";

		String orchestrationPackage = newPathProject+"orchestration\\";

		String repositoryPackage = newPathProject+"repository\\";

		String repositorySpecPackage = newPathProject+"repository\\specs\\";

		String restPackage = newPathProject+"rest\\";

		String securityPackage = newPathProject+"security\\";

		String servicePackage = newPathProject+"service\\";
		String packagePathOperations = servicePackage + "audit\\";

		String pathMainResFile = directoryBack + BaseConstants.MAIN_RES_DIR + "application.properties";
		
		String pathChangeLog = directoryBack + BaseConstants.MAIN_RES_DIR + "\\data\\changelog\\";
		String pathChangeLogMaster = pathChangeLog+"\\changelog-master.xml";

		String repoUrlBack = "https://github.com/chaalidiae/ServerSideCrudDxc.git";


		//////////////////////////////// front params //////////////////////////////////////


		String repoUrlFront = "https://github.com/chaalidiae/frontenddxc.git";

		String directoryFront = directory+"\\front";

		String frontApp = directoryFront+BaseConstants.FRONT_APP;

		String frontModel = directoryFront+BaseConstants.FRONT_MODEL;

		String frontIndexRouter = directoryFront+BaseConstants.FRONT_INDEX_ROUTER;

		String frontAssetsI18n = directoryFront+BaseConstants.FRONT_ASSETS_I18N;

		String frontSideBar = directoryFront+BaseConstants.FRONT_SIDE_BAR;
		
		String frontNavBar = directoryFront+BaseConstants.FRONT_NAV_BAR;

		String frontIndex = directoryFront+BaseConstants.FRONT_INDEX;


		////////////////////////////////////// pull framework /////////////////////////////////////////////

		String host = f.getLienserveur(); 
		String dbType = f.getTypebasededonne().toUpperCase();
		String username = f.getNomutilisateur(); 
		String databaseName = f.getNombd(); 
		String password = f.getMotdepasseutilisateur(); 
		String prefix = "";
		String driverName = ""; 
		String port = f.getPort();
		System.out.println(port);

		String directoryEntity = directoryBack + BaseConstants.MAIN_SRC_DIR;
		String directoryDTO = directoryBack + BaseConstants.MAIN_SRC_DIR;
		String pomPath = directoryBack + "\\pom.xml";

		System.out.println(dbType);
		if(dbType.equals(Enumerateur.MYSQL.name())) {
			prefix = Enumerateur.MYSQL.getPrefix();
			driverName = Enumerateur.MYSQL.getDriverName();

			System.out.println(Enumerateur.MYSQL.name());
			System.out.println(prefix);
			System.out.println(driverName);
		}	
		else if(dbType.equals(Enumerateur.POSTGRESQL.name())) {
			prefix = Enumerateur.POSTGRESQL.getPrefix();
			driverName = Enumerateur.POSTGRESQL.getDriverName();
		}		

		pullFramework(directoryBack,repoUrlBack);
		pullFramework(directoryFront,repoUrlFront);


		try {
			replacePom(pomPath, NewProjectName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	


		//////////////////////////////////////// generating back-end //////////////////////////////////////////
		EntitiesGenerator generator = new EntitiesGenerator(host, driverName, databaseName+"?serverTimezone=UTC", username, password, prefix);
		generator.generateEntities(directoryEntity,"model",true);
		generator.generateEntities(directoryDTO,"dto",false);
		generator.generateEntities("src\\main\\java\\ma\\dxc\\generator\\model","ma.dxc.generator.model",true);

		RenameToDTO dto = new RenameToDTO();
		List<String> liens = dto.lister(directoryDTO +"/dto" );
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

		List<String> classs = ListNameOfFilesinDirectory(orginPathProjectMain+"model");
		createNewAspectClasses(classs,aspectPackage,newPackage);
		createNewOrchestrationClasses(classs,orchestrationPackage,newPackage);
		createNewRepositoryClasses(classs,repositoryPackage,newPackage);
		createNewSpecificationClasses(classs,repositorySpecPackage,newPackage);
		createNewRestServiceClasses(classs,restPackage,newPackage);
		createNewServiceClasses(classs,servicePackage,newPackage);

		try {
			createNewSpecsOperations(classs,packagePathOperations +"Operation.java");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		renamePackagetoNewPackage(modelPackage,newPackage);
		renamePackagetoNewPackage(dtoPackage,newPackage);
		renamePackagetoNewPackage(configPackage,newPackage);
		renamePackagetoNewPackage(securityPackage,newPackage);

		//////////////////////////////////////// generating front parts //////////////////////////////////////////////////////////////////
		
		modifyFrontApp(classs,frontApp);
		//creer les class du front
		List<String> classNames = ListNameOfFilesinDirectory(originModel);
		for (String className : classNames) {
			className = className.replace(".java", "");
			String content = getClassTS(className,classNames);
			String operationPath = frontModel+"operation.enum.ts";
			String newFilePath = frontModel+className.toLowerCase()+".ts";
			String newSpecFilePath = frontModel+className.toLowerCase()+".spec.ts";
			String specContent = "import { "+className+" } from './"+className.toLowerCase()+"';\r\n" + 
					"\r\n" + 
					"describe('"+className+"', () => {\r\n" + 
					"  it('should create an instance', () => {\r\n" + 
					"    expect(new "+className+"()).toBeTruthy();\r\n" + 
					"  });\r\n" + 
					"});";
			createFile(newSpecFilePath, specContent);
			createFile(newFilePath, content);
			addOperationsToFrontAudit(operationPath,className);
		}
		//modifier index.router.ts
		int i = 1;
		for (String string : classNames) {
			i++;
			string = string.replace(".java", "");
			String newPath = ",\r\n" + 
					"  {\r\n" + 
					"    path: '"+string.toLowerCase()+"s',\r\n" + 
					"    loadChildren: () =>\r\n" + 
					"      import('./"+string.toLowerCase()+"s/"+string.toLowerCase()+"s.module').then(m => m."+string+"sModule)\r\n" + 
					"  },\r\n" + 
					"  {\r\n" + 
					"    path: 'new-"+string.toLowerCase()+"',\r\n" + 
					"    loadChildren: () =>\r\n" + 
					"      import('./"+string.toLowerCase()+"s/new-"+string.toLowerCase()+"/new-"+string.toLowerCase()+".module').then(m => m.New"+string+"Module)\r\n" + 
					"  }\r\n"+"];";
			modifyIndexRouter(frontIndexRouter,newPath);

			//ajouter les fichiers d'internationalisation avec la traduction de ID

			createFile(frontAssetsI18n+"\\en\\"+string.toLowerCase()+"s.json", "{\r\n" + 
					"    \"ID\": \"Identity\"\r\n" + 
					"  }");
			createFile(frontAssetsI18n+"\\fr\\"+string.toLowerCase()+"s.json", "{\r\n" + 
					"    \"ID\": \"Identite\"\r\n" + 
					"  }");
			createFile(frontAssetsI18n+"\\en\\new-"+string.toLowerCase()+".json", "{\r\n" + 
					"    \"ID\": \"Identity\"\r\n" + 
					"  }");
			createFile(frontAssetsI18n+"\\fr\\new-"+string.toLowerCase()+".json", "{\r\n" + 
					"    \"ID\": \"Identite\"\r\n" + 
					"  }");
			String newSideBar= "<ul class=\"list-group\">\r\n" + 
					"              <!-- /END Separator -->\r\n" + 
					"              <!-- Menu with submenu -->\r\n" + 
					"              <a href=\"#submenu"+i+"\" data-toggle=\"collapse\" aria-expanded=\"false\" class=\"bg-dark list-group-item list-group-item-action flex-column align-items-start\">\r\n" + 
					"                  <div class=\"d-flex w-100 justify-content-start align-items-center\">\r\n" + 
					"                    <i class=\"fa fa-tachometer fa-eercast mr-3\" aria-hidden=\"true\"></i>\r\n" + 
					"                      <span class=\"menu-collapsed\">"+string+"</span>\r\n" + 
					"                  </div>\r\n" + 
					"              </a>\r\n" + 
					"              <!-- Submenu content -->\r\n" + 
					"              <div id='submenu"+i+"' class=\"collapse sidebar-submenu\">\r\n" + 
					"                  <a routerLink=\"/new-"+string.toLowerCase()+"\" class=\"pb-5 nav-link list-group-item list-group-item-action bg-dark text-white\">\r\n" + 
					"                      <span class=\"menu-collapsed\">Add "+string+"</span>\r\n" + 
					"                  </a>\r\n" + 
					"                  <a routerLink=\"/"+string.toLowerCase()+"s\" class=\"pb-5 nav-link list-group-item list-group-item-action bg-dark text-white\">\r\n" + 
					"                      <span class=\"menu-collapsed\">List "+string+"</span>\r\n" + 
					"                  </a>\r\n" + 
					"              </div>  \r\n" + 
					"          </ul><!-- List Group END HereICanAddNewClasss";
			
			String newNavBar = 	"\r\n"+
								"					<p class=\"active text-center\">"+string+"s</p>\r\n" + 
								"                    <a class=\"nav-link dropdown-item\" routerLink=\"/new-"+string.toLowerCase()+"\">Add "+string+"</a>\r\n" + 
								"                    <a class=\"nav-link dropdown-item\" routerLink=\"/"+string.toLowerCase()+"s\">List "+string+"</a>\r\n"+
								"						<!-- HereICanAddNewClasss";
			modifyAddingClasses(frontSideBar, newSideBar);
			modifyAddingClasses(frontNavBar, newNavBar);
			//creating new modules
			createNewFrontModuleClass(classNames,frontIndex,originModel);

		}

		//////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		String newChangeLogPath = "<include  file=\"data/changelog/changelog-v0.0.3.xml\"/> \r\n" + 
				"  <!-- HereICanAddNewClasss ";
		modifyAddingClasses(pathChangeLogMaster, newChangeLogPath);
		createChangeLog(pathChangeLog, classNames);
		
		
		
		renameModelPackagetoNewPackage(originModel,newPackage);
		renameDtoPackagetoNewPackage(originDto,newPackage);

		moveFilesToNewFolder(orginPathProjectMain+"model",modelPackage);
		moveFilesToNewFolder(orginPathProjectMain+"dto",dtoPackage);


		File directoryDto = new File(originDto);
		File directoryMa = new File(originMa);
		File directoryModel = new File(originModel);
		File directoryTestMa = new File(originTestMa);
		File contactModelPackage = new File(modelPackage+"Contact.java");
		File contactDTOPackage = new File(dtoPackage+"ContactDTO.java");
		File contactFrontIndex = new File(directoryFront+"\\src\\app\\main\\index\\contacts");
		File contactFrontModel = new File(directoryFront+"\\src\\app\\shared\\model\\contact.ts");
		File contactFrontModelSpec = new File(directoryFront+"\\src\\app\\shared\\model\\contact.spec.ts");
		File contactFrontI18nEn = new File(directoryFront+"\\src\\assets\\i18n\\en\\contacts.json");
		File contactFrontI18nFr = new File(directoryFront+"\\src\\assets\\i18n\\fr\\contacts.json");
		File newcontactFrontI18nEn = new File(directoryFront+"\\src\\assets\\i18n\\en\\new-contact.json");
		File newcontactFrontI18nFr = new File(directoryFront+"\\src\\assets\\i18n\\fr\\new-contact.json");
		String appModule = directoryFront+"\\src\\app\\app.module.ts";
		String indexRouter = directoryFront+"\\src\\app\\main\\index\\index.router.ts";
		String toDelRouter1 = "{" + 
				"path: 'contacts'," + 
				"loadChildren: () =>" + 
				"import('./contacts/contacts.module').then(m => m.ContactsModule)" + 
				"},";
		String toDelRouter2 = "{" + 
				"    path: 'new-contact'," + 
				"    loadChildren: () =>" + 
				"      import('./contacts/new-contact/new-contact.module').then(m => m.NewContactModule)" + 
				"  },";

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
				delete(contactModelPackage);
				delete(contactDTOPackage);
				delete(contactFrontIndex);
				delete(contactFrontModel);
				delete(contactFrontModelSpec);
				delete(contactFrontI18nEn);
				delete(contactFrontI18nFr);
				delete(newcontactFrontI18nEn);
				delete(newcontactFrontI18nFr);
				System.out.println("removeLine");
				removeLine(appModule,"contact");
				removeLine(appModule,"Contact");
				//System.out.println("modifyFile");
				//modifyFile(indexRouter,toDelRouter1,"");
				//modifyFile(indexRouter,toDelRouter2,"");


			}catch(IOException e){
				e.printStackTrace();
				System.exit(0);
			}
		}

		try {
			renameMainRessourceFile(pathMainResFile, prefix, host, databaseName, username, password, driverName, port);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done with backend");


		//////////////////////////////////////// generating front-end //////////////////////////////////////////
		
		
		
		System.out.println("Done with frontend");
		
		String directoryPath = System.getProperty("user.dir")+"\\src\\main\\java\\ma\\dxc\\generator\\model";
        
        File modelDirectory = new File(directoryPath);
        try {
			FileUtils.cleanDirectory(modelDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	//******************************Fonctions*******************************//

	/**
	 * 
	 * @param directory
	 */
	static void pullFramework(String directory,String repoUrl) {
		File projectDirectory = new File(directory);
		if (projectDirectory.mkdirs()) { 
			System.out.println("Directory is created"); 
		} 
		else { 
			System.out.println("Directory cannot be created"); 
		} 

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
	}

	public static void replacePom(String filePath, String newString) throws IOException {

		modifyFile(filePath,"Contact", newString);
		modifyFile(filePath,"contact", newString);
	}

	static void renameMainRessourceFile(String pathMainResFile,String prefix, String host, String databaseName, String username, String password, String driverName, String port) throws ConfigurationException {
		String url = prefix + "//" + host + "/" + databaseName;
		PropertiesConfiguration properties = new PropertiesConfiguration(pathMainResFile);
		properties.setProperty("spring.datasource.url", url+"?serverTimezone=UTC");
		properties.setProperty("spring.datasource.username", "root");
		properties.setProperty("spring.datasource.password", "");
		properties.setProperty("spring.datasource.driver-class-name", driverName);
		properties.setProperty("server.port", port);
		properties.save();
		System.out.println("config.properties updated Successfully!!");
	} 


	/**
	 * 
	 * @param pathProject
	 * @param newMainClassName
	 */
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

	/**
	 * 
	 * @param pathProject
	 * @param newMainClassName
	 */
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


	/**
	 * 
	 * @param filePath
	 * @param oldString
	 * @param newString
	 */
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

	static void modifyAllFile(String filePath,String newContent)
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

	static void modifyIndexRouter(String filePath, String newString)
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
			Pattern p = Pattern.compile("\\]"+";$");
			Matcher m = p.matcher(oldContent);
			String newContent = m.replaceAll(newString);

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

	static void modifyAddingClasses(String filePath, String newString)
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
			String newContent = oldContent.replaceAll("HereICanAddNewClasss","-->"+newString);

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

	static void addInTheBegenning(String filePath, String newLine)
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
			String newContent = newLine+"\r\n"+oldContent;

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

	/**
	 * 
	 * @param filePath
	 * @param oldString
	 * @param packageName
	 * @param importText
	 */
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

	static void addUpdatePropertiesToModel(String filePath, String oldString)
	{
		File fileToBeModified = new File(filePath);
		String oldContent = "";
		BufferedReader reader = null;
		List<String> listOfProperties = new ArrayList<String>();
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
			boolean ifExists = oldContent.contains("class "+oldString);
			if(ifExists) {
				String newContent = oldContent;
				listOfProperties = getPropertiesAndTypes(oldString);
				
				String method = "public "+oldString+" updateProperties("+oldString+" "+oldString.toLowerCase()+"){\n";
				for (String string : listOfProperties) {
					string = string.split(" ")[1];
					method=method+"this."+string+" = "+oldString.toLowerCase()+"."+string+";\n";
				}
				method=method+"return this;\n}\n}";

				String tostring = "public String toString() {\r\n" + 
						"		return \""+oldString+" [";
				for (String string : listOfProperties) {
					string = string.split(" ")[1];
					tostring = tostring +string+"=\" + "+string+" + \", \"+\"";
				}
				tostring = tostring + "]\";\r\n" +"	}\n";

				String deleted = "	private boolean deleted = false;\r\n" + 
						"\r\n" + 
						"	\r\n" + 
						"	public boolean isDeleted() {\r\n" + 
						"		return deleted;\r\n" + 
						"	}\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"	public void setDeleted(boolean deleted) {\r\n" + 
						"		this.deleted = deleted;\r\n" + 
						"	}\n";			
				newContent = newContent.replaceAll("[}]$",deleted+tostring+method);


				FileWriter writer = null;
				//Rewriting the input text file with newContent
				writer = new FileWriter(fileToBeModified);
				writer.write(newContent);

				writer.close();
			}
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
				System.out.println("done");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	static void addUpdatePropertiesToDTO(String filePath, String oldString)
	{
		File fileToBeModified = new File(filePath);
		String oldContent = "";
		BufferedReader reader = null;
		List<String> listOfProperties = new ArrayList<String>();
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
			boolean ifExists = oldContent.contains("class "+oldString);
			if(ifExists) {
				String newContent = oldContent;
				listOfProperties = getPropertiesAndTypes(oldString);
				
				String method = "public "+oldString+"DTO updateProperties("+oldString+"DTO "+oldString.toLowerCase()+"){\n";
				for (String string : listOfProperties) {
					string = string.split(" ")[1];
					method=method+"this."+string+" = "+oldString.toLowerCase()+"."+string+";\n";
					System.out.println(string);
				}
				method=method+"return this;\n}\n}";

				String deleted = "	private boolean deleted = false;\r\n" + 
						"\r\n" + 
						"	\r\n" + 
						"	public boolean isDeleted() {\r\n" + 
						"		return deleted;\r\n" + 
						"	}\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"	public void setDeleted(boolean deleted) {\r\n" + 
						"		this.deleted = deleted;\r\n" + 
						"	}\n";			
				newContent = newContent.replaceAll("[}]$",deleted+method);


				FileWriter writer = null;
				//Rewriting the input text file with newContent
				writer = new FileWriter(fileToBeModified);
				writer.write(newContent);

				writer.close();
			}
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
				System.out.println("done");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	static void addOperationsToFrontAudit(String filePath, String oldString)
	{
		File fileToBeModified = new File(filePath);
		String oldContent = "";
		BufferedReader reader = null;
		List<String> listOfProperties = new ArrayList<String>();
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
			
				String newContent = oldContent;
				String operation = ",\n"
									+"	// "+oldString+" operation\n"
									+"	INSERTE_"+oldString.toUpperCase()+",\n"
									+"	UPDATE_"+oldString.toUpperCase()+",\n"
									+"	DELETE_"+oldString.toUpperCase()+"\n}";
				
				newContent = newContent.replaceAll("[}]$",operation);


				FileWriter writer = null;
				//Rewriting the input text file with newContent
				writer = new FileWriter(fileToBeModified);
				writer.write(newContent);

				writer.close();
			
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
				System.out.println("done");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	static String getClassTS(String className, List<String> classNames)
	{
		List<String> listOfProperties = new ArrayList<String>();
		List<String> list = getPropertiesAndTypes(className);
		for (String string : classNames) {
			string=string.replaceAll(".java", "");
		}
		String importText = "";
		for (String string : list) {
			String[] tab = string.split(" ");
			
			if(tab[0].toLowerCase().equals("string")) {
				tab[0]="= '';";}
			else if(tab[0].toLowerCase().equals("date")) {
				tab[0]=": Date = new Date();";}
			else if(tab[0].toLowerCase().equals(tab[1].toLowerCase())) {
				importText = importText + "import { "+tab[0]+" } from './"+tab[0].toLowerCase()+"';\r\n";
				tab[0]=": "+tab[0]+" = null;";
			}
			else if(tab[0].toLowerCase().equals("set")) {
				String type = tab[1].substring(0, tab[1].length() - 1);
				type = type.substring(0, 1).toUpperCase() + type.substring(1);
				importText = importText + "import { "+type+" } from './"+type.toLowerCase()+"';\r\n";
				tab[0]=": Array<"+type+"> = null;";
			}
			else {tab[0]=": any = null;";}
			string = tab[1]+" "+tab[0];
			System.out.println(string);
			listOfProperties.add(string);
		}
		String content = importText+"\r\n"+"export class "+className+" {\r\n" +"  ";
		for (String propertie : listOfProperties) {
			content = content +propertie+"\r\n" +"  ";
		}
		content = content + "\r\n" +"}";
		
		return content;
	}

	static List<String> getPropertiesAndTypes(String className)
	{
		List<String> listOfProperties = new ArrayList();
		String allpath = "src\\main\\java\\ma\\dxc\\generator\\model";
		String path = "src\\main\\java\\";
        File dir = new File(allpath);
        
        File[] list = dir.listFiles();
        
        List<File> l2 = Arrays.asList(list).stream().filter(e-> e.getAbsolutePath().endsWith(".java")).collect(Collectors.toList());
         
        //if (helloWorldJava.getParentFile().exists() || helloWorldJava.getParentFile().mkdirs()) {

            try {

                /** Compilation Requirements *********************************************************************************************/
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path"));


                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(l2);
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);
                /********************************************************************************************* Compilation Requirements **/
                if (task.call()) {
                    /** Load and execute *************************************************************************************************/
                    System.out.println("Yipe");
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(path).toURI().toURL()});
                    Class<?> loadedClass = classLoader.loadClass("ma.dxc.generator.model."+className);

                    for(Field attribut : loadedClass.getDeclaredFields()) {
                    	// System.out.print("   "+Modifier.toString(attribut.getModifiers()));
        	        	String type = attribut.getType().getName();
        	  
        	        	if(type.contains(".")) {
        	        		String[] tab = type.toString().split("\\.");
        	        		type=tab[tab.length-1];
        	        	}
        	            System.out.println(type+" "+attribut.getName());
        	            listOfProperties.add(type+" "+attribut.getName());
                    }


                    /************************************************************************************************* Load and execute **/
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                    }
                }
                fileManager.close();
            } catch (IOException | ClassNotFoundException exp) {
                exp.printStackTrace();
            }
		return listOfProperties;
        
	}
	/**
	 * 
	 * @param oldFileName
	 * @param newFileName
	 */
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

	/**
	 * 
	 * @param repository
	 */
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

	/**
	 * 
	 * @param oldPath
	 * @param newPath
	 */
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

	/**
	 * 
	 * @param oldPath
	 * @param newPath
	 */
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

	/**
	 * 
	 * @param FolderPath
	 * @return
	 */
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

	/**
	 * 
	 * @param FolderPath
	 * @return
	 */
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

	/**
	 * 
	 * @param newClassNames
	 * @param packagePath
	 * @param newPackage
	 */
	static void createNewAspectClasses(List<String> newClassNames , String packagePath, String newPackage) {
		for (String newClassName : newClassNames) {
			newClassName=newClassName.replace(".java", "");
			copyFile(packagePath+"ContactAspect.java",packagePath+newClassName+"Aspect.java");
			modifyFile(packagePath+newClassName+"Aspect.java","Contact",newClassName);
			modifyFile(packagePath+newClassName+"Aspect.java","contact",newClassName.toLowerCase());
			modifyFile(packagePath+newClassName+"Aspect.java","CONTACT",newClassName.toUpperCase());
		}
		List<String> oldClassNames = listFilesInFolder(packagePath);
		for (String oldClassName : oldClassNames) {
			modifyFile(oldClassName, "ma.dxc", newPackage);
		}
		File file = new File(packagePath+"ContactAspect.java");
		try {
			delete(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param newClassNames
	 * @param packagePath
	 * @param newPackage
	 */
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
		File ContactMapper = new File(packagePath+"ContactMapper.java");
		File ContactMapperImpl = new File(packagePath+"ContactMapperImpl.java");
		File ContactOrchestration = new File(packagePath+"ContactOrchestration.java");
		try {
			delete(ContactMapper);
			delete(ContactMapperImpl);
			delete(ContactOrchestration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param newClassNames
	 * @param packagePath
	 * @param newPackage
	 */
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
		File ContactRepository = new File(packagePath+"ContactRepository.java");
		try {
			delete(ContactRepository);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	static void removeLine(String filePath, String lineContent) throws IOException
	{
		File file = new File(filePath);
		List<String> out = Files.lines(file.toPath())
				.filter(line -> !line.contains(lineContent))
				.collect(Collectors.toList());
		Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	static void createNewSpecsOperations(List<String> newClassNames , String packagePathOperations) throws IOException {
		String str1 = "INSERTE_CONTACT,",
				str2= "UPDATE_CONTACT,",
				str3= "DELETE_CONTACT,",
				str4= "}"; 
		removeLine(packagePathOperations, str1);
		removeLine(packagePathOperations, str2);
		removeLine(packagePathOperations, str3);
		removeLine(packagePathOperations, str4);

		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(packagePathOperations, true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);

			for (String newClassName : newClassNames) {
				newClassName=newClassName.replace(".java", "");
				out.println("INSERTE_"+newClassName.toUpperCase()+",");
				out.println("UPDATE_"+newClassName.toUpperCase()+",");
				out.println("DELETE_"+newClassName.toUpperCase()+",");	    
			}
			out.println("}");
			out.close();
		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
		finally {
			if(out != null)
				out.close();
			try {
				if(bw != null)
					bw.close();
			} catch (IOException e) {
				//exception handling left as an exercise for the reader
			}
			try {
				if(fw != null)
					fw.close();
			} catch (IOException e) {
				//exception handling left as an exercise for the reader
			}
		}
	}

	/**
	 * 
	 * @param newClassNames
	 * @param packagePath
	 * @param newPackage
	 */
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
		File ContactSpecification = new File(packagePath+"ContactSpecification.java");
		try {
			delete(ContactSpecification);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param newClassNames
	 * @param packagePath
	 * @param newPackage
	 */
	static void createNewRestServiceClasses(List<String> newClassNames , String packagePath, String newPackage) {
		for (String newClassName : newClassNames) {
			newClassName=newClassName.replace(".java", "");
			copyFile(packagePath+"ContactRestService.java",packagePath+newClassName+"RestService.java");
			modifyFile(packagePath+newClassName+"RestService.java","Contact",newClassName);
			modifyFile(packagePath+newClassName+"RestService.java","contact",newClassName.toLowerCase());
			modifyFile(packagePath+newClassName+"RestService.java","READ","READ"+newClassName.toUpperCase());
			modifyFile(packagePath+newClassName+"RestService.java","ADD","ADD"+newClassName.toUpperCase());
			modifyFile(packagePath+newClassName+"RestService.java","UPDATE","UPDATE"+newClassName.toUpperCase());
			modifyFile(packagePath+newClassName+"RestService.java","DELETE","DELETE"+newClassName.toUpperCase());
		}
		List<String> oldClassNames = listFilesInFolder(packagePath);
		for (String oldClassName : oldClassNames) {
			modifyFile(oldClassName, "ma.dxc", newPackage);
		}
		File ContactRestService = new File(packagePath+"ContactRestService.java");
		try {
			delete(ContactRestService);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param newClassNames
	 * @param packagePath
	 * @param newPackage
	 */
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
		File ContactRestService = new File(packagePath+"ContactService.java");
		File ContactServiceImpl = new File(packagePath+"ContactServiceImpl.java");
		try {
			delete(ContactRestService);
			delete(ContactServiceImpl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param packagePath
	 * @param newPackage
	 */
	static void renamePackagetoNewPackage(String packagePath, String newPackage) {
		List<String> oldClassNames = listFilesInFolder(packagePath);
		for (String oldClassName : oldClassNames) {
			modifyFile(oldClassName, "ma.dxc", newPackage);
		}
	}

	/**
	 * 
	 * @param packagePath
	 * @param newPackage
	 */
	static void renameDtoPackagetoNewPackage(String packagePath, String newPackage) {
		List<String> oldClassNames = listFilesInFolder(packagePath);
		List<String> classs = ListNameOfFilesinDirectory(packagePath);
		for (String oldClassName : oldClassNames) {
			modifyFile(oldClassName, "package dto", "package "+newPackage+".dto");
			for (String string : classs) {
				string = string.replace("DTO.java", "");
				//modifyFile(oldClassName, string, newPackage+".model."+string);
				String importTxt = "import "+newPackage+".model."+string+";";
				String packageText = "package "+newPackage+".dto;";
				addUpdatePropertiesToDTO(oldClassName,string);
				modifyFileToFixDTOPackage(oldClassName,string,packageText,importTxt);
			}

		}
	}

	/**
	 * 
	 * @param packagePath
	 * @param newPackage
	 */
	static void renameModelPackagetoNewPackage(String packagePath, String newPackage) {
		List<String> oldClassNames = listFilesInFolder(packagePath);
		System.out.println("ppppppppppppppppp------------------------ppppppppppppppppppp");
		for (String oldClassName : oldClassNames) {
			modifyFile(oldClassName, "package model", "package "+newPackage+".model");
			List<String> classs = ListNameOfFilesinDirectory(packagePath);
			for (String string : classs) {
				string = string.replace(".java", "");
				addUpdatePropertiesToModel(oldClassName,string);
				modifyFile(oldClassName, "model."+string, newPackage+".model."+string);
			}
		}
		System.out.println("ppppppppppppppppp------------------------ppppppppppppppppppp");
	}

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
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

	static void modifyFrontApp(List<String> newClassNames , String frontApp) {
		String contactImportLine = "";
		for (String newClassName : newClassNames) {
			newClassName=newClassName.replace(".java", "");
			contactImportLine=contactImportLine+"import {"+newClassName+"sService} from './main/index/"+newClassName.toLowerCase()+"s/shared/"+newClassName.toLowerCase()+"s.service';\r\n" + 
					"import {New"+newClassName+"Component} from './main/index/"+newClassName.toLowerCase()+"s/new-"+newClassName.toLowerCase()+"/new-"+newClassName.toLowerCase()+".component';\r\n";
		}
		addInTheBegenning(frontApp,contactImportLine);

	}

	public static void createFile(String filePath, String content) {
		File file = new File(filePath);

		//Create the file
		try {
			if (file.createNewFile())
			{
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
			//Write Content
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	static void createNewFrontModuleClass(List<String> newClassNames,String frontIndex,String originModel) {
		List<String> list = new ArrayList<String>();
		for (String string : newClassNames) {
			string = string.replace(".java", "");
			list.add(string);
		}
		for (String newClassName : newClassNames) {
			newClassName=newClassName.replace(".java", "");
			boolean therIsSet = false;
			for (String string : getPropertiesAndTypes(newClassName)) {
				String[] tab = string.split(" ");
				if(tab[0].toLowerCase().equals("set")|| list.contains(tab[0])) {
					therIsSet = true;
					break;
				}
			}
			String newDirectoryPath = frontIndex+newClassName.toLowerCase()+"s\\";
			String oldDirectoryPath = frontIndex+"contacts\\";
			String oldSharedDirectoryPath = frontIndex+"contacts\\shared\\";
			String oldNewContactDirectoryPath = frontIndex+"contacts\\new-contact\\";
			String oldAllContactDirectoryPath = frontIndex+"contacts\\all-contacts\\";
			String oldSearchContactDirectoryPath = frontIndex+"contacts\\all-contacts\\search-contact\\";
			createDirectory(newDirectoryPath);
			//creating newClass.module.ts
			copyFile(oldDirectoryPath+"contacts.module.ts",newDirectoryPath+newClassName.toLowerCase()+"s.module.ts");
			modifyFile(newDirectoryPath+newClassName.toLowerCase()+"s.module.ts","Contact",newClassName);
			modifyFile(newDirectoryPath+newClassName+"s.module.ts","contact",newClassName.toLowerCase());
			//creating newClass.component.spec.ts
			copyFile(oldDirectoryPath+"contacts.component.spec.ts",newDirectoryPath+newClassName.toLowerCase()+"s.component.spec.ts");
			modifyFile(newDirectoryPath+newClassName.toLowerCase()+"s.component.spec.ts","Contact",newClassName);
			modifyFile(newDirectoryPath+newClassName+"s.component.spec.ts","contact",newClassName.toLowerCase());
			//creating newClass.component.css
			copyFile(oldDirectoryPath+"contacts.component.css",newDirectoryPath+newClassName.toLowerCase()+"s.component.css");
			//creating newClass-routing.module.ts
			copyFile(oldDirectoryPath+"contacts-routing.module.ts",newDirectoryPath+newClassName.toLowerCase()+"s-routing.module.ts");
			modifyFile(newDirectoryPath+newClassName.toLowerCase()+"s-routing.module.ts","Contact",newClassName);
			modifyFile(newDirectoryPath+newClassName+"s-routing.module.ts","contact",newClassName.toLowerCase());
			//creating newClass.component.html
			copyFile(oldDirectoryPath+"contacts.component.html",newDirectoryPath+newClassName.toLowerCase()+"s.component.html");
			modifyFile(newDirectoryPath+newClassName.toLowerCase()+"s.component.html","Contact",newClassName);
			modifyFile(newDirectoryPath+newClassName+"s.component.html","contact",newClassName.toLowerCase());
			modifyFile(newDirectoryPath+newClassName+"s.component.html","CONTACT",newClassName.toUpperCase());
			//creating newClass.component.ts
			copyFile(oldDirectoryPath+"contacts.component.ts",newDirectoryPath+newClassName.toLowerCase()+"s.component.ts");
			modifyFile(newDirectoryPath+newClassName.toLowerCase()+"s.component.ts","Contact",newClassName);
			modifyFile(newDirectoryPath+newClassName+"s.component.ts","contact",newClassName.toLowerCase());

			//creating shared directory
			String sharedDirectory = newDirectoryPath+"\\shared\\";
			createDirectory(sharedDirectory);
			//creating newClass.module.ts
			copyFile(oldSharedDirectoryPath+"contacts.service.ts",sharedDirectory+newClassName.toLowerCase()+"s.service.ts");
			modifyFile(sharedDirectory+newClassName.toLowerCase()+"s.service.ts","Contact",newClassName);
			modifyFile(sharedDirectory+newClassName+"s.service.ts","contact",newClassName.toLowerCase());

			//creating new-newClass directory
			String new_newClassNameDirectory = newDirectoryPath+"\\new-"+newClassName.toLowerCase()+"\\";
			createDirectory(new_newClassNameDirectory);
			//creating new-newClass.module.ts
			copyFile(oldNewContactDirectoryPath+"new-contact.module.ts",new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".module.ts");
			if(therIsSet) 
				createNewNewClassModulWithSet(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".module.ts" ,  newClassName, getPropertiesAndTypes(newClassName),newClassNames);
			else {
				modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".module.ts","Contact",newClassName);
				modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".module.ts","contact",newClassName.toLowerCase());
				
			}
			//creating new-newClass.component.spec.ts
			copyFile(oldNewContactDirectoryPath+"new-contact.component.spec.ts",new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.spec.ts");
			modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.spec.ts","Contact",newClassName);
			modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.spec.ts","contact",newClassName.toLowerCase());
			//creating new-newClass.component.css
			copyFile(oldNewContactDirectoryPath+"new-contact.component.css",new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.css");
			//creating new-newClass-routing.module.ts
			copyFile(oldNewContactDirectoryPath+"new-contact-routing.module.ts",new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+"-routing.module.ts");
			modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+"-routing.module.ts","Contact",newClassName);
			modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+"-routing.module.ts","contact",newClassName.toLowerCase());
			//creating new-newClass.component.html
			copyFile(oldNewContactDirectoryPath+"new-contact.component.html",new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.html");
			createNewFormulaire(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.html" ,  newClassName, getPropertiesAndTypes(newClassName),newClassNames);
			//creating new-newClass.component.ts
			copyFile(oldNewContactDirectoryPath+"new-contact.component.ts",new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.ts");
			if(therIsSet) 
				createNewNewClassWithSet(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.ts",  newClassName, getPropertiesAndTypes(newClassName),newClassNames);
			else {
				modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.ts","Contact",newClassName);
				modifyFile(new_newClassNameDirectory+"new-"+newClassName.toLowerCase()+".component.ts","contact",newClassName.toLowerCase());
				
			}
			//creating all-newClass directory
			String all_newClassNameDirectory = newDirectoryPath+"\\all-"+newClassName.toLowerCase()+"s\\";
			createDirectory(all_newClassNameDirectory);
			//creating all-newClass.component.spec.ts
			copyFile(oldAllContactDirectoryPath+"all-contacts.component.spec.ts",all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.spec.ts");
			modifyFile(all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.spec.ts","Contact",newClassName);
			modifyFile(all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.spec.ts","contact",newClassName.toLowerCase());
			//creating all-newClass.component.css
			copyFile(oldAllContactDirectoryPath+"all-contacts.component.css",all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.css");
			//creating all-newClass.component.html
			copyFile(oldAllContactDirectoryPath+"all-contacts.component.html",all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.html");
			createListOfObjects(all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.html",  newClassName, getPropertiesAndTypes(newClassName));

			//creating all-newClass.component.ts
			copyFile(oldAllContactDirectoryPath+"all-contacts.component.ts",all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.ts");
			modifyFile(all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.ts","Contact",newClassName);
			modifyFile(all_newClassNameDirectory+"all-"+newClassName.toLowerCase()+"s.component.ts","contact",newClassName.toLowerCase());


			//creating search-newClass directory
			String search_newClassNameDirectory = newDirectoryPath+"\\all-"+newClassName.toLowerCase()+"s\\search-"+newClassName.toLowerCase()+"\\";
			createDirectory(search_newClassNameDirectory);
			//creating search-newClass.component.spec.ts
			copyFile(oldSearchContactDirectoryPath+"search-contact.component.spec.ts",search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.spec.ts");
			modifyFile(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.spec.ts","Contact",newClassName);
			modifyFile(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.spec.ts","contact",newClassName.toLowerCase());
			//creating search-newClass.component.css
			copyFile(oldSearchContactDirectoryPath+"search-contact.component.css",search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.css");
			//creating search-newClass.component.html
			copyFile(oldSearchContactDirectoryPath+"search-contact.component.html",search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.html");
			modifyFile(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.html","Contact",newClassName);
			modifyFile(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.html","contact",newClassName.toLowerCase());
			createListOfObjects(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.html",  newClassName, getPropertiesAndTypes(newClassName));
			//creating search-newClass.component.ts
			copyFile(oldSearchContactDirectoryPath+"search-contact.component.ts",search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.ts");
			modifyFile(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.ts","Contact",newClassName);
			modifyFile(search_newClassNameDirectory+"search-"+newClassName.toLowerCase()+".component.ts","contact",newClassName.toLowerCase());



		}

	}

	static void createDirectory(String directoryPath) {
		//creating directory of newClass
		Path path = Paths.get(directoryPath);
		//if directory exists?
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				//fail to create directory
				e.printStackTrace();
			}
		}
	}

	static void createNewFormulaire(String filePath, String className,List<String> propertiesAndTypes, List<String> newClassNames) {
		List<String> list = new ArrayList<String>();
		for (String string : newClassNames) {
			string = string.replace(".java", "");
			list.add(string);
		}
		String header = "<div class=\"container mt-5\">\r\n" + 
				"    <div class=\"row ml-5\" style=\"width: 60%;margin-left: 15%!important;\">\r\n" + 
				"        <div class=\"col\">\r\n" + 
				"            <div class=\"card\">\r\n" + 
				"                <div class=\"card-header bg-darkblue text-white\"><i class=\"fa fa-"+className.toLowerCase()+"\"></i> "+className+" informations\r\n" + 
				"              </div>\r\n" + 
				"              <div class=\"card-body\">\r\n" + 
				"                <form class=\"container size\"  #"+className.toLowerCase()+"Form=\"ngForm\" (ngSubmit)=\"OnSubmit()\" novalidate>";

		String body = "";
		for (String string : propertiesAndTypes) {
			String[] tab = string.split(" ");
			if(!tab[1].toLowerCase().equals("id")) {
				if(tab[0].toLowerCase().equals("date")) {
					body = body +"\r\n" + 
							"  <div class=\"form-group\">\r\n" + 
							"    <label>{{'"+tab[1].toUpperCase()+"' | translate}} :</label>\r\n" + 
							"    <input class=\"form-control\" #"+tab[1]+"=\"ngModel\" [(ngModel)]=\""+className.toLowerCase()+"."+tab[1]+"\" type=\"date\" name=\""+tab[1]+"\" required>\r\n" + 
							"    <small class=\"text-danger\" [class.d-none]=\""+tab[1]+".valid || "+tab[1]+".untouched\">Le champ "+tab[1]+" est requis</small>\r\n" + 
							"  </div>\r\n" + 
							"";
				}
				else if(tab[0].toLowerCase().equals("set") || list.contains(tab[0])) {
					String type = "";
					if(tab[0].toLowerCase().equals("set")) {
						type = tab[1].substring(0, tab[1].length() - 1);
						type = type.substring(0, 1).toUpperCase() + type.substring(1);
						body = body + 
								"                    <div class=\"form-group\">\r\n" + 
								"                        <ng-multiselect-dropdown\r\n" + 
								"                        name=\""+type.toLowerCase()+"2\" \r\n" + 
								"                        [settings]=\"dropdownSettings\"\r\n" + 
								"                        [data]=\""+type.toLowerCase()+"s\"\r\n" + 
								"                        [(ngModel)]=\"selected"+type+"Items\"\r\n" + 
								"                        [placeholder]=\"'"+type+"s'\"\r\n" + 
								"                        (onSelect)=\"on"+type+"ItemSelect($event)\">\r\n" + 
								"                        </ng-multiselect-dropdown>\r\n" + 
								"                    </div>";
					}else {
						type = tab[0];
						body = body + 
								"                    <div class=\"form-group\">\r\n" + 
								"                        <ng-multiselect-dropdown\r\n" + 
								"                        name=\""+type.toLowerCase()+"2\" \r\n" + 
								"                        [settings]=\"dropdownSettingsForUnique\"\r\n" + 
								"                        [data]=\""+type.toLowerCase()+"s\"\r\n" + 
								"                        [(ngModel)]=\"selected"+type+"Items\"\r\n" + 
								"                        [placeholder]=\"'"+type+"s'\"\r\n" + 
								"                        (onSelect)=\"on"+type+"ItemSelect($event)\">\r\n" + 
								"                        </ng-multiselect-dropdown>\r\n" + 
								"                    </div>";
					}
					
				}
				else {
					body = body +"\r\n" + 
							"  <div class=\"form-group\">\r\n" + 
							"      <label>{{'"+tab[1].toUpperCase()+"' | translate}} :</label>\r\n" + 
							"      <input class=\"form-control\" #"+tab[1]+"=\"ngModel\" [class.is-invalid]=\""+tab[1]+".invalid && "+tab[1]+".touched\" [(ngModel)]=\""+className.toLowerCase()+"."+tab[1]+"\" type=\"text\" name=\""+tab[1]+"\" required>\r\n" + 
							"      <small class=\"text-danger\" [class.d-none]=\""+tab[1]+".valid || "+tab[1]+".untouched\">Le champ "+tab[1]+" est requis !</small>\r\n" + 
							"  </div>\r\n" + 
							"";
				}
			}
		}

		String footer = "\r\n" + 
				"  <div>\r\n" + 
				"    <button [disabled]=\""+className.toLowerCase()+"Form.form.invalid\" class=\"btn btn-success\" type=\"submit\">{{'SAVE' | translate}}</button>\r\n" + 
				"  </div>\r\n" + 
				"</form>"+"\r\n" + 
				"            </div>\r\n" + 
				"         </div>\r\n" + 
				"      </div>\r\n" + 
				"    </div>\r\n" + 
				"</div>\r\n" + 
				"<br>";

		String page = header+body+footer;
		modifyAllFile(filePath, page);
	}
	
	static void createNewNewClassWithSet(String filePath, String className,List<String> propertiesAndTypes, List<String> newClassNames) {
		List<String> list = new ArrayList<String>();
		for (String string : newClassNames) {
			string = string.replace(".java", "");
			list.add(string);
		}
		String importText = "import { Component, OnInit } from '@angular/core';\r\n" + 
				"import { IDropdownSettings } from 'ng-multiselect-dropdown';\r\n" + 
				"import { Router, ActivatedRoute } from '@angular/router';\r\n" + 
				"import { "+className+" } from 'src/app/shared/model/"+className.toLowerCase()+"';\r\n" + 
				"import { I18nComponent } from 'src/app/shared/lang/i18n/container/i18n.component';\r\n" + 
				"import { Store } from '@ngrx/store';\r\n" + 
				"import { TranslateService } from '@ngx-translate/core';\r\n" + 
				"import * as fromI18n from '../../../../shared/lang/i18n/reducers';\r\n" + 
				"import { "+className+"sService } from 'src/app/main/index/"+className.toLowerCase()+"s/shared/"+className.toLowerCase()+"s.service';\r\n" + 
				"import { FormBuilder } from '@angular/forms';"+
				"\r\n" + 
				"\r\n" + 
				"@Component({\r\n" + 
				"  selector: 'app-new-"+className.toLowerCase()+"',\r\n" + 
				"  templateUrl: './new-"+className.toLowerCase()+".component.html',\r\n" + 
				"  styleUrls: ['./new-"+className.toLowerCase()+".component.css'],\r\n" +
				"providers: ["+className+"sService";
		String importText2 ="]\r\n"+
				"})\r\n" + 
				"export class New"+className+"Component extends I18nComponent{\r\n" + 
				"  id: string;\r\n" + 
				"  mode = 1;\r\n" + 
				"  "+className.toLowerCase()+":any;\r\n"+
				"  dropdownSettings:IDropdownSettings={};\r\n"+
				"  dropdownSettingsForUnique:IDropdownSettings={};\r\n";
		String constructor = "\r\n" + 
				"  constructor(\r\n" + 
				"    private fb: FormBuilder,\r\n" + 
				"    readonly translate: TranslateService,\r\n"+ 
				"    private router: Router,\r\n" + 
				"    private route: ActivatedRoute,\r\n" + 
				"    readonly store: Store<fromI18n.State>,\r\n"+
				"    private "+className.toLowerCase()+"service: "+className+"sService\r\n"; 
		String contructor2 ="\r\n"+
				") {\r\n" + 
				"      super(store, translate);\r\n" + 
				"\r\n" + 
				"      this."+className.toLowerCase()+" = new "+className+"();";
		String contructor3 ="\r\n" + 
				"      this.dropdownSettingsForUnique = {\r\n" + 
				"        singleSelection: true,\r\n" + 
				"        idField: 'id',\r\n" + 
				"        textField: 'id',\r\n" + 
				"        selectAllText: 'Select All',\r\n" + 
				"        unSelectAllText: 'UnSelect All',\r\n" + 
				"        itemsShowLimit: 3,\r\n" + 
				"        allowSearchFilter: true\r\n" + 
				"      };\r\n" + 
				"      this.dropdownSettings = {\r\n" + 
				"        singleSelection: false,\r\n" + 
				"        idField: 'id',\r\n" + 
				"        textField: 'id',\r\n" + 
				"        selectAllText: 'Select All',\r\n" + 
				"        unSelectAllText: 'UnSelect All',\r\n" + 
				"        itemsShowLimit: 3,\r\n" + 
				"        allowSearchFilter: true\r\n" + 
				"      };\r\n" + 
				"      this.route.queryParams.subscribe((params) => {\r\n" + 
				"        this.id = params.id;\r\n" + 
				"        if (typeof this.id === 'undefined'){\r\n" + 
				"          this.mode = 1;\r\n" + 
				"        }\r\n" + 
				"        else {\r\n" + 
				"          this.mode = 0;\r\n" + 
				"        }\r\n" + 
				"      });\r\n" + 
				"      this."+className.toLowerCase()+"service.get"+className+"ById(this.id)\r\n" + 
				"      .subscribe(data => {\r\n" + 
				"        this."+className.toLowerCase()+" = data;";
		String constructor4 = "\r\n" + 
				"      }, error => console.log('error : \\n' + error));\r\n" + 
				"     }\r\n" + 
				"";
		String constructor5 = "\r\n" + 
				"  OnSubmit(){\r\n";
		String constructor6 = "if (this.mode === 1) {\r\n" + 
				"        this."+className.toLowerCase()+"service.save"+className+"(this."+className.toLowerCase()+").subscribe(data => console.log('"+className.toLowerCase()+" added successfully'));\r\n" + 
				"      } else {\r\n" + 
				"        this."+className.toLowerCase()+"service.update"+className+"(this."+className.toLowerCase()+").subscribe(data => console.log('"+className.toLowerCase()+" updated successfully'));\r\n" + 
				"      }\r\n" + 
				"      this.router.navigate(['/"+className.toLowerCase()+"']);\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"}";
		
		String newImportText = "";
		String newImportText2 = "";
		String newAttributesText = "";
		String newConstructor = "";
		String newConstructor2 = "";
		String newConstructor3 = "";
		String newConstructor4 = "";
		String newConstructor5 = "";
		for (String string : propertiesAndTypes) {
			String[] tab = string.split(" ");
			if(tab[0].toLowerCase().equals("set") || list.contains(tab[0])) {
				String type = "";
				if(tab[0].toLowerCase().equals("set")) {
					type = tab[1].substring(0, tab[1].length() - 1);
					type = type.substring(0, 1).toUpperCase() + type.substring(1);
					
				}else {
					type = tab[0];
					
				}
				newImportText = newImportText +"\r\n"+
								"import { "+type+" } from 'src/app/shared/model/"+type.toLowerCase()+"';\r\n" + 
								"import { "+type+"sService } from 'src/app/main/index/"+type.toLowerCase()+"s/shared/"+type.toLowerCase()+"s.service';\r\n";
				newImportText2 = newImportText2 +","+type+"sService";
				newAttributesText = newAttributesText +"\r\n"+
								"  "+type.toLowerCase()+"sI: any;\r\n" + 
								"  "+type.toLowerCase()+"s: any;\r\n" + 
								"  "+className.toLowerCase()+type+"s:Array<"+type+">;\r\n" + 
								"  selected"+type+"Items: Array<any> = [];";
				newConstructor = newConstructor +",\r\n"+
								"    private "+type.toLowerCase()+"sService: "+type+"sService	\r\n" ;
				newConstructor2 = newConstructor2 + "\r\n"+
								"this.getPageOf"+type+"s();";
				newConstructor3 = newConstructor3 +"\r\n" + 
						"        this."+className.toLowerCase()+type+"s = data['"+type.toLowerCase()+"s'];\r\n" + 
						"        this.selected"+type+"Items = this."+className.toLowerCase()+type+"s\r\n" + 
						"                 .map(item => item)\r\n" + 
						"                 .filter((thing, i, arr) => arr.findIndex(t => t.id === thing.id) === i);\r\n" + 
						"      console.log('selected"+type+"Items efter click on edit: \\n' + this.selected"+type+"Items);";
				newConstructor4 = newConstructor4 + "\r\n" + 
						"  getPageOf"+type+"s() {\r\n" + 
						"    this."+type.toLowerCase()+"sService.get"+type+"s()\r\n" + 
						"      .subscribe(data => {\r\n" + 
						"        this."+type.toLowerCase()+"sI = data;\r\n" + 
						"        this."+type.toLowerCase()+"s = this."+type.toLowerCase()+"sI.map(\r\n" + 
						"          item => {return{id: item.id}})\r\n" + 
						"        .filter((value, index, self) => self.indexOf(value) === index);\r\n" + 
						"      }, error => {\r\n" + 
						"        this.router.navigateByUrl('/**');\r\n" + 
						"      });\r\n" + 
						"  }\r\n" + 
						"\r\n" + 
						"  on"+type+"ItemSelect(item: any) {\r\n" + 
						"    console.log('item : \\n' + item);\r\n" + 
						"    console.log('electedItems : \\n' + this.selected"+type+"Items);\r\n" + 
						"  }\r\n" + 
						"";
				newConstructor5 = newConstructor5 + "\r\n" + 
						"      this."+className.toLowerCase()+"."+type.toLowerCase()+"s = this.selected"+type+"Items.map(x => x);\r\n";
				
			}
		}
		
		String page = newImportText+importText+newImportText2+importText2+newAttributesText+constructor+newConstructor
						+contructor2+newConstructor2+contructor3+newConstructor3+constructor4+newConstructor4
						+constructor5+newConstructor5+constructor6;
		modifyAllFile(filePath, page);
	}
	
	static void createNewNewClassModulWithSet(String filePath, String className,List<String> propertiesAndTypes, List<String> newClassNames) {
		List<String> list = new ArrayList<String>();
		for (String string : newClassNames) {
			string = string.replace(".java", "");
			list.add(string);
		}
		String importText = "import { NgModule } from '@angular/core';\r\n" + 
				"import { CommonModule } from '@angular/common';\r\n" + 
				"\r\n" + 
				"import { New"+className+"RoutingModule } from './new-"+className.toLowerCase()+"-routing.module';\r\n" + 
				"import { New"+className+"Component } from './new-"+className.toLowerCase()+".component';\r\n" + 
				"import { FormsModule, ReactiveFormsModule } from '@angular/forms';\r\n" + 
				"import { I18nModule } from 'src/app/shared/lang/i18n/i18n.module';\r\n" + 
				"import { TranslateModule, TranslateLoader } from '@ngx-translate/core';\r\n" + 
				"import { HttpClient } from '@angular/common/http';\r\n" + 
				"import { TranslateHttpLoader } from '@ngx-translate/http-loader';";
		
		String newImportText = "\r\n" + 
				"import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';";
		
		String body = "\r\n" + 
				"export function createTranslateLoader(http: HttpClient) {\r\n" + 
				"  return new TranslateHttpLoader(http, '../../../../../assets/i18n/', '/new-"+className.toLowerCase()+".json');\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"@NgModule({\r\n" + 
				"  declarations: [New"+className+"Component],\r\n" + 
				"  imports: [\r\n" + 
				"    CommonModule,\r\n" + 
				"    New"+className+"RoutingModule,\r\n" + 
				"    FormsModule,\r\n" + 
				"    I18nModule,\r\n" + 
				"    TranslateModule.forChild({\r\n" + 
				"      loader: {\r\n" + 
				"        provide: TranslateLoader,\r\n" + 
				"        useFactory: createTranslateLoader,\r\n" + 
				"        deps: [HttpClient]\r\n" + 
				"      },\r\n" + 
				"      isolate: true\r\n" + 
				"    }),";
		
		String newBody = "\r\n" + 
				"    NgMultiSelectDropDownModule.forRoot(),"+
				"\r\n" + 
				"    ReactiveFormsModule";
		
		String footer = "]\r\n" + 
				"})\r\n" + 
				"export class New"+className+"Module { }\r\n" + 
				"";
		String page = "";
		boolean therIsSet = false;
		for (String string : propertiesAndTypes) {
			String[] tab = string.split(" ");
			if(tab[0].toLowerCase().equals("set") || list.contains(tab[0])) {
				therIsSet = true;
				break;
			}
		}
		
		if(therIsSet) page = importText+newImportText+body+newBody+footer;
		else page = importText+body+footer;
			
		modifyAllFile(filePath, page);
		
	}
	static void createListOfObjects(String filePath, String className,List<String> propertiesAndTypes){
		String header = "<table class=\"table table-striped\">\r\n" + 
				"    <tr>\r\n" + 
				"      ";
		String body1 = "";
		for (String string : propertiesAndTypes) {
			String[] tab = string.split(" ");
			if(!tab[1].toLowerCase().equals("id"))
				body1 = body1+"\r\n<th>{{'"+tab[1].toUpperCase()+"' | translate}}</th>";
		}


		String body2 = "<th>{{'ACTIONS' | translate}}</th>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr *ngFor=\"let "+className.toLowerCase()+" of "+className.toLowerCase()+"s\">";

		String body3 = "";
		for (String string : propertiesAndTypes) {
			String[] tab = string.split(" ");
			if(tab[0].toLowerCase().equals("set")) {
				String typeText = tab[1].substring(0, tab[1].length() - 1);
				body3 = body3+"\r\n" + 
						"      <td>\r\n" + 
						"        <ul>\r\n" + 
						"          <li *ngFor=\"let "+typeText+" of "+className.toLowerCase()+"."+tab[1]+"\">{{ "+typeText+".id}}</li>\r\n" + 
						"        </ul>\r\n" + 
						"      </td>";
			}
			else if(tab[1].toLowerCase().equals("date"))
				body3 = body3+"\r\n" + 
						"      <td>{{"+className.toLowerCase()+"."+tab[1]+"}}</td>\r\n" + 
						"      ";
			else if(tab[0].toLowerCase().equals(tab[1])) 
				body3 = body3+"\r\n" + 
						"      <td>{{"+className.toLowerCase()+"."+tab[1]+".id}}</td>\r\n" + 
						"      ";
			else if(!tab[1].toLowerCase().equals("id"))
				body3 = body3+"\r\n" + 
						"      <td>{{"+className.toLowerCase()+"."+tab[1]+"}}</td>\r\n" + 
						"      ";
		}


		String footer = "\r\n" + 
				"      <button class=\"btn btn-outline-success\" (click)=\"OnUpdate("+className.toLowerCase()+".id)\">Edit</button>\r\n" + 
				"      <button class=\"btn btn-outline-danger\" (click)=\"OnDelete("+className.toLowerCase()+".id)\">Delete</button>\r\n" + 
				"    </tr>\r\n" + 
				"  </table>\r\n" + 
				"  <ul class=\"pagination\">\r\n" + 
				"    <li class=\"page-item\"><a class=\"page-link\" href=\"\"(click)=\"setPrevious($event)\">Previous</a></li>\r\n" + 
				"    <li class=\"page-item\" *ngFor=\"let p of pages; let i=index\">\r\n" + 
				"      <a class=\"page-link\" href=\"\" (click)=\"setPage(i,$event)\">{{i}}</a>\r\n" + 
				"    </li>\r\n" + 
				"    <li class=\"page-item\"><a class=\"page-link\" href=\"\"(click)=\"setNext($event)\">Next</a></li>\r\n" + 
				"    <select (change)=\"selectSize($event)\" class=\"custom-select\" style=\"width: 5%; margin-left: 1%;\" >\r\n" + 
				"      <option value=\"5\">5</option>\r\n" + 
				"      <option value=\"10\">10</option>\r\n" + 
				"      <option value=\"20\">20</option>\r\n" + 
				"  </select>\r\n" + 
				"  </ul> ";

		String page = header+body1+body2+body3+footer;
		modifyAllFile(filePath, page);
	}

	static void createChangeLog(String pathChangeLog, List<String> newClassNames) {
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>   \r\n" + 
				"<databaseChangeLog  \r\n" + 
				"  xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"  \r\n" + 
				"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  \r\n" + 
				"  xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog\r\n" + 
				"                      http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd\">  \r\n" + 
				"  <changeSet id=\"1\" author=\"benali\">";
		String footer = "</changeSet>\r\n" + 
				"</databaseChangeLog>";
		List<String> list = new ArrayList<String>();
		list.add("READ");
		list.add("ADD");
		list.add("UPDATE");
		list.add("DELETE");
		String insert = "";
		int i = 4;
		for (String string : newClassNames) {
			string = string.replace(".java", "").toUpperCase();
			for (String prefix : list) {
				i++;
				insert = insert + "<insert tableName=\"permission\">\r\n" + 
						"  		<column name=\"id\" value=\""+i+"\" />\r\n" + 
						"  		<column name=\"permission_name\" value=\""+prefix+string+"\" />\r\n" + 
						"  		<column name=\"deleted\" valueBoolean=\"false\" />\r\n" + 
						"  	</insert>";
			}
			
		}
		String page = header+insert+footer;
		copyFile(pathChangeLog+"changelog-v0.0.1.xml",pathChangeLog+"changelog-v0.0.3.xml");
		modifyAllFile(pathChangeLog+"changelog-v0.0.3.xml", page);
	}
}