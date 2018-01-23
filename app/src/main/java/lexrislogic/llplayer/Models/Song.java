package lexrislogic.llplayer.Models;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;

import lexrislogic.llplayer.Singleton.Fun;

public class Song implements Serializable{
    private int id;
    private long phone_id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private long album_id;
    private String data;
    private long seconds;
    private int bookmark;
    private String last_mod_date;
    private int playlist_id=-1;
    private boolean playable=true;
    public Song(){}
    public Song(long new_phone_id,
                String new_title, String new_artist, String new_album,String new_genre,long new_album_id,
                String new_data) {
        id=-1;
        phone_id=new_phone_id;
        title=new_title;
        artist=new_artist;
        album=new_album;
        genre=new_genre;
        album_id=new_album_id;
        data=new_data;
        seconds=0;
        bookmark=0;
        last_mod_date= Fun.getInstance().date_to_string(Calendar.getInstance());
    }
    public Song(int new_id, long new_phone_id,
                String new_title, String new_artist, String new_album,String new_genre,long new_album_id,
                String new_data,
                long new_seconds, int new_bookmark, String new_last_mod_date) {
        id=new_id;
        phone_id=new_phone_id;
        title=new_title;
        artist=new_artist;
        album=new_album;
        genre=new_genre;
        album_id=new_album_id;
        data=new_data;
        seconds=new_seconds;
        bookmark=new_bookmark;
        last_mod_date=new_last_mod_date;
    }

    public int getId() {
        return id;
    }

    public long getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(long phone_id) {
        this.phone_id = phone_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSeconds() {
        return seconds;
    }

    public void addSeconds(long seconds) {
        this.seconds += seconds;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public String get_last_mod_date() {
        return last_mod_date;
    }

    public int set_mod_date(String new_last_mod_date)
    {
        if(new_last_mod_date!=null){
            last_mod_date=new_last_mod_date;
            return 1;
        }
        return 0;
    }
    public boolean getHasArtist(){
        return !artist.equals("<unknown>");
    }

    public boolean getHasAlbum(){
        return !album.equals("Music") && !album.equals("<unknown>");
    }

    public boolean getHasGenre(){
        return genre.length()>0;
    }
    public boolean isValid(){
        if(playable) {
            try {
                File mp3file = new File(data);
                return playable=mp3file.exists();
            } catch (Exception ignore) {
            }
        }
        return false;
    }

    public byte[] getAlbumArt() {
        byte[] drawable=null;
        try{
            Mp3File mp3file = new Mp3File(data);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                drawable=id3v2Tag.getAlbumImage();
            }
        }catch (InvalidDataException | IOException | UnsupportedTagException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public String getLyrics(){
        String lyrics=null;
        try{
            Mp3File mp3file = new Mp3File(data);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                lyrics=id3v2Tag.getLyrics();
            }
        }catch (InvalidDataException | IOException | UnsupportedTagException e) {
            e.printStackTrace();
        }
        return lyrics;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public int getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public String getGenre() {
        return  genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void set_all_data(long new_phone_id,
                String new_title, String new_artist, String new_album,String new_genre,long new_album_id,
                String new_data) {
        id=-1;
        phone_id=new_phone_id;
        title=new_title;
        artist=new_artist;
        album=new_album;
        genre=new_genre;
        album_id=new_album_id;
        data=new_data;
        seconds=0;
        bookmark=0;
        last_mod_date= Fun.getInstance().date_to_string(Calendar.getInstance());
    }
    public void setId(int id){
        this.id=id;
    }
}
