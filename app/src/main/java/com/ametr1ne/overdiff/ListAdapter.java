package com.ametr1ne.overdiff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_article, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return OurData.titles.length;
    }

    private static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static TextView mItemText;
        private static ImageView mItemImage;

        public ListViewHolder(View itemView) {
            super(itemView);

            mItemText = (TextView) itemView.findViewById(R.id.title_article);
            mItemImage = (ImageView) itemView.findViewById(R.id.image_preview);
            itemView.setOnClickListener(this);
        }

        public void bindView (int position) {
            mItemText.setText(OurData.titles[position]);
            mItemImage.setImageResource(OurData.imageId[position]);
        }

        public void onClick (View view) {
        }

    }

}
