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
import lexrislogic.llplayer.Singleton.Var;

public class Settings extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANGE_THEME=575;
    private final static int REQUEST_CODE_CHANGE_LANGUAGE=576;
    private final static int REQUEST_CODE_CHANGE_PLAYER=577;
    private DBHandler db=null;
    public SimpleElementAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement settings_theme=new SimpleElement(
            Var.TYPE_SETTINGS_THEME,
            R.mipmap.settings_theme_icon,
            Language.getInstance().SettingsThemeText,
            Language.getInstance().SettingsThemeSubText,
            -1);
    public SimpleElement settings_language=new SimpleElement(
            Var.TYPE_SETTINGS_LANGUAGE,
            R.mipmap.settings_language_icon,
            Language.getInstance().SettingsLanguageText,
            Language.getInstance().SettingsLanguageSubText,
            -1);
    public SimpleElement settings_player=new SimpleElement(
            Var.TYPE_SETTINGS_PLAYER,
            R.mipmap.settings_player_icon,
            Language.getInstance().SettingsPlayerText,
            Language.getInstance().SettingsPlayerSubText,
            -1);
    public void load_element_list(){
        adapter.set_main_color(db.get_main_primary_text_color());
        adapter.set_secondary_color(db.get_main_secondary_text_color());
        element_list.clear();
        settings_theme.set_main_text(Language.getInstance().SettingsThemeText);
        settings_theme.set_secondary_text(Language.getInstance().SettingsThemeSubText);
        settings_language.set_main_text(Language.getInstance().SettingsLanguageText);
        settings_language.set_secondary_text(Language.getInstance().SettingsLanguageSubText);
        settings_player.set_main_text(Language.getInstance().SettingsPlayerText);
        settings_player.set_secondary_text(Language.getInstance().SettingsPlayerSubText);
        element_list.add(settings_theme);
        element_list.add(settings_language);
        element_list.add(settings_player);
        adapter.notifyDataSetChanged();
    }
    private void refresh_text(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle(Language.getInstance().ConfigurationItem);
        toolbar.invalidate();
    }
    private void refresh_color(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.invalidate();
        View view = findViewById(R.id.settings_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        view.invalidate();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().ConfigurationItem);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.settings_layout);
        view.setBackgroundColor(db.get_secondary_background_color());

        element_list=new ArrayList<>();

        adapter=new SimpleElementAdapter(this,element_list,
                db.get_main_primary_text_color(),
                db.get_main_secondary_text_color());

        ListView listView= (ListView) view.findViewById(R.id.SettingslistView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                Intent intent;
                switch (selected_item.get_type()){
                    case Var.TYPE_SETTINGS_THEME:
                        intent = new Intent(getApplicationContext(), ActivityTheme.class);
                        startActivityForResult(intent, REQUEST_CODE_CHANGE_THEME);
                        break;
                    case Var.TYPE_SETTINGS_LANGUAGE:
                        intent = new Intent(getApplicationContext(), ActivityLanguage.class);
                        startActivityForResult(intent, REQUEST_CODE_CHANGE_LANGUAGE);
                        break;
                    case Var.TYPE_SETTINGS_PLAYER:
                        intent = new Intent(getApplicationContext(), PlayerSettings.class);
                        startActivityForResult(intent, REQUEST_CODE_CHANGE_PLAYER);
                        break;
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_CHANGE_THEME:
                refresh_color();
                break;
            case REQUEST_CODE_CHANGE_LANGUAGE:
                refresh_text();
                break;
            case REQUEST_CODE_CHANGE_PLAYER:
                refresh_color();
                refresh_text();
                break;
        }
        load_element_list();
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
