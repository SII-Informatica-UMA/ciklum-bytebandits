package ciklumBytebandits.entidades.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ciklumBytebandits.entidades.entities.Dieta;

public interface DietaRepository extends JpaRepository<Dieta, Long> {
	List<Dieta> findByNombre(String title);
	List<Dieta> findByIsbnAndNombreOrderByNombreAsc(String isbn, String nombre);
	
	@Query("select b from Dieta b where b.nombre = :nombre")
	List<Dieta> miConsultaCompleja(@Param("nombre") String nombre);
}
