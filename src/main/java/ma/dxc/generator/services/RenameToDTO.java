package ma.dxc.generator.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RenameToDTO {
	
	/**
	 * donner la liste des fichiers dans un repertoire donné
	 * @param lien
	 * @return
	 */
	public List<String> lister(String lien) {
		List<String> result = null;
        File path = new File(lien);
        String pathString = path.getAbsolutePath();
        
		try (Stream<Path> walk = Files.walk(Paths.get(pathString))) {

			result = walk.map(x -> x.toString())
					.filter(f -> f.endsWith(".java")).collect(Collectors.toList());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 * @param files
	 * param = list les liens absoluts des fichier à renomer
	 */
	
	public void renamFileName(List<String> files) {
		
		for(int i=0;i<files.size();i++) {
			String textfile = files.get(i);
			String file= textfile.split("DTO")[1];
	        String filename= file.split("\\.")[0].replace("\\", "");
	        String pathOldFile= textfile.split("\\.")[0];
	        
	        //renaming
	        File oldfile =new File(textfile);
	        String pathNewFile = pathOldFile+"DTO.java";
			File newfile =new File(pathNewFile);
			
			if(oldfile.renameTo(newfile)){
				System.out.println("Rename succesful");
			}else{
				System.out.println("Rename failed");
			}
			replacing(pathNewFile,filename);
			System.out.println("*************");
		}
	}

	/**
	 * renome les noms des class et leurs constructeurs en ajoutant DTO à la fin
	 * @param lien
	 * @param className
	 */
	
	private void replacing(String lien , String className) {
		Map<String,String> variableMap = fillMap(className);
		String shortLien = "src"+lien.split("src")[1];
		Path path = Paths.get(shortLien);
		System.out.println(shortLien);
		Stream<String> lines;
		try {
			lines = Files.lines(path,Charset.forName("UTF-8"));
			
			List<String> replacedLines = lines.map(line -> replaceTag(line,variableMap))
					.collect(Collectors.toList());
			Files.write(path, replacedLines, Charset.forName("UTF-8"));
			lines.close();
			System.out.println("Finding and replacing done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * creation d'une Map pour remplacer className par classNameDTO
	 * methode appeler dans la methode replacing
	 * @param className
	 * @return
	 */
	private Map<String, String> fillMap(String className){
		Map<String,String> map = new HashMap<String,String>();
		map.put(className, className+"DTO");
		return map;
	}
	
	/**
	 * la methode de remplacement
	 * methode appeler dans la methode replacing
	 * @param str
	 * @param map
	 * @return
	 */
	private static String replaceTag(String str, Map<String,String>map) {
		for(Map.Entry<String,String> entry : map.entrySet()) {
			if(str.contains(entry.getKey())) {
				str= str.replace(entry.getKey(), entry.getValue());
			}
		}
		return str;
	}
}
