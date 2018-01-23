package lexrislogic.llplayer.Singleton;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;

import java.util.ArrayList;
import java.util.Calendar;

import lexrislogic.llplayer.Models.PlayMode;
import lexrislogic.llplayer.Models.Song;

public class PlayerVar {
    public final static String PLAYER_VAR_FILENAME_SONG_LIST="llplayer_file_song_list";
    public final static String PLAYER_VAR_FILENAME_DATA="llplayer_file_data";
    public final static String PLAYER_VAR_FILENAME_BACKUP="llplayer_backup_file";
    public final static int LIMIT_INDEX=50;

    public final static int MP_M_ANY=0;
    public final static int MP_M_STOP=1;
    public final static int MP_M_START=2;
    public final static int MP_M_PLAY_PAUSE=3;
    public final static int MP_M_PREV_SONG=4;
    public final static int MP_M_NEXT_SONG=5;
    public final static int MP_M_START_TIMER=6;
    public final static int MP_M_STOP_TIMER=7;
    public final static int MP_M_NEW_SONG=8;
    public final static int MP_M_EQUALIZER=9;
    public final static int MP_M_TEST_EQUALIZER=10;
    public final static int MP_M_UPDATE_LANGUAGE=11;

    public final static int MP_X_ANY=0;
    public final static int MP_X_CLOSE_NOTIFICATION=1;
    public final static int MP_X_BOT=2;

    public final static int MP_V_ANY=0;
    public final static int MP_V_READY_SET_LIST=1;
    public final static int MP_V_READY_GET_INFO=2;
    public final static int MP_V_READY_GET_INFO_BUTTON=3;

    public boolean on_call=false;
    public int repeat_option=Var.REPEAT_TYPE_NO_REPEAT;
    public boolean shuffle_option=false;
    public boolean auto_stop_option=false;
    public boolean auto_replay_option=false;

    public MediaPlayer mediaPlayer=null;
    public Equalizer mediaEqualizer=null;
    public ArrayList<Song> song_list=null;
    public int last_position_saved=-1;
    public int index=-1;
    public int new_index=-1;
    public int play_mode_index=1;
    public boolean finishList=false;
    public boolean validList=false;
    public boolean onPause=true;
    public boolean isPrepared=false;
    public boolean onForeground=false;
    public boolean onTimer=false;
    public int hourTimer=0;
    public int minTimer=0;
    public String lastTitle=null;
    public ArrayList<Integer> random_index=null;
    public long timePlaying=0;
    public Calendar lastTime = null;

    public boolean onCreateDB=false;
    public ArrayList<PlayMode> playModes=null;
    public PlayMode testPlayMode=null;

    private static PlayerVar instance = new PlayerVar( );
    private PlayerVar() {
        song_list=new ArrayList<>();
        random_index = new ArrayList<>();
    }
    public static PlayerVar getInstance() {
        return instance;
    }
}
