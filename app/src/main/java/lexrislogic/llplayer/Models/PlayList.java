package lexrislogic.llplayer.Models;

import java.io.Serializable;

public class PlayList implements Serializable {
    private int id;
    private String name;
    public PlayList(String name){
        this.id=-1;
        this.name=name;
    }
    public PlayList(int id,String name){
        this.id=id;
        this.name=name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
