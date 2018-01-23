package lexrislogic.llplayer.Android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.Models.Album;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.Language;

public class AlbumElementAdapter extends ArrayAdapter<Album> {
    private int main_color=0;
    private int secondary_color=0;
    private Context context;
    public AlbumElementAdapter(Context context, ArrayList<Album> data,int new_main_color,int new_secondary_color) {
        super(context, 0, data);
        this.context=context;
        main_color=new_main_color;
        secondary_color=new_secondary_color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Album item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.elementImage);
        TextView main_text = (TextView) convertView.findViewById(R.id.mainText);
        TextView secondary_text = (TextView) convertView.findViewById(R.id.secondaryText);
        main_text.setTextColor(main_color);
        secondary_text.setTextColor(secondary_color);
        if(item.getName()!=null) {
            Drawable drawable=Fun.get_album_art(context, item.getId());
            if(drawable!=null) {
                imageView.setImageDrawable(drawable);
            } else {
                imageView.setImageResource(R.mipmap.no_album);
            }
            main_text.setText(item.getName());
            secondary_text.setText(item.getArtist());
        } else {
            imageView.setImageResource(R.mipmap.all_album_icon);
            main_text.setText(Language.getInstance().AllSongs);
            secondary_text.setText(item.getArtist());
        }
        return convertView;
    }
}