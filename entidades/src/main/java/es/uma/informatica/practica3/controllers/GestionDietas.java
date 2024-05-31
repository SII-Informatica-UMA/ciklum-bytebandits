package es.uma.informatica.practica3.controllers;

import es.uma.informatica.practica3.dtos.DietaDTO;
import es.uma.informatica.practica3.dtos.DietaNuevaDTO;
import es.uma.informatica.practica3.entities.Dieta;
import es.uma.informatica.practica3.exceptions.ClienteNoExisteException;
import es.uma.informatica.practica3.exceptions.DietaNoExisteException;
import es.uma.informatica.practica3.exceptions.EntrenadorNoExisteException;
import es.uma.informatica.practica3.services.DietaService;
import java.net.URI;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping({"/dieta"})
public class GestionDietas {
   private DietaService dietasService;

   public GestionDietas(DietaService dietasService) {
      this.dietasService = dietasService;
   }

   @GetMapping
   public List<DietaDTO> obtenerDietas(@RequestParam(value = "entrenador",required = false) Long idEntrenador, @RequestParam(value = "cliente",required = false) Long idCliente) {
      if (idEntrenador != null && idCliente != null) {
         throw new IllegalArgumentException("Se debe especificar exactamente un par\u00e1metro de consulta: entrenador o cliente.");
      } else if (idEntrenador == null && idCliente == null) {
         throw new IllegalArgumentException("Se debe especificar exactamente un par\u00e1metro de consulta: entrenador o cliente.");
      } else {
         List<Dieta> dietas = null;
         if (idEntrenador != null) {
            dietas = this.dietasService.dietasDeEntrenador(idEntrenador);
         } else {
            dietas = this.dietasService.dietasDeCliente(idCliente);
         }

         return dietas.stream().map(Mapper::toDietaDTO).toList();
      }
   }

   @PostMapping
   public ResponseEntity<DietaDTO> crearDieta(@RequestParam(value = "entrenador",required = true) Long idEntrenador, @RequestBody DietaNuevaDTO dietaNuevoDTO, UriComponentsBuilder uriBuilder) {
      Dieta g = Mapper.toDieta(dietaNuevoDTO);
      g.setId((Long)null);
      g.setIdEntrenador(idEntrenador);
      g = this.dietasService.crearActualizarDieta(g);
      return ResponseEntity.created((URI)this.generadorUri(uriBuilder.build()).apply(g)).body(Mapper.toDietaDTO(g));
   }

   private Function<Dieta, URI> generadorUri(UriComponents uriBuilder) {
      return (g) -> {
         return UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/dieta").path(String.format("/%d", g.getId())).build().toUri();
      };
   }

   @PutMapping
   public ResponseEntity<DietaDTO> asociarDieta(@RequestParam(value = "cliente",required = true) Long idCliente, @RequestBody DietaDTO dietaDTO) {
      this.dietasService.asignarDieta(dietaDTO.getId(), idCliente);
      return ResponseEntity.of(this.dietasService.obtenerDieta(dietaDTO.getId()).map(Mapper::toDietaDTO));
   }

   @GetMapping({"/{idDieta}"})
   public ResponseEntity<DietaDTO> getDieta(@PathVariable Long idDieta) {
      return ResponseEntity.of(this.dietasService.obtenerDieta(idDieta).map(Mapper::toDietaDTO));
   }

   @PutMapping({"/{idDieta}"})
   public DietaDTO actualizarDieta(@PathVariable Long idDieta, @RequestBody DietaDTO dieta) {
      this.dietasService.obtenerDieta(idDieta).orElseThrow(() -> {
         return new DietaNoExisteException();
      });
      dieta.setId(idDieta);
      Dieta g = this.dietasService.crearActualizarDieta(Mapper.toDieta2(dieta));
      return Mapper.toDietaDTO(g);
   }

   @DeleteMapping({"/{idDieta}"})
   public void eliminarDieta(@PathVariable Long idDieta) {
      this.dietasService.obtenerDieta(idDieta).orElseThrow(() -> {
         return new DietaNoExisteException();
      });
      this.dietasService.eliminarDieta(idDieta);
   }

   @ExceptionHandler({IllegalArgumentException.class})
   @ResponseStatus(
      code = HttpStatus.BAD_REQUEST
   )
   public void handleIllegalArgumentException() {
   }

   @ExceptionHandler({DietaNoExisteException.class})
   @ResponseStatus(
      code = HttpStatus.NOT_FOUND
   )
   public void handleDietaNoExisteException() {
   }

   @ExceptionHandler({ClienteNoExisteException.class})
   @ResponseStatus(
      code = HttpStatus.NOT_FOUND
   )
   public void handleClienteNoExisteException() {
   }

   @ExceptionHandler({EntrenadorNoExisteException.class})
   @ResponseStatus(
      code = HttpStatus.NOT_FOUND
   )
   public void handleEntrenadorNoExisteException() {
   }
}
