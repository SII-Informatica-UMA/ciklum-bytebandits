package ciklumBytebandits.entidades;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ciklumBytebandits.entidades.entities.Dieta;
import ciklumBytebandits.entidades.repositories.DietaRepository;

@Component
public class LineaComandos implements CommandLineRunner {
	private DietaRepository repository;
	public LineaComandos(DietaRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		for (String s: args) {
			System.out.println(s);
		}

		if (args.length > 0) {
			for (Dieta b: repository.findByNombre(args[0])) {
				System.out.println(b);
			}
		}
	}

}
