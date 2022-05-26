package com.example.spotify;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.util.ResourceBundle;

public class LoginController{


    public TextField username;
    public PasswordField password;
    public Label alerta;
    public Hyperlink link;
    public Button btEntrar;
    boolean correcto;
    public static String user;

    private void realizarConsulta() {
        try {
// Establece la conexión
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                    "dbrootpass");
// Ejecuta la consulta
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT username,password FROM usuario");
// Procesa los resultados
            while (rs.next()) {
                String usernameC = rs.getString("username");
                String passwordC = rs.getString("password");
                if (username.getText().equals(usernameC) && password.getText().equals(passwordC)){
                    correcto=true;
                    user=username.getText();
                    break;
                }else{
                    correcto=false;
                }

            }
//Cerrar la conexión
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void btEntrar(ActionEvent actionEvent) {
        realizarConsulta();
        if (username.getText().equals("") || password.getText().equals("")) {
            alerta.setText("Rellena todos los campos correctamente");
            alerta.setOpacity(1);
            password.setText("");
        }else if (!correcto) {
            alerta.setText("Usuario o contraseña incorrecta");
            alerta.setOpacity(1);
            password.setText("");
            username.setText("");
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(Ejecutar.class.getResource("vista_principal.fxml"));
            try {
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage;
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btLink(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(Ejecutar.class.getResource("vista_link.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btUser1(ActionEvent actionEvent) {
            username.setText("angel");
            password.setText("123");
            btEntrar(actionEvent);
    }
}
