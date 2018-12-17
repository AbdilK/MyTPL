
package mytunes.DAL;

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
import mytunes.BE.Playlists;
import mytunes.BE.Songs;

/*
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */

public class PLDAO
{
    private DBConnectionProvider db;
    private final PLSDAO DankPlaylist;


    public PLDAO() throws IOException
    {
        db = new DBConnectionProvider();
        DankPlaylist = new PLSDAO();
    }
// This method creats a playlist in and stores it in our database
    public void createPlaylist(Playlists playlist) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "INSERT INTO Playlists VALUES (?,?)";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, playlist.getPlaylistId());
            ppst.setString(2, playlist.getPlaylistName());
            ppst.executeUpdate();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// This method deletes the selected playlist from our database
    public void deletePlaylist(Playlists playlistToDelete) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM Playlists WHERE PlaylistID=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, playlistToDelete.getPlaylistId());
            ppst.executeUpdate();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// This method gets all the playlists that have been stored from our databse, and loads them when you run the program
    public List<Playlists> getAllPlaylists() throws SQLException {
        List<Playlists> p = new ArrayList<>();
        try (Connection con = db.getConnection()){
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Playlists");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                Playlists plyls = new Playlists(id, name);
                p.add(plyls);
            }

        } catch (SQLServerException ex) {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
// This method allows us to update our playlist and stores the new and updated information in the database
    public void updatePlaylist(Playlists p) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "UPDATE Playlists SET PlaylistName=? WHERE PlaylistID=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setString(1, p.getPlaylistName());
            ppst.setInt(2, p.getPlaylistId());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// This method finds the next available ID and gives it to the playlist
    public Integer nextAvailablePlaylistID() throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "SELECT MAX(PlaylistID) FROM Playlists";
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