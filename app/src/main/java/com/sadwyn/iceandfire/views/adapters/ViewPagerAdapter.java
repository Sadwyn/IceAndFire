package com.sadwyn.iceandfire.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.models.Character;

import java.util.List;

/**
 * Created by Sadwyn on 21.04.2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Character> characters;

    public ViewPagerAdapter(FragmentManager fm, List<Character> characters) {
        super(fm);
        this.characters = characters;
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(characters.get(position));
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return characters.get(position).getName();
    }
}
