
package com.basic.code.fragment.components.tabbar.tablayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**

 * @since 2020/4/21 1:27 AM
 */
public class FragmentCacheAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();

    private List<String> mTitleList = new ArrayList<>();

    public FragmentCacheAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public FragmentCacheAdapter addFragment(Fragment fragment, String title) {
        if (fragment != null) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
        }
        return this;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    public List<Fragment> getFragmentList() {
        return mFragmentList;
    }

    public List<String> getTitleList() {
        return mTitleList;
    }

    public void clear() {
        mFragmentList.clear();
        mTitleList.clear();
        notifyDataSetChanged();
    }
}
