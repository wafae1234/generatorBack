package ma.dxc.generator.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ma.dxc.generator.model.GeneratorForm;

@Repository
public interface GeneratorFormRepo extends CrudRepository<GeneratorForm,String> {
			

}
