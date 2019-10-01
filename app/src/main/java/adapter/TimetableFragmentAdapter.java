package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anamika.TimetableListFragment;

/**
 * Created by Rajesh on 2017-09-05.
 */

public class TimetableFragmentAdapter extends FragmentPagerAdapter {

    public TimetableFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        // return fragment and pass data
        switch (pos) {
            case 0:
                return TimetableListFragment.newInstance("1");
            case 1:
                return TimetableListFragment.newInstance("2");
            case 2:
                return TimetableListFragment.newInstance("3");
            case 3:
                return TimetableListFragment.newInstance("4");
            case 4:
                return TimetableListFragment.newInstance("5");
            case 5:
                return TimetableListFragment.newInstance("6");
            case 6:
                return TimetableListFragment.newInstance("7");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

}