package com.example.spotify;

import com.example.spotify.models.Podcast;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PodcastController {
    public ImageView imagen;
    public Label titulo;
    public Label descripcion;

    public void setDatos(Podcast podcast){
        Image image = new Image(podcast.getImagen());
        imagen.setImage(image);
        titulo.setText(podcast.getTitulo());
        descripcion.setText(podcast.getDescripcion());
    }
}
