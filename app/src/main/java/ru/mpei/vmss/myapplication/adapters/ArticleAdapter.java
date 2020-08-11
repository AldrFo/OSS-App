package ru.mpei.vmss.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Collection;
import java.util.List;

import ru.mpei.vmss.myapplication.R;
import ru.mpei.vmss.myapplication.pojo.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> elements;
    private LayoutInflater inflater;
    private Context context;
    private  OnArticleClickListener onArticleClickListener;

    public ArticleAdapter(Context context, List<Article> elements, OnArticleClickListener onArticleClickListener) {
        this.elements = elements;
        this.context = context;
        this.onArticleClickListener = onArticleClickListener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.article_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = elements.get(position);

        Glide.with(context)
                .load(article.getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail);

        holder.articleTitle.setText(article.getTitle());

    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView articleTitle;

        public ViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.articleElementImage);
            articleTitle = view.findViewById(R.id.articleElementText);
            view.setOnClickListener(v -> {
                Article article = elements.get(getLayoutPosition());
                onArticleClickListener.onClickListener(article);
            });
        }

    }

    public void setArticles(Collection<Article> articles){
        elements = (List<Article>) articles;
        notifyDataSetChanged();
    }

    public interface OnArticleClickListener {
        void onClickListener(Article article);
    }

}