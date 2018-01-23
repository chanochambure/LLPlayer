package lexrislogic.llplayer;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.timeSpinnerAdapter;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;

public class MediaPlayerView extends AppCompatActivity {
    public final static int REQUEST_CODE_MEDIA_PLAYER_VIEW_REFRESH = 111;
    public final static int REQUEST_CODE_MEDIA_PLAYER_VIEW_EQUALIZER = 112;
    public final static String MEDIA_PLAYER_VIEW_ACTION = "MediaPlayerView";
    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MEDIA_PLAYER_VIEW_ACTION)) {
                switch (intent.getIntExtra("MP-V-CODE",PlayerVar.MP_V_ANY)){
                    case PlayerVar.MP_V_READY_SET_LIST:
                        setListMediaPlayer();
                        break;
                    case PlayerVar.MP_V_READY_GET_INFO:
                        refreshViews();
                        break;
                    case PlayerVar.MP_V_READY_GET_INFO_BUTTON:
                        refreshOnlyButtonViews();
                        break;
                    case PlayerVar.MP_V_ANY:
                        break;
                    default:
                        break;
                }
            }
        }
    };
    private Dialog dialog = null;

    private void setListMediaPlayer(){
        PlayerVar.getInstance().song_list=db.get_songs(type, artist, album, genre,playlist_id);
        Fun.refreshSongListIndex(PlayerVar.getInstance().song_list);
        PlayerVar.getInstance().index=index;
        Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        playIntent.putExtra("MP-M-CODE",PlayerVar.MP_M_START);
        sendBroadcast(playIntent);
    }

    private DBHandler db=null;
    private boolean onMeasured=false;
    private boolean change_pos=false;
    private final Handler handler = new Handler();
    private float temp_progress=0;
    private Toast single_toast=null;
    private final Runnable r = new Runnable() {

        @Override
        public void run() {
            if (PlayerVar.getInstance().validList && !PlayerVar.getInstance().onPause && !change_pos) {
                int mCurrentPosition = PlayerVar.getInstance().mediaPlayer.getCurrentPosition();
                SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
                seekBar.setProgress(mCurrentPosition);
                TextView positionText = (TextView) findViewById(R.id.positionText);
                positionText.setText(Fun.to_string_time_format(mCurrentPosition));
                positionText.invalidate();
            }
            handler.postDelayed(this, 1000);
        }
    };

    private void initViews(){
        View view = findViewById(R.id.media_player_layout);
        view.setBackgroundColor(db.get_primary_background_color());
        ImageButton back_button=(ImageButton) findViewById(R.id.backHome);
        back_button.setImageResource(R.mipmap.back_icon);
        back_button.setContentDescription(Language.getInstance().BackMediaPlayerItem);
        back_button.setBackgroundColor(db.get_primary_background_color());
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageButton prev_song_button=(ImageButton) findViewById(R.id.prevSong);
        prev_song_button.setImageResource(R.mipmap.prev_media_player_icon);
        prev_song_button.setContentDescription(Language.getInstance().PrevSongPlayerItem);
        prev_song_button.setBackgroundColor(db.get_primary_background_color());
        prev_song_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                playIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_PREV_SONG);
                sendBroadcast(playIntent);
            }
        });
        ImageButton next_song_button=(ImageButton) findViewById(R.id.nextSong);
        next_song_button.setImageResource(R.mipmap.next_media_player_icon);
        next_song_button.setContentDescription(Language.getInstance().NextSongPlayerItem);
        next_song_button.setBackgroundColor(db.get_primary_background_color());
        next_song_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                playIntent.putExtra("MP-M-CODE",PlayerVar.MP_M_NEXT_SONG);
                sendBroadcast(playIntent);
            }
        });
        ImageButton play_pause_button=(ImageButton) findViewById(R.id.playPause);
        play_pause_button.setContentDescription(Language.getInstance().PlayPauseSongPlayerItem);
        play_pause_button.setBackgroundColor(db.get_primary_background_color());
        if(PlayerVar.getInstance().onPause){
            play_pause_button.setImageResource(R.mipmap.play_media_player_icon);
        }
        else{
            play_pause_button.setImageResource(R.mipmap.pause_media_player_icon);
        }
        play_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                playIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_PLAY_PAUSE);
                sendBroadcast(playIntent);
            }
        });
        ImageButton favorite_button=(ImageButton) findViewById(R.id.favoriteButton);
        favorite_button.setContentDescription(Language.getInstance().Favorites);
        favorite_button.setBackgroundColor(db.get_primary_background_color());
        favorite_button.setImageResource(R.mipmap.no_favorites_icon);
        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerVar.getInstance().validList){
                    Song selection = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index);
                    selection.setBookmark((selection.getBookmark() + 1) % 2);
                    db.update_song(selection);
                    ImageButton favorite_button=(ImageButton) v;
                    if(selection.getBookmark()==1){
                        favorite_button.setImageResource(R.mipmap.favorites_icon);
                    }else{
                        favorite_button.setImageResource(R.mipmap.no_favorites_icon);
                    }
                }
            }
        });
        ImageButton shuffle_button = (ImageButton) findViewById(R.id.shuffleButton);
        shuffle_button.setContentDescription(Language.getInstance().ShuffleMediaPlayerItem);
        shuffle_button.setBackgroundColor(db.get_primary_background_color());
        if(PlayerVar.getInstance().shuffle_option){
            shuffle_button.setImageResource(R.mipmap.shuffle_on_icon);
        }
        else
            shuffle_button.setImageResource(R.mipmap.shuffle_off_icon);
        shuffle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton shuffle_button = (ImageButton) v;
                PlayerVar.getInstance().shuffle_option = !PlayerVar.getInstance().shuffle_option;
                db.update_shuffle_option(PlayerVar.getInstance().shuffle_option);
                if (PlayerVar.getInstance().shuffle_option) {
                    PlayerVar.getInstance().random_index.add(PlayerVar.getInstance().index);
                    single_toast.setText(Language.getInstance().OnShuffle);
                    single_toast.show();
                    shuffle_button.setImageResource(R.mipmap.shuffle_on_icon);
                } else {
                    PlayerVar.getInstance().random_index.clear();
                    single_toast.setText(Language.getInstance().OffShuffle);
                    single_toast.show();
                    shuffle_button.setImageResource(R.mipmap.shuffle_off_icon);
                }
                shuffle_button.invalidate();
            }
        });
        ImageButton repeat_button=(ImageButton) findViewById(R.id.repeatButton);
        repeat_button.setContentDescription(Language.getInstance().RepeatMediaPlayerItem);
        repeat_button.setBackgroundColor(db.get_primary_background_color());
        switch (PlayerVar.getInstance().repeat_option){
            case Var.REPEAT_TYPE_NO_REPEAT:
                repeat_button.setImageResource(R.mipmap.no_repeat_icon);
                break;
            case Var.REPEAT_TYPE_REPEAT_LIST:
                repeat_button.setImageResource(R.mipmap.repeat_list_icon);
                break;
            case Var.REPEAT_TYPE_REPEAT_SONG:
                repeat_button.setImageResource(R.mipmap.repeat_song_icon);
                break;
        }
        repeat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton repeat_button=(ImageButton) v;
                PlayerVar.getInstance().repeat_option=(PlayerVar.getInstance().repeat_option+1)%3;
                db.update_repeat_option(PlayerVar.getInstance().repeat_option);
                switch (PlayerVar.getInstance().repeat_option){
                    case Var.REPEAT_TYPE_NO_REPEAT:
                        single_toast.setText(Language.getInstance().RepeatOff);
                        single_toast.show();
                        repeat_button.setImageResource(R.mipmap.no_repeat_icon);
                        break;
                    case Var.REPEAT_TYPE_REPEAT_LIST:
                        single_toast.setText(Language.getInstance().RepeatList);
                        single_toast.show();
                        repeat_button.setImageResource(R.mipmap.repeat_list_icon);
                        break;
                    case Var.REPEAT_TYPE_REPEAT_SONG:
                        single_toast.setText(Language.getInstance().RepeatSong);
                        single_toast.show();
                        repeat_button.setImageResource(R.mipmap.repeat_song_icon);
                        break;
                }
                repeat_button.invalidate();
            }
        });
        ImageView albumImage = (ImageView) findViewById(R.id.albumArtImage);
        albumImage.setBackgroundColor(db.get_secondary_background_color());
        albumImage.setClickable(true);
        albumImage.setImageResource(R.mipmap.no_album);
        ViewTreeObserver vto = albumImage.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onMeasured = true;
                ImageView albumImage = (ImageView) findViewById(R.id.albumArtImage);
                albumImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                albumImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScrollView scrollLyricsView = (ScrollView) findViewById(R.id.scrollLyricsView);
                        scrollLyricsView.setVisibility(View.VISIBLE);
                    }
                });
                refreshAlbumImage();
            }
        });
        ScrollView scrollLyricsView = (ScrollView) findViewById(R.id.scrollLyricsView);
        int lyrics_background_color=db.get_secondary_background_color();
        scrollLyricsView.setBackgroundColor(Color.argb(
                192,
                Color.red(lyrics_background_color),
                Color.green(lyrics_background_color),
                Color.blue(lyrics_background_color)));
        TextView lyricsText = (TextView) findViewById(R.id.lyricsText);
        lyricsText.setTextColor(db.get_main_primary_text_color());
        lyricsText.setClickable(true);
        lyricsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollLyricsView = (ScrollView) findViewById(R.id.scrollLyricsView);
                scrollLyricsView.setVisibility(View.INVISIBLE);
            }
        });
        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setTextColor(db.get_primary_text_color());
        titleText.setText(Var.UNKNOWN_SONG);
        TextView artistText = (TextView) findViewById(R.id.artistText);
        artistText.setTextColor(db.get_secondary_text_color());
        artistText.setText(Var.UNKNOWN_ARTIST);
        TextView albumText = (TextView) findViewById(R.id.albumText);
        albumText.setTextColor(db.get_secondary_text_color());
        albumText.setText(Var.UNKNOWN_ALBUM);
        TextView positionText = (TextView) findViewById(R.id.positionText);
        positionText.setTextColor(db.get_primary_text_color());
        TextView durationText = (TextView) findViewById(R.id.durationText);
        durationText.setTextColor(db.get_primary_text_color());
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setDrawingCacheBackgroundColor(db.get_primary_text_color());
        seekBar.getProgressDrawable().setColorFilter(db.get_primary_text_color(), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(db.get_secondary_text_color(), PorterDuff.Mode.SRC_ATOP);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                change_pos = false;
                if (PlayerVar.getInstance().validList && PlayerVar.getInstance().isPrepared)
                    PlayerVar.getInstance().mediaPlayer.seekTo((int) (temp_progress * seekBar.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                change_pos = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (PlayerVar.getInstance().validList && fromUser) {
                    temp_progress = (float) (progress * 1.0) / seekBar.getMax();
                    TextView positionText = (TextView) findViewById(R.id.positionText);
                    positionText.setText(Fun.to_string_time_format((int) (temp_progress * seekBar.getMax())));
                }
            }
        });
        ImageButton timer_button=(ImageButton) findViewById(R.id.timerButton);
        timer_button.setImageResource((PlayerVar.getInstance().onTimer)?R.mipmap.timer_on_icon:R.mipmap.timer_off_icon);
        timer_button.setBackgroundColor(db.get_primary_background_color());
        timer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PlayerVar.getInstance().validList) {
                    dialog = new Dialog(MediaPlayerView.this);
                    dialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    dialog.setContentView(R.layout.timer_layout);
                    dialog.setTitle(Language.getInstance().timerDialogTittle);
                    RelativeLayout backgroundDialog = (RelativeLayout) dialog.findViewById(R.id.backgroundDialog);
                    backgroundDialog.setBackgroundColor(db.get_primary_background_color());
                    TextView messageDialogText = (TextView) dialog.findViewById(R.id.messageDialogText);
                    messageDialogText.setText(Language.getInstance().timerDialogMessage);
                    messageDialogText.setTextColor(db.get_primary_text_color());
                    Spinner hourSpinner = (Spinner) dialog.findViewById(R.id.hourSpinner);
                    ArrayList<String> hour = new ArrayList<>();
                    for (int i = 0; i <= 10; ++i) {
                        hour.add(String.valueOf(i) + "h");
                    }
                    timeSpinnerAdapter<String> hourSpinnerAdapter = new timeSpinnerAdapter<>(getApplicationContext(), hour,
                            db.get_secondary_background_color(), db.get_main_primary_text_color());
                    hourSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    hourSpinner.setAdapter(hourSpinnerAdapter);

                    Spinner minuteSpinner = (Spinner) dialog.findViewById(R.id.minuteSpinner);
                    ArrayList<String> minute = new ArrayList<>();
                    for (int i = 0; i <= 59; ++i) {
                        minute.add(String.valueOf(i) + "m");
                    }
                    timeSpinnerAdapter<String> minuteSpinnerAdapter = new timeSpinnerAdapter<>(getApplicationContext(), minute,
                            db.get_secondary_background_color(), db.get_main_primary_text_color());
                    minuteSpinner.setAdapter(minuteSpinnerAdapter);

                    Button optionDialogButton = (Button) dialog.findViewById(R.id.optionDialogButton);
                    optionDialogButton.setTextColor(db.get_main_primary_text_color());
                    optionDialogButton.setBackgroundColor(db.get_secondary_background_color());
                    optionDialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Spinner hourSpinner = (Spinner) dialog.findViewById(R.id.hourSpinner);
                            Spinner minuteSpinner = (Spinner) dialog.findViewById(R.id.minuteSpinner);
                            if (PlayerVar.getInstance().onTimer) {
                                Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                                playIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_STOP_TIMER);
                                sendBroadcast(playIntent);
                                dialog.dismiss();
                            } else {
                                PlayerVar.getInstance().hourTimer = hourSpinner.getSelectedItemPosition();
                                PlayerVar.getInstance().minTimer = minuteSpinner.getSelectedItemPosition();
                                if (PlayerVar.getInstance().minTimer == 0 && PlayerVar.getInstance().hourTimer == 0) {
                                    Toast.makeText(MediaPlayerView.this, Language.getInstance().invalidTimeTimer, Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                                    playIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_START_TIMER);
                                    sendBroadcast(playIntent);
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
                    if (PlayerVar.getInstance().onTimer) {
                        hourSpinner.setSelection(PlayerVar.getInstance().hourTimer);
                        minuteSpinner.setSelection(PlayerVar.getInstance().minTimer);
                        hourSpinner.setClickable(false);
                        minuteSpinner.setClickable(false);
                        optionDialogButton.setText(Language.getInstance().timerStopText);
                    } else {
                        hourSpinner.setSelection(0);
                        minuteSpinner.setSelection(0);
                        hourSpinner.setClickable(true);
                        minuteSpinner.setClickable(true);
                        optionDialogButton.setText(Language.getInstance().timerPlayText);
                    }
                    dialog.show();
                }
            }
        });
        ImageButton list_button=(ImageButton) findViewById(R.id.listButton);
        list_button.setImageResource(R.mipmap.see_current_list_icon);
        list_button.setContentDescription(Language.getInstance().CurrentPlayList);
        list_button.setBackgroundColor(db.get_primary_background_color());
        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PlayerVar.getInstance().validList) {
                    Intent intent = new Intent(getApplicationContext(), CurrentSongList.class);
                    startActivityForResult(intent, REQUEST_CODE_MEDIA_PLAYER_VIEW_REFRESH);
                }
            }
        });
        ImageButton equalizer_button=(ImageButton) findViewById(R.id.equalizerButton);
        equalizer_button.setImageResource(R.mipmap.equalizer_icon);
        equalizer_button.setContentDescription(Language.getInstance().EqualizerText);
        equalizer_button.setBackgroundColor(db.get_primary_background_color());
        equalizer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PlayerVar.getInstance().validList) {
                    Intent intent = new Intent(getApplicationContext(), EqualizerList.class);
                    startActivityForResult(intent, REQUEST_CODE_MEDIA_PLAYER_VIEW_EQUALIZER);
                }
            }
        });
    }

    private void refreshSongList(){
        Intent stopIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        stopIntent.putExtra("MP-M-CODE",PlayerVar.MP_M_STOP);
        sendBroadcast(stopIntent);
    }

    private void refreshViews(){
        if(PlayerVar.getInstance().validList){
            ImageButton play_pause_button=(ImageButton) findViewById(R.id.playPause);
            play_pause_button.setImageResource((PlayerVar.getInstance().onPause)? R.mipmap.play_media_player_icon: R.mipmap.pause_media_player_icon);
            Song selected_item=PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index);
            TextView titleText = (TextView) findViewById(R.id.titleText);
            titleText.setText(selected_item.getTitle());
            TextView artistText = (TextView) findViewById(R.id.artistText);
            artistText.setText(selected_item.getArtist());
            TextView albumText = (TextView) findViewById(R.id.albumText);
            albumText.setText(selected_item.getAlbum());
            refreshAlbumImage();
            TextView lyricsText = (TextView) findViewById(R.id.lyricsText);
            String lyrics = selected_item.getLyrics();
            if (lyrics == null) {
                lyrics = Language.getInstance().NoAvailableLyricsInFile;
            }
            lyricsText.setText(lyrics);
            TextView positionText = (TextView) findViewById(R.id.positionText);
            positionText.setText(Fun.to_string_time_format(PlayerVar.getInstance().mediaPlayer.getCurrentPosition()));
            TextView durationText = (TextView) findViewById(R.id.durationText);
            durationText.setText(Fun.to_string_time_format(PlayerVar.getInstance().mediaPlayer.getDuration()));
            SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
            seekBar.setMax(PlayerVar.getInstance().mediaPlayer.getDuration());
            seekBar.setProgress(PlayerVar.getInstance().mediaPlayer.getCurrentPosition());
            ImageButton favorite_button=(ImageButton) findViewById(R.id.favoriteButton);
            favorite_button.setImageResource((selected_item.getBookmark()==1)?R.mipmap.favorites_icon:R.mipmap.no_favorites_icon);
            play_pause_button.invalidate();
            titleText.invalidate();
            artistText.invalidate();
            albumText.invalidate();
            if(lyricsText.getVisibility()==View.VISIBLE) {
                lyricsText.invalidate();
            }
            positionText.invalidate();
            durationText.invalidate();
            favorite_button.invalidate();
            handler.removeCallbacks(r);
            this.runOnUiThread(r);
        }
    }

    private void refreshAlbumImage(){
        if(onMeasured){
            if(PlayerVar.getInstance().validList && PlayerVar.getInstance().index!=-1) {
                ImageView albumImage = (ImageView) findViewById(R.id.albumArtImage);
                albumImage.setImageResource(R.mipmap.no_album);
                byte[] drawable = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getAlbumArt();
                if (drawable != null && drawable.length>0) {
                    if(Fun.refresh_album_art(drawable, albumImage.getMeasuredHeight(), albumImage.getMeasuredWidth()))
                        albumImage.setImageBitmap(Var.getInstance().album_bitmap);
                }
                albumImage.invalidate();
            }
        }
    }

    private void refreshOnlyButtonViews(){
        if(PlayerVar.getInstance().validList) {
            ImageButton play_pause_button = (ImageButton) findViewById(R.id.playPause);
            if (PlayerVar.getInstance().onPause) {
                play_pause_button.setImageResource(R.mipmap.play_media_player_icon);
            } else {
                play_pause_button.setImageResource(R.mipmap.pause_media_player_icon);
            }
            ImageButton timer_button=(ImageButton) findViewById(R.id.timerButton);
            timer_button.setImageResource((PlayerVar.getInstance().onTimer)?R.mipmap.timer_on_icon:R.mipmap.timer_off_icon);
            play_pause_button.invalidate();
            timer_button.invalidate();
        }
    }

    private int type;
    private int index;
    private String artist;
    private String album;
    private String genre;
    private int playlist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        single_toast=Toast.makeText(MediaPlayerView.this, "", Toast.LENGTH_SHORT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_view);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MEDIA_PLAYER_VIEW_ACTION);
        registerReceiver(bReceiver, intentFilter);
        db=new DBHandler(this);
        Intent mIntent = getIntent();
        type = mIntent.getIntExtra("type", Var.TYPE_CURRENT_PLAYLIST);
        index = mIntent.getIntExtra("index", -1);
        artist = mIntent.getStringExtra("artist");
        album = mIntent.getStringExtra("album");
        genre = mIntent.getStringExtra("genre");
        playlist_id = mIntent.getIntExtra("metadata", -1);
        initViews();
        if(type!=Var.TYPE_CURRENT_PLAYLIST){
            refreshSongList();
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(type==Var.TYPE_CURRENT_PLAYLIST)
            refreshViews();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_MEDIA_PLAYER_VIEW_REFRESH:
                break;
            case REQUEST_CODE_MEDIA_PLAYER_VIEW_EQUALIZER:
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
    protected void onDestroy(){
        unregisterReceiver(bReceiver);
        if(db!=null){
            db.close();
            db=null;
        }
        single_toast=null;
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        handler.removeCallbacks(r);
        super.onDestroy();
    }
}
