package lexrislogic.llplayer.Android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.R;

public class timeSpinnerAdapter<T> extends ArrayAdapter<T>
{
    private int backgroundColor;
    private int textColor;
    public timeSpinnerAdapter(Context ctx, ArrayList<T> objects,int backgroundColor,int textColor)
    {
        super(ctx, R.layout.time_element,R.id.textTimeElement, objects);
        this.textColor=textColor;
        this.backgroundColor=backgroundColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        RelativeLayout back = (RelativeLayout)view.findViewById(R.id.timeElementBackground);
        back.setBackgroundColor(backgroundColor);
        TextView text = (TextView)view.findViewById(R.id.textTimeElement);
        text.setTextColor(textColor);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        RelativeLayout back = (RelativeLayout)view.findViewById(R.id.timeElementBackground);
        back.setBackgroundColor(backgroundColor);
        TextView text = (TextView)view.findViewById(R.id.textTimeElement);
        text.setTextColor(textColor);
        return view;
    }
}
