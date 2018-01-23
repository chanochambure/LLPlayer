package lexrislogic.llplayer.Singleton;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

import lexrislogic.llplayer.Models.Album;
import lexrislogic.llplayer.Models.Song;

public class Var {
    public final static int TYPE_CURRENT_PLAYLIST = 1;
    public final static int TYPE_ALL_SONGS = 2;
    public final static int TYPE_FAVORITE_SONGS = 3;
    public final static int TYPE_MOST_PLAYED_SONGS = 4;
    public final static int TYPE_PLAYLIST = 5;
    public final static int TYPE_ARTIST = 6;
    public final static int TYPE_GENRE = 7;
    public final static int TYPE_ARTIST_ALBUM = 8;

    public final static int TYPE_COLOR_BACKGROUND_PRIMARY = 161;
    public final static int TYPE_COLOR_BACKGROUND_SECONDARY = 162;
    public final static int TYPE_COLOR_PRIMARY_FIRST_TEXT= 163;
    public final static int TYPE_COLOR_PRIMARY_SECOND_TEXT= 164;
    public final static int TYPE_COLOR_SECONDARY_FIRST_TEXT= 165;
    public final static int TYPE_COLOR_SECONDARY_SECOND_TEXT= 166;

    public final static String UNKNOWN_ARTIST = "<unknown artist>";
    public final static String UNKNOWN_ALBUM = "<unknown album>";
    public final static String UNKNOWN_SONG = "<unknown song>";
    public final static String UNKNOWN_GENRE = "<unknown genre>";

    public final static int REPEAT_TYPE_NO_REPEAT= 0;
    public final static int REPEAT_TYPE_REPEAT_LIST= 1;
    public final static int REPEAT_TYPE_REPEAT_SONG= 2;

    public final static int TYPE_BROWSE_ARTIST = 77;
    public final static int TYPE_BROWSE_ALBUM = 78;
    public final static int TYPE_BROWSE_GENRE = 79;

    public final static int TYPE_MAINTENANCE_NEW_SONG=65;
    public final static int TYPE_MAINTENANCE_LOST_SONGS=66;
    public final static int TYPE_MAINTENANCE_REPLACE_SONG=67;
    public final static int TYPE_MAINTENANCE_REMOVE_SONG=68;

    public final static int TYPE_SETTINGS_THEME=95;
    public final static int TYPE_SETTINGS_LANGUAGE=96;
    public final static int TYPE_SETTINGS_PLAYER=97;

    public final static int TYPE_PLAYLIST_RENAME=97;
    public final static int TYPE_PLAYLIST_MODIFY=98;
    public final static int TYPE_PLAYLIST_REMOVE=99;

    public final static int TYPE_USER_MANUAL_MEDIA_PLAYER=25;
    public final static int TYPE_USER_MANUAL_SONGS_MANAGEMENT =26;
    public final static int TYPE_USER_MANUAL_PLAYLIST=27;
    public final static int TYPE_USER_MANUAL_PLAYMODE=28;

    public final static int TYPE_SONG_SHARE=30;
    public final static int TYPE_SONG_RINGTONE=31;
    public final static int TYPE_SONG_DELETE=32;

    public int notification_height=48;
    public int notification_width=48;
    public Bitmap notification_bitmap=null;
    public Bitmap album_notification_bitmap=null;
    public Bitmap album_bitmap=null;
    public Canvas notification_canvas = null;
    public Canvas album_canvas = null;
    public Bitmap album_temp=null;
    public int lastWidthImageView=0;
    public int lastHeightImageView=0;
    public boolean onApp=false;
    public boolean onService=false;
    public int lastColor=0;
    public int lastTypeColor=0;
    public boolean changeColor=false;
    public ArrayList<Song> playListSongs=null;
    public ArrayList<Album> albumList=new ArrayList<>();
    public boolean albumTypeList=false;
    public HashMap<Long,Drawable> allAlbumArts=new HashMap<>();
    public boolean backupReady=false;
    private static Var instance = new Var( );
    private Var() {
    }
    public static Var getInstance() {
        return instance;
    }
}
