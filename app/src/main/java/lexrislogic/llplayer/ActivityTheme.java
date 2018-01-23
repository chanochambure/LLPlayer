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
import lexrislogic.llplayer.Android.SimpleThemeAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class ActivityTheme extends AppCompatActivity {
    private final static int REQUEST_CODE_CHANGE_COLOR=490;
    private DBHandler db=null;
    public SimpleThemeAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement color_background_primary=new SimpleElement(
            Var.TYPE_COLOR_BACKGROUND_PRIMARY,
            R.mipmap.color_background_primary_icon,
            Language.getInstance().ColorBackgroundPrimaryText,
            Language.getInstance().ColorBackgroundPrimarySubText,
            -1);
    public SimpleElement color_background_secondary=new SimpleElement(
            Var.TYPE_COLOR_BACKGROUND_SECONDARY,
            R.mipmap.color_background_secondary_icon,
            Language.getInstance().ColorBackgroundSecondaryText,
            Language.getInstance().ColorBackgroundSecondarySubText,
            -1);
    public SimpleElement color_primary_first_text_color=new SimpleElement(
            Var.TYPE_COLOR_PRIMARY_FIRST_TEXT,
            R.mipmap.color_primary_first_text,
            Language.getInstance().ColorPrimaryFirstTextText,
            Language.getInstance().ColorPrimaryFirstTextSubText,
            -1);
    public SimpleElement color_primary_second_text_color=new SimpleElement(
            Var.TYPE_COLOR_PRIMARY_SECOND_TEXT,
            R.mipmap.color_primary_second_text,
            Language.getInstance().ColorPrimarySecondTextText,
            Language.getInstance().ColorPrimarySecondTextSubText,
            -1);
    public SimpleElement color_secondary_first_text_color=new SimpleElement(
            Var.TYPE_COLOR_SECONDARY_FIRST_TEXT,
            R.mipmap.color_secondary_first_text,
            Language.getInstance().ColorSecondaryFirstTextText,
            Language.getInstance().ColorSecondaryFirstTextSubText,
            -1);
    public SimpleElement color_secondary_second_text_color=new SimpleElement(
            Var.TYPE_COLOR_SECONDARY_SECOND_TEXT,
            R.mipmap.color_secondary_second_text,
            Language.getInstance().ColorSecondarySecondTextText,
            Language.getInstance().ColorSecondarySecondTextSubText,
            -1);
    public void load_element_list(){
        adapter.set_main_color(db.get_main_primary_text_color());
        adapter.set_secondary_color(db.get_main_secondary_text_color());
        element_list.clear();
        element_list.add(color_background_primary);
        element_list.add(color_background_secondary);
        element_list.add(color_primary_first_text_color);
        element_list.add(color_primary_second_text_color);
        element_list.add(color_secondary_first_text_color);
        element_list.add(color_secondary_second_text_color);
        adapter.notifyDataSetChanged();
    }
    private void refresh_color(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.theme_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.invalidate();
        View view = findViewById(R.id.theme_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        view.invalidate();
        adapter.refresh_all_colors(
                db.get_primary_background_color(),
                db.get_secondary_background_color(),
                db.get_primary_text_color(),
                db.get_secondary_text_color(),
                db.get_main_primary_text_color(),
                db.get_main_secondary_text_color());
        adapter.notifyDataSetChanged();
    }
    private void update_color(){
        switch (Var.getInstance().lastTypeColor){
            case Var.TYPE_COLOR_BACKGROUND_PRIMARY:
                db.update_primary_background_color(Var.getInstance().lastColor);
                break;
            case Var.TYPE_COLOR_BACKGROUND_SECONDARY:
                db.update_secondary_background_color(Var.getInstance().lastColor);
                break;
            case Var.TYPE_COLOR_PRIMARY_FIRST_TEXT:
                db.update_primary_text_color(Var.getInstance().lastColor);
                break;
            case Var.TYPE_COLOR_PRIMARY_SECOND_TEXT:
                db.update_secondary_text_color(Var.getInstance().lastColor);
                break;
            case Var.TYPE_COLOR_SECONDARY_FIRST_TEXT:
                db.update_main_primary_text_color(Var.getInstance().lastColor);
                break;
            case Var.TYPE_COLOR_SECONDARY_SECOND_TEXT:
                db.update_main_secondary_text_color(Var.getInstance().lastColor);
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.theme_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(Language.getInstance().SettingsThemeText);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.theme_layout);
        view.setBackgroundColor(db.get_secondary_background_color());

        element_list=new ArrayList<>();

        adapter=new SimpleThemeAdapter(this,element_list,
                db.get_primary_background_color(),
                db.get_secondary_background_color(),
                db.get_primary_text_color(),
                db.get_secondary_text_color(),
                db.get_main_primary_text_color(),
                db.get_main_secondary_text_color());

        ListView listView= (ListView) view.findViewById(R.id.themeListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                Var.getInstance().lastTypeColor=selected_item.get_type();
                switch (Var.getInstance().lastTypeColor){
                    case Var.TYPE_COLOR_BACKGROUND_PRIMARY:
                        Var.getInstance().lastColor=db.get_primary_background_color();
                        break;
                    case Var.TYPE_COLOR_BACKGROUND_SECONDARY:
                        Var.getInstance().lastColor=db.get_secondary_background_color();
                        break;
                    case Var.TYPE_COLOR_PRIMARY_FIRST_TEXT:
                        Var.getInstance().lastColor=db.get_primary_text_color();
                        break;
                    case Var.TYPE_COLOR_PRIMARY_SECOND_TEXT:
                        Var.getInstance().lastColor=db.get_secondary_text_color();
                        break;
                    case Var.TYPE_COLOR_SECONDARY_FIRST_TEXT:
                        Var.getInstance().lastColor=db.get_main_primary_text_color();
                        break;
                    case Var.TYPE_COLOR_SECONDARY_SECOND_TEXT:
                        Var.getInstance().lastColor=db.get_main_secondary_text_color();
                        break;
                }
                Intent intent = new Intent(getApplicationContext(), SelectColor.class);
                intent.putExtra("Title",selected_item.get_main_text());
                startActivityForResult(intent, REQUEST_CODE_CHANGE_COLOR);
            }
        });
        load_element_list();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_CHANGE_COLOR:
                if(Var.getInstance().changeColor) {
                    Var.getInstance().changeColor=false;
                    update_color();
                }
                refresh_color();
                break;
        }
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

