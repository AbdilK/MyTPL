package mytunes.GUI.controller;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mytunes.BE.Playlists;
import mytunes.GUI.model.TuneModel;

/*
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */


public class PlaylistWindowController implements Initializable
{
    private TextField NamePlaylist;
    private MainWindowController MainWController;
    private boolean isEditing = false;
    private int PlaylistNewID;
    private TuneModel tm;
    private Playlists playlist;
    @FXML
    private TextField txtNamePlaylist;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        MainWController = new MainWindowController();
        tm = TuneModel.getInstance();
        playlist = tm.getPlaylist();
        if (playlist != null)
        {
            NamePlaylist.setText(playlist.getPlaylistName());
        }
    }
// This will close the playlist window
    @FXML
    private void clickCancelPlaylist (ActionEvent event) 
    {
        isEditing = false;
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
// This saves the new data you put in the playlist window
    @FXML
    private void clickSavePlaylist(ActionEvent event) 
    {
        if (!isEditing)
        {
            if (!txtNamePlaylist.getText().isEmpty())
            {
                Playlists playlist = new Playlists(tm.nextAvailablePlaylistID(), txtNamePlaylist.getText());
                tm.createPlaylist(playlist);
                MainWController.refreshTablePlaylist();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
        } else
        {
            if (!"".equals(NamePlaylist.getText()))
            {
                Playlists playlist = new Playlists(PlaylistNewID, NamePlaylist.getText());
                tm.updatePlaylist(playlist);
                MainWController.refreshTablePlaylist();
                ((Node) (event.getSource())).getScene().getWindow().hide();
                isEditing = false;
            }
        }
    }

    public void setController(MainWindowController controller, boolean isEditing, int playlistID) // This method allows us to get connection with our MainWindowController and will check whether we are creating or editing
    {
        this.MainWController = controller;
        this.isEditing = isEditing;
        this.PlaylistNewID = playlistID;
    }
}