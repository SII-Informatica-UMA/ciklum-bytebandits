package es.uma.informatica.practica3.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "builderNuevo")
public class ClienteNuevoDTO {
    private Long idUsuario;
    private String direccion;
    private String dni;
    private String fechaNacimiento;
    private String sexo;
}
