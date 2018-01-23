package lexrislogic.llplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class HelpOptions extends AppCompatActivity {
    private DBHandler db=null;
    public SimpleElementAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement manual_media_player=new SimpleElement(
            Var.TYPE_USER_MANUAL_MEDIA_PLAYER,
            R.mipmap.user_manual_media_player,
            Language.getInstance().UserManualMediaPlayerText,
            Language.getInstance().UserManualMediaPlayerSubText,
            -1);
    public SimpleElement manual_song_management=new SimpleElement(
            Var.TYPE_USER_MANUAL_SONGS_MANAGEMENT,
            R.mipmap.user_manual_song_management,
            Language.getInstance().UserManualSongManagementText,
            Language.getInstance().UserManualSongManagementSubText,
            -1);
    public SimpleElement manual_playlist=new SimpleElement(
            Var.TYPE_USER_MANUAL_PLAYLIST,
            R.mipmap.user_manual_playlist,
            Language.getInstance().UserManualPlayListText,
            Language.getInstance().UserManualPlayListSubText,
            -1);
    public SimpleElement manual_playmode=new SimpleElement(
            Var.TYPE_USER_MANUAL_PLAYMODE,
            R.mipmap.user_manual_playmode,
            Language.getInstance().UserManualPlayModeText,
            Language.getInstance().UserManualPlayModeSubText,
            -1);
    public void load_element_list(){
        element_list.add(manual_media_player);
        element_list.add(manual_song_management);
        element_list.add(manual_playlist);
        element_list.add(manual_playmode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.helpToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().UserManualItem);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.helpLayout);
        view.setBackgroundColor(db.get_secondary_background_color());

        element_list=new ArrayList<>();

        adapter=new SimpleElementAdapter(this,element_list,
                db.get_main_primary_text_color(),
                db.get_main_secondary_text_color());

        ListView listView= (ListView) view.findViewById(R.id.helpListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), ShowHelp.class);
                intent.putExtra("title",selected_item.get_main_text());
                switch (selected_item.get_type()){
                    case Var.TYPE_USER_MANUAL_MEDIA_PLAYER:
                        intent.putExtra("content",Language.getInstance().UserManualMediaPlayerContent);
                        startActivity(intent);
                        break;
                    case Var.TYPE_USER_MANUAL_SONGS_MANAGEMENT:
                        intent.putExtra("content",Language.getInstance().UserManualSongManagementContent);
                        startActivity(intent);
                        break;
                    case Var.TYPE_USER_MANUAL_PLAYLIST:
                        intent.putExtra("content",Language.getInstance().UserManualPlayListContent);
                        startActivity(intent);
                        break;
                    case Var.TYPE_USER_MANUAL_PLAYMODE:
                        intent.putExtra("content",Language.getInstance().UserManualPlayModeContent);
                        startActivity(intent);
                        break;
                }
            }
        });
        load_element_list();
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
