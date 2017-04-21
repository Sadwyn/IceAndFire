package com.sadwyn.iceandfire.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.CharactersView;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.components.*;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModel;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.modules.CharactersPresenterModule;
import com.sadwyn.iceandfire.presenters.CharactersFragmentPresenter;
import com.sadwyn.iceandfire.views.adapters.CharactersAdapter;
import com.sadwyn.iceandfire.views.widgets.CharacterWidget;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sadwyn.iceandfire.Constants.DATA_SOURCE_PREF;
import static com.sadwyn.iceandfire.Constants.DEFAULT_SPAN_COUNT;

public class CharactersFragment extends Fragment implements CharactersView {

    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.adv_id)
    AdView adView;

    @Inject
    public CharactersFragmentPresenter presenter;

    private ContentFragmentCallback callback;
    private CharactersAdapter adapter;
    private RecyclerView.OnScrollListener supportListener;
    private CharacterModel model;
    CharactersPresenterComponent presenterComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (presenterComponent == null) {
            presenterComponent = DaggerCharactersPresenterComponent.builder()
                    .charactersPresenterModule(new CharactersPresenterModule(getContext(), this)).build();
        }
        presenterComponent.inject(this);
        model = CharacterModelImpl.getInstance();

    }

    public CharactersFragmentPresenter getPresenter() {
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.characters_fragment, container, false);
        ButterKnife.bind(this, view);
        if(getResources().getBoolean(R.bool.isDemoVersion)){
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("0EA782B39B80C8279038659FEEEEEEF4").build();
            adView.loadAd(adRequest);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            callback = (MainActivity) context;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.setError(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setError(true);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapter = new CharactersAdapter(presenter.getList(), callback);
        presenter.onViewCreated(view, savedInstanceState);

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
        boolean isDemoVersion = getResources().getBoolean(R.bool.isDemoVersion);
        if (!isDemoVersion && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), DEFAULT_SPAN_COUNT);

        }
        else layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if(!isRemoteStorage())
            addOnSwipeItemListener();
        recyclerView.setAdapter(adapter);

    }

    private void addOnSwipeItemListener() {
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            int dragFrom = -1;
            int dragTo = -1;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if(dragFrom == -1) {
                    dragFrom =  fromPosition;
                }
                dragTo = toPosition;
                List<Character> list = getPresenter().getList();
                list.add(toPosition, list.remove(fromPosition));
                adapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                LinearLayout layout = ((LinearLayout)(((CharactersAdapter.TextViewHolder)viewHolder).itemView));
                CardView cardView = (CardView)layout.getChildAt(0);
                TextView textView = (TextView) cardView.getChildAt(0);
                String name = textView.getText().toString();
                model.deleteCharacterBySwipe(getActivity().getApplicationContext(), name);
                presenter.getList().remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                CharacterWidget.updateWidget(getActivity().getApplicationContext());
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
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
        adView.destroy();
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

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
