package lexrislogic.llplayer.Android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lexrislogic.llplayer.EqualizerList;
import lexrislogic.llplayer.MediaPlayerService;
import lexrislogic.llplayer.Models.PlayMode;
import lexrislogic.llplayer.PlayModeEqualizerAdjust;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.PlayerVar;

public class PlayModeElementAdapter extends ArrayAdapter<PlayMode> {
    private int main_color=0;
    private int button_color=0;
    private Context context=null;
    private AlertDialog alertDialog=null;
    public PlayModeElementAdapter(Context context, ArrayList<PlayMode> data,int new_main_color,int new_button_color) {
        super(context, 0, data);
        main_color=new_main_color;
        button_color=new_button_color;
        this.context=context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PlayMode item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.play_mode_element, parent, false);
        }
        TextView radioButton = (TextView) convertView.findViewById(R.id.equalizerOption);
        radioButton.setText(item.getName());
        radioButton.setTextColor(main_color);
        View space = convertView.findViewById(R.id.elementSpace);
        if(item.getId()== PlayerVar.getInstance().play_mode_index){
            space.setBackgroundColor(button_color);
        }
        else{
            space.setBackgroundColor(Color.TRANSPARENT);
        }
        ImageButton btnConfig = (ImageButton) convertView.findViewById(R.id.modEqualizer);
        btnConfig.setFocusable(false);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getId() == 1) {
                    Toast.makeText(getContext(), Language.getInstance().cantModEq, Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getContext(), PlayModeEqualizerAdjust.class);
                    intent.putExtra("id_play_mode", item.getId());
                    intent.putExtra("title_play_mode", item.getName());
                    ((Activity)context).startActivityForResult(intent, EqualizerList.REQUEST_CODE_ADD_EQUALIZER);
                }
            }
        });
        btnConfig.setImageResource(R.mipmap.mod_icon);
        btnConfig.setBackgroundColor(Color.TRANSPARENT);

        ImageButton btnDelete = (ImageButton) convertView.findViewById(R.id.delEqualizer);
        btnDelete.setFocusable(false);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getId() == 1) {
                    Toast.makeText(getContext(), Language.getInstance().cantDelEq, Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(Language.getInstance().removePlayModeEq + ": " + item.getName());
                    alertDialogBuilder.setPositiveButton((Language.getInstance().acceptDialog),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    DBHandler db = new DBHandler(getContext());
                                    db.remove_play_mode(item);
                                    PlayerVar.getInstance().playModes.remove(position);
                                    if (item.getId() == PlayerVar.getInstance().play_mode_index) {
                                        PlayerVar.getInstance().play_mode_index = 1;
                                        Intent eqIntent = new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
                                        eqIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_EQUALIZER);
                                        context.sendBroadcast(eqIntent);
                                    }
                                    notifyDataSetChanged();
                                    Toast.makeText(getContext(),
                                            item.getName() + " - " + Language.getInstance().messageDeleted,
                                            Toast.LENGTH_SHORT).show();
                                    db.close();
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
            }
        });
        btnDelete.setImageResource(R.mipmap.remove_icon);
        btnDelete.setBackgroundColor(Color.TRANSPARENT);
        return convertView;
    }
    public void dismiss_dialog(){
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();
    }
}
