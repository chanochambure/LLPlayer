package lexrislogic.llplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.DynamicListView;
import lexrislogic.llplayer.Android.PlayListSongAdapter;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class ModifyPlayList extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_SONGS=5006;
    private DBHandler db=null;
    private PlayListSongAdapter adapter=null;
    private String title;
    private int playlist_id=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        title=mIntent.getStringExtra("title");
        playlist_id=mIntent.getIntExtra("playlist_id",-1);
        setContentView(R.layout.modify_play_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.modify_playlist_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.modify_playlist_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        TextView emptyText=(TextView) findViewById(R.id.modifyPlaylistEmptyElement);
        emptyText.setText(Language.getInstance().EmptyListText);
        emptyText.setTextColor(db.get_main_secondary_text_color());
        adapter = new PlayListSongAdapter(this, Var.getInstance().playListSongs,db.get_main_primary_text_color(),db.get_main_secondary_text_color());
        DynamicListView listView = (DynamicListView) view.findViewById(R.id.modifyPlaylistListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.modifyPlaylistEmptyElement));
        listView.setFastScrollAlwaysVisible(true);
        listView.setColorAnimation(db.get_primary_background_color());
        listView.setCheeseList(Var.getInstance().playListSongs);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        Button applyChanges=(Button) findViewById(R.id.modifyAction);
        applyChanges.setBackgroundColor(db.get_primary_background_color());
        applyChanges.setTextColor(db.get_primary_text_color());
        applyChanges.setText(Language.getInstance().ModPlayListText);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.update_playlist_songs(playlist_id,Var.getInstance().playListSongs);
                Var.getInstance().playListSongs=null;
                Toast.makeText(ModifyPlayList.this,
                        title + " - " + Language.getInstance().modPlayModeEq,
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_playlist_menu, menu);
        MenuItem add_songs = menu.findItem(R.id.add_songs);
        add_songs.setTitle(Language.getInstance().AddSongsItem);
        add_songs.setIcon(R.mipmap.add_icon);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_songs:
                add_songs_start();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void add_songs_start(){
        Intent intent = new Intent(this, AddSongsPlayList.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_SONGS);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_ADD_SONGS:
                adapter.refresh_hash(Var.getInstance().playListSongs);
                adapter.notifyDataSetChanged();
                break;
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
