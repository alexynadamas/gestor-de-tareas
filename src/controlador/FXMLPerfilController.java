/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.Conexion;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Alex Clemente < DAW >
 */
public class FXMLPerfilController implements Initializable {

    Usuario usuario; // Almacena el usuario recibido desde otra ventana

    @FXML
    private Label labelRol;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelApellidos;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelUsuario;
    @FXML
    private Label labelId;
    @FXML
    private Button buttonCambiarContrasenya;
    @FXML
    private Pane panelCambiarContrasenya;
    @FXML
    private PasswordField nuevaContrasenya;
    @FXML
    private PasswordField confirmarContrasenya;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Hace visible las opciones para introducir una nueva contraseña
     *
     * @param event se lanza con el botón 'Cambiar contraseña'
     */
    @FXML
    private void activarPanelCambiarContraseña(ActionEvent event) {
        panelCambiarContrasenya.setVisible(true);
        buttonCambiarContrasenya.setVisible(false);
    }

    /**
     * Cambia la contraseña del usuario
     *
     * @param event se lanza con el botón 'guardar'
     */
    @FXML
    private void guardarNuevaContraseña(ActionEvent event) {
        if (nuevaContrasenya.getText().equals(confirmarContrasenya.getText())) {
            Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
            try {
                PreparedStatement consulta = con.prepareStatement("UPDATE usuarios SET contrasenya = ? WHERE id = ?;");
                String contrasenya = Contrasenya.cifrarContrasenya(nuevaContrasenya.getText()); // Se cifra la contraseña
                consulta.setString(1, contrasenya);
                consulta.setInt(2, usuario.getId());
                consulta.executeUpdate();
                ocultarPanelnuevaContrasenya();
                Dialogo.informacion("Información", null, "Contraseña actualiza correctamente.");
            } catch (SQLException e) {
                Dialogo.error("Error", null, "Error al insertar en la base de datos.");
            } catch (NoSuchAlgorithmException ex) {
                Dialogo.error("Error", null, "Error al cifrar la contraseña.");
            }
            Conexion.desconectar(con);
        } else {
            Dialogo.error("Error", null, "Las contraseñas no coinciden.");
        }
    }

    /**
     * Llama al método ocultarPanelnuevaContrasenya()
     *
     * @param event se lanza con el botón 'Cancelar'
     */
    @FXML
    private void cancelarCambioContraseña(ActionEvent event) {
        ocultarPanelnuevaContrasenya();
    }

    /**
     * Oculta las opciones para introducir una nueva contraseña
     *
     */
    private void ocultarPanelnuevaContrasenya() {
        nuevaContrasenya.setText(null);
        confirmarContrasenya.setText(null);
        panelCambiarContrasenya.setVisible(false);
        buttonCambiarContrasenya.setVisible(true);

    }

    /**
     * Recibe y almacena el usuario desde la ventana padre. Carga los datos de
     * este usuario
     *
     * @param usuario
     */
    void initStage(Usuario usuario) {
        this.usuario = usuario;

        labelId.setText(String.valueOf(usuario.getId()));
        labelUsuario.setText(usuario.getUsuario());
        labelNombre.setText(usuario.getNombre());
        labelApellidos.setText(usuario.getApellidos());
        labelEmail.setText(usuario.getEmail());
        labelRol.setText(usuario.getRol());
    }

}
