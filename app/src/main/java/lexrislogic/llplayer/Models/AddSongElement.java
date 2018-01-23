package lexrislogic.llplayer.Models;


public class AddSongElement {
    public Song song=null;
    public boolean checked=false;
    public AddSongElement(Song song,boolean checked){
        this.song=song;
        this.checked=checked;
    }
}
