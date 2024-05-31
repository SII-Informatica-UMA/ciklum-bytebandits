package es.uma.informatica.practica3.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EntrenadorDTO {
    private Long id;
    private Long idUsuario;
    private String telefono;
}
