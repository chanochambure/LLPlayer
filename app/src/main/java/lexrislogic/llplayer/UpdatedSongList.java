package lexrislogic.llplayer;

import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SongElementAdapter;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Language;

public class UpdatedSongList extends AppCompatActivity {
    private DBHandler db=null;
    private Toast toast=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updated_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.updated_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().MaintenanceNewSongsText);
        setSupportActionBar(toolbar);
        toast = Toast.makeText(UpdatedSongList.this, "", Toast.LENGTH_SHORT);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.updated_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        ListView listView = (ListView) view.findViewById(R.id.updatedListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setFastScrollAlwaysVisible(true);
        SongElementAdapter adapter = new SongElementAdapter(this, db.get_recent_songs(), db.get_main_primary_text_color(), db.get_main_secondary_text_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song selected_item = (Song) parent.getItemAtPosition(position);
                toast.setText(Language.getInstance().lastDateMod+selected_item.get_last_mod_date()+"\n"+Language.getInstance().pathText+selected_item.getData());
                toast.show();
            }
        });
        TextView emptyText=(TextView) findViewById(R.id.emptyElement);
        emptyText.setText(Language.getInstance().EmptyListText);
        emptyText.setTextColor(db.get_main_secondary_text_color());
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