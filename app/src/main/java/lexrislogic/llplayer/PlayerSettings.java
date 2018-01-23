package lexrislogic.llplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;

public class PlayerSettings extends AppCompatActivity {
    public final static int REQUEST_CODE_BACKUP=1123;
    public DBHandler db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.playerSettingsToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().SettingsPlayerText);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.playerSettingsLayout);
        view.setBackgroundColor(db.get_secondary_background_color());
        TableLayout table = (TableLayout) findViewById(R.id.playerSettingsTable);
        table.setBackgroundColor(db.get_secondary_background_color());
        //Auto Stop Option
        TextView autoStopOptionText = (TextView) findViewById(R.id.textAutoStopOption);
        autoStopOptionText.setTextColor(db.get_main_primary_text_color());
        autoStopOptionText.setText(Language.getInstance().PlayerSettingAutoStop);
        Switch switch_button = (Switch) findViewById(R.id.switchAutoStopOption);
        switch_button.setChecked(PlayerVar.getInstance().auto_stop_option);
        switch_button.setTextColor(db.get_main_primary_text_color());
        switch_button.setHighlightColor(db.get_primary_background_color());
        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlayerVar.getInstance().auto_stop_option = isChecked;
                db.update_auto_stop_option(PlayerVar.getInstance().auto_stop_option);
            }
        });
        //Auto Replay Option
        TextView autoReplayOptionText = (TextView) findViewById(R.id.textAutoReplayOption);
        autoReplayOptionText.setTextColor(db.get_main_primary_text_color());
        autoReplayOptionText.setText(Language.getInstance().PlayerSettingAutoReplay);
        Switch switch_replay_button = (Switch) findViewById(R.id.switchAutoReplayOption);
        switch_replay_button.setChecked(PlayerVar.getInstance().auto_replay_option);
        switch_replay_button.setTextColor(db.get_main_primary_text_color());
        switch_replay_button.setHighlightColor(db.get_primary_background_color());
        switch_replay_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlayerVar.getInstance().auto_replay_option=isChecked;
                db.update_auto_replay_option(PlayerVar.getInstance().auto_replay_option);
            }
        });
        //Save BackUp Option
        TextView textSaveBackupOption = (TextView) findViewById(R.id.textSaveBackupOption);
        textSaveBackupOption.setTextColor(db.get_main_primary_text_color());
        textSaveBackupOption.setText(Language.getInstance().PlayerSettingSaveBackup);
        Button saveBackup = (Button) findViewById(R.id.SaveBackupButton);
        saveBackup.setTextColor(db.get_primary_text_color());
        saveBackup.setBackgroundColor(db.get_primary_background_color());
        saveBackup.setClickable(true);
        saveBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result=Fun.save_backup(db);
                if(result!=null) {
                    Toast.makeText(PlayerSettings.this, Language.getInstance().successBackup + result, Toast.LENGTH_SHORT).show();
                    Button saveBackup = (Button) findViewById(R.id.SaveBackupButton);
                    saveBackup.setClickable(false);
                    saveBackup.setBackgroundColor(db.get_secondary_background_color());
                }
                else
                    Toast.makeText(PlayerSettings.this, Language.getInstance().errorBackup, Toast.LENGTH_SHORT).show();
            }
        });
        //Load BackUp Option
        TextView textLoadBackupOption = (TextView) findViewById(R.id.textLoadBackupOption);
        textLoadBackupOption.setTextColor(db.get_main_primary_text_color());
        textLoadBackupOption.setText(Language.getInstance().PlayerSettingLoadBackup);
        Button loadBackup = (Button) findViewById(R.id.LoadBackupButton);
        loadBackup.setTextColor(db.get_primary_text_color());
        loadBackup.setBackgroundColor(db.get_primary_background_color());
        loadBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PlayerVar.getInstance().onForeground){
                    load_backup();
                }else {
                    Toast.makeText(PlayerSettings.this,Language.getInstance().errorLoadingCloseMP, Toast.LENGTH_SHORT).show();
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_BACKUP:
                {
                    if(Var.getInstance().backupReady) {
                        Var.getInstance().backupReady = false;
                        finish();
                    }
                }
                break;
        }
    }
    public void load_backup(){
        Intent intent = new Intent(getApplicationContext(), BackupActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BACKUP);
    }
    @Override
    public void onPause() {
        super.onPause();
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
