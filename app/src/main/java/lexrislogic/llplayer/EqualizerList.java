package lexrislogic.llplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.PlayModeElementAdapter;
import lexrislogic.llplayer.Models.PlayMode;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;

public class EqualizerList extends AppCompatActivity {
    public static final int REQUEST_CODE_ADD_EQUALIZER = 501;
    private DBHandler db=null;
    private PlayModeElementAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equalizer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.current_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().EqualizerText);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.equalizer_list_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        TextView emptyText=(TextView) findViewById(R.id.equalizer_emptyElement);
        emptyText.setText(Language.getInstance().EmptyListText);
        emptyText.setTextColor(db.get_main_secondary_text_color());
        ListView listView = (ListView) view.findViewById(R.id.equalizerListView);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(findViewById(R.id.equalizer_emptyElement));
        PlayerVar.getInstance().playModes=db.get_play_modes();
        adapter = new PlayModeElementAdapter(this, PlayerVar.getInstance().playModes, db.get_main_primary_text_color(),db.get_primary_background_color());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PlayMode selected_item = (PlayMode) adapterView.getItemAtPosition(i);
                if(selected_item.getId()!=PlayerVar.getInstance().play_mode_index){
                    PlayerVar.getInstance().play_mode_index=selected_item.getId();
                    Intent eqIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                    eqIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_EQUALIZER);
                    sendBroadcast(eqIntent);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.equalizer_menu, menu);
        MenuItem add_equalizer = menu.findItem(R.id.add_equalizer);
        add_equalizer.setTitle(Language.getInstance().AddEqualizerItem);
        add_equalizer.setIcon(R.mipmap.add_icon);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_equalizer:
                Intent intent = new Intent(getApplicationContext(), PlayModeEqualizerAdjust.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_EQUALIZER);
                return true;
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
            case REQUEST_CODE_ADD_EQUALIZER:
                adapter.clear();
                adapter.addAll(db.get_play_modes());
                adapter.notifyDataSetChanged();
                Intent eqIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                eqIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_EQUALIZER);
                sendBroadcast(eqIntent);
                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        adapter.dismiss_dialog();
    }
    @Override
    protected void onDestroy(){
        adapter.dismiss_dialog();
        if(db!=null) {
            db.close();
            db=null;
        }
        super.onDestroy();
    }
}
