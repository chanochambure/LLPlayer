package lexrislogic.llplayer.Android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.Models.AddSongElement;
import lexrislogic.llplayer.R;

public class AddSongElementAdapter extends ArrayAdapter<AddSongElement> {
    private int main_color=0;
    private int secondary_color=0;
    private int primary_back_color=0;
    private int secondary_back_color=0;
    public AddSongElementAdapter(Context context, ArrayList<AddSongElement> data,int new_main_color,int new_secondary_color,int prim,int sec) {
        super(context, 0, data);
        primary_back_color=prim;
        secondary_back_color=sec;
        main_color=new_main_color;
        secondary_color=new_secondary_color;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AddSongElement item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_song_element, parent, false);
        }
        View checkBox = convertView.findViewById(R.id.elementSpace);
        if(item.checked){
            checkBox.setBackgroundColor(primary_back_color);
        }else{
            checkBox.setBackgroundColor(secondary_back_color);
        }
        TextView main_text = (TextView) convertView.findViewById(R.id.mainText);
        main_text.setText(item.song.getTitle());
        if(item.song.isValid()){
            main_text.setTextColor(main_color);
        }
        else{
            main_text.setTextColor(secondary_color);
        }
        TextView secondary_text = (TextView) convertView.findViewById(R.id.secondaryText);
        secondary_text.setText(item.song.getArtist()+"\n"+item.song.getAlbum());
        secondary_text.setTextColor(secondary_color);
        return convertView;
    }
}
