/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;



/**
 *
 * @author Abdil-K, Bjarne666, Hassuni8, KerimTopci
 */
public class Songs
{

    private int songId;
    private String title;
    private String genre;
    private String artist;
    private String duration;
    private int locationID;
    private String songPath;
    private String readDuration;
    private int playlistUniqueID;

    public Songs(int songId, String title, String artist, String genre, String duration, String songPath)
    {
        this.songId = songId;
        this.title = title;
        this.genre = genre;
        this.artist = artist;
        this.duration = duration;
        this.songPath = songPath;
        //readDuration = readableDuration(this.duration);
    }

    public Songs()
    {

    }

    public String getReadDuration()
    {
        return readDuration;
    }

    public int getPlaylistUniqueID()
    {
        return playlistUniqueID;
    }

    public void setPlaylistUniqueID(int playlistUniqueID)
    {
        this.playlistUniqueID = playlistUniqueID;
    }

    public int getsongId()
    {
        return songId;
    }

    public void setsongId(int songId)
    {
        this.songId = songId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public int getLocationID()
    {
        return locationID;
    }

    public void setLocation(int locationID)
    {
        this.locationID = locationID;
    }

    public String getSongPath()
    {
        return songPath;
    }

    public void setSongPath(String songPath)
    {
        this.songPath = songPath;
    }

    @Override
    public String toString()
    {
        String Songs = this.title
                + " | " + this.artist;

        return Songs;
    }
}
