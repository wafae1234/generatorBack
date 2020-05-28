package ma.dxc.generator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.dxc.generator.model.Forminput;

@Repository
public interface forminputrepo extends CrudRepository<Forminput,String> {
			

}
