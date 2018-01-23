package lexrislogic.llplayer.Models;

import java.io.Serializable;

public class PlayMode implements Serializable {
    private int id;
    private String name;
    private String data;

    public PlayMode(String name,String data){
        this.id=-1;
        this.name=name;
        this.data=data;
    }

    public PlayMode(int id,String name,String data){
        this.id=id;
        this.name=name;
        this.data=data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
