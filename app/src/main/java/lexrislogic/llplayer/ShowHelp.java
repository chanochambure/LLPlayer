package lexrislogic.llplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import lexrislogic.llplayer.Android.DBHandler;

public class ShowHelp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        DBHandler db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_help);
        Intent intent=getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.showHelpToolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        toolbar.setTitle(intent.getStringExtra("title"));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View view = findViewById(R.id.showHelpLayout);
        view.setBackgroundColor(db.get_secondary_background_color());
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewHelp);
        scrollView.setBackgroundColor(db.get_secondary_background_color());
        TextView textView = (TextView) findViewById(R.id.showHelpText);
        textView.setTextColor(db.get_main_primary_text_color());
        textView.setText(intent.getStringExtra("content"));
        db.close();
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
}

