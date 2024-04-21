package es.uma.informatica.sii.spring.jpa.demo.entities;

import java.util.Objects;
import java.util.ArrayList;

public class Dieta {
    
    private String nombre;
    private String descripcion;
    private String observaciones;
    private String objetivo;
    private int duracionDias;
    private ArrayList<String> alimentos;
    private String recomendaciones;
    private int id;
    private int usuarioId;

    public Dieta(String nombre, String descripcion, String observaciones, String objetivo, int durDias, ArrayList<String> alimentos, String recomendaciones, int id, int usuarioId, int creadorId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.objetivo = objetivo;
        this.duracionDias = durDias;
        this.alimentos = alimentos;
        this.recomendaciones = recomendaciones;
        this.id = id;
        this.usuarioId = usuarioId;
    }

    public String getNombre(){
        return nombre;
    }
    
    public String getDescripcion(){
        return descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public ArrayList<String> getAlimentos() {
        return alimentos;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public void setAlimentos(ArrayList<String> alimentos) {
        this.alimentos = alimentos;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dieta dieta = (Dieta) o;
        return duracionDias == dieta.duracionDias &&
                id == dieta.id &&
                usuarioId == dieta.usuarioId &&
                Objects.equals(nombre, dieta.nombre) &&
                Objects.equals(descripcion, dieta.descripcion) &&
                Objects.equals(observaciones, dieta.observaciones) &&
                Objects.equals(objetivo, dieta.objetivo) &&
                Objects.equals(alimentos, dieta.alimentos) &&
                Objects.equals(recomendaciones, dieta.recomendaciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcion, observaciones, objetivo, duracionDias, alimentos, recomendaciones, id, usuarioId);
    }

    @Override
    public String toString() {
        return "Dieta{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", duracionDias=" + duracionDias +
                ", alimentos=" + alimentos +
                ", recomendaciones='" + recomendaciones + '\'' +
                ", id=" + id +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
