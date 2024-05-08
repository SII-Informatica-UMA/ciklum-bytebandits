package es.uma.informatica.practica3.repositories;

import es.uma.informatica.practica3.entities.Dieta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaRepository extends JpaRepository<Dieta, Long> {
   List<Dieta> findAllByIdEntrenador(Long idEntrenador);

   @Query("SELECT d FROM Dieta d WHERE :idCliente MEMBER OF d.idClientes")
   List<Dieta> findByIdClientesContaining(Long idCliente);
}