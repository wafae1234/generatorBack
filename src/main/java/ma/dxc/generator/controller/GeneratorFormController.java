package ma.dxc.generator.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.dxc.generator.model.GeneratorForm;
import ma.dxc.generator.repository.GeneratorFormRepo;
import ma.dxc.generator.services.GeneratorFormService;

@CrossOrigin(origins = "*")
@RestController

public class GeneratorFormController {
	@Autowired
	private GeneratorFormRepo fr ;
	@Autowired
	private GeneratorFormService sf;
	@PostMapping(value="/form")
	
	public String recuperer(@RequestBody Map<String,String> body )
	{
		GeneratorForm f = new GeneratorForm();
		f.setNomprojet(body.get("nomprjet"));
		f.setNompackage(body.get("nompackage"));
		f.setTypebasededonne(body.get("typebasededonne"));
		f.setLienserveur(body.get("lienserveur"));
		f.setPort(body.get("port"));
		f.setnombd(body.get("databaseName"));
		f.setNomutilisateur(body.get("nomutilisateur"));
		f.setMotdepasseutilisateur(body.get("motdepasse"));
		//fr.save(f);
		sf.generate(f);
		
		//System.out.println(body.get("nomprjet").toString());
		return "okeeeeeeeeee";
	
	}
	
}
