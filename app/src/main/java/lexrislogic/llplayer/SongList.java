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
import android.provider.*;
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
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;
import lexrislogic.llplayer.Singleton.Language;

public class SongList extends AppCompatActivity {
    public final static int REQUEST_CODE_MEDIA_PLAYER = 10;
    public final static int REQUEST_CODE_MEDIA_PLAYER_AND_CLOSE = 11;
    private int type;
    private String artist;
    private String album;
    private String genre;
    private int id_playlist;
    private ArrayList<Song> song_list;
    private SongElementAdapter adapter;
    private DBHandler db=null;
    private String title=null;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);
        Intent mIntent = getIntent();
        title = mIntent.getStringExtra("title");
        type = mIntent.getIntExtra("type", Var.TYPE_ALL_SONGS);
        artist = mIntent.getStringExtra("artist");
        album = mIntent.getStringExtra("album");
        genre = mIntent.getStringExtra("genre");
        id_playlist = mIntent.getIntExtra("metadata", -1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.song_list_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        song_list = db.get_songs(type, artist, album,genre, id_playlist);
        if(element_list==null) {
            element_list = new ArrayList<>();
        }
        if(song_list!=null){
            toolbar.setSubtitle(Language.getInstance().totalSongMessage+song_list.size());
        }
        element_list.clear();
        element_list.add(share_song);
        element_list.add(ringtone_song);
        element_list.add(delete_song);
        ListView listView = (ListView) view.findViewById(R.id.songListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.emptyElement));
        listView.setFastScrollAlwaysVisible(true);
        if(song_list!=null) {
            adapter = new SongElementAdapter(this, song_list, db.get_main_primary_text_color(), db.get_main_secondary_text_color());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), MediaPlayerView.class);
                    PlayerVar.getInstance().lastTitle = title;
                    intent.putExtra("type", type);
                    intent.putExtra("artist", artist);
                    intent.putExtra("album", album);
                    intent.putExtra("genre", genre);
                    intent.putExtra("metadata", id_playlist);
                    intent.putExtra("index", i);
                    startActivityForResult(intent, REQUEST_CODE_MEDIA_PLAYER);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Song selected_item = (Song) parent.getItemAtPosition(position);
                    if(selected_item.isValid()) {
                        show_songs_options(selected_item);
                    }
                    return true;
                }
            });
        }
        else{
            Intent intent = new Intent(getApplicationContext(), MediaPlayerView.class);
            intent.putExtra("type", type);
            intent.putExtra("artist", artist);
            intent.putExtra("album", album);
            intent.putExtra("genre", genre);
            intent.putExtra("metadata", id_playlist);
            startActivityForResult(intent, REQUEST_CODE_MEDIA_PLAYER_AND_CLOSE);
        }
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
    public void show_songs_options(final Song song){
        dialog = new Dialog(SongList.this);
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
                switch (selected_item.get_type()) {
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
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtone.getData());
        Uri newUri = getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, newUri);
        Toast.makeText(SongList.this, Language.getInstance().SongRingtoneMessage + ringtone.getTitle(), Toast.LENGTH_SHORT).show();
    }
    private void remove_song_dialog(final Song song){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SongList.this);
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
                                    Toast.makeText(SongList.this, song.getTitle() + Language.getInstance().successRemoveSong, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SongList.this, Language.getInstance().cantRemoveSong, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SongList.this, song.getTitle() + Language.getInstance().successRemoveSong, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SongList.this, Language.getInstance().errorRemoveSong, Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_MEDIA_PLAYER:
                if(type!=Var.TYPE_ALL_SONGS) {
                    song_list = db.get_songs(type, artist, album,genre, id_playlist);
                    adapter.clear();
                    adapter.addAll(song_list);
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_MEDIA_PLAYER_AND_CLOSE:
                finish();
                break;
        }
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
