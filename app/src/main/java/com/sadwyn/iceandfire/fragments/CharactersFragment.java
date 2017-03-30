package com.sadwyn.iceandfire.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sadwyn.iceandfire.CharacterView;
import com.sadwyn.iceandfire.models.CharacterModel;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.presenters.CharactersListPresenter;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.views.adapters.CharactersAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sadwyn.iceandfire.Constants.DATA_SOURCE_PREF;
import static com.sadwyn.iceandfire.Constants.DEFAULT_SPAN_COUNT;


public class CharactersFragment extends Fragment implements CharacterView {

    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    private ContentFragmentCallback callback;
    private CharactersAdapter adapter;
    private CharactersListPresenter presenter;
    private RecyclerView.OnScrollListener supportListener;
    CharacterModel model;

    public CharactersListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CharactersListPresenter(getActivity().getApplicationContext(), this);
        model = CharacterModelImpl.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.characters_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            callback = (MainActivity) context;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        adapter = new CharactersAdapter(presenter.getList(), callback);
        presenter.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

         if(isRemoteStorage()) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerView.setOnScrollChangeListener((view12, i, i1, i2, i3) -> {
                    if (CharactersFragment.this.isLastItemDisplaying(recyclerView)) {
                        presenter.addNewData();
                    }
                });
            }
            supportListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (isLastItemDisplaying(recyclerView))
                        presenter.addNewData();
                }
            };
            recyclerView.setOnScrollListener(supportListener);
        }
        else {
             adapter.setError(true);
         }
        RecyclerView.LayoutManager layoutManager;
        boolean isDemoVersion = !getResources().getBoolean(R.bool.isDemoVersion);
        layoutManager = isDemoVersion && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                new GridLayoutManager(getActivity().getApplicationContext(), DEFAULT_SPAN_COUNT) : new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        if(!isRemoteStorage())
        addOnSwipeItemListener();
    }

    private void addOnSwipeItemListener() {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                model.deleteCharacterBySwipe(getContext().getApplicationContext(), viewHolder.getAdapterPosition()+1);
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView);
    }

    private boolean isRemoteStorage() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString(DATA_SOURCE_PREF, "remote").equals("remote");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        recyclerView.removeOnScrollListener(supportListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    public static CharactersFragment newInstance() {
        return new CharactersFragment();
    }

    @Override
    public void showCharactersList(boolean isError) {
        adapter.setError(isError);
        adapter.notifyDataSetChanged();
    }


}
