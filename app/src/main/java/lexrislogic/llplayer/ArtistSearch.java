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

import lexrislogic.llplayer.Android.ArtistElementAdapter;
import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Models.Album;
import lexrislogic.llplayer.Models.Artist;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class ArtistSearch extends AppCompatActivity {
    public final static int REQUEST_CODE_ALBUM_SEARCH=125;
    private DBHandler db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.artistToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().BrowseArtistText);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.artistLayout);
        view.setBackgroundColor(db.get_secondary_background_color());
        ListView listView = (ListView) view.findViewById(R.id.artistListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.artistEmptyElement));
        listView.setFastScrollAlwaysVisible(true);
        ArtistElementAdapter adapter = new ArtistElementAdapter(this, db.get_artists(), db.get_main_primary_text_color(), db.get_main_secondary_text_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist selected_item = (Artist) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), AlbumSearch.class);
                Var.getInstance().albumTypeList=true;
                Var.getInstance().albumList.clear();
                Var.getInstance().albumList.add(new Album(-1,null,selected_item.getName()));
                Var.getInstance().albumList.addAll(selected_item.getAlbums());
                startActivityForResult(intent, REQUEST_CODE_ALBUM_SEARCH);
            }
        });
        TextView emptyText=(TextView) findViewById(R.id.artistEmptyElement);
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
            case REQUEST_CODE_ALBUM_SEARCH:
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

