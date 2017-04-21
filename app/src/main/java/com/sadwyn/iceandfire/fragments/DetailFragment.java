package com.sadwyn.iceandfire.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.DetailBackgroundView;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.components.DaggerDetailsPresenterComponent;
import com.sadwyn.iceandfire.components.DetailsPresenterComponent;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.modules.DetailsPresenterModule;
import com.sadwyn.iceandfire.presenters.DetailFragmentPresenter;
import com.sadwyn.iceandfire.views.adapters.DetailsAdapter;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sadwyn.iceandfire.Constants.CHARACTER_KEY;
import static com.sadwyn.iceandfire.Constants.LIST_KEY;

public class DetailFragment extends Fragment implements DetailBackgroundView {

    @BindView(R.id.detail_layout)
    PercentRelativeLayout detail_fragment_layout;
    @BindView(R.id.bornText)
    TextView bornText;
    @BindView(R.id.cultureText)
    TextView cultureText;
    @BindView(R.id.genderText)
    TextView genderText;
    @BindView(R.id.diedText)
    TextView diedText;
    @BindView(R.id.motherText)
    TextView motherText;
    @BindView(R.id.fatherText)
    TextView fatherText;
    @BindView(R.id.titles)
    RecyclerView recyclerView;
    @BindView(R.id.aliases)
    TextView aliases;

    DetailsPresenterComponent detailsPresenterComponent;
    @Inject
    public DetailFragmentPresenter detailFragmentPresenter;
    public ContentFragmentCallback contentFragmentCallback;
    Character character;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity)
            contentFragmentCallback = (ContentFragmentCallback) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (detailsPresenterComponent == null) {
            detailsPresenterComponent = DaggerDetailsPresenterComponent.builder()
                    .detailsPresenterModule(new DetailsPresenterModule(getContext(), this)).build();
        }
        detailsPresenterComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        detailFragmentPresenter.onViewCreated(view, savedInstanceState);
        character = Parcels.unwrap(getArguments().getParcelable(CHARACTER_KEY));
        initializeData(character);
        CharacterModelImpl model = CharacterModelImpl.getInstance();

        if (getActivity() instanceof MainActivity)
            model.saveCharacterToDB(character, view.getContext());
    }

    private void initializeData(Character character) {
        bornText.setText(getString(R.string.bornText, character.getBorn()));
        genderText.setText(getString(R.string.genderText, character.getGender()));
        cultureText.setText(getString(R.string.kingdomText, character.getCulture()));
        fatherText.setText(getString(R.string.fatherText, character.getFather()));
        motherText.setText(getString(R.string.motherText, character.getMother()));
        diedText.setText(getString(R.string.deadText, character.getDied()));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (!character.getAliases().isEmpty()) aliases.setText(R.string.aliases);
        DetailsAdapter adapter = new DetailsAdapter(character.getAliases());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        App.getRefWatcher(getContext()).watch(this);
    }

    public static DetailFragment newInstance(Character character) {
        Bundle bundle = new Bundle();
        DetailFragment detailFragment = new DetailFragment();
        bundle.putParcelable(CHARACTER_KEY, Parcels.wrap(character));
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public void onSetBackground(Drawable drawable) {
        detail_fragment_layout.setBackground(drawable);
    }
}
