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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Tarea;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Alex Clemente < DAW >
 */
public class FXMLTareasController implements Initializable {

    private Usuario usuario; // Almacenea el usuario logeado en el sistema
    private ObservableList<Tarea> listaTareas = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> filtroEstadoFX;
    @FXML
    private ComboBox<String> filtroPrioridadFX;
    @FXML
    private TableView<Tarea> tareasFX;
    @FXML
    private TextField tituloFX;
    @FXML
    private TextArea descripcionFX;
    @FXML
    private ComboBox<String> prioridadFX;
    @FXML
    private ComboBox<String> estadoFX;
    @FXML
    private Label creadorFX;
    @FXML
    private Slider progresoFX;
    @FXML
    private Label idFX;
    @FXML
    private TableColumn<Tarea, Integer> columnaId;
    @FXML
    private TableColumn<Tarea, String> columnaTitulo;
    @FXML
    private TableColumn<Tarea, String> columnaPrioridad;
    @FXML
    private TableColumn<Tarea, String> columnaEstado;
    @FXML
    private TableColumn<Tarea, Integer> columnaProgreso;
    @FXML
    private Label labelPorcentaje;
    @FXML
    private TextField asignadoFX;
    @FXML
    private TextField textfieldBuscador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Rellenar comboBox
        ObservableList<String> lista = listaEnumerada("Prioridad");
        prioridadFX.setItems(lista);
        prioridadFX.getSelectionModel().selectFirst();
        lista.add("Sin filtro");
        filtroPrioridadFX.setItems(lista);
        filtroPrioridadFX.getSelectionModel().select("Sin filtro");
        lista = listaEnumerada("Estado");
        estadoFX.setItems(lista);
        estadoFX.getSelectionModel().selectFirst();
        lista.add("Sin filtro");
        filtroEstadoFX.setItems(lista);
        filtroEstadoFX.getSelectionModel().select("Sin filtro");

        // listener del slider
        progresoFX.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                labelPorcentaje.setText(String.valueOf(Math.round(progresoFX.getValue())) + " %");
            }
        });
    }

    /**
     * Carga una ventana con un formulario para añadir nuevas tareas
     *
     * @param event se lanza al hacer clic en el boton 'Nueva tarea'
     */
    @FXML
    private void crearTarea(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/vista/FXMLNuevaTarea.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setTitle("Nueva tarea");
            fxmlLoader.<FXMLNuevaTareaController>getController().initStage(this, usuario, listaEnumerada("Prioridad"), listaEnumerada("Estado"));

            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Dialogo.error("Error", null, "Fallo al cargar la ventana de 'Nueva Tarea'.");
        }
    }

    /**
     * Recibe el usuario desde la ventana padre (FXMLUsuario)
     *
     * @param usuario
     */
    void initStage(Usuario usuario) {
        this.usuario = usuario;

        // Carga los datos de la tabla desde la base de datos
        Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
        try {
            PreparedStatement consulta = null;
            // El administrador tiene acceso a todas las tareas
            if (usuario.getRol().equals("Administrador")) {
                consulta = con.prepareStatement("SELECT * FROM Tareas;");
            } else {
                consulta = con.prepareStatement("SELECT * FROM Tareas WHERE creador = ? OR asignacion = ?;");
                consulta.setString(1, usuario.getUsuario());
                consulta.setString(2, usuario.getUsuario());
            }
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                listaTareas.add(new Tarea(rs.getInt("id"), rs.getString("titulo"), rs.getString("descripcion"), rs.getString("prioridad"),
                        rs.getString("estado"), rs.getString("creador"), rs.getString("asignacion"), rs.getInt("progreso")));
            }
            tareasFX.setItems(listaTareas);
            columnaId.setCellValueFactory(new PropertyValueFactory<Tarea, Integer>("id"));
            columnaTitulo.setCellValueFactory(new PropertyValueFactory<Tarea, String>("titulo"));
            columnaPrioridad.setCellValueFactory(new PropertyValueFactory<Tarea, String>("prioridad"));
            columnaEstado.setCellValueFactory(new PropertyValueFactory<Tarea, String>("estado"));
            columnaProgreso.setCellValueFactory(new PropertyValueFactory<Tarea, Integer>("progreso"));
        } catch (SQLException e) {
            Dialogo.error("Error", null, "Error al cargar los datos de la tabla");
        }
        Conexion.desconectar(con);

        // Muestra los datos del primer registro de la tabla
        tareasFX.getSelectionModel().selectFirst();
        Tarea tareaSeleccionada = tareasFX.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            rellenarDatosTarea(tareaSeleccionada);
        }

        // FILTROS
        FilteredList<Tarea> filteredData = new FilteredList<>(listaTareas, p -> true);
        tareasFX.setItems(filteredData);

        // Permite buscar una tarea por cadena de texto
        textfieldBuscador.setPromptText("Buscar...");
        textfieldBuscador.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(tarea -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }
                String name = tarea.getTitulo().toLowerCase();
                return name.contains(text.toLowerCase());
            });
        });
        // Permite filtrar por prioridad
        filtroPrioridadFX.valueProperty().addListener((prop, old, value) -> {
            filteredData.setPredicate(tarea -> {
                if (value == null || value.isEmpty() || value.equals("Sin filtro")) {
                    return true;
                }
                String name = tarea.getPrioridad().toLowerCase();
                return name.contains(value.toLowerCase());
            });
        });
        // Permite filtarar por estado
        filtroEstadoFX.valueProperty().addListener((prop, old, value) -> {
            filteredData.setPredicate(tarea -> {
                if (value == null || value.isEmpty() || value.equals("Sin filtro")) {
                    return true;
                }
                String name = tarea.getEstado().toLowerCase();
                return name.contains(value.toLowerCase());
            });
        });
    }

    /**
     * Actualiza los datos de la tabla a partir de una nueva tarea
     *
     * @param nuevaTarea se recibe desde la ventana nueva tarea
     */
    public void anyadirTareaTabla(Tarea nuevaTarea) {
        listaTareas.add(nuevaTarea);
        tareasFX.refresh();
        tareasFX.getSelectionModel().select(nuevaTarea);
        rellenarDatosTarea(nuevaTarea);
    }

    /**
     * Muestra la información de la tarea seleccionada en la tabla
     *
     * @param event
     */
    @FXML
    private void mostrarInfoTarea(MouseEvent event) {
        Tarea tareaSeleccionada = tareasFX.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            rellenarDatosTarea(tareaSeleccionada);
        }
    }

    /**
     * Rellena la sección de la derecha de la ventana con los datos de una tarea
     *
     * @param Tarea
     */
    private void rellenarDatosTarea(Tarea tarea) {
        idFX.setText(Integer.toString(tarea.getId()));
        tituloFX.setText(tarea.getTitulo());
        descripcionFX.setText(tarea.getDescripcion());
        prioridadFX.setValue(tarea.getPrioridad());
        estadoFX.setValue(tarea.getEstado());
        creadorFX.setText(tarea.getCreador());
        asignadoFX.setText(tarea.getAsignacion());
        labelPorcentaje.setText(Integer.toString(tarea.getProgreso()) + " %");
        progresoFX.setValue(tarea.getProgreso());
    }

    /**
     * Si existen cambios, actualiza los datos de la tarea activa
     *
     * @param event se lanza con el botón 'Actualizar'
     */
    @FXML
    private void actualizarDatosTarea(ActionEvent event) {
        // Comprobar si existen cambios
        Tarea tareaSeleccionada = tareasFX.getSelectionModel().getSelectedItem();
        Tarea tareaModificada = new Tarea(Integer.parseInt(idFX.getText()), tituloFX.getText(), descripcionFX.getText(), prioridadFX.getValue(),
                estadoFX.getValue(), creadorFX.getText(), asignadoFX.getText(), (int) Math.round(progresoFX.getValue()));

        if (tareaSeleccionada == null) {
            Dialogo.advertencia("Advertencia", null, "No se ha seleccionado una tarea");
        } else {
            if (!tareaModificada.equals(tareaSeleccionada)) {

                Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
                try {
                    // Comprobar que existe el usuario al que se le asgina la tarea
                    PreparedStatement consulta = con.prepareStatement("SELECT COUNT(*) AS cont FROM Usuarios WHERE usuario=?");
                    consulta.setString(1, tareaModificada.getAsignacion());
                    ResultSet rs = consulta.executeQuery();
                    rs.next();
                    if (rs.getInt("cont") == 0) {
                        Dialogo.error("Error", null, "El usuario al cual estás asignando la tarea no existe.\nEspecifica un usuario válido.");
                    } else {

                        // Actualizar datos en la tabla
                        tareaSeleccionada.setTitulo(tareaModificada.getTitulo());
                        tareaSeleccionada.setDescripcion(tareaModificada.getDescripcion());
                        tareaSeleccionada.setPrioridad(tareaModificada.getPrioridad());
                        tareaSeleccionada.setEstado(tareaModificada.getEstado());
                        tareaSeleccionada.setCreador(tareaModificada.getCreador());
                        tareaSeleccionada.setAsignacion(tareaModificada.getAsignacion());
                        tareaSeleccionada.setProgreso(tareaModificada.getProgreso());
                        tareasFX.refresh();
                        // Actualizar en la base de datos
                        try {
                            consulta = con.prepareStatement("UPDATE Tareas SET titulo=?, descripcion=?, prioridad=?, estado=?, "
                                    + "creador=?, asignacion=?, progreso=? WHERE id=?;");
                            consulta.setString(1, tareaModificada.getTitulo());
                            consulta.setString(2, tareaModificada.getDescripcion());
                            consulta.setString(3, tareaModificada.getPrioridad());
                            consulta.setString(4, tareaModificada.getEstado());
                            consulta.setString(5, tareaModificada.getCreador());
                            consulta.setString(6, tareaModificada.getAsignacion());
                            consulta.setInt(7, tareaModificada.getProgreso());
                            consulta.setInt(8, tareaModificada.getId());
                            consulta.executeUpdate();
                            Dialogo.informacion("Información", null, "Datos actualizados correctamente");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Dialogo.error("Error", null, "Error al actualizar en la base de datos");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Dialogo.error("Error", null, "Error al actualizar en la base de datos");
                }
            } else {
                Dialogo.advertencia("Advertencia", null, "No han habido cambios");
            }
        }
    }

    /**
     * Elimina la tarea seleccionada en la tabla y base de datos
     *
     * @param event se lanza con el botón 'Eliminar tarea'
     */
    @FXML
    private void eliminarTarea(ActionEvent event) {
        Tarea tareaSeleccionada = tareasFX.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {

            Optional<ButtonType> result = Dialogo.confirmacion("Confirmación", null, "¿Estás seguro de que quieres eliminar esta tarea?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Eliminar la tarea de la tabla
                listaTareas.remove(tareaSeleccionada);
                tareasFX.refresh();

                // Eliminar tarea de la base de datos
                Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
                try {
                    PreparedStatement consulta = con.prepareStatement("DELETE FROM Tareas WHERE id = ?;");
                    consulta.setInt(1, tareaSeleccionada.getId());
                    consulta.executeUpdate();
                } catch (SQLException e) {
                    Dialogo.error("Error", null, "Error al eliminar la tarea");
                }
                Conexion.desconectar(con);

                // Muestra los datos del ultimo registro de la tabla
                tareaSeleccionada = tareasFX.getSelectionModel().getSelectedItem();
                if (tareaSeleccionada != null) {
                    rellenarDatosTarea(tareaSeleccionada);
                }
            }
        } else {
            Dialogo.advertencia("Advertencia", null, "No se ha seleccionado ninguna tarea");
        }
    }

    /**
     * Devuelve la lista de valores de un campo ENUM de la base de datos
     *
     * @return ObservableList<String>
     */
    private ObservableList<String> listaEnumerada(String campo) {
        ObservableList<String> listaEnum = null;
        try {
            Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
            PreparedStatement consulta = con.prepareStatement("SELECT column_type FROM information_schema.COLUMNS "
                    + "WHERE table_schema = 'gestortareas' AND TABLE_NAME = 'tareas' AND column_name = ?;");
            consulta.setString(1, campo);
            ResultSet enumPrioridad = consulta.executeQuery();
            enumPrioridad.next();

            String lista = enumPrioridad.getNString("column_type");
            lista = lista.replace(("enum("), (""));
            lista = lista.replace((")"), (""));
            lista = lista.replace(("'"), (""));
            String[] arrayElementos = lista.split(",");

            listaEnum = FXCollections.observableArrayList();
            for (int i = 0; i < arrayElementos.length; i++) {
                listaEnum.add(arrayElementos[i]);
            }

            Conexion.desconectar(con);
        } catch (SQLException e) {
            Dialogo.error("Error", null, "Error al consultar en la base de datos");
            e.printStackTrace();
        }
        return listaEnum;
    }

}
