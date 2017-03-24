package com.sadwyn.iceandfire.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.fragments.ContentFragmentCallback;
import com.sadwyn.iceandfire.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CharactersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM_KEY = 0;
    private final static int PROGRESS_KEY = 1;

    private List<Character> listOfCharacters;
    private ContentFragmentCallback callback;

    private boolean isError;

    public CharactersAdapter(List<Character> listOfCharacters, ContentFragmentCallback callback) {
        this.listOfCharacters = listOfCharacters;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if(viewType == ITEM_KEY ) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_characters, parent, false);
            vh = new TextViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item,parent,false);
            vh = new ProgressHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TextViewHolder){
            ((TextViewHolder)holder).name.setText(listOfCharacters.get(holder.getAdapterPosition()).getName());
        }
        else if(holder instanceof ProgressHolder){
            ((ProgressHolder)holder).progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position != listOfCharacters.size())
            return ITEM_KEY;
        else if (isError)
            return ITEM_KEY;
        else return PROGRESS_KEY;
    }

    @Override
    public int getItemCount() {
        return listOfCharacters.size();
    }

    public void setError(boolean error) {
        isError = error;
    }



     class ProgressHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.progressBar) ProgressBar progressBar;
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.character_text_item) TextView name;
        @Override
        public void onClick(View view) {
            callback.onItemClick(listOfCharacters.get(getAdapterPosition()));
        }
        TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
    }
}
