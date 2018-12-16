/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

/**
 *
 * @author Hassuni
 */
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.Songs;

public class SONGDAO
{
    private final DBConnectionProvider db;

    public SONGDAO() throws IOException
    {
        db = new DBConnectionProvider();
    }

    public Songs createSong(int songId, String artist, String title, String genre, String duration, String songPath) throws SQLException
    {
        {
            try (Connection con = db.getConnection())
            {
                String sql = "INSERT INTO Songs(songId, title, artist,genre, duration,songPath) VALUES(?,?,?,?,?,?)";
                PreparedStatement ppst = con.prepareStatement(sql);
                ppst.setInt(1, songId);
                ppst.setString(2, title);
                ppst.setString(3, artist);
                ppst.setString(4, genre);
                ppst.setString(5, duration);
                ppst.setString(6, songPath);
                ppst.execute();
                Songs song = new Songs(songId, title, artist, genre, duration, songPath);
                return song;
            }
        }
    }

    public void deleteSong(Songs song) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM Songs WHERE songId = ? " + "DELETE FORM PlaylistSongs WHERE PlaylistSongs.SongID = ?";
            PreparedStatement ppst = con.prepareStatement(sql);
            ppst.setInt(1, song.getsongId());
            ppst.setInt(2, song.getsongId());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Songs> getAllSongs() throws SQLException
    {
        List<Songs> songs = new ArrayList<>();
        try (Connection con = db.getConnection())
        {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Songs;");
            while (rs.next())
            {
                int songId = rs.getInt("songId");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                String duration = rs.getString("duration");
                String songPath = rs.getString("songPath");
                
                Songs song = new Songs(songId, title, artist, genre, duration, songPath);
                songs.add(song);
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return songs;
    }

    public void updateSong(Songs song) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "UPDATE Songs SET title=?,artist=?,genre=?, duration=?,songPath=? WHERE id=?";
            PreparedStatement ppst = con.prepareStatement(sql);
            ppst.setString(1, song.getTitle());
            ppst.setString(2, song.getArtist());
            ppst.setString(3, song.getGenre());
            ppst.setString(4, song.getDuration());
            ppst.setString(5, song.getSongPath());
            ppst.setInt(6, song.getsongId());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Songs> searchSong(String query) throws SQLException
    {
        List<Songs> songs = new ArrayList<>();
        try
        {
            Connection con = db.getConnection();
            String sql = "SELECT * FROM Songs WHERE title like ? OR artist like ?";
            PreparedStatement ppst = con.prepareStatement(sql);
            ppst.setString(1, "%" + query + "%");
            ppst.setString(2, "%" + query + "%");
            ResultSet rs = ppst.executeQuery();
            while (rs.next())
            {
                int songId = rs.getInt("songId");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                String duration = rs.getString("duration");
                String songPath = rs.getString("songPath");
                Songs song = new Songs(songId, title, artist, genre, duration, songPath);
                songs.add(song);
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return songs;
    }

    public Integer nextAvailableSongID() throws SQLException 
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "SELECT MAX(songId) FROM Songs";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int id = 0;
            if (rs.next())
            {
                id = rs.getInt(1);
            }
            return id + 1;
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}