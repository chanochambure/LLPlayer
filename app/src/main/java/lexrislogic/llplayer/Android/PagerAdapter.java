package lexrislogic.llplayer.Android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import lexrislogic.llplayer.TabFragment1;
import lexrislogic.llplayer.TabFragment2;
import lexrislogic.llplayer.TabFragment3;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    TabFragment1 tab1=new TabFragment1();
    TabFragment2 tab2=new TabFragment2();
    TabFragment3 tab3=new TabFragment3();

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}