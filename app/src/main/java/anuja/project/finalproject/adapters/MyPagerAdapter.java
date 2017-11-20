package anuja.project.finalproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by USER on 17-10-2017.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> FragmentList;
    private final List<String> FragmentTitle;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> FragmentList, List<String> FragmentTitle) {
        super(fm);
        this.FragmentList = FragmentList;
        this.FragmentTitle = FragmentTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        return FragmentTitle.get(position);
    }
}
