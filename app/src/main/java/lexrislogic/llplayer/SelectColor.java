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

import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class SelectColor extends AppCompatActivity implements ColorPickerView.OnColorChangedListener{
    public ColorPickerView mColorPickerView;
    private DBHandler db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        db=new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_color);
        Intent mIntent = getIntent();
        String title = mIntent.getStringExtra("Title");
        Toolbar toolbar = (Toolbar) findViewById(R.id.select_color_toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.select_color_layout);
        view.setBackgroundColor(db.get_secondary_background_color());
        mColorPickerView = (ColorPickerView) findViewById(R.id.colorPicker);
        mColorPickerView.setColor(Var.getInstance().lastColor, true);
        mColorPickerView.setOnColorChangedListener(this);
        Button mOkButton = (Button) findViewById(R.id.button_change_color_accept);
        mOkButton.setTextColor(db.get_primary_text_color());
        mOkButton.setBackgroundColor(db.get_primary_background_color());
        mOkButton.setText(Language.getInstance().setColor);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Var.getInstance().changeColor=true;
                finish();
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

    @Override
    public void onColorChanged(int newColor) {
        Var.getInstance().lastColor=newColor;
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
