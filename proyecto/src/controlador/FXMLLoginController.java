/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.Conexion;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Alex Clemente < DAW >
 */
public class FXMLLoginController implements Initializable {

    @FXML
    private AnchorPane anchorPaneFX;
    @FXML
    private TextField formUser;
    @FXML
    private PasswordField formPass;
    @FXML
    private Button accederButton;

    int nuMintentosInicioSesion = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // LLama al metodo accederAplicacion()
        anchorPaneFX.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                accederAplicacion();
            }
        });
        accederButton.setOnAction((ActionEvent event) -> {
            accederAplicacion();
        });
    }

    /**
     * Este método realizará las comprobaciones para hacer login en la
     * aplicación.
     *
     * @author Álex Torres
     */
    private void accederAplicacion() {
        if (formUser.getText().length() == 0 || formPass.getText().length() == 0) { // Comprobar campos vacíos
            Dialogo.error("Error login", null, "Rellena todos los campos.");
        } else {
            Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
            try {
                // Se comprueba si existe ese usuario
                PreparedStatement consulta = con.prepareStatement("SELECT count(*) AS usuario FROM usuarios WHERE usuario=?;");
                consulta.setString(1, formUser.getText());
                ResultSet cantidad = consulta.executeQuery();
                cantidad.next();
                if (cantidad.getInt("usuario") == 0) {
                    Dialogo.error("Error login", null, "El usuario no existe.");
                } else {
                    // Se comprueba la contraseña
                    consulta = con.prepareStatement("SELECT * FROM usuarios WHERE usuario=? and contrasenya=?;");
                    consulta.setString(1, formUser.getText());
                    consulta.setString(2, Contrasenya.cifrarContrasenya(formPass.getText()));
                    ResultSet datosUsuario = consulta.executeQuery();

                    if (datosUsuario.next()) {
                        Usuario usuario = new Usuario(datosUsuario.getInt("id"), datosUsuario.getString("usuario"), datosUsuario.getString("nombre"),
                                datosUsuario.getString("apellidos"), datosUsuario.getString("email"), datosUsuario.getString("rol"));

                        // Se carga la ventana principal de la aplicacion y se manda el usurio logeado
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/FXMLUsuario.fxml"));
                            Parent root = (Parent) fxmlLoader.load();

                            Stage stage = (Stage) formUser.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.show();

                            fxmlLoader.<FXMLUsuarioController>getController().initStage(usuario);
                            stage.setTitle("Gestor de tareas");
                        } catch (IOException e) {
                            e.printStackTrace();
                            Dialogo.error("Error", null, "Fallo al cargar la ventana de administrador.");
                        }
                    } else {
                        Dialogo.error("Error login", null, "Contraseña incorrecta.");
                        nuMintentosInicioSesion++;
                        // Si el usuario no consigue iniciar sesion tras varios intentos, se le mostrará el email del superadminsitrador
                        if (nuMintentosInicioSesion == 3) {
                            System.out.println("hola");
                            consulta = con.prepareStatement("SELECT Email FROM usuarios WHERE id = (SELECT * FROM Superadmin);");
                            ResultSet rs = consulta.executeQuery();
                            
                            rs.next();
                            String email = rs.getString("Email");
                            Dialogo.informacion("Información", null, "Si sigues teniendo problemas para iniciar sesión ponte en contacto con el administrador:"
                                    + "\n\n" + email);
                            nuMintentosInicioSesion = 0;
                        }
                    }
                    Conexion.desconectar(con);
                }
            } catch (SQLException e) {
                Dialogo.error("Error login", null, "Error al realizar el login");
            } catch (NoSuchAlgorithmException ex) {
                Dialogo.error("Error login", null, "Error al cifrar la contraseña");
            }
        }
    }

}
