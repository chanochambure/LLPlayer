package lexrislogic.llplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Models.PlayList;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Singleton.PlayerVar;
import lexrislogic.llplayer.Singleton.Var;
import lexrislogic.llplayer.Singleton.Language;

public class TabFragment1 extends Fragment {
    public final static int REQUEST_CODE_SEE_SONG_LIST = 1;
    public final static int REQUEST_CODE_MODIFY_PLAYLIST= 2;
    public SimpleElementAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement current_playlist=new SimpleElement(
            Var.TYPE_CURRENT_PLAYLIST,
            R.mipmap.current_playlist_icon,
            Language.getInstance().CurrentPlayList,
            Language.getInstance().CurrentPlayListSubText,
            -1);
    public SimpleElement all_songs=new SimpleElement(
            Var.TYPE_ALL_SONGS,
            R.mipmap.all_songs_icon,
            Language.getInstance().AllSongs,
            "",
            -1);
    public SimpleElement favorite_songs=new SimpleElement(
            Var.TYPE_FAVORITE_SONGS,
            R.mipmap.favorites_option_icon,
            Language.getInstance().Favorites,
            Language.getInstance().FavoritesSubText,
            -1);
    public SimpleElement most_played_songs=new SimpleElement(
            Var.TYPE_MOST_PLAYED_SONGS,
            R.mipmap.most_played_songs_icon,
            Language.getInstance().MostPlayedSongs,
            Language.getInstance().MostPlayedSongsSubText,
            -1);
    private Dialog dialog=null;
    private Dialog dialog2=null;
    private AlertDialog alertDialog=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        if(view!=null) {
            element_list=new ArrayList<>();

            DBHandler db=new DBHandler(getContext());
            view.setBackgroundColor(db.get_secondary_background_color());

            adapter=new SimpleElementAdapter(getContext(),element_list,
                    db.get_main_primary_text_color(),
                    db.get_main_secondary_text_color());

            ListView listView= (ListView) view.findViewById(R.id.playLists_ListView);
            listView.setFastScrollEnabled(true);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(getContext(), SongList.class);
                    if (selected_item.get_type() == Var.TYPE_CURRENT_PLAYLIST) {
                        intent.putExtra("title", PlayerVar.getInstance().lastTitle);
                    } else {
                        intent.putExtra("title", selected_item.get_main_text());
                    }
                    intent.putExtra("type", selected_item.get_type());
                    intent.putExtra("metadata", selected_item.getMetadata());
                    startActivityForResult(intent, REQUEST_CODE_SEE_SONG_LIST);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final SimpleElement selected_item = (SimpleElement) parent.getItemAtPosition(position);
                    if (selected_item.get_type() == Var.TYPE_PLAYLIST) {
                        DBHandler db=new DBHandler(getContext());
                        dialog = new Dialog(getContext());
                        dialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                        dialog.setContentView(R.layout.options_layout);
                        dialog.setTitle(Language.getInstance().lostOptionsDialogTittle);
                        RelativeLayout backgroundDialog = (RelativeLayout) dialog.findViewById(R.id.backgroundDialog);
                        backgroundDialog.setBackgroundColor(db.get_secondary_background_color());
                        ListView listView = (ListView) dialog.findViewById(R.id.listOptionsView);
                        ArrayList<SimpleElement> another_element_list=new ArrayList<>();
                        another_element_list.add(new SimpleElement(
                                Var.TYPE_PLAYLIST_RENAME,
                                R.mipmap.rename_playlist_icon,
                                Language.getInstance().RenPlayListText,
                                Language.getInstance().RenPlayListSubText,
                                -1));
                        another_element_list.add(new SimpleElement(
                                Var.TYPE_PLAYLIST_MODIFY,
                                R.mipmap.mod_playlist_icon,
                                Language.getInstance().ModPlayListText,
                                Language.getInstance().ModPlayListSubText,
                                -1));
                        another_element_list.add(new SimpleElement(
                                Var.TYPE_PLAYLIST_REMOVE,
                                R.mipmap.remove_playlist_icon,
                                Language.getInstance().RemPlayListText,
                                Language.getInstance().RemPlayListSubText,
                                -1));
                        SimpleElementAdapter adapter=new SimpleElementAdapter(getContext(),another_element_list,
                                db.get_main_primary_text_color(),
                                db.get_main_secondary_text_color());
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                SimpleElement s_selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                                switch (s_selected_item.get_type()){
                                    case Var.TYPE_PLAYLIST_RENAME:
                                        rename_playlist_dialog(selected_item.getMetadata(),selected_item.get_main_text());
                                        break;
                                    case Var.TYPE_PLAYLIST_MODIFY:
                                        modify_playlist(selected_item.getMetadata(),selected_item.get_main_text());
                                        break;
                                    case Var.TYPE_PLAYLIST_REMOVE:
                                        remove_playlist_dialog(selected_item.getMetadata(),selected_item.get_main_text());
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    return true;
                }
            });
            load_element_list();
        }
        return view;
    }
    public void load_element_list(){
        element_list.clear();
        element_list.add(current_playlist);
        element_list.add(all_songs);
        element_list.add(favorite_songs);
        element_list.add(most_played_songs);
        DBHandler db=new DBHandler(getContext());
        ArrayList<PlayList> playLists=db.get_all_playlist();
        for(PlayList playList:playLists){
            element_list.add(new SimpleElement(Var.TYPE_PLAYLIST,R.mipmap.play_list_icon,playList.getName(),"",playList.getId()));
        }
        adapter.notifyDataSetChanged();
    }
    private void modify_playlist(int playlist_id,String name){
        DBHandler db=new DBHandler(getContext());
        Var.getInstance().playListSongs=db.get_songs(Var.TYPE_PLAYLIST,null,null,null,playlist_id);
        db.close();
        Intent intent = new Intent(getContext(), ModifyPlayList.class);
        intent.putExtra("title", name);
        intent.putExtra("playlist_id", playlist_id);
        startActivityForResult(intent, REQUEST_CODE_MODIFY_PLAYLIST);
    }
    private void rename_playlist_dialog(final int playlist_id,final String name) {
        DBHandler db = new DBHandler(getContext());
        dialog2 = new Dialog(getContext());
        dialog2.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        dialog2.setContentView(R.layout.create_playlist_dialog);
        dialog2.setTitle(Language.getInstance().RenPlayListText);
        RelativeLayout backgroundDialog = (RelativeLayout) dialog2.findViewById(R.id.backgroundDialog);
        backgroundDialog.setBackgroundColor(db.get_secondary_background_color());
        final EditText playListName=(EditText) dialog2.findViewById(R.id.namePlayList);
        playListName.setTextColor(db.get_main_primary_text_color());
        int new_color=db.get_primary_background_color();
        playListName.setBackgroundColor(Color.argb(
                48,
                Color.red(new_color),
                Color.green(new_color),
                Color.blue(new_color)));
        playListName.setText(name);
        playListName.getBackground().setColorFilter(db.get_main_primary_text_color(), PorterDuff.Mode.SRC_ATOP);
        playListName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                playListName.post(new Runnable() {
                    @Override
                    public void run() {
                        playListName.setSelected(true);
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(playListName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        playListName.requestFocus();
        Button applyChanges=(Button) dialog2.findViewById(R.id.createPlatlist);
        applyChanges.setBackgroundColor(db.get_primary_background_color());
        applyChanges.setTextColor(db.get_primary_text_color());
        applyChanges.setText(Language.getInstance().ModPlayModeText);
        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_play_list_name = playListName.getText().toString();
                if (new_play_list_name.length() > 0) {
                    DBHandler db = new DBHandler(getContext());
                    db.update_playlist(new PlayList(playlist_id, new_play_list_name));
                    load_element_list();
                    Toast.makeText(getContext(),
                            name + " - " + Language.getInstance().modPlayList,
                            Toast.LENGTH_SHORT).show();
                    db.close();
                    dialog2.dismiss();
                } else {
                    Toast.makeText(getContext(),
                            Language.getInstance().invalidNamePlayList,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog2.show();
    }
    private void remove_playlist_dialog(final int playlist_id,final String name){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(Language.getInstance().removePlayList + ": " + name);
        alertDialogBuilder.setPositiveButton((Language.getInstance().acceptDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DBHandler db = new DBHandler(getContext());
                        db.remove_playlist(playlist_id);
                        Toast.makeText(getContext(),
                                name + " - " + Language.getInstance().messageDeleted,
                                Toast.LENGTH_SHORT).show();
                        db.close();
                        load_element_list();
                    }
                });
        alertDialogBuilder.setNegativeButton((Language.getInstance().cancelDialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        alertDialog.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_SEE_SONG_LIST:
                break;
            case REQUEST_CODE_MODIFY_PLAYLIST:
                break;
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
        if(dialog2!=null && dialog2.isShowing())
            dialog2.dismiss();
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
    }
    @Override
    public void onDestroy(){
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
        if(dialog2!=null && dialog2.isShowing())
            dialog2.dismiss();
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
        super.onDestroy();
    }
}