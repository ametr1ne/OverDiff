package com.ametr1ne.overdiff.tape;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ametr1ne.overdiff.MainActivity;
import com.ametr1ne.overdiff.R;
import com.ametr1ne.overdiff.fragments.ArticleFragment;
import com.ametr1ne.overdiff.models.Article;
import com.ametr1ne.overdiff.utils.ImageLoadTask;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {

    private List<Article> articles;
    private MainActivity source;

    public ListAdapter(MainActivity source, List<Article> articles) {
        this.source = source;
        this.articles = articles;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_article, parent, false);
        return new ListViewHolder(view,articles,source);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    private static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static TextView mItemText;
        private static ImageView mItemImage;

        private List<Article> articles;
        private MainActivity source;

        private Article article;

        public ListViewHolder(View itemView,List<Article> articles,MainActivity source) {
            super(itemView);
            this.articles = articles;
            this.source = source;

            mItemText = (TextView) itemView.findViewById(R.id.title_article);
            mItemImage = (ImageView) itemView.findViewById(R.id.image_preview);
            itemView.setOnClickListener(this);
        }

        public void bindView (int position) {
            mItemText.setText(articles.get(position).getDescription());
            new ImageLoadTask(articles.get(position).getIcon()
                    , mItemImage).execute();

            article = articles.get(position);

        }

        public void onClick (View view) {
            source.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ArticleFragment(source,article.getHash())).commit();
        }

    }

}
