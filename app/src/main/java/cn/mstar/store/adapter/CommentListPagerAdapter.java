package cn.mstar.store.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by UlrichAbiguime at Shenzhen.
 */
public class CommentListPagerAdapter extends FragmentPagerAdapter {
    private List<?> fragments;
    public CommentListPagerAdapter(FragmentManager fm, List<?> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
