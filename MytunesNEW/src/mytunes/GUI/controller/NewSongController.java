package mytunes.GUI.controller;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JFrame;
import mytunes.BE.Songs;
import mytunes.GUI.model.TuneModel;

public class NewSongController implements Initializable {

    private TuneModel tm;
    @FXML
    private TextField TitleBox;
    @FXML
    private TextField ArtistBox;
    @FXML
    private TextField DurationBox;
    @FXML
    private TextField FilePathBox;
    @FXML
    private ComboBox<String> comboGenre;
    private MainWindowController MainWController;
    private boolean isEditing = false;
    private int SongNewID;
    private List<String> AllGenres;
    private Songs song;
    @FXML
    private Button btn_Choose;
    @FXML
    private Button btn_Save;
    @FXML
    private Button btn_Cancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tm = TuneModel.getInstance();
        song = tm.getSong();
        if (song != null) {
            TitleBox.setText(song.getTitle());
            ArtistBox.setText(song.getArtist());
            DurationBox.setText(song.getDuration());
            FilePathBox.setText(song.getSongPath());
            String songs = song.getGenre();
            comboGenre.getSelectionModel().select(songs);
        }
        AllGenres = tm.getGenres();
        for (String genre : AllGenres) {
            comboGenre.getItems().add(genre);
        }
    }
// This method allows us to pick the path of the while whilst we are editing or creating a song.
    @FXML
    private void clickChooseSong(ActionEvent event) throws IOException 
    {
        FileDialog fileD = new FileDialog(new JFrame());
        fileD.setVisible(true);
        File[] Path = fileD.getFiles();
        Path[0].getName();
        if (Path.length > 0) 
        {
            TitleBox.setText(Path[0].getName());
            String filePath = "./src/mytunes/MusicLibrary/";
            FilePathBox.setText(filePath);
        }
    }
// This closes the EditSong window
    @FXML
    private void clickCancelNewSong(ActionEvent event) // 
    {
        isEditing = false;
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
// This saves that data that you put in the EditSong window
    @FXML
    private void clickSaveSong(ActionEvent event) throws IOException 
    {
        if (!isEditing) {
            if (!"".equals(DurationBox.getText()) && !"".equals(ArtistBox.getText()) && comboGenre.getSelectionModel().getSelectedItem() != null
                    && !"".equals(DurationBox.getText()) && !"".equals(FilePathBox.getText())) {
                int songId = tm.nextAvailableSongID();
                String title = TitleBox.getText();
                String artist = ArtistBox.getText();
                String genre = comboGenre.getSelectionModel().getSelectedItem();
                String duration = DurationBox.getText();                           
                String songPath = FilePathBox.getText();
                tm.createSong(songId, title, artist, genre, duration, songPath);;
                MainWController.refreshTableSongs();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } else {
            if (!"".equals(DurationBox.getText()) && !"".equals(ArtistBox.getText()) && comboGenre.getSelectionModel().getSelectedItem() != null
                    && !"".equals(DurationBox.getText()) && !"".equals(FilePathBox.getText())) {
                int songId = SongNewID;
                String title = TitleBox.getText();
                String artist = ArtistBox.getText();
                String genre = comboGenre.getSelectionModel().getSelectedItem();
                String duration = DurationBox.getText();
                String songPath = FilePathBox.getText();
                Songs editSong = new Songs(songId, artist, title, genre, duration, songPath);
                tm.updateSong(editSong);
                MainWController.refreshTableSongs();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                isEditing = false;
            }
        }
    }

  

    public void setController(MainWindowController controller, boolean isEditing, int songID) // This method allows us to get connection with our MainWindowController and will check whether we are creating or editing
    {
        this.MainWController = controller;
        this.isEditing = isEditing;
        this.SongNewID = songID;
    }
}
