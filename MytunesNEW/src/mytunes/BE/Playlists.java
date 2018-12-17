/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.util.ArrayList;

/**
 *
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */
public class Playlists {

   

    private int PlaylistId;
    private String PlaylistName;
    private int SongAmountPL;
    
    
    private ArrayList<Songs> PlaylistSongs;

    public Playlists(int PlaylistId, String PlaylistName) {
        this.PlaylistId = PlaylistId;
        this.PlaylistName = PlaylistName;
        PlaylistSongs = new ArrayList<>();
    }

    
    public int getPlaylistId() 
    {
        return PlaylistId;
    }

    public void setPlaylistId(int PlaylistId)
    {
        this.PlaylistId = PlaylistId;
    }
    

    public String getPlaylistName() {
        return PlaylistName;
    }

    public void setPlaylistName(String PlaylistName) {
        this.PlaylistName = PlaylistName;
    }
   
    public int getSongAmountPL()
    {
        return SongAmountPL;
    }

    public void setSongAmountPL(int SongAmountPL)
    {
        this.SongAmountPL = SongAmountPL;
    }


    public ArrayList<Songs> getPlaylist() {
        return PlaylistSongs;
    }

    public void addSongToPlaylist(Songs songs) {
        PlaylistSongs.add(songs);
    }

    public void removeSongFromPlaylist(Songs songs) {
        for (Songs song1 : PlaylistSongs) {
            if (song1.equals(songs)) {
                PlaylistSongs.remove(songs);
            }
        }
    }

    @Override
    public String toString(){
        return PlaylistName;
    }

  
}
