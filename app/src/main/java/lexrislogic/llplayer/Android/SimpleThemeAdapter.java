package lexrislogic.llplayer.Android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.Var;

public class SimpleThemeAdapter extends ArrayAdapter<SimpleElement> {
    private int main_color=0;
    private int secondary_color=0;
    private int primary_back_color=0;
    private int secondary_back_color=0;
    private int primary_text_color=0;
    private int secondary_text_color=0;
    public void refresh_all_colors(int primary_back_color,int secondary_back_color,
                                   int primary_text_color,int secondary_text_color,
                                   int new_main_color,int new_secondary_color){
        this.primary_back_color=primary_back_color;
        this.secondary_back_color=secondary_back_color;
        this.primary_text_color=primary_text_color;
        this.secondary_text_color=secondary_text_color;
        main_color=new_main_color;
        secondary_color=new_secondary_color;
    }
    public SimpleThemeAdapter(Context context, ArrayList<SimpleElement> data,
                              int primary_back_color,int secondary_back_color,
                              int primary_text_color,int secondary_text_color,
                              int new_main_color,int new_secondary_color) {
        super(context, 0, data);
        this.primary_back_color=primary_back_color;
        this.secondary_back_color=secondary_back_color;
        this.primary_text_color=primary_text_color;
        this.secondary_text_color=secondary_text_color;
        main_color=new_main_color;
        secondary_color=new_secondary_color;
    }

    public void set_main_color(int newColor){
        main_color=newColor;
    }
    public void set_secondary_color(int newColor){
        secondary_color=newColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SimpleElement item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.elementImage);
        imageView.setImageResource(item.get_drawable_id());
        switch(item.get_type()){
            case Var.TYPE_COLOR_BACKGROUND_PRIMARY:
                imageView.setBackgroundColor(primary_back_color);
                break;
            case Var.TYPE_COLOR_BACKGROUND_SECONDARY:
                imageView.setBackgroundColor(secondary_back_color);
                break;
            case Var.TYPE_COLOR_PRIMARY_FIRST_TEXT:
                imageView.setBackgroundColor(primary_text_color);
                break;
            case Var.TYPE_COLOR_PRIMARY_SECOND_TEXT:
                imageView.setBackgroundColor(secondary_text_color);
                break;
            case Var.TYPE_COLOR_SECONDARY_FIRST_TEXT:
                imageView.setBackgroundColor(main_color);
                break;
            case Var.TYPE_COLOR_SECONDARY_SECOND_TEXT:
                imageView.setBackgroundColor(secondary_color);
                break;
        }
        TextView main_text = (TextView) convertView.findViewById(R.id.mainText);
        main_text.setText(item.get_main_text());
        main_text.setTextColor(main_color);
        TextView secondary_text = (TextView) convertView.findViewById(R.id.secondaryText);
        secondary_text.setText(item.get_secondary_text());
        secondary_text.setTextColor(secondary_color);
        return convertView;
    }
}
