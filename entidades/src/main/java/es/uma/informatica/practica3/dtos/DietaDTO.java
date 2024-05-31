package es.uma.informatica.practica3.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DietaDTO {
   private Long id;
   private String nombre;
   private String descripcion;
   private Long idEntrenador;

}
