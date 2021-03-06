package mytunes.GUI.controller;


import java.net.URL;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javax.sound.sampled.UnsupportedAudioFileException;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;
import mytunes.GUI.model.TuneModel;

/*
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */

public class MainWindowController implements Initializable
{

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
    private MediaPlayer mediaPlayer;
    private double volume = 0;
    @FXML
    private Slider volumeSlider;
    private Button exitBtn;
    private TuneModel tm;
    private String songPath;
    private Media mp;
    private int Length;
    private Songs song = null;
    private Songs song1 = null;
    private Duration songDuration;
    private ObservableList songsAsObservable;
    private ObservableList<Playlists> playlistsAsObservable;

    private ObservableList searchedSongsAsObservable;
    @FXML
    private TableColumn<Songs, String> tblViewLibraryColumnTitle;
    @FXML
    private TableColumn<Songs, String> tblViewLibraryColumnArtist;
    @FXML
    private TableColumn<Songs, String> tblViewLibraryColumnGenre;
    @FXML
    private TableColumn<Songs, String> tblViewLibraryColumnDuration;
    private ProgressBar progressBar;
    private TableColumn<Playlists, String> playlistNameCol;
    private TableColumn<Playlists, Integer> playlistSongsCol;
    private TableColumn<Playlists, String> playlistDurationCol;
    private ProgressBar songProgress;
    private Label songTimeLabel;
    @FXML
    private Label currentTimeLabel;
    private Slider progressSlider;
    @FXML
    private AnchorPane grey;

    @FXML
    private Button btnDeletePlaylists;
    @FXML
    private Label labelCurrentlyPlaying;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnReplay;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnPreviousSong;
    @FXML
    private Button btnNext;
    @FXML
    private ListView<Songs> ViewSongsOnPlaylist;
    // This initializes our observables, progressbar, volumenSlider and such.
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        playlistsAsObservable = FXCollections.observableArrayList();
        progressBar = new ProgressBar(0.5);
        isPlaying = false;
        searchedSongsAsObservable = FXCollections.observableArrayList();
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
        progressSlider = new Slider();
        progressSlider.setMax(1.0);
        progressSlider.setMin(0);
        progressSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {
            songProgress.setProgress(newValue.doubleValue());
            if (song != null)
            {
                Duration duration = Duration.seconds(Length * newValue.doubleValue());
                mediaPlayer.seek(duration);
            }
        });
        tm = TuneModel.getInstance();
        setSongsTable();
        setPlaylistTable();
    }
    // The method underneath gets all songs from our database and loads it into our song library table, with the given string.
    public void setSongsTable() 
    {

        
        songsAsObservable = FXCollections.observableArrayList(tm.getSongsAsObservable());
        tblViewLibraryColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblViewLibraryColumnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewLibraryColumnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tblViewLibraryColumnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblViewLibrary.getColumns().clear();
        tblViewLibrary.setItems(songsAsObservable);
        tblViewLibrary.getColumns().addAll(tblViewLibraryColumnTitle, tblViewLibraryColumnArtist, tblViewLibraryColumnGenre, tblViewLibraryColumnDuration);

    }
   // The method underneath gets all playlists from our database and loads it into our playlist library table, with the given string.
    private void setPlaylistTable() 
    {
        playlistSongsCol = new TableColumn<>("ID");    
        playlistNameCol = new TableColumn<>("Name");
        
        //playlistDurationCol = new TableColumn<>();
        playlistSongsCol.setCellValueFactory(new PropertyValueFactory<>("PlaylistId"));
        playlistNameCol.setCellValueFactory(new PropertyValueFactory<>("PlaylistName"));
        tblViewPlaylists.setItems(tm.getPlaylistsAsObservable());
        tblViewPlaylists.getColumns().addAll(playlistNameCol, playlistSongsCol);

    }
    // This removes a song from a chosen playlist, but does not delete the song from our database.
    @FXML
    private void clickRemoveSongPlaylist(ActionEvent event)
    {
        if (ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
        {
            Songs song = ViewSongsOnPlaylist.getSelectionModel().getSelectedItem();
            tm.deleteSongFromPlaylistSongs(song.getPlaylistUniqueID());
            ViewSongsOnPlaylist.getItems().clear();
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            int index = tblViewPlaylists.getSelectionModel().getSelectedIndex();
            List<Songs> List = tm.getPlaylistSongs(playlist);
            ViewSongsOnPlaylist.getItems().addAll(List);
            refreshTablePlaylist();
            tblViewPlaylists.refresh();
            tblViewPlaylists.getSelectionModel().select(index);
        }
    }
    // This method deletes a specific song from our MusicLibrary and/or from our database.
    // An alert box has been implemented to ask the user, if he/she wishes to delete the specifi song from
    // the database, or the MusicLibrary folder only.
    @FXML
    private void clickDeleteSong(ActionEvent event)
    {
        Songs ToDeleteSong = tblViewLibrary.getSelectionModel().getSelectedItem();
        if (ToDeleteSong != null)
        {
            String name = ToDeleteSong.getTitle() + " " + ToDeleteSong.getArtist();
            Alert alert = new Alert(AlertType.CONFIRMATION, "Click YES to delete the chosen song " + name + " from the database.\nClick NO to cancel your current action", ButtonType.YES, ButtonType.NO);
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
//This method deletes a specific Playlist from our tblViewPlaylist table to the left.
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
// This plays a selected song from our PlaylistSongs table. The play button also changes, depending on whether the song is playing or not.
    @FXML
    private void playSelectedSong() throws UnsupportedAudioFileException, IOException
    {
        
        if (song == null)
        {
            song = ViewSongsOnPlaylist.getSelectionModel().getSelectedItem();
            song1 = tblViewLibrary.getSelectionModel().getSelectedItem();
            setMusicPlayer();
            Runnable runnable = new progressUpdate();
            Thread thread = new Thread(runnable);
            thread.start();
        } else if (song == ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() || song1 == tblViewLibrary.getSelectionModel().getSelectedItem() )
        {
            mediaPlayer.play();
        } else if (song != ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() && ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() != null || song != tblViewLibrary.getSelectionModel().getSelectedItem() && tblViewLibrary.getSelectionModel().getSelectedItem() != null)
        {
            setMusicPlayer();
        } else
        {
            mediaPlayer.play();
        }

        mediaPlayer.setOnEndOfMedia(() ->
        {
            if (ViewSongsOnPlaylist.getItems().size() == ViewSongsOnPlaylist.getSelectionModel().getSelectedIndex() + 1)
            {
                ViewSongsOnPlaylist.getSelectionModel().selectFirst();
            } else
            {
                ViewSongsOnPlaylist.getSelectionModel().selectNext();
            }
            setMusicPlayer();
            mediaPlayer.play();
        });
        
        if (tblViewLibrary.getItems().isEmpty())
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No song has been selected");
            alert.setContentText("Please select a song in the library");
            alert.showAndWait();
        } else
        {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.pause();
                btnPlay.setText("▷");
            } else
            {
                mediaPlayer.play();
                btnPlay.setText("ll");
            }
        }

    }
    private void getSliderVolume(DragEvent event)
    {
        lblSongTitle.setText(Double.toString(volumeSlider.getValue()));

    }
// This is playing our musicplayer, which gets the song information from a songs given title and artist.
    private void setMusicPlayer()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
        song = ViewSongsOnPlaylist.getSelectionModel().getSelectedItem();
        songPath = song.getSongPath();
        mp = new Media(new File(songPath + song.getTitle()).toURI().toString());
        mediaPlayer = new MediaPlayer(mp);

        lblSongTitle.setText(song.getArtist() + "|" + song.getTitle());
        if (volume != 0)
        {
            mediaPlayer.setVolume(volume);
        }
        mediaPlayer.setOnReady(() ->
        {
            Length = (int) mp.getDuration().toSeconds();
            songDuration = mp.getDuration();
            mediaPlayer.play();
        });
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

    private void getSongsFromPlaylist(MouseEvent event)
    {
        Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
        ObservableList obsList = FXCollections.observableArrayList(tm.getPlaylistSongs(playlist));
        ViewSongsOnPlaylist.setItems(obsList);
    }

    @FXML
    private void clickPushSongToPlaylist(ActionEvent event)
    {
        if (tblViewLibrary.getSelectionModel().getSelectedItem() != null)
        {
            Songs song = tblViewLibrary.getSelectionModel().getSelectedItem();

            songsAsObservable.add(song);
            ViewSongsOnPlaylist.getItems().clear();
            ViewSongsOnPlaylist.getItems().addAll(songsAsObservable);

            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            int index = tblViewPlaylists.getSelectionModel().getSelectedIndex();
            tm.addSongToPlaylist(song, playlist);
            ViewSongsOnPlaylist.getItems().clear();
            ViewSongsOnPlaylist.getItems().addAll(tm.getPlaylistSongs(playlist));
            refreshTablePlaylist();
            tblViewPlaylists.refresh();
            tblViewPlaylists.getSelectionModel().select(index);
        }
    }

    @FXML
    private void HitEnterSearch(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER && textFieldFilterSearch.isFocused())
        {
            search();
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
    
    // This will bind a chosen button to open up our NewSong window
    public void openSongWindow(String fxmlPath, int id, boolean isEditing)
    {
        try
        {
            Parent roots;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            roots = (Parent) fxmlLoader.load();
            fxmlLoader.<NewSongController>getController().setController(this, isEditing, id);
            Stage stage = new Stage();
            stage.setScene(new Scene(roots));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // This will bind a chosen button to open up our Playlist window

    public void openPlaylistWindow(String fxmlPath, int id, boolean isEditing)
    {
        try
        {
            Parent roots;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            roots = (Parent) fxmlLoader.load();
            fxmlLoader.<PlaylistWindowController>getController().setController(this, isEditing, id);
            Stage stage = new Stage();
            stage.setScene(new Scene(roots));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // This will bind a chosen button to open up our EditSong window

    public void openEditWindow(String fxmlPath, int id, boolean isEditing)
    {
        try
        {
            Parent roots;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            roots = (Parent) fxmlLoader.load();
            fxmlLoader.<EditSongController>getController().setController(this, isEditing, id);
            Stage stage = new Stage();
            stage.setScene(new Scene(roots));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void clickNewPlaylist(ActionEvent event) throws IOException
    {
        int id = 0;
        String fxmlPath = "/mytunes/GUI/view/PlaylistWindow.fxml";
        Playlists playlist = null;
        tm.setPlaylist(playlist);
        boolean isEditing = false;
        openPlaylistWindow(fxmlPath, id, isEditing);
    }

    @FXML
    private void clickNewSong(ActionEvent event)
    {
        int id = 0;
        String fxmlPath = "/mytunes/GUI/view/NewSong.fxml";
        Songs Song = null;
        tm.setSong(Song);
        boolean isEditing = false;
        openSongWindow(fxmlPath, id, isEditing);
    }

    @FXML
    private void clickEditSongs(ActionEvent event)
    {
        Songs SongToEdit = tblViewLibrary.getSelectionModel().getSelectedItem();
        tm.setSong(SongToEdit);
        if (tblViewLibrary.getSelectionModel().getSelectedItem() != null)
        {
            int id = tblViewLibrary.getSelectionModel().getSelectedItem().getsongId();
            String fxmlPath = "/mytunes/GUI/view/EditSong.fxml";
            boolean isEditing = true;
            openEditWindow(fxmlPath, id, isEditing);
        }
    }
    
    @FXML
    private void clickToEditPlaylist(ActionEvent event)
    {
        Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
        tm.setPlaylist(playlist);
        if (tblViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            int id = tblViewPlaylists.getSelectionModel().getSelectedItem().getPlaylistId();
            String fxmlPath = "/mytunes/GUI/view/PlaylistWindow.fxml";
            boolean isEditing = true;
            openPlaylistWindow(fxmlPath, id, isEditing);
        }
    }


    @FXML
    private void dblClickPlay(MouseEvent event) throws IOException
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

    @FXML
    private void ExitTunes(MouseEvent event)
    {
        System.exit(1);
    }

    @FXML
    private void PlayPreviousSong(ActionEvent event)
    {
        ViewSongsOnPlaylist.getSelectionModel().selectPrevious();
        try
        {
            playSelectedSong();
        } catch (UnsupportedAudioFileException | IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void PlayNextSong(ActionEvent event)
    {
        ViewSongsOnPlaylist.getSelectionModel().selectNext();
        try
        {
            playSelectedSong();
        } catch (UnsupportedAudioFileException | IOException ex)
        {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    
    @FXML
    private void clickPlaylist(MouseEvent event)
    {
        if (tblViewPlaylists.getSelectionModel().getSelectedItem() != null)
        {
            ViewSongsOnPlaylist.getItems().clear();
            Playlists playlist = tblViewPlaylists.getSelectionModel().getSelectedItem();
            List<Songs> list = tm.getPlaylistSongs(playlist);
            ViewSongsOnPlaylist.getItems().addAll(list);
        }
    }

    @FXML
    private void clickRestartSong(ActionEvent event)
    {
        mediaPlayer.seek(mediaPlayer.getStartTime());
        mediaPlayer.play();
    }

    @FXML
    private void clickStopSong(ActionEvent event)
    {
        mediaPlayer.stop();
        btnPlay.setText("▷");
        lblSongTitle.setText("");
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
                    currentTimeLabel.setText(currentDurationCalc(i));
                    updateSongDurationCounter(currentTime.toSeconds());
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

    private void updateSongDurationCounter(final double currentTime)
    {
        double fractionalProgress = (double) currentTime / (double) Length;
        songProgress = new ProgressBar();
        songProgress.setProgress(fractionalProgress);
    }

    private String currentDurationCalc(int timeSec)
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
}
