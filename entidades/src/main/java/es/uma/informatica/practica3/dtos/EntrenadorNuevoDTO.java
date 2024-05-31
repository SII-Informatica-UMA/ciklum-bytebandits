package es.uma.informatica.practica3.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "builderNuevo")
public class EntrenadorNuevoDTO {
    private Long idUsuario;
    private String telefono;
    private String direccion;	
    private String dni;
    private String fechaNacimiento;	
    private String fechaAlta;
    private String especialidad;
    private String titulacion;	
    private String experiencia;	
    private String observaciones;
}
