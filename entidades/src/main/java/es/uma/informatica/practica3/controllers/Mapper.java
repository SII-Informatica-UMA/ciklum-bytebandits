package es.uma.informatica.practica3.controllers;

import es.uma.informatica.practica3.dtos.DietaDTO;
import es.uma.informatica.practica3.dtos.DietaNuevaDTO;
import es.uma.informatica.practica3.entities.Dieta;

public class Mapper {
    public static DietaDTO toDietaDTO(Dieta dieta) {
        return DietaDTO.builder()
            .id(dieta.getId())
            .build();
    }

    public static Dieta toDieta(DietaNuevaDTO dietaNuevaDTO) {
        return es.uma.informatica.practica3.entities.Dieta.builder()
            .nombre(dietaNuevaDTO.getNombre())
            .descripcion(dietaNuevaDTO.getDescripcion())
            .observaciones(dietaNuevaDTO.getObservaciones())
            .objetivo(dietaNuevaDTO.getObjetivo())
            .duracionDias(dietaNuevaDTO.getDuracionDias())
            .alimentos(dietaNuevaDTO.getAlimentos())
            .recomendaciones(dietaNuevaDTO.getRecomendaciones())
            .build();
    }

    public static Dieta toDieta2(DietaDTO dietaDTO) {
        return es.uma.informatica.practica3.entities.Dieta.builder()
            .id(dietaDTO.getId())
            .build();
    }
}
