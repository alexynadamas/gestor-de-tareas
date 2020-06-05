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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Alex Clemente < DAW >
 */
public class FXMLAdministracionUsuariosController implements Initializable {

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    private Label labelId;
    @FXML
    private TextField textfieldNombre;
    @FXML
    private TextField textfieldUsuario;
    @FXML
    private TextField textfieldEmail;
    @FXML
    private ComboBox<String> comboBoxRol;
    @FXML
    private TableView<Usuario> tableviewUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> columnaId;
    @FXML
    private TableColumn<Usuario, String> columnaUsuario;
    @FXML
    private TableColumn<Usuario, String> columnaEmail;
    @FXML
    private TableColumn<Usuario, String> columnaRol;
    @FXML
    private CheckBox checkboxNuevoUsuario;
    @FXML
    private TextField textfieldApellidos;
    @FXML
    private Label idFX;
    @FXML
    private Pane panelContrasenya;
    @FXML
    private TextField textfieldContrasenya;
    @FXML
    private TextField textfieldConfirmarContrasenya;
    @FXML
    private Button buttonEliminar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carga los datos de la tabla desde la base de datos
        Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
        try {
            PreparedStatement consulta = con.prepareStatement("SELECT * FROM Usuarios;");
            ResultSet rs = consulta.executeQuery();
            while (rs.next()) {
                listaUsuarios.add(new Usuario(rs.getInt("id"), rs.getString("usuario"), rs.getString("nombre"), rs.getString("apellidos"),
                        rs.getString("email"), rs.getString("rol")));
            }
            tableviewUsuarios.setItems(listaUsuarios);
            columnaId.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("id"));
            columnaUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, String>("usuario"));
            columnaEmail.setCellValueFactory(new PropertyValueFactory<Usuario, String>("email"));
            columnaRol.setCellValueFactory(new PropertyValueFactory<Usuario, String>("rol"));
        } catch (SQLException e) {
            Dialogo.error("Error", null, "Error al cargar los datos de la tabla");
        }

        // Muestra los datos del primer registro de la tabla
        tableviewUsuarios.getSelectionModel().selectFirst();
        Usuario usuarioSeleccionado = tableviewUsuarios.getSelectionModel().getSelectedItem();
        rellenarDatosUsuario(usuarioSeleccionado);

        // Rellena el comboBox rol desde la base de datos
        try {
            PreparedStatement consulta = con.prepareStatement("SELECT column_type FROM information_schema.COLUMNS "
                    + "WHERE table_schema = 'gestortareas' AND TABLE_NAME = 'Usuarios' AND column_name = 'Rol';");
            ResultSet enumPrioridad = consulta.executeQuery();
            enumPrioridad.next();

            String lista = enumPrioridad.getNString("column_type");
            lista = lista.replace(("enum("), (""));
            lista = lista.replace((")"), (""));
            lista = lista.replace(("'"), (""));
            String[] arrayElementos = lista.split(",");

            ObservableList<String> listaEnum = FXCollections.observableArrayList();
            for (int i = 0; i < arrayElementos.length; i++) {
                listaEnum.add(arrayElementos[i]);
            }
            comboBoxRol.setItems(listaEnum);
            comboBoxRol.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            Dialogo.error("Error", null, "Error al consultar en la base de datos");
        }
        Conexion.desconectar(con);

    }

    /**
     * Muestra información del usuario seleccionado de la tabla
     *
     * @param event se lanza al hacer clic en la tabla
     */
    @FXML
    private void mostrarInfoUsuario(MouseEvent event) {
        if (!checkboxNuevoUsuario.isSelected()) {
            modoNuevoUsuario(false);
            Usuario usuarioSeleccionado = tableviewUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                rellenarDatosUsuario(usuarioSeleccionado);
            }
        }
    }

    /**
     * Rellena la sección de la derecha de la ventana con los datos de un
     * usuario
     *
     * @param Usuario
     */
    private void rellenarDatosUsuario(Usuario usuario) {
        labelId.setText(Integer.toString(usuario.getId()));
        textfieldNombre.setText(usuario.getNombre());
        textfieldApellidos.setText(usuario.getApellidos());
        textfieldUsuario.setText(usuario.getUsuario());
        textfieldEmail.setText(usuario.getEmail());
        comboBoxRol.setValue(usuario.getRol());
    }

    /**
     * Registra los cambios del usuario en la tabla y base de datos
     *
     * @param event se lanza con el botón 'guardar'
     */
    @FXML
    private void guardarDatosUsuario(ActionEvent event) throws SQLException {

        Usuario nuevoUsuario = new Usuario(textfieldUsuario.getText(), textfieldNombre.getText(), textfieldApellidos.getText(),
                textfieldEmail.getText(), comboBoxRol.getValue());

        Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");

        if (checkboxNuevoUsuario.isSelected()) { // Comprueba si es una inserción o actualización

            /*
            * Validación de datos
             */
            boolean validacion = false;
            if (textfieldUsuario.getText().length() > 0 && textfieldContrasenya.getText().length()
                    > 0 && textfieldConfirmarContrasenya.getText().length() > 0) {
                // Comprueba que no exista aún el usuario en la base de datos
                try {
                    PreparedStatement consulta = con.prepareStatement("SELECT COUNT(*) AS cont FROM Usuarios WHERE usuario = ?;");
                    consulta.setString(1, nuevoUsuario.getUsuario());
                    ResultSet rs = consulta.executeQuery();
                    rs.next();
                    if (rs.getInt("cont") > 0) {
                        Dialogo.error("Error", null, "El usuario ya existe");
                    } else {
                        // Comprueba que las contraseñas coincidan
                        if (textfieldContrasenya.getText().equals(textfieldConfirmarContrasenya.getText())) {
                            validacion = true;
                        } else {
                            Dialogo.error("Error", null, "Las contraseñas no coinciden");
                        }
                    }
                } catch (SQLException e) {
                    Dialogo.error("Error", null, "Error al consultar en la base de datos");
                }
            } else {
                Dialogo.error("Error", null, "No se han rellenado los campos obligatorios (\"*\")");
            }

            if (validacion) {
                // Inserta el usuario en la base de datos
                try {
                    PreparedStatement consulta = con.prepareStatement("INSERT INTO Usuarios(Usuario, Nombre, Apellidos,"
                            + " Email, Contrasenya, Rol) VALUES(?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
                    consulta.setString(1, nuevoUsuario.getUsuario());
                    consulta.setString(2, nuevoUsuario.getNombre());
                    consulta.setString(3, nuevoUsuario.getApellidos());
                    consulta.setString(4, nuevoUsuario.getEmail());
                    String contrasenya = Contrasenya.cifrarContrasenya(textfieldContrasenya.getText()); // Se cifra la contraseña
                    consulta.setString(5, contrasenya);
                    consulta.setString(6, nuevoUsuario.getRol());
                    consulta.executeUpdate();
                    // Se obtiene la clave generada por la base de datos
                    ResultSet rs = consulta.getGeneratedKeys();
                    rs.next();
                    int codigo = rs.getInt(1);
                    nuevoUsuario.setId(codigo);

                    // Se añade el usuario a la tabla
                    listaUsuarios.add(nuevoUsuario);
                    tableviewUsuarios.refresh();
                    tableviewUsuarios.getSelectionModel().select(nuevoUsuario);
                    rellenarDatosUsuario(nuevoUsuario);

                    modoNuevoUsuario(false);
                    checkboxNuevoUsuario.setSelected(false);
                } catch (SQLException e) {
                    Dialogo.error("Error", null, "Error al insertar los datos en la base de datos");
                } catch (NoSuchAlgorithmException ex) {
                    Dialogo.error("Error", null, "Error al cifrar la contraseña.");
                }
            }

        } else {  // Actualiza el usuario

            /*
            * Validación de datos
             */
            boolean validacion = false;
            Usuario usuarioSeleccionado = tableviewUsuarios.getSelectionModel().getSelectedItem();

            if (textfieldUsuario.getText().length() > 0) {
                // Comprueba si han habido cambios entre el nuevo y antiguo usuario
                if (usuarioSeleccionado != null) {
                    nuevoUsuario.setId(Integer.parseInt(labelId.getText())); // Se añade la id del usuario

                    if (nuevoUsuario.equals(usuarioSeleccionado)) {
                        Dialogo.advertencia("Advertencia", null, "No han habido cambios");
                    } else {
                        // Si el nuevo nombre de usuario ha cambiado, comprueba que no exista aún en la base de datos
                        if (!nuevoUsuario.getUsuario().equals(usuarioSeleccionado.getUsuario())) {
                            try {
                                PreparedStatement consulta = con.prepareStatement("SELECT COUNT(*) AS cont FROM Usuarios WHERE usuario = ?;");
                                consulta.setString(1, nuevoUsuario.getUsuario());
                                ResultSet rs = consulta.executeQuery();
                                rs.next();
                                if (rs.getInt("cont") > 0) {
                                    Dialogo.error("Error", null, "El usuario ya existe");
                                } else {
                                    validacion = true;
                                }
                            } catch (SQLException e) {
                                Dialogo.error("Error", null, "Error al consultar en la base de datos");
                            }
                        } else {
                            validacion = true;
                        }
                    }
                } else {
                    Dialogo.advertencia("Advertencia", null, "No se ha seleccionado ningún usuario");
                }
            } else {
                Dialogo.error("Error", null, "No se han rellenado los campos obligatorios (\"*\")");
            }

            if (validacion) {
                // Actualiza el usuario en la base de datos
                try {
                    PreparedStatement consulta = con.prepareStatement("UPDATE Usuarios SET Usuario=?, Nombre=?, Apellidos=?,"
                            + " Email=?, Rol=? WHERE id=?;");
                    consulta.setString(1, nuevoUsuario.getUsuario());
                    consulta.setString(2, nuevoUsuario.getNombre());
                    consulta.setString(3, nuevoUsuario.getApellidos());
                    consulta.setString(4, nuevoUsuario.getEmail());
                    consulta.setString(5, nuevoUsuario.getRol());
                    consulta.setInt(6, nuevoUsuario.getId());
                    consulta.executeUpdate();

                    // Se actualiza el usuario en la tabla
                    usuarioSeleccionado.setId(nuevoUsuario.getId());
                    usuarioSeleccionado.setUsuario(nuevoUsuario.getUsuario());
                    usuarioSeleccionado.setEmail(nuevoUsuario.getEmail());
                    usuarioSeleccionado.setRol(nuevoUsuario.getRol());
                    tableviewUsuarios.refresh();
                    tableviewUsuarios.getSelectionModel().select(nuevoUsuario);
                } catch (SQLException e) {
                    Dialogo.error("Error", null, "Error al actualizar en la base de datos");
                }
                Dialogo.informacion("Información", null, "Datos actualizados correctamente");
            }
        }
        Conexion.desconectar(con);
    }

    /**
     * Activa la opción para insertar un nuevo usuario
     *
     * @param event se lanza al activar el checkbox
     */
    @FXML
    private void activarNuevoUsuario(ActionEvent event
    ) {
        if (checkboxNuevoUsuario.isSelected()) {
            modoNuevoUsuario(true);
            labelId.setText("");
            textfieldNombre.setText("");
            textfieldApellidos.setText("");
            textfieldUsuario.setText("");
            textfieldEmail.setText("");
            textfieldContrasenya.setText("");
            textfieldConfirmarContrasenya.setText("");
            comboBoxRol.getSelectionModel().select("Usuario");
        } else {
            modoNuevoUsuario(false);
            Usuario usuarioSeleccionado = tableviewUsuarios.getSelectionModel().getSelectedItem();
            rellenarDatosUsuario(usuarioSeleccionado);
        }
    }

    /**
     * Muestra las opciones para cuando se introduce un nuevo usuario
     *
     * @param activado Booleano que determina si esta activo el modo "Nuevo
     * Usuario"
     */
    private void modoNuevoUsuario(Boolean activado) {
        if (activado) {
            idFX.setVisible(false);
            panelContrasenya.setVisible(true);
            buttonEliminar.setDisable(true);
        } else {
            idFX.setVisible(true);
            panelContrasenya.setVisible(false);
            buttonEliminar.setDisable(false);
        }
    }

    /**
     * Elimina el usuario seleccinado de la tabla
     *
     * @param event se lanza con el botón 'Eliminar'
     */
    @FXML
    private void eliminarUsuarioSeleccionado(ActionEvent event) {
        Usuario usuarioSeleccionado = tableviewUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {

            Optional<ButtonType> result = Dialogo.confirmacion("Confirmación", null, "¿Estás seguro de que quieres eliminar este usuario?");
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Elimina el usuaro de la base de datos
                Connection con = Conexion.conectar("gestortareas", "proyecto", "proyecto");
                try {
                    PreparedStatement consulta = con.prepareStatement("DELETE FROM Usuarios WHERE id = ?;");
                    consulta.setInt(1, usuarioSeleccionado.getId());
                    consulta.executeUpdate();
                } catch (SQLException e) {
                    Dialogo.error("Error", null, "Error al eliminar el usuario");
                }
                Conexion.desconectar(con);

                // Elimina el usuario de la tabla
                listaUsuarios.remove(usuarioSeleccionado);
                tableviewUsuarios.refresh();
            }
        } else {
            Dialogo.advertencia("Advertencia", null, "No se ha seleccionado ningún usuario");
        }
    }
}
