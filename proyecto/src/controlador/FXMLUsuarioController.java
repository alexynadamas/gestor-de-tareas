/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Alex Clemente DAW
 */
public class FXMLUsuarioController implements Initializable {

    private Usuario usuario; // Almacena los datos del usuario logeado

    @FXML
    private BorderPane borderPaneFX;
    @FXML
    private Menu menuAdministrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Recibe el usuario que ha accedido al sistema
     *
     * @param usuario
     */
    void initStage(Usuario usuario) {
        this.usuario = usuario;

        // Activa el menu Adminsitrador
        if (usuario.getRol().equals("Administrador")) {
            menuAdministrar.setVisible(true);
        }

        cargarPanelTareas();
    }

    /**
     * Llama al método cargarPanelTareas()
     *
     * @param event se lanza al hacer clic en el menú 'Mis tareas'
     */
    @FXML
    private void cargarPanelTareas(MouseEvent event) {
        cargarPanelTareas();
    }

    /**
     * Carga el panel de tareas
     */
    private void cargarPanelTareas() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/FXMLTareas.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            fxmlLoader.<FXMLTareasController>getController().initStage(usuario);
            borderPaneFX.getChildren().remove(borderPaneFX.getCenter());
            borderPaneFX.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            Dialogo.error("Error", null, "Fallo al cargar la ventana de tareas.");
        }
    }

    /**
     * Carga el panel de perfil
     *
     * @param event se lanza al hacer clic en el menú 'Mi pefil'
     */
    @FXML
    private void cargarPanelPerfil(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/FXMLPerfil.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            fxmlLoader.<FXMLPerfilController>getController().initStage(usuario);
            borderPaneFX.getChildren().remove(borderPaneFX.getCenter());
            borderPaneFX.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            Dialogo.error("Error", null, "Fallo al cargar la ventana de perfil.");
        }
    }

    /**
     * Carga el panel para adminsitrar usuarios
     *
     * @param event se lanza al hacer clic en el menuItem Administrar>Usuarios
     */
    @FXML
    private void cargarPanelAdministracionUsuarios(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/FXMLAdministracionUsuarios.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            borderPaneFX.getChildren().remove(borderPaneFX.getCenter());
            borderPaneFX.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
            Dialogo.error("Error", null, "Fallo al cargar la ventana de perfil.");;
        }
    }

    /**
     * Carga la pantalla de login.
     *
     * @param event se lanza al hacer clic en el menuItem 'Salir>Cerrar sesión'
     */
    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/FXMLLogin.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Stage stage = (Stage) borderPaneFX.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            Dialogo.error("Error", null, "Fallo al cargar la ventana de login.");
        }
    }

    /**
     * Cierra la aplicación.
     *
     * @param event se lanza al hacer clic en el menuItem 'Salir>Cerrar'
     * aplicación
     */
    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        Stage stage = (Stage) borderPaneFX.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra un diálogo con el autor del proyecto
     *
     * @param event se lanza al hacer clic en el menú 'Autor'
     */
    @FXML
    private void mostrarAutor(MouseEvent event) {
        Dialogo.informacion("información", null, "El autor de esta aplicación es Alex Clemente \ny cachos de Alex Profesor"
                + "\n\nalexclementefornas95@gmail.com");
    }
}
