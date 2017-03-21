package com.sadwyn.iceandfire.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadwyn.iceandfire.R;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.TextViewHolder> {

    private List<String> data;

    public DetailsAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextViewHolder vh;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_detail, parent, false);
        vh = new TextViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
         holder.alias.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_text_item) TextView alias;

        public TextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
