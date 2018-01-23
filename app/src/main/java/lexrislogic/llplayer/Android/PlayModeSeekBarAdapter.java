package lexrislogic.llplayer.Android;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import lexrislogic.llplayer.MediaPlayerService;
import lexrislogic.llplayer.Models.BandEqualizer;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.PlayerVar;

public class PlayModeSeekBarAdapter  extends ArrayAdapter<BandEqualizer> {
    private int text_color;
    private Context context=null;
    private void testEqualizer(){
        Intent eqIntent= new Intent(MediaPlayerService.MEDIA_PLAYER_ACTION);
        eqIntent.putExtra("MP-M-CODE", PlayerVar.MP_M_TEST_EQUALIZER);
        context.sendBroadcast(eqIntent);
    }
    public PlayModeSeekBarAdapter(Context context, ArrayList<BandEqualizer> data,int text_color) {
        super(context, 0, data);
        this.text_color=text_color;
        this.context=context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final BandEqualizer item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.seek_bar_band, parent, false);
        }
        final short lowerEqualizerBandLevel = PlayerVar.getInstance().mediaEqualizer.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = PlayerVar.getInstance().mediaEqualizer.getBandLevelRange()[1];
        TextView frequencyText = (TextView) convertView.findViewById(R.id.frequencyText);
        frequencyText.setTextColor(text_color);
        frequencyText.setText(String.valueOf(PlayerVar.getInstance().mediaEqualizer.getCenterFreq(item.getId()) / 1000) + " Hz");
        TextView lowerEqualizerText = (TextView) convertView.findViewById(R.id.LowerEqualizerText);
        lowerEqualizerText.setTextColor(text_color);
        lowerEqualizerText.setText(String.valueOf(lowerEqualizerBandLevel / 100) + " dB");
        TextView upperEqualizerText = (TextView) convertView.findViewById(R.id.UpperEqualizerText);
        upperEqualizerText.setTextColor(text_color);
        upperEqualizerText.setText(String.valueOf(upperEqualizerBandLevel / 100) + " dB");
        SeekBar seekBar=(SeekBar) convertView.findViewById(R.id.seekBar2);
        seekBar.setDrawingCacheBackgroundColor(text_color);
        seekBar.getProgressDrawable().setColorFilter(text_color, PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(text_color, PorterDuff.Mode.SRC_ATOP);
        seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
        seekBar.setProgress(item.getData() - lowerEqualizerBandLevel);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    item.setData((short) (progress + lowerEqualizerBandLevel));
                    short numberFrequencyBands = PlayerVar.getInstance().mediaEqualizer.getNumberOfBands();
                    String[] parts = PlayerVar.getInstance().testPlayMode.getData().split("o");
                    parts[item.getId()] = String.valueOf(item.getData());
                    String data = "";
                    for (short j = 0; j < numberFrequencyBands; j++) {
                        data += parts[j];
                        if (j < numberFrequencyBands - 1)
                            data += "o";
                    }
                    PlayerVar.getInstance().testPlayMode.setData(data);
                    testEqualizer();
                }
            }
        });
        seekBar.invalidate();
        return convertView;
    }
}

