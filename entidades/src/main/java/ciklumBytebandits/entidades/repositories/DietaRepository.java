package ciklumBytebandits.entidades.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ciklumBytebandits.entidades.entities.Dieta;

public interface DietaRepository extends JpaRepository<Dieta, Integer> {

    @Query( "Select d from Dieta d where d.usuarioId = :nombre" )
    List<Dieta> findByIdUsuaraio(@Param("nombre") String nombre);

}
