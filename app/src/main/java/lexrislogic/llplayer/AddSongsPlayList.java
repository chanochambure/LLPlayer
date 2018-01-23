package lexrislogic.llplayer;

import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.AddSongElementAdapter;
import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Models.AddSongElement;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class AddSongsPlayList extends AppCompatActivity {
    private DBHandler db=null;
    private ArrayList<AddSongElement> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_songs_play_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_songs_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().addSongsPlayList);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.add_songs_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        TextView emptyText=(TextView) findViewById(R.id.addSongsEmptyElement);
        emptyText.setText(Language.getInstance().EmptyListText);
        emptyText.setTextColor(db.get_main_secondary_text_color());
        ListView listView = (ListView) view.findViewById(R.id.addSongsListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.addSongsEmptyElement));
        listView.setFastScrollAlwaysVisible(true);
        final ArrayList<Song> song=db.get_songs(Var.TYPE_ALL_SONGS,null,null,null,-1);
        for(Song son:song){
            list.add(new AddSongElement(son,false));
        }
        final AddSongElementAdapter adapter = new AddSongElementAdapter(this,list, db.get_main_primary_text_color(), db.get_main_secondary_text_color(),db.get_primary_background_color(),db.get_secondary_background_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddSongElement selected_item = (AddSongElement) adapterView.getItemAtPosition(i);
                selected_item.checked = !selected_item.checked;
                adapter.notifyDataSetChanged();
            }
        });
        Button applyChanges=(Button) findViewById(R.id.addSongsAction);
        applyChanges.setBackgroundColor(db.get_primary_background_color());
        applyChanges.setTextColor(db.get_primary_text_color());
        applyChanges.setText(Language.getInstance().AddSongsItem);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(AddSongElement songElement:list){
                    if(songElement.checked){
                        Var.getInstance().playListSongs.add(songElement.song);
                    }
                }
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onDestroy(){
        if(db!=null) {
            db.close();
            db=null;
        }
        super.onDestroy();
    }
}
