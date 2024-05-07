package ciklumBytebandits.dtos;

import java.util.ArrayList;

import ciklumBytebandits.entidades.Dieta;
import lombok.Builder;

@Builder
public class DietaNuevaDto {
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String objetivo;
    private Integer duracionDias;
    private ArrayList<String> alimentos;
    private String recomendaciones;

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public void setObservaciones(final String observaciones) {
        this.observaciones = observaciones;
    }

    public void setObjetivo(final String objetivo) {
        this.objetivo = objetivo;
    }

    public void setDuracionDias(final Integer duracionDias) {
        this.duracionDias = duracionDias;
    }

    public void setAlimentos(final ArrayList<String> alimentos) {
        this.alimentos = alimentos;
    }

    public void setRecomendaciones(final String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public DietaNuevaDto(final String nombre, final String descripcion, final String observaciones, final String objetivo, final Integer duracionDias, final ArrayList<String> alimentos, final String recomendaciones) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.objetivo = objetivo;
        this.duracionDias = duracionDias;
        this.alimentos = alimentos;
        this.recomendaciones = recomendaciones;
    }

    public static class DietaNuevaDtoBuilder {
        private String nombre;
        private String descripcion;
        private String observaciones;
        private String objetivo;
        private Integer duracionDias;
        private ArrayList<String> alimentos;
        private String recomendaciones;

        DietaNuevaDtoBuilder() {
        }

        public DietaNuevaDtoBuilder nombre(final String nombre) {
            this.nombre = nombre;
            return this;
        }

        public DietaNuevaDtoBuilder descripcion(final String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public DietaNuevaDtoBuilder observaciones(final String observaciones) {
            this.observaciones = observaciones;
            return this;
        }

        public DietaNuevaDtoBuilder objetivo(final String objetivo) {
            this.objetivo = objetivo;
            return this;
        }

        public DietaNuevaDtoBuilder duracionDias(final Integer duracionDias) {
            this.duracionDias = duracionDias;
            return this;
        }

        public DietaNuevaDtoBuilder alimentos(final ArrayList<String> alimentos) {
            this.alimentos = alimentos;
            return this;
        }

        public DietaNuevaDtoBuilder recomendaciones(final String recomendaciones) {
            this.recomendaciones = recomendaciones;
            return this;
        }

        public DietaNuevaDto build() {
            return new DietaNuevaDto(this.nombre, this.descripcion, this.observaciones, this.objetivo, this.duracionDias, this.alimentos, this.recomendaciones);
        }

        public String toString() {
            return "DietaNuevaDto.DietaNuevaDtoBuilder(nombre=" + this.nombre + ", descripcion=" + this.descripcion + ", observaciones=" + this.observaciones + ", objetivo=" + this.objetivo + ", duracionDias=" + this.duracionDias + ", alimentos=" + String.valueOf(this.alimentos) + ", recomendaciones=" + this.recomendaciones + ")";
        }
    }

    public DietaNuevaDto() {
    }

    public static DietaNuevaDtoBuilder otroBuilder() {
        return new DietaNuevaDtoBuilder();
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public String getObjetivo() {
        return this.objetivo;
    }

    public Integer getDuracionDias() {
        return this.duracionDias;
    }

    public ArrayList<String> getAlimentos() {
        return this.alimentos;
    }

    public String getRecomendaciones() {
        return this.recomendaciones;
    }

    public Dieta toEntity() {
        return Dieta.builder().nombre(this.nombre).descripcion(this.descripcion).observaciones(this.observaciones).objetivo(this.objetivo).duracionDias(this.duracionDias).alimentos(this.alimentos).recomendaciones(this.recomendaciones).build();
    }
}
