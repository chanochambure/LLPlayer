package lexrislogic.llplayer.Models;

public class SimpleElement {
    private int type;
    private int drawable_id=0;
    private String main_text="";
    private String secondary_text="";
    private int metadata;
    public SimpleElement(int new_type ,int new_drawable_id,String new_main_text,String new_secondary_text,int new_metadata){
        type=new_type;
        drawable_id=new_drawable_id;
        main_text=new_main_text;
        secondary_text=new_secondary_text;
        metadata=new_metadata;
    }

    public int get_drawable_id() {
        return drawable_id;
    }

    public String get_main_text() {
        return main_text;
    }

    public String get_secondary_text() {
        return secondary_text;
    }

    public void set_main_text(String main_text) {
        this.main_text=main_text;
    }

    public void set_secondary_text(String secondary_text) {
        this.secondary_text=secondary_text;
    }

    public int get_type() {
        return type;
    }

    public int getMetadata() {
        return metadata;
    }
}
