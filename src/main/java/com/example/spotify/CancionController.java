package com.example.spotify;

import com.example.spotify.models.Cancion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CancionController {
    public ImageView imagen;
    public Label titulo;
    public Label artista;

    public void setData(Cancion cancion){
        Image image = new Image(cancion.getImagen());
        imagen.setImage(image);
        titulo.setText(cancion.getNombre());
        artista.setText(cancion.getArtista());
    }
}
