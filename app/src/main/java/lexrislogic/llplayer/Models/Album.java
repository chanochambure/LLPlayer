package lexrislogic.llplayer.Models;

public class Album {
    private long id;
    private String name;
    private String artist;
    public Album(long id,String name,String artist){
        this.id=id;
        this.name=name;
        this.artist=artist;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

}
