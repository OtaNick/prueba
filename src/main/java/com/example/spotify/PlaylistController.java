package com.example.spotify;

import com.example.spotify.models.Cancion;
import com.example.spotify.models.Playlist;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlaylistController {
    public ImageView imagen;
    public Label titulo;
    public Label fecha_creacion;

    public void setData(Playlist playlist){
        Image image = new Image("com/example/spotify/img/playlist.jpg");
        imagen.setImage(image);
        titulo.setText(playlist.getTitulo());
        fecha_creacion.setText(playlist.getFecha_creacion());
    }
}
