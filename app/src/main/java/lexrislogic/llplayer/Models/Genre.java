package lexrislogic.llplayer.Models;

public class Genre {
    private String name;
    private int songs=0;
    public Genre(String name,int songs){
        this.name=name;
        this.songs=songs;
    }

    public String getName() {
        return name;
    }

    public int getSongs() {
        return songs;
    }
}
