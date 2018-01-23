package lexrislogic.llplayer.Singleton;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Models.PlayList;
import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.R;

public class Fun {
    public static Drawable get_album_art(Context context,long albumID){
        Drawable data=null;
        if(Var.getInstance().allAlbumArts.containsKey(albumID)){
            data=Var.getInstance().allAlbumArts.get(albumID);
        } else {
            ContentResolver musicResolver = context.getContentResolver();
            Uri musicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media._ID + "=" + String.valueOf(albumID);
            Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, null);
            if (musicCursor != null) {
                if (musicCursor.moveToFirst()) {
                    int albumColumn = musicCursor.getColumnIndex
                            (MediaStore.Audio.Albums.ALBUM_ART);
                    String thisAlbum = musicCursor.getString(albumColumn);
                    try {
                        data = Drawable.createFromPath(thisAlbum);
                        Var.getInstance().allAlbumArts.put(albumID,data);
                    } catch (OutOfMemoryError exception){
                        exception.printStackTrace();
                        data=null;
                        Var.getInstance().allAlbumArts.clear();
                    }
                }
                musicCursor.close();
            }
        }
        return data;
    }

    public static String to_string_time_format(int ms) {
        int seconds = ms/1000;
        int minutes = seconds/60;
        seconds = seconds%60;
        String hours_string="";
        String minutes_string=String.valueOf(minutes);
        String seconds_string=String.valueOf(seconds);
        if(seconds<10)
            seconds_string="0"+seconds_string;
        if(minutes>=60){
            hours_string=String.valueOf(minutes/60)+":";
            minutes=minutes%60;
            minutes_string=String.valueOf(minutes);
            if(minutes<10)
                minutes_string="0"+minutes_string;
        }
        return hours_string+minutes_string+":"+seconds_string;
    }
    public static void refresh_notification_data(Context context){
        if(Var.getInstance().notification_bitmap==null) {
            Resources res = context.getResources();
            Var.getInstance().notification_height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
            Var.getInstance().notification_width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
            Var.getInstance().notification_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res,
                            R.mipmap.no_album),
                    Var.getInstance().notification_width,
                    Var.getInstance().notification_height,
                    false);
        }
        if(Var.getInstance().album_notification_bitmap==null) {
            Resources res = context.getResources();
            Var.getInstance().notification_height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
            Var.getInstance().notification_width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
            Var.getInstance().album_notification_bitmap = Bitmap.createBitmap(
                    Var.getInstance().notification_width,
                    Var.getInstance().notification_height,
                    Bitmap.Config.ARGB_8888);
            Var.getInstance().notification_canvas = new Canvas(Var.getInstance().album_notification_bitmap);
        }
    }
    public static void refresh_notification_album_art(Drawable drawable){
        if(Var.getInstance().notification_canvas!=null && drawable!=null){
            Var.getInstance().notification_canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
            drawable.setBounds(
                    0,
                    0,
                    Var.getInstance().notification_canvas.getWidth(),
                    Var.getInstance().notification_canvas.getHeight());
            drawable.draw(Var.getInstance().notification_canvas);
        }
    }
    public static boolean refresh_album_art(byte[] data,int height,int width){
        if (Var.getInstance().album_bitmap == null && height>0 && width>0) {
            Var.getInstance().album_bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888);
            Var.getInstance().album_canvas = new Canvas(Var.getInstance().album_bitmap);
        }
        if(Var.getInstance().album_canvas!=null){
            if(height==0 || width==0){
                width=Var.getInstance().lastWidthImageView;
                height=Var.getInstance().lastHeightImageView;
            }
            Var.getInstance().lastHeightImageView=height;
            Var.getInstance().lastWidthImageView=width;
            if(Var.getInstance().album_temp!=null) {
                Var.getInstance().album_temp.recycle();
                Var.getInstance().album_temp=null;
            }
            Var.getInstance().album_temp = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(Var.getInstance().album_temp==null)
                return false;
            Var.getInstance().album_canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
            int sourceWidth = Var.getInstance().album_temp.getWidth();
            int sourceHeight = Var.getInstance().album_temp.getHeight();
            if(sourceHeight>0 && sourceWidth>0) {
                float scale = Math.min((float) width / sourceWidth, (float) height / sourceHeight);
                float scaledWidth = scale * sourceWidth;
                float scaledHeight = scale * sourceHeight;
                float left = (width - scaledWidth) / 2;
                float top = (height - scaledHeight) / 2;
                RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
                Var.getInstance().album_canvas.drawBitmap(
                        Var.getInstance().album_temp, null, targetRect, null);
            }
        }
        return true;
    }
    public static void validateIndex(){
        int playlist_index = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getPlaylist_id();
        while(playlist_index!=PlayerVar.getInstance().index){
            PlayerVar.getInstance().last_position_saved=-1;
            if (playlist_index >= PlayerVar.getInstance().song_list.size()){
                PlayerVar.getInstance().finishList=true;
                playlist_index = 0;
            }
            PlayerVar.getInstance().index=playlist_index;
            playlist_index = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getPlaylist_id();
        }
    }
    public static void validateBackIndex(){
        int playlist_index = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getPlaylist_id();
        while(playlist_index!=PlayerVar.getInstance().index){
            PlayerVar.getInstance().last_position_saved=-1;
            PlayerVar.getInstance().index--;
            if(PlayerVar.getInstance().index<0)
                PlayerVar.getInstance().index=PlayerVar.getInstance().song_list.size()-1;
            playlist_index = PlayerVar.getInstance().song_list.get(PlayerVar.getInstance().index).getPlaylist_id();
        }
    }

    public static void refreshSongListIndex(ArrayList<Song> list){
        int counter=list.size();
        for(int i=list.size()-1;i>=0;--i){
            if(list.get(i).isValid())
                counter=i;
            list.get(i).setPlaylist_id(counter);
        }
        PlayerVar.getInstance().validList=counter!=list.size();
    }

    public static void refreshPlayerVar(Context context){
        FileInputStream inStream = null;
        ObjectInputStream objectInputStream = null;
        File directory=context.getExternalFilesDir("USER");
        try {
            inStream = new FileInputStream(directory+"/"+PlayerVar.PLAYER_VAR_FILENAME_SONG_LIST);
            objectInputStream = new ObjectInputStream(inStream);
            PlayerVar.getInstance().song_list=(ArrayList<Song>)objectInputStream.readObject();
            objectInputStream.close();
            inStream.close();
            inStream = new FileInputStream(directory+"/"+PlayerVar.PLAYER_VAR_FILENAME_DATA);
            objectInputStream = new ObjectInputStream(inStream);
            PlayerVar.getInstance().last_position_saved=objectInputStream.readInt();
            PlayerVar.getInstance().index=objectInputStream.readInt();
            PlayerVar.getInstance().play_mode_index=objectInputStream.readInt();
            PlayerVar.getInstance().lastTitle=(String)objectInputStream.readObject();
            objectInputStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            PlayerVar.getInstance().song_list.clear();
            PlayerVar.getInstance().index=-1;
            PlayerVar.getInstance().last_position_saved=-1;
            PlayerVar.getInstance().play_mode_index=1;
            PlayerVar.getInstance().lastTitle=null;
        }
        finally {
            try {
                if (objectInputStream!= null)
                    objectInputStream.close();
                if (inStream != null)
                    inStream.close();
            }
            catch (Exception ignored) {
            }
        }
        if(PlayerVar.getInstance().song_list.size()>0)
            Fun.refreshSongListIndex(PlayerVar.getInstance().song_list);
        else {
            PlayerVar.getInstance().index=-1;
            PlayerVar.getInstance().validList = false;
        }
    }
    public static void savePlayerVar(Context context){
        FileOutputStream outStream = null;
        ObjectOutputStream objectOutStream = null;
        File directory=context.getExternalFilesDir("USER");
        try {
            outStream = new FileOutputStream(directory+"/"+PlayerVar.PLAYER_VAR_FILENAME_DATA);
            objectOutStream = new ObjectOutputStream(outStream);
            if (PlayerVar.getInstance().mediaPlayer!=null) {
                objectOutStream.writeInt(PlayerVar.getInstance().mediaPlayer.getCurrentPosition());
            } else{
                objectOutStream.writeInt(PlayerVar.getInstance().last_position_saved);
            }
            objectOutStream.writeInt(PlayerVar.getInstance().index);
            objectOutStream.writeInt(PlayerVar.getInstance().play_mode_index);
            objectOutStream.writeObject(PlayerVar.getInstance().lastTitle);
            objectOutStream.close();
            outStream.close();
            outStream = new FileOutputStream(directory+"/"+PlayerVar.PLAYER_VAR_FILENAME_SONG_LIST);
            objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeObject(PlayerVar.getInstance().song_list);
            objectOutStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (objectOutStream != null)
                    objectOutStream.close();
                if (outStream != null)
                    outStream.close();
            }
            catch (Exception ignored) {
            }
        }
    }

    private static Fun instance = new Fun( );
    private Fun() {
        String _V_format="yyyy-MM-dd HH:mm:ss";
        date_format= new SimpleDateFormat(_V_format, Locale.getDefault());
    }
    public static Fun getInstance() {
        return instance;
    }
    private SimpleDateFormat date_format= null;
    public String date_to_string(Calendar date) {
        return this.date_format.format(date.getTime());
    }
    public Calendar string_to_date(String date) {
        try {
            Calendar temp_calendar= Calendar.getInstance();
            temp_calendar.setTime(this.date_format.parse(date));
            return temp_calendar;
        } catch (ParseException e) {
            return null;
        }
    }
    public static String save_backup(DBHandler db){
        String return_value=null;
        FileOutputStream outStream = null;
        ObjectOutputStream objectOutStream = null;
        String directory= Environment.getExternalStorageDirectory().toString();
        try {
            File file=new File(directory,PlayerVar.PLAYER_VAR_FILENAME_BACKUP);
            outStream = new FileOutputStream(file);
            objectOutStream = new ObjectOutputStream(outStream);
            objectOutStream.writeInt(db.get_language());
            objectOutStream.writeBoolean(db.get_shuffle_option());
            objectOutStream.writeInt(db.get_repeat_option());
            objectOutStream.writeBoolean(db.get_auto_stop_option());

            objectOutStream.writeInt(db.get_primary_background_color());
            objectOutStream.writeInt(db.get_secondary_background_color());
            objectOutStream.writeInt(db.get_primary_text_color());
            objectOutStream.writeInt(db.get_secondary_text_color());
            objectOutStream.writeInt(db.get_main_primary_text_color());
            objectOutStream.writeInt(db.get_main_secondary_text_color());
            objectOutStream.writeObject(db.get_play_modes());
            ArrayList<PlayList> playlists=db.get_all_playlist();
            objectOutStream.writeObject(db.get_songs(Var.TYPE_ALL_SONGS, null, null, null, -1));
            objectOutStream.writeObject(playlists);
            for(PlayList playList:playlists){
                objectOutStream.writeObject(db.get_songs(Var.TYPE_PLAYLIST,null,null,null,playList.getId()));
            }
            objectOutStream.close();
            outStream.close();
            return_value=file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return_value=null;
        }
        finally {
            try {
                if (objectOutStream != null)
                    objectOutStream.close();
                if (outStream != null)
                    outStream.close();
            }
            catch (Exception e) {
                return_value=null;
            }
        }
        return return_value;
    }
    public static ArrayList<String> getStorages() {
        File storage_dir=new File("/storage/");
        ArrayList<String> data=new ArrayList<>();
        data.add(Language.getInstance().anyPath);
        for(File file:storage_dir.listFiles())
            data.add(file.toString());
        return data;
    }
}
