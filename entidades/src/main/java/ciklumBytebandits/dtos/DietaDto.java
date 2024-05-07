package ciklumBytebandits.dtos;

import ciklumBytebandits.entidades.Dieta;

public class DietaDto extends DietaNuevaDto{
    private Long id;

    public void setId(final Long id) {
        this.id = id;
    }

    public DietaDto() {
    }

    public Long getId() {
        return this.id;
    }
    public static class DietaDtoBuilder {
        private Long id;
        private String nombre;
        private String descripcion;
        private String observaciones;
        private String objetivo;
        private Integer duracionDias;
        private String recomendaciones;

        DietaDtoBuilder() {
        }

        public DietaDtoBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public DietaDtoBuilder nombre(final String nombre) {
            this.nombre = nombre;
            return this;
        }

        public DietaDtoBuilder descripcion(final String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public DietaDtoBuilder observaciones(final String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        public DietaDtoBuilder objetivo(final String objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public DietaDtoBuilder duracionDias(final Integer duracionDias) {
            this.duracionDias = duracionDias;
            return this;
        }

        public DietaDtoBuilder recomendaciones(final String recomendaciones) {
            this.recomendaciones = recomendaciones;
            return this;
        }

        public DietaDto build() {
            return new DietaDto(this.id, this.nombre, this.descripcion, this.observaciones, this.objetivo, this.duracionDias, this.recomendaciones);
        }

        public String toString() {
            return "DietaDto.DietaDtoBuilder(id=" + this.id + ", nombre=" + this.nombre + ", descripcion=" + this.descripcion + ", observaciones=" + this.observaciones + ", objetivo=" + this.objetivo + ", duracionDias=" + this.duracionDias + ", recomendaciones=" + this.recomendaciones + ")";
        }
    }

    public static DietaDtoBuilder builder() {
        return new DietaDtoBuilder();
    }

    public DietaDto(Long id, String nombre, String descripcion, String observaciones, String objetivo, Integer duracionDias, String recomendaciones) {
        super(nombre, descripcion, observaciones, objetivo, duracionDias, null, recomendaciones);
        this.id = id;
    }

    @Override
    public Dieta toEntity() {
        return Dieta.builder().id(this.id).nombre(getNombre()).descripcion(getDescripcion()).observaciones(getObservaciones()).objetivo(getObjetivo()).duracionDias(getDuracionDias()).alimentos(getAlimentos()).recomendaciones(getRecomendaciones()).build();
    }

    public static DietaDto fromEntity(Dieta dieta) {
        return builder().id(dieta.getId()).nombre(dieta.getNombre()).descripcion(dieta.getDescripcion()).observaciones(dieta.getObservaciones()).objetivo(dieta.getObjetivo()).duracionDias(dieta.getDuracionDias()).recomendaciones(dieta.getRecomendaciones()).build();
    }
}

