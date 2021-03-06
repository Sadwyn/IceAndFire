package com.sadwyn.iceandfire.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.views.adapters.ViewPagerAdapter;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sadwyn.iceandfire.Constants.CHARACTER_KEY;
import static com.sadwyn.iceandfire.Constants.LIST_KEY;


public class ViewPagerFragment extends DialogFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    List<Character> characterList;
    ViewPagerAdapter adapter;

    public static ViewPagerFragment newInstance(Character character, List<Character> characterList) {
        Bundle bundle = new Bundle();
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        bundle.putParcelable(LIST_KEY, Parcels.wrap(characterList));
        bundle.putParcelable(CHARACTER_KEY, Parcels.wrap(character));
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @OnClick(R.id.cancelButton)
    public void cancelButton(View view) {
        getDialog().dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_pager_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        Character character = Parcels.unwrap(getArguments().getParcelable(CHARACTER_KEY));
        characterList = Parcels.unwrap(getArguments().getParcelable(LIST_KEY));
        viewPager.setPageTransformer(false, new FlipHorizontalTransformer());
        adapter = new ViewPagerAdapter(getChildFragmentManager(), characterList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(characterList.indexOf(character));
    }
}
