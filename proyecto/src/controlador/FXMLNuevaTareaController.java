/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import conexion.Conexion;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import modelo.Tarea;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Alex Clemente < DAW >
 */
public class FXMLNuevaTareaController implements Initializable {

    private FXMLTareasController ventana; // almacena la instancia de la ventana padre
    private Usuario usuario; // almacena el usuario logeado

    @FXML
    private TextField tituloFX;
    @FXML
    private TextArea descripcionFX;
    @FXML
    private ComboBox<String> prioridadFX;
    @FXML
    private ComboBox<String> estadoFX;
    @FXML
    private ComboBox<String> asignarComboBox;
    @FXML
    private TextField asignadoFX;
    @FXML
    private Slider progresoFX;
    @FXML
    private ImageView flechaImg;
    @FXML
    private ImageView usuarioValidoImg;
    @FXML
    private Label labelPorcentaje;
    @FXML
    private Button buttonGuardar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // LISTENERS
        // Vuelve visible el campo TextField asignadoFX cuando es necesario
        asignarComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (asignarComboBox.getSelectionModel().getSelectedItem().equals("otro usuario")) {
                    asignadoFX.setVisible(true);
                    flechaImg.setVisible(true);
                    usuarioValidoImg.setVisible(true);
                    if (asignadoFX.getText().length() == 0) {
                        buttonGuardar.setDisable(true);
                    }
                } else {
                    asignadoFX.setVisible(false);
                    flechaImg.setVisible(false);
                    usuarioValidoImg.setVisible(false);
                }
            }
        });

        // Activa o desactiva el boton guardar tras comprobar los campos tituloFX y descripcionFX
        tituloFX.textProperty().addListener((observable, oldValue, newValue) -> {
            buttonGuardar.setDisable(true);
            if (((String) newValue).length() > 0 && descripcionFX.getText().length() > 0) {
                buttonGuardar.setDisable(false);
            }
        });
        descripcionFX.textProperty().addListener((observable, oldValue, newValue) -> {
            buttonGuardar.setDisable(true);
            if (((String) newValue).length() > 0 && tituloFX.getText().length() > 0) {
                buttonGuardar.setDisable(false);
            }
        });

        progresoFX.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                labelPorcentaje.setText(String.valueOf(Math.round(progresoFX.getValue())) + " %");
            }
        });
    }

    /**
     * Guarda los datos de la nueva tarea en la base de datos
     *
     * @param event
     */
    @FXML
    private void guardarNuevaTarea(ActionEvent event) {
        String asignadoA = usuario.getUsuario();
        if (asignarComboBox.getSelectionModel().getSelectedItem().equals("otro usuario")) {
            asignadoA = asignadoFX.getText();
        }
        String porcentaje = labelPorcentaje.getText().replace(" %", "");

        Tarea nuevaTarea = new Tarea(tituloFX.getText(), descripcionFX.getText(), prioridadFX.getValue(),
                estadoFX.getValue(), usuario.getUsuario(), asignadoA, Integer.parseInt(porcentaje));

        Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
        try {
            PreparedStatement consulta = con.prepareStatement("INSERT INTO Tareas(Titulo, Descripcion, Prioridad, Estado, Creador, Asignacion, Progreso) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            consulta.setString(1, nuevaTarea.getTitulo());
            consulta.setString(2, nuevaTarea.getDescripcion());
            consulta.setString(3, nuevaTarea.getPrioridad());
            consulta.setString(4, nuevaTarea.getEstado());
            consulta.setString(5, nuevaTarea.getCreador());
            consulta.setString(6, nuevaTarea.getAsignacion());
            consulta.setInt(7, nuevaTarea.getProgreso());
            consulta.executeUpdate();
            // Se obtiene la clave generada por la base de datos
            ResultSet rs = consulta.getGeneratedKeys();
            rs.next();
            int codigo = rs.getInt(1);

            // Se envía la nueva tarea a la ventana de tareas para actualizar la tabla
            ventana.anyadirTareaTabla(nuevaTarea);

            Stage stage = (Stage) buttonGuardar.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            Dialogo.error("Error", null, "Error al insertar en la base de datos.");
        }
        Conexion.desconectar(con);
    }

    /**
     * Cierra la ventana actual
     *
     * @param event se lanza con el botón cancelar
     */
    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) buttonGuardar.getScene().getWindow();
        stage.close();
    }

    /**
     * Recibe el usuario e instancia de la ventana padre
     *
     * @param ventana
     * @param usuario
     */
    void initStage(FXMLTareasController ventana, Usuario usuario, ObservableList<String> listaPrioridades, ObservableList<String> listaEstados) {
        this.ventana = ventana;
        this.usuario = usuario;

        ObservableList<String> listaAsiginar = FXCollections.observableArrayList();
        listaAsiginar.add("mí (" + usuario.getUsuario() + ")");
        listaAsiginar.add("otro usuario");
        asignarComboBox.setItems(listaAsiginar);
        asignarComboBox.getSelectionModel().selectFirst();

        prioridadFX.setItems(listaPrioridades);
        prioridadFX.getSelectionModel().selectFirst();
        estadoFX.setItems(listaEstados);
        estadoFX.getSelectionModel().selectFirst();
    }

    /**
     * Comprueba que exista el usario al cual se le va a asignar la tarea.
     * Muestra un icono de validación
     *
     * @param event se lanza a medide que se escribe en el campo asignadoFX
     */
    @FXML
    private void validarUsuario(KeyEvent event) {

        usuarioValidoImg.setImage(new Image(getClass().getResourceAsStream("/recursos/cerrar.png")));
        buttonGuardar.setDisable(true);
        String usuario = asignadoFX.getText();

        Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
        try {
            PreparedStatement consulta = con.prepareStatement("SELECT COUNT(*) AS usuario FROM usuarios WHERE usuario = ?;");
            consulta.setString(1, usuario);
            ResultSet rs = consulta.executeQuery();
            rs.next();
            if (rs.getInt("usuario") == 1) {
                usuarioValidoImg.setImage(new Image(getClass().getResourceAsStream("/recursos/garrapata.png")));
                buttonGuardar.setDisable(false);
            }
            Conexion.desconectar(con);
        } catch (SQLException e) {
            Dialogo.error("Error", null, "Error al validar usuario");
        }
    }

}
