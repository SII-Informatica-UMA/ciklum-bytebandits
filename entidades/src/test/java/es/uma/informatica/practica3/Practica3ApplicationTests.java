package es.uma.informatica.practica3;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import es.uma.informatica.practica3.controllers.Mapper;
import es.uma.informatica.practica3.dtos.DietaDTO;
import es.uma.informatica.practica3.dtos.DietaNuevaDTO;
import es.uma.informatica.practica3.entities.Dieta;
import es.uma.informatica.practica3.repositories.DietaRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de productos")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class Practica3ApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private DietaRepository dietaRepo;


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

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	/*private void compruebaCampos(Ingrediente expected, Ingrediente actual) {
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}

	private void compruebaCampos(Producto expected, Producto actual) {	
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
		assertThat(actual.getDescripcion()).isEqualTo(expected.getDescripcion());
		assertThat(actual.getIngredientes()).isEqualTo(expected.getIngredientes());
	}*/

	@Nested
	@DisplayName("cuando la base de datos esta vacia")
	public class BaseDatosVacia {

		@Test
		@DisplayName("devuelve la lista de dietas vacia")
		public void devuelveLista() {
			
			var peticion = get("http", "localhost",port, "/dieta");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<DietaDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
		}

		@Test
		@DisplayName("error al obtener una dieta concreta")
		public void errorAlObtenerDietaConcreta() {
			var peticion = get("http", "localhost", port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<DietaDTO>() {
					});

			int statusCode = respuesta.getStatusCode().value();

			assertThat(statusCode).isEqualTo(404);
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
			
			var peticion = put("http", "localhost",port, "/dieta/1", dieta);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			int statusCode = respuesta.getStatusCode().value();

			assertThat(statusCode).isEqualTo(404);
		}


	
	}

	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosConDatos {
		private DietaNuevaDTO d1 = DietaNuevaDTO.builder()
			.nombre("Dieta1")
			.descripcion("a")
			.observaciones("b")
			.objetivo("c")
			.alimentos(Arrays.asList("alimento1", "alimento2"))
			.recomendaciones("d")
			.build();

		private DietaNuevaDTO d2 = DietaNuevaDTO.builder()
			.nombre("Dieta2")
			.descripcion("a")
			.observaciones("b")
			.objetivo("c")
			.alimentos(Arrays.asList("alimento1", "alimento2"))
			.recomendaciones("d")
			.build();


		@BeforeEach
		public void introduceDatos() {
			dietaRepo.save(Mapper.toDieta(d1));
			dietaRepo.save(Mapper.toDieta(d2));
		}

		/*@Test
		@DisplayName("devuelve la lista de dietas correctamente")
		public void devuelveLista() {
			var peticion = get("http", "localhost",port, "/dieta");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<DietaDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}*/
	}
}
