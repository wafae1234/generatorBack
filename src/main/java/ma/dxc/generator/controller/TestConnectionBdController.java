package ma.dxc.generator.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ma.dxc.generator.model.TestConnection;
import ma.dxc.generator.services.TestConnectionService;

@CrossOrigin(origins = "*")
@RestController

public class TestConnectionBdController {
	
	@Autowired
	private TestConnectionService tc;
	private String r;	
	@PostMapping(value="/testconnection")
	public Map<String, String> testConnection(@RequestBody Map<String,String> body )
	{
		TestConnection t = new TestConnection();
		t.setTypebasededonne(body.get("typebasededonne"));
		t.setLienserveur(body.get("lienserveur"));
		t.setNombd(body.get("databaseName"));
		t.setNomutilisateur(body.get("nomutilisateur"));
		t.setMotdepasseutilisateur(body.get("motdepasse"));
		r=tc.teste(t);
		System.out.println(r+" DXC");
		HashMap<String, String> map = new HashMap<>();
		 map.put("repo1", r);
		 return map;
	}
	@GetMapping("/reponsetest")
	@ResponseBody
	public String rep()
	{
		return r;
	}
	
}