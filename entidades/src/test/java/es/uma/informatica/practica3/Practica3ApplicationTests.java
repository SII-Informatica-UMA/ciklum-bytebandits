package es.uma.informatica.practica3;


import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.uma.informatica.practica3.controllers.GestionDietas;
import es.uma.informatica.practica3.controllers.Mapper;
import es.uma.informatica.practica3.dtos.DietaDTO;
import es.uma.informatica.practica3.dtos.DietaNuevaDTO;
import es.uma.informatica.practica3.entities.Dieta;
import es.uma.informatica.practica3.repositories.DietaRepository;
import es.uma.informatica.practica3.security.JwtUtil;
import es.uma.informatica.practica3.services.DietaService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de dietas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
class Practica3ApplicationTests {
@Autowired
	private TestRestTemplate restTemplate;

	@Mock
	private RestTemplate restMock;

	@Value(value="${local.server.port}")
	private int port;

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

	@BeforeEach
	public void initializeDatabase() {
		token = jwtUtil.generateToken("1");
		dietaRepo.deleteAll();
	}

	private URI uri(String scheme, String host, int port, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}
	public URI uriQuery(String scheme, String host, int port, String query, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		ub = ub.query("plan=1");
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
				.header("Authorization", "Bearer " + token)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
				.header("Authorization", "Bearer " + token)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}
	

	@Nested
	@DisplayName("cuando la base de datos esta vacia")
	public class BaseDatosVacia {

		@Test
		@DisplayName("lanza error cuando se llama a delete y la dieta no existe")
		public void errorDeleteSesion() {
			var peticion = delete("http","localhost",port,"/dieta/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("lanza error cuando se llama a get y la dieta no existe")
		public void errorGetSesion() {
			var peticion = get("http","localhost",port,"/dieta/1");
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<DietaDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("lanza error cuando se llama a put y la dieta no existe")
		public void errorPutSesion() {
			DietaDTO dietaDTO = DietaDTO.builder().build();
			var peticion = put("http","localhost",port,"/dieta/1", dietaDTO);
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<DietaDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

	
	}

	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosConDatos {
		
	}
}
