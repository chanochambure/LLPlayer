package lexrislogic.llplayer.Android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.Models.SimpleStructure;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.Language;

public class SimpleStructureElementAdapter extends ArrayAdapter<SimpleStructure> {
    private int main_color=0;
    public SimpleStructureElementAdapter(Context context, ArrayList<SimpleStructure> data,int new_main_color) {
        super(context, 0, data);
        main_color=new_main_color;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SimpleStructure item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_structure_layout, parent, false);
        }
        Spinner spinnerViewPath = (Spinner) convertView.findViewById(R.id.spinnerStorage);
        spinnerViewPath.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, item.path_list));
        spinnerViewPath.setSelection(0);
        spinnerViewPath.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.new_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        TextView replace_text = (TextView) convertView.findViewById(R.id.replaceText);
        replace_text.setText(Language.getInstance().replaceTextBackup);
        replace_text.setTextColor(main_color);
        TextView main_text = (TextView) convertView.findViewById(R.id.mainText);
        main_text.setText(item.path);
        main_text.setTextColor(main_color);
        return convertView;
    }
}
