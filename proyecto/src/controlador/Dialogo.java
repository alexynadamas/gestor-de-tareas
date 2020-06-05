/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Perimite ejecutar diálogos sin instanciar desde otras clases
 *
 * @author Álex Torres
 */
public class Dialogo {

    public static Optional<ButtonType> confirmacion(String titulo, String cabecera, String cuerpo) {
//	return dialogo(titulo, cabecera, cuerpo, Alert.AlertType.CONFIRMATION);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(cuerpo);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static Optional<ButtonType> advertencia(String titulo, String cabecera, String cuerpo) {
        return dialogo(titulo, cabecera, cuerpo, Alert.AlertType.WARNING);
    }

    public static Optional<ButtonType> informacion(String titulo, String cabecera, String cuerpo) {
        return dialogo(titulo, cabecera, cuerpo, Alert.AlertType.INFORMATION);
    }

    public static Optional<ButtonType> error(String titulo, String cabecera, String cuerpo) {
        return dialogo(titulo, cabecera, cuerpo, Alert.AlertType.ERROR);
    }

    private static Optional<ButtonType> dialogo(String titulo, String cabecera, String cuerpo, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(cuerpo);

//	Optional<ButtonType> result = alert.showAndWait();
//	return result;
        return alert.showAndWait();
    }
}
