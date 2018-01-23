package lexrislogic.llplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.File;
import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Android.SongElementAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;

public class CurrentSongList extends AppCompatActivity {
    private DBHandler db=null;
    private ArrayList<SimpleElement> element_list=null;
    public SimpleElement share_song=new SimpleElement(
            Var.TYPE_SONG_SHARE,
            R.mipmap.share_icon,
            Language.getInstance().SongShareText,
            Language.getInstance().SongShareSubText,
            -1);
    public SimpleElement ringtone_song=new SimpleElement(
            Var.TYPE_SONG_RINGTONE,
            R.mipmap.ringtone_icon,
            Language.getInstance().SongRingtoneText,
            Language.getInstance().SongRingtoneSubText,
            -1);
    public SimpleElement delete_song=new SimpleElement(
            Var.TYPE_SONG_DELETE,
            R.mipmap.remove_song_icon,
            Language.getInstance().SongRemoveText,
            Language.getInstance().SongRemoveSubText,
            -1);
    private Dialog dialog=null;
    private AlertDialog alertDialog=null;
    private SongElementAdapter current_adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.current_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(PlayerVar.getInstance().lastTitle);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.current_song_list_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        if(element_list==null) {
            element_list = new ArrayList<>();
        }
        element_list.clear();
        element_list.add(share_song);
        element_list.add(ringtone_song);
        element_list.add(delete_song);
        TextView emptyText=(TextView) findViewById(R.id.current_emptyElement);
        emptyText.setText(Language.getInstance().EmptyListText);
        emptyText.setTextColor(db.get_main_secondary_text_color());
        ListView listView = (ListView) view.findViewById(R.id.current_songListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.current_emptyElement));
        listView.setFastScrollAlwaysVisible(true);
        if(PlayerVar.getInstance().validList) {
            current_adapter = new SongElementAdapter(this, PlayerVar.getInstance().song_list, db.get_main_primary_text_color(), db.get_main_secondary_text_color());
            listView.setAdapter(current_adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (PlayerVar.getInstance().song_list.get(i).isValid()) {
                        PlayerVar.getInstance().new_index = i;
                        Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                        playIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_NEW_SONG);
                        sendBroadcast(playIntent);
                    }
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Song selected_item = (Song) parent.getItemAtPosition(position);
                    if (selected_item.isValid()) {
                        show_songs_options(selected_item);
                    }
                    return true;
                }
            });
            listView.setSelection(PlayerVar.getInstance().index);
        }
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
    public void show_songs_options(final Song song){
        dialog = new Dialog(CurrentSongList.this);
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
                    case Var.TYPE_SONG_SHARE:
                        share_song(song);
                        break;
                    case Var.TYPE_SONG_RINGTONE:
                        set_ringtone_song(song);
                        break;
                    case Var.TYPE_SONG_DELETE:
                        remove_song_dialog(song);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void set_ringtone_song(Song ringtone){
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, ringtone.getData());
        values.put(MediaStore.MediaColumns.TITLE, ringtone.getTitle());
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        Uri uri=MediaStore.Audio.Media.getContentUriForPath(ringtone.getData());
        Uri newUri=getContentResolver().insert(uri,values);
        RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, newUri);
        Toast.makeText(CurrentSongList.this, Language.getInstance().SongRingtoneMessage + ringtone.getTitle(), Toast.LENGTH_SHORT).show();
    }
    private void remove_song_dialog(final Song song){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CurrentSongList.this);
        alertDialogBuilder.setMessage(Language.getInstance().SongRemoveMessage + song.getTitle());
        alertDialogBuilder.setPositiveButton((Language.getInstance().acceptDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int index = -1;
                        int i = 0;
                        for (Song s : PlayerVar.getInstance().song_list) {
                            if (s.getId() == song.getId()) {
                                index = i;
                                break;
                            }
                            i++;
                        }
                        if (index == -1 || index != PlayerVar.getInstance().index) {
                            Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                            Uri itemUri = ContentUris.withAppendedId(contentUri, song.getPhone_id());
                            int media_type = 1;
                            ContentValues contentValues=new ContentValues();
                            contentValues.put("media_type", media_type);
                            getContentResolver().update(itemUri, contentValues, null, null);
                            getContentResolver().delete(itemUri, null, null);
                            File file = new File(song.getData());
                            if(file.exists()) {
                                if (file.delete()) {
                                    Toast.makeText(CurrentSongList.this, song.getTitle() + Language.getInstance().successRemoveSong, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CurrentSongList.this, Language.getInstance().cantRemoveSong, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CurrentSongList.this, song.getTitle() + Language.getInstance().successRemoveSong, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CurrentSongList.this, Language.getInstance().errorRemoveSong, Toast.LENGTH_SHORT).show();
                        }
                        current_adapter.notifyDataSetChanged();
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
    private void share_song(Song song){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("audio/mp3");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(song.getData())));
        startActivity(Intent.createChooser(sharingIntent, Language.getInstance().SongShareMessage));
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
