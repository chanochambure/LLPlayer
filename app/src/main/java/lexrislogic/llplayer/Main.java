package lexrislogic.llplayer;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.PagerAdapter;
import lexrislogic.llplayer.Models.PlayList;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class Main extends AppCompatActivity {
    private final static int REQUEST_CODE_SETTINGS=475;
    private final static int REQUEST_CODE_HELP=476;
    private final static int REQUEST_CODE_SEE_SONG_LIST = 477;
    private DBHandler db = null;
    private Dialog dialog = null;
    private AudioManager mAudioManager=null;
    private ComponentName mReceiverComponent=null;
    private void create_data(){
        if(db==null) {
            db=new DBHandler(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(db.get_primary_background_color());
        toolbar.setTitleTextColor(db.get_primary_text_color());
        toolbar.setSubtitleTextColor(db.get_secondary_text_color());
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.removeAllTabs();
        tabLayout.setBackgroundColor(db.get_primary_background_color());
        tabLayout.setTabTextColors(db.get_secondary_text_color(), db.get_primary_text_color());
        tabLayout.setSelectedTabIndicatorColor(db.get_secondary_background_color());
        TabLayout.Tab tab_1=tabLayout.newTab();
        tab_1.setText(Language.getInstance().PlayListTab);
        tab_1.setIcon(R.mipmap.playlists_tab_icon);
        TabLayout.Tab tab_2=tabLayout.newTab();
        tab_2.setText(Language.getInstance().BrowseTab);
        tab_2.setIcon(R.mipmap.browse_tab_icon);
        TabLayout.Tab tab_3=tabLayout.newTab();
        tab_3.setText(Language.getInstance().MaintenanceTab);
        tab_3.setIcon(R.mipmap.maintenance_tab_icon);
        tabLayout.addTab(tab_1);
        tabLayout.addTab(tab_2);
        tabLayout.addTab(tab_3);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        toolbar.invalidate();
        tabLayout.invalidate();
        viewPager.invalidate();
    }
    boolean from_notification=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        from_notification = intent.getIntExtra("Notification",0)==1;
        Var.getInstance().onApp=true;
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(this,MediaPlayerReceiver.class);
        if(mAudioManager!=null) {
            mAudioManager.registerMediaButtonEventReceiver(mReceiverComponent);
        }
        if(!Var.getInstance().onService) {
            Fun.refreshPlayerVar(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DBHandler db = new DBHandler(this);
        Language.getInstance().set_language(db.get_language());
        db.update_songs();
        if(!Var.getInstance().onService) {
            startService(new Intent(this, MediaPlayerService.class));
        }
        create_data();
    }

    @Override
    public void onStart(){
        super.onStart();
        if(from_notification){
            from_notification=false;
            Intent intent = new Intent(this, SongList.class);
            intent.putExtra("title", PlayerVar.getInstance().lastTitle);
            intent.putExtra("type",Var.TYPE_CURRENT_PLAYLIST);
            intent.putExtra("metadata", -1);
            startActivityForResult(intent, REQUEST_CODE_SEE_SONG_LIST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem add_playlist = menu.findItem(R.id.add_playlist);
        add_playlist.setTitle(Language.getInstance().AddPlayListItem);
        add_playlist.setIcon(R.mipmap.add_icon);
        MenuItem configuration = menu.findItem(R.id.configuration);
        configuration.setTitle(Language.getInstance().ConfigurationItem);
        configuration.setIcon(R.mipmap.conf_icon);
        MenuItem user_manual = menu.findItem(R.id.user_manual);
        user_manual.setTitle(Language.getInstance().UserManualItem);
        user_manual.setIcon(R.mipmap.help_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_playlist) {
            dialog = new Dialog(Main.this);
            dialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            dialog.setContentView(R.layout.create_playlist_dialog);
            dialog.setTitle(Language.getInstance().AddPlayListItem);
            RelativeLayout backgroundDialog = (RelativeLayout) dialog.findViewById(R.id.backgroundDialog);
            backgroundDialog.setBackgroundColor(db.get_secondary_background_color());
            final EditText playListName=(EditText) dialog.findViewById(R.id.namePlayList);
            playListName.setTextColor(db.get_main_primary_text_color());
            int new_color=db.get_primary_background_color();
            playListName.setBackgroundColor(Color.argb(
                    48,
                    Color.red(new_color),
                    Color.green(new_color),
                    Color.blue(new_color)));
            playListName.setText(Language.getInstance().newPlayListName);
            playListName.getBackground().setColorFilter(db.get_main_primary_text_color(), PorterDuff.Mode.SRC_ATOP);
            playListName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    playListName.post(new Runnable() {
                        @Override
                        public void run() {
                            playListName.setSelected(true);
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(playListName, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            });
            playListName.requestFocus();
            Button applyChanges=(Button) dialog.findViewById(R.id.createPlatlist);
            applyChanges.setBackgroundColor(db.get_primary_background_color());
            applyChanges.setTextColor(db.get_primary_text_color());
            applyChanges.setText(Language.getInstance().CreatePlayListText);
            applyChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String new_play_list_name = playListName.getText().toString();
                    if (new_play_list_name.length() > 0) {
                        db.add_playlist(new PlayList(new_play_list_name));
                        create_data();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                Language.getInstance().invalidNamePlayList,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
            return true;
        }
        else if (id == R.id.configuration) {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            return true;
        }
        else if (id == R.id.user_manual) {
            Intent intent = new Intent(getApplicationContext(), HelpOptions.class);
            startActivityForResult(intent, REQUEST_CODE_HELP);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_SETTINGS:
                create_data();
                break;
            case REQUEST_CODE_HELP:
                break;
            case REQUEST_CODE_SEE_SONG_LIST:
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
    @Override
    public void onDestroy(){
        if(mAudioManager!=null) {
            mAudioManager.unregisterMediaButtonEventReceiver(mReceiverComponent);
            mAudioManager=null;
            mReceiverComponent=null;
        }
        if(Var.getInstance().onService && !PlayerVar.getInstance().onForeground)
            stopService(new Intent(this, MediaPlayerService.class));
        if(db!=null){
            db.close();
            db=null;
        }
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        Var.getInstance().onApp=false;
        super.onDestroy();
    }
}
