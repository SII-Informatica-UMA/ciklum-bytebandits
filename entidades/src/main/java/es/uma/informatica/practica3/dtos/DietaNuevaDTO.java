package es.uma.informatica.practica3.dtos;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class DietaNuevaDTO {
   private String nombre;
   private String descripcion;
   private String observaciones;
   private String objetivo;
   private Integer duracionDias;
   private List<String> alimentos;
   private String recomendaciones;

}

