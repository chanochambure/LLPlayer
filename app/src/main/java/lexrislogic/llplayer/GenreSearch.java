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

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.GenreElementAdapter;
import lexrislogic.llplayer.Models.Genre;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class GenreSearch extends AppCompatActivity {
    public final static int REQUEST_CODE_SEE_SONG_LIST=1236;
    private DBHandler db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.genreToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().BrowseGenreText);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.genreLayout);
        view.setBackgroundColor(db.get_secondary_background_color());
        ListView listView = (ListView) view.findViewById(R.id.genreListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.genreEmptyElement));
        listView.setFastScrollAlwaysVisible(true);
        GenreElementAdapter adapter = new GenreElementAdapter(this, db.get_genres(), db.get_main_primary_text_color(), db.get_main_secondary_text_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Genre selected_item = (Genre) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), SongList.class);
                intent.putExtra("title",selected_item.getName());
                intent.putExtra("genre",selected_item.getName());
                intent.putExtra("type", Var.TYPE_GENRE);
                startActivityForResult(intent, REQUEST_CODE_SEE_SONG_LIST);
            }
        });
        TextView emptyText=(TextView) findViewById(R.id.genreEmptyElement);
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
