package com.sadwyn.iceandfire.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.fragments.ContentFragmentCallback;
import com.sadwyn.iceandfire.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CharactersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final static int ITEM_KEY = 0;
    private final static int PROGRESS_KEY = 1;

    private List<Character> listOfCharacters;
    private List<Character> filteredCharacters;

    private ContentFragmentCallback callback;
    private boolean isError;
    private ItemFilter filter;

    public CharactersAdapter(List<Character> listOfCharacters, ContentFragmentCallback callback) {
        this.listOfCharacters = listOfCharacters;
        this.filteredCharacters = listOfCharacters;
        this.callback = callback;
        this.filter = new ItemFilter();
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
            ((TextViewHolder)holder).name.setText(filteredCharacters.get(holder.getAdapterPosition()).getName());
        }
        else if(holder instanceof ProgressHolder){
            ((ProgressHolder)holder).progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position != filteredCharacters.size() - 1 ? ITEM_KEY : isError ? ITEM_KEY : PROGRESS_KEY;
    }

    @Override
    public int getItemCount() {
        return filteredCharacters.size();
    }

    public void setError(boolean error) {
        isError = error;
    }




    public class ProgressHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.progressBar) ProgressBar progressBar;
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

   public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.character_text_item) TextView name;
        @Override
        public void onClick(View view) {
            callback.onItemClick(filteredCharacters.get(getAdapterPosition()));
        }
        TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ItemFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Character> list = listOfCharacters;
            int count = list.size();
            final ArrayList<Character> filteredList = new ArrayList<>(count);

            Character filterableCharacter ;

            for (int i = 0; i < count; i++) {
                filterableCharacter = listOfCharacters.get(i);
                if (filterableCharacter.getName().toLowerCase().contains(filterString)) {
                    filteredList.add(filterableCharacter);
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredCharacters = (ArrayList<Character>) results.values;
            notifyDataSetChanged();
        }
    }
}
