/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;
import mytunes.DAL.PLDAO;
import mytunes.DAL.SONGDAO;
import mytunes.DAL.PLSDAO;

public class TunesManager implements BLLLogicFacade
{
    private static TunesManager instance;
    private final SONGDAO SongDAO;
    private final PLDAO PlaylistDAO;
    private final PLSDAO PlaylistSongsDAO;

    public TunesManager() throws IOException
    {
        SongDAO = new SONGDAO();
        PlaylistDAO = new PLDAO();
        PlaylistSongsDAO = new PLSDAO();
    }

    @Override
    public void createPlaylist(Playlists playlist)
    {
        try
        {
            PlaylistDAO.createPlaylist(playlist);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deletePlaylist(Playlists playlistToDelete)
    {
        try
        {
            PlaylistDAO.deletePlaylist(playlistToDelete);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Playlists> getAllPlaylists()
    {
        List<Playlists> playlists;
        try
        {
            System.out.println("All playlists has been loaded ");
            return playlists = PlaylistDAO.getAllPlaylists();
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void updatePlaylist(Playlists playlist)
    {
        try
        {
            PlaylistDAO.updatePlaylist(playlist);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createSong(int songId, String title, String artist, String genre, String duration, String songPath)
    {
        try
        {
            SongDAO.createSong(songId, title, artist, genre, duration, songPath);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteSong(Songs song)
    {
        try
        {
            SongDAO.deleteSong(song);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Songs> getAllSongs()
    {
        List<Songs> allsongs;
        try
        {
            return allsongs = SongDAO.getAllSongs();
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void updateSong(Songs song)
    {
        try
        {
            SongDAO.updateSong(song);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Songs> searchSong(String query)
    {
        List<Songs> searchResult;
        try
        {
            return searchResult = SongDAO.searchSong(query);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Integer nextAvailableSongID()
    {
        try
        {
            return SongDAO.nextAvailableSongID();
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Integer nextAvailablePlaylistID()
    {
        try
        {
            return PlaylistDAO.nextAvailablePlaylistID();
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Songs> getPlaylistSongs(Playlists playlist)
    {
        try
        {
            return PlaylistSongsDAO.getPlaylistSongs(playlist);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void addSongToPlaylist(Songs song, Playlists playlist)
    {
        try
        {
            PlaylistSongsDAO.addSongToPlaylist(song, playlist);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteSongFromPlaylistSongs(int id)
    {
        try
        {
            PlaylistSongsDAO.deleteSongFromPlaylistSongs(id);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deletePlaylistFromPlaylistSongs(int id)
    {
        try
        {
            PlaylistSongsDAO.deletePlaylistFromPlaylistSongs(id);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reCreatePlaylistSongs(Songs selected, Songs replace)
    {
        try
        {
            PlaylistSongsDAO.reCreatePlaylistSongs(selected, replace);
        } catch (SQLException ex)
        {
            Logger.getLogger(TunesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
