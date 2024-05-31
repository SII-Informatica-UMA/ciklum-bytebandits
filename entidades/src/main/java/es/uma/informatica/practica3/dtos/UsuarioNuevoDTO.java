package es.uma.informatica.practica3.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "builderNuevo")
public class UsuarioNuevoDTO {
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private String password;
	private Boolean administrador;
}
