/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 * Representa un usuario del sistema y sus atributos
 *
 * @author Álex Torres
 */
public class Usuario {

    private int id;
    private String usuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String rol;

    /**
     * Constructor con todos los parámetros
     *
     * @param id
     * @param usuario
     * @param nombre
     * @param apellidos
     * @param email
     * @param rol
     */
    public Usuario(int id, String usuario, String nombre, String apellidos, String email, String rol) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
    }

    /**
     * Constructor sin id
     *
     * @param usuario
     * @param nombre
     * @param apellidos
     * @param email
     * @param rol
     */
    public Usuario(String usuario, String nombre, String apellidos, String email, String rol) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Compara una usuario con otro
     *
     * @param user
     * @return booleano indicando si son iguales
     */
    public boolean equals(Usuario user) {
        if (!user.getUsuario().equals(usuario)) {
            return false;
        }
        if (!user.getNombre().equals(nombre)) {
            return false;
        }
        if (!user.getApellidos().equals(apellidos)) {
            return false;
        }
        if (!user.getEmail().equals(email)) {
            return false;
        }
        if (!user.getRol().equals(rol)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", usuario=" + usuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email + ", rol=" + rol + '}';
    }

}
