package lexrislogic.llplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Models.PlayMode;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;

public class MediaPlayerService extends Service implements
                                                MediaPlayer.OnPreparedListener,
                                                MediaPlayer.OnErrorListener,
                                                MediaPlayer.OnCompletionListener  {
    private void notification(String Text){
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(this, NotificationControl.class);
        stopIntent.setAction(NotificationControl.NOTIFICATION_CODE_CLOSE);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0,
                stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent prevIntent = new Intent(this, NotificationControl.class);
        prevIntent.setAction(NotificationControl.NOTIFICATION_CODE_PREV_SONG);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0,
                prevIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent pauseIntent = new Intent(this, NotificationControl.class);
        pauseIntent.setAction(NotificationControl.NOTIFICATION_CODE_PLAY_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0,
                pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent nextIntent = new Intent(this, NotificationControl.class);
        nextIntent.setAction(NotificationControl.NOTIFICATION_CODE_NEXT_SONG);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0,
                nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.media_player_notification);
        remoteViews.setOnClickPendingIntent(R.id.imageButton,stopPendingIntent);
        remoteViews.setImageViewResource(R.id.imageButton, R.mipmap.close_media_player_icon);
        remoteViews.setOnClickPendingIntent(R.id.imageButton2, nextPendingIntent);
        remoteViews.setImageViewResource(R.id.imageButton2, R.mipmap.next_media_player_icon_notification);
        remoteViews.setOnClickPendingIntent(R.id.imageButton3, pausePendingIntent);
        remoteViews.setImageViewResource(R.id.imageButton3, (PlayerVar.getInstance().onPause) ? R.mipmap.play_media_player_icon_notification : R.mipmap.pause_media_player_icon_notification);
        remoteViews.setOnClickPendingIntent(R.id.imageButton4, prevPendingIntent);
        remoteViews.setImageViewResource(R.id.imageButton4, R.mipmap.prev_media_player_icon_notification);
        remoteViews.setTextViewText(R.id.main_text, Text);
        remoteViews.setTextViewText(R.id.music_text, PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getTitle());
        remoteViews.setTextViewText(R.id.secondary_text, PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getArtist());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon((PlayerVar.getInstance().onPause) ? R.mipmap.main_pause_media_player_icon_notification : R.mipmap.main_play_media_player_icon_notification)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX);
        Fun.refresh_notification_data(getApplicationContext());
        Drawable drawable = Fun.get_album_art(this, PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getAlbum_id());
        remoteViews.setImageViewBitmap(R.id.albumImage, Var.getInstance().notification_bitmap);
        if(drawable!=null){
            Fun.refresh_notification_album_art(drawable);
            if(Var.getInstance().album_notification_bitmap!=null) {
                remoteViews.setImageViewBitmap(R.id.albumImage, Var.getInstance().album_notification_bitmap);
            }
        }
        Notification not = builder.build();
        not.bigContentView=remoteViews;
        startForeground(4567, not);
    }

    public final static String MEDIA_PLAYER_ACTION = "MediaPlayerService";
    private TelephonyManager mgr=null;
    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                if(!PlayerVar.getInstance().onPause){
                    playPause();
                    paused_by_service=true;
                }
                PlayerVar.getInstance().on_call=true;
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlayerVar.getInstance().on_call=false;
                        if(paused_by_service){
                            playPause();
                            paused_by_service=false;
                        }
                    }
                },1000);
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                if(!PlayerVar.getInstance().onPause){
                    playPause();
                    paused_by_service=true;
                }
                PlayerVar.getInstance().on_call=true;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };
    private boolean paused_by_service=false;
    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(MEDIA_PLAYER_ACTION.equals(action)) {
                int extra_option=intent.getIntExtra("MP-X-CODE",PlayerVar.MP_X_ANY);
                switch (intent.getIntExtra("MP-M-CODE",PlayerVar.MP_M_ANY)){
                    case PlayerVar.MP_M_STOP:
                        stopMediaPlayer(extra_option);
                        break;
                    case PlayerVar.MP_M_START:
                        startMediaPlayer();
                        break;
                    case PlayerVar.MP_M_PLAY_PAUSE:
                        playPause();
                        break;
                    case PlayerVar.MP_M_PREV_SONG:
                        prevSong();
                        break;
                    case PlayerVar.MP_M_NEXT_SONG:
                        nextSong(extra_option);
                        break;
                    case PlayerVar.MP_M_START_TIMER:
                        startTimerPlayer();
                        break;
                    case PlayerVar.MP_M_STOP_TIMER:
                        stopTimerPlayer();
                        break;
                    case PlayerVar.MP_M_NEW_SONG:
                        playNewSong();
                        break;
                    case PlayerVar.MP_M_EQUALIZER:
                        updateEqualizer();
                        break;
                    case PlayerVar.MP_M_TEST_EQUALIZER:
                        useTestEqualizer();
                        break;
                    case PlayerVar.MP_M_UPDATE_LANGUAGE:
                        updateNotification();
                        break;
                    case PlayerVar.MP_M_ANY:
                        break;
                    default:
                        break;
                }
            } else if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)){
                if(PlayerVar.getInstance().auto_stop_option) {
                    paused_by_service=false;
                    if (!PlayerVar.getInstance().onPause)
                        playPause();
                }
            } else {
                Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void updateNotification(){
        if(PlayerVar.getInstance().onForeground){
            notification(PlayerVar.getInstance().onPause ? Language.getInstance().Paused : Language.getInstance().Playing);
        }
    }

    private void updateEqualizer(){
        short numberFrequencyBands = PlayerVar.getInstance().mediaEqualizer.getNumberOfBands();
        PlayMode playMode=db.get_play_mode(PlayerVar.getInstance().play_mode_index);
        String[] parts = playMode.getData().split("o");
        for (short i = 0; i < numberFrequencyBands; i++) {
            PlayerVar.getInstance().mediaEqualizer.setBandLevel(i, (short) (Integer.parseInt(parts[i])));
        }
    }

    private void useTestEqualizer(){
        short numberFrequencyBands = PlayerVar.getInstance().mediaEqualizer.getNumberOfBands();
        String[] parts = PlayerVar.getInstance().testPlayMode.getData().split("o");
        for (short i = 0; i < numberFrequencyBands; i++) {
            PlayerVar.getInstance().mediaEqualizer.setBandLevel(i, (short) (Integer.parseInt(parts[i])));
        }
    }

    private void playNewSong(){
        if(!PlayerVar.getInstance().onPause) {
            PlayerVar.getInstance().timePlaying +=
                    (Calendar.getInstance().getTimeInMillis() - PlayerVar.getInstance().lastTime.getTimeInMillis());
        }
        if(PlayerVar.getInstance().timePlaying!=0) {
            PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).addSeconds(PlayerVar.getInstance().timePlaying);
            db.update_song(PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index));
        }
        PlayerVar.getInstance().index=PlayerVar.getInstance().new_index;
        PlayerVar.getInstance().random_index.clear();
        playSong();
    }

    private final Handler handler = new Handler();
    private final Runnable r = new Runnable() {
        @Override
        public void run() {
            if(PlayerVar.getInstance().on_call)
                paused_by_service=false;
            PlayerVar.getInstance().onTimer=false;
            PlayerVar.getInstance().hourTimer=0;
            PlayerVar.getInstance().minTimer=0;
            stopMediaPlayer(PlayerVar.MP_X_CLOSE_NOTIFICATION);
            Toast.makeText(MediaPlayerService.this, Language.getInstance().timerActivatedMessage, Toast.LENGTH_SHORT).show();
        }
    };

    private void startTimerPlayer(){
        PlayerVar.getInstance().onTimer=true;
        int mSeconds=PlayerVar.getInstance().hourTimer*3600+PlayerVar.getInstance().minTimer*60;
        mSeconds*=1000;
        handler.postDelayed(r,mSeconds);
        Toast.makeText(MediaPlayerService.this, Language.getInstance().timerActivated, Toast.LENGTH_SHORT).show();
        Intent playingIntent = new Intent(MediaPlayerView.MEDIA_PLAYER_VIEW_ACTION);
        playingIntent.putExtra("MP-V-CODE", PlayerVar.MP_V_READY_GET_INFO_BUTTON);
        sendBroadcast(playingIntent);
    }

    private void stopTimerPlayer(){
        handler.removeCallbacks(r);
        PlayerVar.getInstance().onTimer=false;
        PlayerVar.getInstance().hourTimer=0;
        PlayerVar.getInstance().minTimer=0;
        Toast.makeText(MediaPlayerService.this, Language.getInstance().timerCanceled, Toast.LENGTH_SHORT).show();
        Intent playingIntent = new Intent(MediaPlayerView.MEDIA_PLAYER_VIEW_ACTION);
        playingIntent.putExtra("MP-V-CODE", PlayerVar.MP_V_READY_GET_INFO_BUTTON);
        sendBroadcast(playingIntent);
    }

    private void startMediaPlayer(){
        if(PlayerVar.getInstance().validList){
            Fun.validateIndex();
            playSong();
        }
        else {
            PlayerVar.getInstance().index=-1;
            Toast.makeText(MediaPlayerService.this, Language.getInstance().messageInvalid, Toast.LENGTH_SHORT).show();
        }
    }
    private void stopMediaPlayer(int extra_option){
        if(PlayerVar.getInstance().on_call)
            paused_by_service=false;
        if(PlayerVar.getInstance().isPrepared && !PlayerVar.getInstance().onPause) {
            playPause();
        }
        onStopForeground();
        if(extra_option==PlayerVar.MP_X_ANY) {
            PlayerVar.getInstance().random_index.clear();
            Intent playingIntent = new Intent(MediaPlayerView.MEDIA_PLAYER_VIEW_ACTION);
            playingIntent.putExtra("MP-V-CODE", PlayerVar.MP_V_READY_SET_LIST);
            sendBroadcast(playingIntent);
        }else if(extra_option==PlayerVar.MP_X_CLOSE_NOTIFICATION){
            Intent playingIntent = new Intent(MediaPlayerView.MEDIA_PLAYER_VIEW_ACTION);
            playingIntent.putExtra("MP-V-CODE", PlayerVar.MP_V_READY_GET_INFO_BUTTON);
            sendBroadcast(playingIntent);
        }
    }
    private void onStartForeground(){
        PlayerVar.getInstance().onForeground=true;
        notification(PlayerVar.getInstance().onPause ? Language.getInstance().Paused : Language.getInstance().Playing);
    }
    private void onStopForeground(){
        Fun.savePlayerVar(this);
        if(PlayerVar.getInstance().validList && PlayerVar.getInstance().timePlaying!=0 && PlayerVar.getInstance().index!=-1) {
            PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).addSeconds(PlayerVar.getInstance().timePlaying);
            db.update_song(PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index));
            PlayerVar.getInstance().timePlaying=0;
        }
        if(!Var.getInstance().onApp) {
            remove_all();
            stopSelf();
        }
        stopForeground(true);
        PlayerVar.getInstance().onForeground=false;
    }
    private void remove_all(){
        handler.removeCallbacks(r);
        if(mgr!=null){
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            mgr=null;
        }
        if(registeredReceiver) {
            unregisterReceiver(bReceiver);
            registeredReceiver =false;
        }
        if(db!=null){
            db.close();
            db=null;
        }
        if(PlayerVar.getInstance().mediaPlayer!=null){
            if(PlayerVar.getInstance().isPrepared)
                PlayerVar.getInstance().mediaPlayer.stop();
            if(PlayerVar.getInstance().mediaEqualizer!=null){
                PlayerVar.getInstance().mediaEqualizer.release();
                PlayerVar.getInstance().mediaEqualizer=null;
            }
            PlayerVar.getInstance().mediaPlayer.release();
            PlayerVar.getInstance().mediaPlayer=null;
        }
        PlayerVar.getInstance().onPause=true;
    }

    private Random rand=null;
    private boolean canPlay=true;

    private void playPause(){
        if(PlayerVar.getInstance().isPrepared && !PlayerVar.getInstance().on_call) {
            if (PlayerVar.getInstance().onPause) {
                PlayerVar.getInstance().lastTime=Calendar.getInstance();
                PlayerVar.getInstance().mediaPlayer.start();
            } else{
                PlayerVar.getInstance().timePlaying+=
                        (Calendar.getInstance().getTimeInMillis() - PlayerVar.getInstance().lastTime.getTimeInMillis());
                PlayerVar.getInstance().mediaPlayer.pause();
            }
            PlayerVar.getInstance().onPause = !PlayerVar.getInstance().onPause;
            onStartForeground();
            Intent playingIntent = new Intent(MediaPlayerView.MEDIA_PLAYER_VIEW_ACTION);
            playingIntent.putExtra("MP-V-CODE", PlayerVar.MP_V_READY_GET_INFO_BUTTON);
            sendBroadcast(playingIntent);
        }
    }
    private void prevSong(){
        if(PlayerVar.getInstance().validList) {
            if(!PlayerVar.getInstance().onPause) {
                PlayerVar.getInstance().timePlaying +=
                        (Calendar.getInstance().getTimeInMillis() - PlayerVar.getInstance().lastTime.getTimeInMillis());
            }
            if(PlayerVar.getInstance().timePlaying!=0) {
                PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).addSeconds(PlayerVar.getInstance().timePlaying);
                db.update_song(PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index));
            }
            if(!PlayerVar.getInstance().auto_replay_option || PlayerVar.getInstance().mediaPlayer.getCurrentPosition()<5000) {
                if (PlayerVar.getInstance().shuffle_option) {
                    boolean valid = false;
                    while (PlayerVar.getInstance().random_index.size() > 1) {
                        PlayerVar.getInstance().random_index.remove(PlayerVar.getInstance().random_index.size() - 1);
                        int last_index = PlayerVar.getInstance().random_index.get(PlayerVar.getInstance().random_index.size() - 1);
                        PlayerVar.getInstance().index = last_index;
                        Fun.validateIndex();
                        if (PlayerVar.getInstance().index == last_index) {
                            PlayerVar.getInstance().random_index.remove(PlayerVar.getInstance().random_index.size() - 1);
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) {
                        PlayerVar.getInstance().index = PlayerVar.getInstance().random_index.get(PlayerVar.getInstance().random_index.size() - 1);
                        PlayerVar.getInstance().random_index.remove(PlayerVar.getInstance().random_index.size() - 1);
                        Fun.validateIndex();
                    }
                } else {
                    PlayerVar.getInstance().index--;
                    if (PlayerVar.getInstance().index < 0)
                        PlayerVar.getInstance().index = PlayerVar.getInstance().song_list.size() - 1;
                    Fun.validateBackIndex();
                }
                playSong();
            } else {
                if(PlayerVar.getInstance().isPrepared)
                    PlayerVar.getInstance().mediaPlayer.seekTo(0);
            }
        }
    }
    private void nextSong(int extra_option){
        if(PlayerVar.getInstance().validList) {
            if(!PlayerVar.getInstance().onPause) {
                PlayerVar.getInstance().timePlaying +=
                        (Calendar.getInstance().getTimeInMillis() - PlayerVar.getInstance().lastTime.getTimeInMillis());
            }
            if(PlayerVar.getInstance().timePlaying!=0) {
                PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).addSeconds(PlayerVar.getInstance().timePlaying);
                db.update_song(PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index));
            }
            if(extra_option==PlayerVar.MP_X_BOT){
                if(PlayerVar.getInstance().repeat_option!=Var.REPEAT_TYPE_REPEAT_SONG){
                    if (PlayerVar.getInstance().shuffle_option) {
                        do {
                            if (PlayerVar.getInstance().random_index.size() >= PlayerVar.LIMIT_INDEX ||
                                    PlayerVar.getInstance().random_index.size() >= PlayerVar.getInstance().song_list.size())
                                PlayerVar.getInstance().random_index.remove(0);
                            do {
                                PlayerVar.getInstance().index = rand.nextInt(PlayerVar.getInstance().song_list.size());
                            }
                            while (PlayerVar.getInstance().random_index.contains(PlayerVar.getInstance().index));
                            int last_index = PlayerVar.getInstance().index;
                            Fun.validateIndex();
                            if (last_index != PlayerVar.getInstance().index)
                                PlayerVar.getInstance().random_index.add(last_index);
                        }
                        while (PlayerVar.getInstance().random_index.contains(PlayerVar.getInstance().index));
                    } else {
                        PlayerVar.getInstance().index++;
                        if (PlayerVar.getInstance().index >= PlayerVar.getInstance().song_list.size()) {
                            if(PlayerVar.getInstance().repeat_option==Var.REPEAT_TYPE_NO_REPEAT)
                                canPlay=false;
                            PlayerVar.getInstance().index = 0;
                        }
                        Fun.validateIndex();
                        if(PlayerVar.getInstance().finishList){
                            if(PlayerVar.getInstance().repeat_option==Var.REPEAT_TYPE_NO_REPEAT)
                                canPlay=false;
                            PlayerVar.getInstance().finishList=false;
                        }
                    }
                }
            }else {
                if (PlayerVar.getInstance().shuffle_option) {
                    do {
                        if (PlayerVar.getInstance().random_index.size() >= PlayerVar.LIMIT_INDEX ||
                                PlayerVar.getInstance().random_index.size() >= PlayerVar.getInstance().song_list.size())
                            PlayerVar.getInstance().random_index.remove(0);
                        do {
                            PlayerVar.getInstance().index = rand.nextInt(PlayerVar.getInstance().song_list.size());
                        }
                        while (PlayerVar.getInstance().random_index.contains(PlayerVar.getInstance().index));
                        int last_index = PlayerVar.getInstance().index;
                        Fun.validateIndex();
                        if (last_index != PlayerVar.getInstance().index)
                            PlayerVar.getInstance().random_index.add(last_index);
                    }
                    while (PlayerVar.getInstance().random_index.contains(PlayerVar.getInstance().index));
                } else {
                    PlayerVar.getInstance().index++;
                    if (PlayerVar.getInstance().index >= PlayerVar.getInstance().song_list.size())
                        PlayerVar.getInstance().index = 0;
                    Fun.validateIndex();
                }
            }
            playSong();
        }
    }

    private DBHandler db=null;

    private void playSong(){
        if(PlayerVar.getInstance().validList){
            if(PlayerVar.getInstance().isPrepared) {
                PlayerVar.getInstance().mediaPlayer.stop();
                PlayerVar.getInstance().isPrepared = false;
            }
            PlayerVar.getInstance().mediaPlayer.reset();
            if(PlayerVar.getInstance().shuffle_option) {
                PlayerVar.getInstance().random_index.add(PlayerVar.getInstance().index);
            }
            Song playSong = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index);
            try {
                PlayerVar.getInstance().mediaPlayer.setDataSource(playSong.getData());
            } catch (Exception ignored) {
            }
            PlayerVar.getInstance().mediaPlayer.prepareAsync();
        }
    }
    private boolean registeredReceiver =false;
    @Override
    public void onCreate() {
        Var.getInstance().onService=true;
        if(PlayerVar.getInstance().onCreateDB) {
            Fun.savePlayerVar(this);
        }
        mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr!=null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.addAction(MEDIA_PLAYER_ACTION);
        registerReceiver(bReceiver, intentFilter);
        registeredReceiver =true;
        db=new DBHandler(this);
        PlayerVar.getInstance().repeat_option=db.get_repeat_option();
        PlayerVar.getInstance().shuffle_option=db.get_shuffle_option();
        PlayerVar.getInstance().auto_stop_option=db.get_auto_stop_option();
        PlayerVar.getInstance().auto_replay_option=db.get_auto_replay_option();
        if(PlayerVar.getInstance().mediaPlayer==null){
            PlayerVar.getInstance().mediaPlayer = new MediaPlayer();
            PlayerVar.getInstance().mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            PlayerVar.getInstance().mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            PlayerVar.getInstance().mediaPlayer.setOnPreparedListener(this);
            PlayerVar.getInstance().mediaPlayer.setOnCompletionListener(this);
            PlayerVar.getInstance().mediaPlayer.setOnErrorListener(this);
        }
        if(PlayerVar.getInstance().mediaEqualizer==null){
            PlayerVar.getInstance().mediaEqualizer=new Equalizer(0,PlayerVar.getInstance().mediaPlayer.getAudioSessionId());
            PlayerVar.getInstance().mediaEqualizer.setEnabled(true);
            short numberFrequencyBands = PlayerVar.getInstance().mediaEqualizer.getNumberOfBands();
            PlayerVar.getInstance().mediaEqualizer.usePreset((short) 0);
            if(PlayerVar.getInstance().onCreateDB) {
                PlayerVar.getInstance().onCreateDB=false;
                short totalPreset=PlayerVar.getInstance().mediaEqualizer.getNumberOfPresets();
                for(short i=0;i<totalPreset;++i) {
                    PlayerVar.getInstance().mediaEqualizer.usePreset(i);
                    String presetName = PlayerVar.getInstance().mediaEqualizer.getPresetName(i);
                    String data = "";
                    for (short j = 0; j < numberFrequencyBands; j++) {
                        data += String.valueOf(PlayerVar.getInstance().mediaEqualizer.getBandLevel(j));
                        if (j < numberFrequencyBands - 1)
                            data += "o";
                    }
                    db.add_play_mode(new PlayMode(presetName, data));
                }
                PlayerVar.getInstance().mediaEqualizer.usePreset((short) 0);
            }
            else{
                PlayMode playMode=db.get_play_mode(PlayerVar.getInstance().play_mode_index);
                String[] parts = playMode.getData().split("o");
                for (short i = 0; i < numberFrequencyBands; i++) {
                    PlayerVar.getInstance().mediaEqualizer.setBandLevel(i, (short) (Integer.parseInt(parts[i])));
                }
            }
        }
        PlayerVar.getInstance().onPause=true;
        rand=new Random();
        if(PlayerVar.getInstance().validList){
            canPlay=false;
            Fun.validateIndex();
            playSong();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int id) {
        super.onStartCommand(intent, flags, id);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        remove_all();
        Var.getInstance().onService=false;
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        PlayerVar.getInstance().isPrepared=false;
        if(!PlayerVar.getInstance().onPause) {
            PlayerVar.getInstance().timePlaying +=
                    (Calendar.getInstance().getTimeInMillis() - PlayerVar.getInstance().lastTime.getTimeInMillis());
        }
        PlayerVar.getInstance().onPause=true;
        nextSong(PlayerVar.MP_X_BOT);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        stopForeground(true);
        PlayerVar.getInstance().timePlaying=0;
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        PlayerVar.getInstance().isPrepared=true;
        PlayerVar.getInstance().onPause=false;
        PlayerVar.getInstance().timePlaying=0;
        PlayerVar.getInstance().lastTime= Calendar.getInstance();
        if(canPlay && !PlayerVar.getInstance().on_call) {
            mp.start();
            onStartForeground();
        }
        else {
            canPlay = true;
            PlayerVar.getInstance().onPause=true;
            mp.start();
            mp.pause();
            if(PlayerVar.getInstance().last_position_saved!=-1) {
                PlayerVar.getInstance().mediaPlayer.seekTo(PlayerVar.getInstance().last_position_saved);
                PlayerVar.getInstance().last_position_saved=-1;
            }
            if(PlayerVar.getInstance().onForeground)
                onStopForeground();
        }
        Intent playingIntent = new Intent(MediaPlayerView.MEDIA_PLAYER_VIEW_ACTION);
        playingIntent.putExtra("MP-V-CODE", PlayerVar.MP_V_READY_GET_INFO);
        sendBroadcast(playingIntent);
    }
}
