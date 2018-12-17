
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
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */
public class PLSDAO
{
   DBConnectionProvider db;

    public PLSDAO() throws IOException
    {
        db = new DBConnectionProvider();
    }
    // returns all the songs of the playlist that have been selected.
    public List<Songs> getPlaylistSongs(Playlists playlist) throws SQLException 
    {
        List<Songs> songs = new ArrayList<>();
        try
        {
            Connection con = db.getConnection();
            String sql = "SELECT Songs.songId, title, artist, genre, duration, songPath FROM Songs " +
                "JOIN PlaylistSongs ON PlaylistSongs.SongID = Songs.songId " +
                "JOIN Playlists ON PlaylistSongs.PlaylistID = Playlists.PlaylistID WHERE Playlists.PlaylistID = ?";
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
    // when we select a song and want to insert it into our playlistsongs
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
    // when we delete song, we also want to delete every trace of it from playlistSongs, which is done by deleting song id.
    public void deleteSongFromPlaylistSongs(int id) throws SQLException  
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM PlaylistSongs WHERE SongID=?";                  
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, id);
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // when we delete a playlist, we want to delete every record from playlistSongs, which is having the deleted playlist id.
    public void deletePlaylistFromPlaylistSongs(int id) throws SQLException 
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM PlaylistSongs WHERE PlaylistID=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, id);
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // This method switches the positions of two songs in the playlist
    public void reCreatePlaylistSongs(Songs selected, Songs replace) throws SQLException 
    {
        try
        {
            Connection con = db.getConnection();
            int selectedID = selected.getsongId();   
            int replaceID = replace.getsongId();   
            String sql = "UPDATE PlaylistSongs SET SongID = ? WHERE ID = ?";    
            String sqll = "UPDATE PlaylistSongs SET SongID = ? WHERE ID = ?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, replaceID);                       
            ppst.setInt(2, selected.getPlaylistUniqueID());     
            PreparedStatement ppst2 = con.prepareCall(sqll);    
            ppst2.setInt(1, selectedID);
            ppst2.setInt(2, replace.getPlaylistUniqueID());
            ppst.execute();
            ppst2.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
} 

