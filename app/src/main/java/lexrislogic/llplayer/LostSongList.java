package lexrislogic.llplayer;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Android.SongElementAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class LostSongList extends AppCompatActivity {
    private final static int REQUEST_CODE_REPLACE_SONG=41;
    private ArrayList<SimpleElement> element_list=null;
    private Dialog dialog=null;
    private AlertDialog alertDialog=null;
    public SimpleElement replace_song=new SimpleElement(
            Var.TYPE_MAINTENANCE_REPLACE_SONG,
            R.mipmap.replace_song_icon,
            Language.getInstance().MaintenanceReplaceSongText,
            Language.getInstance().MaintenanceReplaceSongSubText,
            -1);
    public SimpleElement remove_song=new SimpleElement(
            Var.TYPE_MAINTENANCE_REMOVE_SONG,
            R.mipmap.remove_song_icon,
            Language.getInstance().MaintenanceRemoveSongText,
            Language.getInstance().MaintenanceRemoveSongSubText,
            -1);
    private DBHandler db=null;
    private SongElementAdapter song_adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.lost_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().MaintenanceLostSongsText);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if(element_list==null) {
            element_list = new ArrayList<>();
        }
        element_list.clear();
        element_list.add(replace_song);
        element_list.add(remove_song);
        View view = findViewById(R.id.lost_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        ListView listView = (ListView) view.findViewById(R.id.lostListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setFastScrollAlwaysVisible(true);
        song_adapter = new SongElementAdapter(this, db.get_lost_songs(), db.get_main_primary_text_color(), db.get_main_secondary_text_color());
        listView.setAdapter(song_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Song song=(Song)parent.getItemAtPosition(position);
                dialog = new Dialog(LostSongList.this);
                dialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                dialog.setContentView(R.layout.options_layout);
                dialog.setTitle(Language.getInstance().lostOptionsDialogTittle);
                RelativeLayout backgroundDialog = (RelativeLayout) dialog.findViewById(R.id.backgroundDialog);
                backgroundDialog.setBackgroundColor(db.get_secondary_background_color());
                ListView listView = (ListView) dialog.findViewById(R.id.listOptionsView);
                SimpleElementAdapter adapter=new SimpleElementAdapter(getApplicationContext(),element_list,
                        db.get_main_primary_text_color(),
                        db.get_main_secondary_text_color());
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                        switch (selected_item.get_type()){
                            case Var.TYPE_MAINTENANCE_REPLACE_SONG:
                                replace_song(song);
                                break;
                            case Var.TYPE_MAINTENANCE_REMOVE_SONG:
                                remove_song_dialog(song);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
    private void remove_song_dialog(final Song song){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LostSongList.this);
        alertDialogBuilder.setMessage(Language.getInstance().removeSong+ ": " + song.getTitle()+" | "+song.getArtist());
        alertDialogBuilder.setPositiveButton((Language.getInstance().acceptDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        db.remove_song_references(song);
                        Toast.makeText(LostSongList.this,
                                song.getTitle() + " | " + song.getArtist() + " - " + Language.getInstance().messageDeleted,
                                Toast.LENGTH_SHORT).show();
                        song_adapter.clear();
                        song_adapter.addAll(db.get_lost_songs());
                        song_adapter.notifyDataSetChanged();
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
    private void replace_song(Song lost_song){
        Intent intent = new Intent(getApplicationContext(), ReplaceSong.class);
        intent.putExtra("lost_song_id",lost_song.getId());
        startActivityForResult(intent, REQUEST_CODE_REPLACE_SONG);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_REPLACE_SONG:
                break;
        }
        song_adapter.clear();
        song_adapter.addAll(db.get_lost_songs());
        song_adapter.notifyDataSetChanged();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
    }
    @Override
    protected void onDestroy(){
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
        if(db!=null) {
            db.close();
            db=null;
        }
        super.onDestroy();
    }
}
