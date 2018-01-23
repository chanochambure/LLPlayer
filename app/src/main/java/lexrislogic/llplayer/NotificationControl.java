package lexrislogic.llplayer;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lexrislogic.llplayer.Singleton.PlayerVar;

public class NotificationControl extends BroadcastReceiver {
    public final static String NOTIFICATION_CODE_ANY="NotificationControl.ANY";
    public final static String NOTIFICATION_CODE_CLOSE="NotificationControl.CLOSE";
    public final static String NOTIFICATION_CODE_PLAY_PAUSE="NotificationControl.PLAY_PAUSE";
    public final static String NOTIFICATION_CODE_PREV_SONG="NotificationControl.PREV_SONG";
    public final static String NOTIFICATION_CODE_NEXT_SONG="NotificationControl.NEXT_SONG";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(NOTIFICATION_CODE_CLOSE.equals(action)) {
            stopMediaPlayer(context);
        } else if(NOTIFICATION_CODE_PLAY_PAUSE.equals(action)) {
            playPauseMediaPlayer(context);
        } else if(NOTIFICATION_CODE_PREV_SONG.equals(action)) {
            prevSongMediaPlayer(context);
        } else if(NOTIFICATION_CODE_NEXT_SONG.equals(action)) {
            nextSongMediaPlayer(context);
        } else if(NOTIFICATION_CODE_ANY.equals(action)){
        }
    }
    private void stopMediaPlayer(Context context){
        Intent stopIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        stopIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_STOP);
        stopIntent.putExtra("MP-X-CODE", PlayerVar.MP_X_CLOSE_NOTIFICATION);
        context.sendBroadcast(stopIntent);
    }

    private void playPauseMediaPlayer(Context context){
        Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        playIntent.putExtra("MP-M-CODE",PlayerVar.MP_M_PLAY_PAUSE);
        context.sendBroadcast(playIntent);
    }

    private void prevSongMediaPlayer(Context context){
        Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        playIntent.putExtra("MP-M-CODE",PlayerVar.MP_M_PREV_SONG);
        context.sendBroadcast(playIntent);
    }

    private void nextSongMediaPlayer(Context context){
        Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        playIntent.putExtra("MP-M-CODE",PlayerVar.MP_M_NEXT_SONG);
        context.sendBroadcast(playIntent);
    }
}
