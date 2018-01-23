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

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;

public class ActivityLanguage extends AppCompatActivity {
    private DBHandler db=null;
    public SimpleElementAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement spanish_language=new SimpleElement(
            Language.CODE_ES,
            R.mipmap.language_es,
            Language.getInstance().LanguageESText,
            Language.getInstance().LanguageESSubText,
            -1);
    public SimpleElement english_language=new SimpleElement(
            Language.CODE_EN,
            R.mipmap.language_en,
            Language.getInstance().LanguageENText,
            Language.getInstance().LanguageENSubText,
            -1);
    public void load_element_list(){
        element_list.add(spanish_language);
        element_list.add(english_language);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.language_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().SettingsLanguageText);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.language_layout);
        view.setBackgroundColor(db.get_secondary_background_color());

        element_list=new ArrayList<>();

        adapter=new SimpleElementAdapter(this,element_list,
                db.get_main_primary_text_color(),
                db.get_main_secondary_text_color());

        ListView listView= (ListView) view.findViewById(R.id.languageListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                Language.getInstance().set_language(selected_item.get_type());
                db.update_language();
                Intent eqIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                eqIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_UPDATE_LANGUAGE);
                sendBroadcast(eqIntent);
                finish();
            }
        });
        load_element_list();
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
    protected void onDestroy(){
        if(db!=null) {
            db.close();
            db=null;
        }
        super.onDestroy();
    }
}
