package lexrislogic.llplayer.Models;

import java.util.ArrayList;

public class SimpleStructure {
    public String path="";
    public int new_position=0;
    public ArrayList<String> path_list=new ArrayList<>();
    public SimpleStructure(String path,ArrayList<String> path_list)
    {
        this.path=path;
        this.path_list.addAll(path_list);
    }
}
