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
import android.widget.TextView;

import lexrislogic.llplayer.Android.AlbumElementAdapter;
import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Models.Album;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class AlbumSearch extends AppCompatActivity {
    public final static int REQUEST_CODE_SEE_SONG_LIST=1236;
    private DBHandler db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_searh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.albumToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().BrowseAlbumText);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.albumLayout);
        view.setBackgroundColor(db.get_secondary_background_color());
        ListView listView = (ListView) view.findViewById(R.id.albumListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.albumEmptyElement));
        listView.setFastScrollAlwaysVisible(true);
        AlbumElementAdapter adapter = new AlbumElementAdapter(this, Var.getInstance().albumList, db.get_main_primary_text_color(), db.get_main_secondary_text_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Album selected_item = (Album) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), SongList.class);
                intent.putExtra("artist",selected_item.getArtist());
                if(selected_item.getName()!=null) {
                    intent.putExtra("title", selected_item.getArtist() + ": " + selected_item.getName());
                    intent.putExtra("album",selected_item.getName());
                    intent.putExtra("type", Var.TYPE_ARTIST_ALBUM);
                } else {
                    intent.putExtra("title", selected_item.getArtist() + ": " + Language.getInstance().AllSongs);
                    intent.putExtra("type", Var.TYPE_ARTIST);
                }
                startActivityForResult(intent, REQUEST_CODE_SEE_SONG_LIST);
            }
        });
        TextView emptyText=(TextView) findViewById(R.id.albumEmptyElement);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_SEE_SONG_LIST:
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