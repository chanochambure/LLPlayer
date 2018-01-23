package lexrislogic.llplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import lexrislogic.llplayer.Singleton.PlayerVar;

public class MediaPlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if(Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event != null) {
                int action = event.getAction();
                if (action == KeyEvent.ACTION_DOWN) {
                    action = event.getKeyCode();
                    switch (action) {
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            prevSongMediaPlayer(context);
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            nextSongMediaPlayer(context);
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            playPauseMediaPlayer(context);
                            break;
                    }
                }
            }
        }
    }
    private void playPauseMediaPlayer(Context context){
        Intent playIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        playIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_PLAY_PAUSE);
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