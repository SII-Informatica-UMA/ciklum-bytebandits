package es.uma.informatica.practica3.services;

import es.uma.informatica.practica3.entities.Dieta;
import es.uma.informatica.practica3.exceptions.DietaNoExisteException;
import es.uma.informatica.practica3.repositories.DietaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DietaService {
   private DietaRepository dietaRepository;

   public DietaService(DietaRepository dietaRepository) {
      this.dietaRepository = dietaRepository;
   }

   public List<Dieta> dietasDeEntrenador(Long idEntrenador) {
      return this.dietaRepository.findAllByIdEntrenador(idEntrenador);
   }

   public List<Dieta> dietasDeCliente(Long idCliente) {
      return this.dietaRepository.findByIdClientesContaining(idCliente);
   }

   public Dieta crearActualizarDieta(Dieta g) {
      return (Dieta)this.dietaRepository.save(g);
   }

   public Optional<Dieta> obtenerDieta(Long id) {
      return this.dietaRepository.findById(id);
   }

   public void eliminarDieta(Long id) {
      this.dietaRepository.deleteById(id);
   }

   public void asignarDieta(Long idDieta, Long idCliente) {
      Optional<Dieta> d = this.obtenerDieta(idDieta);
      d.ifPresent((dieta) -> {
         dieta.getIdClientes().add(idCliente);
         this.dietaRepository.save(dieta);
      });
      d.orElseThrow(DietaNoExisteException::new);
   }
}

