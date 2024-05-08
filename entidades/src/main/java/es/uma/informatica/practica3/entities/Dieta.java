package es.uma.informatica.practica3.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.List;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
public class Dieta {
   @Id
   @GeneratedValue
   private Long id;
   private String nombre;
   private String descripcion;
   private String observaciones;
   private String objetivo;
   private Integer duracionDias;
   @ElementCollection
   private List<String> alimentos;
   private String recomendaciones;
   private Long idEntrenador;
   @ElementCollection
   private Set<Long> idClientes;

   
}
