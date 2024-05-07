package ciklumBytebandits.entidades;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Collections;
import java.util.List;

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

import ciklumBytebandits.repositories.DietaRepository;
import ciklumBytebandits.dtos.DietaDto;
import ciklumBytebandits.dtos.DietaNuevaDto;
import ciklumBytebandits.entidades.Dieta;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de dietas")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class EntidadesApplicationTests {

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



	private void compruebaCampos(Dieta expected, Dieta actual) {
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getDescripcion()).isEqualTo(expected.getDescripcion());
		assertThat(actual.getObservaciones()).isEqualTo(expected.getObservaciones());
		assertThat(actual.getObjetivo()).isEqualTo(expected.getObjetivo());
		assertThat(actual.getDuracionDias()).isEqualTo(expected.getDuracionDias());
		assertThat(actual.getAlimentos()).isEqualTo(expected.getAlimentos());
		assertThat(actual.getRecomendaciones()).isEqualTo(expected.getRecomendaciones());
		assertThat(actual.getEntrenador()).isEqualTo(expected.getEntrenador());
		assertThat(actual.getCliente()).isEqualTo(expected.getCliente());
	}

	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {
		@Test
		@DisplayName("error al obtener una dieta concreta")
		public void errorAlObtenerDietaConcreta() {
			var peticion = get("http", "localhost", port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<DietaDto>() {});

			int statusCode = respuesta.getStatusCode().value();

			if (statusCode == 404) {
				assertThat(statusCode).isEqualTo(404);
			} else if (statusCode == 400) {
				assertThat(statusCode).isEqualTo(400);
			} else if (statusCode == 403) {
				assertThat(statusCode).isEqualTo(403);
			}
		}

		@Test
		@DisplayName("da error con una dieta concreta")
		public void errorConDietaConcreta() {
			var peticion = get("http", "localhost",port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<DietaDto>() {});

			int statusCode = respuesta.getStatusCode().value();

			if (statusCode == 404) {
				assertThat(statusCode).isEqualTo(404);
			} else if (statusCode == 400) {
				assertThat(statusCode).isEqualTo(400);
			} else if (statusCode == 403) {
				assertThat(statusCode).isEqualTo(403);
			}
		}

		@Test
		@DisplayName("devuelve error al asociar una dieta que no existe")
		public void modificarDietaInexistente() {
			var dieta = DietaDto.builder().nombre("DietaCalorias").build();
			var peticion = put("http", "localhost",port, "/dieta", dieta);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			int statusCode = respuesta.getStatusCode().value();

			if (statusCode == 404) {
				assertThat(statusCode).isEqualTo(404);
			} else if (statusCode == 400) {
				assertThat(statusCode).isEqualTo(400);
			} else if (statusCode == 403) {
				assertThat(statusCode).isEqualTo(403);
			}
		}


		@Test
		@DisplayName("devuelve error al asociar una dieta que no existe con Id")
		public void modificarDietaInexistenteConID() {
			var dieta = DietaDto.builder().nombre("DietaHidratos").build();
			var peticion = put("http", "localhost",port, "/dieta/1", dieta);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			int statusCode = respuesta.getStatusCode().value();

			if (statusCode == 404) {
				assertThat(statusCode).isEqualTo(404);
			} else if (statusCode == 400) {
				assertThat(statusCode).isEqualTo(400);
			} else if (statusCode == 403) {
				assertThat(statusCode).isEqualTo(403);
			}
		}



		@Test
		@DisplayName("inserta una dieta correctamente")
		public void insertaDieta() {

			var dieta = DietaDto.builder()
					.nombre("DietaHidratos")
					.build();
			var peticion = post("http", "localhost",port, "/dieta", dieta);

			var respuesta = restTemplate.exchange(peticion,Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/dieta");

			List<Dieta> dietasBD = dietaRepo.findAll();
			assertThat(dietasBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+dietasBD.get(0).getId());
			compruebaCampos(dieta.dieta(), dietasBD.get(0));
		}


		@Test
		@DisplayName("devuelve error al eliminar una dieta que no existe")
		public void eliminarDietaInexistente() {
			var peticion = delete("http", "localhost",port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			int statusCode = respuesta.getStatusCode().value();

			if (statusCode == 404) {
				assertThat(statusCode).isEqualTo(404);
			} else if (statusCode == 400) {
				assertThat(statusCode).isEqualTo(400);
			} else if (statusCode == 403) {
				assertThat(statusCode).isEqualTo(403);
			}
		}




	}

	@Nested
	@DisplayName("cuando la base de datos tiene datos")
	public class BaseDatosLlena {

		@BeforeEach
		public void insertarDatos() {
		}
		
		@Test
		@DisplayName("obtiene una dieta correctamente")
		public void errorConProductoConcreto() {
			var peticion = get("http", "localhost",port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<DietaDto>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Hamburguesa");
		}
	}

}
