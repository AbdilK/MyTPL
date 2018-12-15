/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

/**
 *
 * @author Hassuni
 */
import java.util.List;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;

public interface BLLLogicFacade
{
    public void createPlaylist(Playlists p);

    public void deletePlaylist(Playlists playlistToDelete);

    public List<Playlists> getAllPlaylists();

    public void updatePlaylist(Playlists p);

    public void createSong(int songId, String title, String artist, String genre, String duration, String Songpath);

    public void deleteSong(Songs song);

    public List<Songs> getAllSongs();

    public void updateSong(Songs song);

    public List<Songs> searchSong(String query);

    public Integer nextAvailableSongID();

    public Integer nextAvailablePlaylistID();

    public List<Songs> getPlaylistSongs(Playlists p);

    public void addSongToPlaylist(Songs s, Playlists p);

    public void deleteSongFromPlaylistSongs(int id);

    public void deletePlaylistFromPlaylistSongs(int id);

    public void reCreatePlaylistSongs(Songs chosen, Songs toSwapWith);
}
