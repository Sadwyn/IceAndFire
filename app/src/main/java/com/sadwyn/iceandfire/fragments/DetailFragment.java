package com.sadwyn.iceandfire.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.DetailBackgroundView;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.presenters.DetailBackgroundPresenter;
import com.sadwyn.iceandfire.views.adapters.DetailsAdapter;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sadwyn.iceandfire.Constants.CHARACTER_KEY;

public class DetailFragment extends DialogFragment implements DetailBackgroundView {

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

    private Character character;
    public DetailBackgroundPresenter backgroundPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        backgroundPresenter =  new DetailBackgroundPresenter(getActivity().getApplicationContext(), this);
        if (getArguments() != null) {
            character = Parcels.unwrap(getArguments().getParcelable(CHARACTER_KEY));
        }
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
        backgroundPresenter.onViewCreated(view, savedInstanceState);
        if (character != null) {
            initializeData();
            CharacterModelImpl model = CharacterModelImpl.getInstance();
            if (getActivity() instanceof MainActivity)
                model.saveCharacterToDB(character, view.getContext());
        }
    }

    private void initializeData() {
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
