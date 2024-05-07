package ciklumBytebandits.servicios;

import ciklumBytebandits.entidades.Dieta;
import ciklumBytebandits.excepciones.DietaException;
import ciklumBytebandits.repositories.DietaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DietaService {
    private DietaRepository dietaRepo;

    public DietaService(DietaRepository dietaRepository) {
        this.dietaRepo = dietaRepository;
    }

    public List<Dieta> dietasDeEntrenador(Long entrenadorId) {
        return this.dietaRepo.findByEntrenadorId(entrenadorId);
    }

    public List<Dieta> dietasDeCliente(Long clienteId) {
        return this.dietaRepo.findByClienteId(clienteId);
    }

    public Dieta crearActualizarDieta(Dieta g) {
        return (Dieta)this.dietaRepo.save(g);
    }

    public Optional<Dieta> obtenerDieta(Long id) {
        return this.dietaRepo.findById(id);
    }

    public void asociarDieta(Long dietaID, Long clienteId) {
        Optional<Dieta> d = this.obtenerDieta(dietaID);
        d.ifPresent((dieta) -> {
            dieta.getId().add(clienteId);
            this.dietaRepo.save(dieta);
        });
        d.orElseThrow(DietaException::new);
    }

    public void eliminarDieta(Long id) {
        this.dietaRepo.deleteById(id);
    }
}

