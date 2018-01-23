package lexrislogic.llplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleStructureElementAdapter;
import lexrislogic.llplayer.Models.PlayList;
import lexrislogic.llplayer.Models.PlayMode;
import lexrislogic.llplayer.Models.SimpleStructure;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;

public class BackupActivity extends AppCompatActivity {
    private AlertDialog alertDialog=null;
    public DBHandler db=null;
    private Boolean return_value=false;
    private int language=0;
    private boolean shuffle_option=false;
    private int repeat_option=0;
    private boolean auto_stop_option=false;
    private int pri_back_color=0;
    private int sec_back_color=0;
    private int pri_text_color=0;
    private int sec_text_color=0;
    private int pri_main_text_color=0;
    private int sec_main_text_color=0;
    private ArrayList<PlayMode> playmodes=new ArrayList<>();
    private ArrayList<Song> all_songs=new ArrayList<>();
    private ArrayList<PlayList> playlists=new ArrayList<>();
    private ArrayList<ArrayList<Song>> playlists_songs=new ArrayList<>();
    private HashMap<Integer,Integer> new_initial_path =new HashMap<>();
    private ArrayList<SimpleStructure> paths=new ArrayList<>();
    public void load_backup(){
        return_value=false;
        FileInputStream inStream = null;
        ObjectInputStream objectInputStream = null;
        language=0;
        shuffle_option=false;
        repeat_option=0;
        auto_stop_option=false;
        pri_back_color=0;
        sec_back_color=0;
        pri_text_color=0;
        sec_text_color=0;
        pri_main_text_color=0;
        sec_main_text_color=0;
        playmodes.clear();
        all_songs.clear();
        playlists.clear();
        playlists_songs.clear();
        String directory= Environment.getExternalStorageDirectory().toString();
        ArrayList<String> all=Fun.getStorages();
        try {
            File file=new File(directory, PlayerVar.PLAYER_VAR_FILENAME_BACKUP);
            inStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(inStream);
            language=objectInputStream.readInt();
            shuffle_option=objectInputStream.readBoolean();
            repeat_option=objectInputStream.readInt();
            auto_stop_option=objectInputStream.readBoolean();

            pri_back_color=objectInputStream.readInt();
            sec_back_color=objectInputStream.readInt();
            pri_text_color=objectInputStream.readInt();
            sec_text_color=objectInputStream.readInt();
            pri_main_text_color=objectInputStream.readInt();
            sec_main_text_color=objectInputStream.readInt();
            playmodes.addAll((ArrayList<PlayMode>) objectInputStream.readObject());
            all_songs.addAll((ArrayList<Song>) objectInputStream.readObject());
            for(Song song:all_songs)
            {
                String song_path="";
                String path=song.getData();
                int slash_counter=0;
                int new_pos=0;
                for(int i=0;i<path.length();++i)
                {
                    new_pos=i;
                    char a=path.charAt(i);
                    if((a=='/') || (a=='\\'))
                    {
                        slash_counter++;
                    }
                    if(slash_counter==3)
                        break;
                    song_path+=a;
                }
                String all_path=song.getData().substring(new_pos);
                song.setData(all_path);
                boolean insert=true;
                int pos=0;
                for(SimpleStructure str:paths)
                {
                    if(str.path.equals(song_path)) {
                        insert = false;
                        break;
                    }
                    pos++;
                }
                if(insert)
                    paths.add(new SimpleStructure(song_path,all));
                new_initial_path.put(song.getId(), pos);
            }
            playlists.addAll((ArrayList<PlayList>) objectInputStream.readObject());
            for(int i=0;i<playlists.size();i++)
                playlists_songs.add((ArrayList<Song>)objectInputStream.readObject());
            objectInputStream.close();
            inStream.close();
            return_value=true;
        } catch (Exception e) {
            e.printStackTrace();
            return_value=false;
        }
        finally {
            try {
                if (objectInputStream!= null)
                    objectInputStream.close();
                if (inStream != null)
                    inStream.close();
            }
            catch (Exception ignored) {
                return_value=false;
            }
        }
    }
    private void use_backup(){
        HashMap<Integer,Integer> new_index=new HashMap<>();
        if(return_value){
            PlayerVar.getInstance().song_list.clear();
            PlayerVar.getInstance().validList=false;
            PlayerVar.getInstance().index=-1;
            PlayerVar.getInstance().last_position_saved=-1;
            PlayerVar.getInstance().play_mode_index=1;
            PlayerVar.getInstance().lastTitle=null;
            Var.getInstance().allAlbumArts.clear();
            db.restart_all_tables();
            Language.getInstance().set_language(language);
            db.update_language();
            db.update_shuffle_option(shuffle_option);
            db.update_repeat_option(repeat_option);
            db.update_auto_stop_option(auto_stop_option);
            db.update_primary_background_color(pri_back_color);
            db.update_secondary_background_color(sec_back_color);
            db.update_primary_text_color(pri_text_color);
            db.update_secondary_text_color(sec_text_color);
            db.update_main_primary_text_color(pri_main_text_color);
            db.update_main_secondary_text_color(sec_main_text_color);
            for(PlayMode playmode:playmodes){
                db.add_play_mode(playmode);
            }
            int s=1;
            for(Song song:all_songs){
                SimpleStructure selected_path=paths.get(new_initial_path.get(song.getId()));
                String new_path="";
                if(selected_path.new_position==0)
                    new_path=""+selected_path.path;
                else
                    new_path=selected_path.path_list.get(selected_path.new_position);
                song.setData(new_path+song.getData());
                new_index.put(song.getId(),s);
                db.add_song_public(song);
                s++;
            }
            int i=0;
            for(PlayList playlist:playlists){
                db.add_playlist(playlist);
                for(Song song:playlists_songs.get(i)){
                    song.setId(new_index.get(song.getId()));
                }
                db.update_playlist_songs(i+1,playlists_songs.get(i));
                i++;
            }
            db.update_songs();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.backupToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().BackupText);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.BackupLayout);
        view.setBackgroundColor(db.get_secondary_background_color());
        load_backup();
        if(!return_value)
        {
            Toast.makeText(BackupActivity.this,Language.getInstance().errorLoadingFileMP+PlayerVar.PLAYER_VAR_FILENAME_BACKUP, Toast.LENGTH_SHORT).show();
            finish();
        }
        ListView listView = (ListView) findViewById(R.id.listPathView);
        SimpleStructureElementAdapter adapter=new SimpleStructureElementAdapter(getApplicationContext(),paths, db.get_main_primary_text_color());
        listView.setAdapter(adapter);
        Button applyChanges=(Button) findViewById(R.id.backupStart);
        applyChanges.setBackgroundColor(db.get_primary_background_color());
        applyChanges.setTextColor(db.get_primary_text_color());
        applyChanges.setText(Language.getInstance().startBackup);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_backup();
            }
        });
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
    private void start_backup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BackupActivity.this);
        alertDialogBuilder.setMessage(Language.getInstance().LoadBackupMessage);
        alertDialogBuilder.setPositiveButton((Language.getInstance().acceptDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        use_backup();
                        Toast.makeText(BackupActivity.this,Language.getInstance().successRefreshBD, Toast.LENGTH_SHORT).show();
                        Fun.savePlayerVar(BackupActivity.this);
                        Var.getInstance().backupReady=true;
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
