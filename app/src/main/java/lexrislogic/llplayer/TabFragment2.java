package lexrislogic.llplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import lexrislogic.llplayer.Android.DBHandler;
import lexrislogic.llplayer.Android.SimpleElementAdapter;
import lexrislogic.llplayer.Models.SimpleElement;
import lexrislogic.llplayer.Singleton.Language;
import lexrislogic.llplayer.Singleton.Var;

public class TabFragment2 extends Fragment {
    private final static int REQUEST_CODE_ARTIST_SEARCH=881;
    private final static int REQUEST_CODE_ALBUM_SEARCH=882;
    private final static int REQUEST_CODE_GENRE_SEARCH=883;
    public SimpleElementAdapter adapter=null;
    ArrayList<SimpleElement> element_list=null;
    public SimpleElement artist_search=new SimpleElement(
            Var.TYPE_BROWSE_ARTIST,
            R.mipmap.artist_icon,
            Language.getInstance().BrowseArtistText,
            Language.getInstance().BrowseArtistSubText,
            -1);
    public SimpleElement album_searh=new SimpleElement(
            Var.TYPE_BROWSE_ALBUM,
            R.mipmap.album_icon,
            Language.getInstance().BrowseAlbumText,
            Language.getInstance().BrowseAlbumSubText,
            -1);
    public SimpleElement genre_search=new SimpleElement(
            Var.TYPE_BROWSE_GENRE,
            R.mipmap.genre_icon,
            Language.getInstance().BrowseGenreText,
            Language.getInstance().BrowseGenreSubText,
            -1);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        if(view!=null) {
            element_list=new ArrayList<>();
            final DBHandler db=new DBHandler(getContext());
            view.setBackgroundColor(db.get_secondary_background_color());

            adapter=new SimpleElementAdapter(getContext(),element_list,
                    db.get_main_primary_text_color(),
                    db.get_main_secondary_text_color());

            ListView listView= (ListView) view.findViewById(R.id.browseListView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SimpleElement selected_item = (SimpleElement) adapterView.getItemAtPosition(i);
                    Intent intent;
                    switch (selected_item.get_type()){
                        case Var.TYPE_BROWSE_ARTIST:
                            intent = new Intent(getContext(), ArtistSearch.class);
                            startActivityForResult(intent, REQUEST_CODE_ARTIST_SEARCH);
                            break;
                        case Var.TYPE_BROWSE_ALBUM:
                            intent = new Intent(getContext(), AlbumSearch.class);
                            Var.getInstance().albumTypeList=false;
                            Var.getInstance().albumList.clear();
                            Var.getInstance().albumList.addAll(db.get_albums());
                            startActivityForResult(intent, REQUEST_CODE_ALBUM_SEARCH);
                            break;
                        case Var.TYPE_BROWSE_GENRE:
                            intent = new Intent(getContext(), GenreSearch.class);
                            startActivityForResult(intent, REQUEST_CODE_GENRE_SEARCH);
                            break;
                    }
                }
            });
        }
        load_element_list();
        return view;
    }
    public void load_element_list(){
        element_list.add(artist_search);
        element_list.add(album_searh);
        element_list.add(genre_search);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_CODE_ARTIST_SEARCH:
                break;
            case REQUEST_CODE_ALBUM_SEARCH:
                break;
            case REQUEST_CODE_GENRE_SEARCH:
                break;
        }
    }
}