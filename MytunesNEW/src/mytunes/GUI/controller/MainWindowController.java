package mytunes.GUI.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.sound.sampled.UnsupportedAudioFileException;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;
import mytunes.GUI.model.TuneModel;
import mytunes.gui.Controller.PlaylistWindowController;

public class MainWindowController implements Initializable
{

    @FXML
    private ListView<Songs> tblSongsOnPlaylist;
    @FXML
    private TextField textFieldFilterSearch;
    @FXML
    private Label lblSongTitle;
    @FXML
    private TableView<Playlists> tblViewPlaylists;
    @FXML
    private TableView<Songs> tblViewLibrary;
    private boolean isPlaying;
    private boolean muted;
    private Media hit;
    private MediaPlayer mediaPlayer;
    private int songLenght;
    private double volume = 0;
    @FXML
    private ImageView playBtn;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ImageView nxtBtn;
    @FXML
    private ImageView previousBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Button minimizeBtn;
    @FXML
    private ImageView ToggleUpSongPL;
    @FXML
    private ImageView ToggleDownSongPL;
    @FXML
    private ImageView PushSongLeft;
    @FXML
    private ImageView speaker;
    private TuneModel tm;
    private String songPath;
    private Songs song = null;
    private Duration songDuration;
    private ObservableList songsAsObservable;
    private ObservableList playlistsAsObservable;
    private ObservableList searchedSongsAsObservable;
    @FXML
    private TableColumn<Songs, String> titleCol;
    @FXML
    private TableColumn<Songs, String> artistCol;
    @FXML
    private TableColumn<Songs, String> genreCol;
    @FXML
    private TableColumn<Songs, String> durationCol;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TableColumn<Playlists, String> playlistNameCol;
    @FXML
    private TableColumn<Playlists, Integer> playlistSongsCol;
    @FXML
    private TableColumn<Playlists, String> playlistDurationCol;
    @FXML
    private ProgressBar songProgress;
    @FXML
    private Label songTimeLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Slider progressSlider;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        isPlaying = false;
        searchedSongsAsObservable = FXCollections.observableArrayList();
        progressBar.setProgress(0.5);
        muted = false;
        volumeSlider.setMax(1.0);
        volumeSlider.setMin(0);
        volumeSlider.setValue(0.5);
        final ProgressIndicator pi = new ProgressIndicator(0);
        volumeSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            progressBar.setProgress(newValue.doubleValue());
            if (song != null)
            {
                mediaPlayer.setVolume(newValue.doubleValue());
                volume = newValue.doubleValue();
            }
        });
        progressSlider.setMax(1.0);
        progressSlider.setMin(0);
        progressSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            songProgress.setProgress(newValue.doubleValue());
            if (song != null)
            {
                Duration duration = Duration.seconds(songLenght * newValue.doubleValue());
                mediaPlayer.seek(duration);
            }
        });
        tm = TuneModel.getInstance();
        setSongsTable();
        setPlaylistTable();
    }

    public void setSongsTable() // This method gets all songs from database and loeads it into tableSongs
    {
        songsAsObservable = FXCollections.observableArrayList(tm.getSongsAsObservable());
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblViewLibrary.getColumns().clear();
        tblViewLibrary.setItems(songsAsObservable);
        tblViewLibrary.getColumns().addAll(titleCol, artistCol, genreCol, durationCol);
       
    }

    private void setPlaylistTable() // This method gets all playlists from database and loeads it into tablePlaylist
    {
        playlistsAsObservable = FXCollections.observableArrayList(tm.getPlaylistsAsObservable());
        playlistNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        playlistSongsCol.setCellValueFactory(new PropertyValueFactory<>("countOfSongsOnPlaylist"));
        playlistDurationCol.setCellValueFactory(new PropertyValueFactory<>("durationOfPlaylist"));
        tblViewPlaylists.getColumns().clear();
        tblViewPlaylists.setItems(playlistsAsObservable);
        tblViewPlaylists.getColumns().addAll(playlistNameCol, playlistSongsCol, playlistDurationCol);
      
    }

    @FXML
    private void clickRemoveSongPlaylist(ActionEvent event) 
    {
        if (tblSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
        {
            Songs song = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
            tm.deleteSongFromPlaylistSongs(song.getPlaylistUniqueID());
            tblSongsOnPlaylist.getItems().clear();
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            int index = tblViewPlaylists.getSelectionModel().getSelectedIndex();
            List<Songs> List = tm.getPlaylistSongs(playlist);
            tblSongsOnPlaylist.getItems().addAll(List);
            refreshTablePlaylist();
            tblViewPlaylists.refresh();
            tblViewPlaylists.getSelectionModel().select(index);
        }
    }

    @FXML
    private void clickEditSong(ActionEvent event) throws IOException 
    {
        Songs SongToEdit = tblViewLibrary.getSelectionModel().getSelectedItem();
        tm.setSong(SongToEdit);
        if (tblViewLibrary.getSelectionModel().getSelectedItem() != null)
        {
            int id = tblViewLibrary.getSelectionModel().getSelectedItem().getsongId();
            String path = "/mytunes/GUI/view/Editsong.fxml";
            boolean IsEditing = true;
            openSongWindow(path, id, IsEditing);
        }
    }

    @FXML
    private void clickNewSong(ActionEvent event) throws IOException 
    {
        Songs NewSong = null;
        tm.setSong(NewSong);
        String path = "/mytunes/GUI/view/NewSong.fxml";
        int id = 0;
        boolean isEditing = false;
        openSongWindow(path, id, isEditing);
    }

    @FXML
    private void clickDeleteSong(ActionEvent event) 
    {
        Songs ToDeleteSong = tblViewLibrary.getSelectionModel().getSelectedItem();
        if (ToDeleteSong != null)
        {
            String name = ToDeleteSong.getTitle() + " " + ToDeleteSong.getArtist();
            Alert alert = new Alert(AlertType.CONFIRMATION, "Click YES to confirm deletion of the chosen song " + name + " Click OK to delete song from Database only and YES to delete from PC and Database", ButtonType.YES, ButtonType.OK, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK)
            {
                if (ToDeleteSong != null)
                {
                    tm.deleteSong(ToDeleteSong);
                    tm.deleteSongFromPlaylistSongs(ToDeleteSong.getsongId());
                    refreshTableSongs();
                }
            } else if (alert.getResult() == ButtonType.YES)
            {
                tm.deleteSong(ToDeleteSong);
                tm.deleteSongFromPlaylistSongs(ToDeleteSong.getsongId());
                refreshTableSongs();
                try
                {
                    File file = new File(ToDeleteSong.getSongPath());
                    Files.delete(file.toPath());
                } catch (IOException ex)
                {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void clickNewPlaylist(ActionEvent event) throws IOException  
    {
        int id = 0;
        String path = "/mytunes/GUI/view/PlaylistWindow.fxml";
        Playlists playlist = null;
        tm.setPlaylist(playlist);
        boolean edit = false;
        openPlaylistWindow(path, id, edit);
    }

    @FXML
    private void clickEditPlaylist(ActionEvent event) throws IOException 
    {
        Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
        tm.setPlaylist(playlist);
        if (tblViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            int id = tblViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId();
            String path = "/mytunes/GUI/view/PlaylistWindow.fxml";
            boolean edit = true;
            openPlaylistWindow(path, id, edit);
        }
    }

    @FXML
    private void clickDeletePlaylist(ActionEvent event) 
    {
        if (tblViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            String name = tblViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistName();
            Alert alert = new Alert(AlertType.CONFIRMATION, "DELETE " + name + " ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES)
            {
                Playlists playlistToDelete = tblViewPlaylists.getSelectionModel().getSelectedItem();
                if (playlistToDelete.getSongAmountPL() > 0)
                {
                    tm.deletePlaylistFromPlaylistSongs(playlistToDelete.getPlaylistId());
                }
                tm.deletePlaylist(playlistToDelete);
                refreshTablePlaylist();
            }
        }
    }

    private void playSelectedSong() throws UnsupportedAudioFileException, IOException 
    {
        if (song == null)
        {
            setMusicPlayer();
            Runnable runnable = new progressUpdate();
            Thread thread = new Thread(runnable);
            thread.start();
        } else if (song == tblSongsOnPlaylist.getSelectionModel().getSelectedItem())
        {
            mediaPlayer.play();
        } else if (song != tblSongsOnPlaylist.getSelectionModel().getSelectedItem() && tblSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
        {
            setMusicPlayer();
        } else
        {
            mediaPlayer.play();
        }

        mediaPlayer.setOnEndOfMedia(() ->
        {
            if (tblSongsOnPlaylist.getItems().size() == tblSongsOnPlaylist.getSelectionModel().getSelectedIndex() + 1)
            {
                tblSongsOnPlaylist.getSelectionModel().selectFirst();
            } else
            {
                tblSongsOnPlaylist.getSelectionModel().selectNext();
            }
            setMusicPlayer();
            mediaPlayer.play();
        });
    }

    private void setMusicPlayer() 
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
        song = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
        songPath = song.getSongPath();
        hit = new Media(new File(songPath).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        songTimeLabel.setText(song.getDuration());
        lblSongTitle.setText(song.getArtist() + "|" + song.getTitle());
        if (volume != 0)
        {
            mediaPlayer.setVolume(volume);
        }
        mediaPlayer.setOnReady(() ->
        {
            songLenght = (int) hit.getDuration().toSeconds();
            songDuration = hit.getDuration();
            mediaPlayer.play();
        });
    }

    @FXML
    private void playReleased(MouseEvent event) throws UnsupportedAudioFileException, IOException 
    {
        if (!isPlaying)
        {
            isPlaying = true;
            if (tblSongsOnPlaylist.getSelectionModel().getSelectedItem() != null || song != null)
            {
                playSelectedSong();
                mediaPlayer.setMute(muted);
            }
            //playBtn.setImage(new Image("mytunes/pausebtn.png"));
        } else
        {
            isPlaying = false;
            if (song != null)
            {
                mediaPlayer.pause();
            }
            
        }
    }

    @FXML
    private void doubleClickToPlay(MouseEvent event) throws IOException // This method allowes us to play song by double-click
    {
        if (event.getClickCount() == 2)
        {
            try
            {
                if (!isPlaying)
                {
                    isPlaying = true;
                    playSelectedSong();
                    mediaPlayer.setMute(muted);
                    
                } else
                {
                    isPlaying = true;
                    playSelectedSong();
                    mediaPlayer.setMute(muted);
                    
                }
            } catch (UnsupportedAudioFileException ex)
            {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    private void getSliderValue(DragEvent event)
    {
        lblSongTitle.setText(Double.toString(volumeSlider.getValue()));
    }



    @FXML
    private void nextReleased(MouseEvent event) 
    {
        tblSongsOnPlaylist.getSelectionModel().selectNext();
        try
        {
            playSelectedSong();
        } catch (UnsupportedAudioFileException | IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void previousReleased(MouseEvent event) //Plays previous song on the list
    {
        tblSongsOnPlaylist.getSelectionModel().selectPrevious();
        try
        {
            playSelectedSong();
        } catch (UnsupportedAudioFileException | IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    @FXML
    private void clickCloseProgram (MouseEvent event) 
    {
        System.exit(1);
    }

    @FXML
    private void Minimize(MouseEvent event)
    {
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void clickToggleUpSongPressed(MouseEvent event) 
    {
        
        if (tblSongsOnPlaylist.getSelectionModel().getSelectedIndex() > 0)
        {
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            int chosenItem = tblSongsOnPlaylist.getSelectionModel().getSelectedIndex();
            int itemToSwapWith = chosenItem - 1;
            Songs songActual = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
            Songs songToSwapWith = tblSongsOnPlaylist.getItems().get(itemToSwapWith);
            if (songActual.getsongId() != songToSwapWith.getsongId())
            {
                tm.reCreatePlaylistSongs(songToSwapWith, songActual);
                tblSongsOnPlaylist.getItems().clear();
                tblSongsOnPlaylist.getItems().addAll(tm.getPlaylistSongs(playlist));
                tblSongsOnPlaylist.getSelectionModel().select(itemToSwapWith);
            }
        }
       
    }

   
    @FXML
    private void clickToggleDownSongPressed(MouseEvent event)
    {
        int sizeOfPlaylist = tblSongsOnPlaylist.getItems().size();
        if (tblSongsOnPlaylist.getSelectionModel().getSelectedIndex() < sizeOfPlaylist - 1)
        {
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            int chosenItem = tblSongsOnPlaylist.getSelectionModel().getSelectedIndex();
            int itemToSwapWith = chosenItem + 1;
            Songs songActual = tblSongsOnPlaylist.getSelectionModel().getSelectedItem();
            Songs songToSwapWith = tblSongsOnPlaylist.getItems().get(itemToSwapWith);
            if (songActual.getsongId() != songToSwapWith.getsongId())
            {
                tm.reCreatePlaylistSongs(songActual, songToSwapWith);
                tblSongsOnPlaylist.getItems().clear();
                tblSongsOnPlaylist.getItems().addAll(tm.getPlaylistSongs(playlist));
                tblSongsOnPlaylist.getSelectionModel().select(itemToSwapWith);
            }
        }
   }


    @FXML
    private void clickPushSongPressed(MouseEvent event)
    {
        if (tblViewLibrary.getSelectionModel().getSelectedItem() != null && tblViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            Songs song = tblViewLibrary.getSelectionModel().getSelectedItem();
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            int index = tblViewPlaylists.getSelectionModel().getSelectedIndex();
            tm.addSongToPlaylist(song, playlist);
            tblSongsOnPlaylist.getItems().clear();
            tblSongsOnPlaylist.getItems().addAll(tm.getPlaylistSongs(playlist));
            refreshTablePlaylist();
            tblViewPlaylists.refresh();
            tblViewPlaylists.getSelectionModel().select(index);
        }
       }

    @FXML
    private void youClickedPlaylist(MouseEvent event) 
    {
        if (tblViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            tblSongsOnPlaylist.getItems().clear();
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            List<Songs> list = tm.getPlaylistSongs(playlist);
            tblSongsOnPlaylist.getItems().addAll(list);
        }
    }

    private void search()
    {
        String text = textFieldFilterSearch.getText();
        List<Songs> Search = tm.searchSong(text);
        searchedSongsAsObservable.clear();
        searchedSongsAsObservable.addAll(Search);
        if (Search.size() > 0 && text.length() > 0)
        {
            tblViewLibrary.setItems(searchedSongsAsObservable);
        } else if (Search.isEmpty() && text.length() > 0)
        {
            tblViewLibrary.getItems().clear();
        } else if (Search.size() > 0 && text.length() == 0)
        {
            refreshTableSongs();
        }
    }

    @FXML
    private void enterSearch(KeyEvent event) 
    {
        if (event.getCode() == KeyCode.ENTER && textFieldFilterSearch.isFocused())
        {
            search();
        }
    }

    

    private class progressUpdate implements Runnable
    {

        @Override
        public void run()
        {
            while (true)
            {
                Platform.runLater(() ->
                {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    double d = currentTime.toSeconds();
                    int i = (int) d;
                    currentTimeLabel.setText(currentTimeCalculator(i));
                    updateProgressBar(currentTime.toSeconds());
                });
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void updateProgressBar(final double currentTime)
    {
        double fractionalProgress = (double) currentTime / (double) songLenght;
        songProgress.setProgress(fractionalProgress);
    }

    private String currentTimeCalculator(int timeSec) 
    {
        int minutes = timeSec / 60;
        int seconds = timeSec % 60;
        if (seconds < 10)
        {
            return minutes + ":0" + seconds;
        } else
        {
            return minutes + ":" + seconds;
        }
    }

    public void refreshTableSongs() 
    {
        tblViewLibrary.getItems().clear();
        tblViewLibrary.setItems(tm.getSongsAsObservable());
    }

    public void refreshTablePlaylist() 
    {
        tblViewPlaylists.getItems().clear();
        tblViewPlaylists.setItems(tm.getPlaylistsAsObservable());
    }
    // This will open our EditSong fxml
    public void openSongWindow(String path, int playlistId, boolean isEditing)
    {
        try
        {
            Parent root1;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            root1 = (Parent) fxmlLoader.load();
            fxmlLoader.<EditSongController>getController().setController(this, isEditing, playlistId);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // This will open our PlaylistWindow 
    public void openPlaylistWindow(String path, int id, boolean edit) 
    {
        try
        {
            Parent root1;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            root1 = (Parent) fxmlLoader.load();
            fxmlLoader.<PlaylistWindowController>getController().setController(this, edit, id);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
