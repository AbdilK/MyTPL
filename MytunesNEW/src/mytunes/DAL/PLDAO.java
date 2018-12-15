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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mytunes.BE.Playlists;
import mytunes.BE.Songs;

public class PLDAO
{
    private DBConnectionProvider db;
    private final PLSDAO DankPlaylist;


    public PLDAO() throws IOException
    {
        db = new DBConnectionProvider();
        DankPlaylist = new PLSDAO();
    }

    public void createPlaylist(Playlists p) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "INSERT INTO Playlists (id,name) VALUES (?,?)";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, p.getPlaylistId());
            ppst.setString(2, p.getPlaylistName());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePlaylist(Playlists playlistToDelete) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "DELETE FROM Playlists WHERE id=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setInt(1, playlistToDelete.getPlaylistId());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Playlists> getAllPlaylists() throws SQLException {
        List<Playlists> p = new ArrayList<>();
        try (Connection con = db.getConnection()){
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Playlists");
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                Playlists plyls = new Playlists(id, name);
                //ikke færdig ^
            }

        } catch (SQLServerException ex) {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    public void updatePlaylist(Playlists p) throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "UPDATE Playlists SET name=? WHERE id=?";
            PreparedStatement ppst = con.prepareCall(sql);
            ppst.setString(1, p.getPlaylistName());
            ppst.setInt(2, p.getPlaylistId());
            ppst.execute();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(SONGDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer nextAvailablePlaylistID() throws SQLException
    {
        try
        {
            Connection con = db.getConnection();
            String sql = "SELECT MAX(id) FROM Playlists";
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
    /**public String calculatePlaylistDuration(Playlists p) throws SQLException  { 
        int h = 0;
        int min = 0;
        int sec = 0;
        String first;
        String second;
        String third;
        String songTime;
        int wholeSecs = 0;
        
        for (Songs song : PLSDAO.getPlaylistSongs(p)) {
            songTime = song.getDuration();

            wholeSecs += 60*Integer.parseInt(songTime.substring(0, songTime.indexOf(":")));
            wholeSecs += Integer.parseInt(songTime.substring(songTime.indexOf(":") + 1, songTime.length()));

        }
        h = wholeSecs/3600;
        wholeSecs -= h*3600;
        min = wholeSecs/60;
        wholeSecs -= min*60;
        sec = wholeSecs;
        if(h<10)first="0"+h;
        else first = ""+h;
        if(min<10)second="0"+min;
        else second = ""+min;      
        if(sec<10)third="0"+sec;
        else third = ""+sec;
        return first + ":" + second + ":" + third;
    }**/
    
}