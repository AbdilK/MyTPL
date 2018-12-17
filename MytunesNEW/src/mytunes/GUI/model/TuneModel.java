/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;
import mytunes.BLL.TunesManager;
import mytunes.BLL.BLLLogicFacade;

/*
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */


public class TuneModel {

    public final BLLLogicFacade mytune;
    public ObservableList olSongs;
    public ObservableList olPlaylists;
    private Playlists playlist;
    private Songs song;
    private List<String> AllGenres;
    public static TuneModel instance;

    public TuneModel() throws IOException {
        AllGenres = new ArrayList();
        addNewGenre();
        olSongs = FXCollections.observableArrayList();
        olPlaylists = FXCollections.observableArrayList();
        mytune = new TunesManager();
    }

    public static TuneModel getInstance() // IF there is an existing mytunesModel we are returning instance of it, otherwise we are creating and returning new one.
    {
        if (instance == null) {
            try {
                instance = new TuneModel();
            } catch (IOException ex) {
                Logger.getLogger(TuneModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    public ObservableList getSongsAsObservable() // We are getting all songs and returning it as ObservableList
    {
        olSongs.clear();
        olSongs.addAll(getAllSongs());
        return olSongs;
    }

    public ObservableList getPlaylistsAsObservable() // We are getting all playlists and returning it as ObservableList
    {
        olPlaylists.clear();
        olPlaylists.addAll(getAllPlaylists());
        return olPlaylists;
    }

    public void createPlaylist(Playlists playlist) {
        mytune.createPlaylist(playlist);
    }

    public void deletePlaylist(Playlists playlistToDelete) {
        mytune.deletePlaylist(playlistToDelete);
    }

    public List<Playlists> getAllPlaylists() {
        List<Playlists> p = mytune.getAllPlaylists();
        return p;
    }

    public void updatePlaylist(Playlists playlist) {
        mytune.updatePlaylist(playlist);
    }

    public void createSong(int songId, String title, String artist, String genre, String duration, String songPath) {
        mytune.createSong(songId, title, artist, genre, duration, songPath);
    }

    public void deleteSong(Songs song) {
        mytune.deleteSong(song);
    }

    public List<Songs> getAllSongs() {
        List<Songs> song = mytune.getAllSongs();
        return song;
    }

    public void updateSong(Songs song) {
        mytune.updateSong(song);
    }

    public List<Songs> searchSong(String query) {
        List<Songs> s = mytune.searchSong(query);
        return s;
    }

    public Integer nextAvailableSongID() {
        return mytune.nextAvailableSongID();
    }

    public Integer nextAvailablePlaylistID() {
        return mytune.nextAvailablePlaylistID();
    }

    public List<Songs> getPlaylistSongs(Playlists playlist) {
        return mytune.getPlaylistSongs(playlist);
    }

    public void addSongToPlaylist(Songs song, Playlists playlist) {
        mytune.addSongToPlaylist(song, playlist);
    }

    public void deleteSongFromPlaylistSongs(int id) {
        mytune.deleteSongFromPlaylistSongs(id);
    }

    public void deletePlaylistFromPlaylistSongs(int id) {
        mytune.deletePlaylistFromPlaylistSongs(id);
    }

    public void reCreatePlaylistSongs(Songs chosen, Songs toSwapWith) {
        mytune.reCreatePlaylistSongs(chosen, toSwapWith);
    }

    public Playlists getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlists playlist) {
        this.playlist = playlist;
    }

    public Songs getSong() {
        return song;
    }

    public void setSong(Songs song) {
        this.song = song;
    }

    public List<String> getGenres() {
        return AllGenres;
    }

    public void addNewGenre(String genre) {
        AllGenres.add(genre);
    }

    private void addNewGenre() {
        AllGenres.add("Pop");
        AllGenres.add("Metal");
        AllGenres.add("Hip hop");
        AllGenres.add("Minimal Techno");
        AllGenres.add("Rap");
        AllGenres.add("Classical");
        AllGenres.add("Country");
        AllGenres.add("Ballads");
        AllGenres.add("Dance");
        AllGenres.add("Love");
        AllGenres.add("Gospel");
        AllGenres.add("Jazz");

    }

    
    
}

