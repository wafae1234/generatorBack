package ma.dxc.generator.controller;

import java.io.Console;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.dxc.generator.model.Forminput;
import ma.dxc.generator.repository.forminputrepo;
import ma.dxc.generator.services.Forminputservice;

@CrossOrigin(origins = "*")
@RestController

public class ForminputController {
	@Autowired
	private forminputrepo fr ;
	@Autowired
	private Forminputservice sf;
	@PostMapping(value="/form")
	
	public String recuperer(@RequestBody Map<String,String> body )
	{
		Forminput f = new Forminput();
		f.setNomprojet(body.get("nomprjet"));
		f.setNompackage(body.get("nompackage"));
		f.setTypebasededonne(body.get("typebasededonne"));
		f.setLienserveur(body.get("lienserveur"));
		f.setPortbd(body.get("portbd"));
		f.setNomutilisateur(body.get("nomutilisateur"));
		f.setMotdepasseutilisateur(body.get("motdepasse"));
		//fr.save(f);
		sf.generate(f);
		
		//System.out.println(body.get("nomprjet").toString());
		return "okeeeeeeeeee";
	
	}
	
}
