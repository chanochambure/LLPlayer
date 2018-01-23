package lexrislogic.llplayer.Models;

import java.util.ArrayList;

public class Artist {
    private String name;
    private ArrayList<Album> albums;
    public Artist(String name,ArrayList<Album> albums){
        this.name=name;
        this.albums=albums;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }
}
