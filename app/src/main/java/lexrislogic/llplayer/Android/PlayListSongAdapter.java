/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lexrislogic.llplayer.Android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import lexrislogic.llplayer.Models.Song;
import lexrislogic.llplayer.R;
import lexrislogic.llplayer.Singleton.Var;

public class PlayListSongAdapter extends ArrayAdapter<Song> {

    final int INVALID_ID = -1;
    private int main_color;
    private int secondary_color;
    HashMap<Song, Integer> mIdMap = new HashMap<>();

    public void refresh_hash(ArrayList<Song> array){
        mIdMap.clear();
        for (int i = 0; i < array.size(); ++i) {
            mIdMap.put(array.get(i), i);
        }
    }

    public PlayListSongAdapter(Context context, ArrayList<Song> objects,int main_color,int secondary_color) {
        super(context, 0, objects);
        refresh_hash(objects);
        this.main_color=main_color;
        this.secondary_color=secondary_color;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Song item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
        final Song item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_song_adapter, parent, false);
        }
        ImageButton delSong = (ImageButton) convertView.findViewById(R.id.delSong);
        delSong.setFocusable(false);
        delSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Var.getInstance().playListSongs.remove(position);
                refresh_hash(Var.getInstance().playListSongs);
                notifyDataSetChanged();
            }
        });
        delSong.setImageResource(R.mipmap.remove_icon);
        delSong.setBackgroundColor(Color.TRANSPARENT);
        TextView main_text = (TextView) convertView.findViewById(R.id.mainText);
        main_text.setText(item.getTitle());
        if(item.isValid()){
            main_text.setTextColor(main_color);
        }
        else{
            main_text.setTextColor(secondary_color);
        }
        TextView secondary_text = (TextView) convertView.findViewById(R.id.secondaryText);
        secondary_text.setText(item.getArtist()+"\n"+item.getAlbum());
        secondary_text.setTextColor(secondary_color);
        return convertView;
    }
}
