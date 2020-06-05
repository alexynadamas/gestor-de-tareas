/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Objects;

/**
 * Representa una tarea y sus propiedadaes
 *
 * @author Alex Clemente < DAW >
 */
public class Tarea {

    private int id;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private String estado;
    private String creador;
    private String asignacion;
    private int progreso;

    /**
     * Constructor con todos los parámetros
     *
     * @param id
     * @param titulo
     * @param descripcion
     * @param prioridad
     * @param estado
     * @param creador
     * @param asignacion
     * @param progreso
     */
    public Tarea(int id, String titulo, String descripcion, String prioridad, String estado, String creador, String asignacion, int progreso) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.creador = creador;
        this.asignacion = asignacion;
        this.progreso = progreso;
    }

    /**
     * Constructor sin id
     *
     * @param titulo
     * @param descripcion
     * @param prioridad
     * @param estado
     * @param creador
     * @param asignacion
     * @param progreso
     */
    public Tarea(String titulo, String descripcion, String prioridad, String estado, String creador, String asignacion, int progreso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.creador = creador;
        this.asignacion = asignacion;
        this.progreso = progreso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(String asignacion) {
        this.asignacion = asignacion;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    /**
     * Compara una tarea con otra
     *
     * @param tarea
     * @return booleano indicando si son iguales
     */
    public boolean equals(Tarea tarea) {
        if (!tarea.getTitulo().equals(titulo)) {
            return false;
        }
        if (!tarea.getDescripcion().equals(descripcion)) {
            return false;
        }
        if (!tarea.getPrioridad().equals(prioridad)) {
            return false;
        }
        if (!tarea.getEstado().equals(estado)) {
            return false;
        }
        if (!tarea.getCreador().equals(creador)) {
            return false;
        }
        if (!tarea.getAsignacion().equals(asignacion)) {
            return false;
        }
        if (!(tarea.getProgreso() == progreso)) {
            return false;
        }
        return true;
    }

}
