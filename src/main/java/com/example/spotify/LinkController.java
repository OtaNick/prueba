package com.example.spotify;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkController implements Initializable {

    public TextField username;
    public TextField contrasenya;
    public TextField email;
    public TextField pais;
    public TextField codigoPostal;
    public Label alerta;
    public RadioButton btFemenino;
    public RadioButton btMasculino;
    public DatePicker fecha;
    public RadioButton btOtro;
    boolean correcto=false;

    //Fijar tamaños de los TextFields
    public void initialize(URL location, ResourceBundle resources) {
        fijarTamañoMáximo(username,45);
        fijarTamañoMáximo(contrasenya,20);
        fijarTamañoMáximo(email,150);
        fijarTamañoMáximo(pais,45);
        fijarTamañoMáximo(codigoPostal,20);
    }
    public static void fijarTamañoMáximo(final TextField campoTexto, final int tamañoMáximo) {
        campoTexto.lengthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number valorAnterior, Number valorActual) {

                if (valorActual.intValue() > valorAnterior.intValue()) {
                    Pattern permitido = Pattern.compile("[A-Za-zÑÇÁÉÍÓÚÀÈÌÒÙÏÜÂÊÎÔÛñçáéíóúàèìòùïüâêîôû@0123456789.]");
                    Matcher mpermitido = permitido.matcher(campoTexto.getText().substring(valorAnterior.intValue()));

                    if (!mpermitido.find()) {
                        // caracter no permitido, borrarlo
                        campoTexto.setText(campoTexto.getText().substring(0, valorAnterior.intValue()));
                        return;
                    }

                    // Revisa que la longitud del texto no sea mayor a la variable definida.
                    if (campoTexto.getText().length() >= tamañoMáximo) {
                        campoTexto.setText(campoTexto.getText().substring(0, tamañoMáximo));
                    }
                }
            }
        });
    }


    private void agregarDatos() {
        try {
            // Establece la conexión
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                    "dbrootpass");

            //interpretar masculino o femenino
            char genero = 0;
            if (btFemenino.isSelected()) {
                genero = 'f';
            } else if (btMasculino.isSelected()) {
                genero = 'm';
            } else if (btOtro.isSelected()) {
                genero = 'o';
            }

            //cambiar el formato del datepicker
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
            fecha.setConverter(new LocalDateStringConverter(formatter, null));

            //Realizar la agregación
            Statement s = con.createStatement();
            s.executeUpdate("INSERT INTO usuario (username,`password`,email,genero,fecha_nacimiento,pais,codigo_postal)" +
                    "values (" + '"' + username.getText() + '"' + "," + '"' + contrasenya.getText() + '"' + "," + '"' + email.getText() + '"' + "," + '"' + genero + '"' + "," + '"' + fecha.getValue() + '"' + "," + '"' + pais.getText() + '"' + "," + '"' + codigoPostal.getText() + '"' + ");");

            // Cerramos la conexión a la base de datos.
            con.close();
            correcto=true;
        } catch (SQLIntegrityConstraintViolationException e) {
            alerta.setText("Usuario o email ya en uso");
            alerta.setOpacity(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btVolver(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(Ejecutar.class.getResource("login.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btConfirmar(ActionEvent actionEvent) {

        if (username.getText().equals("") || contrasenya.getText().equals("") || email.getText().equals("") || pais.getText().equals("") || codigoPostal.getText().equals("") || !btMasculino.isSelected() && !btFemenino.isSelected() && !btOtro.isSelected() ||
            fecha.getValue()==null){
            alerta.setText("Todos los campos deben estar rellenados");
            alerta.setOpacity(1);
        }else if (!email.getText().contains("@")){
            alerta.setText("Email incorrecto");
            alerta.setOpacity(1);
        }else{
            agregarDatos();
            if (correcto){
                btVolver(actionEvent);
            }
        }
    }

    //Configuración botones de genero
    public void btMasculino(ActionEvent actionEvent) {
        if (btFemenino.isSelected() || btOtro.isSelected()){
            btMasculino.setSelected(false);
        }
    }
    public void btFemenino(ActionEvent actionEvent) {
        if (btMasculino.isSelected() || btOtro.isSelected()){
            btFemenino.setSelected(false);
        }
    }
    public void btOtro(ActionEvent actionEvent) {
        if (btFemenino.isSelected() || btMasculino.isSelected()){
            btOtro.setSelected(false);
        }
    }
}
