package mytunes.GUI.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import mytunes.BE.Playlists;
import mytunes.GUI.model.TuneModel;

public class PlaylistWindowController implements Initializable
{
    @FXML
    private TextField NamePlaylist;
    private MainWindowController MainWController;
    private boolean isEditing = false;
    private int PlaylistNewID;
    private TuneModel tm;
    private Playlists playlist;

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

    @FXML
    private void clickCancelPlaylist (ActionEvent event) 
    {
        isEditing = false;
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void clickSavePlaylist(ActionEvent event) 
    {
        if (!isEditing)
        {
            if (!"".equals(NamePlaylist.getText()))
            {
                Playlists playlist = new Playlists(tm.nextAvailablePlaylistID(), NamePlaylist.getText());
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

    public void setController(MainWindowController controller, boolean isEditing, int playlistID) 
    {
        this.MainWController = controller;
        this.isEditing = isEditing;
        this.PlaylistNewID = playlistID;
    }
}