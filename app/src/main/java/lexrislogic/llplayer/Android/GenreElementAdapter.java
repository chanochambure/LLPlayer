package lexrislogic.llplayer.Android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.Models.Genre;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.Language;

public class GenreElementAdapter extends ArrayAdapter<Genre> {
    private int main_color=0;
    private int secondary_color=0;
    public GenreElementAdapter(Context context, ArrayList<Genre> data,int new_main_color,int new_secondary_color) {
        super(context, 0, data);
        main_color=new_main_color;
        secondary_color=new_secondary_color;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Genre item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.no_image_element, parent, false);
        }
        TextView main_text = (TextView) convertView.findViewById(R.id.mainText);
        main_text.setText(item.getName());
        main_text.setTextColor(main_color);
        TextView secondary_text = (TextView) convertView.findViewById(R.id.secondaryText);
        secondary_text.setText(String.valueOf(item.getSongs())+" "+ Language.getInstance().GenreTotalSongs);
        secondary_text.setTextColor(secondary_color);
        return convertView;
    }
}


