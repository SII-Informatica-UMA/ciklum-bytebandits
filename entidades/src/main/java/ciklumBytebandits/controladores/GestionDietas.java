package ciklumBytebandits.controladores;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import ciklumBytebandits.entidades.Dieta;
import ciklumBytebandits.servicios.DietaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/dieta"})
@RestController
@CrossOrigin
@Tag(name = "Gestiona dietas y las asigna", description = "Funciones para gestionar dietas y asociarlas a clientes")

public class GestionDietas {
    private DietaService dietasService;

    public GestionDietas(DietaService dietasService) {
        this.dietasService = dietasService;
    }

    @GetMapping
    @Operation(description = "Consulta las dietas de un entrenador o las asociadas a un cliente. Se debe especificar si estamos consultando las dietas de un entrenador o un cliente",
            responses = {@ApiResponse(responseCode = "200", description = "Devuelve las dietas de un entrenador o la dieta asociada a un cliente."),
                    @ApiResponse(responseCode = "403", description = "Acceso no autorizado",
                            content = {@Content(schema = @Schema(implementation = Void.class))})})


    public List<DietaDTO> obtenerDietas(@RequestParam(value = "entrenador", required = false) Long entrenadorId, @RequestParam(value = "cliente", required = false) Long clienteId) {

        List<Dieta> dietas;
        if (entrenadorId == null && clienteId == null) {
            throw new IllegalArgumentException("No se ha especificado ningún parámetro para buscar su dieta");
        }
        if (entrenadorId != null && clienteId != null) {
            throw new IllegalArgumentException("Solo se requiere un parámetro de consulta");
        }
        if (clienteId != null) {
            dietas = this.dietasService.dietasDeEntrenador(clienteId);
        } else {
            dietas = this.dietasService.dietasDeCliente(entrenadorId);
        }
        return dietas.stream().map(DietaDTO::fromEntity).toList();
    }

    @PostMapping
    @Operation(description = "El entrenador crea dietas",
            responses = {@ApiResponse(responseCode = "201", description = "Crea la dieta y la muestra",
                    headers = {@Header(name = "Location", description = "URI nuevo",
                            schema = @Schema(type = "string", subTypes = {URI.class}))}),
                    @ApiResponse(responseCode = "403", description = "Acceso no autorizado",
                            content = {@Content(schema = @Schema(implementation = Void.class))})})

    public ResponseEntity<DietaDTO> creaDieta(@RequestParam(value = "entrenador", required = true) Long entrenadorId,
                                              @RequestBody DietaNuevaDTO dietaNuevoDTO, UriComponentsBuilder uriBuilder) {
        Dieta g = dietaNuevoDTO.toEntity();
        g.setId(null);
        g.setIdEntrenador(entrenadorId);

        Dieta g2 = this.dietasService.crearActualizarDieta(g);
        return ResponseEntity.created(generadorUri(uriBuilder.build()).apply(g2)).body(DietaDTO.fromEntity(g2));
    }

    private Function<Dieta, URI> generadorUri(UriComponents uriBuilder) {
        return g -> {
            return UriComponentsBuilder.newInstance().uriComponents(uriBuilder).path("/dieta").path(String.format("/%d", g.getId())).build().toUri();
        };
    }

    @PutMapping
    @Operation(description = "El entrenador asocia dietas a entrenador",
            responses = {@ApiResponse(responseCode = "200",
                    description = "El entrenador le añade una dieta al cliente y si ya habia una la reemplaza"),
                    @ApiResponse(responseCode = "403", description = "Acceso no autorizado",
                            content = {@Content(schema = @Schema(implementation = Void.class))})})

    public ResponseEntity<DietaDTO> asociaDieta(@RequestParam(value = "cliente", required = true) Long clienteId, @RequestBody DietaDTO dietaDTO) {
        this.dietasService.asignaDieta(dietaDTO.getId(), clienteId);
        return ResponseEntity.of(this.dietasService.obtenerDieta(dietaDTO.getId()).map(DietaDTO::fromEntity));
    }



    @PutMapping({"/{idDieta}"})
    @Operation(description = "el entrenador que crea la dieta la acualiza",
            responses = {@ApiResponse(responseCode = "200", description = "La dieta se ha podido actualizar"),
                    @ApiResponse(responseCode = "404", description = "Dieta no existe",
                            content = {@Content(schema = @Schema(implementation = Void.class))}),
                    @ApiResponse(responseCode = "403", description = "Acceso no autorizado",
                            content = {@Content(schema = @Schema(implementation = Void.class))})})

    public DietaDTO actualizaDieta(@PathVariable Long idDieta, @RequestBody DietaDTO dieta) {
        this.dietasService.obtenerDieta(idDieta).orElseThrow(() -> {
            return new DietaException();
        });
        dieta.setId(idDieta);
        Dieta g = this.dietasService.crearActualizarDieta(dieta.toEntity());
        return DietaDTO.fromEntity(g);
    }


    @GetMapping({"/{idDieta}"})
    @Operation(description = " El entrenador que ha creado las dietas obtiene las de sus cleintes.",
            responses = {@ApiResponse(responseCode = "200", description = "Dieta existe"),
                    @ApiResponse(responseCode = "404", description = "Dieta no existe",
                            content = {@Content(schema = @Schema(implementation = Void.class))}),
                    @ApiResponse(responseCode = "403", description = "Acceso no autorizado",
                            content = {@Content(schema = @Schema(implementation = Void.class))})})

    public ResponseEntity<DietaDTO> getDieta(@PathVariable Long idDieta) {
        return ResponseEntity.of(this.dietasService.obtenerDieta(idDieta).map(DietaDTO::fromEntity));
    }

    @DeleteMapping({"/{idDieta}"})
    @Operation(description = "El entrenador que crea la dieta la puede eliminar",
            responses = {@ApiResponse(responseCode = "200", description = "Dieta elminada"),
                    @ApiResponse(responseCode = "404", description = "Dieta no existe",
                            content = {@Content(schema = @Schema(implementation = Void.class))}),
                    @ApiResponse(responseCode = "403", description = "Acceso no autorizado",
                            content = {@Content(schema = @Schema(implementation = Void.class))})})


    public void eliminaDieta(@PathVariable Long idDieta) {
        this.dietasService.obtenerDieta(idDieta).orElseThrow(() -> {
            return new DietaException();
        });
        this.dietasService.eliminaDieta(idDieta);
    }

    @ExceptionHandler({DietaException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void handleDietaNoExisteException() {
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public void handleIllegalArgumentException() {
    }


}

