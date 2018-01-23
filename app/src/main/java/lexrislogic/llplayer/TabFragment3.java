package lexrislogic.llplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class TabFragment3 extends Fragment {
    private final static int REQUEST_CODE_NEW_UPDATED_SONGS=781;
    private final static int REQUEST_CODE_LOST_SONGS=782;
    public SimpleElementAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement new_songs=new SimpleElement(
            Var.TYPE_MAINTENANCE_NEW_SONG,
            R.mipmap.new_songs_icon,
            Language.getInstance().MaintenanceNewSongsText,
            Language.getInstance().MaintenanceNewSongsSubText,
            -1);
    public SimpleElement lost_songs=new SimpleElement(
            Var.TYPE_MAINTENANCE_LOST_SONGS,
            R.mipmap.lost_songs_icon,
            Language.getInstance().MaintenanceLostSongsText,
            Language.getInstance().MaintenanceLostSongsSubText,
            -1);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        if(view!=null) {
            element_list=new ArrayList<>();
            DBHandler db=new DBHandler(getContext());
            view.setBackgroundColor(db.get_secondary_background_color());

            adapter=new SimpleElementAdapter(getContext(),element_list,
                    db.get_main_primary_text_color(),
                    db.get_main_secondary_text_color());

            ListView listView= (ListView) view.findViewById(R.id.maintenance_ListView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                    Intent intent;
                    switch (selected_item.get_type()){
                        case Var.TYPE_MAINTENANCE_NEW_SONG:
                            intent = new Intent(getContext(), UpdatedSongList.class);
                            startActivityForResult(intent, REQUEST_CODE_NEW_UPDATED_SONGS);
                            break;
                        case Var.TYPE_MAINTENANCE_LOST_SONGS:
                            intent = new Intent(getContext(), LostSongList.class);
                            startActivityForResult(intent, REQUEST_CODE_LOST_SONGS);
                            break;
                    }
                }
            });
        }
        load_element_list();
        return view;
    }
    public void load_element_list(){
        element_list.add(new_songs);
        element_list.add(lost_songs);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_NEW_UPDATED_SONGS:
                break;
            case REQUEST_CODE_LOST_SONGS:
                break;
        }
    }
}