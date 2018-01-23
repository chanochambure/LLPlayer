package lexrislogic.llplayer.Models;

public class BandEqualizer {
    private short id;
    private short data;

    public BandEqualizer(short id,short data){
        this.id=id;
        this.data=data;
    }

    public short getId() {
        return id;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }
}
