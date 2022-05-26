package com.example.spotify;

import com.example.spotify.models.Cancion;
import com.example.spotify.models.Playlist;
import com.example.spotify.models.Podcast;
import com.example.spotify.models.Usuario;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ImageView corazon;
    public Label username;
    public ImageView bandera;
    public Button btPremium;
    public Label avisoPremium;
    public MenuItem btCalidad;
    public MenuItem btTipoDescarga;
    public ScrollPane ScrollPanePodcast;
    public  Label cancionActual;
    public  ImageView imagenActual;
    public  Label cantanteActual;
    public Button btCorazon;
    public ScrollPane scrollGeneral;
    private boolean esVerde = false;
    public int idioma_id;
    boolean premium = false;
    Image espanyol = new Image("com/example/spotify/img/banderaEspaña.png");
    Image ingles = new Image("com/example/spotify/img/banderaIngles.jpg");
    Image frances = new Image("com/example/spotify/img/banderaFrancia.png");
    Image italiano = new Image("com/example/spotify/img/banderaItalia.jpg");
    Image aleman = new Image("com/example/spotify/img/banderaAlemania.png");
    Image coraVerde = new Image("com/example/spotify/img/corazonVerde.png");
    Image coraTrans = new Image("com/example/spotify/img/ic_love.png");
    Usuario usuario = new Usuario();
    Cancion cancion = new Cancion();

    FXMLLoader fxmlLoader;

    @FXML
    private HBox PodcastContainer;

    @FXML
    private HBox favoritosContainer;

    @FXML
    public HBox azarContainer;

    @FXML
    public HBox playlistContainer;
    

    List<Podcast> podcasts;
    List<Cancion> favoritas;
    List<Cancion> azar;
    List<Playlist> playlist;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollGeneral.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollGeneral.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        username.setText(LoginController.user);
        fxmlLoader = new FXMLLoader(Ejecutar.class.getResource("login.fxml"));
        cancionActual.setText("Esperando cancion...");
        cantanteActual.setText("Esperando cantante...");

        //Idioma y ver si es premium
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                    "root",
                    "dbrootpass");
            Statement stmt = con.createStatement();

            //saber id USUARIO
            ResultSet rs = stmt.executeQuery("select id from usuario where username = " + '"' + username.getText() + '"');
            while (rs.next()) {
                usuario.setId(rs.getInt("id"));
            }

            //saber id IDIOMA
            rs = stmt.executeQuery("SELECT idioma_id FROM configuracion WHERE usuario_id=(SELECT id FROM usuario WHERE username=" + '"' + username.getText() + '"' + ")");
            while (rs.next()) {
                idioma_id= rs.getInt("idioma_id");
            }

            if (idioma_id==1){
                bandera.setImage(espanyol);
            }else if(idioma_id==2){
                bandera.setImage(ingles);
            }else if(idioma_id==3){
                bandera.setImage(frances);
            }else if(idioma_id==4){
                bandera.setImage(italiano);
            }else if(idioma_id==5){
                bandera.setImage(aleman);
            }

            rs=stmt.executeQuery("select usuario_id from premium");
            while (rs.next()) {
                int usuario_idC = rs.getInt("usuario_id");
                if (usuario.getId() == usuario_idC){
                    premium=true;
                    break;
                }else {
                    premium=false;
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (premium){
            btPremium.setVisible(false);
        }else {
            btCalidad.setDisable(true);
            btTipoDescarga.setDisable(true);
            btPremium.setVisible(true);
        }

        podcasts = new ArrayList<>(getPodcast());
        favoritas = new ArrayList<>(getFavorites());
        azar = new ArrayList<>(getAzar());
        playlist = new ArrayList<>(getPLaylist());


        try {

            for (Podcast podcast : podcasts) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("vista_podcast.fxml"));

                VBox vBox = fxmlLoader.load();

                PodcastController podcastController = fxmlLoader.getController();
                podcastController.setDatos(podcast);

                PodcastContainer.getChildren().add(vBox);

                vBox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        btCorazon.setVisible(false);
                        cantanteActual.setText(podcast.getDescripcion());
                        cancionActual.setText(podcast.getTitulo());
                        imagenActual.setImage(new Image(podcast.getImagen()));
                    }

                });

                vBox.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: grey;" +
                                "-fx-background-radius: 20;");
                    }
                });

                vBox.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: black;" +
                                "-fx-background-radius: 20;");
                    }
                });
            }
            for (Cancion song : favoritas) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("vista_cancion.fxml"));

                VBox vBox = fxmlLoader.load();

                CancionController cancionController = fxmlLoader.getController();
                cancionController.setData(song);

                favoritosContainer.getChildren().add(vBox);

                vBox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        btCorazon.setVisible(true);
                        esVerde=false;
                        cantanteActual.setText(song.getArtista());
                        cancionActual.setText(song.getNombre());
                        imagenActual.setImage(new Image(song.getImagen()));

                        try {
                            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                                    "root",
                                    "dbrootpass");
                            Statement stmt = con.createStatement();

                            //saber id CANCION
                            ResultSet rs = stmt.executeQuery("select id from cancion where titulo = " + '"' + cancionActual.getText() + '"');
                            while (rs.next()) {
                                cancion.setId(rs.getInt("id"));
                            }

                            rs = stmt.executeQuery("select usuario_id, cancion_id from guarda_cancion where usuario_id =" + '"' + usuario.getId() + '"' + "and cancion_id = " + '"' + cancion.getId() + '"');
                            while (rs.next()) {
                                esVerde = true;
                            }
                            if (esVerde) {
                                corazon.setImage(coraVerde);
                            } else {
                                corazon.setImage(coraTrans);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //Raton encima de..
                vBox.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: grey;" +
                                "-fx-background-radius: 20;");
                    }
                });
                //Raton fuera de..
                vBox.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: black;" +
                                "-fx-background-radius: 20;");

                    }
                });
            }
            for (Cancion song : azar) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("vista_cancion.fxml"));

                VBox vBox = fxmlLoader.load();

                CancionController cancionController = fxmlLoader.getController();
                cancionController.setData(song);

                azarContainer.getChildren().add(vBox);

                vBox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        btCorazon.setVisible(true);
                        esVerde=false;
                        cantanteActual.setText(song.getArtista());
                        cancionActual.setText(song.getNombre());
                        imagenActual.setImage(new Image(song.getImagen()));

                        try {
                            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                                    "root",
                                    "dbrootpass");
                            Statement stmt = con.createStatement();

                            //saber id CANCION
                            ResultSet rs = stmt.executeQuery("select id from cancion where titulo = " + '"' + cancionActual.getText() + '"');
                            while (rs.next()) {
                                cancion.setId(rs.getInt("id"));
                            }

                            rs = stmt.executeQuery("select usuario_id, cancion_id from guarda_cancion where usuario_id =" + '"' + usuario.getId() + '"' + "and cancion_id = " + '"' + cancion.getId() + '"');
                            while (rs.next()) {
                                esVerde = true;
                            }
                            if (esVerde) {
                                corazon.setImage(coraVerde);
                            } else {
                                corazon.setImage(coraTrans);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                });
                //Raton encima de..
                vBox.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: grey;" +
                                "-fx-background-radius: 20;");
                    }
                });

                //Raton fuera de..
                vBox.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: black;" +
                                "-fx-background-radius: 20;");
                    }
                });
            }
            for (Playlist list : playlist) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                if (list.getTitulo().contains("favorita")){
                    continue;
                }

                fxmlLoader.setLocation(getClass().getResource("vista_playlist.fxml"));

                VBox vBox = fxmlLoader.load();

                PlaylistController playlistController = fxmlLoader.getController();
                playlistController.setData(list);

                playlistContainer.getChildren().add(vBox);

                vBox.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        btCorazon.setVisible(false);
                        esVerde=false;
                        cantanteActual.setText(list.getFecha_creacion());
                        cancionActual.setText(list.getTitulo());
                        imagenActual.setImage(new Image("com/example/spotify/img/playlist.jpg"));
                    }

                });

                //Raton encima de..
                vBox.onMouseMovedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: grey;" +
                                "-fx-background-radius: 20;");
                    }
                });
                //Raton fuera de..
                vBox.onMouseExitedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        vBox.setStyle("-fx-background-color: black;" +
                                "-fx-background-radius: 20;");
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Podcast> getPodcast() {
        List<Podcast> ls = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                    "root",
                    "dbrootpass");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select podcast.titulo ,podcast.imagen,podcast.descripcion from podcast_usuario pu inner join podcast on podcast.id=pu.podcast_id inner join usuario on usuario.id=pu.usuario_id where pu.usuario_id = " + "'" + usuario.getId() + "'");
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String imagen = rs.getString("imagen");
                String descripcion = rs.getString("descripcion");
                Podcast podcast = new Podcast();
                podcast.setTitulo(titulo);
                podcast.setImagen(imagen);
                podcast.setDescripcion(descripcion);
                ls.add(podcast);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public List<Cancion> getFavorites() {

        List<Cancion> ls = new ArrayList();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                    "dbrootpass");
            Statement stmt = con.createStatement();
            ResultSet titulo = stmt.executeQuery("select cancion.titulo, cancion.ruta, artista.nombre\n" +
                    "from guarda_cancion gc\n" +
                    "inner join cancion on cancion.id=gc.cancion_id\n" +
                    "inner join usuario on usuario.id=gc.usuario_id\n" +
                    "inner join album on album.id=cancion.album_id\n" +
                    "inner join artista on artista.id=album.artista_id\n" +
                    "where usuario.id =" + '"' + usuario.getId() + '"');

            while (titulo.next()) {
                String titulo_c = titulo.getString("cancion.titulo");
                String artista_c = titulo.getString("artista.nombre");
                String imagen_c = titulo.getString("cancion.ruta");

                Cancion cancion = new Cancion();
                cancion.setNombre(titulo_c);
                cancion.setArtista(artista_c);
                cancion.setImagen(imagen_c);
                ls.add(cancion);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public List<Cancion> getAzar() {

        List<Cancion> ls = new ArrayList();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                    "dbrootpass");
            Statement stmt = con.createStatement();
            ResultSet titulo = stmt.executeQuery("select cancion.titulo, cancion.ruta, artista.nombre\n" +
                    "from guarda_cancion gc\n" +
                    "inner join cancion on cancion.id=gc.cancion_id\n" +
                    "inner join album on album.id=cancion.album_id\n" +
                    "inner join artista on artista.id=album.artista_id\n" +
                    "where gc.cancion_id not in (select cancion_id from guarda_cancion where usuario_id = " + usuario.getId() + " ) " +
                    "group by titulo limit 10;");

            while (titulo.next()) {
                String titulo_c = titulo.getString("cancion.titulo");
                String artista_c = titulo.getString("artista.nombre");
                String imagen_c = titulo.getString("cancion.ruta");

                Cancion cancion = new Cancion();
                cancion.setNombre(titulo_c);
                cancion.setArtista(artista_c);
                cancion.setImagen(imagen_c);
                ls.add(cancion);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public List<Playlist> getPLaylist() {

        List<Playlist> ls = new ArrayList();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                    "dbrootpass");
            Statement stmt = con.createStatement();
            ResultSet titulo = stmt.executeQuery("select titulo, fecha_creacion from playlist where usuario_id = " + usuario.getId() + ";");

            while (titulo.next()) {
                String titulo_p = titulo.getString("playlist.titulo");
                String fecha_p = titulo.getString("playlist.fecha_creacion");

                Playlist playlist = new Playlist();
                playlist.setTitulo(titulo_p);
                playlist.setFecha_creacion(fecha_p);
                ls.add(playlist);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls;
    }

    public void btCora(ActionEvent actionEvent) {
        //Si esta en favoritos...
        if (corazon.getImage().equals(coraVerde)) {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                        "dbrootpass");
                Statement stmt = con.createStatement();
                String query = "DELETE FROM spotify.guarda_cancion WHERE (usuario_id = " + '"' + usuario.getId() + '"' +") and (cancion_id =" + '"' +   cancion.getId() + '"' + ")";
                stmt.executeUpdate(query);
                corazon.setImage(coraTrans);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify", "root",
                        "dbrootpass");
                Statement stmt = con.createStatement();
                String query = "INSERT INTO guarda_cancion VALUES ( " + '"' + usuario.getId() + '"' + "," + '"' +   cancion.getId() + '"' + ")";
                stmt.executeUpdate(query);
                corazon.setImage(coraVerde);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void btSalir(ActionEvent actionEvent) {
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

    //Configuracion de idioma
    public void btEspañol(ActionEvent actionEvent) {
        idioma_id=1;
        cambiarIdioma(espanyol);
    }
    public void btIngles(ActionEvent actionEvent) {
        idioma_id=2;
        cambiarIdioma(ingles);
    }
    public void btFrances(ActionEvent actionEvent) {
        idioma_id=3;
        cambiarIdioma(frances);
    }
    public void btItaliano(ActionEvent actionEvent) {
        idioma_id=4;
        cambiarIdioma(italiano);
    }
    public void btAleman(ActionEvent actionEvent) {
        idioma_id=5;
        cambiarIdioma(aleman);
    }
    public void cambiarIdioma(Image image){
        bandera.setImage(image);
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33006/spotify",
                    "root",
                    "dbrootpass");
            // Preparamos la consulta y la ejecutamos
            Statement s = con.createStatement();
            s.executeUpdate("UPDATE configuracion SET idioma_id = " + "'" + idioma_id + "'" +  "WHERE usuario_id=(SELECT id FROM usuario WHERE username=" + '"' + username.getText() + '"' + ")");
            // Cerramos la conexión a la base de datos.
                    con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }



    //Configuracion calidad
    public void btAutomatico(ActionEvent actionEvent) {
    }
    public void btBaja(ActionEvent actionEvent) {
    }
    public void btNormal(ActionEvent actionEvent) {
    }
    public void btAlta(ActionEvent actionEvent) {
    }
    public void btMuyAlta(ActionEvent actionEvent) {
    }

    public void btRefresh(ActionEvent actionEvent) {
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
    public void btPlaylist(ActionEvent actionEvent) {
        scrollGeneral.setVvalue(1);
    }
    public void btParati(ActionEvent actionEvent) {
        scrollGeneral.setVvalue(0.92);
    }
    public void btFavoritas(ActionEvent actionEvent) {
        scrollGeneral.setVvalue(0.45);
    }

    public void btPodcast(ActionEvent actionEvent) {
        scrollGeneral.setVvalue(0);
    }


}
