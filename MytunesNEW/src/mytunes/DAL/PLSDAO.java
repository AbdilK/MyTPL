/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.*;

/**
 *
 * @author Hassuni
 */
public class PLSDAO
{
   DBConnectionProvider db;

    public PLSDAO() throws IOException
    {
        db = new DBConnectionProvider();
    }

    public List<Songs> getPlaylistSongs(Playlists playlist) throws SQLException // returns all song of selected playlist
    {
        List<Songs> songs = new ArrayList<>();
        try
        {
            Connection con = db.getConnection();
            String sql = "SELECT songs.songId, title, artist, genre, duration, songPath FROM PlaylistSongs JOIN Songs  ON PlaylistSongs.SongID =  Songs.songId JOIN  Playlists ON playlists.PlaylistID = Playlists.PlaylistID where Playlists.PlaylistID=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, playlist.getPlaylistId());
            ResultSet rs = ppst.executeQuery();
            while (rs.next())
            {
                int songId = rs.getInt("songId");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                String duration = rs.getString("duration");
                String songPath = rs.getString("songPath");
                int playlistUniqueID = rs.getInt("songId");
                Songs song = new Songs(songId, title, artist, genre, duration, songPath);
                song.setPlaylistUniqueID(playlistUniqueID);
                songs.add(song);
                
            }
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return songs;
        
    }

    public void addSongToPlaylist(Songs song, Playlists playlist) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "INSERT INTO PlaylistSongs (SongID, PlaylistID) VALUES (?,?)";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, song.getsongId());
            ppst.setInt(2, playlist.getPlaylistId());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteSongFromPlaylistSongs(int id) throws SQLException  // After we delete song, we want to also delete every record from playlistSongs which is having deleted song id
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM PlaylistSongs WHERE id=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, id);
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePlaylistFromPlaylistSongs(int id) throws SQLException // After we delete playlist, we want to also delete every record from playlistSongs which is having deleted playlist id
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM playlistSongs WHERE playlistID=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, id);
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reCreatePlaylistSongs(Songs selected, Songs Replace) throws SQLException // This method switches positions of two songs in the playlist
    {
        try
        {
            Connection con = db.getConnection();
            int selectedID = selected.getsongId();   
            int ReplaceID = Replace.getsongId();   
            String sql = "UPDATE PlaylistSongs SET SongID = ? WHERE id = ?";    
            String sql2 = "UPDATE PlaylistSongs SET SongID = ? WHERE id = ?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, ReplaceID);                       
            ppst.setInt(2, selected.getPlaylistUniqueID());     
            PreparedStatement ppst2 = con.prepareCall(sql2);    
            ppst2.setInt(1, selectedID);
            ppst2.setInt(2, Replace.getPlaylistUniqueID());
            ppst.execute();
            ppst2.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} 

