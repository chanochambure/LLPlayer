package lexrislogic.llplayer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.PlayModeSeekBarAdapter;
import lexrislogic.llplayer.Models.BandEqualizer;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;

public class PlayModeEqualizerAdjust extends AppCompatActivity {
    private DBHandler db=null;
    private void testEqualizer(){
        Intent eqIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        eqIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_TEST_EQUALIZER);
        sendBroadcast(eqIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_mode_equalizer_adjust);
        Intent mIntent = getIntent();
        final int id_play_mode = mIntent.getIntExtra("id_play_mode",-1);
        String title = mIntent.getStringExtra("title_play_mode");
        Toolbar toolbar = (Toolbar) findViewById(R.id.equalizer_adjust_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        if(id_play_mode==-1) {
            title = Language.getInstance().NewEqualizerText;
        }
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.equalizer_adjust_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        TextView equalizerTitleName=(TextView) findViewById(R.id.PlayModeText);
        equalizerTitleName.setTextColor(db.get_main_primary_text_color());
        equalizerTitleName.setText(Language.getInstance().EqualizerTextTitle);
        final EditText playModeName=(EditText) findViewById(R.id.PlayModeName);
        playModeName.setTextColor(db.get_main_primary_text_color());
        int new_color=db.get_primary_background_color();
        playModeName.setBackgroundColor(Color.argb(
                48,
                Color.red(new_color),
                Color.green(new_color),
                Color.blue(new_color)));
        playModeName.setText(title);
        playModeName.getBackground().setColorFilter(db.get_main_primary_text_color(), PorterDuff.Mode.SRC_ATOP);
        Button applyChanges=(Button) findViewById(R.id.applyChangesButton);
        applyChanges.setBackgroundColor(db.get_primary_background_color());
        applyChanges.setTextColor(db.get_primary_text_color());
        if(id_play_mode==-1) {
            applyChanges.setText(Language.getInstance().CreatePlayModeText);
            PlayerVar.getInstance().testPlayMode=db.get_play_mode(PlayerVar.getInstance().play_mode_index);
        } else {
            applyChanges.setText(Language.getInstance().ModPlayModeText);
            PlayerVar.getInstance().testPlayMode=db.get_play_mode(id_play_mode);
        }
        testEqualizer();
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_play_mode_name=playModeName.getText().toString();
                if(new_play_mode_name.length()>0) {
                    PlayerVar.getInstance().testPlayMode.setName(new_play_mode_name);
                    if (id_play_mode == -1) {
                        db.add_play_mode(PlayerVar.getInstance().testPlayMode);
                        Toast.makeText(getApplicationContext(), new_play_mode_name +" - "+ Language.getInstance().createdPlayModeEq, Toast.LENGTH_SHORT).show();
                    } else {
                        db.update_play_mode(PlayerVar.getInstance().testPlayMode);
                        Toast.makeText(getApplicationContext(),new_play_mode_name +" - "+ Language.getInstance().modPlayModeEq, Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),
                            Language.getInstance().invalidNamePlayMode,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        ArrayList<BandEqualizer> array=new ArrayList<>();
        String[] parts = PlayerVar.getInstance().testPlayMode.getData().split("o");
        short numberFrequencyBands = PlayerVar.getInstance().mediaEqualizer.getNumberOfBands();
        for (short i = 0; i < numberFrequencyBands; i++) {
            array.add(new BandEqualizer(i, (short) (Integer.parseInt(parts[i]))));
        }
        ListView listView = (ListView) view.findViewById(R.id.seekBars);
        PlayModeSeekBarAdapter adapter = new PlayModeSeekBarAdapter(this, array,db.get_main_primary_text_color());
        listView.setAdapter(adapter);
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
    public void onDestroy(){
        if(db!=null){
            db.close();
            db=null;
        }
        super.onDestroy();
    }
}
