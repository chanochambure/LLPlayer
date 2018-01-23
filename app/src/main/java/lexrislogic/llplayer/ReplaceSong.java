package lexrislogic.llplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SongElementAdapter;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Language;

public class ReplaceSong extends AppCompatActivity {
    private DBHandler db=null;
    private Song lost_song=null;
    private AlertDialog alertDialog=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        Intent mIntent = getIntent();
        lost_song=db.get_song_by_id(mIntent.getIntExtra("lost_song_id",-1));
        setContentView(R.layout.activity_replace_song);
        Toolbar toolbar = (Toolbar) findViewById(R.id.replace_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().replaceText + lost_song.getTitle()+" - "+lost_song.getArtist());
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.replace_song_list_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        TextView emptyText=(TextView) findViewById(R.id.replace_emptyElement);
        emptyText.setText(Language.getInstance().EmptyListText);
        emptyText.setTextColor(db.get_main_secondary_text_color());
        ListView listView = (ListView) view.findViewById(R.id.replace_songListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.replace_emptyElement));
        listView.setFastScrollAlwaysVisible(true);
        SongElementAdapter adapter = new SongElementAdapter(this, db.get_valid_songs(), db.get_main_primary_text_color(), db.get_main_secondary_text_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song selected_item = (Song) adapterView.getItemAtPosition(i);
                replace_song_dialog(selected_item);
            }
        });
    }
    void replace_song_dialog(final Song new_song){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReplaceSong.this);
        String messageDialog=Language.getInstance().areYouSureReplace+"\n"+
                lost_song.getTitle() + " | " + lost_song.getArtist()+
                "\n"+Language.getInstance().areYouSureReplaceby+"\n"+new_song.getTitle() + " | " + new_song.getArtist();
        alertDialogBuilder.setMessage(messageDialog);
        alertDialogBuilder.setPositiveButton((Language.getInstance().acceptDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        db.replace_song_references(lost_song, new_song);
                        Toast.makeText(ReplaceSong.this,
                                lost_song.getTitle() + " | " + lost_song.getArtist() + " - " + Language.getInstance().messageReplaced,
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton((Language.getInstance().cancelDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        alertDialog.show();
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
    public void onPause() {
        super.onPause();
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
    }
    @Override
    protected void onDestroy(){
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
        if(db!=null) {
            db.close();
            db=null;
        }
        super.onDestroy();
    }
}
