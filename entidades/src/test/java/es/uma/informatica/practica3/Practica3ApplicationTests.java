package es.uma.informatica.practica3;


import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;


import es.uma.informatica.practica3.controllers.GestionDietas;
import es.uma.informatica.practica3.controllers.Mapper;
import es.uma.informatica.practica3.dtos.DietaDTO;
import es.uma.informatica.practica3.dtos.DietaNuevaDTO;
import es.uma.informatica.practica3.dtos.EntrenadorDTO;
import es.uma.informatica.practica3.dtos.UsuarioDTO;
import es.uma.informatica.practica3.entities.Dieta;
import es.uma.informatica.practica3.exceptions.DietaNoExisteException;
import es.uma.informatica.practica3.exceptions.EntrenadorNoExisteException;
import es.uma.informatica.practica3.repositories.DietaRepository;
import es.uma.informatica.practica3.security.JwtUtil;
import es.uma.informatica.practica3.services.DietaService;
import org.springframework.web.util.UriComponentsBuilder;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de dietas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class Practica3ApplicationTests {
	@Autowired
	private TestRestTemplate testRestTemplate;


	@Mock
	private RestTemplate restTemplate;


	@Value(value = "${local.server.port}")
	private int port;
	private int portGestionEntrenadores;
	private int portGestionUsuarios;


	@Autowired
	private DietaRepository dietaRepo;


	@Autowired
	private JwtUtil jwtUtil;


	private String token;


	@InjectMocks
	private DietaService dietaService;


	@InjectMocks
	private GestionDietas controlador;


	private Mapper mapper = new Mapper();


	@Mock
	private UserDetails userDetails;


	private MockRestServiceServer mockServer;


	@BeforeEach
	public void inicializar() {
		Mockito.when(userDetails.getUsername()).thenReturn("testUser");
		token = jwtUtil.generateToken(userDetails);
		dietaService.metodoInicializar(token, restTemplate);
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}





	private URI uri(String scheme, String host, int port, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}


	public URI uriQuery(String scheme, String host, int port, String query, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		ub = ub.query("plan=1");
		return ub.build();
	}


	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.get(uri)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}


	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.delete(uri)
				.header("Authorization", "Bearer " + token)
				.build();
		return peticion;
	}


	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.post(uri)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}


	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.put(uri)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}


	private <T> RequestEntity<T> putWithQuery(String scheme, String host, int port, String path, Map<String, String> queryParams, T object) throws URISyntaxException {
		URI uri = uriWithQuery(scheme, host, port, path, queryParams);
		return RequestEntity.put(uri)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
	}


	private URI uriWithQuery(String scheme, String host, int port, String path, Map<String, String> queryParams) throws URISyntaxException {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
				.scheme(scheme)
				.host(host)
				.port(port)
				.path(path);


		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}


		return builder.build().toUri();
	}


	@Nested
	@DisplayName("cuando la base de datos esta vacia")
	public class BaseDatosVacia {


		@Test
		@DisplayName("lanza error cuando se llama a delete y la dieta no existe")
		public void errorDeleteDieta() {
			var peticion = delete("http", "localhost", port, "/dieta/1");
			var respuesta = testRestTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}


		@Test
		@DisplayName("lanza error cuando se llama a get y la dieta no existe")
		public void errorGetDieta() {
			var peticion = get("http", "localhost", port, "/dieta/1");
			var respuesta = testRestTemplate.exchange(peticion, new ParameterizedTypeReference<DietaDTO>() {
			});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}


		@Test
		@DisplayName("lanza error cuando se llama a put y la dieta no existe")
		public void errorPutDieta() {
			DietaDTO dietaDTO = DietaDTO.builder().build();
			var peticion = put("http", "localhost", port, "/dieta/1", dietaDTO);
			var respuesta = testRestTemplate.exchange(peticion, new ParameterizedTypeReference<DietaDTO>() {
			});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}


		@Test
		@DisplayName("error al actualizar una dieta inexistente")
		public void actualizarDietaInexistente() throws URISyntaxException {
			var dieta = DietaNuevaDTO.builder()
					.nombre("Dieta1")
					.descripcion("a")
					.observaciones("b")
					.objetivo("c")
					.alimentos(Arrays.asList("alimento1", "alimento2"))
					.recomendaciones("d")
					.build();


			var queryParams = Map.of("idCliente", "1");
			var peticion = putWithQuery("http", "localhost", port, "/dieta/1", queryParams, dieta);
			var respuesta = restTemplate.exchange(peticion, DietaDTO.class);


			if (respuesta != null) {
				int statusCode = respuesta.getStatusCode().value();
				assertThat(statusCode).isEqualTo(404);
			} else {
				fail("La respuesta del servidor es nula");
			}
		}


		@Test
		@DisplayName("error 400 al asociar una dieta a un cliente sin ser el entrenador")
		public void errorBadRequestAsociarDietaSinPermisoEntrenador() throws URISyntaxException {
			// Simulamos una solicitud de asociación de dieta a cliente
			var dieta = DietaNuevaDTO.builder()
					.nombre("Dieta1")
					.descripcion("Descripción de la dieta")
					// Agrega otros campos necesarios para la dieta
					.build();


			// Definimos la solicitud PUT con la dieta y los parámetros necesarios
			var queryParams = Map.of("idCliente", "1"); // ID del cliente al que se intenta asociar la dieta
			var peticion = putWithQuery("http", "localhost", port, "/dieta", queryParams, dieta);


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (Exception e) {
				// Manejo de excepciones
				e.printStackTrace();
			}


			// Verificamos que se haya recibido un error 400 (Bad Request)
			if (respuesta != null) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
			} else {
				fail("La respuesta del servidor es nula");
			}
		}




		@Test
		@DisplayName("devuelve error al asociar una dieta que no existe")
		public void modificarDietaInexistente() {
			var dieta = DietaNuevaDTO.builder()
					.nombre("Dieta1")
					.descripcion("a")
					.observaciones("b")
					.objetivo("c")
					.alimentos(Arrays.asList("alimento1", "alimento2"))
					.recomendaciones("d")
					.build();


			var peticion = put("http", "localhost", port, "/dieta/1", dieta);


			var respuesta = restTemplate.exchange(peticion, Void.class);


			if (respuesta != null) {
				int statusCode = respuesta.getStatusCode().value();
				assertThat(statusCode).isEqualTo(404);
			} else {
				fail("La respuesta del servidor es nula");
			}
		}


		@Test
		@DisplayName("error 403 al intentar asociar una dieta a un cliente sin permisos")
		public void errorForbiddenAsociarDietaSinPermisoEntrenador() throws URISyntaxException {
			// Simulamos una solicitud de asociación de dieta a cliente
			DietaNuevaDTO dieta = DietaNuevaDTO.builder()
					.nombre("Dieta1")
					.descripcion("Descripción de la dieta")
					// Agrega otros campos necesarios para la dieta
					.build();


			// Definimos la solicitud PUT con la dieta y los parámetros necesarios
			var queryParams = Map.of("idCliente", "1"); // ID del cliente al que se intenta asociar la dieta
			var peticion = putWithQuery("http", "localhost", port, "/dieta", queryParams, dieta);


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.Forbidden e) {
				// Si se lanza una excepción Forbidden, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción Forbidden, falla el test
			fail("Se esperaba una excepción Forbidden, pero la solicitud fue exitosa.");
		}




		@Test
		@DisplayName("error 404 al intentar actualizar una dieta que no existe")
		public void errorNotFoundActualizarDietaInexistente() throws URISyntaxException {
			// Creamos una dieta ficticia que no existe
			DietaNuevaDTO dietaInexistente = DietaNuevaDTO.builder()
					.nombre("Dieta Inexistente")
					.descripcion("Descripción de la dieta inexistente")
					// Agrega otros campos necesarios para la dieta
					.build();


			// Definimos la solicitud PUT con la dieta inexistente
			var peticion = put("http", "localhost", port, "/dieta/999", dietaInexistente);


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (Exception e) {
				// Manejo de excepciones
				e.printStackTrace();
			}


			// Verificamos que se haya recibido un error 404 (Not Found)
			if (respuesta != null) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			} else {
				fail("La respuesta del servidor es nula");
			}
		}




		@Test
		@DisplayName("error 400 al intentar crear una dieta sin ser el entrenador")
		public void errorBadRequestCrearDietaSinPermisoEntrenador() throws URISyntaxException {
			// Simulamos una solicitud de creación de dieta
			DietaNuevaDTO nuevaDieta = DietaNuevaDTO.builder()
					.nombre("Dieta Ejemplo")
					.descripcion("Descripción de la dieta")
					// Agrega otros campos necesarios para la dieta
					.build();


			// Definimos la solicitud POST con la nueva dieta
			var peticion = post("http", "localhost", port, "/dieta", nuevaDieta);


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (Exception e) {
				// Manejo de excepciones
				e.printStackTrace();
			}


			// Verificamos que se haya recibido un error 400 (Bad Request)
			if (respuesta != null) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
			} else {
				fail("La respuesta del servidor es nula");
			}
		}


		@Test
		@DisplayName("error 403 al intentar crear una dieta sin permisos")
		public void errorForbiddenCrearDietaSinPermisos() throws URISyntaxException {
			// Simulamos una solicitud de creación de dieta
			DietaNuevaDTO nuevaDieta = DietaNuevaDTO.builder()
					.nombre("Dieta Ejemplo")
					.descripcion("Descripción de la dieta")
					// Agrega otros campos necesarios para la dieta
					.build();


			// Definimos la solicitud POST con la nueva dieta
			var peticion = post("http", "localhost", port, "/dieta", nuevaDieta);


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.Forbidden e) {
				// Si se lanza una excepción Forbidden, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción Forbidden, falla el test
			fail("Se esperaba una excepción Forbidden, pero la solicitud fue exitosa.");
		}


		@Test
		@DisplayName("error 404 al intentar crear una dieta en una ruta no existente")
		public void errorNotFoundCrearDietaRutaNoExistente() throws URISyntaxException {
			// Simulamos una solicitud de creación de dieta
			DietaNuevaDTO nuevaDieta = DietaNuevaDTO.builder()
					.nombre("Dieta Ejemplo")
					.descripcion("Descripción de la dieta")
					// Agrega otros campos necesarios para la dieta
					.build();


			// Definimos la solicitud POST con la nueva dieta en una ruta no existente
			var peticion = post("http", "localhost", port, "/ruta/no/existente", nuevaDieta);


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.NotFound e) {
				// Si se lanza una excepción NotFound, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción NotFound, falla el test
			fail("Se esperaba una excepción NotFound, pero la solicitud fue exitosa.");
		}




		@Test
		@DisplayName("error 400 al intentar obtener una dieta con un identificador inválido")
		public void errorBadRequestObtenerDietaIdInvalido() throws URISyntaxException {
			// Definimos la solicitud GET para obtener una dieta con un identificador inválido
			var peticion = get("http", "localhost", port, "/dieta/abc"); // Identificador inválido "abc"


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.BadRequest e) {
				// Si se lanza una excepción BadRequest, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción BadRequest, falla el test
			fail("Se esperaba una excepción BadRequest, pero la solicitud fue exitosa.");
		}




		@Test
		@DisplayName("error 403 al intentar obtener una dieta sin permisos")
		public void errorForbiddenObtenerDietaSinPermiso() throws URISyntaxException {
			// Definimos la solicitud GET para obtener una dieta
			var peticion = get("http", "localhost", port, "/dieta/1"); // ID de la dieta a obtener


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.Forbidden e) {
				// Si se lanza una excepción Forbidden, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción Forbidden, falla el test
			fail("Se esperaba una excepción Forbidden, pero la solicitud fue exitosa.");
		}




		@Test
		@DisplayName("error 404 al intentar obtener una dieta inexistente")
		public void errorNotFoundObtenerDietaInexistente() throws URISyntaxException {
			// Definimos la solicitud GET para obtener una dieta inexistente
			var peticion = get("http", "localhost", port, "/dieta/999"); // ID de una dieta inexistente


			// Realizamos la solicitud
			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.NotFound e) {
				// Si se lanza una excepción NotFound, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción NotFound, falla el test
			fail("Se esperaba una excepción NotFound, pero la solicitud fue exitosa.");
		}


		@Test
		@DisplayName("error 400 al intentar actualizar una dieta con datos incorrectos")
		public void errorBadRequestActualizarDietaDatosIncorrectos() {
			// Simulamos una solicitud PUT para actualizar una dieta con datos incorrectos
			DietaDTO dietaDTO = DietaDTO.builder()
					.id(1L)
					.descripcion(null) // Datos incorrectos para la dieta
					.build();


			var peticion = put("http", "localhost", port, "/dieta/1", dietaDTO);


			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.BadRequest e) {
				// Si se lanza una excepción BadRequest, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción BadRequest, falla el test
			fail("Se esperaba una excepción BadRequest, pero la solicitud fue exitosa.");
		}


		@Test
		@DisplayName("error 403 al intentar actualizar una dieta sin permisos")
		public void errorForbiddenActualizarDietaSinPermiso2() {
			// Simulamos una solicitud PUT para actualizar una dieta sin permisos
			DietaDTO dietaDTO = DietaDTO.builder()
					.id(1L)
					.descripcion("Descripción de la dieta")
					.build();


			var peticion = put("http", "localhost", port, "/dieta/1", dietaDTO);


			ResponseEntity<Void> respuesta = null;
			try {
				respuesta = restTemplate.exchange(peticion, Void.class);
			} catch (HttpClientErrorException.Forbidden e) {
				// Si se lanza una excepción Forbidden, la capturamos y la manejamos
				assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
				return;
			} catch (Exception e) {
				// Otros tipos de excepciones
				e.printStackTrace();
			}


			// Si la solicitud no lanza una excepción Forbidden, falla el test
			fail("Se esperaba una excepción Forbidden, pero la solicitud fue exitosa.");
		}




	}


	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosConDatos {

		@BeforeEach
        public void setUp() {
            // Crear y guardar dietas en la base de datos
            Dieta dieta1 = Dieta.builder()
                    .nombre("Dieta1")
                    .descripcion("Descripción de la Dieta1")
                    .observaciones("Observaciones Dieta1")
                    .objetivo("Objetivo Dieta1")
                    .duracionDias(30)
                    .alimentos(Arrays.asList("alimento1", "alimento2"))
                    .recomendaciones("Recomendaciones Dieta1")
                    .idEntrenador(1L)
                    .idClientes(new HashSet<>(Arrays.asList(1L, 2L)))
                    .build();


            dietaRepo.save(dieta1);
        }


        /*@Test
        @DisplayName("Actualización exitosa de una dieta por el entrenador creador")
        public void actualizarDietaExitosa() {
            // Preparar una dieta existente para actualizar
            DietaDTO dietaDTO = DietaDTO.builder()
                    .id(1L)
                    .nombre("Dieta Actualizada")
                    .descripcion("Descripción actualizada")
                    .build();
       
            // Realizar la solicitud de actualización
            RequestEntity<DietaDTO> request = put("http", "localhost", port, "/dieta/1", dietaDTO);
            ResponseEntity<Void> response = testRestTemplate.exchange(request, Void.class);
       
            // Verificar que la solicitud fue exitosa (código 200)
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(dietaRepo.findById(1L).get().getNombre()).isEqualTo("Dieta Actualizada");
        }*/


        /*@Test
        @DisplayName("obtiene una dieta concreta correctamente")
        public void cogeDietaConcretaCorrectamente() throws JsonProcessingException {
            var peticion = "http://localhost:" + portGestionUsuarios + "/usuario";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);


            List<UsuarioDTO> usuariosDTO = new ArrayList<>();
            UsuarioDTO user = new UsuarioDTO(1L, "testuser");
            usuariosDTO.add(user);


            ObjectMapper objectMapper = new ObjectMapper();
            String usuariosJson = objectMapper.writeValueAsString(usuariosDTO);
       
            mockServer.expect(requestTo(peticion))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("Authorization", "Bearer " + token))
            .andRespond(withSuccess(usuariosJson, MediaType.APPLICATION_JSON));
       
            peticion = "http://localhost:" + portGestionEntrenadores + "/entrenador?centro=0";
       
            List<EntrenadorDTO> entrenadoresDTO = new ArrayList<>();
            EntrenadorDTO entrenadorDTO = new EntrenadorDTO(1L, 1L, "777");
            entrenadoresDTO.add(entrenadorDTO);
       
            String entrenadoresJson = objectMapper.writeValueAsString(entrenadoresDTO);
       
            mockServer.expect(requestTo(peticion))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("Authorization", "Bearer " + token))
            .andRespond(withSuccess(entrenadoresJson, MediaType.APPLICATION_JSON));
       
            peticion = "http://localhost:" + port + "/dieta/1";
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
       
            // Realizar la solicitud GET y verificar el resultado
            ResponseEntity<DietaDTO> respuesta = testRestTemplate.exchange(peticion, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<DietaDTO>() {});


            mockServer.verify();


            assertThat(respuesta.getStatusCode().equals(HttpStatus.OK));
            assertThat(respuesta.getBody().getNombre().equals("Vegetariana"));


        }*/

        /*@Test
        @DisplayName("crea una dieta a un entrenador correctamente")
        public void creaDietaConcretaCorrectamente() throws JsonProcessingException {
            String token = "your-test-token";
            String peticion;
            
            // Setup Mock Server
            mockServer = MockRestServiceServer.createServer(restTemplate);
            
            // Mocking /usuario endpoint
            peticion = "http://localhost:" + port + "/usuario";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            List<UsuarioDTO> usuariosDTO = new ArrayList<>();
            UsuarioDTO user = new UsuarioDTO(1L, "testuser");
            usuariosDTO.add(user);

            String usuariosJson = objectMapper.writeValueAsString(usuariosDTO);

            mockServer.expect(requestTo(peticion))
                    .andExpect(method(HttpMethod.GET))
                    .andExpect(header("Authorization", "Bearer " + token))
                    .andRespond(withSuccess(usuariosJson, MediaType.APPLICATION_JSON));

            // Mocking /entrenador endpoint
            peticion = "http://localhost:" + port + "/entrenador?centro=0";

            List<EntrenadorDTO> entrenadoresDTO = new ArrayList<>();
            EntrenadorDTO entrenadorDTO = new EntrenadorDTO(1L, 1L, "777");
            entrenadoresDTO.add(entrenadorDTO);

            String entrenadoresJson = objectMapper.writeValueAsString(entrenadoresDTO);

            mockServer.expect(requestTo(peticion))
                    .andExpect(method(HttpMethod.GET))
                    .andExpect(header("Authorization", "Bearer " + token))
                    .andRespond(withSuccess(entrenadoresJson, MediaType.APPLICATION_JSON));

            // Creating a diet
            DietaDTO newDieta = new DietaDTO();
            newDieta.setNombre("Musculos");
            newDieta.setDescripcion("Diet for muscle building");
            newDieta.setIdEntrenador(1L);

            peticion = "http://localhost:" + port + "/dieta";
            HttpEntity<DietaDTO> requestEntity = new HttpEntity<>(newDieta, headers);

            // Realizar la solicitud POST para crear la dieta
            ResponseEntity<DietaDTO> respuesta = testRestTemplate.exchange(
                    peticion, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<DietaDTO>() {
                    });

            // Verificar que la respuesta sea exitosa
            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Vegetariana");

            mockServer.verify();
        }*/


        @Test
        @DisplayName("intenta actualizar una dieta inexistente y devuelve 404")
        public void actualizarDietaInexistente() {
            var dietaInexistente = DietaDTO.builder()
                    .id(15L)  
                    .descripcion("Descripción de una dieta que no existe")
                    .build();


            // Intentar actualizar la dieta inexistente
            var peticion = put("http", "localhost", port, "/dieta/15", dietaInexistente);
            var respuesta = testRestTemplate.exchange(peticion, new ParameterizedTypeReference<DietaDTO>() {});


            // Verificar que se devuelve un estado 404 Not Found
            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }




        @Test
        @DisplayName("intenta eliminar una dieta inexistente y devuelve 404")
        public void errorEliminarDietaInexistente() {
            // Definimos la solicitud DELETE para eliminar una dieta inexistente con ID 15
            var peticion = delete("http", "localhost", port, "/dieta/15");


            // Enviamos la solicitud DELETE y esperamos la respuesta
            var respuesta = testRestTemplate.exchange(peticion, Void.class);


            // Verificamos que la respuesta no sea nula
            assertThat(respuesta).isNotNull();


            // Verificamos que el código de estado de la respuesta sea 404 (Not Found)
            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }




       
        @Test
        @DisplayName("elimina una dieta ")
        public void eliminarDietaExitosa() {
            try {
                // Preparar la solicitud DELETE para eliminar una dieta existente
                RequestEntity<Void> request = delete("http", "localhost", port, "/dieta/1");
   
                // Realizar la solicitud
                ResponseEntity<Void> response = testRestTemplate.exchange(request, Void.class);
   
                // Verificar que se devuelve un código 200 OK
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(dietaRepo.count()).isEqualTo(0);
   
                // Verificar que la dieta se ha eliminado correctamente de la base de datos (opcional)
                // Aquí puedes agregar código para verificar que la dieta ya no existe en la base de datos
            } catch (Exception e) {
                // Manejar cualquier excepción inesperada
                fail("Se produjo una excepción: " + e.getMessage());
            }
        }


        @Test
        public void eliminarDietaBadRequest() {
            try {
                // Preparar la solicitud DELETE con un ID de dieta no válido
                Long idDietaInvalido = null; // ID de dieta no válido
                RequestEntity<Void> request = delete("http", "localhost", port, "/dieta/" + idDietaInvalido);
   
                // Realizar la solicitud
                ResponseEntity<Void> response = testRestTemplate.exchange(request, Void.class);
   
                // Verificar que se devuelve un código 400 Bad Request
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                // Manejar cualquier excepción inesperada
                fail("Se produjo una excepción: " + e.getMessage());
            }
        }




        @Test
        public void eliminarDietaAccesoNoAutorizado() {
            try {
                // Preparar la solicitud DELETE para eliminar una dieta sin permisos adecuados (por ejemplo, no es el entrenador que la creó)
                Long idDieta = 1L; // ID de la dieta a eliminar
                RequestEntity<Void> request = delete("http", "localhost", port, "/dieta/" + idDieta);
   
                // Realizar la solicitud
                ResponseEntity<Void> response = testRestTemplate.exchange(request, Void.class);
   
                // Si la solicitud tiene éxito, debería lanzar una excepción HttpClientErrorException.Forbidden
                fail("Se esperaba una excepción HttpClientErrorException.Forbidden");
            } catch (HttpClientErrorException.Forbidden e) {
                // Verificar que se devuelve un código 403 Forbidden
                assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                // Manejar cualquier excepción inesperada
                fail("Se produjo una excepción: " + e.getMessage());
            }
        }


        @Test
        @DisplayName("error al intentar actualizar una dieta con datos inválidos")
        public void actualizarDietaBadRequest() {
            DietaDTO dietaDTO = DietaDTO.builder()
                    .id(1L)
                    .descripcion("Descripción actualizada")
                    .build();


            // Realizar la solicitud de actualización
            RequestEntity<DietaDTO> request = put("http", "localhost", port, "/dieta/" + "pepe" , dietaDTO);
            ResponseEntity<Void> response = testRestTemplate.exchange(request, Void.class);


            // Verificar que se devuelve un código 400 Bad Request
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }


        @Test
        @DisplayName("error de acceso no autorizado al intentar actualizar una dieta")
        public void actualizarDietaAccesoNoAutorizado() {
            // Preparar una solicitud de actualización de dieta sin permisos
            DietaDTO dietaDTO = DietaDTO.builder()
                    .id(1L)
                    .nombre("Dieta Actualizada")
                    .descripcion("Descripción actualizada")
                    .build();


            try {
                RequestEntity<DietaDTO> request = put("http", "localhost", port, "/dieta/1", dietaDTO);
   
                // Realizar la solicitud
                ResponseEntity<Void> response = testRestTemplate.exchange(request, Void.class);
   
                // Si la solicitud tiene éxito, debería lanzar una excepción HttpClientErrorException.Forbidden
                fail("Se esperaba una excepción HttpClientErrorException.Forbidden");
            } catch (HttpClientErrorException.Forbidden e) {
                // Verificar que se devuelve un código 403 Forbidden
                assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                // Manejar cualquier excepción inesperada
                fail("Se produjo una excepción: " + e.getMessage());
            }
        }


        @Test
        @DisplayName("error al intentar obtener una dieta con datos inválidos")
        public void obtenerDietaBadRequest() {
            // Preparar la solicitud GET para obtener una dieta con ID inválido
            RequestEntity<Void> request = get("http", "localhost", port, "/dieta/abc");


            // Realizar la solicitud
            ResponseEntity<DietaDTO> response = testRestTemplate.exchange(request, new ParameterizedTypeReference<DietaDTO>() {});


            // Verificar que se devuelve un código 400 Bad Request
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }


        @Test
        @DisplayName("error de acceso no autorizado al intentar obtener una dieta")
        public void obtenerDietaAccesoNoAutorizado() {
            try {
                RequestEntity<Void> request = get("http", "localhost", port, "/dieta/1");
   
                // Realizar la solicitud
                ResponseEntity<DietaDTO> response = testRestTemplate.exchange(request, new ParameterizedTypeReference<DietaDTO>() {});
   
                // Si la solicitud tiene éxito, debería lanzar una excepción HttpClientErrorException.Forbidden
                fail("Se esperaba una excepción HttpClientErrorException.Forbidden");
            } catch (HttpClientErrorException.Forbidden e) {
                // Verificar que se devuelve un código 403 Forbidden
                assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                // Manejar cualquier excepción inesperada
                fail("Se produjo una excepción: " + e.getMessage());
            }
        }

	
	/*@Test
		@DisplayName("obtiene una dieta concreta")
		public void cogeDietaConcretaCorrectamente() throws JsonProcessingException {
			var peticion = "http://localhost:" + port + "/usuario";
        	HttpHeaders headers = new HttpHeaders();
        	headers.set("Authorization", "Bearer " + token);

        	List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        	UsuarioDTO user = new UsuarioDTO(1L, "testuser");
        	usuariosDTO.add(user);

        	ObjectMapper objectMapper = new ObjectMapper();
        	String usuariosJson = objectMapper.writeValueAsString(usuariosDTO);
		
        	mockServer.expect(requestTo(peticion))
			.andExpect(method(HttpMethod.GET))
			.andExpect(header("Authorization", "Bearer " + token))
			.andRespond(withSuccess(usuariosJson, MediaType.APPLICATION_JSON));
		
        	peticion = "http://localhost:" + port + "/entrenador?centro=0";
		
        	List<EntrenadorDTO> entrenadoresDTO = new ArrayList<>();
        	EntrenadorDTO entrenadorDTO = new EntrenadorDTO(1L);
        	entrenadoresDTO.add(entrenadorDTO);
		
        	String entrenadoresJson = objectMapper.writeValueAsString(entrenadoresDTO);
		
			mockServer.expect(requestTo(peticion))
			.andExpect(method(HttpMethod.GET))
			.andExpect(header("Authorization", "Bearer " + token))
			.andRespond(withSuccess(entrenadoresJson, MediaType.APPLICATION_JSON));
		
	    	peticion = "http://localhost:" + port + "/dieta/";
			HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		
			// Realizar la solicitud GET y verificar el resultado
			ResponseEntity<DietaDTO> respuesta = restTemplateTest.exchange(peticion, HttpMethod.GET, requestEntity,new ParameterizedTypeReference<DietaDTO>() {});

			mockServer.verify();

			assertThat(respuesta.getStatusCode().equals(HttpStatus.OK));
        	assertThat(respuesta.getBody().size()).isEqualTo(1);

		}*/

		/*


		@Test
            @DisplayName("asocia una dieta a un cliente")
            public void asociaDietaAClienteCorrectamente() throws JsonProcessingException {
                var peticion = "http://localhost/:" + port + "/dieta";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + token);

                Map<String, Long> requestBody = new HashMap<>();
                requestBody.put("dietaId", 1L);
                requestBody.put("clienteId", 1L);

                ObjectMapper objectMapper = new ObjectMapper();
                String requestJson = objectMapper.writeValueAsString(requestBody);

                mockServer.expect(requestTo(peticion))
                        .andExpect(method(HttpMethod.PUT))
                        .andExpect(header("Authorization", "Bearer " + token))
                        .andExpect(content().json(requestJson))
                        .andRespond(withSuccess());

                HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);

                ResponseEntity<Void> respuesta = testRestTemplate.exchange(peticion, HttpMethod.PUT, requestEntity, Void.class);

                mockServer.verify();

                assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
            }
        }
		 */

	}
}
