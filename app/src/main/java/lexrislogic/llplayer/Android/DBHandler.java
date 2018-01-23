package lexrislogic.llplayer.Android;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.*;

import lexrislogic.llplayer.Models.Album;
import lexrislogic.llplayer.Models.Artist;
import lexrislogic.llplayer.Models.Genre;
import lexrislogic.llplayer.Models.PlayList;
import lexrislogic.llplayer.Models.PlayMode;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.Singleton.Fun;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;
import lexrislogic.llplayer.Singleton.Language;

public class DBHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "LLPlayerDB";

    private static final String TABLE_CONFIGURATION ="user_configuration";
        private static final String KEY_C_SELECTED_LANGUAGE ="language_selected";
        private static final String KEY_C_SHUFFLE_OPTION="shuffle_option";
        private static final String KEY_C_REPEAT_OPTION="repeat_option";
        private static final String KEY_C_AUTO_STOP="auto_stop";
        private static final String KEY_C_AUTO_REPLAY="auto_replay";
        private static final String KEY_C_LAST_PHONE_ID ="last_phone_id";
        private static final String KEY_C_PRIMARY_TEXT_COLOR= "primary_text_color";
        private static final String KEY_C_SECONDARY_TEXT_COLOR= "secondary_text_color";
        private static final String KEY_C_PRIMARY_BACKGROUND_COLOR= "primary_background_color";
        private static final String KEY_C_SECONDARY_BACKGROUND_COLOR= "secondary_background_color";
        private static final String KEY_C_MAIN_P_TEXT_COLOR ="main_p_text_color";
        private static final String KEY_C_MAIN_S_TEXT_COLOR ="main_s_text_color";

    private static final String TABLE_SONGS ="songs";
        private static final String KEY_S_ID = "id";
        private static final String KEY_S_PHONE_ID = "phone_id";
        private static final String KEY_S_TITLE = "title";
        private static final String KEY_S_ARTIST = "artist";
        private static final String KEY_S_ALBUM = "album";
        private static final String KEY_S_GENRE= "genre";
        private static final String KEY_S_ALBUM_ID = "album_id";
        private static final String KEY_S_PATH = "path";
        private static final String KEY_S_SECONDS = "seconds_played";
        private static final String KEY_S_BOOKMARK = "bookmark";
        private static final String KEY_S_LAST_MOD_DATE = "last_mod_date";
        private static final String KEY_S_LOWER_PATH = "lower_path";

    private static final String TABLE_EQUALIZER_PLAY_MODES="equalizer_play_modes";
        private static final String KEY_E_ID = "id";
        private static final String KEY_E_NAME = "name";
        private static final String KEY_E_DATA = "data";

    private static final String TABLE_PLAYLIST="playlists";
        private static final String KEY_P_ID = "id";
        private static final String KEY_P_NAME = "name";

    private static final String TABLE_PLAYLIST_SONGS="playlists_songs";
        private static final String KEY_PS_ID_PLAYLIST = "id_playlist";
        private static final String KEY_PS_ID_SONG = "id_song";
        private static final String KEY_PS_POSITION = "position";
    private Context db_context=null;
    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db_context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        PlayerVar.getInstance().onCreateDB=true;
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONFIGURATION);
        String CREATE_CONFIGURATION_TABLE= "CREATE TABLE "+ TABLE_CONFIGURATION + "("
                + KEY_C_SELECTED_LANGUAGE + " INTEGER,"
                + KEY_C_SHUFFLE_OPTION + " INTEGER,"
                + KEY_C_REPEAT_OPTION + " INTEGER,"
                + KEY_C_AUTO_STOP + " INTEGER,"
                + KEY_C_LAST_PHONE_ID+ " INTEGER,"
                + KEY_C_PRIMARY_TEXT_COLOR + " INTEGER,"
                + KEY_C_SECONDARY_TEXT_COLOR + " INTEGER,"
                + KEY_C_PRIMARY_BACKGROUND_COLOR + " INTEGER,"
                + KEY_C_SECONDARY_BACKGROUND_COLOR + " INTEGER,"
                + KEY_C_MAIN_P_TEXT_COLOR + " INTEGER,"
                + KEY_C_MAIN_S_TEXT_COLOR + " INTEGER)";
        db.execSQL(CREATE_CONFIGURATION_TABLE);
        String INSERT_CONFIGURATION_OPTION_DEFAULT="INSERT INTO "+TABLE_CONFIGURATION +" VALUES ("
                + String.valueOf(Language.CODE_ES)+","
                + String.valueOf(0)+","
                + String.valueOf(Var.REPEAT_TYPE_NO_REPEAT)+","
                + String.valueOf(1)+","
                + String.valueOf(-1)+","
                + String.valueOf(Color.parseColor("#EEEEEE"))+","
                + String.valueOf(Color.parseColor("#555555"))+","
                + String.valueOf(Color.parseColor("#D97700"))+","
                + String.valueOf(Color.parseColor("#DDDDDD"))+","
                + String.valueOf(Color.parseColor("#000000"))+","
                + String.valueOf(Color.parseColor("#888888"))+")";
        db.execSQL(INSERT_CONFIGURATION_OPTION_DEFAULT);
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + " ("
                + KEY_S_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_S_PHONE_ID  + " INTEGER,"
                + KEY_S_TITLE + " TEXT,"
                + KEY_S_ARTIST + " TEXT,"
                + KEY_S_ALBUM + " TEXT,"
                + KEY_S_GENRE + " TEXT,"
                + KEY_S_ALBUM_ID + " INTEGER,"
                + KEY_S_PATH + " TEXT,"
                + KEY_S_SECONDS + " INTEGER,"
                + KEY_S_BOOKMARK + " INTEGER,"
                + KEY_S_LAST_MOD_DATE + " DATETIME,"
                + KEY_S_LOWER_PATH + " TEXT)";
        db.execSQL(CREATE_SONGS_TABLE);
        String CREATE_EQUALIZER_PLAY_MODES_TABLE = "CREATE TABLE " + TABLE_EQUALIZER_PLAY_MODES+ " ("
                + KEY_E_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_E_NAME + " TEXT,"
                + KEY_E_DATA + " TEXT)";
        db.execSQL(CREATE_EQUALIZER_PLAY_MODES_TABLE);
        String CREATE_PLAY_LISTS_TABLE = "CREATE TABLE " + TABLE_PLAYLIST+ " ("
                + KEY_P_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_P_NAME+ " TEXT)";
        db.execSQL(CREATE_PLAY_LISTS_TABLE );
        String CREATE_PLAY_LISTS_SONGS = "CREATE TABLE " + TABLE_PLAYLIST_SONGS+ " ("
                + KEY_PS_ID_PLAYLIST+ " INTEGER,"
                + KEY_PS_ID_SONG + " INTEGER,"
                + KEY_PS_POSITION + " INTEGER)";
        db.execSQL(CREATE_PLAY_LISTS_SONGS);
        db.execSQL("ALTER TABLE "+TABLE_CONFIGURATION+" ADD COLUMN "+KEY_C_AUTO_REPLAY+" INTEGER DEFAULT 1");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if((oldVersion==1) && newVersion==2) {
            db.execSQL("ALTER TABLE "+TABLE_CONFIGURATION+" ADD COLUMN "+KEY_C_AUTO_REPLAY+" INTEGER DEFAULT 1");
        }
    }
    //Configuration
    //Get Language
    public int get_language() {
        String selectQuery = "SELECT "+KEY_C_SELECTED_LANGUAGE+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Language
    public int update_language()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_SELECTED_LANGUAGE, Language.getInstance().get_language());
        int return_value=db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Shuffle Option
    public boolean get_shuffle_option() {
        String selectQuery = "SELECT "+KEY_C_SHUFFLE_OPTION+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value==1;
    }
    //Change Shuffle Option
    public int update_shuffle_option(boolean new_shuffle_option)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_SHUFFLE_OPTION, (new_shuffle_option) ? 1 : 0);
        int return_value= db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Auto Stop Option
    public boolean get_auto_stop_option() {
        String selectQuery = "SELECT "+KEY_C_AUTO_STOP+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value==1;
    }
    //Change Auto Stop Option
    public int update_auto_stop_option(boolean new_auto_stop_option)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_AUTO_STOP, (new_auto_stop_option) ? 1 : 0);
        int return_value= db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Auto Replay Option
    public boolean get_auto_replay_option() {
        String selectQuery = "SELECT "+KEY_C_AUTO_REPLAY+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value==1;
    }
    //Change Auto Replay Option
    public int update_auto_replay_option(boolean new_auto_replay_option)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_AUTO_REPLAY, (new_auto_replay_option) ? 1 : 0);
        int return_value= db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Repeat Option
    public int get_repeat_option() {
        String selectQuery = "SELECT "+KEY_C_REPEAT_OPTION+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Repeat Option
    public int update_repeat_option(int new_repeat_option)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_REPEAT_OPTION, new_repeat_option);
        int return_value = db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Last Phone ID
    public long get_last_phone_id() {
        String selectQuery = "SELECT "+KEY_C_LAST_PHONE_ID+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        long return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getLong(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Primary Text Color
    public int update_last_phone_id(SQLiteDatabase db, long new_last_phone_id)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_C_LAST_PHONE_ID, new_last_phone_id);
        return db.update(TABLE_CONFIGURATION, values,null,null);
    }
    //Get Primary Text Color
    public int get_primary_text_color() {
        String selectQuery = "SELECT "+KEY_C_PRIMARY_TEXT_COLOR+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Primary Text Color
    public int update_primary_text_color(int new_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_PRIMARY_TEXT_COLOR, new_color);
        int return_value = db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Secondary Text Color
    public int get_secondary_text_color() {
        String selectQuery = "SELECT "+KEY_C_SECONDARY_TEXT_COLOR+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Secondary Text Color
    public int update_secondary_text_color(int new_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_SECONDARY_TEXT_COLOR, new_color);
        int return_value = db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Primary Background Color
    public int get_primary_background_color() {
        String selectQuery = "SELECT "+KEY_C_PRIMARY_BACKGROUND_COLOR+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Primary Background Color
    public int update_primary_background_color(int new_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_PRIMARY_BACKGROUND_COLOR, new_color);
        int return_value = db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Secondary Background Color
    public int get_secondary_background_color() {
        String selectQuery = "SELECT "+KEY_C_SECONDARY_BACKGROUND_COLOR+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Secondary Background Color
    public int update_secondary_background_color(int new_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_SECONDARY_BACKGROUND_COLOR, new_color);
        int return_value = db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Main Primary Text Color
    public int get_main_primary_text_color() {
        String selectQuery = "SELECT "+KEY_C_MAIN_P_TEXT_COLOR+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Main Primary Text Color
    public int update_main_primary_text_color(int new_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_MAIN_P_TEXT_COLOR, new_color);
        int return_value=db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }
    //Get Main Secondary Text Color
    public int get_main_secondary_text_color() {
        String selectQuery = "SELECT "+KEY_C_MAIN_S_TEXT_COLOR+" FROM " + TABLE_CONFIGURATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int return_value = 0;
        if (cursor.moveToFirst()) {
            return_value = (cursor.getInt(0));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Change Main Secondary Text Color
    public int update_main_secondary_text_color(int new_color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_C_MAIN_S_TEXT_COLOR, new_color);
        int return_value = db.update(TABLE_CONFIGURATION, values,null,null);
        db.close();
        return return_value;
    }

    //PlayModeEqualizer
    //Get PlayModes
    public ArrayList<PlayMode> get_play_modes() {
        ArrayList<PlayMode> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EQUALIZER_PLAY_MODES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new PlayMode(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    //Get PlayMode
    public PlayMode get_play_mode(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_EQUALIZER_PLAY_MODES+
                " WHERE "+KEY_E_ID+"=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        PlayMode return_value=null;
        if (cursor.moveToFirst()) {
            return_value= new PlayMode(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Add PlayMode
    public void add_play_mode(PlayMode playMode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_E_NAME, playMode.getName());
        values.put(KEY_E_DATA, playMode.getData());
        db.insert(TABLE_EQUALIZER_PLAY_MODES, null, values);
        db.close();
    }
    //Update PlayMode
    public int update_play_mode(PlayMode playMode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_E_NAME, playMode.getName());
        values.put(KEY_E_DATA, playMode.getData());
        int return_value=db.update(TABLE_EQUALIZER_PLAY_MODES, values, KEY_S_ID + " = ?", new String[]{String.valueOf(playMode.getId())});
        db.close();
        return return_value;
    }
    //Remove PlayMode
    public void remove_play_mode(PlayMode playMode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EQUALIZER_PLAY_MODES, KEY_E_ID + "=?", new String[]{String.valueOf(playMode.getId())});
        db.close();
    }
    //PlayList
    //Get All PlayList
    public ArrayList<PlayList> get_all_playlist(){
        ArrayList<PlayList> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PLAYLIST,null,null,null,null,null,null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new PlayList(cursor.getInt(0), cursor.getString(1)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //Add PlayList
    public void add_playlist(PlayList playList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_P_NAME, playList.getName());
        db.insert(TABLE_PLAYLIST, null, values);
        db.close();
    }
    //Update PlayList
    public int update_playlist(PlayList playList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_E_NAME, playList.getName());
        int return_value=db.update(TABLE_PLAYLIST, values, KEY_P_ID + " = ?", new String[]{String.valueOf(playList.getId())});
        db.close();
        return return_value;
    }
    //Update PlayList Songs
    public void update_playlist_songs(int playlist_id,ArrayList<Song> songs){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST_SONGS, KEY_PS_ID_PLAYLIST + "=?", new String[]{String.valueOf(playlist_id)});
        int i=0;
        for(Song song:songs){
            ContentValues values = new ContentValues();
            values.put(KEY_PS_ID_PLAYLIST, playlist_id);
            values.put(KEY_PS_ID_SONG, song.getId());
            values.put(KEY_PS_POSITION, i);
            db.insert(TABLE_PLAYLIST_SONGS, null, values);
            ++i;
        }
        db.close();
    }
    //Remove PlayList
    public void remove_playlist(int playListId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST_SONGS, KEY_PS_ID_PLAYLIST + "=?", new String[]{String.valueOf(playListId)});
        db.delete(TABLE_PLAYLIST, KEY_P_ID + "=?", new String[]{String.valueOf(playListId)});
        db.close();
    }
    //Song
    //Get Songs
    public ArrayList<Song> get_songs(int type,String artist,String album,String genre,int id_playlist){
        ArrayList<Song> list= new ArrayList<>();
        String where_clause = null;
        String[] where_args = null;
        String order_by = null;
        String limit = null;
        String table_name = TABLE_SONGS;
        String[] columns=null;
        switch (type)
        {
            case Var.TYPE_ALL_SONGS:
                order_by=KEY_S_TITLE+" COLLATE NOCASE ASC, "
                        +KEY_S_ARTIST+" COLLATE NOCASE ASC, "
                        +KEY_S_ALBUM+" COLLATE NOCASE ASC ";
                break;
            case Var.TYPE_FAVORITE_SONGS:
                where_clause=KEY_S_BOOKMARK+"=1";
                order_by=KEY_S_TITLE+" COLLATE NOCASE ASC, "
                        +KEY_S_ARTIST+" COLLATE NOCASE ASC, "
                        +KEY_S_ALBUM+" COLLATE NOCASE ASC ";
                break;
            case Var.TYPE_MOST_PLAYED_SONGS:
                where_clause=KEY_S_SECONDS+">0";
                order_by=KEY_S_SECONDS+" DESC";
                limit="25";
                break;
            case Var.TYPE_CURRENT_PLAYLIST:
                return null;
            case Var.TYPE_PLAYLIST:
                table_name+=" Sa CROSS JOIN "+TABLE_PLAYLIST_SONGS+" Sb";
                where_clause="Sb."+KEY_PS_ID_PLAYLIST+"=? AND Sa."+KEY_S_ID+"=Sb."+KEY_PS_ID_SONG;
                where_args=new String[]{String.valueOf(id_playlist)};
                order_by="Sb."+KEY_PS_POSITION;
                columns=new String[]{
                        "Sa."+KEY_S_ID,
                        "Sa."+KEY_S_PHONE_ID,
                        "Sa."+KEY_S_TITLE,
                        "Sa."+KEY_S_ARTIST,
                        "Sa."+KEY_S_ALBUM,
                        "Sa."+KEY_S_GENRE,
                        "Sa."+KEY_S_ALBUM_ID,
                        "Sa."+KEY_S_PATH,
                        "Sa."+KEY_S_SECONDS,
                        "Sa."+KEY_S_BOOKMARK,
                        "Sa."+KEY_S_LAST_MOD_DATE};
                break;
            case Var.TYPE_ARTIST:
                where_clause=KEY_S_ARTIST+"=?";
                where_args=new String[]{artist};
                order_by=KEY_S_TITLE+" COLLATE NOCASE ASC, "
                        +KEY_S_ALBUM+" COLLATE NOCASE ASC ";
                break;
            case Var.TYPE_GENRE:
                where_clause=KEY_S_GENRE+"=?";
                where_args=new String[]{genre};
                order_by=KEY_S_ARTIST+" COLLATE NOCASE ASC, "
                        +KEY_S_TITLE+" COLLATE NOCASE ASC ";
                break;
            case Var.TYPE_ARTIST_ALBUM:
                where_clause=KEY_S_ARTIST+"=? and "
                            +KEY_S_ALBUM+"=?";
                where_args=new String[]{artist,album};
                order_by=KEY_S_TITLE+" COLLATE NOCASE ASC";
                break;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(table_name,columns,where_clause,where_args,null,null,order_by,limit);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Song(cursor.getInt(0), cursor.getLong(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getLong(6),
                            cursor.getString(7),
                            cursor.getLong(8), cursor.getInt(9), cursor.getString(10)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //Get Song
    public Song get_song_by_id(int id){
        String selectQuery = "SELECT * FROM " + TABLE_SONGS+
                " WHERE "+KEY_S_ID+"=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        Song return_value=null;
        if (cursor.moveToFirst()) {
            return_value= new Song(cursor.getInt(0), cursor.getLong(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getLong(6),
                    cursor.getString(7),
                    cursor.getLong(8), cursor.getInt(9), cursor.getString(10));
        }
        cursor.close();
        db.close();
        return return_value;
    }
    //Get Song by Path
    private Song get_song_by_path(SQLiteDatabase db,String path) {
        String selectQuery = "SELECT * FROM " + TABLE_SONGS+
                " WHERE "+KEY_S_LOWER_PATH+"=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{path});
        Song return_value=null;
        if (cursor.moveToFirst()) {
            return_value= new Song(cursor.getInt(0), cursor.getLong(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getLong(6),
                    cursor.getString(7),
                    cursor.getLong(8), cursor.getInt(9), cursor.getString(10));
        }
        cursor.close();
        return return_value;
    }
    //get Recent Songs
    public ArrayList<Song> get_recent_songs(){
        ArrayList<Song> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String order_by=KEY_S_LAST_MOD_DATE+" DESC";
        Cursor cursor = db.query(TABLE_SONGS,null,KEY_S_LAST_MOD_DATE+" > datetime('now', '-7 days')",null,null,null,order_by);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Song(cursor.getInt(0), cursor.getLong(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getLong(6),
                            cursor.getString(7),
                            cursor.getLong(8), cursor.getInt(9), cursor.getString(10)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    //get Lost Songs
    public ArrayList<Song> get_lost_songs(){
        ArrayList<Song> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String order_by=KEY_S_TITLE+" COLLATE NOCASE ASC, "
                +KEY_S_ARTIST+" COLLATE NOCASE ASC, "
                +KEY_S_ALBUM+" COLLATE NOCASE ASC ";
        Cursor cursor = db.query(TABLE_SONGS,null,null,null,null,null,order_by);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Song(cursor.getInt(0), cursor.getLong(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getLong(6),
                            cursor.getString(7),
                            cursor.getLong(8), cursor.getInt(9), cursor.getString(10)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        Iterator<Song> i = list.iterator();
        while (i.hasNext()) {
            Song s = i.next();
            if(s.isValid())
                i.remove();
        }
        return list;
    }
    //get Lost Songs
    public ArrayList<Song> get_valid_songs(){
        ArrayList<Song> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String order_by=KEY_S_TITLE+" COLLATE NOCASE ASC, "
                +KEY_S_ARTIST+" COLLATE NOCASE ASC, "
                +KEY_S_ALBUM+" COLLATE NOCASE ASC ";
        Cursor cursor = db.query(TABLE_SONGS,null,null,null,null,null,order_by);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Song(cursor.getInt(0), cursor.getLong(1),
                            cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5),cursor.getLong(6),
                            cursor.getString(7),
                            cursor.getLong(8), cursor.getInt(9), cursor.getString(10)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        Iterator<Song> i = list.iterator();
        while (i.hasNext()) {
            Song s = i.next();
            if(!s.isValid())
                i.remove();
        }
        return list;
    }
    private ContentValues add_song_values=new ContentValues();
    //Add Song Public
    public void add_song_public(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        add_song_values.put(KEY_S_PHONE_ID, song.getPhone_id());
        add_song_values.put(KEY_S_TITLE, song.getTitle());
        add_song_values.put(KEY_S_ARTIST, song.getArtist());
        add_song_values.put(KEY_S_ALBUM, song.getAlbum());
        add_song_values.put(KEY_S_GENRE, song.getGenre());
        add_song_values.put(KEY_S_ALBUM_ID, song.getAlbum_id());
        add_song_values.put(KEY_S_PATH, song.getData());
        add_song_values.put(KEY_S_LOWER_PATH, song.getData().toLowerCase());
        add_song_values.put(KEY_S_LAST_MOD_DATE, song.get_last_mod_date());
        add_song_values.put(KEY_S_SECONDS, song.getSeconds());
        add_song_values.put(KEY_S_BOOKMARK, song.getBookmark());
        db.insert(TABLE_SONGS, null, add_song_values);
        db.close();
    }
    //Add Song
    private void add_song(SQLiteDatabase db,Song song) {
        add_song_values.put(KEY_S_PHONE_ID, song.getPhone_id());
        add_song_values.put(KEY_S_TITLE, song.getTitle());
        add_song_values.put(KEY_S_ARTIST, song.getArtist());
        add_song_values.put(KEY_S_ALBUM, song.getAlbum());
        add_song_values.put(KEY_S_GENRE, song.getGenre());
        add_song_values.put(KEY_S_ALBUM_ID, song.getAlbum_id());
        add_song_values.put(KEY_S_PATH, song.getData());
        add_song_values.put(KEY_S_LOWER_PATH, song.getData().toLowerCase());
        add_song_values.put(KEY_S_LAST_MOD_DATE, song.get_last_mod_date());
        add_song_values.put(KEY_S_SECONDS, song.getSeconds());
        add_song_values.put(KEY_S_BOOKMARK, song.getBookmark());
        db.insert(TABLE_SONGS, null, add_song_values);
    }

    //Update Song
    public int update_song(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_S_SECONDS, song.getSeconds());
        values.put(KEY_S_BOOKMARK, song.getBookmark());
        int return_value=db.update(TABLE_SONGS, values, KEY_S_ID + " = ?", new String[]{String.valueOf(song.getId())});
        db.close();
        return return_value;
    }
    //Update Song
    private int update_song_db(SQLiteDatabase db, Song song) {
        add_song_values.put(KEY_S_PHONE_ID, song.getPhone_id());
        add_song_values.put(KEY_S_TITLE, song.getTitle());
        add_song_values.put(KEY_S_ARTIST, song.getArtist());
        add_song_values.put(KEY_S_ALBUM, song.getAlbum());
        add_song_values.put(KEY_S_GENRE, song.getGenre());
        add_song_values.put(KEY_S_ALBUM_ID, song.getAlbum_id());
        add_song_values.put(KEY_S_PATH, song.getData());
        add_song_values.put(KEY_S_LOWER_PATH, song.getData().toLowerCase());
        add_song_values.put(KEY_S_LAST_MOD_DATE, song.get_last_mod_date());
        add_song_values.put(KEY_S_SECONDS, song.getSeconds());
        add_song_values.put(KEY_S_BOOKMARK, song.getBookmark());
        return db.update(TABLE_SONGS, add_song_values, KEY_S_ID + " = ?", new String[]{String.valueOf(song.getId())});
    }
    // Update Songs
    public void update_songs()
    {
        long last_phone_id=get_last_phone_id();
        ContentResolver musicResolver = db_context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND "
                + MediaStore.Audio.Media._ID + "> "+String.valueOf(last_phone_id);
        String sortOrder = MediaStore.Audio.Media._ID + " ASC";
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATA};
        String[] genresProjection = {
                MediaStore.Audio.Genres.NAME,
                MediaStore.Audio.Genres._ID
        };
        Cursor musicCursor = musicResolver.query(musicUri,projection, selection, null, sortOrder);
        int new_songs=0;
        SQLiteDatabase db=this.getWritableDatabase();
        Song new_song=new Song();
        if(musicCursor!=null) {
            if (musicCursor.moveToFirst()){
                //get columns
                int idColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media._ID);
                int titleColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.TITLE);
                int artistColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM);
                int albumIdColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);
                int dataColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.DATA);
                ArrayList<Song> updated_songs=new ArrayList<>();
                //add songs to list
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    if(thisId>last_phone_id)
                        last_phone_id=thisId;
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String thisAlbum = musicCursor.getString(albumColumn);
                    long thisAlbumId = musicCursor.getLong(albumIdColumn);
                    String thisData = musicCursor.getString(dataColumn);
                    String thisGenre = "";
                    {
                        Uri uri = MediaStore.Audio.Genres.getContentUriForAudioId("external", (int) thisId);
                        Cursor genresCursor = db_context.getContentResolver().query(uri, genresProjection, null, null, null);
                        if(genresCursor!=null) {
                            if (genresCursor.moveToFirst()) {
                                int genre_column_index = genresCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                                thisGenre="";
                                do {
                                    String new_genre=genresCursor.getString(genre_column_index);
                                    if(new_genre.length()>0)
                                        thisGenre +=  new_genre+ " ";
                                } while (genresCursor.moveToNext());
                            }
                            genresCursor.close();
                        }
                    }
                    Song song=get_song_by_path(db,thisData.toLowerCase());
                    if(song != null) {
                        song.setPhone_id(thisId);
                        song.setTitle(thisTitle);
                        song.setArtist(thisArtist);
                        song.setAlbum(thisAlbum);
                        song.setGenre(thisGenre);
                        song.setAlbum_id(thisAlbumId);
                        song.set_mod_date(Fun.getInstance().date_to_string(Calendar.getInstance()));
                        if(!song.getHasArtist()){
                            song.setArtist(Var.UNKNOWN_ARTIST);
                        }
                        if(!song.getHasAlbum()){
                            song.setAlbum(Var.UNKNOWN_ALBUM);
                        }
                        if(!song.getHasGenre()){
                            song.setGenre(Var.UNKNOWN_GENRE);
                        }
                        updated_songs.add(song);
                    }
                    else{
                        new_songs++;
                        new_song.set_all_data(thisId, thisTitle, thisArtist, thisAlbum, thisGenre, thisAlbumId, thisData);
                        if(!new_song.getHasArtist()){
                            new_song.setArtist(Var.UNKNOWN_ARTIST);
                        }
                        if(!new_song.getHasAlbum()){
                            new_song.setAlbum(Var.UNKNOWN_ALBUM);
                        }
                        if(!new_song.getHasGenre()){
                            new_song.setGenre(Var.UNKNOWN_GENRE);
                        }
                        add_song(db, new_song);
                    }
                }
                while (musicCursor.moveToNext());
                for(Song song_data:updated_songs) {
                    update_song_db(db,song_data);
                    for(Song song:PlayerVar.getInstance().song_list){
                        if(song.getId()==song_data.getId()){
                            song.setPhone_id(song_data.getPhone_id());
                            song.setTitle(song_data.getTitle());
                            song.setArtist(song_data.getArtist());
                            song.setAlbum(song_data.getAlbum());
                            song.setGenre(song_data.getGenre());
                            song.setAlbum_id(song_data.getAlbum_id());
                            song.set_mod_date(song_data.get_last_mod_date());
                        }
                    }
                }
                if(new_songs>0) {
                    Toast.makeText(db_context, Language.getInstance().newSongs + ": " + String.valueOf(new_songs), Toast.LENGTH_SHORT).show();
                }
                if(updated_songs.size()>0) {
                    Fun.savePlayerVar(db_context);
                    Toast.makeText(db_context, Language.getInstance().updatedSongs+": "+String.valueOf(updated_songs.size()), Toast.LENGTH_SHORT).show();
                }
            }
            musicCursor.close();
        }
        update_last_phone_id(db, last_phone_id);
        db.close();
    }
    //Replace Song References
    public void replace_song_references(Song lost_song,Song new_song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PS_ID_SONG, new_song.getId());
        db.update(TABLE_PLAYLIST_SONGS, values, KEY_PS_ID_SONG + " = ?", new String[]{String.valueOf(lost_song.getId())});
        db.close();
        remove_song_references(lost_song);
    }
    //Remove Song
    public void remove_song_references(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST_SONGS, KEY_PS_ID_SONG + "=?", new String[]{String.valueOf(song.getId())});
        db.delete(TABLE_SONGS, KEY_S_ID + "=?", new String[]{String.valueOf(song.getId())});
        if(PlayerVar.getInstance().song_list!=null) {
            for (Song s : PlayerVar.getInstance().song_list) {
                if(s.getId()==song.getId()){
                    s.setPlayable(false);
                    Fun.savePlayerVar(db_context);
                    break;
                }
            }
        }
        db.close();
    }
    //Other
    public ArrayList<Artist> get_artists(){
        ArrayList<Artist> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String order_by= KEY_S_ARTIST+" COLLATE NOCASE ASC, "
                +KEY_S_ALBUM+" COLLATE NOCASE ASC ";
        String group_by=KEY_S_ARTIST+","+KEY_S_ALBUM;
        Cursor cursor = db.query(TABLE_SONGS,new String[]{KEY_S_ARTIST,KEY_S_ALBUM,"MAX("+KEY_S_ALBUM_ID+")"},null,null,group_by,null,order_by);
        String last_artist="";
        ArrayList<Album> albums=new ArrayList<>();
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    String new_artist=cursor.getString(0);
                    if(!new_artist.equals(last_artist)){
                        if(albums.size()>0){
                            list.add(new Artist(last_artist,albums));
                            albums=new ArrayList<>();
                        }
                        last_artist=new_artist;
                    }
                    albums.add(new Album(cursor.getLong(2),cursor.getString(1),last_artist));
                } while (cursor.moveToNext());
                if(albums.size()>0){
                    list.add(new Artist(last_artist,albums));
                }
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public ArrayList<Album> get_albums(){
        ArrayList<Album> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String order_by= KEY_S_ALBUM+" COLLATE NOCASE ASC, "
                +KEY_S_ARTIST+" COLLATE NOCASE ASC ";
        String group_by=KEY_S_ALBUM+","+KEY_S_ARTIST;
        Cursor cursor = db.query(TABLE_SONGS,new String[]{KEY_S_ALBUM,KEY_S_ARTIST,"MAX("+KEY_S_ALBUM_ID+")"},null,null,group_by,null,order_by);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Album(cursor.getLong(2),cursor.getString(0),cursor.getString(1)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public ArrayList<Genre> get_genres(){
        ArrayList<Genre> list= new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String order_by= KEY_S_GENRE+" COLLATE NOCASE ASC ";
        String group_by=KEY_S_GENRE;
        Cursor cursor = db.query(TABLE_SONGS,new String[]{KEY_S_GENRE,"COUNT("+KEY_S_ID+")"},null,null,group_by,null,order_by);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(new Genre(cursor.getString(0),cursor.getInt(1)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public void restart_all_tables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONFIGURATION);
        String CREATE_CONFIGURATION_TABLE= "CREATE TABLE "+ TABLE_CONFIGURATION + "("
                + KEY_C_SELECTED_LANGUAGE + " INTEGER,"
                + KEY_C_SHUFFLE_OPTION + " INTEGER,"
                + KEY_C_REPEAT_OPTION + " INTEGER,"
                + KEY_C_AUTO_STOP + " INTEGER,"
                + KEY_C_LAST_PHONE_ID+ " INTEGER,"
                + KEY_C_PRIMARY_TEXT_COLOR + " INTEGER,"
                + KEY_C_SECONDARY_TEXT_COLOR + " INTEGER,"
                + KEY_C_PRIMARY_BACKGROUND_COLOR + " INTEGER,"
                + KEY_C_SECONDARY_BACKGROUND_COLOR + " INTEGER,"
                + KEY_C_MAIN_P_TEXT_COLOR + " INTEGER,"
                + KEY_C_MAIN_S_TEXT_COLOR + " INTEGER)";
        db.execSQL(CREATE_CONFIGURATION_TABLE);
        String INSERT_CONFIGURATION_OPTION_DEFAULT="INSERT INTO "+TABLE_CONFIGURATION +" VALUES ("
                + String.valueOf(Language.CODE_ES)+","
                + String.valueOf(0)+","
                + String.valueOf(Var.REPEAT_TYPE_NO_REPEAT)+","
                + String.valueOf(1)+","
                + String.valueOf(-1)+","
                + String.valueOf(Color.parseColor("#EEEEEE"))+","
                + String.valueOf(Color.parseColor("#555555"))+","
                + String.valueOf(Color.parseColor("#D97700"))+","
                + String.valueOf(Color.parseColor("#DDDDDD"))+","
                + String.valueOf(Color.parseColor("#000000"))+","
                + String.valueOf(Color.parseColor("#888888"))+")";
        db.execSQL(INSERT_CONFIGURATION_OPTION_DEFAULT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SONGS);
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + " ("
                + KEY_S_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_S_PHONE_ID  + " INTEGER,"
                + KEY_S_TITLE + " TEXT,"
                + KEY_S_ARTIST + " TEXT,"
                + KEY_S_ALBUM + " TEXT,"
                + KEY_S_GENRE + " TEXT,"
                + KEY_S_ALBUM_ID + " INTEGER,"
                + KEY_S_PATH + " TEXT,"
                + KEY_S_SECONDS + " INTEGER,"
                + KEY_S_BOOKMARK + " INTEGER,"
                + KEY_S_LAST_MOD_DATE + " DATETIME,"
                + KEY_S_LOWER_PATH + " TEXT)";
        db.execSQL(CREATE_SONGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EQUALIZER_PLAY_MODES);
        String CREATE_EQUALIZER_PLAY_MODES_TABLE = "CREATE TABLE " + TABLE_EQUALIZER_PLAY_MODES+ " ("
                + KEY_E_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_E_NAME + " TEXT,"
                + KEY_E_DATA + " TEXT)";
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLAYLIST);
        db.execSQL(CREATE_EQUALIZER_PLAY_MODES_TABLE);
        String CREATE_PLAY_LISTS_TABLE = "CREATE TABLE " + TABLE_PLAYLIST+ " ("
                + KEY_P_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_P_NAME+ " TEXT)";
        db.execSQL(CREATE_PLAY_LISTS_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PLAYLIST_SONGS);
        String CREATE_PLAY_LISTS_SONGS = "CREATE TABLE " + TABLE_PLAYLIST_SONGS+ " ("
                + KEY_PS_ID_PLAYLIST+ " INTEGER,"
                + KEY_PS_ID_SONG + " INTEGER,"
                + KEY_PS_POSITION + " INTEGER)";
        db.execSQL(CREATE_PLAY_LISTS_SONGS);
        db.execSQL("ALTER TABLE "+TABLE_CONFIGURATION+" ADD COLUMN "+KEY_C_AUTO_REPLAY+" INTEGER DEFAULT 1");
        db.close();
    }
}
