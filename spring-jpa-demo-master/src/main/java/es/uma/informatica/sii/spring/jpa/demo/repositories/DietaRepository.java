package es.uma.informatica.sii.spring.jpa.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uma.informatica.sii.spring.jpa.demo.entities.Dieta;

public interface DietaRepository extends JpaRepository<Dieta, Long> {
	List<Dieta> findByNombre(String title);
	List<Dieta> findByIsbnAndNombreOrderByNombreAsc(String isbn, String nombre);
	
	@Query("select b from Book b where b.nombre = :nombre")
	List<Dieta> miConsultaCompleja(@Param("nombre") String nombre);
}
