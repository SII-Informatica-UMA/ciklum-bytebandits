package es.uma.informatica.practica3.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UsuarioDTO {
	private Long id;
	private String nombre;

}
