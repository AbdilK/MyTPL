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
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.sound.sampled.UnsupportedAudioFileException;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;
import mytunes.GUI.model.TuneModel;
import mytunes.GUI.controller.PlaylistWindowController;
import mytunes.GUI.controller.EditSongController;

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
    private Media hit;
    private MediaPlayer mediaPlayer;
    private int songLenght;
    private double volume = 0;
    @FXML
    private Slider volumeSlider;
    private Button exitBtn;
    //@FXML
    //private Button ToggleUpSongPL;
    //@FXML
    //private Button ToggleDownSongPL;
    private TuneModel tm;
    private String songPath;
    private Songs song = null;
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

        /**
         * songsAsObservable =
         * FXCollections.observableArrayList(tm.getSongsAsObservable());
         *
         * tblViewLibraryColumnTitle = new TableColumn<>("Title");
         * tblViewLibraryColumnArtist = new TableColumn<>("Artist");
         * tblViewLibraryColumnGenre = new TableColumn<>("Genre");
         * tblViewLibraryColumnDuration = new TableColumn<>("Duration");
         *
         *
         * tblViewLibraryColumnTitle.setCellValueFactory(new
         * PropertyValueFactory<>("title"));
         * tblViewLibraryColumnArtist.setCellValueFactory(new
         * PropertyValueFactory<>("artist"));
         * tblViewLibraryColumnGenre.setCellValueFactory(new
         * PropertyValueFactory<>("genre"));
         * tblViewLibraryColumnDuration.setCellValueFactory(new
         * PropertyValueFactory<>("duration"));
         * tblViewLibrary.getColumns().clear();
         * tblViewLibrary.setItems(songsAsObservable);
         * tblViewLibrary.getColumns().addAll(tblViewLibraryColumnTitle,
         * tblViewLibraryColumnArtist, tblViewLibraryColumnGenre,
         * tblViewLibraryColumnDuration);
         * ViewSongsOnPlaylist.getColumns().add(tblViewLibraryColumnTitle);
         *
         */
        songsAsObservable = FXCollections.observableArrayList(tm.getSongsAsObservable());
        tblViewLibraryColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tblViewLibraryColumnArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tblViewLibraryColumnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tblViewLibraryColumnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        tblViewLibrary.getColumns().clear();
        tblViewLibrary.setItems(songsAsObservable);
        tblViewLibrary.getColumns().addAll(tblViewLibraryColumnTitle, tblViewLibraryColumnArtist, tblViewLibraryColumnGenre, tblViewLibraryColumnDuration);

    }

    private void setPlaylistTable() // This method gets all playlists from database and loeads it into tablePlaylist
    {

        playlistNameCol = new TableColumn<>("Name");
        playlistSongsCol = new TableColumn<>("ID");
        //playlistDurationCol = new TableColumn<>();

        playlistNameCol.setCellValueFactory(new PropertyValueFactory<>("PlaylistName"));
        playlistSongsCol.setCellValueFactory(new PropertyValueFactory<>("PlaylistId"));
        // playlistDurationCol.setCellValueFactory(new PropertyValueFactory<>("durationOfPlaylist"));'
        tblViewPlaylists.setItems(tm.getPlaylistsAsObservable());
        tblViewPlaylists.getColumns().addAll(playlistNameCol, playlistSongsCol);

    }

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

    @FXML
    private void clickDeleteSong(ActionEvent event)
    {
        Songs ToDeleteSong = tblViewLibrary.getSelectionModel().getSelectedItem();   // "some text\nmore text in a new line"
        if (ToDeleteSong != null)
        {
            String name = ToDeleteSong.getTitle() + " " + ToDeleteSong.getArtist();
            Alert alert = new Alert(AlertType.CONFIRMATION, "Click YES to delete the chosen song " + name + " from your library and database.\nClick OK to delete song from Database only\nClick NO to cancel your current action", ButtonType.YES, ButtonType.OK, ButtonType.NO);
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

    @FXML
    private void playSelectedSong() throws UnsupportedAudioFileException, IOException
    {
        
        if (song == null)
        {
            song = ViewSongsOnPlaylist.getSelectionModel().getSelectedItem();
            setMusicPlayer();
            Runnable runnable = new progressUpdate();
            Thread thread = new Thread(runnable);
            thread.start();
        } else if (song == ViewSongsOnPlaylist.getSelectionModel().getSelectedItem())
        {
            mediaPlayer.play();
        } else if (song != ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() && ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() != null)
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
            alert.setTitle("No selection");
            alert.setHeaderText("No song selected");
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

    private void setMusicPlayer()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
        }
        song = ViewSongsOnPlaylist.getSelectionModel().getSelectedItem();
        songPath = song.getSongPath();
        hit = new Media(new File(songPath + song.getTitle()).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);

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

    private void playReleased(MouseEvent event) throws UnsupportedAudioFileException, IOException
    {
        if (!isPlaying)
        {
            isPlaying = true;
            if (ViewSongsOnPlaylist.getSelectionModel().getSelectedItem() != null || song != null)
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

    private void getSliderValue(DragEvent event)
    {
        lblSongTitle.setText(Double.toString(volumeSlider.getValue()));

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

    /**
     * @FXML private void clickToggleUp(MouseEvent event) { if
     * (ViewSongsOnPlaylist.getSelectionModel().getSelectedIndex() > 0) {
     * Playlists playlist =
     * tblViewPlaylists.getSelectionModel().getSelectedItem(); int chosenItem =
     * ViewSongsOnPlaylist.getSelectionModel().getSelectedIndex(); int
     * itemToSwapWith = chosenItem - 1; Songs songActual =
     * ViewSongsOnPlaylist.getSelectionModel().getSelectedItem(); Songs
     * songToSwapWith = ViewSongsOnPlaylist.getItems().get(itemToSwapWith); if
     * (songActual.getsongId() != songToSwapWith.getsongId()) {
     * tm.reCreatePlaylistSongs(songToSwapWith, songActual);
     * ViewSongsOnPlaylist.getItems().clear();
     * ViewSongsOnPlaylist.getItems().addAll(tm.getPlaylistSongs(playlist));
     * ViewSongsOnPlaylist.getSelectionModel().select(itemToSwapWith); } } }
     *
     * @FXML private void clickToggleDown(MouseEvent event) { int sizeOfPlaylist
     * = ViewSongsOnPlaylist.getItems().size(); if
     * (ViewSongsOnPlaylist.getSelectionModel().getSelectedIndex() <
     * sizeOfPlaylist - 1) { Playlists playlist =
     * tblViewPlaylists.getSelectionModel().getSelectedItem(); int chosenItem =
     * ViewSongsOnPlaylist.getSelectionModel().getSelectedIndex(); int
     * itemToSwapWith = chosenItem + 1; Songs songActual =
     * ViewSongsOnPlaylist.getSelectionModel().getSelectedItem(); Songs
     * songToSwapWith = ViewSongsOnPlaylist.getItems().get(itemToSwapWith); if
     * (songActual.getsongId() != songToSwapWith.getsongId()) {
     * tm.reCreatePlaylistSongs(songActual, songToSwapWith);
     * ViewSongsOnPlaylist.getItems().clear();
     * ViewSongsOnPlaylist.getItems().addAll(tm.getPlaylistSongs(playlist));
     * ViewSongsOnPlaylist.getSelectionModel().select(itemToSwapWith); } } }
     */
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
                    currentTimeLabel.setText(currentTimeCalculator(i));
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
        double fractionalProgress = (double) currentTime / (double) songLenght;
        songProgress = new ProgressBar();
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
}
