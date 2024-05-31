package es.uma.informatica.practica3.services;

import es.uma.informatica.practica3.entities.Dieta;
import es.uma.informatica.practica3.exceptions.ClienteNoExisteException;
import es.uma.informatica.practica3.exceptions.DietaNoExisteException;
import es.uma.informatica.practica3.repositories.DietaRepository;
import es.uma.informatica.practica3.security.SecurityConfguration;
import es.uma.informatica.practica3.dtos.ClienteDTO;
import es.uma.informatica.practica3.dtos.EntrenadorDTO;
import es.uma.informatica.practica3.dtos.UsuarioDTO;
import es.uma.informatica.practica3.exceptions.EntrenadorNoExisteException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional
public class DietaService {
   private DietaRepository dietaRepository;

   private RestTemplate restTemplate;
   private String token;

   
	private int portGestionEntrenadores;

	private int portGestionUsuarios;

   private int portGestionClientes;



   @Autowired
   public DietaService(DietaRepository dietaRepository) {
      this.dietaRepository = dietaRepository;
   }

   public DietaService(DietaRepository dietaRepository, RestTemplate rest) {
      this.dietaRepository = dietaRepository;
      this.restTemplate = rest;
  }


   public List<Dieta> dietasDeEntrenador(Long idEntrenador) {
      /*if(esEntrenador(idEntrenador)){
         return this.dietaRepository.findAllByIdEntrenador(idEntrenador);
      } else {
         throw new EntrenadorNoExisteException();
      }*/
      return this.dietaRepository.findAllByIdEntrenador(idEntrenador);

   }

   public List<Dieta> dietasDeCliente(Long idCliente) {
      /*if(esCliente(idCliente)){
         return this.dietaRepository.findByIdClientesContaining(idCliente);
      } else {
         throw new ClienteNoExisteException();
      }*/
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

   public void metodoInicializar(String token, RestTemplate restTemplate){
      this.token = token;
      this.restTemplate = restTemplate;
   }

   public boolean esEntrenador(Long id) {
      var authenticatedUser = SecurityConfguration.getAuthenticatedUser();
      
      if (authenticatedUser.isPresent()) {

         String userName = authenticatedUser.get().getUsername();

			var peticion = "http://localhost:" + portGestionUsuarios + "/usuario";

         // Crear la entidad HTTP con la dieta
         HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", "Bearer" + token);
         HttpEntity<List<UsuarioDTO>> requestEntityUser = new HttpEntity<>(headers);

         // Invocar al servicio REST usando exchange
         ResponseEntity<List<UsuarioDTO>> responseUser = restTemplate.exchange(peticion, HttpMethod.GET, requestEntityUser, new ParameterizedTypeReference<List<UsuarioDTO>>() {});
         
         List<UsuarioDTO> listaUsuarios = responseUser.getBody();
         Optional<UsuarioDTO> usuarioDTOOptional = listaUsuarios.stream().filter(usuario -> usuario.getNombre().equals(userName)).findFirst();
      
         if (usuarioDTOOptional.isPresent()) {
            // Obtener el ID del usuario encontrado
            Long userId = usuarioDTOOptional.get().getId();

            // Petición para obtener la lista de entrenadores
            var peticion2 = "http://localhost:" + portGestionEntrenadores + "/entrenador";
            HttpEntity<List<EntrenadorDTO>> requestEntityEntrenador = new HttpEntity<>(headers);
            
            // Invocar al servicio REST para obtener la lista de entrenadores
            ResponseEntity<List<EntrenadorDTO>> responseEntrenador = restTemplate.exchange(peticion2, HttpMethod.GET, requestEntityEntrenador, new ParameterizedTypeReference<List<EntrenadorDTO>>() {});
            
            List<EntrenadorDTO> listaEntrenadores = responseEntrenador.getBody();
            
            // Comprobar si el ID del usuario está en la lista de entrenadores
            Optional<EntrenadorDTO> entrenadorDTOOptional = listaEntrenadores.stream().filter(entrenador -> entrenador.getId().equals(userId)).findFirst();
            
            if(entrenadorDTOOptional.isPresent()){
               return entrenadorDTOOptional.get().getId().equals(id);
            } else {
               throw new EntrenadorNoExisteException();
            }
        } else {
            throw new EntrenadorNoExisteException();
        }      
      } else {
         throw new EntrenadorNoExisteException();
      }
   }

   public boolean esCliente(Long id) {
      var authenticatedUser = SecurityConfguration.getAuthenticatedUser();
      
      if (authenticatedUser.isPresent()) {

         String userName = authenticatedUser.get().getUsername();

			var peticion = "http://localhost:" + portGestionUsuarios + "/usuario";

         // Crear la entidad HTTP con la dieta
         HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", "Bearer" + token);
         HttpEntity<List<UsuarioDTO>> requestEntityUser = new HttpEntity<>(headers);

         // Invocar al servicio REST usando exchange
         ResponseEntity<List<UsuarioDTO>> responseUser = restTemplate.exchange(peticion, HttpMethod.GET, requestEntityUser, new ParameterizedTypeReference<List<UsuarioDTO>>() {});
         
         List<UsuarioDTO> listaUsuarios = responseUser.getBody();
         Optional<UsuarioDTO> usuarioDTOOptional = listaUsuarios.stream().filter(usuario -> usuario.getNombre().equals(userName)).findFirst();
      
         if (usuarioDTOOptional.isPresent()) {
            // Obtener el ID del usuario encontrado
            Long userId = usuarioDTOOptional.get().getId();

            // Petición para obtener la lista de entrenadores
            var peticion2 = "http://localhost:" + portGestionClientes + "/entrenador";
            HttpEntity<List<ClienteDTO>> requestEntityCliente = new HttpEntity<>(headers);
            
            // Invocar al servicio REST para obtener la lista de entrenadores
            ResponseEntity<List<ClienteDTO>> responseCliente = restTemplate.exchange(peticion2, HttpMethod.GET, requestEntityCliente, new ParameterizedTypeReference<List<ClienteDTO>>() {});
            
            List<ClienteDTO> listaCliente = responseCliente.getBody();
            
            // Comprobar si el ID del usuario está en la lista de entrenadores
            Optional<ClienteDTO> clienteDTOOptional = listaCliente.stream().filter(cliente -> cliente.getId().equals(userId)).findFirst();
            
            if(clienteDTOOptional.isPresent()){
               return clienteDTOOptional.get().getId().equals(id);
            } else {
               throw new ClienteNoExisteException();
            }
        } else {
            throw new ClienteNoExisteException();
        }      
      } else {
         throw new ClienteNoExisteException();
      }
   }
}

